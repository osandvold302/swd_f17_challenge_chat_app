/** this class extends Message so the client can send specialize messages to the server*/
public class MessageToServer extends Message {
    /** the channel the message should go to*/
    private String channel;

    /** constructor for messages
     * @param channel which message to send to the server
     */
    public MessageToServer(String channel){
        this.channel = channel;
    }

    /** returns the channel the message sent in
     * @return the channel
     */
    public String getChannel(){
        return channel;
    }
}
