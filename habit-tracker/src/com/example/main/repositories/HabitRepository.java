package com.example.main.repositories;

import com.example.main.Habit;
import com.example.main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitRepository {
    private Map<User, List<Habit>> habits = new HashMap<>();

    public void save(User user, Habit habit) {
        habits.computeIfAbsent(user, k -> new ArrayList<>()).add(habit);
    }

    public void update(User user, Habit habit) {
        List<Habit> userHabits = habits.get(user);
        if (userHabits != null) {
            userHabits.removeIf(h -> h.getId().equals(habit.getId()));
            userHabits.add(habit);
        }
    }

    public void delete(User user, Habit habit) {
        List<Habit> userHabits = habits.get(user);
        if (userHabits != null) {
            userHabits.remove(habit);
        }
    }

    public List<Habit> findByUser(User user) {
        return habits.getOrDefault(user, new ArrayList<>());
    }
}
