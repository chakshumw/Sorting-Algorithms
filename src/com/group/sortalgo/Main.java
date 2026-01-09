package com.group.sortalgo;

import javafx.application.Application;
import javafx.application.Platform;
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
    private Label bestAlgoNameLabel;
    private Label bestAlgoTimeLabel;
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

        // Root background
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: " + LIGHT_BG + ";");

        // Header
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 18, 12, 18));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setBackground(new Background(new BackgroundFill(
                Color.web(DARK_BG), new CornerRadii(14), Insets.EMPTY
        )));

        Label logo = new Label("SortLab");
        logo.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: 800;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label pill = new Label(" Sorting Dashboard ");
        pill.setStyle(
                "-fx-background-color: " + ACCENT_PURPLE + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 14;"
        );

        header.getChildren().addAll(logo, spacer, pill);

        // Main panel
        VBox mainPanel = new VBox(16);
        mainPanel.setPadding(new Insets(18));
        mainPanel.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(18), Insets.EMPTY
        )));
        mainPanel.setEffect(new DropShadow(18, Color.rgb(15, 23, 42, 0.12)));

        VBox bestCard = buildBestAlgorithmCard();
        VBox datasetCard = buildDatasetCard(stage);

        HBox topRow = new HBox(16, bestCard, datasetCard);
        HBox.setHgrow(bestCard, Priority.ALWAYS);

        HBox bottomRow = buildBottomRow();

        mainPanel.getChildren().addAll(topRow, bottomRow);

        // Footer
        Label footer = new Label("2021T01251 • 2021T01254 • 2021T01241");
        footer.setStyle(
                "-fx-text-fill: #6B7280;" +
                        "-fx-font-size: 11px;"
        );

        VBox footerBox = new VBox(footer);
        footerBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(header, mainPanel, footerBox);

        stage.setScene(new Scene(root, 960, 560));
        stage.show();
    }

    // UI BUILDERS

    private VBox buildBestAlgorithmCard() {
        Label title = new Label("Best algorithm");
        title.setStyle(
                "-fx-text-fill: #6B7280;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: 600;"
        );

        bestAlgoNameLabel = new Label("N/A");
        bestAlgoNameLabel.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #111827;" +
                        "-fx-padding: 10 0;"
        );

        bestAlgoTimeLabel = new Label("Run an analysis to find the fastest method.");
        bestAlgoTimeLabel.setStyle(
                "-fx-text-fill: #6B7280;" +
                        "-fx-font-size: 11px;"
        );

        VBox card = new VBox(10, title, bestAlgoNameLabel, bestAlgoTimeLabel);
        card.setPadding(new Insets(20));
        styleCard(card);

        return card;
    }

    private VBox buildDatasetCard(Stage stage) {
        Label title = new Label("Dataset & controls");
        title.setStyle(
                "-fx-text-fill: #6B7280;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: 600;"
        );

        Button loadButton = new Button("Load CSV");
        stylePrimaryButton(loadButton);

        runButton = new Button("Run sorting");
        styleDarkButton(runButton);
        runButton.setDisable(true);

        columnSelector = new ComboBox<>();
        columnSelector.setPromptText("Numeric column");
        columnSelector.setPrefWidth(220);
        columnSelector.setStyle(
                "-fx-background-radius: 999;" +
                        "-fx-border-radius: 999;" +
                        "-fx-border-color: #E5E7EB;" +
                        "-fx-padding: 4 10;" +
                        "-fx-font-size: 11px;"
        );

        HBox controls = new HBox(8, loadButton, columnSelector, runButton);
        controls.setAlignment(Pos.CENTER_RIGHT);

        fileLabel = new Label("No file loaded");
        fileLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 11px;");

        VBox card = new VBox(10, title, fileLabel, controls);
        card.setPadding(new Insets(16));
        styleCard(card);

        loadButton.setOnAction(e -> onLoadCSV(stage));
        runButton.setOnAction(e -> onRunSorting());

        return card;
    }

    private HBox buildBottomRow() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time (ms)");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setCategoryGap(18);
        barChart.setBarGap(6);
        barChart.setStyle("-fx-background-color: transparent;");

        VBox chartCard = new VBox(8, new Label("Performance"), barChart);
        chartCard.setPadding(new Insets(14));
        styleCard(chartCard);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(9);
        logArea.setStyle(
                "-fx-font-family: 'JetBrains Mono', 'Consolas', monospace;" +
                        "-fx-font-size: 11px;" +
                        "-fx-text-fill: #111827;" +
                        "-fx-control-inner-background: #F9FAFB;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E5E7EB;" +
                        "-fx-border-width: 1;"
        );

        Platform.runLater(() -> {
            logArea.lookup(".content").setStyle("-fx-padding: 15;");
        });

        Label logTitle = new Label("Execution log");
        logTitle.setStyle(
                "-fx-text-fill: #374151;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: 700;"
        );

        VBox logCard = new VBox(10, logTitle, logArea);
        logCard.setPadding(new Insets(14));
        styleCard(logCard);

        HBox row = new HBox(14, chartCard, logCard);
        HBox.setHgrow(chartCard, Priority.ALWAYS);
        HBox.setHgrow(logCard, Priority.ALWAYS);

        return row;
    }

    // STYLING HELPERS

    private void stylePrimaryButton(Button b) {
        b.setStyle(
                "-fx-background-color: " + ACCENT_PURPLE + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: 600;" +
                        "-fx-font-size: 11px;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 16;"
        );
    }

    private void styleDarkButton(Button b) {
        b.setStyle(
                "-fx-background-color: #111827;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: 600;" +
                        "-fx-font-size: 11px;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 16;"
        );
    }

    private void styleCard(Region card) {
        card.setBackground(new Background(new BackgroundFill(
                Color.web("#F8F8FC"), new CornerRadii(12), Insets.EMPTY
        )));
        card.setBorder(new Border(new BorderStroke(
                Color.web("#E5E7EB"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(12),
                new BorderWidths(1)
        )));
    }


    private void onLoadCSV(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showOpenDialog(stage);
        if (file == null) return;

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
            showError(e.getMessage());
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

        String bestName = PerformanceEvaluator.bestAlgorithm(times);
        Long bestTime = bestName != null ? times.get(bestName) : null;

        if (bestName != null && bestTime != null) {
            bestAlgoNameLabel.setText(bestName);
            bestAlgoTimeLabel.setText("Fastest: " + bestTime + " ns total");
        } else {
            bestAlgoNameLabel.setText("N/A");
            bestAlgoTimeLabel.setText("No result.");
        }

        // Log
        StringBuilder sb = new StringBuilder("Execution times (ns):\n\n");
        for (Map.Entry<String, Long> entry : times.entrySet()) {
            sb.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append(" ns\n");
        }
        logArea.setText(sb.toString());

        // Chart
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Long> entry : times.entrySet()) {
            series.getData().add(
                    new XYChart.Data<>(entry.getKey(), entry.getValue() / 1_000_000.0)
            );
        }

        barChart.getData().add(series);

        // Highlight best bar
        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> point : series.getData()) {
                if (point.getNode() == null) continue;

                if (point.getXValue().equals(bestName)) {
                    point.getNode().setStyle("-fx-bar-fill: " + ACCENT_PURPLE + ";");
                } else {
                    point.getNode().setStyle("-fx-bar-fill: #D1D5DB;");
                }
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
