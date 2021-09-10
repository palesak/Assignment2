/**
 * This is where the app comes together, the buttons are drawn and the vistuals are drawn
 *
 * @author Palesa Khoali
 * 10 September 2021
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class WordApp {
    static int noWords = 4;
    public static int totalWords;

    static int frameX = 1000;
    static int frameY = 600;
    static int yLimit = 480;

    static WordDictionary word = new WordDictionary();
    static WordRecord[] words;
    static boolean done;
    static Score score = new Score();

    static WordPanel w;
    static JLabel[] labels;
    static TheGame theGame;


    public static void setupGUI(int frameX, int frameY, int yLimit) {

        JFrame frame = new JFrame("WordGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameX, frameY);

        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setSize(frameX,frameY);


        w = new WordPanel(words, yLimit, theGame);
        w.setSize(frameX, yLimit + 100);
        g.add(w);


        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));


        JLabel caught = new JLabel("Caught: " + score.getCaught() + "    ");
        JLabel missed = new JLabel("Missed:" + score.getMissed() + "    ");
        JLabel scr = new JLabel("Score:" + score.getScore() + "    ");

        labels = new JLabel[] {caught, missed, scr};

        txt.add(caught);
        txt.add(missed);
        txt.add(scr);

        final JTextField textEntry = new JTextField("", 20);
        textEntry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!theGame.pauseGame()) {
                    String text = textEntry.getText();
                    if (!theGame.checkWord(text)) {
                        theGame.setChanged();
                    }
                }
                textEntry.setText("");
                textEntry.requestFocus();
            }
        });

        txt.add(textEntry);
        txt.setMaximumSize(txt.getPreferredSize());
        g.add(txt);

        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
        JButton startB = new JButton("Start");


        startB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!theGame.inPlay()) {
                    new Thread(w).start();
                } else if (theGame.pauseGame()) {
                    theGame.changePause();
                }
                textEntry.requestFocus();
            }
        });

        JButton pauseB = new JButton("Pause");


        pauseB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (theGame.inPlay()) {
                    theGame.changePause();
                }
                textEntry.setText("");
                textEntry.requestFocus();
            }
        });

        JButton resetB = new JButton("Reset");


        resetB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (theGame.inPlay()) {
                    theGame.endGame();
                }
            }
        });

        JButton quitB = new JButton("Quit");


        quitB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (theGame.inPlay()) {
                    theGame.endGame();
                }
                System.exit(0);
            }
        });

        b.add(startB);
        b.add(pauseB);
        b.add(resetB);
        b.add(quitB);

        g.add(b);

        frame.setLocationRelativeTo(null);
        frame.add(g);
        frame.setContentPane(g);
        frame.setVisible(true);

    }

    public static int getTotalWords() {
        return totalWords;
    }

    public static String[] getDict(String filename)throws Exception {
        String[] str = null;
        Scanner reader = new Scanner(new FileInputStream(filename));
        int len = reader.nextInt();

        str = new String[len];
        for (int i = 0; i < len; i++) {
           str[i] = reader.next();
        }
        reader.close();

        return str;

    }

    public static void main(String[] args)throws Exception {

        done = true;

        totalWords = Integer.parseInt(args[0]);
        noWords = Integer.parseInt(args[1]);
        assert (totalWords >= noWords);
        String[] temp = getDict(args[2]);


        if (temp != null) {
            word = new WordDictionary(temp);
        }

        WordRecord.dictionary = word;

        words = new WordRecord[noWords];

        int x_inc = frameX / noWords;

        for (int i = 0; i < noWords; i++) {
            words[i] = new WordRecord(word.getNewWord(), i * x_inc, yLimit);
        }

        theGame = new TheGame();

        setupGUI(frameX, frameY, yLimit);

    }
}