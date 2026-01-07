package com.group.sortalgo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    private List<String[]> rows;
    private List<Integer> numericCols;

    private ComboBox<String> columnSelector;
    private TextArea logArea;
    private Label fileLabel;
    private Button runButton;
    private BarChart<String, Number> barChart;

    private static final String ACCENT_PURPLE = "#8B5CF6";
    private static final String DARK_BG = "#050509";
    private static final String LIGHT_BG = "#ECECF4";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("SortLab – Sorting Algorithm Dashboard");

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: " + LIGHT_BG + ";");

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(12, 18, 12, 18));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setBackground(new Background(new BackgroundFill(
                Color.web(DARK_BG), new CornerRadii(14), Insets.EMPTY
        )));

        Label logo = new Label("SortLab");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 800;");
        header.getChildren().add(logo);

        // Main panel
        VBox mainPanel = new VBox(16);
        mainPanel.setPadding(new Insets(18));
        mainPanel.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(18), Insets.EMPTY
        )));
        mainPanel.setEffect(new DropShadow(18, Color.rgb(15, 23, 42, 0.12)));

        VBox datasetCard = buildDatasetCard(stage);
        VBox outputCard = buildOutputCard();

        mainPanel.getChildren().addAll(datasetCard, outputCard);

        root.getChildren().addAll(header, mainPanel);

        Scene scene = new Scene(root, 960, 560);
        stage.setScene(scene);
        stage.show();
    }

    // ===== UI builders =====
    private VBox buildDatasetCard(Stage stage) {
        Label title = new Label("Dataset & Controls");

        Button loadButton = new Button("Load CSV");
        loadButton.setOnAction(e -> onLoadCSV(stage));

        runButton = new Button("Run sorting");
        runButton.setDisable(true);
        runButton.setOnAction(e -> onRunSorting());

        columnSelector = new ComboBox<>();
        columnSelector.setPromptText("Numeric column");

        fileLabel = new Label("No file loaded");

        VBox box = new VBox(10, title, fileLabel, loadButton, columnSelector, runButton);
        box.setPadding(new Insets(16));
        return box;
    }

    private VBox buildOutputCard() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time (ms)");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(10);

        VBox box = new VBox(10, barChart, logArea);
        box.setPadding(new Insets(16));
        return box;
    }

    // ===== Logic =====
    private void onLoadCSV(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = chooser.showOpenDialog(stage);
        if (file == null) {
            showError("No file selected.");
            return;
        }

        try {
            rows = CSVLoader.loadCSV(file.getAbsolutePath());

            if (rows == null || rows.size() < 2) {
                showError("CSV file must have a header and at least one data row.");
                runButton.setDisable(true);
                return;
            }

            numericCols = CSVLoader.getNumericColumnIndices(rows);
            if (numericCols.isEmpty()) {
                showError("No numeric columns found in this CSV.");
                runButton.setDisable(true);
                return;
            }

            columnSelector.getItems().clear();
            List<String> names = CSVLoader.getColumnNames(rows);
            for (Integer idx : numericCols) {
                columnSelector.getItems().add(names.get(idx));
            }

            columnSelector.getSelectionModel().selectFirst();
            fileLabel.setText(file.getName());
            runButton.setDisable(false);

        } catch (IOException e) {
            showError("File cannot be read:\n" + e.getMessage());
        }
    }

    private void onRunSorting() {
        if (rows == null || numericCols == null || numericCols.isEmpty()) {
            showError("Please load a valid CSV file first.");
            return;
        }

        int selectedIndex = columnSelector.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Please select a numeric column.");
            return;
        }

        int colIndex = numericCols.get(selectedIndex);

        double[] data = CSVLoader.getColumnAsDoubleArray(rows, colIndex);
        if (data.length == 0) {
            showError("Selected column has no data.");
            return;
        }

        Map<String, Long> times = PerformanceEvaluator.runAll(data);

        // Log output
        StringBuilder sb = new StringBuilder("Execution times (ns):\n\n");
        for (Map.Entry<String, Long> entry : times.entrySet()) {
            sb.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append(" ns\n");
        }
        logArea.setText(sb.toString());

        // Chart output
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Long> entry : times.entrySet()) {
            series.getData().add(
                    new XYChart.Data<>(entry.getKey(),
                            entry.getValue() / 1_000_000.0)
            );
        }

        barChart.getData().add(series);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
