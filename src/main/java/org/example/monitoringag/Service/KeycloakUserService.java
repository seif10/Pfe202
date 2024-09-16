package org.example.monitoringag.Service;


import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Entity.PasswordResetToken;
import org.example.monitoringag.Entity.User;
import org.example.monitoringag.Repository.PasswordResetTokenRepository;
import org.example.monitoringag.Repository.UserRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KeycloakUserService {

    @Autowired
    private Keycloak keycloak;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Autowired
    private UserRepository userRepository;

    @Value("${application.resetPassword.url}")
    private String resetPasswordUrl; // URL base pour la réinitialisation du mot de passe



    private String realm = "monitoringagent";

    public void createUser(User user) {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                System.err.println("User creation failed: Username '" + user.getUsername() + "' already exists");
                return;
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                System.err.println("User creation failed: Email '" + user.getEmail() + "' already exists");
                return;
            }

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation keycloakUser = new UserRepresentation();
            keycloakUser.setUsername(user.getUsername());
            keycloakUser.setEmail(user.getEmail());
            keycloakUser.setFirstName(user.getFirstName());
            keycloakUser.setLastName(user.getLastName());
            keycloakUser.setEnabled(true);

            Response response = usersResource.create(keycloakUser);

            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                // Set password
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(user.getPassword());
                usersResource.get(userId).resetPassword(passwordCred);

                // Save user to local database
                userRepository.save(user);
            } else {
                // Handle errors
                System.out.println("User creation failed: " + response.getStatus());
                // Capture and handle specific error cases
            }
        } catch (Exception e) {
            // Handle generic exceptions
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public void deleteUser(String username) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Search for user in Keycloak
            List<UserRepresentation> users = usersResource.search(username);
            if (users.isEmpty()) {
                System.out.println("User not found in Keycloak: " + username);
                return;
            }

            UserRepresentation userRepresentation = users.get(0);
            String userId = userRepresentation.getId();

            // Delete user in Keycloak
            usersResource.get(userId).remove();

            // Delete user in local database
            User user = userRepository.findByUsername(username);
            if (user != null) {
                userRepository.delete(user);
            } else {
                System.out.println("User not found in local database: " + username);
            }

        } catch (Exception e) {
            // Handle generic exceptions
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }



    public List<User> showLUsersFromDB(){
        return userRepository.findAll();
    }


    // Password Reset Request :

    public void savePasswordResetToken(String userId, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(userId);
        resetToken.setToken(token);
        passwordResetTokenRepository.save(resetToken);
    }

    public String findUserIdByResetToken(String token) {
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByToken(token);
        return resetToken.map(PasswordResetToken::getUserId).orElse(null);
    }

    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }


}
