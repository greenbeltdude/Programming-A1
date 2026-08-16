import java.io.*;
import java.util.ArrayList;

/**
 * This class trains the model using a train data file, tests it using a test data file, constructing a Confusion
 * matrix by classifying the given documents depending on their predicted and their actual spam truthfulness.
 *
 * @author 726685gc George Clarke
 */
public class NaiveBayes {
    private ArrayList<WordCounter> wordCounterList;
    private int totalNumberSpam = 0;
    private int totalNumberNoSpam = 0;
    private int totalNumberDocuments = 0;


    /**
     * This constructor stores the focus words that the model will use to detect spam, and
     * stores them in wordcounter objects in the word counter list.
     *
     * @param focusWords is the array of focuswords that the model will use to detect spam.
     */
    public NaiveBayes(String[] focusWords) {
        this.wordCounterList = new ArrayList<>();

        for (String fw : focusWords) {
            WordCounter wc = new WordCounter(fw);
            this.wordCounterList.add(wc);
        }
    }

    /**
     * For every WordCounter object in wordCounterList, it runs the class specific add sample,
     * as defined in the WordCounter class (adds the sample and records focusWord occurrences).
     *
     * @param document is the string that will be added as a sample.
     */
    public void addSample(String document) {
        for (WordCounter wc : this.wordCounterList) {
            wc.addSample(document);
        }

        String[] docArray = document.split(" ");
        this.totalNumberDocuments += 1;
        if (docArray[0].equals("1")) {
            this.totalNumberSpam += 1;
        }
        else {
            this.totalNumberNoSpam += 1;
        }

    }

    /**
     * Uses the training of the model to classify a new document, returning its prediction of whether
     * it is spam or not.
     *
     * @param unclassifiedDocument is the document which is not known to be spam or not.
     * @return whether probability of spam is higher than probability of no spam.
     */
    public boolean classify(String unclassifiedDocument) {
        // Initializes probabilities
        double probSpam = (double) this.totalNumberSpam / this.totalNumberDocuments;
        double probNoSpam = (double) this.totalNumberNoSpam / this.totalNumberDocuments;

        // Iterates over the words in uD and word counters, updating the probabilities.
        String[] uD = unclassifiedDocument.split(" ");
        for (String word:uD) {
            for (WordCounter wc:this.wordCounterList) {
                String focusWord = wc.getFocusWord();
                if (focusWord.equals(word)) {
                    probSpam = probSpam * wc.getConditionalSpam();
                    probNoSpam = probNoSpam * wc.getConditionalNoSpam();
                }
            }
        }
        return probNoSpam < probSpam;
    }

    /**
     * Reads through the training file and adds it as samples to all the WordCounter objects.
     *
     * @param trainingFile the file with the data that the model is trained on.
     * @throws IOException if the file is not found or other file related issue occurs.
     */
    public void trainClassifier(File trainingFile) throws IOException {
        ArrayList<String> docInFileList = fileToStringList(trainingFile);

        for (String document:docInFileList) {
            this.addSample(document);
        }
    }

    /**
     * Takes a file of document strings which we don't know are spam or not and predicts if they are spam.
     * Appends a file with the predictions for each doc.
     *
     * @param input the input file which is going to have every line be predicted either spam or not.
     * @param output the file which will store the predictions for every document string in input.
     * @throws IOException if the file is not found or other file related issue occurs.
     */
    public void classifyFile(File input, File output) throws IOException {
        ArrayList<String> docsToClassify = fileToStringList(input);

        ArrayList<String> classificationList = new ArrayList<>();

        for (String doc:docsToClassify) {
            if (this.classify(doc)) {
                classificationList.add("1");
            }
            else {
                classificationList.add("0");
            }
        }

        printListToFile(classificationList,output);
    }

    /**
     * Returns a confusion matrix with the proportions it got right and the proportions it got wrong
     * when predicting the test data.
     *
     * @param testdata the file of document strings it will predict to generate the confusion matrix.
     * @return the confusion matrix with the correctness proportions of the prediction of the testing data.
     * @throws IOException if the test data file not found or some other file related issue occurs.
     */
    public ConfusionMatrix computeAccuracy(File testdata) throws IOException {
        ConfusionMatrix cm = new ConfusionMatrix();
        ArrayList<String> testDataList = fileToStringList(testdata);
        ArrayList<String> trueValList = trueValueList(testDataList);
        ArrayList<String> strippedList = strippedStringList(testDataList);
        ArrayList<String> predictedValList = getClassifyList(strippedList);

        for (int val = 0; val < trueValList.size(); val++) {
            String actual = trueValList.get(val);
            String predicted = predictedValList.get(val);
            if (actual.equals(predicted)) {
                if (predicted.equals("1")) {
                    cm.updateTruePositive();
                }
                else {
                    cm.updateTrueNegative();
                }
            }
            else {
                if (predicted.equals("1")) {
                    cm.updateFalsePositive();
                }
                else {
                    cm.updateFalseNegative();
                }
            }
        }
        return cm;
    }

    private static ArrayList<String> fileToStringList(File file) throws IOException {
        ArrayList<String> stringList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                stringList.add(line);
                line = br.readLine();
            }
        }

        return stringList;
    }

    private static String stripString(String input) {
        String[] inputArray = input.split(" ");
        String strippedInput = inputArray[1];
        for (int s = 2; s < inputArray.length; s++) {
            strippedInput += (" " + inputArray[s]);
        }
        return strippedInput;
    }

    private static ArrayList<String> strippedStringList(ArrayList<String> testDataList) {
        ArrayList<String> strippedList = new ArrayList<>();
        for (String s:testDataList) {
            strippedList.add(stripString(s));
        }
        return strippedList;
    }

    private static ArrayList<String> trueValueList(ArrayList<String> testDataList) {
        ArrayList<String> trueValList = new ArrayList<>();
        for (String t:testDataList) {
            String[] stringArray = t.split(" ");
            trueValList.add(stringArray[0]);
        }
        return trueValList;
    }

    private static void printListToFile(ArrayList<String> input, File output)
    throws IOException {

        try (PrintWriter pw = new PrintWriter(output)) {
            for (String s:input) {
                pw.println(s);
            }
            pw.flush();
        }
    }

    private ArrayList<String> getClassifyList(ArrayList<String> stringList) {
        ArrayList<String> classifyList = new ArrayList<>();
        for (String s: stringList) {
            boolean isSpam = this.classify(s);
            if (isSpam) {
                classifyList.add("1");
            }
            else {
                classifyList.add("0");
            }
        }
        return classifyList;
    }

}