package Controllers;

import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/** this controller will be in control fo the "menuView.fxml" view
 */
public class MenuController {
    // get User
    // get channels
    // for each channel, add a button into the VBox

    private User user;
    @FXML
    private VBox messageDispArea;

    public void setUser(User user) {
        this.user = user;
    }


    // messageDispArea.getChildren().add(new Button(channel.getName()));
}