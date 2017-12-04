/**
 * class for requesting a message
 */
public class RequestMessage extends MessageToServer
{
    /**
     * constructs message request
     * @param channel channel to request message from
     */
    public RequestMessage(String channel){
        super(channel);
    }
}
