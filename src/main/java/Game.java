import java.util.ArrayList;

public class Game {

    String comp_level;
    int player_id;
    ArrayList<String> b_state;
    int high_score;

    public String getComp_level() {
        return comp_level;
    }

    public int getHigh_score() {
        return high_score;
    }

    public void setComp_level(String comp_level) {
        this.comp_level = comp_level;
    }

    public void setHigh_score(int high_score) {
        this.high_score = high_score;
    }

    Game(int player_id) {
        this.player_id = player_id;
    }
}
