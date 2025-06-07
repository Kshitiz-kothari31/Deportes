package com.example.deportes2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchInput;
    private RecyclerView recyclerView;
    private SportAdapter adapter;
    private List<String> sportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.search_input);
        recyclerView = findViewById(R.id.search_results_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SportItem> sportsList = new ArrayList<>();
        sportsList.add(new SportItem("Football", R.drawable.football));
        sportsList.add(new SportItem("Basketball", R.drawable.basketball));
        sportsList.add(new SportItem("Table Tennis", R.drawable.tabletenis));
        sportsList.add(new SportItem("Volleyball", R.drawable.volleyball));
        sportsList.add(new SportItem("Swimming", R.drawable.swimmer));
        sportsList.add(new SportItem("Badminton", R.drawable.batminton));


        adapter = new SportAdapter(sportsList, sport -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedSport", sport.name); // Ensure correct data retrieval
            setResult(Activity.RESULT_OK, resultIntent);
            finish(); // Close SearchActivity
        });

        recyclerView.setAdapter(adapter);

        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }
}
