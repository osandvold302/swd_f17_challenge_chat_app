import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

/** this class is the controller for the view to create a new channel*/
public class NewChannelController extends GeneralController {
    /** this field stores the reference to the text field object the user writes their channel name in*/
    @FXML
    private TextField channelNameField;
    /** this field stores the list of users the client wants to add to a channel*/
    @FXML
    private TextArea usernameField;

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
        nameListWithCommas+= " ," + getClient().getID();
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
    public void setUpNewChannel(ActionEvent makeNewChannel){
        String channel = channelNameField.getText();
        User user = getClient();
        user.newChannel(channel,getListOfNames(usernameField.getText()));
        boolean determined = false; // determine if we have gotten an existing channel
        while(!determined){ // we don't know if channel exists
            String message = getClient().receiveMessage();  // wait for output.flush message
            System.out.println(message);
            if(message.equals("Channel already exists!")){
                channelNameField.setStyle("-fx-text-inner-color: red;");
                channelNameField.setText("This channel already exists :(");
                determined = true;
            }
            if(!message.isEmpty() && message.substring(0,11).equals("NEWCHANNEL:")){
                getClient().changeChannel(channel);
                determined = true;
                MessageController.setClient(getClient());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("messagesView.fxml"));
                try{
                    Parent root = (Parent)loader.load();
                    getStage().setScene(new Scene(root));
                }catch(IOException err){
                    err.printStackTrace();
                }
            }
        }
    }

}
