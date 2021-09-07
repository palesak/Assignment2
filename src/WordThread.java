
/*public class WordThread implements Runnable {

    private WordRecord word;
    private WordController wordController;

    public WordThread(WordRecord word, WordController wordController) {
        super();
        this.word = word;
        this.wordController = wordController;
    }


    public synchronized void reset() {
        word.resetWord();
    }


    @Override
    public void run() {
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
    }
}*/
