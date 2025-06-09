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

import com.google.gson.reflect.TypeToken;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

//    public static void getFriendsList(String currentUserId, String accessToken, FriendsCallback callback) {
//        // Get accepted requests where currentUserId is either sender or receiver
//        String url = SUPABASE_URL + "/rest/v1/friend_requests?or=(from_user_id.eq." + currentUserId + ",to_user_id.eq." + currentUserId + ")&status=eq.accepted";
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("apikey", API_KEY)
//                .addHeader("Authorization", "Bearer " + accessToken)
//                .get()
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callback.onResult(null, e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    // parse the responseData JSON to extract friend user ids (both sender and receiver)
//                    // exclude the currentUserId itself, keep only the friend user IDs
//                    // Then fetch user profiles for these IDs and pass them back in callback
//                } else {
//                    callback.onResult(null, "Error: " + response.code());
//                }
//            }
//        });
//    }


    public static void fetchUserProfile(String userId, String accessToken, UserProfileCallback callback) {
        String url = SUPABASE_URL + "/rest/v1/profiles?id=eq." + userId + "&select=id,name,profileImageUrl";

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
                        String imageUrl = obj.optString("profileImageUrl", "");

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

    public static void updateFriendRequestStatus(String requestId, String newStatus, String accessToken, CallbackBoolean callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("status", newStatus);  // e.g., "accepted" or "rejected"
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onResult(false);
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/friend_requests?id=eq." + requestId)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .patch(body)  // PATCH for updating the record
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResult(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResult(response.isSuccessful());
            }
        });
    }

    public static void unfriendUser(String currentUserId, String otherUserId, String accessToken, okhttp3.Callback callback) {
        // Construct URL to delete friendship where (user1Id=currentUserId AND user2Id=otherUserId) OR vice versa
        // Assuming your friends table stores both user1Id and user2Id columns to represent friendship

        String filter = "or=(and(sender_id.eq." + currentUserId + ",receiver_id.eq." + otherUserId + "),and(sender_id.eq." + otherUserId + ",receiver_id.eq." + currentUserId + "))";
        String encodedFilter = Uri.encode(filter);
        String url = SUPABASE_URL + "/rest/v1/friends?" + encodedFilter;

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("apikey", API_KEY)
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

                try{
                    JSONObject body = new JSONObject();
                    body.put("user_id", userId);
                    body.put("friend_id", friendId);
                    body.put("status", "accepted");
                    Log.d("ADD_FRIEND_BODY", body.toString()); // Optional, remove if not in schema
                }catch (JSONException e){
                    e.printStackTrace();
                    callback.onFailure("JSON Error: " + e.getMessage());
                    return;
                }

                int responseCode = conn.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    callback.onSuccess();
                    Log.d("SUPABASE_SUCCESS", "Add friend success");
                } else {
                    InputStream errorStream = conn.getErrorStream();
                    String errorMsg = new BufferedReader(new InputStreamReader(errorStream))
                            .lines().collect(Collectors.joining("\n"));
                    callback.onFailure("Error: " + responseCode + " - " + errorMsg);
                    Log.e("SUPABASE_ERROR", "Add friend failed: " + errorMsg);
                }

            } catch (Exception e) {
                callback.onFailure("Exception: " + e.getMessage());
                Log.e("SUPABASE_EXCEPTION", "Exception occurred", e);
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
        HttpUrl url = HttpUrl.parse(SUPABASE_URL + "/rest/v1/friend_requests")
                .newBuilder()
                .addQueryParameter("sender_id", "eq." + currentUserId)
                .addQueryParameter("receiver_id", "eq." + friendUserId)
                .addQueryParameter("status", "eq.rejected")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }

    public interface FriendRequestStatusCallback {
        void onResult(String status, String error);
    }

    public interface SupabaseCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface CallbackBoolean {
        void onResult(boolean success);
    }

    public interface FriendRequestExistCallback {
        void onResult(boolean exists, @Nullable String errorMessage);
    }

    public interface UserProfileCallback {
        void onProfileFetched(UserProfile profile);
        void onError(String error);
    }

    public interface FriendsCallback {
        void onFriendsFetched(List<UserProfile> friends);
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
