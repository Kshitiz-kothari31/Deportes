package com.example.deportes2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.imageview.ShapeableImageView;

public class Sports extends Fragment {
    public Sports(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isNetworkAvailable()) {
            showInternetDialog();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShapeableImageView football, basketball, tabletenis, volleyball, swimming, batminton;

        football = getView().findViewById(R.id.footballimage);
        basketball = getView().findViewById(R.id.basketballimage);
        tabletenis = getView().findViewById(R.id.tabletenisimage);
        volleyball = getView().findViewById(R.id.volleyballimage);
        swimming = getView().findViewById(R.id.swimmingimage);
        batminton = getView().findViewById(R.id.batmintonimage);

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.fetchVideosFromBackend("Football");  // ✅ Call via activity
                    activity.switchFragments(activity.footballVideosFragment);
                }
            }
        });

        basketball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.fetchVideosFromBackend("Basketball");
                    activity.switchFragments(activity.basketballVideosFragment);
                }
            }
        });

//        tabletenis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).switchFragments(
//                            ((MainActivity) getActivity()).tabletenisVideosFragment);
//                    fetchVideosFromBackend("TableTenis");
//                }
//            }
//        });
//
//        volleyball.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).switchFragments(
//                            ((MainActivity) getActivity()).volleyballVideosFragment);
//                    fetchVideosFromBackend("Volleyball");
//                }
//            }
//        });
//
//        swimming.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).switchFragments(
//                            ((MainActivity) getActivity()).swimmingVideosFragment);
//                    fetchVideosFromBackend("Swimming");
//                }
//            }
//        });
//
//        batminton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).switchFragments(
//                            ((MainActivity) getActivity()).batmintonVideosFragment);
//                    fetchVideosFromBackend("Batminton");
//                }
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sports, container, false);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please enable your internet connection to use this app.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}