package bd.edu.su.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private TextView notificationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationTextView = findViewById(R.id.notificationText);

        String message = getIntent().getStringExtra("message");
        String from = getIntent().getStringExtra("from_id");

        notificationTextView.setText("From: "+from+" | Message: "+message);
    }
}
