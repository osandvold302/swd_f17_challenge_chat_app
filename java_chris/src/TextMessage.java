public class TextMessage extends MessageToServer {
    private String user;
    private String message;
    public TextMessage(String channel, String user, String message){
        super(channel);
        this.user = user;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public String getMessage(){
        return message;
    }
}
