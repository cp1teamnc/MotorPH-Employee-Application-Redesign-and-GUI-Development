package GUI;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static AuthenticationService instance;
    private Map<String, User> users;
    private User currentUser;

    private AuthenticationService() {
        users = new HashMap<>();
        // Add some default users (in a real application, these would come from a database)
        users.put("admin", new User("admin", "admin123", "ADMIN"));
        users.put("user", new User("user", "user123", "USER"));
    }

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setLoggedIn(true);
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.setLoggedIn(false);
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null && currentUser.isLoggedIn();
    }
} 