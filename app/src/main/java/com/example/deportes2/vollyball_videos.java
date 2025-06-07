package com.example.deportes2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class vollyball_videos extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vollyball_videos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton v_rotation, v_decision_making, v_serving,  v_scoring_system, v_touch_rules, v_substitutions;

        v_rotation = view.findViewById(R.id.vollyball_rotation);
        v_rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Rotation.mp4", videos);
            }
        });

        v_decision_making = getView().findViewById(R.id.vollyball_decision_making);
        v_decision_making.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("DecisionMaking.mp4", videos);
            }
        });

        v_serving = getView().findViewById(R.id.vollyball_serving);
        v_serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Serving.mp4", videos);
            }
        });

         v_scoring_system = getView().findViewById(R.id.vollyball_system_scoring);
        v_scoring_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("", videos);
            }
        });

        v_touch_rules = getView().findViewById(R.id.vollyball_touch_rules);
        v_touch_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("", videos);
            }
        });

        v_substitutions = getView().findViewById(R.id.vollyball_substitutions);
        v_substitutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("", videos);
            }
        });
    }

    private void playVideoWithName(String videoName, List<String> videoUrls){
        for(String url : videoUrls){
            if(url.contains(videoName)){
                Intent intent = new Intent(requireContext(), VideoPlayer.class);
                intent.putExtra("actionName", url);
                startActivity(intent);
                return;
            }
        }

        Toast.makeText(requireContext(), "Video not found: " + videoName, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().show();
            }
        }
    }
}