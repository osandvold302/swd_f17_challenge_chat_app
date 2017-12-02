import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class User {
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket client;

    public User(){
        try{
            client = new Socket(InetAddress.getByName("127.0.0.1"), 23555);
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Model. Will be used by controller. USE WHEN USER PRESSES SEND
     * @param message message to be sent by user
     * @return true if succesfully sent false if an error occurred
     */
    public boolean sendMessage(String message){
        try {
            output.writeObject(message);
            output.flush();
            return true;
        }catch(IOException e){
            return false;
        }
    }

    public String recieveMessage(){
        try {
            String message = (String) input.readObject();
            return message;
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}

