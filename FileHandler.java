import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private static final String FILE_NAME = "houses.dat";

    public static void saveHouses(ArrayList<House> houses) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(houses);
        }
    }

    public static ArrayList<House> loadHouses() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ArrayList<House>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
