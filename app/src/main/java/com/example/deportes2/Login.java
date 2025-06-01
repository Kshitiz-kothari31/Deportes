package com.example.deportes2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    EditText email, password;
    AppCompatButton login;
    TextView signup_page;

    String supabaseProjectUrl = "https://rgjgyfnwqzgpgqfihcrd.supabase.co";
    String supabaseApiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJnamd5Zm53cXpncGdxZmloY3JkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwNTM2MzQsImV4cCI6MjA2MzYyOTYzNH0.WHv-2iO_5hUkcfCZeF1e2WX-YI4zEw2gMmshaRq1LB4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.loginBtn);
        signup_page = findViewById(R.id.signup_link);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();

                if (TextUtils.isEmpty(user_email)) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(user_password)) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                } else if (user_password.length() < 6) {
                    password.setError("Password must be at least 6 characters");
                    password.requestFocus();
                    return;
                }

                loginUser(user_email, user_password);
            }
        });

        signup_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, signUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        String json = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";

        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(supabaseProjectUrl + "/auth/v1/token?grant_type=password")
                .post(body)
                .addHeader("apikey", supabaseApiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Login.this, "Network error", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String accessToken = jsonObject.getString("access_token");

                        JSONObject userObject = jsonObject.getJSONObject("user"); // ✅ Fix here
                        String userId = userObject.getString("id");

                        String refreshToken = jsonObject.getString("refresh_token");

                        SharedPreferences.Editor editor = getSharedPreferences("AuthPrefs", MODE_PRIVATE).edit();
                        editor.putString("access_token", accessToken);
                        editor.putString("refresh_token", refreshToken);
                        editor.putString("user_id", userId);
                        long expiresIn = jsonObject.getLong("expires_in"); // seconds until expiry
                        long expiresAt = (System.currentTimeMillis() / 1000) + expiresIn; // current unix timestamp + expiresIn

                        editor.putLong("expires_at", expiresAt);
                        editor.apply();

                        runOnUiThread(() -> {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(Login.this, "Login parsing error", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    String error = response.body().string();
                    runOnUiThread(() -> Toast.makeText(Login.this, "Create your account!", Toast.LENGTH_LONG).show());
                    Intent intent = new Intent(Login.this, signUp.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}