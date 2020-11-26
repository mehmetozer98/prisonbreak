package worldofzuul;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
        // Game game = new Game();
        // game.play();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("World of Zuul");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
