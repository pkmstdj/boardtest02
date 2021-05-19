package dcp.n.boardtest02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private EditText writeTitle;
    private EditText writeContents;
    private EditText writeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        writeTitle = findViewById(R.id.write_title);
        writeContents = findViewById(R.id.write_contents);
        writeName = findViewById(R.id.write_name);

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = firestore.collection("board").document().getId();

                Map<String, Object> post = new HashMap<>();
                post.put("id", id);
                post.put("title", writeTitle.getText().toString());
                post.put("contents", writeContents.getText().toString());
                post.put("name", writeName.getText().toString());

                v.setEnabled(false);

                firestore.collection("board").document(id).set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(WriteActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        v.setEnabled(true);
                        Toast.makeText(WriteActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                        Log.w("WriteActivity_firebase", "Error adding document", e);
                    }
                });
            }
        });
    }
}