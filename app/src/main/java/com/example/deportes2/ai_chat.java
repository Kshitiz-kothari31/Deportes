package com.example.deportes2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

public class ai_chat extends AppCompatActivity {

    EditText prompt;
    ImageButton sendPrompt;
    private ChatFutures chatModel;
    private LinearLayout chatResponse;
    TextView welcometext;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ai_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ai_chat), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prompt = findViewById(R.id.prompt);
        sendPrompt = findViewById(R.id.sendPrompt);
        chatResponse = findViewById(R.id.chatResponse);
        welcometext = findViewById(R.id.WelcomeText);
        chatModel = getChatModel();
        rootView = getWindow().getDecorView().getRootView();

        setupKeyboardListener(rootView, prompt, welcometext);


        // ✅ Get username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String userName = prefs.getString("name", "User"); // fallback to "User" if null

        // ✅ Set the welcome text dynamically
        welcometext.setText("Hello, " + userName);

        sendPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View currentFocusView = getCurrentFocus();
                if (currentFocusView == null) {
                    currentFocusView = new View(ai_chat.this);
                }
                imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);

                welcometext.setVisibility(View.GONE);
                String query = prompt.getText().toString();
                prompt.setText("");

                chatBody("You", query, getDrawable(R.drawable.user), false);
                chatBody("AI", "", getDrawable(R.drawable.ai_btn), true);
                GeminiResp.getResponse(chatModel, query, new ResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        int lastIndex = chatResponse.getChildCount() - 1;
                        View lastView = chatResponse.getChildAt(lastIndex);
                        TextView typingIndicator = lastView.findViewById(R.id.typingIndicator);
                        if (typingIndicator != null && typingIndicator.getVisibility() == View.VISIBLE) {
                            chatResponse.removeViewAt(lastIndex);
                        }

                        chatBody("AI", response, getDrawable(R.drawable.ai_btn), false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        int lastIndex = chatResponse.getChildCount() - 1;
                        View lastView = chatResponse.getChildAt(lastIndex);
                        TextView typingIndicator = lastView.findViewById(R.id.typingIndicator);
                        if (typingIndicator != null && typingIndicator.getVisibility() == View.VISIBLE) {
                            chatResponse.removeViewAt(lastIndex);
                        }

                        chatBody("AI", "Please try again!", getDrawable(R.drawable.ai_btn), false);
                    }
                });
            }
        });
    }

    private ChatFutures getChatModel(){

        GeminiResp model = new GeminiResp();
        GenerativeModelFutures modelFutures = model.getModel();

        return modelFutures.startChat();
    }

    private void chatBody(String userName, String query, Drawable img, boolean isTyping) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_chat_msg, null);

        TextView name = view.findViewById(R.id.TextView1);
        TextView message = view.findViewById(R.id.textView2);
        ImageView imageView = view.findViewById(R.id.imgView);
        TextView typingIndicator = view.findViewById(R.id.typingIndicator);

        name.setText(userName);
        imageView.setImageDrawable(img);
        if (isTyping) {
            typingIndicator.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
        } else {
            typingIndicator.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            message.setText(query);
        }

        chatResponse.addView(view);
        chatResponse.setVisibility(View.VISIBLE);
        NestedScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void setupKeyboardListener(View rootView, EditText prompt, TextView welcomeText) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();

            int keypadHeight = screenHeight - r.bottom;
            boolean isKeyboardVisible = keypadHeight > screenHeight * 0.15;

            if (isKeyboardVisible) {
                setBottomMargin(prompt, 300);
                setBottomMargin(welcomeText, 500);
            } else {
                setBottomMargin(prompt, 25);
                setBottomMargin(welcomeText, 0);
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