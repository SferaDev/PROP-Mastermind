package presentation.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import presentation.utils.LocaleUtils;
import presentation.view.*;
import presentation.view.components.ColorRow;
import presentation.view.components.ControlRow;

import java.net.URL;
import java.util.ResourceBundle;

public class NebulaViewController implements Initializable {
    @FXML
    private VBox navDrawer;
    @FXML
    private BorderPane mainContent;

    private BoardPane boardPane;
    private boolean isPlaying = false;

    private String username;

    private GameView currentGameView;
    private LoadView newGameView = new LoadView();
    private StatsView statsView = new StatsView();
    private UserOptionsView userOptionsView = new UserOptionsView();
    private HelpView helpView = new HelpView();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpDrawer();
        onNavigationDrawerClick("");
    }

    private void setUpDrawer() {
        navDrawer.getChildren().add(navDrawer.getChildren().size() - 1, createDrawerButton("PLAY"));
        navDrawer.getChildren().add(navDrawer.getChildren().size() - 1, createDrawerButton("STATS"));
        navDrawer.getChildren().add(navDrawer.getChildren().size() - 1, createDrawerButton("USER"));
        navDrawer.getChildren().add(navDrawer.getChildren().size(), createDrawerButton("HELP"));
        navDrawer.getChildren().add(navDrawer.getChildren().size(), createDrawerButton("LOG_OUT"));
    }

    private void onNavigationDrawerClick(String item) {
        switch (item) {
            default:
            case "PLAY":
                if (isPlaying) mainContent.setCenter(currentGameView);
                else mainContent.setCenter(newGameView);
                break;
            case "STATS":
                mainContent.setCenter(statsView);
                break;
            case "USER":
                mainContent.setCenter(userOptionsView);
                break;
            case "HELP":
                mainContent.setCenter(helpView);
                break;
            case "LOG_OUT":
                System.exit(0); // TODO: Should close and launch LoginView
                break;
        }
    }

    public void startGame(String role, String computer, int pegs, int colors) {
        isPlaying = true;
        boardPane = new BoardPane();
        currentGameView = new GameView(boardPane, role, computer, pegs, colors);
        mainContent.setCenter(currentGameView);
    }

    public void finishGame() {
        isPlaying = false;
        mainContent.setCenter(newGameView);
    }

    public void addControlLastTurn(int blacks, int whites) {
        boardPane.addControlRow(new ControlRow(blacks, whites));
    }

    public void addTurn(ColorRow colorRow) {
        boardPane.addColorRow(colorRow);
    }

    public void addActionPane(Node pane) {
        currentGameView.addActionPane(pane);
    }

    public void removeActionPane() {
        currentGameView.removeActionPane();
        // TODO: Spinner
        //currentGameView.addActionPane(new JFXSpinner());
    }

    private JFXButton createDrawerButton(String textId) {
        JFXButton button = new JFXButton();
        button.setPrefWidth(100);
        button.setText(LocaleUtils.getInstance().getString(textId));
        button.setTextFill(Color.WHITE);
        button.setFont(new Font(20));
        button.setOnAction(event -> onNavigationDrawerClick(textId));
        return button;
    }

    public void addCorrectRow(ColorRow row) {
        currentGameView.addCorrectRow(row);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}