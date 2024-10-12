package com.example.main.services;

import com.example.main.Frequency;
import com.example.main.Habit;
import com.example.main.User;
import com.example.main.repositories.CompletionHistoryRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsService
{
    private final CompletionHistoryRepository completionHistoryRepository;

    public StatisticsService(CompletionHistoryRepository completionHistoryRepository)
    {
        this.completionHistoryRepository = completionHistoryRepository;
    }

    public List<LocalDate> getStatistics(User user, Habit habit, LocalDate period)
    {
        return completionHistoryRepository
                .getHabitCompletionHistory(user, habit).stream()
                .filter(date -> date.isAfter(period))
                .collect(Collectors.toList());
    }

    public Map<Habit, Integer> countStreak(User user)
    {
        Map<Habit, List<LocalDate>> habits = completionHistoryRepository.getUserCompletionHistory(user);
        Map<Habit, Integer> result = new HashMap<>();
        if (habits.isEmpty())
        {
            return null;
        }
        for (Habit habit : habits.keySet())
        {
            if(habit.getFrequency() == Frequency.DAILY) result.put(habit, countConsecutiveDays(habits.get(habit)));
            if(habit.getFrequency() == Frequency.WEEKLY) result.put(habit, countConsecutiveWeeks(habits.get(habit)));
        }
        return result;
    }

    public Map<Habit, Float> percentOfConsistency(User user)
    {
        Map<Habit, Float> result = new HashMap<>();
        Map<Habit, List<LocalDate>> habits = completionHistoryRepository.getUserCompletionHistory(user);
        for (Map.Entry<Habit, List<LocalDate>> entry : habits.entrySet())
        {
            Habit habit = entry.getKey();
            List<LocalDate> completionHistory = entry.getValue();
            result.put(habit, countPercentage(completionHistory, habit.getFrequency()));
        }
        return result;
    }

    private Float countPercentage(List<LocalDate> dates, Frequency frequency) {
        if (dates.isEmpty()) {
            return 0.0f;
        }

        LocalDate startDate = dates.getFirst();
        LocalDate endDate = LocalDate.now();
        long totalDays;

        if (frequency == Frequency.DAILY) {
            totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        } else if (frequency == Frequency.WEEKLY) {
            totalDays = ChronoUnit.WEEKS.between(startDate, endDate) + 1;
        } else {
            return null;
        }

        int validDays = 0;
        Set<LocalDate> dateSet = new HashSet<>(dates);

        if (frequency == Frequency.DAILY) {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (dateSet.contains(date)) {
                    validDays++;
                }
            }
        } else {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusWeeks(1)) {
                if (dateSet.contains(date)) {
                    validDays++;
                }
            }
        }

        return (validDays / (float) totalDays) * 100;
    }


    private Integer countConsecutiveDays(List<LocalDate> dates)
    {
        if(dates.isEmpty())
        {
            return 0;
        }
        int currentStreak = 1;

        for(int i = 1; i < dates.size(); i++)
        {
            if (dates.get(i).minusDays(1).equals(dates.get(i - 1)))
            {
                currentStreak++;
            }
            else
            {
                currentStreak = 1;
            }
        }
        return currentStreak;
    }

    private Integer countConsecutiveWeeks(List<LocalDate> dates)
    {
        if(dates.isEmpty())
        {
            return 0;
        }
        int currentStreak = 1;

        for(int i = 1; i < dates.size(); i++)
        {
            if (dates.get(i).minusWeeks(1).equals(dates.get(i - 1)))
            {
                currentStreak++;
            }
            else
            {
                currentStreak = 1;
            }
        }
        return currentStreak;
    }

}
