package com.financetracker.gui.panes;

import com.financetracker.model.User;
import com.financetracker.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class AuthPane extends VBox {
    public AuthPane(UserService userService, Consumer<User> onSuccess) {
        // 布局
        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER);

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // --- 登录 Tab ---
        Tab loginTab = new Tab("登录");
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);

        TextField loginUser = new TextField();
        loginUser.setPromptText("用户名");
        PasswordField loginPass = new PasswordField();
        loginPass.setPromptText("密码");
        Button btnLogin = new Button("登录");
        Label lblLoginMsg = new Label();

        btnLogin.setOnAction(e -> {
            try {
                User u = userService.authenticateUser(
                        loginUser.getText().trim(),
                        loginPass.getText().trim()
                );
                if (u != null) {
                    lblLoginMsg.setText("✔ 登录成功");
                    onSuccess.accept(u);
                } else {
                    lblLoginMsg.setText("✘ 用户名或密码错误");
                }
            } catch (Exception ex) {
                lblLoginMsg.setText("！" + ex.getMessage());
            }
        });

        loginBox.getChildren().addAll(loginUser, loginPass, btnLogin, lblLoginMsg);
        loginTab.setContent(loginBox);

        // --- 注册 Tab ---
        Tab regTab = new Tab("注册");
        VBox regBox = new VBox(10);
        regBox.setAlignment(Pos.CENTER);

        TextField regUser = new TextField();
        regUser.setPromptText("用户名");
        PasswordField regPass = new PasswordField();
        regPass.setPromptText("密码");
        TextField regEmail = new TextField();
        regEmail.setPromptText("邮箱（可选）");
        Button btnReg = new Button("注册");
        Label lblRegMsg = new Label();

        btnReg.setOnAction(e -> {
            try {
                userService.registerUser(
                        regUser.getText().trim(),
                        regPass.getText().trim(),
                        regEmail.getText().trim()
                );
                lblRegMsg.setText("✔ 注册成功，请切换到登录");
                tabs.getSelectionModel().select(loginTab);
            } catch (Exception ex) {
                lblRegMsg.setText("！" + ex.getMessage());
            }
        });

        regBox.getChildren().addAll(regUser, regPass, regEmail, btnReg, lblRegMsg);
        regTab.setContent(regBox);

        tabs.getTabs().addAll(loginTab, regTab);
        getChildren().add(tabs);
    }
}
