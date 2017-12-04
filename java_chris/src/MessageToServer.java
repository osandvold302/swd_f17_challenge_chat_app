/**
 * Class contains message that is being sent to
 * the server that will be sent to a specific channel
 */
public class MessageToServer extends Message {

    /**
     * Channel that receives the message
     */
    private String channel;

    /**
     * Setter for channel
     * @param channel
     */
    public MessageToServer(String channel){
        this.channel = channel;
    }

    /**
     * Getter for the channel
     * @return chanel message is being sent to
     */
    public String getChannel(){
        return channel;
    }
}
