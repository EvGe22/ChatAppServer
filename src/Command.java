import java.util.Scanner;

public class Command {
    protected CommandType type ;

    public Command(){}

    public Command(CommandType type){
        this.type=type;
    }

    public static Command getCommand(Scanner in){
        String string ="";
        if (in.hasNextLine()) {
            string = in.nextLine();
        }
        else{
            return null;
        }
        if (string.contains(Protocol.ACCEPTED)) return new Command(CommandType.ACCEPT);
        if (string.contains(Protocol.DISCONNECT)) return new Command(CommandType.DISCONNECT);
        if (string.contains(Protocol.REJECTED)) return new Command(CommandType.REJECT);
        if (string.contains(Protocol.MESSAGE)) return new MessageCommand((in.nextLine()));
        if (string.contains(Protocol.HELLO_SERVER)) return new Command(CommandType.HELLO_SERVER);
        if (string.contains(Protocol.SIGNUP)){
            string = string.replace(Protocol.SIGNUP,"");
            String [] tmp = string.split(" ");
            return new SignUpCommand(tmp[0],tmp[1]);
        }
        return null;
    }

}