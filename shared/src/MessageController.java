import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Scanner;

public class MessageController extends GeneralController {

    /** this is where all of the messages will be printed*/
    @FXML
    private static VBox messageDispArea;
    /** field where user types their information*/
    @FXML
    private TextField textMessageField;
    /** button to bring back to menu*/
    @FXML
    private Button backToMenu;
    /** the name of the channel*/
    @FXML
    private static Label channelNameLabel;

    private static boolean onMessages = true;

    /** static function will set all the messages from the past conversations   */
    public static void initMessages(){
        getClient().requestMessageHistory(getClient().getCurrentChannel());

        // client would listen for all of the messages to come to the screen
        String requestAllMessages = getClient().receiveMessage();
        while(!requestAllMessages.substring(0,8).equals("REQUEST:")){
            requestAllMessages = getClient().receiveMessage();
        }
        requestAllMessages = requestAllMessages.substring(8); // trim REQUEST:

        Scanner splitter = new Scanner(requestAllMessages);
        while(splitter.hasNext()){
            messageDispArea.getChildren().add(new Label(splitter.nextLine()));
        }

        channelNameLabel.setText(getClient().getCurrentChannel());

        while(onMessages){
            String text = getClient().receiveMessage();
            if(text!=null && !text.substring(0,11).equals("NEWCHANNEL:")){
                addMessages(text);
            }
        }
    }

    /** this function will send the message when the user presses the send button or presses enter
     * @param event the button being pressed
     */
    @FXML
    private void messageListenerButton(ActionEvent event){
        String message = textMessageField.getText();

        getClient().sendMessage(getClient().getID() + " >> " +message);

        textMessageField.setText("");
    }

    @FXML
    private void setBackToMenu(ActionEvent event){
        onMessages = false;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuView.fxml"));
        MenuController.initMenu();
        try{
            Parent root = (Parent)loader.load();
            getStage().setScene(new Scene(root));
        }catch(IOException err){
            err.printStackTrace();
        }
    }

    private static void addMessages(String message){
        messageDispArea.getChildren().add(new Label(getClient().getID()+" >> "+message));
    }

}
