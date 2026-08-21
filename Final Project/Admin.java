public class Admin extends Person {

    private String username;
    private String password;

    public Admin(String name, String username, String password) {
        super(name);
        this.username = username;
        this.password = password;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public String describeRole() {
        return name + " (Administrator)";
    }
}
