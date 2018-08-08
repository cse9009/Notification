package bd.edu.su.notification;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView user_name;
    CircleImageView user_image;
    ConstraintLayout user_layout;

    public ViewHolder(View itemView) {
        super(itemView);

        user_name = itemView.findViewById(R.id.user_full_name);
        user_image = itemView.findViewById(R.id.user_image);
        user_layout = itemView.findViewById(R.id.user_containerLayout);

    }
}
