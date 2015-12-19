public class SignUpCommand extends Command{

    String login,passHash;

    public SignUpCommand(){
        super(CommandType.SIGNUP);
    }

    public SignUpCommand(String login,String passHash){
        super(CommandType.SIGNUP);
        this.login=login;
        this.passHash=passHash;
    }

    public String getLogin() {
        return login;
    }

    public String getPassHash() {
        return passHash;
    }
}