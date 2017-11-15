package presentation.utils;

import domain.controller.DomainController;
import domain.model.exceptions.CommandInterruptException;
import domain.model.exceptions.FinishGameException;

import java.util.Scanner;

/**
 * The type Terminal controller.
 */
public class TerminalUtils {
    private static final TerminalUtils mInstance = new TerminalUtils();
    private final Scanner scanner = new Scanner(System.in);

    private TerminalUtils() {
        // Empty Constructor
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static TerminalUtils getInstance() {
        return mInstance;
    }

    /**
     * Print line.
     *
     * @param string the string
     */
    public void printLine(String string) {
        System.out.println(string);
        System.out.flush();
    }

    /**
     * Error line.
     *
     * @param string the string
     */
    public void errorLine(String string) {
        TerminalMenuBuilder builder = new TerminalMenuBuilder();
        builder.addTitle("Mastermind: Error");
        builder.addDescription(string);
        builder.addOption(Constants.BACK, builder::finishExecution);
        builder.execute();
    }

    /**
     * Read string string.
     *
     * @return the string
     */
    public String readString() {
        return scanner.next();
    }

    /**
     * Read integer integer.
     *
     * @return the integer
     */
    public Integer readInteger() {
        try {
            return Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Read game integer.
     *
     * @return the integer
     */
    public Integer readGameInteger() throws FinishGameException, CommandInterruptException {
        String string = readString();
        if (executeCommands(string)) throw new CommandInterruptException();
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String outputTimestamp(long time) {
        long elapsed = time / 1000;
        int hours = (int) (elapsed / (3600));
        int minutes = (int) ((elapsed - (hours * 3600)) / 60);
        int seconds = (int) (elapsed - (hours * 3600) - minutes * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private boolean executeCommands(String token) throws FinishGameException {
        switch (token.toLowerCase()) {
            case "hint":
            case "help":
            case "ajuda":
                DomainController.getInstance().getGameController().provideHelp();
                return true;
            case "save":
            case "guardar":
            case "guarda":
                DomainController.getInstance().getGameController().saveCurrentGame();
                return true;
            case "exit":
            case "surt":
                DomainController.getInstance().getGameController().stopCurrentGame();
                return true;
        }
        return false;
    }
}