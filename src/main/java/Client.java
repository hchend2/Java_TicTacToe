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
        connectionThread.sockect.close();
    }
    public void sendData(Serializable data) throws Exception {
        connectionThread.out.writeObject(data);
        connectionThread.out.reset();
    }

    private void handle_update_board(Pair data) {
        newBoard.accept((int)data.getValue());
    }

    private void handle_game_result(Pair data) {

    }
}
