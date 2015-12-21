import java.util.ArrayList;
import java.util.Collection;

public class ContactsOnlineCommand extends Command{

    private ArrayList<String> contacts = new ArrayList<String>();

    public ContactsOnlineCommand(){
        super(CommandType.ONLINE_CONTACTS);
    }

    public ContactsOnlineCommand(Collection<String> tmp){
        super(CommandType.ONLINE_CONTACTS);
        contacts = (ArrayList<String>) tmp;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }
}
