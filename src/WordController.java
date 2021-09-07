import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;


public class WordController {
    private WordPanel[] wordThreads;

    /**
     * Whether or not the game has finished.
     */
    private volatile boolean ended;
    /**
     * Whether or not the game is paused.
     */
    private volatile boolean paused;
    /**
     * Whether or not there has been a change.
     */
    private volatile boolean changed;
    /**
     * Whether or not the game is running.
     */
    private volatile boolean running;

    private final Object labelLock = new Object();

    public WordController() {
        super();
        this.wordThreads = new WordPanel[WordApp.words.length];
        ended = true;
        paused = false;
        running = false;
    }

    /**
     * Updates the score labels on the WordPanel with the updated scores.
     */
    public void updateScoreLabels() {
        synchronized (labelLock) {
            WordApp.labels[0].setText("Caught: " + WordApp.score.getCaught() + "    ");
            WordApp.labels[1].setText("Missed:" + WordApp.score.getMissed() + "    ");
            WordApp.labels[2].setText("Score:" + WordApp.score.getScore() + "    ");

            setChanged();
        }
    }

    /**
     * Checks the word to see if it matches the provided text.
     *
     * @param text The text to check against.
     * @return boolean Whether or not the word matched.
     */
    public synchronized boolean checkWord(String text) {
        Arrays.sort(WordApp.words, new Comparator<WordRecord>() {
            @Override
            public int compare(WordRecord o1, WordRecord o2) {
                if (o1.equals(o2)) {
                    return 0;
                }
                if (o1.getY() > o2.getY()) {
                    return -1;
                }
                return 1;
            }
        });

        for (WordRecord word : WordApp.words) {
            if (word.matchWord(text)) {
                WordApp.score.caughtWord(text.length());
                updateScoreLabels();
                if (WordApp.score.getCaught() >= WordApp.totalWords) {
                    winGame();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Creates WordThread objects to manipulate the WordRecords in separate threads.
     */
    public void startWords() {
        ended = false;
        running = true;
        int index = 0;
        for (WordRecord word : WordApp.words) {
            wordThreads[index] = new WordPanel(word, this);
            new Thread(wordThreads[index]).start();
            index++;
        }
    }

    /**
     * Increments the missed word count and detects if it's gone over the limit.
     */
    public synchronized void missedWord() {
        WordApp.score.missedWord();
        updateScoreLabels();
        if (WordApp.score.getMissed() >= 10) {
            stopGame();
            setChanged();
            JOptionPane.showMessageDialog(WordApp.w, "Game Over!\n" +
                    "Your score was: " + WordApp.score.getScore() +
                    "\nYou caught " + WordApp.score.getCaught() + " word(s)." +
                    "\nYou missed " + WordApp.score.getMissed() + " word(s).");


            resetScore();
            WordApp.w.repaintOnce();
        }
    }

    /**
     * Resets the scores in the Score object.
     */
    public void resetScore() {
        WordApp.score.resetScore();
        updateScoreLabels();
    }

    /**
     * Stops the game. Stops all threads and sets the appropriate flags.
     */
    public void stopGame() {
        for (WordPanel wordThread : wordThreads) {
            wordThread.reset();
        }
        ended = true;
        running = false;
    }

    /**
     * Ends the game. By stopping the game, reseting the score and reseting the display.
     */
    public void endGame() {
        stopGame();
        resetScore();
        WordApp.w.repaintOnce();
    }

    /**
     * @return boolean indicating whether or not the game has ended.
     */
    public boolean ended() {
        return ended;
    }

    /**
     * Runs if the win condition is met.
     * Stops the game and displays a message.
     */
    public void winGame() {
        stopGame();
        setChanged();
        JOptionPane.showMessageDialog(WordApp.w, "Game Over!\n" +
                "Your score was: " + WordApp.score.getScore() +
                "\nYou caught " + WordApp.score.getCaught() + " word(s)." +
                "\nYou missed " + WordApp.score.getMissed() + " word(s).");
        resetScore();
        WordApp.w.repaintOnce();
    }

    /**
     * Sets the paused flag to the opposite of whatever it currently is.
     */
    public void setPaused() {
        paused = !paused;
    }

    /**
     * @return boolean indicating whether or not the game is paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return boolean indicating if the game's model has changed.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the changed flag to false.
     */
    public synchronized void setUnchanged() {
        changed = false;
    }

    /**
     * Sets the changed flag to true.
     */
    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * @return boolean indicating if the game is running.
     */
    public boolean isRunning() {
        return running;
    }
}