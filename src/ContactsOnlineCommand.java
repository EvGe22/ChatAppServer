import java.util.ArrayList;
import java.util.Collection;

public class ContactsOnlineCommand extends Command{

    private ArrayList<String> contacts = new ArrayList<String>();

    public ContactsOnlineCommand(){
        super(CommandType.CONTACTS);
    }

    public ContactsOnlineCommand(Collection<String> tmp){

    }

}
