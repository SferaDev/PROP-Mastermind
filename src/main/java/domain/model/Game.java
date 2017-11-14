package domain.model;

import domain.controller.StatController;
import domain.model.exceptions.*;
import domain.model.player.ComputerPlayer;
import domain.model.player.Player;
import domain.model.player.UserPlayer;
import domain.model.row.ColorRow;
import domain.model.row.ControlRow;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Game.
 */
public class Game implements java.io.Serializable {
    /**
     * It is an Status, that is an enumeration that could be: START, GUESS, CORRECT, CHECK STATUS, SCORE, CORRECT or FINISHED
     */
    private Status gameStatus;
    /**
     * It is the player (computer or user) that choose the combination to guess
     */
    private Player gameMaker;
    /**
     * It is the player (computer or user) that tries to guess the combination
     */
    private Player gameBreaker;
    /**
     * It is a class that contains information about the game
     */
    private GameInfo gameInfo;
    /**
     * It is the combination decided by the Maker
     */
    private ColorRow correctGuess;
    /**
     * It is an ArrayList that contains all the combinations tried by the Breaker
     */
    private ArrayList<ColorRow> mGuess = new ArrayList<>();
    /**
     * It is an ArrayList that contains all the control combinations given by the Maker
     */
    private ArrayList<ControlRow> mControl = new ArrayList<>();
    /**
     * It is the number of turns the Breaker have used
     */
    private int gameTurn = 1;

    /**
     * Instantiates a new Game.
     * Depending the Role, set the player attributes
     * Puts the game in stat START.
     * @param user1 the user 1 is the user
     * @param user2 the user 2 is the computer
     * @param info  is a class GameInfo and contains the user, the role, the number of pegs, the number of colors, the number of turns, the date and the time spent.
     *
     * @pre user1 and user2 roles are opposite
     *
     */
    public Game(Player user1, Player user2, GameInfo info) {
        // Depending the Role, set the player attributes
        switch (user1.getPlayerRole()) {
            case MAKER:
                gameMaker = user1;
                gameBreaker = user2;
                break;
            case BREAKER:
                gameBreaker = user1;
                gameMaker = user2;
                break;
        }

        gameInfo = info;
        gameStatus = Status.START;
    }

    /**
     * Start game.
     */
    public void startGame() throws FinishGameException {
        while (gameStatus != Status.CORRECT && gameStatus != Status.FINISHED) {
            switch (gameStatus) {
                case START:
                    try {
                        actionStart();
                    } catch (InterruptedException ignored) {}
                    break;
                case GUESS:
                    try {
                        actionGuess();
                    } catch (InterruptedException ignored) {}
                    break;
                case CONTROL:
                    try {
                        actionControl();
                    } catch (InterruptedException ignored) {}
                    break;
                case CHECK:
                    actionCheck();
                    break;
                case SCORE:
                    actionScore();
                    break;
            }
        }
    }

    private void actionScore() {
        if (gameBreaker instanceof UserPlayer) {
            // Notify the breaker his score
            int score = ((int) Math.pow(gameInfo.mColors, gameInfo.mPegs))/ gameTurn;
            gameBreaker.notifyScore(score);
            StatController.getInstance().addScore(gameInfo.mUser, gameInfo.getGameTitle(),
                    score, gameInfo.getElapsedTime());
        }
        gameStatus = Status.CORRECT;
    }

    private void actionCheck() {
        // Update gameStatus
        if (mGuess.get(mGuess.size() - 1).equals(correctGuess)) {
            gameStatus = Status.SCORE;
        } else if ((gameTurn + 1) >= gameInfo.mTurns) {
            gameStatus = Status.FINISHED;
        } else {
            gameTurn++;
            gameStatus = Status.GUESS;
        }
    }

    private void actionControl() throws FinishGameException, InterruptedException {
        // Ask Maker for a control of the input guess
        ControlRow control = gameMaker.scoreGuess(mGuess.get(mGuess.size() - 1));
        ControlRow correctControl = ComputerPlayer.compareGuess(correctGuess, mGuess.get(mGuess.size() - 1));

        // If Maker is lying, notify him
        if (!correctControl.equals(control)) {
            gameMaker.notifyInvalidControl();
        }

        // Send Breaker the correct control and store it
        gameBreaker.receiveControl(correctControl);
        mControl.add(correctControl);
        gameStatus = Status.CHECK;
    }

