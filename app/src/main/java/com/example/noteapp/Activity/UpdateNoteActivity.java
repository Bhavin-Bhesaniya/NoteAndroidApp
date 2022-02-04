package com.example.noteapp.Activity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.noteapp.Entity.Note;
import com.example.noteapp.R;
import com.example.noteapp.ViewModel.NoteViewModel;
import com.example.noteapp.databinding.ActivityUpdateNoteBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class UpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUpdateNoteBinding binding;
    String up_note_title, up_note_detail, priority = "1", up_note_date, up_note_time;
    int up_note_id;
    NoteViewModel noteViewModel;

    //Time Picker
    int cyear, cmonth, cday;
    MaterialTimePicker picker;
    Calendar calendar;

    //Notification
    NotificationManagerCompat notificationCompat;
    Notification notification;

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
        up_note_date = getIntent().getStringExtra("date");
        up_note_time = getIntent().getStringExtra("time");


        binding.upNoteTitle.setText(up_note_title);
        binding.upNotesDetail.setText(up_note_detail);
        if (priority.equals("3")) {
            binding.upPriorityHigh.setImageResource(R.drawable.ic_baseline_done_24);
        } else if (priority.equals("2")) {
            binding.upPriorityMedium.setImageResource(R.drawable.ic_baseline_done_24);
        } else {
            binding.upPriorityLow.setImageResource(R.drawable.ic_baseline_done_24);
        }
        binding.timeDispTime.setText(up_note_time);
        binding.dateDispTime.setText(up_note_date);


        binding.upPriorityHigh.setOnClickListener(this);
        binding.upPriorityMedium.setOnClickListener(this);
        binding.upPriorityLow.setOnClickListener(this);

        binding.setDateBtn.setOnClickListener(this);
        binding.setTimeBtn.setOnClickListener(this);


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
        Note note = new Note();
        note.id = up_note_id;
        note.note_title = title;
        note.notes = noteDetail;
        note.note_priority = priority;
        note.note_date = binding.dateDispTime.getText().toString();
        note.note_time = binding.timeDispTime.getText().toString();
        noteViewModel.UpdateNote(note);
        callNotification();
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
        } else if (id == R.id.setDateBtn) {
            showDatePicker();
        } else if (id == R.id.setTimeBtn) {
            showTimePicker();
        }
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.dateDispTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, cyear, cmonth, cday);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .build();

        picker.show(getSupportFragmentManager(), "Android");
        picker.addOnPositiveButtonClickListener(v -> {
            if (picker.getHour() > 12) {
                binding.timeDispTime.setText(String.format("%02d", (picker.getHour() - 12)) + " : " + String.format("%02d", picker.getMinute()) + " PM");
            } else {
                binding.timeDispTime.setText(picker.getHour() + " : " + picker.getMinute() + " AM");
            }
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
            calendar.set(Calendar.MINUTE, picker.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        });
    }
    public void callNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNoteUp", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNoteUp")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Note App Notification")
                .setContentText("Note Updated Successfully")
                .setTimeoutAfter(7000);
        notification = builder.build();
        notificationCompat = NotificationManagerCompat.from(this);
        notificationCompat.notify(1, notification);
    }
}