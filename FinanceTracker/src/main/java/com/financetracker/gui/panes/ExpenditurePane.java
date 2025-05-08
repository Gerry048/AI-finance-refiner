package com.financetracker.gui.panes;

import com.financetracker.model.Category;
import com.financetracker.service.TransactionService;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ExpenditurePane extends VBox {
    public ExpenditurePane(TransactionService txService) {
        setSpacing(20);
        setAlignment(Pos.CENTER);

        // 取当月第一天到今天
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);

        // 统计
        Map<Category, BigDecimal> totals =
                txService.getTotalSpendingByCategory(start, now);

        PieChart pie = new PieChart();
        pie.setTitle("各类支出占比");
        totals.forEach((cat, amt) ->
                pie.getData().add(
                        new PieChart.Data(cat.getName(), amt.doubleValue())
                )
        );

        // 柱状图
        CategoryAxis x = new CategoryAxis();
        x.setLabel("分类");
        NumberAxis y = new NumberAxis();
        y.setLabel("金额");

        BarChart<String, Number> bar = new BarChart<>(x, y);
        bar.setTitle("支出对比");
        XYChart.Series<String, Number> ser = new XYChart.Series<>();
        ser.setName("本月支出");
        totals.forEach((cat, amt) ->
                ser.getData().add(
                        new XYChart.Data<>(cat.getName(), amt.doubleValue())
                )
        );
        bar.getData().add(ser);

        getChildren().addAll(pie, bar);
    }
}
