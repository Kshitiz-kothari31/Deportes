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


public class tabletennies_videos extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabletennies_videos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton t_footwork, t_coordination, t_serve,  t_chop, t_grip, t_stroke;

        t_footwork = view.findViewById(R.id.tabletenis_footwork);
        t_footwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("passing.mp4", videos);
            }
        });

        t_coordination = getView().findViewById(R.id.tabletenis_coordination);
        t_coordination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("exercisesAndCoordination.mp4", videos);
            }
        });

        t_serve= getView().findViewById(R.id.tabletenis_serve);
        t_serve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Serve.mp4", videos);
            }
        });

        t_chop = getView().findViewById(R.id.tabletenis_chop);
        t_chop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Chop&Push.mp4", videos);
            }
        });

        t_grip = getView().findViewById(R.id.tabletenis_grip);
        t_grip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("WrapGrip.mp4", videos);
            }
        });

        t_stroke = getView().findViewById(R.id.tabletenis_stroke);
        t_stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Strokes.mp4", videos);
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