package NerdMarket.admin;

import NerdMarket.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestBody Map<String, Long> body) {
        try {
            Long adminId = body.get("adminId");
            adminService.deleteUserAccount(adminId, userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam Long adminId) {
        try {
            List<Users> users = adminService.getAllUsers(adminId);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{userId}/promote")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long userId, @RequestBody Map<String, Long> body) {
        try {
            Long adminId = body.get("adminId");
            Users updated = adminService.promoteToAdmin(adminId, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{userId}/demote")
    public ResponseEntity<?> removeAdmin(@PathVariable Long userId, @RequestBody Map<String, Long> body) {
        try {
            Long adminId = body.get("adminId");
            Users updated = adminService.removeAdmin(adminId, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{userId}/toggle-active")
    public ResponseEntity<?> toggleActive(@PathVariable Long userId, @RequestBody Map<String, Object> body) {
        try {
            Long adminId = ((Number) body.get("adminId")).longValue();
            boolean active = (boolean) body.get("active");
            Users updated = adminService.toggleUserActive(adminId, userId, active);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
