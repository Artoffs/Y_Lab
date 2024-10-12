package com.example.main.services;

import com.example.main.Habit;
import com.example.main.User;
import com.example.main.repositories.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        registerUser("admin", "admin", "admin");
    }

    public boolean registerUser(String email, String password, String name) {
        if (userRepository.findByEmail(email) != null) {
            return false;
        }
        User user = new User(email, password, name);
        userRepository.save(user);
        return true;
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Неверный email или пароль
    }
}
