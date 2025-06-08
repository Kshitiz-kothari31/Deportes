package com.example.deportes2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import okhttp3.*;

public class SupabaseManager {

    public static final String SUPABASE_URL = "https://rgjgyfnwqzgpgqfihcrd.supabase.co";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJnamd5Zm53cXpncGdxZmloY3JkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwNTM2MzQsImV4cCI6MjA2MzYyOTYzNH0.WHv-2iO_5hUkcfCZeF1e2WX-YI4zEw2gMmshaRq1LB4";

    private static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static void upsertProfile(String userId, String name, String username, String bio,
                                     @Nullable String profileImageUrl, @Nullable String bgImageUrl, String accessToken,
                                     Callback callback) {
        JSONObject json = new JSONObject();

        try {
            json.put("id", userId);
            json.put("name", name);
            json.put("username", username);
            json.put("bio", bio);
            if (profileImageUrl != null) json.put("profile_img", profileImageUrl);
            if (bgImageUrl != null) json.put("bg_img", bgImageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/profiles")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void uploadImageToBucket(Context context, String path, Uri imageUri , String accessToken, ImageUploadCallback  callback){
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if(inputStream == null){
                callback.onFailure("Unable to open image stream.");
                return;
            }

            byte[] imageBytes = readBytesFromInputStream(inputStream);
            RequestBody requestBody = RequestBody.create(imageBytes, MediaType.parse("image/*"));

            Request request = new Request.Builder()
                    .url(SUPABASE_URL + "/storage/v1/object/posts-images/" + path)
                    .addHeader("apikey", API_KEY)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "image/*")
                    .put(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFailure(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String publicUrl = "https://rgjgyfnwqzgpgqfihcrd.supabase.co/storage/v1/object/public/posts-images/" + path;
                        callback.onSuccess(publicUrl);
                    } else {
                        callback.onFailure("Upload failed with code: " + response.code());
                    }
                }
            });


        }catch (Exception e) {
            callback.onFailure("Exception: " + e.getMessage());
        }
    }

    private static byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    public interface ImageUploadCallback {
        void onSuccess(String imageUrl);
        void onFailure(String errorMessage);
    }

    public static void getProfile(String userId, String accessToken, Callback callback) {
        HttpUrl url = HttpUrl.parse(SUPABASE_URL + "/rest/v1/profiles")
                .newBuilder()
                .addQueryParameter("id", "eq." + userId)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void refreshAccessToken(Context context, RefreshTokenCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("refresh_token", null);

        if (refreshToken == null) {
            callback.onFailure();
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL(SUPABASE_URL + "/auth/v1/token?grant_type=refresh_token");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String body = "{\"refresh_token\":\"" + refreshToken + "\"}";
                conn.getOutputStream().write(body.getBytes());

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONObject json = new JSONObject(response.toString());
                    String newAccessToken = json.getString("access_token");
                    String newRefreshToken = json.getString("refresh_token");
                    long expiresIn = json.getLong("expires_in");

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("access_token", newAccessToken);
                    editor.putString("refresh_token", newRefreshToken);
                    editor.putLong("expires_at", (System.currentTimeMillis() / 1000) + expiresIn);
                    editor.apply();

                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }

            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure();
            }
        }).start();
    }

    public interface RefreshTokenCallback {
        void onSuccess();
        void onFailure();
    }

    public static void checkAndRefreshToken(Context context, TokenCheckCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        long expiresAt = prefs.getLong("expires_at", 0);
        long currentTime = System.currentTimeMillis() / 1000;

        if (currentTime >= expiresAt) {
            // Token expired, refresh it
            refreshAccessToken(context, new RefreshTokenCallback() {
                @Override
                public void onSuccess() {
                    SharedPreferences prefsReloaded = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                    String newAccessToken = prefsReloaded.getString("access_token", null);

                    callback.onTokenReady(newAccessToken);
                }

                @Override
                public void onFailure() {
                    callback.onError("Session expired. Please log in again.");
                }
            });
        } else {
            // Token still valid
            String currentToken = prefs.getString("access_token", null);
            if (currentToken == null) {
                Log.d("Debug", currentToken);
                return;
            }
            callback.onTokenReady(currentToken);
        }
    }

    // In SupabaseManager.java
    public static void fetchAllPosts(String accessToken, PostsCallback callback) {
        new Thread(() -> {
            String url = SUPABASE_URL + "/rest/v1/Posts?select=*,profiles(name,profile_img)&order=created_at.desc";
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    conn.disconnect();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Post>>() {}.getType();
                    List<Post> postList = gson.fromJson(sb.toString(), listType);

                    // Shuffle
                    Collections.shuffle(postList);

                    callback.onPostsFetched(postList);
                } else {
                    callback.onError("HTTP Error: " + responseCode);
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public static void fetchLikeAndCommentCounts(String postId, String accessToken, CountsCallback callback){
        new Thread(() -> {
            try {
                HttpUrl likesUrl = HttpUrl.parse(SUPABASE_URL + "/rest/v1/Likes")
                        .newBuilder()
                        .addQueryParameter("post_id", "eq." + postId)
                        .build();

                HttpUrl commentUrl = HttpUrl.parse(SUPABASE_URL + "/rest/v1/Comments")
                        .newBuilder()
                        .addQueryParameter("post_id", "eq." + postId)
                        .build();

                Request likesRequest = new Request.Builder()
                        .url(likesUrl)
                        .addHeader("apikey", API_KEY)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .get()
                        .build();

                Request commentsRequest = new Request.Builder()
                        .url(commentUrl)
                        .addHeader("apikey", API_KEY)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .get()
                        .build();

                Response likesResponse = client.newCall(likesRequest).execute();
                Response commentsResponse = client.newCall(commentsRequest).execute();

                int likeCount = new JSONArray(likesResponse.body().string()).length();
                int commentCount = new JSONArray(commentsResponse.body().string()).length();

                callback.onCountFetched(likeCount, commentCount);
            }catch (Exception e){
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public static void likePost(Context context, String postId) {
        String accessToken = getAccessToken(context);
        String userId = getUserIdFromJwt(accessToken);

        JSONObject json = new JSONObject();
        try {
            json.put("user_id", userId);
            json.put("post_id", postId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String url = "https://rgjgyfnwqzgpgqfihcrd.supabase.co/rest/v1/Likes";

        JsonObjectRequest request = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                url,
                json,
                response -> {},
                error -> Log.e("LIKE_POST", "Failed: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("apikey", SupabaseManager.API_KEY);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public static void unlikePost(Context context, String postId) {
        String accessToken = getAccessToken(context);
        String userId = getUserIdFromJwt(accessToken);

        String url = "https://rgjgyfnwqzgpgqfihcrd.supabase.co/rest/v1/Likes?user_id=eq." + userId + "&post_id=eq." + postId;

        StringRequest request = new StringRequest(
                com.android.volley.Request.Method.DELETE,
                url,
                response -> {},
                error -> Log.e("UNLIKE_POST", "Failed: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("apikey", SupabaseManager.API_KEY);
                headers.put("Content-Type", "application/json");
                headers.put("Prefer", "return=minimal");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        return prefs.getString("access_token", null);
    }

    public static String getUserIdFromJwt(String accessToken) {
        if (accessToken == null || !accessToken.contains(".")) return null;
        try {
            String[] parts = accessToken.split("\\.");
            String payload = parts[1];

            // Pad base64 if necessary
            int padding = 4 - (payload.length() % 4);
            if (padding != 4) {
                for (int i = 0; i < padding; i++) {
                    payload += "=";
                }
            }

            byte[] decodedBytes = android.util.Base64.decode(payload, android.util.Base64.DEFAULT);
            String decodedJson = new String(decodedBytes);
            JSONObject json = new JSONObject(decodedJson);
            return json.getString("sub");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void fetchPostsByUser(String userId, String accessToken, PostsCallback callback) {
        new Thread(() -> {
            String url = SUPABASE_URL + "/rest/v1/Posts?user_id=eq." + userId + "&order=created_at.desc&select=*,profiles(name,profile_img)";
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    conn.disconnect();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Post>>() {}.getType();
                    List<Post> postList = gson.fromJson(sb.toString(), listType);

                    callback.onPostsFetched(postList);
                } else {
                    callback.onError("HTTP Error: " + responseCode);
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public static void sendFriendRequest(String currentUserId, String friendUserId, String accessToken, Callback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("from_user_id", currentUserId);
            json.put("to_user_id", friendUserId);
            json.put("status", "pending");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/friend_requests")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }


    public interface CountsCallback{
        void onCountFetched(int likeCount, int commentCount);
        void onError(String error);
    }

    public interface PostsCallback {
        void onPostsFetched(List<Post> posts);
        void onError(String error);
    }

    public interface TokenCheckCallback {
        void onTokenReady(String validAccessToken);
        void onError(String errorMessage);
    }

}
