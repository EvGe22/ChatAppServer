import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static HashMap<String,String> logins = new HashMap<String, String>();
    public static HashMap<String,TalkingThread> onlineUsers = new HashMap<String, TalkingThread>();
    public static ArrayList<TalkingThread> temporary = new ArrayList<TalkingThread>();
    IncomingCallListenerThread incomingThread = new IncomingCallListenerThread();

    public Main(){
        Thread listener = new Thread(incomingThread);
        //listener.setDaemon(true);
        listener.start();
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
