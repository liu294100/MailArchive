package org.apollo.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apollo.mail.entity.User;
import org.apollo.mail.mapper.UserMapper;
import org.apollo.mail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void createUser(User user) {
        save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        updateById(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        removeById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}