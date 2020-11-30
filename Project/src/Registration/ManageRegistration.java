package Registration;

public class ManageRegistration {

    private UserSystem userSystem;

    public ManageRegistration(UserSystem userSystem) {
        setUserSystem(userSystem);
    }

    public String registerUser(String email, String username, String password) {
        return userSystem.registerUser(email, username, password);
    }

    public void displayUsers() {
        userSystem.displayUsers();
    }

    public void setUserSystem(UserSystem userSystem) {
        this.userSystem = userSystem;
    }
}