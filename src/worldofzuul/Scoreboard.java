package worldofzuul;

import java.util.ArrayList;
import worldofzuul.persistence.FileHandler;

public class Scoreboard {
    private ArrayList<Highscore> scores;
    private FileHandler fileHandler;

    public Scoreboard() {
        fileHandler = new FileHandler("src/worldofzuul/Names/highscores.csv");
    }
    
    public void load() {
        scores = fileHandler.loadScores();
    }
    
    public void save() {
        fileHandler.saveScores(scores);
    }
}
