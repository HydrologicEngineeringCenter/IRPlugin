package alteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIMainTester extends Application {
    //thi si a pointless comment. again
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene scene = new Scene(root, 950, 400);
        scene.getStylesheets().add("/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("IRP Alternative Editor");
        primaryStage.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}
