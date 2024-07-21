package org.example.monitoringag.Controller;

import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Entity.User;
import org.example.monitoringag.Service.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private KeycloakUserService keycloakUserService;

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

    @GetMapping("/showAll")
    public List<User> showParsedLog(){
        return keycloakUserService.showLUsersFromDB();
    }


}
