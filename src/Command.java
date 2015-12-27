import java.util.ArrayList;
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
        if (string.startsWith(Protocol.ACCEPTED)) return new Command(CommandType.ACCEPT);
        if (string.startsWith(Protocol.DISCONNECT)) return new Command(CommandType.DISCONNECT);
        if (string.startsWith(Protocol.REJECTED)) return new Command(CommandType.REJECT);
        if (string.startsWith(Protocol.MESSAGE)) return new MessageCommand((in.nextLine()));
        if (string.startsWith(Protocol.HELLO_SERVER)) return new Command(CommandType.HELLO_SERVER);
        if (string.startsWith(Protocol.SIGNUP)){
            string = string.replace(Protocol.SIGNUP,"");
            String [] tmp = string.split(" ");
            return new SignUpCommand(tmp[0],tmp[1]);
        }
        if (string.startsWith(Protocol.LOGOUT)) return new Command(CommandType.LOGOUT);
        if (string.startsWith(Protocol.LOGIN)){
            string = string.replace(Protocol.LOGIN,"");
            String [] tmp = string.split(" ");
            return new LoginCommand(tmp[0],tmp[1]);
        }
        if (string.startsWith(Protocol.CONTACTS)){
            System.out.println("Got contacts" + string);
            ArrayList<Contact> arrayList= new ArrayList<Contact>();
            String tmp = string.replace(Protocol.CONTACTS+" ","");
            String[] tmpArr = tmp.split(" ");
            if (tmpArr.length<=1) return null;
            for (int i=0;i<tmpArr.length;i+=2) {
                arrayList.add(new Contact(tmpArr[i],tmpArr[i+1]));
            }
            return new ContactsCommand(arrayList);
        }
        if (string.startsWith(Protocol.GET_MY_CONTACTS)) return new Command(CommandType.GET_MY_CONTACTS);

        if (string.startsWith(Protocol.GET_CONTACTS)) return new GetContactsCommand(string.replace(Protocol.GET_CONTACTS,""));

        if (string.startsWith(Protocol.CALL)) return new CallCommand(string.replace(Protocol.CALL+" ",""));

        if (string.startsWith(Protocol.DISCONNECT_FROM_USER)) return new Command(CommandType.DISCONNECT_FROM_USER);

        if (string.startsWith(Protocol.ONLINE_CONTACTS)){
            string = string.replace(Protocol.ONLINE_CONTACTS+" ","");
            ArrayList<String > tmp = new ArrayList<String>();
            for (String s : string.split(" ")){
                tmp.add(s);
            }
            return new OnlineCommand(tmp);
        }
        return null;
    }

}