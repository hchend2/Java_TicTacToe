import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class JavaTicTacToe extends Application {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }


    private  Client clientConnection;

    private  int filled_square = 0, port;
    private Integer player_id;
    String host = null;
    boolean getResult = false;
    private Text client_title3 = new Text();
    final  String turnX = "Turn X";
    final  String turnO = "Turn O";
    final String labelText = "Tic Tac Toe Game";
    final String msg1 = "Your " + turnX;
    final String msg2 = "Your " + turnO;
    final String msg3 = "Winner is: X";
    final String msg4 = "Winner is: O";
    final String msg5 = "No Winner: Tie";
    String msg6 = "Click START to begin";
    private  Button playAgain, exit_b, connect_client, clear_info, easy_mode, medium_mode, expert_mode;
    private  Button button2;
    private  Button button3;
    private  Button button4;
    final Label label = new Label();
    
    
    private TextField ip_input, port_input, result;
    private Vector<Rectangle> board = new Vector<>();
    private ArrayList<String> curr_moves = new ArrayList<>();
    private PauseTransition pause_send_move = new PauseTransition(Duration.seconds(1));
    private HashMap<String, Scene> SceneMap = new HashMap<>();
    private ListView<Serializable> scores = new ListView<>();
    private ListView<Serializable> progress = new ListView<>();


//    TicTacToeLogic check;
//    String ls = check.isGridFull(); // just a test ...


    public void buttonXHandler(Button btn) {
        btn.setOnAction(event -> {
            btn.setText("X");
        });
    }

    public void buttonOHandler(Button btn) {
        btn.setOnAction(event -> {
            btn.setText("O");
        });
    }

    public GridPane myGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setPadding(new Insets(20));

//        Button button = new Button();
//        button.setPrefSize(30, 30);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(90, 90);
                button.setLineSpacing(10.0);
