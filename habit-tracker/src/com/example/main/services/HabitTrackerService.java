package com.example.main.services;

import com.example.main.Frequency;
import com.example.main.Habit;
import com.example.main.User;
import com.example.main.repositories.CompletionHistoryRepository;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.NoSuchElementException;

// Сервис для управления выполнением привычек
public class HabitTrackerService
{
    private final CompletionHistoryRepository completionHistoryRepository;

    public HabitTrackerService(CompletionHistoryRepository completionHistoryRepository)
    {
        this.completionHistoryRepository = completionHistoryRepository;
    }


    public boolean markHabitAsCompleted(User user, Habit habit)
    {
        if (!checkIfCompleted(user, habit))
        {
            completionHistoryRepository.saveCompletion(user, habit, LocalDate.now());
            return true;
        }
        return false;
    }

    public boolean checkIfCompleted(User user, Habit habit) {
        try {
            LocalDate lastCompletionDate = completionHistoryRepository.getHabitCompletionHistory(user, habit).getLast();
            LocalDate now = LocalDate.now();

            if (habit.getFrequency() == Frequency.DAILY) {
                return lastCompletionDate.isEqual(now);
            } else if (habit.getFrequency() == Frequency.WEEKLY) {
                return lastCompletionDate.get(WeekFields.ISO.weekOfYear()) == now.get(WeekFields.ISO.weekOfYear());
            }
            return false;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }
}
