package com.financetracker.gui.panes;

import com.financetracker.model.Transaction;
import com.financetracker.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.List;

public class TransactionListPane extends VBox {
    private final TransactionService txService;
    private final TableView<Transaction> table = new TableView<>();

    public TransactionListPane(TransactionService txService) {
        this.txService = txService;

        // 列：日期
        TableColumn<Transaction, String> dateCol = new TableColumn<>("日期");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        // 列：分类
        TableColumn<Transaction, String> catCol = new TableColumn<>("分类");
        catCol.setCellValueFactory(c ->
                c.getValue().getCategory() != null
                        ? new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory().getName())
                        : new javafx.beans.property.SimpleStringProperty("未分类")
        );

        // 列：金额
        TableColumn<Transaction, BigDecimal> amtCol = new TableColumn<>("金额");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // 列：描述
        TableColumn<Transaction, String> descCol = new TableColumn<>("描述");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().addAll(dateCol, catCol, amtCol, descCol);
        refresh();

        getChildren().add(table);
    }

    public void refresh() {
        List<Transaction> list = txService.getAllTransactions();
        table.setItems(FXCollections.observableArrayList(list));
    }
}
