package com.example.noteapp.Activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.noteapp.Entity.Note;
import com.example.noteapp.R;
import com.example.noteapp.ViewModel.NoteViewModel;
import com.example.noteapp.databinding.ActivityUpdateNoteBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

public class UpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUpdateNoteBinding binding;
    String up_note_title, up_note_subtitle, up_note_detail, priority = "1";
    int up_note_id;
    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        up_note_id = getIntent().getIntExtra("id", 0);
        up_note_title = getIntent().getStringExtra("title");
        up_note_detail = getIntent().getStringExtra("note");
        priority = getIntent().getStringExtra("priority");


        binding.upNoteTitle.setText(up_note_title);
        binding.upNotesDetail.setText(up_note_detail);

        if (priority.equals("3")) {
            binding.upPriorityHigh.setImageResource(R.drawable.ic_baseline_done_24);
        } else if (priority.equals("2")) {
            binding.upPriorityMedium.setImageResource(R.drawable.ic_baseline_done_24);
        } else {
            binding.upPriorityLow.setImageResource(R.drawable.ic_baseline_done_24);
        }


        binding.upPriorityHigh.setOnClickListener(this);
        binding.upPriorityMedium.setOnClickListener(this);
        binding.upPriorityLow.setOnClickListener(this);


        binding.updateNoteBtn.setOnClickListener(v -> {
            String title = binding.upNoteTitle.getText().toString();
            String noteDetail = binding.upNotesDetail.getText().toString();
            UpdateNotes(title, noteDetail);
        });

        binding.deleteNoteBtn.setOnClickListener(v -> {

            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateNoteActivity.this);
            View view = LayoutInflater.from(UpdateNoteActivity.this).inflate(R.layout.delete_bottom_sheet,
                    (LinearLayout) findViewById(R.id.bottomSheet));
            sheetDialog.setContentView(view);
            sheetDialog.show();

            Button yesDelete, noDelete;
            yesDelete = view.findViewById(R.id.yesDeleteBtn);
            noDelete = view.findViewById(R.id.noDeleteBtn);

            yesDelete.setOnClickListener(v1 -> {
                noteViewModel.DeleteNote(up_note_id);
                finish();
                Toast.makeText(getApplicationContext(), "Not Successfully deleted", Toast.LENGTH_SHORT).show();
            });

            noDelete.setOnClickListener(v1 -> {
                sheetDialog.dismiss();
            });

        });
    }

    private void UpdateNotes(String title, String noteDetail) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

        Note note = new Note();
        note.id = up_note_id;
        note.note_title = title;
        note.notes = noteDetail;
        note.note_date = sequence.toString();
        note.note_priority = priority;
        noteViewModel.UpdateNote(note);

        Toast.makeText(getApplicationContext(), "Note Updated SuccessFully", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.upPriorityHigh) {
            priority = "3";
            binding.upPriorityHigh.setImageResource(R.drawable.ic_baseline_done_24);
            binding.upPriorityMedium.setImageResource(0);
            binding.upPriorityLow.setImageResource(0);
        } else if (id == R.id.upPriorityMedium) {
            priority = "2";
            binding.upPriorityMedium.setImageResource(R.drawable.ic_baseline_done_24);
            binding.upPriorityLow.setImageResource(0);
            binding.upPriorityHigh.setImageResource(0);
        } else if (id == R.id.upPriorityLow) {
            priority = "1";
            binding.upPriorityLow.setImageResource(R.drawable.ic_baseline_done_24);
            binding.upPriorityMedium.setImageResource(0);
            binding.upPriorityHigh.setImageResource(0);
        }
    }
}