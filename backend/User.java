package backend;

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

    public String getUsername() {
        return username;
    }

    public boolean changeUsername(String newUsername){
        this.username = newUsername;
        return true;
    };

    public boolean changePassword(String username, String oldPassword, String newPassword){
        if (this.password != null && authenticate(username, oldPassword)) {
            this.password = newPassword;
            return true;
        }
        else return false;
    };

    public boolean authenticate(String username, String inputPassword) {
        if (this.username.equals(username) && inputPassword != null) return (inputPassword.equals(password));
        else if (this.username.equals(username) && inputPassword == null) return true;
        else return false;
    };
}