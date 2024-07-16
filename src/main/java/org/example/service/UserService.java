package org.example.service;

import org.example.bean.User;

public interface UserService {
    User userLogin(String username, String password);

    User selectUserByNameAndEmail(String username, String email);

    boolean userRegister(User user);
}
