package com.example.deportes2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import okhttp3.*;

public class SupabaseManager {

    public static final String SUPABASE_URL = "https://rgjgyfnwqzgpgqfihcrd.supabase.co";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJnamd5Zm53cXpncGdxZmloY3JkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwNTM2MzQsImV4cCI6MjA2MzYyOTYzNH0.WHv-2iO_5hUkcfCZeF1e2WX-YI4zEw2gMmshaRq1LB4";

    private static final OkHttpClient client = new OkHttpClient();
    private static Context appContext;

    public static OkHttpClient getClient() {
        return client;
    }

    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
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
            json.put("sender_id", currentUserId);
            json.put("receiver_id", friendUserId);
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

    public static void getProfile(String userId, String accessToken, UserProfileCallback callback) {
        String url = SUPABASE_URL + "/rest/v1/profiles?id=eq." + userId + "&select=id,name,profile_img";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Fetch profile error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Fetch profile failed: " + response.message());
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);

                    if (jsonArray.length() > 0) {
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String imageUrl = obj.optString("profile_img", "error");

                        UserProfile profile = new UserProfile(id, name, imageUrl);
                        callback.onProfileFetched(profile);
                    } else {
                        callback.onError("User not found");
                    }
                } catch (JSONException e) {
                    callback.onError("Parsing error: " + e.getMessage());
                }
            }
        });
    }

    public static void checkFriendRequestExists(String currentUserId, String friendUserId, String accessToken, FriendRequestExistCallback callback) {
        try {
            String rawFilter = "or=(and(sender_id.eq." + currentUserId + ",receiver_id.eq." + friendUserId + "),"
                    + "and(sender_id.eq." + friendUserId + ",receiver_id.eq." + currentUserId + "))";

            String encodedFilter = URLEncoder.encode(rawFilter, "UTF-8");

            String url = SUPABASE_URL + "/rest/v1/friend_requests?select=*&" + encodedFilter;

            Log.d("FriendRequestCheck", "URL: " + url);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", API_KEY)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onResult(false, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.d("SupabaseResponse", responseData);
                            JSONArray jsonArray = new JSONArray(responseData);
                            boolean exists = jsonArray.length() > 0;
                            callback.onResult(exists, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onResult(false, e.getMessage());
                        }
                    } else {
                        callback.onResult(false, "Error: " + response.code());
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback.onResult(false, e.getMessage());
        }
    }


    public static void fetchIncomingFriendRequests(String currentUserId, String accessToken, Consumer<List<FriendRequest>> callback) {
        String url = SUPABASE_URL + "/rest/v1/friend_requests?receiver_id=eq." + currentUserId + "&status=eq.pending&select=*";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.accept(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<FriendRequest> requests = new ArrayList<>();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            requests.add(new FriendRequest(
                                    obj.getString("id"),
                                    obj.getString("sender_id"),
                                    obj.getString("receiver_id"),
                                    obj.getString("status")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.accept(requests);
            }
        });
    }

    public static void updateFriendRequestStatus(String requestId, String status, String accessToken, StatusUpdateCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(SUPABASE_URL + "/rest/v1/friend_requests?id=eq." + requestId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PATCH");
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject body = new JSONObject();
                body.put("status", status);

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                callback.onComplete(responseCode >= 200 && responseCode < 300);
            } catch (Exception e) {
                callback.onComplete(false);
            }
        }).start();
    }

    public static void unfriendUser(String currentUserId, String friend_id, String accessToken, okhttp3.Callback callback) {
        String url = SUPABASE_URL + "/rest/v1/friends?" +
                "or=(and(user_id.eq." + currentUserId + ",friend_id.eq." + friend_id + ")," +
                "and(user_id.eq." + friend_id + ",friend_id.eq." + currentUserId + "))";

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("apikey", API_KEY)
                .addHeader("Accept", "application/json")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }


    public static void cancelFriendRequest(String currentUserId, String friendUserId, String accessToken, okhttp3.Callback callback) {
        String url = SUPABASE_URL + "/rest/v1/friend_requests?" +
                "sender_id=eq." + currentUserId + "&receiver_id=eq." + friendUserId;

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("apikey", API_KEY)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

    public static void addToFriendsTable(String userId, String friendId, String accessToken, SupabaseCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(SUPABASE_URL + "/rest/v1/friends");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject body = new JSONObject();
                body.put("user_id", userId);
                body.put("friend_id", friendId);
                body.put("status", "accepted");

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to insert into friends table.");
                }
            } catch (Exception e) {
                callback.onFailure(e.toString());
            }
        }).start();
    }

    public static void checkFriendRequestStatus(String senderId, String receiverId, String accessToken, FriendRequestStatusCallback callback) {
        new Thread(() -> {
            try {
                String urlString = SUPABASE_URL + "/rest/v1/friend_requests?" +
                        "or=(" +
                        "and(sender_id.eq." + senderId + ",receiver_id.eq." + receiverId + ")," +
                        "and(sender_id.eq." + receiverId + ",receiver_id.eq." + senderId + ")" +
                        ")&or=(status.eq.pending,status.eq.accepted)";

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    JSONArray array = new JSONArray(sb.toString());

                    if (array.length() > 0) {
                        JSONObject request = array.getJSONObject(0);
                        String status = request.getString("status");
                        callback.onResult(status, null);
                    } else {
                        callback.onResult(null, null); // No valid request (pending or accepted)
                    }

                } else {
                    callback.onResult(null, "HTTP Error: " + responseCode);
                }

                conn.disconnect();
            } catch (Exception e) {
                callback.onResult(null, e.getMessage());
            }
        }).start();
    }

    public static void deleteRejectedFriendRequest(String currentUserId, String friendUserId, String accessToken, Callback callback) {
        // Supabase URL encoding for complex filter: use `or` with parentheses properly
        String url = SUPABASE_URL + "/rest/v1/friend_requests" +
                "?or=(and(sender_id.eq." + currentUserId + ",receiver_id.eq." + friendUserId + ",status.eq.rejected)," +
                "and(sender_id.eq." + friendUserId + ",receiver_id.eq." + currentUserId + ",status.eq.rejected))";

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")  // Optional: return deleted rows
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }



    public static void updateFriendRequestStatusBetweenUsers(String userId1, String userId2, String status, String accessToken, okhttp3.Callback callback) {
        String url = SUPABASE_URL + "/rest/v1/friend_requests?" +
                "or=(and(sender_id.eq." + userId1 + ",receiver_id.eq." + userId2 + ")," +
                "and(sender_id.eq." + userId2 + ",receiver_id.eq." + userId1 + "))";

        JSONObject json = new JSONObject();
        try {
            json.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .patch(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Prefer", "return=minimal") // or return=representation if you want the result
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

    public static void getFriendsList(String accessToken, FriendsCallback callback) {
        new Thread(() -> {
            try {
                String userId = getUserIdFromJwt(accessToken);

                // 1. Fetch from `friends` table
                String friendsUrl = SUPABASE_URL + "/rest/v1/friends" +
                        "?or=(user_id.eq." + userId + ",friend_id.eq." + userId + ")" +
                        "&status=eq.accepted" +
                        "&select=*";

                URL url = new URL(friendsUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("apikey", API_KEY);

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    callback.onError("Failed to fetch friends list: " + responseCode);
                    return;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) responseBuilder.append(line);
                in.close();

                JSONArray friendsArray = new JSONArray(responseBuilder.toString());

                // 2. Extract friend IDs
                List<String> friendIds = new ArrayList<>();
                for (int i = 0; i < friendsArray.length(); i++) {
                    JSONObject obj = friendsArray.getJSONObject(i);
                    String user_id = obj.getString("user_id");
                    String friend_id = obj.getString("friend_id");
                    String actualFriendId = user_id.equals(userId) ? friend_id : user_id;
                    friendIds.add(actualFriendId);
                }

                if (friendIds.isEmpty()) {
                    callback.onFriendsFetched(new ArrayList<>());
                    return;
                }

                // 3. Fetch profiles using OR filters
                StringBuilder orConditions = new StringBuilder();
                for (String id : friendIds) {
                    if (orConditions.length() > 0) orConditions.append(",");
                    orConditions.append("id.eq.").append(id);
                }

                String profilesUrl = SUPABASE_URL + "/rest/v1/profiles?or=(" + orConditions + ")&select=id,name,profile_img";

                URL profilesFetchUrl = new URL(profilesUrl);
                HttpURLConnection profileConn = (HttpURLConnection) profilesFetchUrl.openConnection();
                profileConn.setRequestMethod("GET");
                profileConn.setRequestProperty("Authorization", "Bearer " + accessToken);
                profileConn.setRequestProperty("apikey", API_KEY);

                int profileCode = profileConn.getResponseCode();
                if (profileCode != 200) {
                    callback.onError("Failed to fetch profiles: " + profileCode);
                    return;
                }

                BufferedReader profileIn = new BufferedReader(new InputStreamReader(profileConn.getInputStream()));
                StringBuilder profileBuilder = new StringBuilder();
                String line2;
                while ((line2 = profileIn.readLine()) != null) profileBuilder.append(line2);
                profileIn.close();

                JSONArray profileArray = new JSONArray(profileBuilder.toString());
                List<UserProfile> resultProfiles = new ArrayList<>();
                for (int i = 0; i < profileArray.length(); i++) {
                    JSONObject profile = profileArray.getJSONObject(i);
                    String id = profile.getString("id");
                    String name = profile.optString("name", "Unknown");
                    String imageUrl = profile.optString("profile_img", "");
                    resultProfiles.add(new UserProfile(id, name, imageUrl));
                }

                callback.onFriendsFetched(resultProfiles);

            } catch (Exception e) {
                e.printStackTrace();
                callback.onError("Exception: " + e.getMessage());
            }
        }).start();
    }

    public static void sendMessage(String senderId, String receiverId, String messageText, String accessToken, SupabaseCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(SUPABASE_URL + "/rest/v1/messages");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("apikey", API_KEY);  // if you use it
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("sender_id", senderId);
                jsonBody.put("receiver_id", receiverId);
                jsonBody.put("message", messageText);

                String body = jsonBody.toString();
                conn.getOutputStream().write(body.getBytes());

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess();
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    callback.onFailure("Unauthorized: Access token invalid or expired.");
                } else {
                    callback.onFailure("Server error: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure("Network error: " + e.getMessage());
            }
        }).start();
    }


    private static void updateConversation(String user1Id, String user2Id, String lastMessage, String accessToken) {
        new Thread(() -> {
            try {
                // Always order the user1/user2 the same way
                String minId = user1Id.compareTo(user2Id) < 0 ? user1Id : user2Id;
                String maxId = user1Id.compareTo(user2Id) < 0 ? user2Id : user1Id;

                URL url = new URL(SUPABASE_URL + "/rest/v1/conversations?select=id&user1_id=eq." + minId + "&user2_id=eq." + maxId);
                HttpURLConnection getConn = (HttpURLConnection) url.openConnection();
                getConn.setRequestMethod("GET");
                getConn.setRequestProperty("Authorization", "Bearer " + accessToken);
                getConn.setRequestProperty("apikey", API_KEY);

                InputStream is = getConn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
                JSONArray arr = new JSONArray(response.toString());

                boolean exists = arr.length() > 0;
                getConn.disconnect();

                if (exists) {
                    // Update conversation
                    String convId = arr.getJSONObject(0).getString("id");
                    URL patchUrl = new URL(SUPABASE_URL + "/rest/v1/conversations?id=eq." + convId);
                    HttpURLConnection patchConn = (HttpURLConnection) patchUrl.openConnection();
                    patchConn.setRequestMethod("PATCH");
                    patchConn.setRequestProperty("Authorization", "Bearer " + accessToken);
                    patchConn.setRequestProperty("Content-Type", "application/json");
                    patchConn.setDoOutput(true);

                    JSONObject updateJson = new JSONObject();
                    updateJson.put("last_message", lastMessage);
                    updateJson.put("last_updated", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date()));

                    OutputStream os = patchConn.getOutputStream();
                    os.write(updateJson.toString().getBytes());
                    os.flush();
                    os.close();
                    patchConn.disconnect();
                } else {
                    // Create conversation
                    URL postUrl = new URL(SUPABASE_URL + "/rest/v1/conversations");
                    HttpURLConnection postConn = (HttpURLConnection) postUrl.openConnection();
                    postConn.setRequestMethod("POST");
                    postConn.setRequestProperty("Authorization", "Bearer " + accessToken);
                    postConn.setRequestProperty("Content-Type", "application/json");
                    postConn.setDoOutput(true);

                    JSONObject createJson = new JSONObject();
                    createJson.put("user1_id", minId);
                    createJson.put("user2_id", maxId);
                    createJson.put("last_message", lastMessage);
                    createJson.put("last_updated", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date()));

                    OutputStream os = postConn.getOutputStream();
                    os.write(createJson.toString().getBytes());
                    os.flush();
                    os.close();
                    postConn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void fetchMessages(String userId, String friendId, String accessToken, MessagesCallback callback) {
        new Thread(() -> {
            try {
                String urlStr = SUPABASE_URL + "/rest/v1/messages?or=(and(sender_id.eq." + userId + ",receiver_id.eq." + friendId + "),and(sender_id.eq." + friendId + ",receiver_id.eq." + userId + "))&order=created_at.asc";
                URL url = new URL(urlStr);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    // Parse your JSON array to List<ChatMessage> here (depends on your ChatMessage class)
                    List<ChatMessage> messages = parseMessagesJson(response.toString());

                    callback.onSuccess(messages);

                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    callback.onError("Unauthorized: Access token invalid or expired.");
                } else {
                    callback.onError("Server error: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError("Network error: " + e.getMessage());
            }
        }).start();
    }

    public static List<ChatMessage> parseMessagesJson(String json) {
        List<ChatMessage> messages = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String id = obj.optString("id");
                String senderId = obj.optString("sender_id");
                String receiverId = obj.optString("receiver_id");
                String message = obj.optString("message");
                String createdAt = obj.optString("created_at");

                ChatMessage msg = new ChatMessage(id, senderId, receiverId, message, createdAt);
                messages.add(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public interface MessagesCallback {
        void onSuccess(List<ChatMessage> messages);  // or List<Message> if you use Message class
        void onError(String error);
    }

    public interface FriendsCallback {
        void onFriendsFetched(List<UserProfile> friends);
        void onError(String error);
    }

    // Callback Interfaces
    public interface SupabaseCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface StatusUpdateCallback {
        void onComplete(boolean success);
    }

    public interface FriendRequestStatusCallback {
        void onResult(String status, String error);
    }

    public interface FriendRequestExistCallback {
        void onResult(boolean exists, @Nullable String errorMessage);
    }

    public interface UserProfileCallback {
        void onProfileFetched(UserProfile profile);
        void onError(String error);
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
