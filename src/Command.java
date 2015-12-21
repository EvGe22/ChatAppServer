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
        if (string.contains(Protocol.LOGOUT)) return new Command(CommandType.LOGOUT);
        if (string.contains(Protocol.LOGIN)){
            string = string.replace(Protocol.LOGIN,"");
            String [] tmp = string.split(" ");
            return new LoginCommand(tmp[0],tmp[1]);
        }
        if (string.contains(Protocol.CONTACTS)){
            ArrayList<Contact> arrayList= new ArrayList<Contact>();
            String tmp = string.replace(Protocol.CONTACTS+" ","");
            String[] tmpArr = tmp.split(" ");
            for (int i=0;i<tmpArr.length;i+=2) {
                Contact contact = new Contact();
                contact.setNick(tmpArr[i]);
                if (tmpArr[i+1].equals("true")) contact.setFav(true);
                else contact.setFav(false);
                arrayList.add(contact);
            }
            return new ContactsCommand(arrayList);
        }
        if (string.contains(Protocol.GET_MY_CONTACTS)) return new Command(CommandType.GET_MY_CONTACTS);

        if (string.contains(Protocol.GET_CONTACTS)) return new Command(CommandType.GET_CONTACTS);
        return null;
    }

}