package bd.edu.su.notification;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button logout_button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private CircleImageView mImage;
    private TextView mName;
    private String user_id = "";


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout_button = view.findViewById(R.id.logout_button);
        mImage = view.findViewById(R.id.profile_image);
        mName = view.findViewById(R.id.profile_full_name);

        mStore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name = task.getResult().getString("name");
                    String image = task.getResult().getString("image");

                    mName.setText(name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.image11);

                    Glide.with(container.getContext()).setDefaultRequestOptions(requestOptions).load(image).into(mImage);
                }
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> tokeObj = new HashMap<>();
                tokeObj.put("tokenId", FieldValue.delete());
                mStore.collection("Users").document(mAuth.getCurrentUser().getUid()).update(tokeObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mAuth.signOut();
                            startActivity(new Intent(getActivity(),LoginActivity.class));
                        }else{
                            showErrorDialog(task.getException().getMessage().toString());
                        }
                    }
                });

            }
        });

        return view;
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(getContext())
                .setTitle("Logout failed")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .show();
    }

}
