package server;

//Imports
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {
    private static final Gson GSON = new Gson();
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static boolean isValidJsonString(String jsonString) {
        try {
            var jsonMaybe = JsonParser.parseString(jsonString);
            return jsonMaybe.isJsonArray() || jsonMaybe.isJsonObject();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}
