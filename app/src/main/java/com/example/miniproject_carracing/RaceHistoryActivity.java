package com.example.miniproject_carracing;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import model.Bet;
import model.GameSession;

public class RaceHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BetAdapter adapter;
    private List<Bet> betList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_history);

        recyclerView = findViewById(R.id.recyclerViewRaceHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        betList = GameSession.betHistory != null ? GameSession.betHistory : new ArrayList<>();

        adapter = new BetAdapter(betList);
        recyclerView.setAdapter(adapter);
    }
}
