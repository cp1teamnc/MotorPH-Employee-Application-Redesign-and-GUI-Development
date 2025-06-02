package GUI;

public class User {
    private String username;
    private String password;
    private String role;
    private boolean isLoggedIn;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLoggedIn = false;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
} 