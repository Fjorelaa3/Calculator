package Project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculator");

        BorderPane rootPane = createRootPane();
        createMenuBar(rootPane);
        createCalculatorPane(rootPane);

        Scene scene = new Scene(rootPane, 400, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createRootPane() {
        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(10));
        return rootPane;
    }

    private void createMenuBar(BorderPane rootPane) {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #333333;");
        rootPane.setTop(menuBar);
    }

    private void createCalculatorPane(BorderPane rootPane) {
        VBox calculatorPane = new VBox(10);
        calculatorPane.setAlignment(Pos.CENTER);

        inputField = new TextField();
        inputField.setPromptText("Enter an expression");
        inputField.getStyleClass().add("input-field"); // Apply CSS style class

        GridPane buttonGrid = createButtonGrid();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        calculatorPane.getChildren().addAll(inputField, buttonGrid);
        rootPane.setCenter(calculatorPane);
    }

    private GridPane createButtonGrid() {
        GridPane buttonGrid = new GridPane();
        buttonGrid.setAlignment(Pos.CENTER);

        String[][] buttonLabels = {
                {"C","7", "8", "9", "/"},
                {"|x|","4", "5", "6", "*"},
                {"log","1", "2", "3", "-"},
                {"ln","0", ".", "=", "+"},
                {"1/x","^", "√", "n!"}
        };

        for (int row = 0; row < buttonLabels.length; row++) {
            for (int col = 0; col < buttonLabels[row].length; col++) {
                Button button = createButton(buttonLabels[row][col]);
                button.setPrefWidth(70);
                button.getStyleClass().add("calc-button"); // Apply CSS style class
                buttonGrid.add(button, col, row);
            }
        }

        return buttonGrid;
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setOnAction(e -> handleButtonAction(label));
        return button;
    }

    private void handlePowerOperation() {
        String currentText = inputField.getText();
        inputField.setText(currentText + "^");
    }

    private void handleSquareRootOperation() {
        String currentText = inputField.getText();
        if (!currentText.isEmpty()) {
            double number = Double.parseDouble(currentText);
            if (number >= 0) {
                double result = Math.sqrt(number);
                inputField.setText(String.valueOf(result));
            } else {
                inputField.setText("Invalid input");
            }
        }
    }

    private void handleLogarithmOperation() {
        String currentText = inputField.getText();
        if (!currentText.isEmpty()) {
            double number = Double.parseDouble(currentText);
            if (number > 0) {
                double result = Math.log10(number);
                inputField.setText(String.valueOf(result));
            } else {
                inputField.setText("Invalid input");
            }
        }
    }

    private void handleNaturalLogarithmOperation() {
        String currentText = inputField.getText();
        if (!currentText.isEmpty()) {
            double number = Double.parseDouble(currentText);
            if (number > 0) {
                double result = Math.log(number);
                inputField.setText(String.valueOf(result));
            } else {
                inputField.setText("Invalid input");
            }
        }
    }

    private void handleAbsoluteValueOperation() {
        String currentText = inputField.getText();
        if (!currentText.isEmpty()) {
            double number = Double.parseDouble(currentText);
            double result = Math.abs(number);
            inputField.setText(String.valueOf(result));
        }
    }

    private void handleInverseOperation() {
        String currentText = inputField.getText();
        inputField.setText("1/(" + currentText + ")");
    }

    private void handleFactorialOperation() {
        String currentText = inputField.getText();
        inputField.setText(currentText + "!");
    }

    private void handleButtonAction(String label) {
        String currentText = inputField.getText();
        switch (label) {
            case "=":
                calculateResult();
                break;
            case "C":
                inputField.clear();
                break;
            case "ln":
                handleNaturalLogarithmOperation();
                break;
            case "log":
                handleLogarithmOperation();
                break;
            case "^":
                handlePowerOperation();
                break;
            case "√":
                handleSquareRootOperation();
                break;
            case "|x|":
                handleAbsoluteValueOperation();
                break;
            case "1/x":
                handleInverseOperation();
                break;
            case "n!":
                handleFactorialOperation();
                break;
            default:
                inputField.setText(currentText + label);
                break;
        }
    }

    private void calculateResult() {
        String expression = inputField.getText();
        try {
            double result = ExpressionEvaluator.evaluate(expression);
            inputField.setText(String.valueOf(result));
        } catch (IllegalArgumentException e) {
            inputField.setText("Invalid expression");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
