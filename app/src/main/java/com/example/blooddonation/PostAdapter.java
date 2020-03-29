package com.example.blooddonation;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.LogTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private static final String TAG = "PostAdapter";

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_layout,parent,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);

        holder.bloodGroup.setText(post.getBloodGroup());
        holder.location.setText(post.getLocation());
        String ct= post.getTs();
        long milliseconds = Long.parseLong(ct);
        String simpleDateFormat = DateFormat.getDateTimeInstance().format(milliseconds);
//        simpleDateFormat.format(post.getTs());
        holder.time.setText(simpleDateFormat);
        holder.description.setText(post.getDescription());

        publisherInfo(holder.image_profile, holder.publisher, post.getPublisher());

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile;
        TextView publisher, time, description, responds, bloodGroup, location;
        Button comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.postPublisherImage);
            publisher = itemView.findViewById(R.id.postPublisherName);
            time = itemView.findViewById(R.id.postTime);
            bloodGroup = itemView.findViewById(R.id.postBloodGroup);
            location = itemView.findViewById(R.id.postLocation);
            description = itemView.findViewById(R.id.postDescription);
            responds = itemView.findViewById(R.id.postResponds);
//            comments = itemView.findViewById(R.id.postComments);

        }
    }

    private void publisherInfo(final ImageView image_profile, final TextView publisher, String userId){

//        Log.i("Problem", "publisherInfo: "+userId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(TextUtils.equals(user.getImageURL(),"default")){
                    image_profile.setImageResource(R.drawable.ic_person_black_50dp);
                }else {
                    Glide.with(mContext).load(user.getImageURL()).into(image_profile);
                }
//                username.setText(user.getEmail());
                publisher.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
