package com.example.noteapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.noteapp.Entity.Note;
import com.example.noteapp.ViewModel.NoteViewModel;
import com.example.noteapp.databinding.ActivityInsertNoteBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityInsertNoteBinding binding;
    String note_title, note_detail, priority = "1", note_time;

    NoteViewModel noteViewModel;

    NotificationManagerCompat notificationCompat;
    Notification notification;

    //Time Picker
    int cyear, cmonth, cday;
    MaterialTimePicker picker;
    Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        binding.priorityHigh.setOnClickListener(this);
        binding.priorityMedium.setOnClickListener(this);
        binding.priorityLow.setOnClickListener(this);
        binding.setTimeBtn.setOnClickListener(this);
        binding.saveNoteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.saveNoteBtn) {
            note_title = binding.noteTitle.getText().toString();
            note_detail = binding.notesDetail.getText().toString();
            note_time = binding.timeDispTime.getText().toString();
            if (!note_title.isEmpty() || !note_detail.isEmpty()) {
                if (!note_time.isEmpty()) {
                    CreateNotes(note_title, note_detail);
                } else {
                    Toast.makeText(this, "Please Select Time", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.setTimeBtn) {
            showTimePicker();
        } else if (id == R.id.priorityHigh) {
            priority = "3";
            binding.priorityHigh.setImageResource(R.drawable.ic_baseline_done_24);
            binding.priorityMedium.setImageResource(0);
            binding.priorityLow.setImageResource(0);
        } else if (id == R.id.priorityMedium) {
            priority = "2";
            binding.priorityMedium.setImageResource(R.drawable.ic_baseline_done_24);
            binding.priorityLow.setImageResource(0);
            binding.priorityHigh.setImageResource(0);
        } else if (id == R.id.priorityLow) {
            priority = "1";
            binding.priorityLow.setImageResource(R.drawable.ic_baseline_done_24);
            binding.priorityMedium.setImageResource(0);
            binding.priorityHigh.setImageResource(0);
        }
    }

    private void CreateNotes(String note_title, String note_detail) {
        Note note = new Note();
        note.note_title = note_title;
        note.notes = note_detail;
        note.note_priority = priority;
        note.note_time = binding.timeDispTime.getText().toString();
        noteViewModel.InsertNote(note);
        callNotification();
        scheduleNotification(getNotification(note_title), calendar.getTimeInMillis());
        Toast.makeText(getApplicationContext(), "Note Added SuccessFully", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void callNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Mych", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Mych")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Note App Notification")
                .setContentText("Note Created Successfully")
                .setTimeoutAfter(7000);
        notification = builder.build();
        notificationCompat = NotificationManagerCompat.from(this);
        notificationCompat.notify(1, notification);
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
            calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
            calendar.set(Calendar.MINUTE, picker.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        });
    }

    private Notification getNotification(String note_title) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(InsertNoteActivity.this, "Android")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Your Note Reminder : ")
                .setContentText(note_title)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("NOTIFICATION_ID", 1);
        notificationIntent.putExtra("NOTIFICATION", notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
    }
}