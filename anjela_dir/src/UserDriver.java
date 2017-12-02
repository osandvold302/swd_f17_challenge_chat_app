import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class UserDriver extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception {
        User root = FXMLLoader.load(getClass().getResource("startupView.fxml"));
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
