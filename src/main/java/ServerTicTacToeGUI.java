
//import com.sun.security.ntlm.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;
import java.io.Serializable;
import java.util.HashMap;

public class ServerTicTacToeGUI extends Application {

    private Server server_connection;
    private int port;
    private HashMap<String, Scene> SceneMap = new HashMap<>();
    private TextField port_input;
    private Button exit_b, open_server, close_server;
    private ListView<Serializable> progress = new ListView<>();
    private ListView<Serializable> round_play_stats = new ListView<>();
    private ListView<Serializable> connected_client = new ListView<>();
    private Text ser_title;

    public static void main(String[] args) {
        launch(args);
    }

    private void handleCallback(GameInfo dta) {
//        switch (dta.type) {
        if ( dta.type == "UpdatePlayers") {
            connected_client.getItems().clear();
            for (Server.ClientThread thread : dta.clients) {
                if (thread == null) {
                    continue;
                }
                connected_client.getItems().add("Player #" + thread.count);
            }
        }
//        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server");

        //        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private Scene ServerGUI1() {
        // creating and setting up various variables like buttons, text fields, and titles to their positions
        // and set their styles for scene 1
        Text serverTitle1 = desired_text(new Text("Tic Tac"));
        Text serverTitle2 = desired_text(new Text("Toe!"));
        Text empty = desired_text(new Text(""));
        ser_title = desired_text(new Text("NEW SERVER INFORMATION"));

        empty.setStyle("-fx-font-size: 20px");
        ser_title.setStyle("-fx-font-size: 25px");

        port_input = _textField(new Text("PORT NUMBER"));

        open_server = new Button("CREATE");
        exit_b = new Button("EXIT");

        exit_b.setStyle(
                "-fx-pref-width: 150px;" + "-fx-pref-height: 10px;" +
                        "-fx-font-size: 25px;" + "-fx-spacing: 100px;" +
                        "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;"
        );
        open_server.setStyle(
                "-fx-pref-width: 200px;" + "-fx-pref-height: 10px;" +
                        "-fx-font-size: 25px;" + "-fx-spacing: 100px;" +
                        "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;"
        );

        VBox startBox = new VBox();
        VBox inputBox = new VBox(10);

        startBox.setAlignment(Pos.CENTER);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setMaxSize(300, 20);
        inputBox.setPadding(new Insets(20, 0, 0, 0));
        // disable auto selection
        port_input.setFocusTraversable(false);
        open_server.setFocusTraversable(false);
        exit_b.setFocusTraversable(false);

        startBox.getChildren().addAll(serverTitle1, serverTitle2, empty, ser_title, inputBox);
        inputBox.getChildren().addAll(port_input, open_server, exit_b);

        return new Scene(startBox, 700, 700);
    }


    private Scene ServerGUI2() {
        BorderPane borderPane = new BorderPane();
        close_server = new Button("CLOSE SERVER");

        close_server.setStyle(
                "-fx-pref-width: 220px;" + "-fx-pref-height: 10px;" +
                        "-fx-font-size: 22px;" + "-fx-spacing: 100px;" +
                        "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;"
        );

        HBox buttonsArea = new HBox(40, close_server);
        close_server.setFocusTraversable(false);
        buttonsArea.setAlignment(Pos.CENTER);
        buttonsArea.setPadding(new Insets(20, 0, 20, 0));

        VBox leftBox = new VBox(connected_client);
        connected_client.setPrefHeight(600);
        connected_client.setPrefWidth(500);
        connected_client.setStyle(
                "-fx-background-radius: 1em;" +
                        "-fx-border-radius: 1em;"
        );
        connected_client.setFocusTraversable(false);

        leftBox.setPrefHeight(700);
        leftBox.setPrefWidth(500);
        leftBox.setPadding(new Insets(20, 10, 20, 20));
        leftBox.setAlignment(Pos.CENTER);

        VBox rightBox = new VBox(round_play_stats, progress);
        rightBox.setPrefHeight(700);
        rightBox.setPrefWidth(500);
        rightBox.setPadding(new Insets(20, 10, 20, 20));
        rightBox.setSpacing(20);
        rightBox.setAlignment(Pos.CENTER);
        round_play_stats.setPrefHeight(280);
        round_play_stats.setPrefWidth(500);
        round_play_stats.setStyle(
                "-fx-background-radius: 1em;" +
                        "-fx-border-radius: 1em;"
        );
        round_play_stats.setFocusTraversable(false);

        progress.setPrefHeight(280);
        progress.setPrefWidth(500);
        progress.setStyle(
                "-fx-background-radius: 1em;" +
                        "-fx-border-radius: 1em;"
        );
        progress.setFocusTraversable(false);

        borderPane.setRight(rightBox);
        borderPane.setCenter(leftBox);
        borderPane.setBottom(buttonsArea);
        return new Scene(borderPane, 900, 700);
    }

    private TextField _textField(Text txt) {
        TextField textField = new TextField();
        textField.setPromptText(txt.getText());
        textField.setFocusTraversable(false);
        textField.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-border-radius: 1em;" +
                "-fx-display-caret: false"
        );
        return textField;
    }

    private Text  desired_text(Text str) {
        str.setStyle("-fx-font-size: 40");
        return str;
    }

    private void clear_all(boolean f) {
        port_input.setDisable(f);
        exit_b.setDisable(f);
        open_server.setDisable(f);
    }

}
