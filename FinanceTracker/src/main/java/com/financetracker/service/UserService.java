package com.financetracker.service;

import com.financetracker.model.User;

/**
 * 处理用户注册、认证和基础信息管理的接口。
 */
public interface UserService {

    /**
     * 注册一个新用户。
     *
     * @param username 用户名。
     * @param password 用户的明文密码 (实现时内部需要进行哈希处理!)。
     * @param email    用户的电子邮件 (可选)。
     * @return 创建成功的用户对象。
     * @throws IllegalArgumentException 如果用户名已存在或密码不符合要求。
     */
    User registerUser(String username, String password, String email);

    /**
     * 验证用户登录。
     *
     * @param username 用户名。
     * @param password 用户尝试输入的明文密码。
     * @return 如果认证成功，返回对应的 User 对象；否则返回 null 或抛出认证异常。
     */
    User authenticateUser(String username, String password); // 或 Optional<User> authenticateUser(...) 或 boolean isValidLogin(...)

    /**
     * 根据用户名查找用户。
     *
     * @param username 用户名。
     * @return 找到的用户对象，未找到则返回 null 或 Optional.empty()。
     */
    User findUserByUsername(String username); // 或 Optional<User>

    /**
     * 更新用户信息 (例如修改邮箱, 修改密码)。
     *
     * @param user 包含更新信息的用户对象 (必须有有效的 userId)。
     * @param newPassword 如果需要修改密码，则传入新密码的明文；否则传入null。 (实现时内部哈希)
     * @return 更新后的用户对象。
     * @throws IllegalArgumentException 如果用户对象或ID无效，或未找到用户。
     */
    User updateUser(User user, String newPassword);

    // 注意：实际应用中密码管理会更复杂，可能需要单独的 changePassword 方法等。
}