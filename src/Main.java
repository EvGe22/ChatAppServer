import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static HashMap<String,String> logins = new HashMap<String, String>();
    public static HashMap<String,TalkingThread> onlineUsers = new HashMap<String, TalkingThread>();
    public static ArrayList<TalkingThread> temporary = new ArrayList<TalkingThread>();
    IncomingCallListenerThread incomingThread = new IncomingCallListenerThread();

    public Main(){
        readLoginsFromFile();
        Thread listener = new Thread(incomingThread);
        listener.setDaemon(true);
        listener.start();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("1.Registered users\n2.Online users\n3.Shut down");
            switch (in.nextInt()){
                case 1:{

                    break;
                }
                case 2:{

                    break;
                }
                case 3:{
                    writeLoginsToFile();
                    return;
                }
            }
        }
    }

    public void writeLoginsToFile() {
        FileWriter out = null;
        try {
            out = new FileWriter("logins.txt");
            Set loginsArr = logins.entrySet();
            for (Map.Entry<String, String> entry: logins.entrySet()){
                out.write(new StringBuilder(entry.getKey()).append(" ").append(entry.getValue()).append("\n").toString());
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLoginsFromFile(){
        Scanner in = null;
        try{
            in = new Scanner(new FileReader("logins.txt"));
            String[] tmp;
            while (in.hasNextLine()){
                tmp = in.nextLine().split(" ");
                logins.put(tmp[0],tmp[1]);
            }
            in.close();
        } catch (IOException e){
            System.out.println("Failed to find a file Contacts.txt");
        }
    }



    private class IncomingCallListenerThread implements Runnable {

        IncomingCallListener incomingCallListener = new IncomingCallListener();
        Connection connection = null;

        @Override
        public void run() {
            while (true) {
                connection = incomingCallListener.getConnection();
                if (connection!=null){
                    TalkingThread tmp = new TalkingThread(connection);
                    tmp.start();
                    temporary.add(tmp);
                }
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
