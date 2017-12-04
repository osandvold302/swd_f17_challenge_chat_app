/** the new channel message a client can send to the server*/
public class NewChannelMessage extends MessageToServer {
    /** the users in the new channel*/
    private String[] users;

    /** default constructor
     * @param channel to create
     * @param users to add into the channel
     */
    public NewChannelMessage(String channel, String[] users){
        super(channel);
        this.users = users;
    }

    /** getter for users in channel
     * @return the array of all the channels
     */
    public String[] getUsers(){
        return users;
    }
}
