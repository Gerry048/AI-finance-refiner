package com.financetracker.gui.panes;

import com.financetracker.model.Category;
import com.financetracker.model.Transaction;
import com.financetracker.service.CategoryService;
import com.financetracker.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DataEntryPane extends VBox {
    private final TransactionService txService;
    private final CategoryService catService;

    public DataEntryPane(TransactionService txService,
                         CategoryService catService) {
        this.txService = txService;
        this.catService = catService;

        setPadding(new Insets(10));
        setSpacing(10);

        Label title = new Label("添加交易记录");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("选择日期");

        TextField amountField = new TextField();
        amountField.setPromptText("输入金额");

        TextField typeField = new TextField();
        typeField.setPromptText("输入分类");

        TextArea remarksArea = new TextArea();
        remarksArea.setPromptText("输入描述");
        remarksArea.setPrefRowCount(3);

        Button saveBtn = new Button("保存");
        saveBtn.setOnAction(evt -> {
            try {
                if (datePicker.getValue() == null
                        || amountField.getText().isBlank()
                        || typeField.getText().isBlank()) {
                    showAlert(Alert.AlertType.WARNING, "请填写所有必填字段");
                    return;
                }
                LocalDate date = datePicker.getValue();
                BigDecimal amt = new BigDecimal(amountField.getText());
                String type = typeField.getText().trim();
                // 如果分类不存在就创建
                Category cat = catService.getCategoryByName(type);
                if (cat == null) cat = catService.addCategory(type);

                Transaction tx = new Transaction(date, amt, cat, remarksArea.getText());
                txService.addTransaction(tx);

                showAlert(Alert.AlertType.INFORMATION, "保存成功");
                // 清空
                datePicker.setValue(null);
                amountField.clear();
                typeField.clear();
                remarksArea.clear();

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "金额格式有误");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "保存失败: " + ex.getMessage());
            }
        });

        getChildren().addAll(
                title,
                new Label("日期："), datePicker,
                new Label("金额："), amountField,
                new Label("分类："), typeField,
                new Label("描述："), remarksArea,
                saveBtn
        );
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}
