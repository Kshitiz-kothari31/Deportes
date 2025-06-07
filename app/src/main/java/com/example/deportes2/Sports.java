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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

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

    private View loadingLayout;
    private ConstraintLayout sportsFragment;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize SearchView on 3 june
        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.search);
        searchView.setFocusable(false);
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getActivity().startActivityForResult(intent, 100);
        });
        ShapeableImageView football, basketball, tabletenis, volleyball, swimming, batminton;

        football = getView().findViewById(R.id.footballimage);
        basketball = getView().findViewById(R.id.basketballimage);
        tabletenis = getView().findViewById(R.id.tabletenisimage);
        volleyball = getView().findViewById(R.id.volleyballimage);
        swimming = getView().findViewById(R.id.swimmingimage);
        batminton = getView().findViewById(R.id.batmintonimage);

        loadingLayout = view.findViewById(R.id.loadingLayout);
        sportsFragment = view.findViewById(R.id.sports_Constraintlayout);

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    loadingLayout.setVisibility(View.VISIBLE); // Show loading

                    activity.fetchVideosFromBackend("Football", new OnVideosLoadedListener() {
                        @Override
                        public void onVideosLoaded(List<String> videoIds) {
                            loadingLayout.setVisibility(View.GONE); // Hide loading
                            activity.switchFragments(activity.footballVideosFragment); // Switch now
                        }
                    });
                }
            }
        });

        basketball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    loadingLayout.setVisibility(View.VISIBLE); // Show loading

                    activity.fetchVideosFromBackend("Basketball", new OnVideosLoadedListener() {
                        @Override
                        public void onVideosLoaded(List<String> videoIds) {
                            loadingLayout.setVisibility(View.GONE); // Hide loading
                            activity.switchFragments(activity.basketballVideosFragment); // Switch now
                        }
                    });
                }
            }
        });

         tabletenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    loadingLayout.setVisibility(View.VISIBLE); // Show loading

                    activity.fetchVideosFromBackend("Tabletenis", new OnVideosLoadedListener() {
                        @Override
                        public void onVideosLoaded(List<String> videoIds) {
                            loadingLayout.setVisibility(View.GONE); // Hide loading
                            activity.switchFragments(activity.tabletenisVideosFragment); // Switch now
                        }
                    });
                }
            }
        });

        volleyball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    loadingLayout.setVisibility(View.VISIBLE); // Show loading

                    activity.fetchVideosFromBackend("Volleyball", new OnVideosLoadedListener() {
                        @Override
                        public void onVideosLoaded(List<String> videoIds) {
                            loadingLayout.setVisibility(View.GONE); // Hide loading
                            activity.switchFragments(activity.volleyballVideosFragment); // Switch now
                        }
                    });
                }
            }
        });

        swimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    loadingLayout.setVisibility(View.VISIBLE); // Show loading

                    activity.fetchVideosFromBackend("Swimming", new OnVideosLoadedListener() {
                        @Override
                        public void onVideosLoaded(List<String> videoIds) {
                            loadingLayout.setVisibility(View.GONE); // Hide loading
                            activity.switchFragments(activity.swimmingVideosFragment); // Switch now
                        }
                    });
                }
            }
        });

        batminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    loadingLayout.setVisibility(View.VISIBLE); // Show loading

                    activity.fetchVideosFromBackend("Batminton", new OnVideosLoadedListener() {
                        @Override
                        public void onVideosLoaded(List<String> videoIds) {
                            loadingLayout.setVisibility(View.GONE); // Hide loading
                            activity.switchFragments(activity.batmintonVideosFragment); // Switch now
                        }
                    });
                }
            }
        });
    }

    private void loadVideos(String sportName) {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();

            loadingLayout.setVisibility(View.VISIBLE);
            sportsFragment.setVisibility(View.GONE);

            activity.fetchVideosFromBackend(sportName, new Sports.OnVideosLoadedListener() {
                @Override
                public void onVideosLoaded(List<String> videoUrls) {
                    loadingLayout.setVisibility(View.GONE);

                    switch (sportName) {
                        case "Football":
                            activity.switchFragments(activity.footballVideosFragment);
                            break;
                        case "Basketball":
                            activity.switchFragments(activity.basketballVideosFragment);
                            break;
                        case "Tabletenis":
                            activity.switchFragments(activity.tabletenisVideosFragment);
                            break;
                        case "Volleyball":
                            activity.switchFragments(activity.volleyballVideosFragment);
                            break;
                        case "Swimming":
                            activity.switchFragments(activity.swimmingVideosFragment);
                            break;
                        case "Batminton":
                            activity.switchFragments(activity.batmintonVideosFragment);
                            break;
                        // Add other cases as needed
                    }
                }
            });
        }
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

    public interface OnVideosLoadedListener {
        void onVideosLoaded(List<String> videoPublicIds);
    }
}