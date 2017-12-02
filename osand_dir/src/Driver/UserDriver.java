package Driver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDriver extends Application
{
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        // get the FXML reference loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startupView.fxml"));
        Parent root = (Parent)loader.load();    // create a root based on the view

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /** call this function when the user changes their state
     * @param fxmlView the string representation of the view to switch to
     */
    public void switchViews(String fxmlView){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlView));

        try{
            Parent root = (Parent)loader.load();

            stage.setScene(new Scene(root));
        }catch(IOException err){
            err.printStackTrace();
        }
    }
}
