package presentation.view;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import presentation.controller.PresentationController;
import presentation.utils.ComponentUtils;
import presentation.view.components.RaisedButton;

public class LoadView extends GridPane {
    private Integer pegs = 3;
    private Integer colors = 3;
    public LoadView() {
        VBox newGameBox = new VBox();
        VBox savedGameBox = new VBox();

        RaisedButton newGameButton = new RaisedButton("Start Game"); // TODO: Strings
        RaisedButton savedGameButton = new RaisedButton("Start Game"); // TODO: Strings

        populateNewGameBox(newGameBox, newGameButton);

        getColumnConstraints().addAll(ComponentUtils.createColumnConstraint(50), ComponentUtils.createColumnConstraint(50));
        getRowConstraints().addAll(ComponentUtils.createRowConstraint(100));

        add(createBorderPane("New Game", newGameBox, newGameButton), 0, 0); // TODO: Strings
        add(createBorderPane("Saved Games", savedGameBox, savedGameButton), 1, 0); // TODO: Strings
    }

    private void populateNewGameBox(VBox newGameBox, RaisedButton button) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(0, 0, 0, 0));

        Label labelPegs = createLabel("Pegs:");
        labelPegs.setFont(Font.font(18));
        grid.add(labelPegs, 0, 0);

        JFXComboBox<String> pegsComboBox = new JFXComboBox<>();
        pegsComboBox.setStyle("-fx-background-color: #cfd8dc;");
        fillComboBox(pegsComboBox, 3, 9);
        pegsComboBox.getSelectionModel().clearAndSelect(0);
        pegsComboBox.setOnAction(event -> handleComboBoxPegs(pegsComboBox.getSelectionModel().getSelectedItem()));
        grid.add(pegsComboBox, 1, 0);

        Label labelColors = createLabel("Colors:");
        labelColors.setFont(Font.font(18));
        grid.add(labelColors, 2, 0);

        JFXComboBox<String> colorComboBox = new JFXComboBox<>();
        colorComboBox.setStyle("-fx-background-color: #cfd8dc;");
        fillComboBox(colorComboBox, 3, 9);
        colorComboBox.getSelectionModel().clearAndSelect(0);
        colorComboBox.setOnAction(event -> handleComboBoxColor(colorComboBox.getSelectionModel().getSelectedItem()));
        grid.add(colorComboBox, 3, 0);

        ToggleGroup roleGroup = new ToggleGroup();
        ToggleGroup algorithmGroup = new ToggleGroup();

        JFXRadioButton breakerRole = createRadioButton("Breaker");
        JFXRadioButton makerRole = createRadioButton("Maker");

        breakerRole.setToggleGroup(roleGroup);
        makerRole.setToggleGroup(roleGroup);
        breakerRole.setSelected(true);

        JFXRadioButton geneticAlgorithm = createRadioButton("Genetic");
        JFXRadioButton fiveGuessAlgorithm = createRadioButton("FiveGuess");

        geneticAlgorithm.setToggleGroup(algorithmGroup);
        fiveGuessAlgorithm.setToggleGroup(algorithmGroup);

        breakerRole.setSelected(true);
        geneticAlgorithm.setSelected(true);

        VBox roleBox = new VBox();
        roleBox.setSpacing(25);
        roleBox.getChildren().addAll(breakerRole, makerRole);

        VBox algorithmBox = new VBox();
        algorithmBox.setSpacing(25);
        algorithmBox.getChildren().addAll(geneticAlgorithm, fiveGuessAlgorithm);

        Label roleLabel = createLabel("Role"); // TODO: Strings
        Label algorithmLabel = createLabel("Algorithm"); // TODO: Strings

        roleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            algorithmBox.setVisible(newValue.getUserData().equals("Maker"));
            algorithmLabel.setVisible(newValue.getUserData().equals("Maker"));

            if (newValue.getUserData().equals("Breaker") && colorComboBox.getItems().size() == 4) {
                fillComboBox(pegsComboBox, 3, 9);
                fillComboBox(colorComboBox, 3, 9);

                geneticAlgorithm.setSelected(true);
            }

            algorithmGroup.selectedToggleProperty().addListener((observable1, oldValue1, newValue1) -> {
                if (newValue1.getUserData().equals("FiveGuess") && colorComboBox.getItems().size() == 7){
                    fillComboBox(pegsComboBox, 3, 6);
                    fillComboBox(colorComboBox, 3, 6);
                }
                else if (newValue1.getUserData().equals("Genetic") && colorComboBox.getItems().size() == 4) {
                    fillComboBox(pegsComboBox, 3, 9);
                    fillComboBox(colorComboBox, 3, 9);
                }
            });
        });

        algorithmBox.setVisible(false);
        algorithmLabel.setVisible(false);


        newGameBox.setSpacing(20);
        newGameBox.getChildren().addAll(grid, roleLabel, roleBox, algorithmLabel, algorithmBox);

        button.setOnAction(event -> {
            String computerName = algorithmGroup.getSelectedToggle().getUserData() + "Computer";
            String role = roleGroup.getSelectedToggle().getUserData().toString();
            PresentationController.getInstance().requestStartGame(computerName, role, pegs, colors); // TODO
        });
    }

    private void fillComboBox(JFXComboBox<String> comboBox, int start, int end) {
        comboBox.getItems().clear();
        for (int i = start; i <= end; ++i) comboBox.getItems().add(String.valueOf(i));
    }

    private void handleComboBoxPegs(String selectedItem) {
        this.pegs = Integer.parseInt(selectedItem);
    }

    private void handleComboBoxColor(String selectedItem) {
        this.colors = Integer.parseInt(selectedItem);
    }

    private JFXRadioButton createRadioButton(String title) {
        JFXRadioButton result = new JFXRadioButton(title);
        result.setUserData(title);
        result.setTextFill(Color.WHITE);
        result.setFont(Font.font(18));
        return result;
    }

    private Label createLabel(String title) {
        Label result = new Label(title);
        result.setFont(Font.font(25));
        result.setTextFill(Color.WHITE);
        return result;
    }

    private BorderPane createBorderPane(String title, VBox mainContent, RaisedButton button) {
        BorderPane result = new BorderPane();
        Label titleLabel = createLabel(title);
        BorderPane.setMargin(titleLabel, new Insets(30));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        result.setTop(titleLabel);
        BorderPane.setMargin(mainContent, new Insets(30));
        mainContent.setSpacing(10);
        mainContent.setAlignment(Pos.TOP_CENTER);
        result.setCenter(mainContent);
        BorderPane.setMargin(button, new Insets(30));
        BorderPane.setAlignment(button, Pos.CENTER);
        result.setBottom(button);
        return result;
    }
}
