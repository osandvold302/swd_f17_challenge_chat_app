/**
 * message sent to specific channel by a user
 */
public class TextMessage extends MessageToServer
{
    /**
     * user that wrote the message
     */
    private String user;

    /**
     * words in message
     */
    private String message;

    /**
     * ties the user, text, and channel to
     * a message
     * @param channel the channel the user is on
     * @param user the person sending the message
     * @param message the text being sent
     */
    public TextMessage(String channel, String user, String message)
    {
        super(channel);
        this.user = user;
        this.message = message;
    }

    /**
     * Getter for user
     * @return user that sent the message
     */
    public String getUser() {
        return user;
    }


    /**
     * Getter for message
     * @return text in the message
     */
    public String getMessage(){
        return message;
    }
}
