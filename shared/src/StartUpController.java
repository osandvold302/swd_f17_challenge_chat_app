import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

/** this model will handle the events on the startup screen - if the user enters a valid username,
 * they will be able to advance to the next screen to see their messages or create a channel
 */
public class StartUpController extends GeneralController {
    /** this reference is to the button on the startup screen*/
    @FXML
    private Button goButton;
    /** this reference is to the text field the user writes their username into*/
    @FXML
    private TextField userIDField;
    /** this field stores the username the client enters in the startup screen*/
    private String username;

    /** this field holds the label editor*/
    @FXML
    private Label errorLabel;

    /** this function will validate whether the username is in the database or not*/
    @FXML
    private void validUserName(ActionEvent login){
        username = userIDField.getText();
        if(!isValidUser(username)){
            userIDField.setStyle("-fx-background-color: red;");
            errorLabel.setStyle("-fx-background-color: red;");
            System.out.println("User entered invalid information");
            return;
        }
        userIDField.setStyle("-fx-text-inner-color: default;");
        errorLabel.setStyle("-fx-text-inner-color: default;");
        User newUser = new User(username);
        ArrayList<String> channels = newUser.getChannels();
        FXMLLoader loader;
        // channels exist for user
        if(!channels.isEmpty()){
            MenuController.setClient(newUser);
            // change view to menu view
            loader = new FXMLLoader(getClass().getResource("menuView.fxml"));
            try{
                Parent root = (Parent)loader.load();
                getStage().setScene(new Scene(root));
            }catch(IOException err){
                err.printStackTrace();
            }
        }else{    // they don't have channels - set up the create channel view
            NewChannelController.setClient(newUser);
            loader = new FXMLLoader(getClass().getResource("createChannelView.fxml"));
            try{
                Parent root = (Parent)loader.load();
                getStage().setScene(new Scene(root));
            }catch(IOException err){
                err.printStackTrace();
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
     * the username MUST NOT CONTAIN ANY COMMAS OR SPACES
     * @return true if valid, false if not
     */
    public boolean isValidUser(String username) {
        if(username.contains(",")){
            return false;
        }else if(username.contains(" ")){
            return false;
        }else {
            return true;
        }
    }

}