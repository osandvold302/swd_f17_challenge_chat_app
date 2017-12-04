import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** this controller will be in control fo the "menuView.fxml" view
 */
public class MenuController extends GeneralController {
    // get User
    // get channels
    // for each channel, add a button into the VBox
    @FXML
    private static VBox messageDispArea;
    private static ArrayList<String> channels;

    @FXML
    public void initialize(){
        getChannels();
        for(String channel : channels){
            Button channelDisp = new Button(channel);
            channelDisp.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String channelName = ((Button) event.getSource()).getText();
                    getClient().changeChannel(channelName);

                    // change view
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("messagesView.fxml"));
                    // once in messageView, request information

                }
            });
            messageDispArea.getChildren().add(new Button(channel));
        }
    }
    private static void getChannels(){
        channels = getClient().getChannels();
    }


}