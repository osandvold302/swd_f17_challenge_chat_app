import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

/** this controller will be in control fo the "menuView.fxml" view
 */
public class MenuController extends GeneralController {
    /** this reference to the display area where channels will appear*/
    @FXML
    private VBox messageDispArea;

    /** called when FXMLLoader loads   */
    @FXML
    public void initialize(){
        // get the channels for the user
        for(String channel : getClient().getChannels()){ // try to add all the buttons for the channels
            Button channelDisp = new Button(channel);
            channelDisp.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String channelName = ((Button) event.getSource()).getText();
                    System.out.println("Button pressed: " +channelName);
                    getClient().changeChannel(channelName);
                    MessageController.setClient(getClient());
                    System.out.println("Before loading new view");

                    // change view
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("messagesView.fxml"));
                    // once in messageView, request information
                    try{
                        Parent root = (Parent)loader.load();
                        getStage().setScene(new Scene(root));
                    }catch(IOException err){
                        err.printStackTrace();
                    }
                }
            });
            // add buttons to the VBox
            messageDispArea.getChildren().add(channelDisp);
        }
    }

    /** listener for when the user presses the create new channel button
     * @param event user presses button
     */
    @FXML
    private void newChannel(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("createChannelView.fxml"));
        try{
            Parent root = (Parent)loader.load();
            getStage().setScene(new Scene(root));
        }catch(IOException err){
            err.printStackTrace();
        }
    }
}