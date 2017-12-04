/** the message to send to the client from the server*/
public class MessageToClient extends Message {
    /** the channel from the client*/
    private String channel;
    /** the message to send from the server*/
    private String text;

    /** default constructor
     * @param channel the message is from
     * @param text what the message is
     */
    public MessageToClient(String channel, String text){
        this.channel = channel;
        this.text = text;
    }

    /** getter for channel
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /** getter for message
     * @return the message
     */
    public String getText(){
        return text;
    }
}
