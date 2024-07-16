package org.example.service.Impl;

import org.example.bean.User;
import org.example.service.UserService;
import org.example.sql.Operate;
import org.example.utils.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User userLogin(String username, String password) {
        Operate operate = new Operate(1, new Object[]{username, password});
        return operate.userLogin();
    }

    /**
     * @param username
     * @param email
     * @return
     */
    @Override
    public User selectUserByNameAndEmail(String username, String email) {
        Operate operate = new Operate(2, new Object[]{username, email});
        return operate.selectUserByNameAndEmail();
    }

    /**
     * @param user
     * @return
     */
    @Override
    public boolean userRegister(User user) {
        String salt = PasswordUtils.randomSalt();
        user.setSalt(salt);
        String newPassword = PasswordUtils.encryptPassword(user.getUsername(), user.getPassword(), salt);
        Operate operate = new Operate(3, new Object[]{user.getUsername(), user.getEmail(), newPassword,user.getSalt()});
        return operate.userRegister();
    }
}
