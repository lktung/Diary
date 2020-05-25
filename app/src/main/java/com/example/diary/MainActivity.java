package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary.Adapter.MyAdapterRecycler;
import com.example.diary.Model.DiaryDetail;
import com.example.diary.ui.addDiary.AddDiaryActivity;
import com.example.diary.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static FirebaseAuth auth;
    private DrawerLayout dl;
    private NavigationView nav_view;
    private FloatingActionButton floatingActionButton;
    private TextView lb_email_account;
    private List<DiaryDetail> diaryDetailList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapterRecycler mAdapter;
    private DatabaseReference dbReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getdata();
        dbReference = FirebaseDatabase.getInstance().getReference();

        //open screen login
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        Log.e("test",auth.getCurrentUser().getUid());
        dl = (DrawerLayout)findViewById(R.id.drawerlayout);

        Intent intent = getIntent();
        final String email_account = intent.getStringExtra("email_current");

        //tool bar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //navigation drawer
        nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                nav_view.removeOnLayoutChangeListener(this);

                lb_email_account = (TextView)findViewById(R.id.lb_name_nav);
                lb_email_account.setText(email_account);
            }
        });
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                // close drawer when item is tapped
                dl.closeDrawers();

                if(id == R.id.sigin_nav){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.logout_nav){
                    nav_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                            nav_view.removeOnLayoutChangeListener(this);

                            lb_email_account = (TextView)findViewById(R.id.lb_name_nav);
                            lb_email_account.setText("");
                        }
                    });
                    Toast.makeText(MainActivity.this,"logout",Toast.LENGTH_SHORT).show();
                    auth.signOut();
                }
                return true;
            }
        });

        //floating button
        floatingActionButton = (FloatingActionButton)findViewById(R.id.btn_fta_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddDiaryActivity.class);
                startActivity(intent);
            }
        });

        //recyclerview
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view_main);
        mAdapter = new MyAdapterRecycler(MainActivity.this,diaryDetailList);
        recyclerView.setAdapter(mAdapter);

    }
    public void getdata(){
        diaryDetailList.clear();
        String id = auth.getCurrentUser().getUid();
        Log.e("test",id);
        dbReference.child(id).child("diary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DiaryDetail diaryDetail = dataSnapshot.getValue(DiaryDetail.class);
                diaryDetailList.add(diaryDetail);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*dbReference.child(id).child("diary").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DiaryDetail detail = dataSnapshot.getValue(DiaryDetail.class);
                diaryDetailList.add(detail);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    /*public void getData() {
        diaryDetailList.clear();
        FirebaseDatabase.getInstance().getReference().child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    DiaryDetail diaryDetail = d.getValue(DiaryDetail.class);
                    diaryDetailList.add(diaryDetail);
                }
                //sortJournal(diaries);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
