import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserDriver extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception {
        // get the FXML reference loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startupView.fxml"));
        Parent root = (Parent)loader.load();    // create a root based on the view

        //root.getStylesheets().add("startupViewSheet.css");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        StartUpController.setStage(primaryStage);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
