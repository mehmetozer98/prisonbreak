package worldofzuul;

public class Highscore {
    private int score;
    private String name;

    public Highscore(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
    
}
