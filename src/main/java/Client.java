import javafx.util.Pair;

import java.awt.image.ConvolveOp;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Client {
    private ClientThread connectionThread = new ClientThread();
    private Consumer<Serializable> scores;
    private Consumer<Serializable> progress;
    private Consumer<Serializable> player;
    private Consumer<Serializable> newBoard;
    private Consumer<Serializable> gameResult;
    private String IP;
    private int port;

    Client(String ip, int port,
        Consumer <Serializable> progress,
        Consumer <Serializable> scores,
        Consumer <Serializable> player,
        Consumer <Serializable> newBoard,
        Consumer <Serializable> gameResult)
    {
        this.IP = ip;
        this.port = port;
        this.progress = progress;
        this.scores = scores;
        this.player = player;
        this.newBoard = newBoard;
        this.gameResult = gameResult;
        connectionThread.setDaemon(true);
    }
    private String getIP() {
        return this.IP;
    }

    private int getPort() {
        return this.port;
    }
    public void start() {
        connectionThread.start();
    }
    public  void shut_down() throws Exception {
        connectionThread.socket.close();
    }
    public void sendData(Serializable data) throws Exception {
        connectionThread.out.writeObject(data);
        connectionThread.out.reset();
    }
    private void handle_update_board(Pair data) {
        newBoard.accept((int)data.getValue());
    }

    private void handle_game_result(Pair data) {
        Pair<Integer, ArrayList<String>> s_pair = (Pair<Integer, ArrayList<String>>) data.getValue();

        gameResult.accept(data.getKey().toString() + "\n Score: " + s_pair.getKey().toString());
    }
    private void handle_scores(Pair dta) {
        ArrayList<Integer> s_pair = (ArrayList<Integer>) dta.getValue();
        scores.accept(s_pair);
    }
    class ClientThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        public void run() {
            try {
                Socket socket1 = new Socket(getIP(), getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket1.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket1.getInputStream());

                this.socket = socket1;
                this.out = out;
                socket1.setTcpNoDelay(true);
                progress.accept("Connected to the game on server: " + getPort() + "\n\n");
                scores.accept("Top 3 Scores on the Server: " + getPort() + "\n\n");

                while (true) {
                    Pair<String, Pair<Integer, ArrayList<String>>> dta = (Pair) in.readObject();
                    String user_choice = dta.getKey();
                    switch (user_choice) {
                        case "ConnectedPlayers": Pair<Integer, ArrayList<String>> subpair = dta.getValue();
                            player.accept(subpair.getKey());
                            break;
                        case "UpdatedBoard":
                        case "UpdatedBoardNoMoves":
                            handle_update_board(dta.getValue());
                            break;
                        case "Win":
                        case "Lost":
                        case "Tie":
                            handle_game_result(dta);
                            break;
                        case "Top3Scores": handle_scores(dta);
                    }
                }
            } catch (Exception e) {
                progress.accept("Game Server Closed\n Thank you for playing.");
            }
        }
    }
}
