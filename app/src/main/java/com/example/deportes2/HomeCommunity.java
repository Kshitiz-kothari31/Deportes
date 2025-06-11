package com.example.deportes2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class HomeCommunity extends Fragment {

    private TextView name;
    private RecyclerView recyclerView;

    public HomeCommunity() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.userName);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        updateGreeting();

        getParentFragmentManager().setFragmentResultListener("profileUpdated", this, (requestKey, bundle) -> {
            String updatedName = bundle.getString("user_name", "User");
            name.setText("Hi, " + updatedName + "!");

            SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
            prefs.edit().putString("user_name", updatedName).apply();
        });

        loadPostsOnce(); // Called only on app open / login
    }

    private void updateGreeting() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String Name = prefs.getString("user_name", "User");
        name.setText("Hi, " + Name + "!");
    }

    private void loadPostsOnce() {
        SupabaseManager.checkAndRefreshToken(requireContext(), new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String validAccessToken) {
                SupabaseManager.fetchAllPosts(validAccessToken, new SupabaseManager.PostsCallback() {
                    @Override
                    public void onPostsFetched(List<Post> posts) {
                        // Shuffle posts before displaying
                        Collections.shuffle(posts);
                        PostAdapter adapter = new PostAdapter(posts, requireContext());
                        requireActivity().runOnUiThread(() -> recyclerView.setAdapter(adapter));
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("SupabaseError", error);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
