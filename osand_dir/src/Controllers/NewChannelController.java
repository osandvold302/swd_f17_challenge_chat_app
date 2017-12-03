package Controllers;

import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.IOException;

/** this class is the controller for the view to create a new channel*/
public class NewChannelController {
    /** the user who is creating the new channel*/
    private String username;
    /** this field stores the reference to the text field object the user writes their channel name in*/
    @FXML
    private TextField channelName;
    /** this field stores the reference to the button the user presses to create a channel*/
    @FXML
    private Button createChannelButton;
    /** this field stores the list of users the client wants to add to a channel*/
    @FXML
    private TextArea usersList;

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        NewChannelController.primaryStage = primaryStage;
    }

    /** validate channel name
     * @param channel the name the client wants for their channel
     * @return true if the channel is unique, false if not
     */
    public boolean validateChannelName(String channel){
        // TODO:
        // if channel is not in database
        // write to database
        // return true
        //else
        return false;
    }

    /** will return an array of the list of users to add to the channel
     * @param nameListWithCommas a string with all the names of the users to add in a channel
     * @return the array of all the users in the channel
     */
    public String[] getListOfNames(String nameListWithCommas){
        if(nameListWithCommas.isEmpty()){   // if the user did not type anything
           String[] names = new String[1];
           names[0] = "";
           return names;    // return nothing
        }
        nameListWithCommas+= " ," + username;
        String[] names = nameListWithCommas.split(",");
        for(int i=0;i<names.length;i++){
            names[i] = names[i].trim(); // get rid of the whitespace
        }
        return names;
    }

    /** this function will set up a new channel
     * @param makeNewChannel the button pressed to create a channel
     */
    @FXML
    public void setUpNewChannel(ActionListener makeNewChannel){
        String tryChannelName = channelName.getText();

        // TODO: database
        // if channel name exists in database
        // channelName.setStyle("-fx-text-inner-color: red;");
        // channelName.setText("This channel already exists :(");
        // else
        String[] usersInChannel = getListOfNames(usersList.getText());
        // send the usersInChannel to the User to send to the Server

        // TODO: Change View
        FXMLLoader loader = new FXMLLoader(getClass().getResource("messagesView.fxml"));

        try{
            Parent root = (Parent)loader.load();

            primaryStage.setScene(new Scene(root));

        }catch(IOException err){
            err.printStackTrace();
        }
    }

}
