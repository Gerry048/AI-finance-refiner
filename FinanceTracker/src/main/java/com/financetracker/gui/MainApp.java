package com.financetracker.gui;

import com.financetracker.gui.panes.*;
import com.financetracker.model.User;
import com.financetracker.repository.CsvDataStorage;
import com.financetracker.repository.DataStorage;
import com.financetracker.service.CategoryService;
import com.financetracker.service.TransactionService;
import com.financetracker.service.UserService;
import com.financetracker.service.impl.CategoryServiceImpl;
import com.financetracker.service.impl.TransactionServiceImpl;
import com.financetracker.service.impl.UserServiceImpl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private UserService userService;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // 用 data/users/ 目录存放所有账号信息
        DataStorage userStorage = new CsvDataStorage("users");
        this.userService = new UserServiceImpl(userStorage);

        showAuthUI();
    }

    /** 显示登录/注册界面 */
    private void showAuthUI() {
        AuthPane authPane = new AuthPane(userService, this::showMainUI);
        Scene authScene = new Scene(authPane, 800, 600);
        primaryStage.setTitle("FinanceTracker • 登录");
        primaryStage.setScene(authScene);
        primaryStage.show();
    }

    /**
     * 登录成功后展示主界面
     */
    private void showMainUI(User currentUser) {
        // 针对当前用户，创建独立数据存储和 Service
        DataStorage storage = new CsvDataStorage(currentUser.getId());
        CategoryService     catSvc = new CategoryServiceImpl(storage);
        TransactionService  txSvc  = new TransactionServiceImpl(storage);

        // 登出回调
        Runnable logoutAction = this::showAuthUI;

        // 导航栏
        HBox nav = new HBox(10);
        nav.setPadding(new Insets(10));
        Button bHome = new Button("主页");
        Button bEntry= new Button("录入交易");
        Button bList = new Button("交易列表");
        Button bExp  = new Button("支出统计");
        Button bIns  = new Button("洞察预测");
        Button bSav  = new Button("储蓄目标");
        nav.getChildren().addAll(bHome, bEntry, bList, bExp, bIns, bSav);

        BorderPane root = new BorderPane();
        root.setTop(nav);

        Scene mainScene = new Scene(root, 900, 600);
        primaryStage.setTitle("FinanceTracker • " + currentUser.getUsername());
        primaryStage.setScene(mainScene);
        primaryStage.show();

        // 绑定按钮
        bHome.setOnAction(e -> root.setCenter(new HomePane(currentUser, logoutAction)));
        bEntry.setOnAction(e -> root.setCenter(new DataEntryPane(txSvc, catSvc)));
        bList.setOnAction(e -> root.setCenter(new TransactionListPane(txSvc)));
        bExp.setOnAction(e -> root.setCenter(new ExpenditurePane(txSvc)));
        bIns.setOnAction(e -> root.setCenter(new InsightsPane(txSvc)));
        bSav.setOnAction(e -> root.setCenter(new SavingsPane()));

        // 默认显示主页（带退出按钮）
        root.setCenter(new HomePane(currentUser, logoutAction));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
