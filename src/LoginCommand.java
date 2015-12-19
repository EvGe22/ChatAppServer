public class LoginCommand extends Command{

    String login,passHash;

    public LoginCommand(){
        super(CommandType.LOGIN);
    }

    public LoginCommand(String login,String passHash){
        super(CommandType.LOGIN);
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
