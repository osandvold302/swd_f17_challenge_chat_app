public class MessageToServer extends Message {
    private String channel;
    public MessageToServer(String channel){
        this.channel = channel;
    }
    public String getChannel(){
        return channel;
    }
}
