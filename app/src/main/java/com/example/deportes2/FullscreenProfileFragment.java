package com.example.deportes2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.google.android.material.imageview.ShapeableImageView;

import com.example.deportes2.SupabaseManager;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class FullscreenProfileFragment extends Fragment {

    private ImageView bgImg;
    private ShapeableImageView profileImg;
    private AppCompatButton editBtn, changebgBtn, changeProfileBtn;
    private TextView nameTextView, userNameTextView, bioTextView;
    private EditText nameEditText, userNameEdit,  bioEditText;

    private boolean isEditMode = false;
    private boolean editingProfileImage = false;

    private ActivityResultLauncher<String> imagePickerLauncher;

    private String accessToken;
    private String userId;

    private Uri selectedProfileUri = null;
    private Uri selectedBackgroundUri = null;

    Toolbar toptoolbar;

    public FullscreenProfileFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fullscreen_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bgImg = view.findViewById(R.id.bg_img);
        changebgBtn = view.findViewById(R.id.changebgImageBtn);
        profileImg = view.findViewById(R.id.profile_img);
        changeProfileBtn = view.findViewById(R.id.changeprofileImageBtn);

        editBtn = view.findViewById(R.id.edit_btn);

        nameTextView = view.findViewById(R.id.name);
        nameEditText = view.findViewById(R.id.name_input);

        userNameTextView = view.findViewById(R.id.user_name);
        userNameEdit = view.findViewById(R.id.user_name_input);

        bioTextView = view.findViewById(R.id.bio);
        bioEditText = view.findViewById(R.id.bio_input);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            if (editingProfileImage) {
                                selectedProfileUri = uri;
                                profileImg.setImageURI(uri);
                            } else {
                                selectedBackgroundUri = uri;
                                bgImg.setImageURI(uri);
                            }
                        }

                    }
                });

        SharedPreferences prefs = getActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", null);
        accessToken = prefs.getString("access_token", null);

        if (userId != null && accessToken != null) {
            SupabaseManager.getProfile(userId, accessToken, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        requireActivity().runOnUiThread(() -> {
                            try {
                                JSONArray jsonArray = new JSONArray(responseBody);
                                if (jsonArray.length() > 0) {
                                    JSONObject profile = jsonArray.getJSONObject(0);

                                    String name = profile.optString("name", "");
                                    String username = profile.optString("username", "");
                                    String bio = profile.optString("bio", "");
                                    String profileImageUrl = profile.optString("profile_img", "error");
                                    String backgroundImageUrl = profile.optString("bg_img", "error");

                                    nameTextView.setText(name);
                                    userNameTextView.setText(username);
                                    bioTextView.setText(bio);

                                    Log.d("ProfileImgURL", profileImageUrl);
                                    Log.d("BgImgURL", backgroundImageUrl);

                                    // Load profile and background images using Glide or Picasso
                                    Glide.with(requireContext()).load(profileImageUrl).into(profileImg);
                                    Glide.with(requireContext()).load(backgroundImageUrl).into(bgImg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Log.e("Supabase", "Failed to get profile: " + response.code());
                    }
                }
            });
        } else {
            Log.e("Supabase", "Missing access token or user ID in SharedPreferences");
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditMode){
                    isEditMode = true;
                    editBtn.setText("Done");

                    nameEditText.setText(nameTextView.getText().toString());
                    userNameEdit.setText(userNameTextView.getText().toString());
                    bioEditText.setText(bioTextView.getText().toString());

                    nameEditText.setVisibility(View.VISIBLE);
                    userNameEdit.setVisibility(View.VISIBLE);
                    bioEditText.setVisibility(View.VISIBLE);

                    changebgBtn.setVisibility(View.VISIBLE);
                    changeProfileBtn.setVisibility(View.VISIBLE);

                    nameTextView.setVisibility(View.GONE);
                    userNameTextView.setVisibility(View.GONE);
                    bioTextView.setVisibility(View.GONE);

                    changeProfileBtn.setOnClickListener(img -> {
                        editingProfileImage = true;
                        imagePickerLauncher.launch("image/*");
                    });

                    changebgBtn.setOnClickListener(img -> {
                        editingProfileImage = false;
                        imagePickerLauncher.launch("image/*");
                    });

                }else{
                    isEditMode = false;
                    editBtn.setText("Edit");

                    nameTextView.setText(nameEditText.getText().toString());
                    userNameTextView.setText(userNameEdit.getText().toString());
                    bioTextView.setText(bioEditText.getText().toString());

                    String name = nameEditText.getText().toString();
                    String username = userNameEdit.getText().toString();
                    String bio = bioEditText.getText().toString();

                    nameEditText.setVisibility(View.GONE);
                    userNameEdit.setVisibility(View.GONE);
                    bioEditText.setVisibility(View.GONE);

                    changebgBtn.setVisibility(View.GONE);
                    changeProfileBtn.setVisibility(View.GONE);

                    nameTextView.setVisibility(View.VISIBLE);
                    userNameTextView.setVisibility(View.VISIBLE);
                    bioTextView.setVisibility(View.VISIBLE);

                    if (selectedProfileUri != null && selectedBackgroundUri != null) {
                        SupabaseManager.uploadImageToBucket(requireContext(), "profile_images/" + userId + ".jpg", selectedProfileUri, accessToken, new SupabaseManager.ImageUploadCallback() {
                            @Override
                            public void onSuccess(String profileUrl) {
                                Log.d("Upload", "Profile image uploaded: " + profileUrl);
                                SupabaseManager.uploadImageToBucket(requireContext(), "background_images/" + userId + ".jpg", selectedBackgroundUri, accessToken, new SupabaseManager.ImageUploadCallback() {
                                    @Override
                                    public void onSuccess(String backgroundUrl) {
                                        Log.d("Upload", "Background image uploaded: " + backgroundUrl);
                                        saveProfileToSupabase(userId, name, username, bio, profileUrl, backgroundUrl, accessToken);
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        Log.e("Upload", "Background upload failed: " + errorMessage);

                                        saveProfileToSupabase(userId, name, username, bio, null, null, accessToken);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Log.e("Upload", "Profile upload failed: " + errorMessage);

                                saveProfileToSupabase(userId, name, username, bio, null, null, accessToken);
                            }
                        });
                    }else {
                        saveProfileToSupabase(userId, name, username, bio, null, null, accessToken);
                    }
                }
            }
        });
    }

    private void saveProfileToSupabase(String uId, String name, String username, String bio, String profileUrl, String bgUrl, String accessT) {
        SupabaseManager.upsertProfile(uId, name, username, bio, profileUrl, bgUrl, accessT, new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure on main thread
                requireActivity().runOnUiThread(() -> {
                    Log.d("Profile update failed", e.getMessage() + " - " + e.getLocalizedMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_LONG).show();
                        Bundle result = new Bundle();
                        result.putString("user_name", name);

                        getParentFragmentManager().setFragmentResult("profileUpdated", result);
                    });
                }else{
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    requireActivity().runOnUiThread(() -> {
                        Log.d("Profile update failed", "Error: " + errorBody);
                    });
                }
            }
        });
    }

    private void addPostToLayout(String name, String time, String message, String imageUrl, String profile_url){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View postView = inflater.inflate(R.layout.post, null);

        TextView USER_NAME = postView.findViewById(R.id.name);
        TextView TIME = postView.findViewById(R.id.time);
        TextView POST_MESSAGE = postView.findViewById(R.id.postMessage);
        ShapeableImageView POST_IMG = postView.findViewById(R.id.postimg);
        ShapeableImageView USER_IMAGE = postView.findViewById(R.id.userImg);

        USER_NAME.setText(name);
        TIME.setText(time);
        POST_MESSAGE.setText(message);
        Glide.with(this).load(profile_url).into(USER_IMAGE);
        Glide.with(this).load(imageUrl).into(POST_IMG);

        LinearLayout postsContainer = requireView().findViewById(R.id.postsContainer);
        postsContainer.addView(postView);

    }

}