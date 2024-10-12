package com.example.main.repositories;

import com.example.main.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }

    public User findByEmail(String email) {
        return users.get(email);
    }

    public void save(User user) {
        users.put(user.getEmail(), user);
    }

    public void update(User user) {
        users.put(user.getEmail(), user);
    }

    public void delete(String email) {
        users.remove(email);
    }
}
