import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;
import javafx.util.Pair;

public class Server {
    private  int port;
    private int count = 0;
    private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    private ArrayList<String> client_id = new ArrayList<>();
    private String comp_level;
    private ArrayList<Game> games = new ArrayList<Game>();

    private _server server;
    private Consumer<Serializable> callback;

    Server(int port, Consumer<Serializable> call) {
        this.port = port;
        callback = call;
        server = new _server();
        server.start();
    }

    public int getPort() {
        return this.port;
    }

    public class _server extends Thread {
        public void run() {

            try (ServerSocket my_socket = new ServerSocket(Server.this.port);) {
                System.out.println("Waiting for a client");

                while(true) {
                    ClientThread ct = new ClientThread(my_socket.accept(), clients.size());

                    clients.add(ct);
                    client_id.add(Integer.toString(ct.count));

                    callback.accept(new GameInfo("UpdatePlayers", clients, comp_level));
                    ct.start();

                    ct.update_client(new Pair("ConnectedPlayer", clients));
                    Game g = new Game(count);
                    games.add(g);

                    count++;
                }
            } catch (Exception e) {
                callback.accept("Server did not launch");
            }
        }
    }

    class ClientThread extends Thread {
        Socket connection;
        int count;
        boolean in_game;
        ObjectOutputStream out;
        ObjectInputStream in;

        ClientThread(Socket soc, int count) {
            this.connection = soc;
            this.count = count;
            this.in_game = false;
        }

        void update_client(Pair pair) {
            for (ClientThread client : clients) {
                if (client == null) {
                    continue;
                }
                try {
                    client.out.writeObject(new Pair(pair.getKey(), new Pair(client.count, pair.getValue())));
                    client.out.reset();
                } catch (Exception e) {

                }
            }
        }

        String winnerIs(ArrayList<String> gameBoard) {


            return "";
        }

        boolean check_for_winner(int player_id, Pair<String, ArrayList<String>> s_pair) {

            return true;
        }

        void _comp_level(Pair dta) {
            int player_id = (Integer) dta.getKey();
            Pair<String, String> s_pair = (Pair<String, String>) dta.getValue();
            games.get(player_id).comp_level = s_pair.getValue();
        }

        void _game_board_update() {

        }

        void _play_again(Pair dta) {

        }
    }
}
