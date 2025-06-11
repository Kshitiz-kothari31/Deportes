package com.example.deportes2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchUserAdapter adapter;
    private List<UserModel> userList = new ArrayList<>();
    private String accessToken;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_search_activity);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchUserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        adapter.setOnUserClickListener(user -> {
            Intent intent = new Intent(Search.this, SearchUserProfileLayoutActivity.class);
            intent.putExtra("FRIEND_USER_ID", user.getId());
            startActivity(intent);
        });

        accessToken = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
                .getString("access_token", "");

        // Set SearchView query listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.trim().isEmpty()) {
                    SupabaseManager.checkAndRefreshToken(Search.this, new SupabaseManager.TokenCheckCallback() {
                        @Override
                        public void onTokenReady(String newAccessToken) {
                            accessToken = newAccessToken;
                            fetchUsersFromSupabase(newText.trim());
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("SearchDebug", "Token error: " + errorMessage);
                        }
                    });
                }
                return true;
            }
        });
    }

    private void fetchUsersFromSupabase(String query) {
        Log.d("SearchDebug", "Query: " + query);
        HttpUrl url = HttpUrl.parse(SupabaseManager.SUPABASE_URL + "/rest/v1/profiles")
                .newBuilder()
                .addQueryParameter("name", "ilike.*" + query + "*")
                .addQueryParameter("select", "id,name,username,profile_img")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseManager.API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

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
                Type listType = new TypeToken<List<UserModel>>() {}.getType();
                List<UserModel> users = new Gson().fromJson(responseBody, listType);

                runOnUiThread(() -> adapter.updateList(users));
            }
        });
    }
}
