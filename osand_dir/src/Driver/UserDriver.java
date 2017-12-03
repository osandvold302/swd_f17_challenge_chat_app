package Driver;

import Controllers.NewChannelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserDriver extends Application
{
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        // get the FXML reference loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/startupView.fxml"));
        Parent root = (Parent)loader.load();    // create a root based on the view

        root.getStylesheets().add("startupViewSheet.css");
        stage.setScene(new Scene(root));
        stage.show();

        loader.getController().getClass();
        NewChannelController.setPrimaryStage(stage);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /** call this function when the user changes their state
     * @param fxmlView the string representation of the view to switch to
     */
    public final void switchViews(String fxmlView){

    }
}
