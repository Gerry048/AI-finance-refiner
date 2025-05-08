package com.financetracker.gui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SavingsPane extends VBox {
    public SavingsPane() {
        // 设置主布局的间距和对齐方式
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);

        // 创建标题
        Label title = new Label("Saving Goals");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // 创建目标列表容器
        VBox goalsList = new VBox(5); // 子节点间距5像素

        // 示例数据
        String[] goalNames = {"Vacation", "New Car", "Emergency Fund"};
        double[] completionPercentages = {50.0, 30.0, 75.0};

        // 为每个目标创建显示行
        for (int i = 0; i < goalNames.length; i++) {
            HBox goalBox = new HBox(10); // 标签间距10像素
            Label nameLabel = new Label(goalNames[i] + ":");
            Label percentageLabel = new Label(String.format("%.2f%%", completionPercentages[i]));
            goalBox.getChildren().addAll(nameLabel, percentageLabel);
            goalsList.getChildren().add(goalBox);
        }

        // 将标题和目标列表添加到主布局
        this.getChildren().addAll(title, goalsList);
    }
}
