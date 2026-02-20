package com.fitness.dto;

public class CoachSummaryDTO {
    private long activeStudents;
    private long alerts;
    private String goalProgress;

    public CoachSummaryDTO(long activeStudents, long alerts, String goalProgress) {
        this.activeStudents = activeStudents;
        this.alerts = alerts;
        this.goalProgress = goalProgress;
    }

    public long getActiveStudents() { return activeStudents; }
    public void setActiveStudents(long activeStudents) { this.activeStudents = activeStudents; }

    public long getAlerts() { return alerts; }
    public void setAlerts(long alerts) { this.alerts = alerts; }

    public String getGoalProgress() { return goalProgress; }
    public void setGoalProgress(String goalProgress) { this.goalProgress = goalProgress; }
}
