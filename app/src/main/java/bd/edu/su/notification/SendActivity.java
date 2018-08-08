package bd.edu.su.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendActivity extends AppCompatActivity {
    private TextView userNameText;
    private ImageView imageView;
    private EditText notificationText;
    private Button sendNotificationButton;
    private String current_uid = "";
    private String recipient_uid = "";
    private String recipient_user_name = "";
    private String recipient_image_uri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        userNameText = findViewById(R.id.send_to_user_name);
        imageView = findViewById(R.id.send_to_user_image);
        notificationText = findViewById(R.id.send_notification_text);
        sendNotificationButton = findViewById(R.id.send_notification_button);

        recipient_uid = getIntent().getStringExtra("user_id");
        recipient_user_name = getIntent().getStringExtra("user_name");
        recipient_image_uri = getIntent().getStringExtra("user_image");
//
//        userNameText.setText(recipient_user_name);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.image11);
//        Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(recipient_image_uri).into(imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userNameText.setText(recipient_user_name);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image11);
        Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(recipient_image_uri).into(imageView);
    }
}
