
/**
 * Created by Jethro Muller on 2014/08/20.
 * Threads the WordRecord objects to allow for concurrent access and movement.
 */
public class WordThread implements Runnable {
    private WordRecord word;
    private TheGame theGame;

    public WordThread(WordRecord word, TheGame theGame) {
        super();
        this.word = word;
        this.theGame = theGame;
    }

    public synchronized void reset() {
        word.resetWord();
    }

    @Override
    public void run() {
        //If the game is still being played
        while (!theGame.ended()) {
            // if the word was missed.
            if (word.missed()) {
                theGame.missedWord();
                word.resetWord();
                theGame.setChanged();
                //If the game is paused, don't do anything.
            } else if (theGame.isPaused()) {
                continue;
                //The word wasn't missed and the game isn't paused
                //Drop the word and update the scores.
            } else {
                word.drop(1);
                theGame.updateScoreLabels();
            }
            try {
                //Waits until the next movement time.
                Thread.sleep(word.getSpeed() / 15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}