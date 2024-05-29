package server;

//Imports
import com.google.gson.Gson;

public class JsonUtil {
    private static final Gson GSON = new Gson();
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }
}
