package bd.edu.su.notification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Users> users;
    private Context context;

    public RecyclerAdapter(List<Users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users user = users.get(position);
        holder.user_name.setText(user.getName());

        holder.user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TestActivity.class);
                intent.putExtra("user_id",user.getUser_id());
                intent.putExtra("user_name",user.getName());
                intent.putExtra("user_image",user.getImage());
                context.startActivity(intent);
            }
        });

        Glide.with(context).load(user.getImage()).into(holder.user_image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
