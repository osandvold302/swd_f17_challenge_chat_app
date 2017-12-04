import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Scanner;

public class MessageController extends GeneralController {

    /** this is where all of the messages will be printed*/
    @FXML
    private TextArea messageDispArea;
    /** field where user types their information*/
    @FXML
    private TextField textFieldMessages;
    /** the name of the channel*/
    @FXML
    private Label channelNameLabel;

    /** static function will set all the messages from the past conversations   */
    @FXML
    public void initialize(){
        messageDispArea.setText(" ");
        channelNameLabel.setText(getClient().getCurrentChannel());

        // client would listen for all of the messages to come to the screen
        String requestAllMessages = getClient().receiveMessage();
        while(!requestAllMessages.substring(0,8).equals("REQUEST:")){
            requestAllMessages = getClient().receiveMessage();
        }
        if(requestAllMessages.length()>8 && requestAllMessages.contains(getClient().getCurrentChannel())){
            requestAllMessages = requestAllMessages.substring(8+getClient().getCurrentChannel().length()); // trim REQUEST:
            requestAllMessages=requestAllMessages.replace("\\n","\n");
            messageDispArea.setText(requestAllMessages);
        }

    }

    /** this function will send the message when the user presses the send button or presses enter
     * @param event the button being pressed
     */
    @FXML
    private void messageListenerButton(ActionEvent event){
        String message = textFieldMessages.getText();
        message = message.trim();

        getClient().sendMessage(message+"\\n");
        addMessages(getClient().getID()+" >> "+message);

        textFieldMessages.setText("");
    }

    /** this function will send the user back to the menu
     * @param event pressing the back button
     */
    @FXML
    private void setBackToMenu(ActionEvent event){
        MenuController.setClient(getClient());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuView.fxml"));
        try{
            Parent root = (Parent)loader.load();
            getStage().setScene(new Scene(root));
        }catch(IOException err){
            err.printStackTrace();
        }
    }

    /** function to add messages to the text area
     * @param message to send
     */
    private void addMessages(String message){
        messageDispArea.appendText(message+"\n");
    }
}
