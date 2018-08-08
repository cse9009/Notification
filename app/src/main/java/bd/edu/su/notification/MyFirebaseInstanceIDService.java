package bd.edu.su.notification;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String token_id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        this.token_id = FirebaseInstanceId.getInstance().getToken();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("TOKEN", "Refreshed token: " + refreshedToken);

        //MyFirebaseInstanceIDService fi = new MyFirebaseInstanceIDService();
        //String my_token = fi.getToken_id();
//        String currentUserId = mAuth.getCurrentUser().getUid();
//
//        Map<String,Object> token_obj = new HashMap<>();
//        token_obj.put("tokenId",refreshedToken);
//
//        mFirestore.collection("Users").document(currentUserId).update(token_obj);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }

    public String getToken_id() {
        if(token_id == null){
            token_id= "abcd";
        }
        return token_id;
    }
}
