package com.example.blooddonation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    Context mContext;
    List<Message> mMessage;

    FirebaseUser currentUser;

    public MessageAdapter(Context mContext, List<Message> mMessage) {
        this.mContext = mContext;
        this.mMessage = mMessage;
    }

    private static final String TAG = "MessageAdapter";
    private static final int LEFT_MESSAGE = 1, RIGHT_MESSAGE = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == LEFT_MESSAGE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_layout, parent, false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_right_layout,parent,false);
        }
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Message message = mMessage.get(position);

        holder.messageBox.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView messageBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageBox = itemView.findViewById(R.id.messageArea);

        }
    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mMessage.get(position).getSender().equals(currentUser.getUid()))
            return RIGHT_MESSAGE;
        else
            return LEFT_MESSAGE;
    }
}
