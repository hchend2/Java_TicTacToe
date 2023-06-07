import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class firstPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    ServerTicTacToe serverttt = new ServerTicTacToe();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connected tic-tac-toe game");

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        Label lb1 = new Label("Make sure you start the serve first");

        Button button1 = new Button("Start Server");
        Button button2 = new Button("Start Game");

        HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
//        sceneMap.put("server",);

        layout.getChildren().addAll(lb1, button1, button2);
        Scene scene = new Scene(layout, 400, 400);

        primaryStage.show();
    }
    
    public Scene serverGUI() {

        Label lb1 = new Label("This is the server side");

        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);

        vb.getChildren().addAll(lb1);
        Scene scene = new Scene(vb, 300, 300);

        return scene;
    }
}
