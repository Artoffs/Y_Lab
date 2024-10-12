package com.example.main.services;

import com.example.main.User;
import com.example.main.repositories.UserRepository;

import java.util.List;
import java.util.Map;

public class AdminService
{
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Map<String, User> getUserList()
    {
        return userRepository.getUsers();
    }

    public void banUser(User user)
    {
        userRepository.delete(user.getEmail());
    }
}
