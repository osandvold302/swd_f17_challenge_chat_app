import javafx.stage.Stage;

/** this class is the general container for the controllers for each view of the chat server*/
public class GeneralController {
    /** a reference to the client who is working with the server*/
    private static User client;
    /** a reference to the stage for the GUI to place the scene on*/
    private static Stage primaryStage;

    /** this function will take in a stage reference and set the controller's stage
     * @param stage where to place the scene
     */
    public static void setStage(Stage stage){
        primaryStage = stage;
    }

    public static Stage getStage(){
        return primaryStage;
    }
    /** getter for Client reference
     * @return a User reference
     */
    public static User getClient() {
        return client;
    }

    /** setter for Client reference
     * @param user that this controller is using
     */
    public static void setClient(User user) {
        client = user;
    }
}
