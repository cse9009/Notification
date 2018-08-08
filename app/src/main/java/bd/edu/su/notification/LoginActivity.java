package bd.edu.su.notification;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private ProgressBar progressBar;
    private Button login,register_link;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progressBar);
        login = findViewById(R.id.login_button);
        register_link = findViewById(R.id.login_registration_link);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String my_email = email.getText().toString().trim();
                String my_password = password.getText().toString().trim();

                if(!TextUtils.isEmpty(my_email) && !TextUtils.isEmpty(my_password)){

                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    mAuth.signInWithEmailAndPassword(my_email,my_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                //get the token ID
                                //String my_token = FirebaseInstanceId.getInstance().getToken();
                                MyFirebaseInstanceIDService fi = new MyFirebaseInstanceIDService();
                                fi.onTokenRefresh();

                                String my_token = fi.getToken_id();
                                String currentUserId = mAuth.getCurrentUser().getUid();

                                Map<String,Object> token_obj = new HashMap<>();
                                token_obj.put("tokenId",my_token);

                                //showErrorDialog("Token ID: "+my_token);

                                mFirestore.collection("Users").document(currentUserId).update(token_obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                                        showErrorDialog(e.getMessage().toString());
                                    }
                                });



                            }else{
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                showErrorDialog("Credential is mismatch.");
                            }

                        }
                    });

                }else{
                    showErrorDialog("All fields are required.");
                }
            }
        });

        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Login failed...")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .show();
    }
}
