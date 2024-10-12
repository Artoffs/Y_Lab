package com.example.main;

import com.example.main.repositories.CompletionHistoryRepository;
import com.example.main.repositories.HabitRepository;
import com.example.main.repositories.UserRepository;
import com.example.main.services.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class HabitTrackerApp {
    private static final UserRepository userRepository = new UserRepository();
    private static final UserService userService = new UserService(userRepository);
    private static final AdminService adminService = new AdminService(userRepository);
    private static final HabitService habitService = new HabitService(new HabitRepository());
    private static final CompletionHistoryRepository completionHistoryRepository = new CompletionHistoryRepository();
    private static final HabitTrackerService habitTrackerService = new HabitTrackerService(completionHistoryRepository);
    private static final StatisticsService statisticsService = new StatisticsService(completionHistoryRepository);
    private static User currentUser;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (currentUser == null) {
                showAuthMenu(scanner);
            } else if (currentUser.getEmail().equals("admin")) {
                showAdminMenu(scanner);
            } else {
                showMainMenu(scanner);
            }
        }
    }

    private static void showAdminMenu(Scanner scanner)
    {
        System.out.println("1. Заблокировать пользователя");
        System.out.println("2. Выйти из аккаунта");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice)
        {
            case 1:
                showBanPanel(scanner);
                break;
            case 2:
                currentUser = null;
                break;
        }
    }

    private static void showBanPanel(Scanner scanner)
    {
        System.out.println("Список пользователей: \n");
        for (Map.Entry<String, User> entry : adminService.getUserList().entrySet())
        {
            System.out.println("Email: " + entry.getKey() + " Username: " + entry.getValue().getName());
        }
        System.out.println("Введите email пользователя, которого хотите забанить:");
        String userEmail = scanner.nextLine();
        if(userEmail.equals("admin"))
        {
            System.out.println("Хорошая попытка, но нет)");
            return;
        }
        adminService.banUser(userRepository.findByEmail(userEmail));
        System.out.println("Пользователь успешно забанен");
    }

    private static void showAuthMenu(Scanner scanner) {
        System.out.println("1. Зарегистрироваться");
        System.out.println("2. Войти");
        System.out.println("3. Выйти");
        System.out.print("Выберите: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                register(scanner);
                break;
            case 2:
                login(scanner);
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.err.println("\n\n\nНеправильная операция. Повторите снова.\n\n\n");
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите ваше имя: ");
        String name = scanner.nextLine();
        if (userService.registerUser(email, password, name)) {
            System.out.println("\n\n\nУспешная регистрация!\n\n\n");
        } else {
            System.err.println("\n\n\nТакой email уже существует. Попробуйте снова.\n\n\n");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        currentUser = userService.loginUser(email, password);
        if (currentUser != null) {
            System.out.println("\n\n\nУспешный вход!\n\n\n");
        } else {
            System.err.println("\n\n\nНеправильный email или пароль. Попробуйте снова.\n\n\n");
        }
    }

    private static void showMainMenu(Scanner scanner) {
        System.out.println("1. Создать привычку");
        System.out.println("2. Отметить выполнение привычки");
        System.out.println("3. Посмотреть все привычки");
        System.out.println("4. Редактировать привычку");
        System.out.println("5. Удалить привычку");
        System.out.println("6. Редактировать информацию об аккаунте");
        System.out.println("7. Показать статистику");
        System.out.println("8. Выйти из аккаунта");
        System.out.print("Выберите функцию: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                createHabit(scanner);
                break;
            case 2:
                viewHabits();
                showCompletionMenu(scanner);
                break;
            case 3:
                viewHabits();
                break;
            case 4:
                editHabit(scanner);
                break;
            case 5:
                deleteHabit(scanner);
                break;
            case 6:
                showChangeInfoMenu(scanner);
                break;
            case 7:
                showStatisticsMenu(scanner);
                break;
            case 8:
                currentUser = null;
                break;
            default:
                System.err.println("\n\n\nНеправильная операция. Повторите снова.\n\n\n");
        }
    }

    private static void createHabit(Scanner scanner) {
        System.out.print("Введите название привычки: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание привычки: ");
        String description = scanner.nextLine();
        System.out.print("Выберите частоту: (1 = ежедневно, 2 = еженедельно): ");
        int frequency = scanner.nextInt();
        habitService.createHabit(currentUser, title, description, frequency == 1 ? Frequency.DAILY : Frequency.WEEKLY);
        System.out.println("\n\n\nПривычка успешно создана!\n\n\n");
    }

    private static void showCompletionMenu(Scanner scanner)
    {
        System.out.print("Введите название привычки, которую хотите отметить: ");
        String title = scanner.nextLine();
        Habit habit = findHabitByTitle(title);
        if(habit != null) {
            if (habitTrackerService.markHabitAsCompleted(currentUser, habit)) {
                System.out.println("\n\n\nПривычка успешно отмечена!\n\n\n");
            } else {
                System.out.println("\"\n\n\nВы уже отмечали эту привычку!\n\n\n\"");
            }
        } else {
            System.err.println("\n\n\nПривычка не найдена! Попробуйте снова!\n\n\n");
        }
    }

    private static void viewHabits() {
        System.out.println("Ваши привычки:");
        for (Habit habit : habitService.getHabits(currentUser)) {
            System.out.println(habit);
        }
    }

    private static void editHabit(Scanner scanner) {
        System.out.print("Введите название привычки, которую хотите изменить: ");
        String title = scanner.nextLine();
        Habit habit = findHabitByTitle(title);
        if (habit != null) {
            System.out.print("Введите новое описание: ");
            String description = scanner.nextLine();
            System.out.print("Введите новую частоту: (1 = ежедневно, 2 = еженедельно): ");
            int frequency = scanner.nextInt();
            habit.setDescription(description);
            habit.setFrequency(frequency == 1 ? Frequency.DAILY : Frequency.WEEKLY);
            habitService.updateHabit(currentUser, habit);
            System.out.println("\n\n\nПривычка успешно обновлена!\n\n\n");
        } else {
            System.err.println("\n\n\nПривычка с таким именем не найдена.\n\n\n");
        }
    }

    private static void showChangeInfoMenu(Scanner scanner)
    {
        System.out.println("1. Изменить имя");
        System.out.println("2. Изменить email");
        System.out.println("3. Изменить пароль");
        System.out.print("Выберите функцию: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice)
        {
            case 1:
                System.out.println("Введите новое имя: ");
                String newName = scanner.nextLine();
                if (!currentUser.getName().equals(newName))
                {
                    currentUser.setName(newName);
                    break;
                }
                System.err.println("\n\n\nНовое имя не может совпадать с предыдущим!\n\n\n");
                break;
            case 2:
                System.out.println("Введите новый email: ");
                String newEmail = scanner.nextLine();
                if (!currentUser.getEmail().equals(newEmail))
                {
                    currentUser.setEmail(newEmail);
                    break;
                }
                System.err.println("\n\n\nНовый email не может совпадать с предыдущим!\n\n\n");
                break;
            case 3:
                System.out.println("Введите новый пароль: ");
                String newPassword = scanner.nextLine();
                if (!currentUser.getPassword().equals(newPassword))
                {
                    currentUser.setPassword(newPassword);
                    break;
                }
                System.err.println("\n\n\nНовый пароль не может совпадать с предыдущим!\n\n\n");
                break;
        }
        userService.updateUser(currentUser);
    }

    private static void deleteHabit(Scanner scanner) {
        System.out.print("Введите название привычки, которую хотите удалить: ");
        String title = scanner.nextLine();
        Habit habit = findHabitByTitle(title);
        if (habit != null) {
            habitService.deleteHabit(currentUser, habit);
            completionHistoryRepository.deleteHabitHistory(currentUser, habit);
            System.out.println("\n\n\nПривычка успешно удалена!\n\n\n");
        } else {
            System.err.println("\n\n\nПривычка с таким именем не найдена!\n\n\n");
        }
    }

    private static void showStatisticsMenu(Scanner scanner)
    {
        System.out.println("Какую статистику Вы хотите узнать?");
        System.out.println("1. За период");
        System.out.println("2. Подсчет текущей серии выполнения привычек");
        System.out.println("3. Подсчет процента успешного выполнения привычек");
        System.out.println("Выберите функцию: ");
        int choice = scanner.nextInt();
        switch (choice)
        {
            case 1:
                viewHabits();
                showPeriodStatistics(scanner);
                break;
            case 2:
                showStreak();
                break;
            case 3:
                showPercentStats();
                break;
            case 4:
                break;
            default:
                System.out.println("\n\n\nОпция с таким номером не найдена. Попробуйте снова!\n\n\n");
                break;
        }

    }

    private static void showStreak()
    {
        Map<Habit, Integer> streak = statisticsService.countStreak(currentUser);
        if (streak == null)
        {
            System.out.println("\n\n\nВы еще не отметили ни одной привычки!\n\n\n");
            return;
        }
        for (Map.Entry<Habit, Integer> entry : streak.entrySet())
        {
            Habit habit = entry.getKey();
            Integer streakCount = entry.getValue();
            System.out.println(habit.getTitle() + " " + streakCount.toString());
        }
    }

    private static void showPeriodStatistics(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Введите название привычки, по которой хотели бы получить статистику: ");
        String title = scanner.nextLine();
        Habit habit = findHabitByTitle(title);
        System.out.println("За какой период Вы бы хотели узнать статистику? (1 - за день, 2 - за неделю, 3 - за месяц)");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println(statisticsService.getStatistics(currentUser, habit, LocalDate.now().minusDays(1)));
                break;
            case 2:
                System.out.println(statisticsService.getStatistics(currentUser, habit, LocalDate.now().minusWeeks(1)));
                break;
            case 3:
                System.out.println(statisticsService.getStatistics(currentUser, habit, LocalDate.now().minusMonths(1)));
                break;
            default:
                System.out.println("Опция с таким номером не найдена. Попробуйте снова!");
                break;
        }
    }

    public static void showPercentStats()
    {
        Map<Habit, Float> percentages = statisticsService.percentOfConsistency(currentUser);
        for (Map.Entry<Habit, Float> entry: percentages.entrySet())
        {
            Habit habit = entry.getKey();
            Float percent = entry.getValue();
            System.out.println("Привычка: " + habit.getTitle() + " " + percent.toString() + "%");
        }
     }

    private static Habit findHabitByTitle(String title) {
        for (Habit habit : habitService.getHabits(currentUser)) {
            if (habit.getTitle().equalsIgnoreCase(title)) {
                return habit;
            }
        }
        return null;
    }
}
