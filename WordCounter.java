/**
 * This class tracks the number of words and instances of the focusWord in a given doc as well as
 * trains the model and calculates the probability of spam for a given document.
 *
 * @author 726685gc George Clarke
 */
public class WordCounter {
    private String focusWord;
    private int focusWordCountNoSpam = 0;
    private int focusWordCountSpam = 0;
    private int totalLengthNoSpam = 0;
    private int totalLengthSpam = 0;
    private int numberNoSpamDocs = 0;
    private int numberSpamDocs = 0;

    /**
     * Constructs the class object that tracks the occurrences of focusWord.
     *
     * @param focusWord is the word whose instances in the documents are being tracked.
     */
    public WordCounter(String focusWord) {
        this.focusWord = focusWord;
    }

    /**
     * Returns the string of the focus word.
     *
     * @return the focus word stored
     */
    public String getFocusWord() {
        return this.focusWord;
    }

    /**
     * Determines if the sample is true or not then counts every word except the identifier to the doc totals.
     *
     * @param document is the document being added as a sample
     */
    public void addSample(String document) {
       // Turn into a word Array
       String[] wordArray = document.split(" ");

       // Classification is true if it is not spam, false if it is spam.
       boolean classification = wordArray[0].equals("1");

       // Counting Occurrences of focusWord (not including the first element)
       int documentCount = 0;
       for (int i = 1; i < wordArray.length; i++) {
           if (wordArray[i].equals(this.focusWord)) {
               documentCount += 1;
           }
       }

        // Update the Word Number Trackers and the number of docs
        if (!classification) {
            this.totalLengthNoSpam += wordArray.length - 1;
            this.focusWordCountNoSpam += documentCount;
            this.numberNoSpamDocs += 1;
        }
        else {
            this.totalLengthSpam += wordArray.length - 1;
            this.focusWordCountSpam += documentCount;
            this.numberSpamDocs += 1;
        }
    }

    /**
     * Determines if the conditions for the model being trained are met.
     *
     * @return the truth value of whether the conditions are met or not
     */
    public boolean isCounterTrained() {
        boolean condition1 = this.focusWordCountNoSpam + this.focusWordCountSpam > 0;
        boolean condition2 = this.numberNoSpamDocs > 0;
        boolean condition3 = this.numberSpamDocs > 0;
        return condition1 && condition2 && condition3;
    }

    /**
     * Determines the probability of not spam after the model is trained.
     *
     * @return the probability of it not being spam.
     * @throws IllegalStateException if the Counter isn't trained
     */
    public double getConditionalNoSpam() throws IllegalStateException {
        if (!this.isCounterTrained()) {
            throw new IllegalStateException();
        }
        return (double) this.focusWordCountNoSpam / this.totalLengthNoSpam;

    }

    /**
     * Determines the probability of spam after the model is trained.
     *
     * @return the probability of it being spam
     * @throws IllegalStateException if the Counter isn't trained
     */
    public double getConditionalSpam() throws IllegalStateException {
        if (!this.isCounterTrained()) {
            throw new IllegalStateException();
        }
        return (double) this.focusWordCountSpam / this.totalLengthSpam;
    }

}