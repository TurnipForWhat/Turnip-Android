package turnip.turnip;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class API {
    final static String TAG = "API";
    final static String API_URL = "http://databaseproject.jaxbot.me";
    final static String STATIC_URL = "http://databaseproject.jaxbot.me";
    public static String authkey = "";
    static Context ctx = null;

    public static void init(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("API", 0);
        authkey = preferences.getString("authkey", "");
        API.ctx = ctx;
    }

    public static void signOut() {
        authkey = "";
        saveAuthkey();
    }

    public static boolean signUpWithFacebook(String id, String name, String email) {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("fbid", id);
        json.addProperty("email", email);

        JsonObject result = postJson(API_URL + "/signup", json);
        if (result == null) return false;

        try {
            if (!result.get("success").getAsBoolean())
                return false;

            // We're good!
            authkey = result.get("login_token").getAsString();
            saveAuthkey();

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, "createUser failed to find success");
            return false;
        }

    }

    // Yes, I know this is hella vulnerable. It's for a class™
    public static boolean loginWithFacebook(String facebook_id) {
        JsonObject json = new JsonObject();
        json.addProperty("facebook_id", facebook_id);

        try {
            JsonObject result = postJson(API_URL + "/login/facebook", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            // We're good!
            authkey = result.get("login_token").getAsString();
            saveAuthkey();

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean createUser(String username, String password, String email) {
        JsonObject json = new JsonObject();
        json.addProperty("name", username);
        json.addProperty("password", password);
        json.addProperty("email", email);

            JsonObject result = postJson(API_URL + "/signup", json);
            if (result == null) return false;

            try {
                if (!result.get("success").getAsBoolean())
                    return false;

                // We're good!
                authkey = result.get("login_token").getAsString();
                saveAuthkey();

                return true;
            } catch (JsonIOException e) {
                Log.e(TAG, "createUser failed to find success");
                return false;
            }

    }

    public static boolean login(String email, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        try {
            JsonObject result = postJson(API_URL + "/login", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            // We're good!
            authkey = result.get("login_token").getAsString();
            saveAuthkey();

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean setInterests(ArrayList<Integer> interests, ArrayList<Integer> antiInterests) {
        JsonObject json = new JsonObject();
        JsonArray interests_jsonArray = new JsonArray();
        JsonArray antiInterests_jsonArray = new JsonArray();
        for (int i = 0; i < interests.size(); i++) {
            interests_jsonArray.add(new JsonPrimitive(interests.get(i)));
        }
        for (int i = 0; i < antiInterests.size(); i++) {
            antiInterests_jsonArray.add(new JsonPrimitive(antiInterests.get(i)));
        }
        json.add("interests", interests_jsonArray);
        json.add("anti_interests", antiInterests_jsonArray);

        try {
            JsonObject result = postJson(API_URL + "/interests", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            return true;
        } catch (JsonIOException e) {
            System.out.println(e);
            Log.e(TAG, e.toString());
        }
        return false;
    }

    public static UserFeed feed() {
        try {
            JsonObject result = getJson(API_URL + "/feed");
            if (result == null) return null;

            Boolean status = result.get("status").getAsBoolean();
            ArrayList<User> friends = new ArrayList<User>();

            JsonArray jsonFriends = result.get("friends").getAsJsonArray();
            for (int i = 0; i < jsonFriends.size(); i++) {
                JsonObject obj = jsonFriends.get(i).getAsJsonObject();
                User friend = new User(obj.get("name").getAsString(), obj.get("id").getAsInt(), obj.get("profile_picture_id").getAsString(), obj.get("status").getAsBoolean());
                friends.add(friend);
            }
            UserFeed uf = new UserFeed(status, friends);
            return uf;
        } catch (JsonIOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public static boolean saveGMSToken(String token) {
        if (authkey.equals("")) return false;

        JsonObject json = new JsonObject();
        json.addProperty("token", token);

        try {
            JsonObject result = postJson(API_URL + "/gms", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean toggle(Boolean readyToTurnip) {
        JsonObject json = new JsonObject();
        json.addProperty("status", readyToTurnip);

        try {
            JsonObject result = postJson(API_URL + "/toggle", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }
    public static Bitmap getImage(String file) throws MalformedURLException {
        Log.i(TAG, "Get image" + file);
        byte[] bytes = getHTTPBytes(STATIC_URL + "/" + file + ".jpg");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bmp;
    }

    private static JsonObject getJson(String urlString) {
        try {
            System.out.println(urlString);
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Access-Token", authkey);
            try {
                System.out.println("mental errors");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
                System.out.println("mental errors2");
                return jsonObject;
            } finally {
                urlConnection.disconnect();
            }
         } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    private static byte[] getHTTPBytes(String urlString) throws MalformedURLException {
        Log.i(TAG, urlString);
        URL url = new URL(urlString);

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Access-Token", authkey);

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return getBytesFromInputStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        System.out.println(url);

        return null;
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();)
        {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }

    private static JsonObject postJson(String urlString, JsonObject json) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("X-Access-Token", authkey);
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                OutputStreamWriter outWriter = new OutputStreamWriter(out);

                JsonWriter writer = new JsonWriter(outWriter);
                Gson gson = new Gson();
                gson.toJson(json, writer);
                writer.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
                return jsonObject;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            System.out.println(e);
            Log.e(TAG, e.toString());
        }

        return null;
    }

    public static JsonObject postHTTPBytes(String urlString, byte[] data) throws MalformedURLException {
        URL url = new URL(urlString);

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Access-Token", authkey);
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(data);
                out.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
                return jsonObject;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }



    private static void saveAuthkey()
    {
        SharedPreferences preferences = ctx.getSharedPreferences("API", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("authkey", authkey);
        editor.apply();
    }

}

