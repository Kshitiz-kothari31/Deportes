package com.example.deportes2;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.Request;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class Search extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private SearchUserAdapter adapter;
    private List<UserModel> userList = new ArrayList<>();
    private String accessToken;  // Make sure you have access token saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.users_search_activity);

        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchUserAdapter(Search.this, userList);
        recyclerView.setAdapter(adapter);

        adapter.setOnUserClickListener( user -> {
            Intent intent = new Intent(Search.this, SearchUserProfileLayoutActivity.class);
            intent.putExtra("FRIEND_USER_ID", user.getId());
            startActivity(intent);
        });

        accessToken = getSharedPreferences("AuthPrefs", MODE_PRIVATE).getString("access_token", "");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    SupabaseManager.checkAndRefreshToken(Search.this, new SupabaseManager.TokenCheckCallback() {
                        @Override
                        public void onTokenReady(String newAccessToken) {
                            accessToken = newAccessToken;
                            fetchUsersFromSupabase(query);
                        }
                        @Override
                        public void onError(String errorMessage) {
                            Log.e("SearchDebug", "Token error: " + errorMessage);
                        }
                    });
                }
            }
        });
    }

    private void fetchUsersFromSupabase(String query) {
        Log.d("SearchDebug", "Query: " + query.toString());
        HttpUrl url = HttpUrl.parse(SupabaseManager.SUPABASE_URL + "/rest/v1/profiles")
                .newBuilder()
                .addQueryParameter("name", "ilike.*" + query + "*")
                .addQueryParameter("select", "id,name,username,profile_img")
                .build();
        Log.d("SearchDebug", "URL: " + url.toString());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseManager.API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Log.d("SearchDebug", "Token: " + accessToken);
        SupabaseManager.getClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Search", "Failed: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Search", "Failed: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                Log.d("SearchDebug", "Response: " + responseBody);
                Type listType = new TypeToken<List<UserModel>>() {}.getType();
                List<UserModel> users = new Gson().fromJson(responseBody, listType);
                runOnUiThread(() -> adapter.updateList(users));            }
        });
    }
}
