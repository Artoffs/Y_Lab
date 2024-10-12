package com.example.main.repositories;

import com.example.main.Habit;
import com.example.main.User;

import java.time.LocalDate;
import java.util.*;

// Репозиторий для хранения истории выполнения привычек.
public class CompletionHistoryRepository
{
    private final Map<User, Map<Habit, List<LocalDate>>> completionHistory = new HashMap<>();

    public void saveCompletion(User user, Habit habit, LocalDate date)
    {
        completionHistory
                .computeIfAbsent(user, k -> new HashMap<>())
                .computeIfAbsent(habit, k -> new ArrayList<>())
                .add(date);
    }

    public List<LocalDate> getHabitCompletionHistory(User user, Habit habit) {
        return completionHistory
                .getOrDefault(user, Collections.emptyMap())
                .getOrDefault(habit, Collections.emptyList());
    }

    public Map<Habit, List<LocalDate>> getUserCompletionHistory(User user) {
        return completionHistory
                .getOrDefault(user, Collections.emptyMap());

    }

    public void deleteHabitHistory(User user, Habit habit)
    {
        completionHistory.get(user).remove(habit);
    }

}
