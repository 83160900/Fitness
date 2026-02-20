package com.fitness.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleRequest {
    private String personalEmail;
    private String studentEmail;
    private LocalDateTime startTime;
    private String recurrence;
    private UUID slotId;
    private boolean confirm;
    private String reason;

    public ScheduleRequest() {}

    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public String getRecurrence() { return recurrence; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }

    public UUID getSlotId() { return slotId; }
    public void setSlotId(UUID slotId) { this.slotId = slotId; }

    public boolean isConfirm() { return confirm; }
    public void setConfirm(boolean confirm) { this.confirm = confirm; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
