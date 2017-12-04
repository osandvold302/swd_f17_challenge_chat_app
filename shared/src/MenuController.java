import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

/** this controller will be in control fo the "menuView.fxml" view
 */
public class MenuController extends GeneralController {
    @FXML
    private VBox messageDispArea;
    private ArrayList<String> channels;

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
                    try{
                        Parent root = (Parent)loader.load();
                        getStage().setScene(new Scene(root));
                    }catch(IOException err){
                        err.printStackTrace();
                    }
                }
            });
            messageDispArea.getChildren().add(new Button(channel));
        }
    }
    private void getChannels(){
        channels = getClient().getChannels();
    }

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