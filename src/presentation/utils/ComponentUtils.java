package presentation.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ComponentUtils {
    public static void showErrorDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LocaleUtils.getInstance().getString("APP_TITLE"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void buildScene(Class context, Stage stage, Parent parent, double minWidth, double minHeight) {
        stage.setTitle(LocaleUtils.getInstance().getString("APP_TITLE"));
        stage.setScene(new Scene(parent));
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.getIcons().add(new Image(context.getResource("/resources/img/ic_launcher.png").toExternalForm()));
        stage.show();
    }

    public static ColumnConstraints createColumnConstraint(int percent) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(percent);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        return columnConstraints;
    }

    public static RowConstraints createRowConstraint(int percent) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(percent);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        return rowConstraints;
    }
}
