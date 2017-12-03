package Controllers;

import Driver.UserDriver;
import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.IOException;

/** this model will handle the events on the startup screen - if the user enters a valid username,
 * they will be able to advance to the next screen to see their messages or create a channel
 */
public class StartUpController {
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
    /** this holds the stage the gui will be posted to*/
    private static Stage primaryStage;

    public static void setStage(Stage stage){
        primaryStage = stage;
    }

    /** this function will validate whether the username is in the database or not*/
    @FXML
    private void validUserName(ActionListener login){
        username = userIDField.getText();
        // TODO: DATABASE VERIFICATION

        while(!isValidUser(username)){
            userIDField.setStyle("-fx-text-inner-color: red;");
            errorLabel.setStyle("-fx-text-inner-color: red;");

        }
        userIDField.setStyle("-fx-text-inner-color: default;");
        errorLabel.setStyle("-fx-text-inner-color: default;");
        // if the user exits and username does not have a comma inside
        if(userExists(username) && isValidUser(username)){
            // change view to menu view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuView.fxml"));

            try{
                Parent root = (Parent)loader.load();
                primaryStage.setScene(new Scene(root)); // change the root

            }catch(IOException err){
                err.printStackTrace();
            }
            // create a new User
            User user = new User(username);

            loader.<MenuController>getController().setUser(user);
            //TODO: get all channels from this user
        }else if(isValidUser(username)){    // they don't have channels
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createChannelView.fxml"));

            try{
                Parent root = (Parent)loader.load();
                primaryStage.setScene(new Scene(root));
            }catch(IOException err){
                err.printStackTrace();
            }
        }
        user = new User(username);

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
        return username.matches("^,");
    }

    /** TODO: */
    public boolean userExists(String lookupName){
        return true;
    }
}