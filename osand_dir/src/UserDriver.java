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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startupView.fxml"));
        Parent root = (Parent)loader.load();    // create a root based on the view

        root.getStylesheets().add("C:\\Users\\osand\\Desktop\\SWD\\team15_swd_challenge\\team15_swd\\osand_dir\\src\\Driver\\startupViewSheet.css");
        stage.setScene(new Scene(root));
        stage.show();

        loader.getController().getClass();
        NewChannelController.setStage(stage);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
