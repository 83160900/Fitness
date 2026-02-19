package com.fitness.controller;

import com.fitness.dto.LoginRequest;
import com.fitness.dto.LoginResponse;
import com.fitness.dto.RegisterRequest;
import com.fitness.domain.model.User;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String identifier = null;
            if (request.getUser() != null && !request.getUser().trim().isEmpty()) {
                identifier = request.getUser().trim();
            } else if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                identifier = request.getEmail().trim().toLowerCase();
            } else {
                return ResponseEntity.badRequest().body("Informe CPF ou e-mail.");
            }

            String password = request.getPassword() != null ? request.getPassword().trim() : "";
            System.out.println("[DEBUG_LOG] Login: Tentativa para '" + identifier + "'");

            try {
                java.util.Optional<User> optUser;
                String cleanIdentifier = identifier.toLowerCase().trim();
                String onlyDigits = cleanIdentifier.replaceAll("\\D", "");
                
                final String finalIdentifier = identifier;

                if (onlyDigits.length() == 11) {
                    System.out.println("[DEBUG_LOG] Login: Buscando por CPF '" + onlyDigits + "'");
                    optUser = userRepository.findByCpf(onlyDigits);
                    if (optUser.isEmpty()) {
                        System.out.println("[DEBUG_LOG] Login: CPF nÃ£o encontrado, tentando por e-mail '" + cleanIdentifier + "'");
                        optUser = userRepository.findByEmail(cleanIdentifier);
                    }
                } else {
                    System.out.println("[DEBUG_LOG] Login: Buscando por e-mail '" + cleanIdentifier + "'");
                    optUser = userRepository.findByEmail(cleanIdentifier);
                }

                return optUser
                        .map(user -> {
                            String storedPassword = user.getPassword() != null ? user.getPassword().trim() : "";
                            if (storedPassword.equals(password)) {
                                System.out.println("[DEBUG_LOG] Login: OK para '" + finalIdentifier + "'. ForceChange=" + user.isForcePasswordChange());
                                return ResponseEntity.ok(LoginResponse.builder()
                                        .name(user.getName())
                                        .email(user.getEmail())
                                        .role(user.getRole())
                                        .specialty(user.getSpecialty())
                                        .registrationNumber(user.getRegistrationNumber())
                                        .photoUrl(user.getPhotoUrl())
                                        .forcePasswordChange(user.isForcePasswordChange())
                                        .message("Login realizado com sucesso!")
                                        .build());
                            } else {
                                return ResponseEntity.status(401).body("Credenciais invÃƒÂ¡lidas!");
                            }
                        })
                        .orElseGet(() -> ResponseEntity.status(401).body("Credenciais invÃƒÂ¡lidas!"));
            } catch (org.springframework.dao.DataAccessException dae) {
                System.err.println("[DEBUG_LOG] Login: ERRO DE BANCO - " + dae.getMessage());
                String cause = dae.getMostSpecificCause() != null ? dae.getMostSpecificCause().getMessage() : dae.getMessage();
                return ResponseEntity.status(500).body("Erro de Banco de Dados durante login: " + cause);
            }
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Login: ERRO CRITICO - " + e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro Interno no Servidor: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody com.fitness.dto.ChangePasswordRequest req) {
        try {
            if (req.getNewPassword() == null || req.getNewPassword().trim().length() < 4) {
                return ResponseEntity.badRequest().body("Nova senha invÃƒÂ¡lida");
            }
            String identifier = req.getUser() != null ? req.getUser().trim() : (req.getEmail() != null ? req.getEmail().trim().toLowerCase() : null);
            if (identifier == null || identifier.isEmpty()) {
                return ResponseEntity.badRequest().body("Informe CPF ou e-mail");
            }
            java.util.Optional<User> optUser;
            String onlyDigits = identifier.replaceAll("\\D", "");
            if (onlyDigits.length() == 11) {
                optUser = userRepository.findByCpf(onlyDigits);
            } else {
                optUser = userRepository.findByEmail(identifier.toLowerCase());
            }
            return optUser.map(u -> {
                String current = u.getPassword() != null ? u.getPassword().trim() : "";
                if (req.getCurrentPassword() != null && current.equals(req.getCurrentPassword().trim())) {
                    u.setPassword(req.getNewPassword().trim());
                    u.setForcePasswordChange(false);
                    userRepository.save(u);
                    return ResponseEntity.ok("Senha alterada com sucesso");
                }
                return ResponseEntity.status(401).body("Senha atual incorreta");
            }).orElseGet(() -> ResponseEntity.status(404).body("UsuÃƒÂ¡rio nÃƒÂ£o encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao alterar senha: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("[DEBUG_LOG] Cadastro: InÃƒÆ’Ã‚Â­cio da tentativa para '" + request.getEmail() + "'");
        
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body("E-mail ÃƒÆ’Ã‚Â© obrigatÃƒÆ’Ã‚Â³rio");
            }

            String cleanEmail = request.getEmail().trim().toLowerCase();
            System.out.println("[DEBUG_LOG] Cadastro: Verificando existÃƒÆ’Ã‚Âªncia de '" + cleanEmail + "'");
            
            if (userRepository.findByEmail(cleanEmail).isPresent()) {
                System.out.println("[DEBUG_LOG] Cadastro: E-mail '" + cleanEmail + "' jÃƒÆ’Ã‚Â¡ existe.");
                return ResponseEntity.badRequest().body("E-mail jÃƒÆ’Ã‚Â¡ cadastrado!");
            }

            System.out.println("[DEBUG_LOG] Cadastro: Criando objeto User...");
            User user = new User();
            user.setName(request.getName());
            user.setEmail(cleanEmail);
            user.setPassword(request.getPassword());
            user.setRole(request.getRole() != null ? request.getRole() : com.fitness.domain.enums.UserRole.ALUNO);
            user.setSpecialty(request.getSpecialty());
            user.setRegistrationNumber(request.getRegistrationNumber());
            user.setFormation(request.getFormation());
            user.setExperience(request.getExperience());
            user.setActive(true);
            user.setLgpdConsent(true);

            System.out.println("[DEBUG_LOG] Cadastro: Chamando userRepository.save()...");
            userRepository.save(user);
            System.out.println("[DEBUG_LOG] Cadastro: UsuÃƒÆ’Ã‚Â¡rio '" + cleanEmail + "' salvo com sucesso no banco!");

            return ResponseEntity.ok("UsuÃƒÆ’Ã‚Â¡rio registrado com sucesso!");
        } catch (org.springframework.dao.DataAccessException e) {
            System.err.println("[DEBUG_LOG] Cadastro: ERRO DE BANCO - " + e.getMessage());
            String specificCause = (e.getMostSpecificCause() != null) ? e.getMostSpecificCause().getMessage() : e.getMessage();
            return ResponseEntity.status(500).body("Erro de Banco de Dados (Cadastro): " + specificCause);
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Cadastro: ERRO DESCONHECIDO - " + e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
            String msg = e.getMessage();
            if (e.getCause() != null) msg += " | Causa: " + e.getCause().getMessage();
            return ResponseEntity.status(500).body("Erro Interno (Cadastro): " + msg);
        }
    }
}
