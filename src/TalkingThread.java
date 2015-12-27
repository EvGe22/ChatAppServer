import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TalkingThread extends Thread {

    String login;
    Connection connection;
    TalkingThread otherOne;
    Command lastCommand;
    boolean run=true, isBusy=false;


    public TalkingThread(Connection connection){
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            while (run) {
                lastCommand = connection.recieve();
                if (lastCommand==null) continue;
                switch (lastCommand.type) {
                    case LOGIN: {
                        LoginCommand loginCommand = (LoginCommand) lastCommand;
                        if (!Main.onlineUsers.containsKey(loginCommand.getLogin()) && loginCommand.getPassHash().equals(Main.logins.get(loginCommand.getLogin()))) {
                            connection.accept();
                            Main.temporary.remove(this);
                            login = loginCommand.getLogin();
                            Main.onlineUsers.put(login, this);
                            System.out.println(login + " logged in");
                        }
                        else {
                            connection.reject();

                        }
                        break;
                    }
                    case SIGNUP: {
                        SignUpCommand signUpCommand = (SignUpCommand) lastCommand;
                        if (Main.logins.containsKey(signUpCommand.getLogin())) {
                            connection.reject();
                            break;
                        }
                        connection.accept();
                        Main.logins.put(signUpCommand.getLogin(), signUpCommand.getPassHash());
                        Main.temporary.remove(this);
                        login = signUpCommand.getLogin();
                        Main.onlineUsers.put(login, this);


                        break;
                    }
                    case DISCONNECT: {
                        System.out.println("got disconnect");
                        connection.disconnect();
                        run = false;
                        if (otherOne!=null) otherOne.disconnectFromUser();
                        if (Main.onlineUsers.containsKey(login)) Main.onlineUsers.remove(login);
                        if (Main.temporary.contains(this)) Main.temporary.remove(this);
                        System.out.println(login + " disconnected");
                        break;
                    }
                    case LOGOUT:{
                        System.out.println(login + " logged out");
                        Main.onlineUsers.remove(login);
                        Main.temporary.add(this);
                        if (otherOne!=null) {
                            otherOne.disconnectFromUser();
                            otherOne = null;
                        }
                        login=null;
                        connection.logOut();
                        break;
                    }
                    case GET_CONTACTS:{
                        new Thread() {
                            @Override
                            public void run() {
                                GetContactsCommand command = (GetContactsCommand) lastCommand;
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Map.Entry<String, String> entry : Main.logins.entrySet())
                                {
                                    if (entry.getKey().toLowerCase().contains(command.getRegex().toLowerCase()) && !entry.getKey().equalsIgnoreCase(login)) stringBuilder.append(" ")
                                            .append(entry.getKey()).append(" ").append(Main.onlineUsers.containsKey(entry.getKey()));

                                }

                                if (!stringBuilder.toString().equals(""))
                                connection.sendContacts(stringBuilder.toString());
                                else connection.sendEmptyContacts();
                            }

                        }.start();
                        break;
                    }
                    case GET_MY_CONTACTS:{
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Scanner in = new Scanner(new FileReader(login + "Contacts.txt"));
                                    StringBuilder stringBuilder = new StringBuilder();
                                    while (in.hasNextLine()) {
                                        String tmp = in.nextLine();
                                        String[] tmpArr = tmp.split(" ");
                                        stringBuilder.append(" ").append(tmp).append(" ").append(Main.onlineUsers.containsKey(tmpArr[0]));
                                    }
                                    connection.sendMyContacts(stringBuilder.toString());
                                    in.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    connection.sendEmptyMyContacts();
                                }
                            }
                        }.start();
                        break;
                    }
                    case CONTACTS:{
                                try {
                                    ContactsCommand contactsCommand = (ContactsCommand) lastCommand;
                                    if (contactsCommand.getArrayList()==null) return;
                                    File file = new File(login + "Contacts.txt");
                                    System.out.println( Files.deleteIfExists(file.toPath()));
                                    System.out.println(file.exists());
                                    System.out.println(file.createNewFile());
                                    FileWriter out = new FileWriter(file);
                                    for (Contact contact : contactsCommand.getArrayList()) {
                                        out.write(new StringBuilder(contact.getNick()).append(" ").append(contact.isFav()).append("\n").toString());
                                    }
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        break;
                    }
                    case DISCONNECT_FROM_USER:{

                        otherOne.disconnectFromUser();
                        otherOne=null;
                        break;
                    }
                    case CALL:{

                        CallCommand callCommand = (CallCommand) lastCommand;
                        if (Main.onlineUsers.containsKey(callCommand.getNick())){
                            otherOne = Main.onlineUsers.get(callCommand.getNick());
                            if (otherOne.sendCall(this)) {
                                isBusy = true;
                            }
                            else{
                                otherOne = null;
                                connection.sendBusy();
                            }
                        }
                        else{
                            connection.sendOffline();
                        }
                        break;
                    }
                    case ACCEPT:{
                        otherOne.sendAccept();
                        break;
                    }
                    case REJECT:{

                        otherOne.sendReject();
                        isBusy=false;
                        otherOne=null;
                        break;
                    }
                    case MESSAGE:{
                        if (otherOne==null) disconnectFromUser();
                        else {

                            otherOne.sendMessage(((MessageCommand) lastCommand).getMessage());

                        }
                        break;
                    }
                    case ONLINE_CONTACTS:{
                        new Thread(){
                            @Override
                            public void run() {
                                OnlineCommand onlineCommand = (OnlineCommand) lastCommand;
                                StringBuilder stringBuilder = new StringBuilder();
                                for (String s : onlineCommand.getArrayList()){
                                    stringBuilder.append(" ").append(Main.onlineUsers.containsKey(s));
                                }
                                connection.sendOnlineContacts(stringBuilder.toString());
                            }
                        }.start();
                    }

                }

            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
            if (otherOne!=null){
                otherOne.disconnectFromUser();
            }
            run=false;
        }
    }

    private void sendMessage(String message) {
        connection.sendMessage(message);

    }

    public boolean sendCall(TalkingThread otherOne){
        if (isBusy) return false;
        this.otherOne = otherOne;
        connection.sendCall(this.otherOne.getLogin());
        return true;
    }

    public String getLogin() {
        return login;
    }

    public void sendAccept(){
        connection.accept();
    }

    public void sendReject(){

        connection.reject();
        otherOne = null;
    }

    public void disconnectFromUser(){

        connection.disconnectFromUser();
        otherOne = null;
    }


}
