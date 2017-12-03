import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;

/** this model will handle the events on the startup screen - if the user enters a valid username,
 * they will be able to advance to the next screen to see their messages or create a channel
 */
public class StartUpController extends GeneralController{
    /** this reference is to the button on the startup screen*/
    @FXML
    private Button goButton;
    /** this reference is to the text field the user writes their username into*/
    @FXML
    private TextField userIDField;
    /** this field stores the username the client enters in the startup screen*/
    private String username;

    private User user;
    /** this field holds the label editor*/
    @FXML
    private Label errorLabel;

    /** this function will validate whether the username is in the database or not*/
    @FXML
    public void validUserName(ActionEvent login){
        username = userIDField.getText();
        // TODO: DATABASE VERIFICATION

        while(!isValidUser(username)){
            userIDField.setStyle("-fx-text-inner-color: red;");
            errorLabel.setStyle("-fx-text-inner-color: red;");

        }
        userIDField.setStyle("-fx-text-inner-color: default;");
        errorLabel.setStyle("-fx-text-inner-color: default;");
        // if the user exits and username does not have a comma inside
        FXMLLoader loader;
        if(userExists(username)){
            // change view to menu view
            loader = new FXMLLoader(getClass().getResource("menuView.fxml"));
            //TODO: get all channels from this user
            try{
                Parent root = (Parent)loader.load();
                getStage().setScene(new Scene(root));
            }catch(IOException err){
                err.printStackTrace();
            }finally {
                user = new User(username);
                MenuController control = loader.getController();
                control.setClient(user);
            }
        }else{    // they don't have channels - set up the create channel view
            loader = new FXMLLoader(getClass().getResource("createChannelView.fxml"));
            try{
                Parent root = (Parent)loader.load();
                getStage().setScene(new Scene(root));
            }catch(IOException err){
                err.printStackTrace();
            }finally {
                user = new User(username);
                NewChannelController control = loader.getController();
                control.setClient(user);
            }
        }
    }

    /** this function will return the username the client sends to the app
     * @return the username the client enters - unique
     */
    public String getUsername() {
        return username;
    }

    /** this function will return whether the username entered is valid
     * the username MUST NOT CONTAIN ANY COMMAS
     * @return true if valid, false if not
     */
    public boolean isValidUser(String username) {
        return (username.matches("^,") || !username.contains(" "));
    }

    /** TODO: */
    public boolean userExists(String lookupName){
        return true;
    }
}