/**
 * This class is where the entire game is played and the threads are
 * @author Palesa Khoali
 */

import java.util.Arrays;
import java.util.Comparator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.*;


public class TheGame implements Runnable{

    private final Object labelLock = new Object();

    /**
     * 1. Words begin to fall
     */
    private Threads[] wordThreads;

    private WordRecord word;

    private boolean inplay;

    public boolean inPlay() {
        return inplay;
    }

    public void startWords() {
        int index = 0;
        stopped = false;
        inplay = true;
        for (WordRecord word : WordApp.words) {
            wordThreads[index] = new Threads(word, this);
            new Thread(wordThreads[index]).start();
            index++;
        }
    }


    /**
     * 2. Pause the words
     */

    private boolean paused;

    public boolean pauseGame() {
        return paused;
    }

    public void changePause() {
        paused = !paused;
    }


    /**
     * 3. Game is stopped
     */

    private boolean stopped;

    public boolean stopped() {
        return stopped;
    }



    /**
     * 4. Appearance of the game changed
     */

    private boolean changed;

    public synchronized void setUnchanged() {
        changed = false;
    }

    public synchronized void setChanged() {
        changed = true;
    }

    public boolean isChanged() {
        return changed;
    }


    /**
     * 5. calling the super class
     */

    public TheGame() {
        super();
        this.wordThreads = new Threads[WordApp.words.length];
        stopped = true;
        paused = false;
        inplay = false;
    }

    /**
     * 6. Updating the score
     */

    public void updateScoreLabels() {
        synchronized (labelLock) {
            WordApp.labels[0].setText("Caught: " + WordApp.score.getCaught() + "    ");
            WordApp.labels[1].setText("Missed:" + WordApp.score.getMissed() + "    ");
            WordApp.labels[2].setText("Score:" + WordApp.score.getScore() + "    ");
            setChanged();
        }
    }

    public synchronized void missedWord() {
        WordApp.score.missedWord();
        updateScoreLabels();
        if (WordApp.score.getMissed()+WordApp.score.getCaught() >= WordApp.getTotalWords()) {
            for (Threads wordThread : wordThreads) {

                wordThread.word.resetWord();
            }
            stopped = true;
            inplay = false;
            setChanged();
            JOptionPane.showMessageDialog(WordApp.w, "Words Limit exceeded, Game over\n" +
                    "Your score was: " + WordApp.score.getScore() +
                    "\nYou caught " + WordApp.score.getCaught() + " word(s)." +
                    "\nYou missed " + WordApp.score.getMissed() + " word(s)." );

            WordApp.score.resetScore();
            updateScoreLabels();
            WordApp.w.setup();
        }
    }


    /**
     *  7. Resetting game when game is over
     */

    public void gameOver() {
        //stopGame();
        for (Threads wordThread : wordThreads) {

            wordThread.word.resetWord();
        }
        stopped = true;
        inplay = false;
        setChanged();
        JOptionPane.showMessageDialog(WordApp.w, "Game Over 1!\n" +
                "Your score was: " + WordApp.score.getScore() +
                "\nYou caught " + WordApp.score.getCaught() + " word(s)." +
                "\nYou missed " + WordApp.score.getMissed() + " word(s)." );
        WordApp.score.resetScore();
        updateScoreLabels();
        WordApp.w.setup();
    }

    public synchronized void reset() {
        word.resetWord();
    }

    public void endGame()   {
        for (Threads wordThread : wordThreads) {

            wordThread.word.resetWord();
        }
        stopped = true;
        inplay = false;
        WordApp.score.resetScore();
        updateScoreLabels();
        WordApp.w.setup();
    }

    public synchronized boolean checkWord(String text) {
        Arrays.sort(WordApp.words, new Comparator<WordRecord>() {
            @Override
            public int compare(WordRecord word1, WordRecord word2) {
                if (word1.equals(word2)) {
                    return 0;
                }
                if (word1.getY() > word2.getY()) {
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


    /**
     * 7. Implements Threads
     */
    public class Threads implements Runnable {

        private WordRecord word;
        private TheGame theGame;

        public Threads(WordRecord word, TheGame theGame) {
            super();
            this.word = word;
            this.theGame = theGame;
        }

        public synchronized void reset() {
            word.resetWord();
        }

        public void run() {

            while (!theGame.stopped()) {
                if (word.missed()) {
                    theGame.missedWord();
                    word.resetWord();
                    //theGame.setChanged();
                } else if (theGame.pauseGame()) {
                    continue;
                } else {
                    word.drop(1);
                    theGame.updateScoreLabels();
                }
                try {
                    Thread.sleep(word.getSpeed() / 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
    }

}

