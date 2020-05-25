package com.example.diary.ui.addDiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diary.MainActivity;
import com.example.diary.Model.DiaryDetail;
import com.example.diary.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;


public class AddDiaryActivity extends AppCompatActivity {

    private int defaultcolor;
    private  Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private ImageButton btn_done;
    private EditText edt_title,edt_content;
    private String date_picker = new String();
    private String time_picker = new String();
    private DatabaseReference mDatabase;
    private long maxid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //find id
        edt_title = (EditText)findViewById(R.id.edt_title_addDiary);
        edt_content = (EditText)findViewById(R.id.edt_content_addDiary);
        toolbar = (Toolbar)findViewById(R.id.toolbar_addDiary);
        bottomNav = (BottomNavigationView)findViewById(R.id.nav_bottom);
        btn_done = (ImageButton)findViewById(R.id.btn_add_diary);

        //toolbar
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setTitle("");

        // bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.date_navBottom:
                        Calendar calendar1 = Calendar.getInstance();
                        int year = calendar1.get(Calendar.YEAR);
                        int month = calendar1.get(Calendar.MONTH);
                        int day = calendar1.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiaryActivity.this,R.style.colorDialog,new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                               // Log.e("test","onDateset" + dayOfMonth + "/" + monthOfYear + "/" + year );
                                date_picker = dayOfMonth +"/"+monthOfYear+"/"+year;
                                Log.e("test",date_picker.toString());
                            }
                        },year,month,day);

                        datePickerDialog.show();
                        return true;
                    case R.id.time_navBottom:
                        Calendar calendar = Calendar.getInstance();
                        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddDiaryActivity.this,R.style.colorDialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                               // Log.e("test", "onTimeSet: " + hourOfDay + ", " + minute);
                                time_picker = String.valueOf(hourOfDay)+" : "+String.valueOf(minute);
                                Log.e("test",time_picker.toString());
                            }
                        }, hour, minute, true);

                        timePickerDialog.show();
                        return true;
                    case R.id.color_navBottom:
                        defaultcolor = ContextCompat.getColor(AddDiaryActivity.this, R.color.colorPrimary);
                        OpenColorPickerDialog(false);
                        return true;
                    case R.id.delete_navBottom:
                        return true;
                }
                return false;
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddDiaryActivity.this,"added success",Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent();
                Bundle bundle = new Bundle();*/
                Date date_current = Calendar.getInstance().getTime();

                DiaryDetail diary_add = new DiaryDetail();
                diary_add.setId_(String.valueOf(maxid + 1));
                diary_add.setTitle_(edt_title.getText().toString());
                diary_add.setContent_(edt_content.getText().toString());
                diary_add.setColor_(defaultcolor);
                diary_add.setDateOfCreation_(date_current);
                diary_add.setDateOccurrence_(date_picker);
                diary_add.setTimeOccurrence(time_picker);

                mDatabase.child(MainActivity.auth.getCurrentUser().getUid()).child("diary").push().setValue(diary_add);
                finish();
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(AddDiaryActivity.this, defaultcolor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                defaultcolor = color;
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

                Toast.makeText(AddDiaryActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }
}
