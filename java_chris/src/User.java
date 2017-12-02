import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

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
        try{
            client = new Socket(InetAddress.getByName("127.0.0.1"), 23555);
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(ID);
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        channels = new ArrayList<>();
        this.ID = ID;
    }

    /** send a message from the current channel
     * @param message what the user is trying to send
     * @return whether they were able to send the message (true if sent, false if not)
     */
    public boolean sendMessage(String message){
        try {
            output.writeObject(currentChannel+","+ID+","+message);
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
    public boolean newChannel(String channel,String[] users){
        if(channelExists(channel)){
            return false;
        }else {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(channel);
                for (String user : users) {
                    builder.append("," + user);
                }
                output.writeObject(builder.toString());
                output.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return true;
        }
    }

    //Sees if user already is a part of a channel with this name
    public boolean channelExists(String channel){
        for(String chan:channels){
            if(chan.equals(channel)){
                return true;
            }
        }
        return false;
    }

    //Send a message to request the history of a channel
    public void requestMessageHistory(String channel){
        try {
            output.writeObject(channel + "," + "REQUEST" + "," + "REQUESTOLDMESSAGES");
            output.flush();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    //Changes channel to specified channel and requests the message history from the server.
    public void changeChannel(String channel){
        currentChannel = channel;
        requestMessageHistory(channel);
    }

    public String receiveMessage(){
        try {
            //Get information from the server and find out what group and user it is from
            String information = (String) input.readObject();
            Scanner splitter = new Scanner(information);
            splitter.useDelimiter(",");
            String channel = splitter.next();
            String user = splitter.next();
            String message = splitter.next();
            StringBuilder wholeMessage = new StringBuilder();
            wholeMessage.append(message);
            while(splitter.hasNext()){
                wholeMessage.append(","+splitter.next());
            }
            splitter.close();

            if(user.equals("REQUEST"))
            {
                return "REQUEST:"+wholeMessage.toString();
            }

            if(!channelExists(channel)){
                channels.add(channel);
                return "NewChannel:"+channel;
            }

            //If this channel is the one the user is currently looking at, spit out the message
            if(channel.equals(currentChannel)) {
                return user + " >> " + wholeMessage.toString();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            output.writeObject("TERMINATE");
            output.flush();
            output.close();
            input.close();
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
