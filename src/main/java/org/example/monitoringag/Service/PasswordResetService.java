package org.example.monitoringag.Service;

import org.example.monitoringag.DTO.PasswordResetRequest;
import org.example.monitoringag.DTO.tokenDto;
import org.example.monitoringag.Repository.PasswordResetTokenRepository;
import org.example.monitoringag.Repository.UserRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${application.resetPassword.url}")
    private String resetPasswordUrl; // URL base pour la réinitialisation du mot de passe

    private String realm = "monitoringagent";

    public void requestPasswordReset(PasswordResetRequest email) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Rechercher les utilisateurs sans filtrage
            List<UserRepresentation> users = usersResource.search(
                    null, // username
                    null, // first
                    null, // max
                    null, // enabled
                    null  // emailVerified
            );

            // Vérifiez les résultats
            System.out.println("Number of users found: " + users.size());
            users.forEach(user -> System.out.println("User email: " + user.getEmail()));

            // Filtrer les utilisateurs par e-mail
            UserRepresentation userRepresentation = users.stream()
                    .filter(user -> email.getEmail().equals(user.getEmail()))
                    .findFirst()
                    .orElse(null);

            if (userRepresentation == null) {
                System.out.println("User not found for email: " + email.getEmail());
                return;
            }

            String userId = userRepresentation.getId();

            // Générer un token de réinitialisation
            String token = UUID.randomUUID().toString();
            // Stocker le token dans la base de données
            keycloakUserService.savePasswordResetToken(userId, token);

            // Envoyer un e-mail avec le lien de réinitialisation
            String resetLink = resetPasswordUrl + "?token=" + token;
            sendResetEmail(email.getEmail(), resetLink);
        } catch (Exception e) {
            System.err.println("Error processing password reset request: " + e.getMessage());
        }
    }




    private void sendResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, please click the following link: " + resetLink);

        mailSender.send(message);
    }

    public boolean resetPassword(tokenDto token) {
        // Vérifiez le token et récupérez l'ID utilisateur
        String userId = keycloakUserService.findUserIdByResetToken(token.getToken());
        if (userId == null) {
            return false; // Token invalide
        }

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Réinitialiser le mot de passe de l'utilisateur
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(token.getNewpassword());

        try {
            usersResource.get(userId).resetPassword(passwordCred);
            // Supprimer le token après la réinitialisation
            keycloakUserService.deletePasswordResetToken(token.getToken());
            return true;
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }
}
