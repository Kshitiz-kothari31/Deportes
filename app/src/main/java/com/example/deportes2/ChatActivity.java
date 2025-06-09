package com.example.deportes2;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageButton attachButton;
    View rootView;

    private List<ChatMessage> messages = new ArrayList<>();
    private MessageAdapter adapter;
    private RecyclerView recyclerView;

    private ImageButton sendBtn, backbtn;
    private EditText messageEditText;

    private String currentUserId;
    private String friendUserId;

    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    ShapeableImageView msgImage, userProfileImage;
    TextView userName, status;

    String friendId, friendUsername, friendProfileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.chat_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        friendId = getIntent().getStringExtra("friend_id");
        Log.d("ChatActivity", "Friend ID: " + friendId);
        friendUsername = getIntent().getStringExtra("friend_name");
        Log.d("ChatActivity", "Friend Username: " + friendUsername);
        friendProfileUrl = getIntent().getStringExtra("friend_profile_url");
        Log.d("ChatActivity", "Friend Profile URL: " + friendProfileUrl);


        rootView = getWindow().getDecorView().getRootView();

        messageEditText = findViewById(R.id.messageEditText);
        attachButton = findViewById(R.id.attachButton);
        setupKeyboardListener(rootView, messageEditText, attachButton);

        msgImage = findViewById(R.id.messageImg);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        selectedImageUri = result.getData().getData();
                        msgImage.setImageURI(selectedImageUri);
                        msgImage.setVisibility(View.VISIBLE);
                    }
                }
        );

        attachButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getString("user_id", null);  // Make sure saved on login
        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        friendUserId = getIntent().getStringExtra("friend_id");
        String friendName = getIntent().getStringExtra("friend_name");
        if (friendName != null) {
            setTitle(friendName);
        }

        sendBtn = findViewById(R.id.sendButton);
        backbtn = findViewById(R.id.leftarrowbtn);
        userProfileImage = findViewById(R.id.chatProfileImage);

        Glide.with(this)
                .load(friendProfileUrl)
                .placeholder(R.drawable.profile_circle)
                .into(userProfileImage);

        userName = findViewById(R.id.chatUserName);
        userName.setText(friendUsername);
        status = findViewById(R.id.status);
        messageEditText = findViewById(R.id.messageEditText);
        recyclerView = findViewById(R.id.messagesRecyclerView);

        adapter = new MessageAdapter(messages, currentUserId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMessagesWithTokenCheck();

        sendBtn.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessageWithTokenCheck(messageText);
            }
        });

        backbtn.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void loadMessagesWithTokenCheck() {
        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                SupabaseManager.fetchMessages(currentUserId, friendUserId, accessToken, new SupabaseManager.MessagesCallback() {
                    @Override
                    public void onSuccess(List<ChatMessage> fetchedMessages) {
                        runOnUiThread(() -> {
                            messages.clear();
                            messages.addAll(fetchedMessages);
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messages.size() - 1);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Failed to load messages: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
            }

            @Override
            public void onError(String errorMsg) {
                runOnUiThread(() -> {
                    Toast.makeText(ChatActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    finish();  // Close activity if session expired
                });
            }
        });
    }

    private void sendMessageWithTokenCheck(String messageText) {
        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                SupabaseManager.sendMessage(currentUserId, friendUserId, messageText, accessToken, new SupabaseManager.SupabaseCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> {
                            messageEditText.setText("");
                            loadMessagesWithTokenCheck();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Error sending message: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
            }

            @Override
            public void onError(String errorMsg) {
                runOnUiThread(() -> {
                    Toast.makeText(ChatActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void setupKeyboardListener(View rootView, EditText prompt, ImageButton attachButton) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();

            int keypadHeight = screenHeight - r.bottom;
            boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;

            if (isKeyboardVisible) {
                setBottomMargin(prompt, 300);
                setBottomMargin(attachButton, 300);
            } else {
                setBottomMargin(prompt, 25);
                setBottomMargin(attachButton, 25);
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
