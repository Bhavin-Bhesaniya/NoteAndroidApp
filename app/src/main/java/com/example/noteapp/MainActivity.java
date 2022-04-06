package com.example.noteapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.noteapp.RoomDB.NoteAdatper;
import com.example.noteapp.RoomDB.Note;
import com.example.noteapp.RoomDB.NoteViewModel;
import com.example.noteapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    TextView noFilter, highToLowFilter, lowToHighFilter;
    ImageView showFilter;
    RecyclerView noteRecyclerView;
    NoteAdatper noteAdatper;
    NoteViewModel noteViewModel;
    boolean filterVisible = true, fabOnOff = true;
    List<Note> filterNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showFilter = findViewById(R.id.showFilter);
        noFilter = findViewById(R.id.NoFilterTv);
        highToLowFilter = findViewById(R.id.HighToLowTv);
        lowToHighFilter = findViewById(R.id.LowToHighTv);


        binding.addBtn.setOnClickListener(this);
        showFilter.setOnClickListener(this);
        noFilter.setOnClickListener(this);
        highToLowFilter.setOnClickListener(this);
        lowToHighFilter.setOnClickListener(this);
        binding.addNewNote.setOnClickListener(this);


        binding.addNewNote.setVisibility(View.GONE);
        binding.showFilter.setVisibility(View.GONE);
        noFilter.setVisibility(View.GONE);
        highToLowFilter.setVisibility(View.GONE);
        lowToHighFilter.setVisibility(View.GONE);

        createNotificationChannel();

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteRecyclerView = findViewById(R.id.noteRecyclerView);

        loadData(0);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addNewNote) {
            startActivity(new Intent(MainActivity.this, InsertNoteActivity.class));
        } else if (id == R.id.addBtn) {
            if (fabOnOff) {
                binding.addNewNote.setVisibility(View.VISIBLE);
                binding.showFilter.setVisibility(View.VISIBLE);
                fabOnOff = false;
            } else {
                binding.addNewNote.setVisibility(View.GONE);
                binding.showFilter.setVisibility(View.GONE);
                fabOnOff = true;
            }
        } else if (id == R.id.showFilter) {
            if (filterVisible) {
                noFilter.setVisibility(View.VISIBLE);
                highToLowFilter.setVisibility(View.VISIBLE);
                lowToHighFilter.setVisibility(View.VISIBLE);
                filterVisible = false;
            } else {
                noFilter.setVisibility(View.GONE);
                highToLowFilter.setVisibility(View.GONE);
                lowToHighFilter.setVisibility(View.GONE);
                filterVisible = true;
            }
        } else if (id == R.id.NoFilterTv) {
            loadData(0);
            noFilter.setBackgroundResource(R.drawable.yellow_border);
            highToLowFilter.setBackgroundResource(R.drawable.filter_list_btn);
            lowToHighFilter.setBackgroundResource(R.drawable.filter_list_btn);
        } else if (id == R.id.HighToLowTv) {
            loadData(1);
            highToLowFilter.setBackgroundResource(R.drawable.yellow_border);
            noFilter.setBackgroundResource(R.drawable.filter_list_btn);
            lowToHighFilter.setBackgroundResource(R.drawable.filter_list_btn);
        } else {
            loadData(2);
            lowToHighFilter.setBackgroundResource(R.drawable.yellow_border);
            highToLowFilter.setBackgroundResource(R.drawable.filter_list_btn);
            noFilter.setBackgroundResource(R.drawable.filter_list_btn);
        }

    }

    private void loadData(int i) {
        if (i == 0) {
            noteViewModel.getAllNote.observe(this, notes -> {
                setAdatper(notes);
                filterNoteList = notes;
            });
        } else if (i == 1) {
            noteViewModel.getHighToLow.observe(this, notes -> {
                setAdatper(notes);
                filterNoteList = notes;
            });
        } else if (i == 2) {
            noteViewModel.getLowToHigh.observe(this, notes -> {
                setAdatper(notes);
                filterNoteList = notes;
            });
        }
    }

    public void setAdatper(List<Note> notes) {
        noteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        noteAdatper = new NoteAdatper(MainActivity.this, notes);
        noteRecyclerView.setAdapter(noteAdatper);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_note, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Note...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NotesFilter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void NotesFilter(String newText) {
        ArrayList<Note> filterName = new ArrayList<>();
        for (Note note : this.filterNoteList) {
            if (note.note_title.contains(newText)) {
                filterName.add(note);
            }
        }
        this.noteAdatper.searchNote(filterName);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "bhavin channel";
            String description = "Checking alarm";
            int important = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Android", name, important);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}