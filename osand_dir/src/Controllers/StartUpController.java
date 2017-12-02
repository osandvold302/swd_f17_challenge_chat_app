package Controllers;

import Driver.UserDriver;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.ActionListener;

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
    /** this field stores whether the username is valid or not*/
    private boolean validUser;

    private User user;

    /** this function will validate whether the username is in the database or not*/
    @FXML
    private void validUserName(ActionListener login){
        username = userIDField.getText();
        // TODO: DATABASE VERIFICATION
        validUser = true;

        // if user is in channel
        // change the view to the tab view
        // else if user is not in channel
        // change view to new Channel View

        user = new User(username);

    }

    /** this function will return the username the client sends to the app
     * @return the username the client enters - unique
     */
    public String getUsername() {
        return username;
    }

    /** this function will return whether the username entered is valid
     * @return true if valid, false if not
     */
    public boolean isValidUser() {
        return validUser;
    }
}
