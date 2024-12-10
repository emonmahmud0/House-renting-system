import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String name;
    private String role; // "Tenant" or "Landlord"

    public User(String userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name + ", Role: " + role;
    }
}
