import java.io.*;
import java.util.ArrayList;

public class UserHandler {
    private static final String USER_FILE = "users.dat";

    public static void saveUsers(ArrayList<User> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        }
    }

    public static ArrayList<User> loadUsers() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            return (ArrayList<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static User findUserById(String userId, ArrayList<User> users) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
