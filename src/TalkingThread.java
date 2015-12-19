public class TalkingThread extends Thread {

    String login;
    Connection connection;
    TalkingThread otherOne;
    Command lastCommand;
    boolean run=true;

    public TalkingThread(Connection connection){
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            while (run) {
                lastCommand = connection.recieve();
                switch (lastCommand.type) {
                    case LOGIN: {
                        LoginCommand loginCommand = (LoginCommand) lastCommand;
                        if (loginCommand.getPassHash().equals(Main.logins.get(loginCommand.getLogin()))) {
                            connection.accept();
                            Main.temporary.remove(this);
                            login = loginCommand.getLogin();
                            Main.onlineUsers.put(login, this);
                            System.out.println(login + "logged in");
                        } else {
                            connection.reject();
                        }
                        break;
                    }
                    case SIGNUP: {
                        SignUpCommand signUpCommand = (SignUpCommand) lastCommand;
                        if (Main.logins.containsKey(signUpCommand.getLogin())) {
                            connection.reject();
                            break;
                        }
                        connection.accept();
                        Main.logins.put(signUpCommand.getLogin(), signUpCommand.getPassHash());
                        Main.temporary.remove(this);
                        login = signUpCommand.getLogin();
                        Main.onlineUsers.put(login, this);
                        System.out.println(login + "signed up");

                        break;
                    }
                    case DISCONNECT: {
                        connection.disconnect();
                        run = false;
                        Main.onlineUsers.remove(login);
                        System.out.println(login + " disconnected");
                        break;
                    }
                }

            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
            run=false;
        }
    }
}
