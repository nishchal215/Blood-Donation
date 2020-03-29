package com.example.blooddonation;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private static final String TAG = "FeedFragment";

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_feed,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            postLists = new ArrayList<>();
            postAdapter = new PostAdapter(getContext(), postLists);
            recyclerView.setAdapter(postAdapter);

            readPosts();
        }

        return view;
    }

    private void readPosts(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String,String> map=new HashMap<>();
                   // Post post = snapshot.getValue(Post.class);
                   // postLists.add(post);
                    map=(Map<String, String>) snapshot.getValue();
//                    Log.i(TAG, ""+map);
                    Post post=new Post(map.get("Blood group"),map.get("Description"),map.get("Publisher"),String.valueOf(map.get("TimeStamp")),map.get("Location"));
                    postLists.add(post);
//                    Log.i(TAG, "onDataChange: "+map.get("TimeStamp"));
//                    Log.i("Post",""+" haha"+postLists.get(0).getPublisher());

                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
