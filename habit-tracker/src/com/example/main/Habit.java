package com.example.main;

public class Habit {
    private static int idCounter = 0;
    private Integer id;
    private String title;
    private String description;
    private Frequency frequency;

    public Habit(String title, String description, Frequency frequency) {
        this.id = ++idCounter;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
