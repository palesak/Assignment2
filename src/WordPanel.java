import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;
public class WordPanel extends JPanel implements Runnable {
        private WordRecord[] words;
        private int noWords;
        private int maxY;
        private TheGame theGame;

        public void paintComponent(Graphics g) {
            int width = getWidth();
            int height = getHeight();
            g.clearRect(0, 0, width, height);
            g.setColor(Color.red);
            g.fillRect(0, maxY - 10, width, height);

            g.setColor(Color.black);
            g.setFont(new Font("Helvetica", Font.PLAIN, 26));
            //draw the words
            //animation must be added
            for (int i = 0; i < noWords; i++) {
                // Some words stuck out.
                g.drawString(words[i].getWord(), words[i].getX(), words[i].getY() - 5);
            }

        }

        public WordPanel(WordRecord[] words, int maxY, TheGame theGame) {
            this.words = words; //will this work?
            noWords = words.length;
            this.maxY = maxY;
            this.theGame = theGame;
        }

        public void run() {
            //Starts the game by generating the words and assigning them to WordThread objects.
            theGame.startWords();

            //Loops until the game is ended.
            while (!theGame.ended()) {
                //Repaints if the model has changed.
                if (theGame.isChanged()) {
                    repaint();
                    theGame.setUnchanged();
                }
                //Makes the thread sleep for 2ms to prevent a large number of unnecessary calls.
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void repaintOnce() {
            repaint();
        }
    }
