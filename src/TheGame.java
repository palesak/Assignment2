import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;

public class TheGame {
    private WordThread[] wordThreads;

    private volatile boolean ended;

    private volatile boolean paused;

    private volatile boolean changed;

    private volatile boolean running;

    private final Object labelLock = new Object();

    public TheGame() {
        super();
        this.wordThreads = new WordThread[WordApp.words.length];
        ended = true;
        paused = false;
        running = false;
    }

    public void updateScoreLabels() {
        synchronized (labelLock) {
            WordApp.labels[0].setText("Caught: " + WordApp.score.getCaught() + "    ");
            WordApp.labels[1].setText("Missed:" + WordApp.score.getMissed() + "    ");
            WordApp.labels[2].setText("Score:" + WordApp.score.getScore() + "    ");
            setChanged();
        }
    }

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
                    gameOver();
                }
                return true;
            }
        }
        return false;
    }

    public void startWords() {
        ended = false;
        running = true;
        int index = 0;
        for (WordRecord word : WordApp.words) {
            wordThreads[index] = new WordThread(word, this);
            new Thread(wordThreads[index]).start();
            index++;
        }
    }

    public synchronized void missedWord() {
        WordApp.score.missedWord();
        updateScoreLabels();
        if (WordApp.score.getMissed()+WordApp.score.getCaught() >= WordApp.getTotalWords()) {
            stopGame();
            setChanged();
            JOptionPane.showMessageDialog(WordApp.w, "Game Over 2!\n" +
                    "Your score was: " + WordApp.score.getScore() +
                    "\nYou caught " + WordApp.score.getCaught() + " word(s)." +
                    "\nYou missed " + WordApp.score.getMissed() + " word(s)." );

            resetScore();
            WordApp.w.repaintOnce();
        }
    }

    public void resetScore() {
        WordApp.score.resetScore();
        updateScoreLabels();
    }

    public void stopGame() {
        for (WordThread wordThread : wordThreads) {
            wordThread.reset();
        }
        ended = true;
        running = false;
    }

    public void endGame() {
        stopGame();
        resetScore();
        WordApp.w.repaintOnce();
    }

    public boolean ended() {
        return ended;
    }

    public void gameOver() {
        stopGame();
        setChanged();
        JOptionPane.showMessageDialog(WordApp.w, "Game Over 1!\n" +
                "Your score was: " + WordApp.score.getScore() +
                "\nYou caught " + WordApp.score.getCaught() + " word(s)." +
                "\nYou missed " + WordApp.score.getMissed() + " word(s)." );
        resetScore();
        WordApp.w.repaintOnce();
    }

    public void setPaused() {
        paused = !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isChanged() {
        return changed;
    }

    public synchronized void setUnchanged() {
        changed = false;
    }

    public synchronized void setChanged() {
        changed = true;
    }

    public boolean isRunning() {
        return running;
    }
}