package NerdMarket.admin;

import NerdMarket.users.UserRepository;
import NerdMarket.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUserAccount(Long adminId, Long targetUserId) {
        Users admin = userRepository.findUsersById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("Unauthorized - admin access required");
        }

        if (adminId.equals(targetUserId)) {
            throw new RuntimeException("Can't delete your own account");
        }

        Users target = userRepository.findUsersById(targetUserId);
        if (target == null) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(targetUserId);
    }

    public List<Users> getAllUsers(Long adminId) {
        Users admin = userRepository.findUsersById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("Unauthorized - admin access required");
        }
        return userRepository.findAll();
    }

    public Users promoteToAdmin(Long adminId, Long targetUserId) {
        Users admin = userRepository.findUsersById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("Unauthorized - admin access required");
        }

        Users target = userRepository.findUsersById(targetUserId);
        if (target == null) {
            throw new RuntimeException("User not found");
        }

        target.setAdmin(true);
        return userRepository.save(target);
    }

    public Users removeAdmin(Long adminId, Long targetUserId) {
        Users admin = userRepository.findUsersById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("Unauthorized - admin access required");
        }

        if (adminId.equals(targetUserId)) {
            throw new RuntimeException("Can't remove your own admin status");
        }

        Users target = userRepository.findUsersById(targetUserId);
        if (target == null) {
            throw new RuntimeException("User not found");
        }

        target.setAdmin(false);
        return userRepository.save(target);
    }

    public Users toggleUserActive(Long adminId, Long targetUserId, boolean active) {
        Users admin = userRepository.findUsersById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("Unauthorized - admin access required");
        }

        Users target = userRepository.findUsersById(targetUserId);
        if (target == null) {
            throw new RuntimeException("User not found");
        }

        target.setActive(active);
        return userRepository.save(target);
    }
}
