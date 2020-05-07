package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity {
    TextView courseNamead,courseIDad;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        this.courseNamead=findViewById(R.id.courseNameAD);
        this.courseIDad=findViewById(R.id.courseIDad);
        firebaseFirestore=FirebaseFirestore.getInstance();
        final Intent intent=getIntent();
        CollectionReference collectionReference=firebaseFirestore.collection("Courses");
        collectionReference.whereEqualTo("courseID",LoginActivity.allCourses.get(intent.getIntExtra("coursePosition",0))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(DocumentSnapshot snapshot:task.getResult().getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    courseNamead.setText((String)data.get("courseName"));
                    courseIDad.setText((String)data.get("courseID"));
                }
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(LoginActivity.currentUser!=null &&  LoginActivity.currentUser.isStudent()){
            MenuItem addCourse = menu.findItem(R.id.EditCourse);
            addCourse.setVisible(false);

        }
        return true;
    }
    //Connecting menu to this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Connecting xml file

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.course_detail_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

}