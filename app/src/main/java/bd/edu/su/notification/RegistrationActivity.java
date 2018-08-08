package bd.edu.su.notification;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 123;
    private EditText email,password,full_name;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button create_account,login_link;
    private Uri image_uri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorerageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        full_name  = findViewById(R.id.reg_full_name);
        progressBar = findViewById(R.id.reg_progressBar);
        imageView = findViewById(R.id.reg_image);
        create_account = findViewById(R.id.reg_create_account);
        login_link = findViewById(R.id.reg_login_link);
        image_uri = null;
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorerageReference = FirebaseStorage.getInstance().getReference().child("images");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choose an Image"),PICK_IMAGE);
            }
        });


        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String my_name = full_name.getText().toString().trim();
                String my_email = email.getText().toString().trim();
                String my_password = password.getText().toString().trim();

                if(!TextUtils.isEmpty(my_name) && !TextUtils.isEmpty(my_email) && !TextUtils.isEmpty(my_password)){
                    if(image_uri != null){
                        progressBar.setVisibility(ProgressBar.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(my_email,my_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    final String user_id = mAuth.getCurrentUser().getUid();
                                    StorageReference user_profile = mStorerageReference.child(user_id+".jpg");
                                    user_profile.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            if(task.isSuccessful()){
                                                String download_url = task.getResult().getDownloadUrl().toString();
                                                Map<String,Object> userMap = new HashMap<>();
                                                userMap.put("name",my_name);
                                                userMap.put("image",download_url);

                                                mFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){
                                                            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                                                            finish();

                                                        }else{
                                                            showErrorDialog("Failed to save full name.");
                                                        }

                                                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                    }
                                                });

                                            }else{
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                showErrorDialog("Failed to upload an image.");
                                            }
                                        }
                                    });

                                }else{
                                    showErrorDialog("Registration is failed");
                                }
                            }
                        });
                    }else{
                        showErrorDialog("Image is required.");
                    }
                }else{
                    showErrorDialog("All fields are required.");
                }
            }
        });

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            image_uri = data.getData();
            imageView.setImageURI(image_uri);
        }
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Can't create account")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .show();
    }
}
