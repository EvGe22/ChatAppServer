import java.io.IOException;
import java.net.ServerSocket;

public class IncomingCallListener {

    private ServerSocket serverSocket;
    private Command lastCommand;


    public IncomingCallListener(){
        try {
            serverSocket = new ServerSocket(Protocol.PORT_NUMBER);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = new Connection(serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.sendClientHello();
        lastCommand = connection.recieve();
        if (lastCommand.type==CommandType.HELLO_SERVER) return connection;
        else return null;
    }


}
