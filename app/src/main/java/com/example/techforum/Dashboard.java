package com.example.techforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.techforum.CreatePost.CreatepostDialogue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {
BottomNavigationView Navigation;
FloatingActionButton create_post;
ImageView menuIcon,signOut;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout contentView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Navigation=findViewById(R.id.bottomNavigationBar);
        create_post=findViewById(R.id.create_post);
        menuIcon=findViewById(R.id.hamburger_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.content);
        signOut=findViewById(R.id.dashboard_signOut);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
//        progressBar=findViewById(R.id.home_progressbar);
//        progressBar.setVisibility(View.VISIBLE);

        replace(new home_fragment());
//        progressBar.setVisibility(View.GONE);
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialgue();
            }
        });
        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch(id){
                    case R.id.home:
                    {
                        replace(new home_fragment());
                        create_post.setVisibility(View.VISIBLE);
                        break;}
                    case R.id.chats:{
                        replace(new chat());
                        create_post.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.news:{
                        replace(new news());
                        create_post.setVisibility(View.GONE);
//                        replace (new ContactsContract.Profile());
                    }

                }
                return true;
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(Dashboard.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void replace(Fragment fragmentPost) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction().replace(R.id.home_frameLayout,fragmentPost);
        ft.commit();
    }

    private void openDialgue() {
        CreatepostDialogue ob=new CreatepostDialogue();
        ob.show(getSupportFragmentManager(),"PostDialogue");

    }

}