import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Singleton class to store a Gson instance with the correct attributes.
 */
public class GsonSingleton {
    private final static Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    private GsonSingleton() {}

    public static Gson getInstance() {
        return gson;
    }
}
