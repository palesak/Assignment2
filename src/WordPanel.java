import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.*;


public class WordPanel extends JPanel implements Runnable {
        private int noWords;
        private int maxY;
        private TheGame theGame;
        private WordRecord[] words;

        public void setup() {
            repaint();
        }


        public void paintComponent(Graphics g) {
            int width = getWidth();
            int height = getHeight();
            g.clearRect(0, 0, width, height);
            g.setColor(Color.pink);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.red);
            g.fillRect(0, maxY - 10, width, height);

            g.setColor(Color.black);
            g.setFont(new Font("Helvetica", Font.PLAIN, 26));
            for (int i = 0; i < noWords; i++) {
                g.drawString(words[i].getWord(), words[i].getX(), words[i].getY() - 5);
            }
        }

        public WordPanel(WordRecord[] words, int maxY, TheGame theGame) {
            this.words = words;
            noWords = words.length;
            this.maxY = maxY;
            this.theGame = theGame;
        }

        public void run() {

            theGame.startWords();

            while (!theGame.stopped()) {
                if (theGame.isChanged()) {
                    repaint();
                    theGame.setUnchanged();
                }

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
