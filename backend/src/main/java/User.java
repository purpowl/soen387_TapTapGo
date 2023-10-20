package src.main.java;

public abstract class User {
    protected String username;
    protected String password;

    public User(String username) {
        this.username = username;
        this.password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean changeUsername(String oldUsername, String newUsername, String password){
        if (oldUsername == null || newUsername == null) return false;
        else if (oldUsername.equals(newUsername)) return false;
        else if (this.username.equals(oldUsername) && this.password.equals(password)) {
            this.username = newUsername;
            return true;
        }
        else return false;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword){
        if (this.password != null && authenticate(username, oldPassword)) {
            this.password = newPassword;
            return true;
        }
        else return false;
    };

    // if the user has a password, authenticate them
    public boolean authenticate(String username, String inputPassword) {
        if (this.username.equals(username) && inputPassword != null) return (inputPassword.equals(password));
        else return false;
    };
}