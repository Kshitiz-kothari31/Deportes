package com.example.deportes2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import org.w3c.dom.Text;

import java.util.List;

public class HomeCommunity extends Fragment {

    private  TextView name;
    private RecyclerView RecyclerView;

    SupabaseManager supabaseManager = new SupabaseManager();
    String accessToken;

    public HomeCommunity(){

    }
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
        RecyclerView = getView().findViewById(R.id.recyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Load from SharedPreferences
        updateGreeting();

        // Listen for profile update result
        getParentFragmentManager().setFragmentResultListener("profileUpdated", this, (requestKey, bundle) -> {
            String updatedName = bundle.getString("user_name", "User");
            name.setText("Hi, " + updatedName + "!");

            SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
            prefs.edit().putString("user_name", updatedName).apply();
        });

        SupabaseManager.checkAndRefreshToken(requireContext(), new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String validAccessToken) {
                SupabaseManager.fetchAllPosts(validAccessToken, new SupabaseManager.PostsCallback() {
                    @Override
                    public void onPostsFetched(List<Post> posts) {
                        // Use your posts here
                        PostAdapter adapter = new PostAdapter(posts, requireContext());

                        requireActivity().runOnUiThread(() -> {
                            RecyclerView.setAdapter(adapter);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("Error fetching posts: ", error);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void updateGreeting(){
        SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String Name = prefs.getString("user_name", "User");

        name.setText("Hi, " + Name + "!");
    }

    private Handler refreshHandler = new Handler();
    private Runnable refreshRunnable;

    @Override
    public void onResume(){
        super.onResume();
        updateGreeting();
        startAutoRefresh();
    }

    public void onPause(){
        super.onPause();
        stopAutoRefresh();
    }

    private void startAutoRefresh(){
        refreshRunnable = () -> {
            loadPosts();
            refreshHandler.postDelayed(refreshRunnable, 10000);
        };
        refreshHandler.post(refreshRunnable);
    }

    private void stopAutoRefresh(){
        refreshHandler.removeCallbacks(refreshRunnable);
    }

    private void loadPosts() {
        SupabaseManager.checkAndRefreshToken(requireContext(), new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String validAccessToken) {
                SupabaseManager.fetchAllPosts(validAccessToken, new SupabaseManager.PostsCallback() {
                    @Override
                    public void onPostsFetched(List<Post> posts) {
                        PostAdapter adapter = new PostAdapter(posts, requireContext());
                        requireActivity().runOnUiThread(() -> {
                            RecyclerView.setAdapter(adapter);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("Error fetching posts: ", error);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

}