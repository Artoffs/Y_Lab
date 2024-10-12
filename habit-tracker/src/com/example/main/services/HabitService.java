package com.example.main.services;

import com.example.main.Frequency;
import com.example.main.Habit;
import com.example.main.repositories.HabitRepository;
import com.example.main.User;

import java.util.List;

public class HabitService {
    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void createHabit(User user, String title, String description, Frequency frequency) {
        Habit habit = new Habit(title, description, frequency);
        habitRepository.save(user, habit);
    }

    public void updateHabit(User user, Habit habit) {
        habitRepository.update(user, habit);
    }

    public void deleteHabit(User user, Habit habit) {
        habitRepository.delete(user, habit);
    }

    public List<Habit> getHabits(User user) {
        return habitRepository.findByUser(user);
    }
}
