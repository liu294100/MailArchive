package org.apollo.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apollo.mail.entity.User;

public interface UserService extends IService<User> {
    // 自定义业务方法
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
}