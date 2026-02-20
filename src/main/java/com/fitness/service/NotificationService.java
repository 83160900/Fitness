package com.fitness.service;

import com.fitness.domain.model.Notification;
import com.fitness.domain.model.ScheduleSlot;
import com.fitness.domain.model.User;
import com.fitness.repository.NotificationRepository;
import com.fitness.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WhatsAppService whatsappService;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, WhatsAppService whatsappService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.whatsappService = whatsappService;
    }

    public void notifyReservationCreated(ScheduleSlot slot) {
        Notification notification = new Notification();
        notification.setType("RESERVA");
        notification.setSenderEmail(slot.getStudentEmail());
        notification.setRecipientEmail(slot.getPersonalEmail());
        notification.setSlotId(slot.getId());
        notification.setTitle("Nova Reserva de Aula");
        
        String dateStr = slot.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        notification.setMessage("O aluno " + slot.getStudentEmail() + " reservou um horário para o dia " + dateStr + ". Por favor, confirme a reserva.");

        notificationRepository.save(notification);

        // Enviar WhatsApp para o Personal
        Optional<User> personalOpt = userRepository.findByEmail(slot.getPersonalEmail());
        personalOpt.ifPresent(personal -> {
            if (personal.getPhone() != null && !personal.getPhone().isEmpty()) {
                try {
                    String waMessage = "Olá " + personal.getName() + ", você tem uma nova solicitação de aula!\n" +
                            "Aluno: " + slot.getStudentEmail() + "\n" +
                            "Data: " + dateStr + "\n" +
                            "Acesse o app para confirmar.";
                    whatsappService.sendMessage(personal.getPhone(), waMessage);
                    notification.setSentToWhatsApp(true);
                } catch (Exception e) {
                    notification.setWhatsappError(e.getMessage());
                }
                notificationRepository.save(notification);
            }
        });
    }

    public void notifyReservationConfirmed(ScheduleSlot slot) {
        Notification notification = new Notification();
        notification.setType("CONFIRMACAO");
        notification.setSenderEmail(slot.getPersonalEmail());
        notification.setRecipientEmail(slot.getStudentEmail());
        notification.setSlotId(slot.getId());
        notification.setTitle("Reserva Confirmada!");

        String dateStr = slot.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        notification.setMessage("Sua reserva para o dia " + dateStr + " foi confirmada pelo Personal.");

        notificationRepository.save(notification);

        // Enviar WhatsApp para o Aluno
        Optional<User> studentOpt = userRepository.findByEmail(slot.getStudentEmail());
        studentOpt.ifPresent(student -> {
            if (student.getPhone() != null && !student.getPhone().isEmpty()) {
                try {
                    String waMessage = "Olá " + student.getName() + ", sua aula para o dia " + dateStr + " foi CONFIRMADA! Nos vemos lá.";
                    whatsappService.sendMessage(student.getPhone(), waMessage);
                    notification.setSentToWhatsApp(true);
                } catch (Exception e) {
                    notification.setWhatsappError(e.getMessage());
                }
                notificationRepository.save(notification);
            }
        });
    }
}
