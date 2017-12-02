import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService executor; // will run players
    private ServerSocket server; // server socket
    private SockServer[] sockServer; // Array of objects to be threaded
    private int counter = 0; // counter of number of connections
    private int nClientsActive = 0;
    private final int CLIENTLIMIT = 1000;

    public Server() {
        sockServer = new SockServer[CLIENTLIMIT];
        executor = Executors.newFixedThreadPool(CLIENTLIMIT);
    }

    //ALL THE MAIN SERVER DOES IS START SOCKET CONNECTIONS TO EACH CLIENT EACH ON THEIR OWN THREAD
    public void runServer() {
        try {
            server = new ServerSocket(23555, 100);
            while (true) {
                try {
                    sockServer[counter] = new SockServer();
                    sockServer[counter].waitForConnection();
                    nClientsActive++;
                    executor.execute(sockServer[counter]);
                } catch (EOFException eofException) {
                    eofException.printStackTrace();
                } finally {
                    ++counter;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //Receive messages, identify group and socket, determine who else is in that group and store information, send same message to all members

    private class SockServer implements Runnable {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket connection;
        private boolean alive = false;

        public void run() {
            try {
                alive = true;
                try {
                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());
                    processConnection(); // process connection
                    nClientsActive--;
                } catch (EOFException eofException) {
                    eofException.printStackTrace();
                } finally {
                    alive = false;
                    try {
                        output.close();
                        input.close();
                        connection.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        private void waitForConnection() throws IOException {
            connection = server.accept();
        }

        // process connection with client
        private void processConnection() throws IOException {
            String message = "Connection " + myConID + " successful";
            output.writeObject(message);
            output.flush();

            do // process messages sent from client
            {
                try // read message and display it
                {
                    message = (String) input.readObject(); // read new message
                    displayMessage("\n" + myConID + message); // display message
                } // end try
                catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("\nUnknown object type received");
                } // end catch

            } while (!message.equals("CLIENT>>> TERMINATE"));
        } // end method processConnection
    }
}
