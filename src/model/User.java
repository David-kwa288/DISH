package model;

import java.util.List;

public class User {
    private String username;
    private String passwordHash;
    private List<Celebrity> favoriteCelebrities;

    // Constructor
    public User(String username, String passwordHash, List<Celebrity> favoriteCelebrities) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.favoriteCelebrities = favoriteCelebrities;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Celebrity> getFavoriteCelebrities() {
        return favoriteCelebrities;
    }

    public void setFavoriteCelebrities(List<Celebrity> favoriteCelebrities) {
        this.favoriteCelebrities = favoriteCelebrities;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", favoriteCelebrities=" + favoriteCelebrities +
                '}';
    }
}
