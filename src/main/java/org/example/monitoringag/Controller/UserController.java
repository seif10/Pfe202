package org.example.monitoringag.Controller;

import org.example.monitoringag.DTO.UserProfileDTO;
import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Entity.User;
import org.example.monitoringag.Service.KeycloakUserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private Keycloak keycloak;

    @PostMapping("/adduser")
    public void addUser(@RequestBody User user) {
        keycloakUserService.createUser(user);
    }

    @DeleteMapping("delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            keycloakUserService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileDTO userProfile) {
        try {
            // Retrieve the user
            UserRepresentation user = keycloak.realm("monitoringagent")
                    .users()
                    .get(userProfile.getId())
                    .toRepresentation();

            // Update the user information
            user.setFirstName(userProfile.getFirstName());
            user.setLastName(userProfile.getLastName());
            user.setEmail(userProfile.getEmail());

            // Update the user in Keycloak
            keycloak.realm("monitoringagent")
                    .users()
                    .get(userProfile.getId())
                    .update(user);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user profile: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extract the token from the request
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            } else {
                return ResponseEntity.badRequest().body("Invalid or missing token");
            }

            // Logout the user by invalidating the token
            keycloak.tokenManager().invalidate(token);

            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error logging out user: " + e.getMessage());
        }
    }

    @GetMapping("/showAll")
    public List<User> showUsers(){
        return keycloakUserService.showLUsersFromDB();
    }

}
