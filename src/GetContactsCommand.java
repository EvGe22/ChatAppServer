public class GetContactsCommand extends Command {

    String regex;

    public GetContactsCommand(){
        super(CommandType.GET_CONTACTS);
    }

    public GetContactsCommand(String regex) {
        super(CommandType.GET_CONTACTS);
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
