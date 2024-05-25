package DatabaseObjects;

public class User {
    private String username;
    private String password;
    private String email;
    private String authToken;


    public User(String username, String pass, String email) {
        this.username = username;
        this.password = pass;
        this.email = email;
    }

    //Getters and setters
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
