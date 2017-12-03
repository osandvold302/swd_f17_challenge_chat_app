public class MessageToClient extends Message {
    private String channel;
    private String text;
    public MessageToClient(String channel, String text){
        this.channel = channel;
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public String getText(){
        return text;
    }
}
