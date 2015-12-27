import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class Connection{
    private Socket socket;
    private Scanner in;
    private DataOutputStream out;
    private CommandType lastCommand;

    public Connection(Socket socket){
        this.socket=socket;
        try {
            in = new Scanner(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            //ошибка при попытке создать Connection, как-то показать
        }
    }

    public void sendClientHello(){
        try {
            out.write(new StringBuilder(Protocol.HELLO_CLIENT).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.HELLO_CLIENT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCall(String nick){
        try {
            out.write(new StringBuilder(Protocol.CALL).append(" ").append(nick).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.CALL;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accept(){
        try {
            out.write(new StringBuilder(Protocol.ACCEPTED).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.ACCEPT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reject(){
        try {
            out.write(new StringBuilder(Protocol.REJECTED).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.REJECT;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            out.write(new StringBuilder(Protocol.MESSAGE).append("\n").append(message).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.MESSAGE;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            out.write(new StringBuilder(Protocol.DISCONNECT).append("\n").toString().getBytes("UTF-8"));
            socket.close();
            in.close();
            out.close();
            lastCommand=CommandType.DISCONNECT;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disconnectFromUser(){
        try{
            out.write(new StringBuilder(Protocol.DISCONNECT_FROM_USER).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.DISCONNECT_FROM_USER;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendOffline(){
        try{
            out.write(new StringBuilder(Protocol.OFFLINE).append("\n").toString().getBytes("UTF-8"));
            lastCommand=CommandType.OFFLINE;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendContacts(String contacts){
        try {
            out.write(new StringBuilder(Protocol.CONTACTS).append(contacts).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.CONTACTS;
    }

    public void sendEmptyContacts() {
        try {
            out.write(new StringBuilder(Protocol.EMPTYCONTACTS).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.EMPTYCONTACTS;
    }

    public void sendMyContacts(String contacts) {
        try {
            out.write(new StringBuilder(Protocol.MY_CONTACTS).append(contacts).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.MY_CONTACTS;
    }

    public void sendEmptyMyContacts() {
        try {
            out.write(new StringBuilder(Protocol.EMPTYMYCONTACTS).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.EMPTYMYCONTACTS;
    }

    public void sendBusy() {
        try {
            out.write(new StringBuilder(Protocol.BUSY).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.BUSY;
    }

    public void sendOnlineContacts(String s) {
        try {
            out.write(new StringBuilder(Protocol.ONLINE_CONTACTS).append(s).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.ONLINE_CONTACTS;
    }

    public void logOut() {
        try {
            out.write(new StringBuilder(Protocol.LOGOUT).append("\n").toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastCommand=CommandType.LOGOUT;
    }

    @Override
    public boolean equals(Object obj) {
        return socket.equals(((Connection) obj).socket);
    }

    public Command recieve(){
        return  Command.getCommand(in);
    }

}
