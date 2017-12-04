import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server class is a subclass of JFrame.
 * The Server processes new connections and adds them
 * to the thread pool.
 */
public class Server extends JFrame {
    /** Information about clients and connections*/
    private JTextArea displayArea;
    /** Executor for the threads each sockServer runs on*/
    private ExecutorService executor;
    /** ServerSocket to which Clients connect*/
    private ServerSocket server;
    /** Map of all sockServers currently in action*/
    private HashMap<String,SockServer> outputs;

    /**
     * Server constructor. Generates a new window with title "Server"
     * and a scroll pane. The executor service is initialized as well.
     */
    public Server() {
        super("Server");
        executor = Executors.newCachedThreadPool();
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 300);
        setVisible(true);
        outputs = new HashMap<>();
    }

    /**
     * Makes connections with clients and generates a new SockServer
     * for each and adds it to the thread pool.
     */
    public void runServer() {
        try {
            server = new ServerSocket(23555, 100); // create ServerSocket
            while (true) {
                try {//Generate new SockServers each time a connection is made
                    SockServer newSockServer = new SockServer();
                    newSockServer.waitForConnection();
                    executor.execute(newSockServer);//Add them to the pool
                } catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Makes the swing components thread safe by running it on its own thread
     * @param messageToDisplay will be appended to the displayArea
     */
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        displayArea.append(messageToDisplay);
                    }
                }
        );
    }


    /** This new Inner Class implements Runnable and objects instantiated from this
     * class will become server threads each serving a different client
     */
    private class SockServer implements Runnable {
        /** Stream to which information goes to client*/
        private ObjectOutputStream output;
        /** Stream from which information comes from client*/
        private ObjectInputStream input;
        /** The socket connected to the client*/
        private Socket connection;
        /** Status of a SockServer*/
        private String ID;

        /**
         * Set up steams, add user and send back it's channels.
         * Listen for messages and respond to them as necessary
         * until client is terminated.
         */
        public void run() {
            try {
                try {
                    output = new ObjectOutputStream(connection.getOutputStream());
                    input = new ObjectInputStream(connection.getInputStream());
                    ID = (String) input.readObject();
                    displayMessage("New User connected with ID: "+ID+"\n");
                    outputs.put(ID,this);
                    displayMessage("User added to map\n");
                    output.writeObject(getListFromFile("User-Channel.csv",ID));
                    output.flush();
                    displayMessage("Channel list sent.\n");
                    processConnection();
                }
                catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                } catch(ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        /**
         * Waits for a connection to be made and sets up the new socket pair
         * @throws IOException
         */
        private void waitForConnection() throws IOException {
            connection = server.accept();
        }

        /**
         * Listens for output from the user connected to this socket.
         * Dispatches messages dependent on the message received.
         * @throws IOException
         */
        private void processConnection() throws IOException {
            boolean done = false;
            do{
                try{
                    //Get information from the server and find out what group and user it is from
                    MessageToServer message = (MessageToServer) input.readObject();
                    System.out.println("received message, what type  of message");
                    if(message instanceof RequestMessage){//If the user wants message history
                        String channel = message.getChannel();
                        output.writeObject(new RequestFulfillment(channel,getConversationHistory(channel)));
                        output.flush();
                        displayMessage("Sent request fulfillment.");
                    } else if(message instanceof TextMessage){//If the user is sending a message to a channel
                        String channel = message.getChannel();
                        String user = ((TextMessage) message).getUser();
                        String text = ((TextMessage) message).getMessage();
                        if(text.equals("TERMINATE")){//If the user is quitting
                            done = true;
                        }else {
                            addMessage(channel, user, text);
                            String[] users = getListFromFile("User-Channel.csv",channel);
                            for (String eachUser : users) {
                                SockServer serverOut = outputs.get(eachUser);
                                if (serverOut!=null && serverOut != this) {
                                    ObjectOutputStream out = serverOut.getOutput();
                                    out.writeObject(new TextMessage(channel, user, text));
                                    out.flush();
                                    displayMessage("Sent text message");
                                }
                            }
                        }
                    } else if(message instanceof NewChannelMessage){//If the user wants to make a new channel
                        System.out.println("got new channel message");
                        String channel = message.getChannel();
                        String[] users = ((NewChannelMessage) message).getUsers();
                        if(!channelExists(channel)){
                            addChannel(channel, users);
                            for (String eachUser : users) {
                                SockServer serverSocket = outputs.get(eachUser);
                                System.out.println(eachUser);
                                if (serverSocket!=null) {
                                    ObjectOutputStream out = serverSocket.getOutput();
                                    out.writeObject(new RequestFulfillment(channel, "You've been added to a new Channel!"));
                                    out.flush();
                                    displayMessage("Send new channel message");
                                }
                            }
                        }else{//If the channel already exists
                            output.writeObject(new TextMessage(channel, "","Channel already exists."));
                            output.flush();
                        }
                    }
                } catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("\nUnknown object type received");
                }
            } while(!done);
        }

        /**
         * Adds to the file mapping clients to users a new channel with the specified users
         * @param channel new channel name
         * @param users array of users to be added
         */
        private void addChannel(String channel, String[] users){
            try {//Write to the end of the file a new line containing the name of the channel and all of the users
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(".\\shared\\src\\Channel-User.csv", true));
                StringBuilder builder = new StringBuilder();
                builder.append(channel);
                for(int i=0; i<users.length; i++){
                    builder.append(","+users[i]);
                }
                builder.append("\n");
                bufferedWriter.write(builder.toString());
                bufferedWriter.close();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }

        /**
         * To the specified channel, write that the user sent the message in the channel log file.
         * @param channel the channel the message was sent on
         * @param user the user who sent the message
         * @param message the message that was sent
         */
        private void addMessage(String channel, String user, String message){
            try {//Read each line and store it
                ArrayList<String> entries = fileToArray("Channel-Log.csv");
                for(int i=0; i<entries.size(); i++) {//If the line is that of the channel, append the message
                    Scanner lineReader = new Scanner(entries.get(i));
                    lineReader.useDelimiter(",");
                    if(lineReader.next().equals(channel)){
                        entries.set(i,entries.get(i)+user+" >> "+message+",");
                    }
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(".\\shared\\src\\Channel-Log.csv"));
                for(String e:entries){//Rewrite the file with the new message appended
                    writer.write(e+"\n");
                }
                writer.close();
            } catch(IOException ioe){
                ioe.printStackTrace();
            }
        }

        /**
         * Searches for the specified channel and returns as a String the entire log of all messages ever sent in it.
         * @param channel the channel being searched
         * @return the entire message log
         */
        private String getConversationHistory(String channel){
            StringBuilder builder = new StringBuilder();
                ArrayList<String> entries = fileToArray("Channel-Log.csv");
                for(String e: entries) {//For each line, check if the channel is on the specified line.
                    Scanner lineReader = new Scanner(e);
                    lineReader.useDelimiter(",");
                    if(lineReader.next().equals(channel)){
                        while(lineReader.hasNext()){//If it is, read everything else on that line and add it to the string of the backlog
                            builder.append(lineReader.next()+"\n");
                        }
                        return builder.toString();
                    }
                }
            return null;
        }

        /**
         * Searches the channel list to see if the specified channel already exists.
         * @param channel Channel being questioned
         * @return true if channel already exists and false if it doesn't
         */
        private boolean channelExists(String channel){
                ArrayList<String> entries = fileToArray("Channel-User.csv");
                for(String e: entries) {//In each line, if the first entry is equal to the current channel, return true
                    Scanner lineReader = new Scanner(e);
                    lineReader.useDelimiter(",");
                    if(lineReader.next().equals(channel)){
                        return true;
                    }
                }
            return false;
        }

        /**
         * Searches specified file for the specified key and returns an array of the values assorted with it.
         * @param fileName file information is stored in
         * @param keyWord term being searched
         * @return an array of the values associated with the key
         */
        private String[] getListFromFile(String fileName, String keyWord){
            ArrayList<String> users = new ArrayList<>();
                ArrayList<String> entries = fileToArray(fileName);
                if(entries.size()!=0) {
                    for (String e : entries) {//For each line, if the first term is the key word, add the terms following it to the list
                        Scanner lineReader = new Scanner(e);
                        lineReader.useDelimiter(",");
                        if (lineReader.next().equals(keyWord)) {
                            while (lineReader.hasNext()) {
                                users.add(lineReader.next());
                            }
                            String[] userArray = new String[users.size()];
                            for (int i = 0; i < userArray.length; i++) {//Switch all of the values from the arraylist to an array
                                userArray[i] = entries.get(i);
                            }
                            displayMessage("getListFromFile: The file had contents and is returning a nonempty array.\n");
                            return userArray;
                        }
                    }
                }else{
                    displayMessage("getListFromFile: The empty file is returning an empty array.\n");
                    return new String[0];
                }
            displayMessage("getListFromFile: the list was empty.");
            return new String[0];
        }

        /**
         * Writes the contents of a file to an ArrayList line by line
         * @param fileName name of the file to be read
         * @return An ArrayList of the file contents.
         * @throws IOException
         */
        private ArrayList<String> fileToArray(String fileName){
            ArrayList<String> entries = new ArrayList<>();
            displayMessage("fileToArray: Made arraylist\n");
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(".\\shared\\src\\"+fileName));
                displayMessage("fileToArray: Made bufferedReader\n");
                String line = bufferedReader.readLine();
                while (line != null) {
                    entries.add(line);
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
                displayMessage("fileToArray: Failed.\n");
            }
            displayMessage("fileToArray: Buffered reader closed\n");
            if (entries.isEmpty()) {
                displayMessage("fileToArray: File " + fileName + " is empty.\n");
            } else {
                displayMessage("fileToArray: File " + fileName + " had contents.\n");
            }
            return entries;
        }

        /**
         * Gets the outputStream of the specified SockServer
         * @return ObjectOutputStream of the SockServer
         */
        public ObjectOutputStream getOutput() {
            return output;
        }

        /**
         * Shuts down the server and closes all of its connections and streams.
         */
        private void closeConnection() {
            outputs.remove(ID);
            try{
                output.close();
                input.close();
                connection.close();
            }catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