    private void actionGuess() throws FinishGameException, InterruptedException {
        // Ask Breaker an input guess and store it
        ColorRow input = gameBreaker.breakerGuess(gameInfo.mPegs, gameInfo.mColors);
        if (ColorRow.isValid(input, gameInfo.mPegs, gameInfo.mColors)) {
            mGuess.add(input);
            gameStatus = Status.CONTROL;
        } else {
            gameBreaker.notifyInvalidInput();
        }
    }

    private void actionStart() throws FinishGameException, InterruptedException {
        // Ask Maker the correct guess
        ColorRow input = gameMaker.makerGuess(gameInfo.mPegs, gameInfo.mColors);
        if (ColorRow.isValid(input, gameInfo.mPegs, gameInfo.mColors)) {
            correctGuess = input;
            gameStatus = Status.GUESS;
        } else {
            gameMaker.notifyInvalidInput();
        }
    }

    /**
     * Gets the game status.
     * @return the game status
     */
    public String getGameStatus() {
        return gameStatus.toString();
    }

    /**
     * Gets game title.
     * @return the game title
     */
    public String getGameTitle() {
        return gameInfo.getGameTitle();
    }

    /**
     * Finish game, set the status FINISHED
     */
    public void finishGame() {
        gameStatus = Status.FINISHED;
    }

    /**
     * Provides help to the user depending in the status; if the status is GUESS gives a combination to help to guess the correct combination
     * and if the status is CONTROL gives the correct control answer
     */
    public void provideHelp() {
        switch (gameStatus) {
            case GUESS:
                if (mControl.size() == 0) return;
                gameBreaker.notifyHint(ComputerPlayer.guessHelp(correctGuess, mControl.get(mControl.size() - 1),
                        gameInfo.mPegs, gameInfo.mColors));
            case CONTROL:
                if (mGuess.size() == 0) return;
                gameMaker.notifyHint(ComputerPlayer.compareGuess(correctGuess, mGuess.get(mGuess.size() - 1)));
        }
    }

    /**
     * Prepare save with the correct time spent in the game
     */
    public void prepareSave() {
        gameInfo.mTotalTime += gameInfo.getElapsedTime();
    }

    /**
     * Set the actual date in mStart
     */
    public void updateStart() {
        gameInfo.updateStart();
    }

    /**
     * The enum Status.
     */
    public enum Status {
        /**
         * Start status: The Maker decides the combination
         */
        START,
        /**
         * Guess status: The Breaker tries a combination
         */
        GUESS,
        /**
         * Control status: The Maker answers with a control combination
         */
        CONTROL,
        /**
         * Check status: The status is checked, it could be: FINISHED, SCORE or GUESS
         */
        CHECK,
        /**
         * Score status: A punctuation is given to the Breaker
         */
        SCORE,
        /**
         * Correct status: The breaker wins
         */
        CORRECT,
        /**
         * Finished status: The breakers finished the game because exceeds the number of turns
         */
        FINISHED
    }

    /**
     * It is the game information
     */
    public static class GameInfo implements java.io.Serializable {
        private String mUser;
        private Player.Role mRole;
        private int mPegs, mColors, mTurns;
        private Date mStart = new Date();
        private long mTotalTime = 0;

        /**
         * Instantiates a new Game info.
         *
         * @param user   the user that is playing the game
         * @param role   the role of the user
         * @param pegs   the number of pegs in a combination
         * @param colors the number of possible colors in a combination
         * @param turns  the number of maxim turns in the game
         */
        public GameInfo(String user, Player.Role role, int pegs, int colors, int turns) {
            mUser = user;
            mRole = role;
            mPegs = pegs;
            mColors = colors;
            mTurns = turns;
        }

        /**
         * Gets the game title.
         *
         * @return the game title
         */
        String getGameTitle() {
            return mUser + "-" + mRole + "-" + mPegs + "-" + mColors + "-" + mStart;
        }

        /**
         * Gets elapsed time in ms.
         *
         * @return the elapsed time
         */
        long getElapsedTime() {
            return mTotalTime + (new Date().getTime() - mStart.getTime());
        }

        private void updateStart() {
            mStart = new Date();
        }
    }
}
