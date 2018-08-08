package bd.edu.su.notification;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends AppCompatActivity {

    private TextView userNameText;
    private CircleImageView imageView;
    private EditText notificationText;
    private Button sendNotificationButton;
    private ProgressBar progressBar;
    private String current_uid = "";
    private String recipient_uid = "";
    private String recipient_user_name = "";
    private String recipient_image_uri = "";
    private FirebaseFirestore mStore;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        userNameText = findViewById(R.id.send_to_user_name);
        imageView = findViewById(R.id.send_to_user_image);
        notificationText = findViewById(R.id.send_notification_text);
        sendNotificationButton = findViewById(R.id.send_notification_button);
        progressBar = findViewById(R.id.send_notification_progressBar);
        mStore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recipient_uid = getIntent().getStringExtra("user_id");
        recipient_user_name = getIntent().getStringExtra("user_name");
        recipient_image_uri = getIntent().getStringExtra("user_image");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notification = notificationText.getText().toString().trim();
                if(!TextUtils.isEmpty(notification)){
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    //Using Realtime Database
                    InstantMessage instantMessage = new InstantMessage(current_uid,notification);
                    databaseReference.child("Users").child(recipient_uid).child("Notifications").push().setValue(instantMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                showErrorDialog("Realtime database entry failed.");
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }
                        }
                    });

                    Map<String,Object> noti = new HashMap<>();
                    noti.put("from",current_uid);
                    noti.put("message",notification);

                    mStore.collection("Users/"+recipient_uid + "/Notifications").add(noti).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                notificationText.setText("");
                                showErrorDialog("Successful");
                            }else{
                                showErrorDialog("Can't send notification");
                            }
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    });

                }else{
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    showErrorDialog("Notification text is required.");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userNameText.setText(recipient_user_name);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image11);
        Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(recipient_image_uri).into(imageView);
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Sending notification...")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .show();
    }
}
