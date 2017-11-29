package com.andsanchez.game2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public Object getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_user, parent, false);
        }

        // Set data into the view.
        ImageView photoImageView = rowView.findViewById(R.id.photoImageView);
        TextView nameTextView = rowView.findViewById(R.id.nameTextView);
        TextView maxTextView = rowView.findViewById(R.id.maxTextView);
        TextView scoreTextView = rowView.findViewById(R.id.scoreTextView);

        User user = this.users.get(position);
        nameTextView.setText(user.getName());
        maxTextView.setText("Max: " + String.valueOf(user.getMax()));
        scoreTextView.setText("Pun: g" + String.valueOf(user.getScore()));
        Picasso.with(this.context).load(user.getPhotoUrl()).into(photoImageView);

        return rowView;
    }
}
