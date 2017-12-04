/**
 * creates request for message logs from
 * specific channel
 */
public class RequestFulfillment extends MessageToClient
{
    /**
     * constructs a request for message log
     * @param channel the channel the message logs are
     *                requested for
     * @param messageLog string of messages from the channel
     */
    public RequestFulfillment(String channel, String messageLog)
    {
        super(channel,messageLog);
    }
}
