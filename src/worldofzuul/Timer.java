package worldofzuul;

import java.util.Scanner;

public class Timer {

    private int attemptTurns;
    private int maxTurns;

    public Timer(int maxTurns) {
        attemptTurns = 0;
        this.maxTurns = maxTurns;
    }

    public void countAttempt() {
        if (attemptTurns < maxTurns) {
            attemptTurns += 1;
        }
    }

    public boolean isMax() {
        if (attemptTurns == maxTurns) {
            return true;
        }
        return false;
    }

    public int getAttemptTurns() {
        return attemptTurns;
    }

    public String turnsToYears() {
        int months = attemptTurns;

        int years = months / 12;

        int remainingMonths = months % 12;

        return years + " year(s) and " + remainingMonths + " month(s)";

    }

    public int getMaxTurns() {
        return maxTurns;
    }
    
    

}
