package com.financetracker.gui.panes;

import com.financetracker.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class HomePane extends VBox {
    public HomePane(User user, Runnable onLogout) {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        // 头像（请确保 /images/avatar.jpg 存在于 resources 目录下）
        Image avatarImage = new Image(
                getClass().getResourceAsStream("/images/6cc1d32ebc46e256e82c571feebea05.jpg")
        );
        ImageView avatarView = new ImageView(avatarImage);
        avatarView.setFitWidth(100);
        avatarView.setFitHeight(100);
        avatarView.setPreserveRatio(true);

        // 用户信息
        Label nameLabel  = new Label("用户名: " + user.getUsername());
        Label emailLabel = new Label("邮箱: "    + (user.getEmail() != null ? user.getEmail() : ""));

        // 退出登录按钮
        Button logoutBtn = new Button("退出登录");
        logoutBtn.setOnAction(e -> onLogout.run());

        getChildren().addAll(avatarView, nameLabel, emailLabel, logoutBtn);
    }
}

