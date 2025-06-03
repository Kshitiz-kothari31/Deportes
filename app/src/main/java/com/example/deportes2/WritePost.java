package com.example.deportes2;

//import static com.example.deportes2.SupabaseManager.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WritePost extends AppCompatActivity {

    Toolbar post_toolbar;
    View rootView;

    private AppCompatButton postBtn;
    private EditText postMessage;
    private ImageView postImg, addImgIcon;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private static final String SUPABASE_URL = "https://rgjgyfnwqzgpgqfihcrd.supabase.co";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJnamd5Zm53cXpncGdxZmloY3JkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwNTM2MzQsImV4cCI6MjA2MzYyOTYzNH0.WHv-2iO_5hUkcfCZeF1e2WX-YI4zEw2gMmshaRq1LB4";

    OkHttpClient client = SupabaseManager.getClient();
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_write_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.writepost), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rootView = getWindow().getDecorView().getRootView();

        post_toolbar = findViewById(R.id.postActivityToolbar);
        setupKeyboardListener(rootView, post_toolbar);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        accessToken = prefs.getString("access_token", null);

        addImgIcon = findViewById(R.id.add_img_icon);
        postImg = findViewById(R.id.postImage);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        selectedImageUri = result.getData().getData();
                        postImg.setImageURI(selectedImageUri);
                        postImg.setVisibility(View.VISIBLE);
                    }
                }
        );

        addImgIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        postBtn = findViewById(R.id.postButton);
        postMessage = findViewById(R.id.postMessage);

        postBtn.setOnClickListener(v -> {
            String message = postMessage.getText().toString().trim();

            if (message.isEmpty()) {
                Toast.makeText(WritePost.this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            SupabaseManager.checkAndRefreshToken(WritePost.this, new SupabaseManager.TokenCheckCallback() {
                @Override
                public void onTokenReady(String validAccessToken) {
                    accessToken = validAccessToken;

                    if (selectedImageUri != null) {
                        String imagePath = "post_images/" + UUID.randomUUID() + ".jpg";

                        SupabaseManager.uploadImageToBucket(
                                WritePost.this,
                                imagePath,
                                selectedImageUri,
                                accessToken,
                                new SupabaseManager.ImageUploadCallback() {
                                    @Override
                                    public void onSuccess(String imageUrl) {
                                        fetchUserIdAndPost(message, imageUrl);
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        runOnUiThread(() ->
                                                Toast.makeText(WritePost.this, "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                }
                        );
                    } else {
                        fetchUserIdAndPost(message, null);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(WritePost.this, errorMessage, Toast.LENGTH_SHORT).show();
                        // You can redirect to LoginActivity here if needed
                    });
                }
            });
        });

    }

    private void fetchUserIdAndPost(String message, @Nullable String imageUrl){

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/user")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("apikey", API_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(WritePost.this, "Failed to fetch user", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() ->
                            Toast.makeText(WritePost.this, "Auth error: " + response.message(), Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject userJson = new JSONObject(responseBody);
                    String userId = userJson.getString("id");

                    // Now upload the post with the correct user ID
                    uploadPostToSupabase(userId, message, imageUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void uploadPostToSupabase(String userId, String content, @Nullable String imageUrl){
        JSONObject json = new JSONObject();
        try {
            json.put("id", UUID.randomUUID().toString());
            json.put("user_id", userId);
            json.put("content", content);
            if(imageUrl != null){
                json.put("image_url", imageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/Posts")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(WritePost.this, "Failed to upload post", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(WritePost.this, "Post uploaded!", Toast.LENGTH_SHORT).show();
                        postMessage.setText("");
                        finish(); // or update UI
                    } else {
                        String errorBody = response.body().toString();
                        Log.e("PostUpload", "Failed: " + errorBody);
                    }
                });
            }
        });

    }

    private void setupKeyboardListener(View rootView, Toolbar postToolbar) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();

            int keypadHeight = screenHeight - r.bottom;
            boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;

            if (isKeyboardVisible) {
                setBottomMargin(postToolbar, 300);
            } else {
                setBottomMargin(postToolbar, 25);
            }
        });
    }

    private void setBottomMargin(View view, int bottomMarginDp) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int bottomMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                bottomMarginDp,
                getResources().getDisplayMetrics()
        );
        params.bottomMargin = bottomMarginPx;
        view.setLayoutParams(params);
    }
}