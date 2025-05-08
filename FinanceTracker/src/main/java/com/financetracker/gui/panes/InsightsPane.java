package com.financetracker.gui.panes;

import com.financetracker.service.TransactionService;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class InsightsPane extends VBox {
    private final TransactionService txService;

    // 新增这个构造器
    public InsightsPane(TransactionService txService) {
        this.txService = txService;
        buildChart();
    }

    // 保留无参构造器（如果你还想保留默认演示数据的话）
    public InsightsPane() {
        this(null);
    }

    private void buildChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Expenditure");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Daily Expenditure");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Expenditure");

        if (txService != null) {
            // 用真实数据：
            txService.getAllTransactions().forEach(tx ->
                    series.getData().add(new XYChart.Data<>(tx.getDate().getDayOfMonth(),
                            tx.getAmount().doubleValue()))
            );
        } else {
            // 回退到示例数据
            for (int i = 1; i <= 30; i++) {
                series.getData().add(new XYChart.Data<>(i, Math.random() * 100));
            }
        }

        chart.getData().add(series);
        this.getChildren().add(chart);
    }
}
