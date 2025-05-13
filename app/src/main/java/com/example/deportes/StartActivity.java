package com.example.deportes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class StartActivity extends AppCompatActivity {

    Button nextbutton;
    private int currentFragmentIndex = 0;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        nextbutton = findViewById(R.id.nextButton);

        fragments = new Fragment[] {
                new SlideOneFragment(),
                new SlideTwoFragment(),
                new SlideThreeFragment()
        };

        if( savedInstanceState == null ){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragments[0]).commit();
        }


        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( currentFragmentIndex < fragments.length ){

                    Fragment nextFragment = fragments[currentFragmentIndex];

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, nextFragment);
                    fragmentTransaction.commit();
                    currentFragmentIndex++;
                }else{
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}