package com.example.deportes2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.*;

public class SupabaseManager {

    private static final String SUPABASE_URL = "https://rgjgyfnwqzgpgqfihcrd.supabase.co";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJnamd5Zm53cXpncGdxZmloY3JkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwNTM2MzQsImV4cCI6MjA2MzYyOTYzNH0.WHv-2iO_5hUkcfCZeF1e2WX-YI4zEw2gMmshaRq1LB4";

    private static final OkHttpClient client = new OkHttpClient();

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

    public static String getPublicUrl(String bucketName, String fileName) {
        return SUPABASE_URL + "/storage/v1/object/public/" + bucketName + "/" + fileName;
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

    public static void listVideosInFolder(String bucketName, String folderPath, Callback callback) {
        HttpUrl url = HttpUrl.parse(SUPABASE_URL + "/storage/v1/object/list/" + bucketName)
                .newBuilder()
                .addQueryParameter("prefix", folderPath)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }
}
