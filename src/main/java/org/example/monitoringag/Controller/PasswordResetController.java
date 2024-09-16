package org.example.monitoringag.Controller;

import org.example.monitoringag.DTO.PasswordResetRequest;
import org.example.monitoringag.DTO.tokenDto;
import org.example.monitoringag.Service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // Endpoint pour demander une réinitialisation de mot de passe
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest email) {
        try {
            passwordResetService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset request received. If the email is registered, a reset link will be sent.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing password reset request: " + e.getMessage());
        }
    }

    // Endpoint pour réinitialiser le mot de passe
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody tokenDto token) {
        try {
            boolean success = passwordResetService.resetPassword(token);
            if (success) {
                return ResponseEntity.ok("Password has been successfully reset.");
            } else {
                return ResponseEntity.status(400).body("Invalid or expired reset token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error resetting password: " + e.getMessage());
        }
    }
}
