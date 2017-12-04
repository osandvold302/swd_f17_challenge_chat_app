/**
 * Class contains the message and the channel the
 * message is sent to
 */
public class MessageToClient extends Message
{
    /**
     *The channel the message is being sent to
     */
    private String channel;

    /**
     * The message that is being sent
     */
    private String text;

    /**
     * Sets the message and the channel the message
     * will be sent to
     * @param channel Channel the message will be sent to
     * @param text The message that will be sent
     */
    public MessageToClient(String channel, String text){
        this.channel = channel;
        this.text = text;
    }

    /**
     * Getter for channel
     * @return channel name as string
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Getter for text in text messages
     * @return text from a text message as String
     */
    public String getText(){
        return text;
    }
}
