import java.util.Map;
import java.util.Set;

public class TalkingThread extends Thread {

    String login;
    Connection connection;
    TalkingThread otherOne;
    Command lastCommand;
    boolean run=true;

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
                        if (loginCommand.getPassHash().equals(Main.logins.get(loginCommand.getLogin()))) {
                            connection.accept();
                            Main.temporary.remove(this);
                            login = loginCommand.getLogin();
                            Main.onlineUsers.put(login, this);
                            System.out.println(login + " logged in");
                        } else {
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
                        System.out.println(login + " signed up");

                        break;
                    }
                    case DISCONNECT: {
                        connection.disconnect();
                        run = false;
                        // Disconnecting the other user, if he exists
                        if (Main.onlineUsers.containsKey(login)) Main.onlineUsers.remove(login);
                        if (Main.temporary.contains(this)) Main.temporary.remove(this);
                        System.out.println(login + " disconnected");
                        break;
                    }
                    case LOGOUT:{
                        System.out.println(login + " logged out");
                        Main.onlineUsers.remove(login);
                        Main.temporary.add(this);
                        // Disconnecting the other user, if he exists
                        login=null;
                        break;
                    }
                    case GET_CONTACTS:{
                        StringBuilder stringBuilder = new StringBuilder();
                        int i;
                        for(Map.Entry<String, String> entry: Main.logins.entrySet()){
                            stringBuilder.append(" ").append(entry.getKey()).append(" ").append(Main.onlineUsers.containsKey(entry.getKey())).append(" ").append(false);
                        }
                        System.out.println(stringBuilder.toString());
                        connection.sendContacts(stringBuilder.toString());


                    }
                }

            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
            run=false;
        }
    }
}
