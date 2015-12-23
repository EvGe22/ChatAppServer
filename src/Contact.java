public class Contact {

    String nick;
    boolean isFav;

    public Contact() {

    }

    public Contact(String nick, String isFav) {
        this.nick = nick;
        this.isFav = Boolean.parseBoolean(isFav);
    }



    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean isFav) {
        this.isFav = isFav;
    }
}
