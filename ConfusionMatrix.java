/**
 * Tracks the confusion matrix object that stores how well the model predicted the data given.
 *
 * @author 726685gc George Clarke
 */
public class ConfusionMatrix {
    private int trueNegs;
    private int truePos;
    private int falseNegs;
    private int falsePos;

    /**
     * Defines the Confusion Matrix and its initial values for every possible 2-ary tuple
     * of the truth value of the predicted and the truth value of the actual, where true/false refers
     * to whether the document is spam or not.
     */
    public ConfusionMatrix() {
        this.trueNegs = 0;
        this.truePos = 0;
        this.falseNegs = 0;
        this.falsePos = 0;
    }

    /**
     * Gives the value stored for number of predicted no spam and actually no spam
     *
     * @return the value of predicted no spam and actual no spam.
     */
    public int getTrueNegatives() {
        return this.trueNegs;
    }

    /**
     * Gives the value stored for number of predicted spam and actually spam
     *
     * @return the value of predicted spam and actual spam.
     */
    public int getTruePositives() {
        return this.truePos;
    }

    /**
     * Gives the value stored for number of predicted no spam and actually spam
     *
     * @return the value of predicted no spam and actual spam.
     */
    public int getFalseNegatives() {
        return this.falseNegs;
    }

    /**
     * Gives the value stored for number of predicted spam and actually no spam
     *
     * @return the value of predicted spam and actual no spam.
     */
    public int getFalsePositives() {
        return this.falsePos;
    }

    /**
     * Increases the value of predicted no spam actual no spam by 1
     */
    public void updateTrueNegative() {
        this.trueNegs += 1;
    }

    /**
     * Increases the value of predicted spam actual spam by 1
     */
    public void updateTruePositive() {
        this.truePos += 1;
    }

    /**
     * Increases the value of predicted no spam actual spam by 1
     */
    public void updateFalseNegative() {
        this.falseNegs += 1;
    }

    /**
     * Increases the value of predicted spam actual no spam by 1
     */
    public void updateFalsePositive() {
        this.falsePos += 1;
    }

}