//                buttonXHandler(button);
                button.setStyle("-fx-border-color: yellow;" +
                        "-fx-border-width: 0.5;"+
                        "-fx-border-radius: 10;");
                gridPane.add(button, j, i);
            }
        }
        return gridPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Tic Tac Toe");

        primaryStage.setTitle("Tic Tac Toe Game");

        SceneMap.put("ClientScene1", clientGUI1();
        SceneMap.put("ClientScene2", ClientGUI2());
        SceneMap.put("ClientScene3", ClientGUI3());

        exit_b.setOnAction(e -> primaryStage.close());

        playAgain.setOnAction(e -> {
            primaryStage.setScene(SceneMap.get("ClientScene2"));

            clearBoard();
            setClickable(false);

            filled_square = 0;

            init_moves();

            result.setText("");

            getResult = false;

            // send the updated game board to the server
            try {
                clientConnection.sendData(new Pair(player_id, new Pair("playAgain", curr_moves)));
            } catch (Exception error) {
                error.printStackTrace();
            }
        });

        exit_b.setOnAction(e -> {
            primaryStage.close();
            try {
                // close down the client
                clientConnection.sendData("QUIT");
                clientConnection.shut_down();;
                Platform.exit();
                System.exit(0);
            } catch (Exception ignored) {
            }
        });

        connect_client.setOnAction(e -> {
            try {
                host = ip_input.getText();
                port = Integer.parseInt(port_input.getText());
                clientConnection = new Client(host, port,
                        data -> Platform.runLater(() -> progress.getItems().add(data.toString())),
                        data -> Platform.runLater(() -> {
                            scores.getItems().clear();
                            scores.getItems().add(data.toString());
                        }),
                        data -> Platform.runLater(() -> player_id = (Integer) data),
                        data -> Platform.runLater(() -> {
                            computerMakeMove((int) data - 1);
                        }),
                        data -> Platform.runLater(() -> update_clientGUI(data.toString()))
                );
                primaryStage.setScene(SceneMap.get("ClientScene2"));

                clientConnection.start();

                System.out.println("Client Start Called");
            } catch (Exception ignored) {
                client_title3.setText("SERVER NOT FOUND. RETRY!!!");
                client_title3.setStyle("-fx-font-size: 25;" + "-fx-fill: RED;");
                ip_input.setText("");
                port_input.setText("");
                clearAll();
                ip_input.setFocusTraversable(false);
                port_input.setFocusTraversable(false);
            }
        });
        EventHandler<KeyEvent> tabEventHandler = event -> {
            if (event.getCode() == KeyCode.TAB) {
                if (ip_input.isFocused()) port_input.requestFocus();
                else if (port_input.isFocused()) ip_input.requestFocus();
            }
            if (event.getCode() == KeyCode.ENTER) {
                connect_client.fire();
                event.consume();
            }
        };

        ip_input.setOnKeyPressed(tabEventHandler);
        port_input.setOnKeyPressed(tabEventHandler);

        easy_mode.setOnAction(mouseClick -> {
            try {
                clientConnection.sendData(new Pair(player_id, new Pair("levelType", "easy")));

                playAgain.setDisable(true);
                exit_b.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            primaryStage.setScene(SceneMap.get("ClientScene3"));
        });

        medium_mode.setOnAction(mouseEvent -> {
            try {
                clientConnection.sendData(new Pair(player_id, new Pair("levelType", "medium")));

                playAgain.setDisable(true);
                exit_b.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            primaryStage.setScene(SceneMap.get("ClientScene3"));
        });

        expert_mode.setOnAction(mouseEvent -> {
            try {
                clientConnection.sendData(new Pair(player_id, new Pair("levelType", "expert")));

                playAgain.setDisable(true);
                exit_b.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            primaryStage.setScene(SceneMap.get("ClientScene3"));
        });

        primaryStage.setScene(SceneMap.get("ClientScene1"));
        primaryStage.show();
    }

    private Scene clientGUI1() {
        Text client_title1 = desiredText(new Text("Tic Tac Toe"));
        Text client_title2 = desiredText(new Text("Min Max Game"));

        Text empty = desiredText(new Text(""));
        client_title3 = desiredText(new Text("Enter Player Info"));
        
        empty.setStyle("-fx-font-size: 20px");
        client_title3.setStyle("fx-font-size: 25px");

        ip_input =  createTextField(new Text("IP ADD"));
        port_input = createTextField(new Text("PORT NUM"));

        connect_client = createButton("CONNECT");
        clear_info = createButton("CLEAR");
        exit_b = createButton("EXIT");

        VBox startBox = new VBox();
        HBox buttons = new HBox();
        VBox input_box = new VBox();

        startBox.setAlignment(Pos.CENTER);
        input_box.setAlignment(Pos.CENTER);
        input_box.setMaxSize(350, 25);
        input_box.setPadding(new Insets(20,0,0,0));
        input_box.setSpacing(10);

        startBox.getChildren().addAll(client_title1, client_title2, empty, client_title3, input_box);
        buttons.getChildren().addAll(connect_client, clear_info);
        input_box.getChildren().addAll(ip_input, port_input, buttons, exit_b);

        clear_info.setOnAction(e -> clearAll());

        return new Scene(startBox, 700, 700);
    }

    private Scene ClientGUI2() {
        Text text = new Text("Select Computer Level");
        text.setStyle("-fx-font-size: 20px; -fx-underline: single");
        easy_mode = createButton("EASY");
        medium_mode = createButton("MEDIUM");
        expert_mode = createButton("EXPERT");

        VBox mode_area = new VBox(20, text, easy_mode, medium_mode, expert_mode);
        mode_area.setAlignment(Pos.CENTER);
        return new Scene(mode_area, 400, 400);
    }

    private Scene ClientGUI3() {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-spacing: 20;-fx-padding: 20");
        playAgain = createButton("PLAY AGAIN");
        HBox buttonsArea = new HBox(20, playAgain, exit_b);
        buttonsArea.setAlignment(Pos.CENTER);

        GridPane stackPane = createGridPane();
        init_moves();
        setClickable(false);
        stackPane.setGridLinesVisible(true);

        result = new TextField();
        result.setPromptText("RESULT");
        result.setPrefWidth(170);
        result.setPrefHeight(150);
        result.setAlignment(Pos.CENTER);
        result.setFocusTraversable(false);
        result.setDisable(true);
        result.setStyle("-fx-font-size: 20px;" +
                "-fx-border-radius: 1em;" +
                "-fx-background-radius: 1em;" +
                "-fx-text-alignment: center;" +
                "-fx-opacity: 1");

        VBox vBox = new VBox(40, scores, result);
        vBox.setPadding(new Insets(0, 0, 0, 20));
        vBox.setPrefHeight(200);
        vBox.setPrefWidth(200);
        vBox.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(30, stackPane, vBox);
        scores.setFocusTraversable(false);
        scores.setPrefWidth(200);
        scores.setPrefHeight(200);
        scores.getItems().add("Top Scores: ");

        hBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setCenter(hBox);
        borderPane.setBottom(buttonsArea);

        return new Scene(borderPane, 700, 500);
    }

    private  GridPane createGridPane() {
        Rectangle TopLeft = createRectangle(0);
        Rectangle TopCenter = createRectangle(1);
        Rectangle TopRight = createRectangle(2);

        Rectangle CenterLeft = createRectangle(3);
        Rectangle CenterCenter = createRectangle(4);
        Rectangle CenterRight = createRectangle(5);

        Rectangle BottomLeft = createRectangle(6);
        Rectangle BottomCenter = createRectangle(7);
        Rectangle BottomRight = createRectangle(8);

        GridPane gridPane = new GridPane();
        gridPane.add(TopLeft, 0, 0);
        gridPane.add(TopCenter, 1, 0);
        gridPane.add(TopRight, 2, 0);

        gridPane.add(CenterLeft, 0, 1);
        gridPane.add(CenterCenter, 1, 1);
        gridPane.add(CenterRight, 2, 1);

        gridPane.add(BottomLeft, 0, 2);
        gridPane.add(BottomCenter, 1, 2);
        gridPane.add(BottomRight, 2, 2);

        board.add(TopLeft);
        board.add(TopCenter);
        board.add(TopRight);

        board.add(CenterLeft);
        board.add(CenterCenter);
        board.add(CenterRight);

        board.add(BottomLeft);
        board.add(BottomCenter);
        board.add(BottomRight);

        return gridPane;
    }

    private void clearAll() {
        ip_input.setText("");
        port_input.setText("");
        clear_info.setFocusTraversable(false);
    }
    private TextField createTextField(Text str) {
        TextField textField = new TextField();
        textField.setPromptText(str.getText());
        textField.setFocusTraversable(false);

        textField.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-background-radius: 1em;" +
                "-fx-border-radius: 1em;" +
                "-fx-display-caret: false;"
        );
        return textField;
    }
    private Text desiredText(Text str) {
        str.setStyle("-fx-font-size: 40");
        return str;
    }

    private Button createButton(String txt) {

        Button button = new Button(txt);
        button.setFocusTraversable(false);

        button.setStyle(
            "-fx-pref-width: 150px;" + "-fx-pref-height: 10px;" +
                    "-fx-font-size: 20px;" + "-fx-spacing: 100px;" +
                    "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;" +
                    "-fx-background-color: #ffffff;" + "-fx-border-color: #000000;"
        );
        button.setOnMouseEntered(e ->
                button.setStyle(
                    "-fx-pref-width: 150px;" + "-fx-pref-height: 10px;" +
                            "-fx-font-size: 20px;" + "-fx-spacing: 100px;" +
                            "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;" +
                            "-fx-background-color: #c2c2c2;" + "-fx-border-color: #000000;"
                )
        );
        button.setOnMouseExited(e ->
                button.setStyle(
                    "-fx-pref-width: 150px;" + "-fx-pref-height: 10px;" +
                            "-fx-font-size: 20px;" + "-fx-spacing: 100px;" +
                            "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;" +
                            "-fx-background-color: #ffffff;" + "-fx-border-color: #000000;"
                )
        );
        button.setOnMousePressed(e ->
                button.setStyle(
                    "-fx-pref-width: 150px;" + "-fx-pref-height: 10px;" +
                            "-fx-font-size: 20px;" + "-fx-spacing: 100px;" +
                            "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;" +
                            "-fx-background-color: #676767;" + "-fx-border-color: #000000;"
                )
        );
        button.setOnMouseReleased(e ->
                button.setStyle(
                    "-fx-pref-width: 150px;" + "-fx-pref-height: 10px;" +
                            "-fx-font-size: 20px;" + "-fx-spacing: 100px;" +
                            "-fx-border-radius: 1em;" + "-fx-background-radius: 1em;" +
                            "-fx-background-color: #ffffff;" + "-fx-border-color: #000000;"
                )
        );
        return button;
    }

    private  Rectangle createRectangle(int location) {
        Rectangle rectangle = new Rectangle(133, 133);
        rectangle.setFill(new Color(0,0,0,0));
        rectangle.setOnMouseClicked(e -> playerMakeMove(location));

        return rectangle;
    }

    private void update_clientGUI(String dta) {
        result.setText(dta);
        setClickable(true);
        getResult = true;

        playAgain.setDisable(false);
        exit_b.setDisable(false);

    }
    private void enableOpenMoves() {

        if (getResult) { return; }
        for (int i = 0; i < 9; i++) {
            if (curr_moves.get(i).equals("X") || curr_moves.get(i).equals("O"))
                continue;
            board.get(i).setDisable(false);
        }
    }

    private void playerMakeMove(int location) {
        board.get(location).setFill(new ImagePattern(new Image("O.png")));
        setClickable(true);
        curr_moves.set(location, "O");
        filled_square++;


        pause_send_move.setOnFinished(e ->{
            try {
                if (filled_square <= 9) {

                    clientConnection.sendData(new Pair(player_id, new Pair("gameBoardUpdate", curr_moves)));
                    pause_send_move.stop();
                }
            } catch (Exception i) {
                i.printStackTrace();
            }
        });
        pause_send_move.play();
    }

    private void computerMakeMove(int loc) {
        board.get(loc).setFill(new ImagePattern(new Image("X.png")));
        curr_moves.set(loc, "X");
        enableOpenMoves();
        filled_square++;

    }
    private void init_moves() {
        curr_moves.clear();
        for (int i = 0; i < 9; i++) {
            curr_moves.add("b");
        }
    }
    private  void setClickable(boolean bool) {
        for (Rectangle rectangle : board) {
            rectangle.setDisable(bool);
        }
    }
    private void clearBoard() {
        for (Rectangle rectangle: board) {
            rectangle.setFill(new Color(0,0,0,0));
            rectangle.setDisable(false);
        }
    }
}
