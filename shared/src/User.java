import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class User {
    /** this is a stream that the user will write to when they send messages to a channel*/
    private ObjectInputStream input;
    /** this is a stream that the user will read from to get messages from their channel*/
    private ObjectOutputStream output;
    /** this is the socket the client connects to in order to interact with the server */
    private Socket client;
    /** this is the channel the user is currently on*/
    private String currentChannel;
    /** this is the array list that stores all the channels the user belongs to */
    private final ArrayList<String> channels;
    /** this is the user's unique ID*/
    private final String ID;

    /** default constructor for User object
     * @param ID unique identifier
     */
    public User(String ID){
        String[] channelList = null;
        try{
            // local host - where the server is already running
            client = new Socket(InetAddress.getByName("127.0.0.1"), 23555);
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(ID);
            output.flush(); // send the user ID to the server
            input = new ObjectInputStream(client.getInputStream());
            channelList = (String[])input.readObject(); // get channels from server
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        channels = new ArrayList<>();   // give memory to a new channel list
        if(channelList!=null){  // add channels from server to arrayList
            for(int i=0; i<channelList.length; i++){
                channels.add(channelList[i]);
            }
        }
        this.ID = ID;   // set ID
    }

    /** send a message from the current channel
     * @param message what the user is trying to send
     * @return whether they were able to send the message (true if sent, false if not)
     */
    public boolean sendMessage(String message){
        try {
            output.writeObject(new TextMessage(currentChannel,ID,message));
            output.flush();
            return true;
        }catch(IOException e){
            return false;
        }
    }

    /** Send a message to create a new channel with the list of users specified
     * @param channel what they want to create
     * @param users who they want to add
     * @return whether they were successful in adding a channel
     */
    public void newChannel(String channel,String[] users){
        try {
            output.writeObject(new NewChannelMessage(channel, users));
            output.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /** Sees if user already is a part of a channel with this name
     * @param channel to check if they are a part of
     * @return whether they belong to that channel or not
     */
    public boolean channelExists(String channel){
        for(String chan:channels){
            if(chan.equals(channel)){
                return true;
            }
        }
        return false;
    }

    /** Send a message to request the history of a channel
     * @param channel to get all the messages from
     */
    public void requestMessageHistory(String channel){
        try {
            output.writeObject(new RequestMessage(channel));
            output.flush();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /** Changes channel to specified channel and requests the message history from the server.
     * @param channel to set the channel to
     */
    public void changeChannel(String channel){
        currentChannel = channel;
        requestMessageHistory(channel);
    }

    /** function to get messages from server
     * @return the message sent from server
     */
    public String receiveMessage(){
        try {
            //Get information from the server and find out what group and user it is from
            Message message = (Message) input.readObject();
            if(message instanceof RequestFulfillment){
                String channel = ((RequestFulfillment) message).getChannel();
                if(channelExists(channel)){
                    return "REQUEST:"+((RequestFulfillment)message).getText();
                } else{
                    channels.add(channel);
                    return "NEWCHANNEL:"+((RequestFulfillment) message).getText();
                }

            } else if(message instanceof TextMessage){
                String channel = ((TextMessage) message).getChannel();
                if(channel.equals(currentChannel)){
                    return ((TextMessage) message).getUser()+" >> "+((TextMessage)message).getMessage();
                }
                if(!channelExists(channel)){
                    return "Channel already exists!";
                }
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return null;
    }

    /** getter for the channels the user is a part of
     * @return the arraylist of channels the user is a part of
     */
    public ArrayList<String> getChannels(){
        return channels;
    }

    /** this function closes all the streams */
    public void close(){
        try {
            output.writeObject(new TextMessage("","","TERMINATE"));
            output.flush();
            output.close();
            input.close();
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /** method to get the current channel user is in
     * @return the string of the channel name
     */
    public String getCurrentChannel() {
        return currentChannel;
    }
}