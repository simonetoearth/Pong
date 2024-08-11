package edu.sdccd.cisc191;

public class LastGameScore {
    private int playerScore;
    private int aiScore;

    public LastGameScore(int playerScore, int aiScore) {
        this.playerScore = playerScore;
        this.aiScore = aiScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getAiScore() {
        return aiScore;
    }

    @Override
    public String toString() {
        return "Player Score: " + playerScore + ", AI Score: " + aiScore;
    }
}
