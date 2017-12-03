import javax.swing.*;
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends JFrame {
    private JTextArea displayArea;
    private ExecutorService executor;
    private ServerSocket server;
    private HashMap<String,SockServer> outputs;

    public Server() {
        super("Server");
        executor = Executors.newCachedThreadPool();
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 300);
        setVisible(true);
    }

    public void runServer() {
        try {
            server = new ServerSocket(23555, 100); // create ServerSocket
            while (true) {
                try {
                    SockServer newSockServer = new SockServer();
                    newSockServer.waitForConnection();
                    executor.execute(newSockServer);
                } catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                        displayArea.append(messageToDisplay); // append message
                    }
                }
        );
    }


    /* This new Inner Class implements Runnable and objects instantiated from this
     * class will become server threads each serving a different client
     */
    private class SockServer implements Runnable {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket connection;
        private boolean alive = false;

        public void run() {
            try {
                alive = true;
                try {
                    getStreams();
                    String ID = (String) input.readObject();
                    outputs.put(ID,this);
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

        private void waitForConnection() throws IOException {
            connection = server.accept();
            connection.getInetAddress().getHostName();
        }

        private void getStreams() throws IOException {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
        }

        private void processConnection() throws IOException {
            boolean done = false;
            do{
                try{
                    //Get information from the server and find out what group and user it is from
                    String information = (String) input.readObject();
                    Scanner splitter = new Scanner(information);
                    splitter.useDelimiter(",");
                    String channel = splitter.next();
                    // if any user closes their message, the socket closes and sends "TERMINATE" to its output streams
                    // the server receives this message
                    if(channel.equals("TERMINATE")){
                        done = true;    // no longer loops to listen to client

                    }else{
                        if(){ // TODO: channel NOT IN DATABASE
                            //ADD channel TO DATABASE
                            while (splitter.hasNext()) {
                                String user = splitter.next();
                                //ADD user TO DATABASE TABLE INCLUDING channel
                            }
                        }else{  // channel exists in database already
                            String user = splitter.next();
                            String message = splitter.next();
                            StringBuilder wholeMessage = new StringBuilder();
                            wholeMessage.append(message);
                            while (splitter.hasNext()) {
                                wholeMessage.append("," + splitter.next());
                            }
                            splitter.close();


                            //User requested message log of current channel
                            if (user.equals("REQUEST") && message.equals("REQUESTOLDMESSAGES")) {
                                //SEARCH DATABASE FOR ENTIRE MESSAGE LOG OF channel AND SAVE IT AS conversationHistory
                                String conversationHistory;
                                output.write(channel + "," + "REQUEST" + conversationHistory);
                                output.flush();
                            } else {
                                //GO TO channel IN DATABASE
                                //WRITE MESSAGE AND SENDER TO DATABASE
                                //GET USER IDS AND ADD TO userList
                                ArrayList<String> userList = new ArrayList<>();
                                for(String eachUser:userList){
                                    SockServer serverOut = outputs.get(eachUser);
                                    if(serverOut.isAlive()) {
                                        ObjectOutputStream out = serverOut.getOutput();
                                        out.writeObject(channel + "," + user + "," + message);
                                        out.flush();
                                    }
                                }
                                ;                           }
                        }

                    }

                } // end try
                catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("\nUnknown object type received");
                } // end catch

            } while(!done);
        }

        public boolean isAlive(){
            return alive;
        }

        public ObjectOutputStream getOutput() {
            return output;
        }

        private void closeConnection() {
            alive = false;
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
