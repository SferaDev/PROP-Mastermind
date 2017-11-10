package domain;

/**
 * The interface Input output.
 */
public interface InputOutput {
    /**
     * Input control blacks int.
     *
     * @param pegs the pegs
     * @return the int
     */
// Inputs
    int inputControlBlacks(int pegs);

    /**
     * Input control whites int.
     *
     * @param pegs the pegs
     * @return the int
     */
    int inputControlWhites(int pegs);

    /**
     * Input color row int [ ].
     *
     * @param pegs   the pegs
     * @param colors the colors
     * @return the int [ ]
     */
    int[] inputColorRow(int pegs, int colors);

    /**
     * Output control row.
     *
     * @param blacks the blacks
     * @param whites the whites
     */
// Outputs
    void outputControlRow(int blacks, int whites);

    /**
     * Output color row.
     *
     * @param row the row
     */
    void outputColorRow(String row);

    /**
     * Output message.
     *
     * @param message the message
     */
    void outputMessage(String message);

    /**
     * Notify invalid input.
     */
    void notifyInvalidInput();

    /**
     * Notify score.
     *
     * @param score the score
     */
    void notifyScore(int score);

}
