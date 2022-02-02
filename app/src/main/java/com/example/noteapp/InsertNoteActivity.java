package com.example.noteapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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
    String note_title, note_detail, priority = "1";

    NoteViewModel noteViewModel;

    NotificationManagerCompat notificationCompat;
    Notification notification;

    //Time Picker
    int cyear, cmonth, cday;
    MaterialTimePicker picker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        binding.priorityHigh.setOnClickListener(this);
        binding.priorityMedium.setOnClickListener(this);
        binding.priorityLow.setOnClickListener(this);
        binding.setDateBtn.setOnClickListener(this);
        binding.setTimeBtn.setOnClickListener(this);
        binding.saveNoteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.saveNoteBtn) {
            note_title = binding.noteTitle.getText().toString();
            note_detail = binding.notesDetail.getText().toString();
            CreateNotes(note_title, note_detail);
        } else if (id == R.id.setDateBtn) {
            showDatePicker();
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
        note.note_date = binding.dateDispTime.getText().toString();
        note.note_priority = priority;
        note.note_time = binding.timeDispTime.getText().toString();
        noteViewModel.InsertNote(note);
        callNotification();
//        setAlarm();
        scheduleNotification(getNotification(), calendar.getTimeInMillis());
        Toast.makeText(getApplicationContext(), "Note Added SuccessFully", Toast.LENGTH_SHORT).show();
//        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(InsertNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.dateDispTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, cyear, cmonth, cday);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
        datePickerDialog.show();
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
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
            calendar.set(Calendar.MINUTE, picker.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        });
    }

    private void setAlarm() {
        long sec = calendar.getTimeInMillis();
        Log.d("Se", "Set Alarm : " + sec);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, sec, pendingIntent);
        Toast.makeText(this, "SuccessesFully Set", Toast.LENGTH_SHORT).show();
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(InsertNoteActivity.this, "Android")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Task Pending")
                .setContentText("Hey alarm running")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("NOTIFICATION_ID", 1);
        notificationIntent.putExtra("NOTIFICATION", notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pendingIntent);
    }
}