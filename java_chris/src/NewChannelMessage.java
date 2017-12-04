/**
 * new channel with message to send to users
 * connected to the channel
 */
public class NewChannelMessage extends MessageToServer
{
    /**
     * array of users that the message will be sent to
     */
    private String[] users;

    /**
     * constructs a new channel with message
     * and users connected to channel
     * @param channel channel the message is being sent to
     * @param users users the message is being sent to
     */
    public NewChannelMessage(String channel, String[] users){
        super(channel);
        this.users = users;
    }

    /**
     * Getter for array of users
     * @return array of users
     */
    public String[] getUsers(){
        return users;
    }
}
