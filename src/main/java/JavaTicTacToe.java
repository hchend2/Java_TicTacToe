import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaTicTacToe extends Application {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    //private  Cell[][] gridCell = new Cell[3][3];
    final private  String turnX = "Turn X";
    final private  String turnO = "Turn O";
    private  Button button1;
    private  Button button2;
    private  Button button3;
    private  Button button4;

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
                gridPane.add(button, j, i);
            }
        }
        return gridPane;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Tic Tac Toe");

        button1 = new Button();
        button2 = new Button();
        button3 = new Button();
        button4 = new Button();

        button1.setText(turnX);
        button2.setText(turnO);
        button3.setText("Start");
        button4.setText("Reset");
        HBox hBox = new HBox(50, button3, button4);
        VBox vBox = new VBox(50, hBox, button1, button2);

        vBox.setPadding(new Insets(40));
        vBox.setAlignment(Pos.CENTER_RIGHT);
//        vBox.getChildren().addAll(button1, button2);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(myGrid());
        borderPane.setStyle("-fx-background-color: #2f4f4f; -fx-border-width: 1; -fx-border-color: black");
//        borderPane.setBackground(new Background(new BackgroundFill(Color.YELLOWGREEN, new CornerRadii(0), Insets.EMPTY)));
        borderPane.setRight(vBox);

        Scene scene = new Scene(borderPane, 500,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
