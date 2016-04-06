package turnip.turnip;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class API {
    final static String TAG = "API";
    final static String API_URL = "http://databaseproject.jaxbot.me";
    final static String STATIC_URL = "http://espur.jaxbot.me/images/";
    static String authkey;
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

    public static boolean createUser(String username, String password, String email) {
        JsonObject json = new JsonObject();
        json.addProperty("name", username);
        json.addProperty("password", password);
        json.addProperty("email", email);

        try {
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
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUser failed to create JSONObject");
        }
        return false;
    }

    public static boolean login(String email, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        try {
            JsonObject result = postJson(API_URL + "/signup", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            // We're good!
            authkey = result.get("login_token").getAsString();
            saveAuthkey();

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUser failed to create JSONObject");
        }
        return false;
    }

    public static boolean login(String email, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        try {
            JsonObject result = postJson(API_URL + "/signup", json);
            if (result == null) return false;

            if (!result.get("success").getAsBoolean())
                return false;

            // We're good!
            authkey = result.get("login_token").getAsString();
            saveAuthkey();

            return true;
        } catch (JsonIOException e) {
            Log.e(TAG, url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUser failed to create JSONObject");
        }
        return false;
    }

    public static Bitmap getImage(String file) throws MalformedURLException {
        Bitmap bmp = BitmapFactory.decodeStream(getHTTPBytes(STATIC_URL + "/" + file + ".jpg"));
        return bmp;
    }

    private static JsonObject getJson(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Access-Token", authkey);
            try {
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

    private static InputStream getHTTPBytes(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Access-Token", authkey);

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return in;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        System.out.println(url);

        return null;
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

