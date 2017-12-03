package Controllers;

import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/** this class is the general container for the controllers for each view of the chat server*/
public class GeneralController {
    /** a reference to the client who is working with the server*/
    public User client;
    /** a reference to the stage for the GUI to place the scene on*/
    public static Stage primaryStage;

    /** this function will take in a stage reference and set the controller's stage
     * @param stage where to place the scene
     */
    public static void setStage(Stage stage){
        primaryStage = stage;
    }

    /** getter for Client reference
     * @return a User reference
     */
    public User getClient() {
        return client;
    }

    /** setter for Client reference
     * @param client that this controller is using
     */
    public void setClient(User client) {
        this.client = client;
    }
}
