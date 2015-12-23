import java.util.ArrayList;

/**
 * Created by EvGe22 PC on 24.12.2015.
 */
public class OnlineCommand extends Command {

    ArrayList<String> arrayList;

    public OnlineCommand() {
        super(CommandType.ONLINE_CONTACTS);
    }

    public OnlineCommand(ArrayList<String> onlines) {
        super(CommandType.ONLINE_CONTACTS);
        arrayList = onlines;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }
}