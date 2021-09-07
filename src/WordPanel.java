import javax.swing.*;
import java.awt.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
    public static volatile boolean done;
    private WordRecord[] words;
    private int noWords;
    private int maxY;
    private WordController wordController;
    private WordRecord word;
    //private WordController wordController;

    /*public void WordThread(WordRecord word, WordController wordController) {
        super();
        this.word = word;
        this.wordController = wordController;
    }*/


    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.clearRect(0,0,width,height);
        g.setColor(Color.red);
        g.fillRect(0,maxY-10,width,height);

        //g.setColor(Color.green);
       // g.fillRect(maxY,0,width,height);

        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 26));
        //draw the words
        //animation must be added
        for (int i=0;i<noWords;i++){
            //g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
            g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()-2);  //y-offset for skeleton so that you can see the words
        }

    }


    public WordPanel(WordRecord[] words, int maxY, WordController wordController) {
        this.words = words; //will this work?
        noWords = words.length;
        this.maxY = maxY;
        this.wordController = wordController;
    }

    public synchronized void reset() {
        word.resetWord();
    }



    /*public void run() {
        //If the game is still being played
        while (!wordController.ended()) {
            // if the word was missed.
            if (word.missed()) {
                wordController.missedWord();
                word.resetWord();
                wordController.setChanged();
                //If the game is paused, don't do anything.
            } else if (wordController.isPaused()) {
                continue;
                //The word wasn't missed and the game isn't paused
                //Drop the word and update the scores.
            } else {
                word.drop(1);
                wordController.updateScoreLabels();
            }
            try {
                //Waits until the next movement time.
                Thread.sleep(word.getSpeed() / 15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
    //@Override
    public void run() {
        //Starts the game by generating the words and assigning them to WordThread objects.
        wordController.startWords();

        //Loops until the game is ended.
        while (!wordController.ended()) {
            //Repaints if the model has changed.
            if (wordController.isChanged()) {
                repaint();
                wordController.setUnchanged();
            }
            else if (word.missed()) {
                wordController.missedWord();
                word.resetWord();
                wordController.setChanged();
                //If the game is paused, don't do anything.
            } else if (wordController.isPaused()) {
                continue;
                //The word wasn't missed and the game isn't paused
                //Drop the word and update the scores.
            } else {
                word.drop(1);
                wordController.updateScoreLabels();
            }
            //Makes the thread sleep for 2ms to prevent a large number of unnecessary calls.
            /*try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
    public void repaintOnce() {
        repaint();
    }
}