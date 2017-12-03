public class NewChannelMessage extends MessageToServer {
    private String[] users;
    public NewChannelMessage(String channel, String[] users){
        super(channel);
        this.users = users;
    }

    public String[] getUsers(){
        return users;
    }
}
