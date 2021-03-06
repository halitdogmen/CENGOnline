package com.example.plhomework.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText courseName;
    EditText courseID;
    ProgressBar progressBar;
    String courseIDstring;
    String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        courseName=findViewById(R.id.courseName);
        courseID=findViewById(R.id.courseID);
        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        courseIDstring=intent.getStringExtra("courseID");
        firebaseFirestore.collection("Courses").whereEqualTo("courseID",courseIDstring).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                     documentId= snapshot.getId();
                    System.out.println(documentId);
                    Map<String, Object> data = snapshot.getData();
                    courseName.setText((String)data.get("courseName"));
                    courseID.setText((String)data.get("courseID"));

                }
            }
        });
    }
    public void editCourseClicked(View view) {
        final DocumentReference documentReference = firebaseFirestore.collection("Courses").document(documentId);
        WriteBatch batch = firebaseFirestore.batch();
        if (courseName.getText().toString().matches("") || courseID.getText().toString().matches("") ) {
            Toast.makeText(EditCourseActivity.this,"Fill the spaces!",Toast.LENGTH_LONG).show();
        } else {
            batch.update(documentReference, "courseName", courseName.getText().toString());
            batch.update(documentReference, "courseID", courseID.getText().toString());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EditCourseActivity.this, "Course has been edited!", Toast.LENGTH_LONG).show();
                    LoginActivity.allCourses.remove(LoginActivity.allCourses.indexOf(courseIDstring));
                    LoginActivity.allCourses.add(courseID.getText().toString());
                    System.out.println(LoginActivity.allCourses.indexOf(courseID.getText().toString()));
                    firebaseFirestore.collection("Course_User").whereEqualTo("courseID", courseIDstring).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                documentId = snapshot.getId();
                                DocumentReference documentReference1 = firebaseFirestore.collection("Course_User").document(documentId);
                                documentReference1.update("courseID", courseID.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            }
                            firebaseFirestore.collection("Course_Announcement").whereEqualTo("courseID",courseIDstring).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                                        documentId=snapshot.getId();
                                        DocumentReference documentReference1=firebaseFirestore.collection("Course_Announcement").document(documentId);
                                        documentReference1.update("courseID",courseID.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                    }
                                    firebaseFirestore.collection("Course_Assignment").whereEqualTo("courseID",courseIDstring).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                                                documentId=snapshot.getId();
                                                DocumentReference documentReference1=firebaseFirestore.collection("Course_Assignment").document(documentId);
                                                documentReference1.update("courseID",courseID.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                            }

                                            Intent intent=new Intent(EditCourseActivity.this, CourseDetailActivity.class);
                                            intent.putExtra("courseID",courseID.getText().toString());
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                    Intent intent=new Intent(EditCourseActivity.this, CourseFeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}
