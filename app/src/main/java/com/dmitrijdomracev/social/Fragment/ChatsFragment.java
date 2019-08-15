package com.dmitrijdomracev.social.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmitrijdomracev.social.Adapter.UserAdapter;
import com.dmitrijdomracev.social.Model.Chat;
import com.dmitrijdomracev.social.Model.User;
import com.dmitrijdomracev.social.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;

    UserAdapter userAdapter;
    List<User> mUsers;

    FirebaseUser fUser;
    DatabaseReference reference;

    static final String TAG = "chatsTag";

    private List<String> usersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(fUser.getUid())){
                        addUID(usersList, chat.getReceiver());
//                        usersList.add(chat.getReceiver());
//                        Log.d(TAG, chat.getReceiver() + " (получатель) добавлен в usersList");
                    }

                    if (chat.getReceiver().equals(fUser.getUid())){
                        addUID(usersList, chat.getSender());
//                        usersList.add(chat.getSender());
//                        Log.d(TAG, chat.getSender() + " (отправитель) добавлен в usersList");

                    }

                }

                readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void addUID(List<String> usersList, String userID) {
        if (!usersList.contains(userID)) {
            usersList.add(userID);
            Log.d(TAG, userID + " добавлен в usersList");

        }
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    for (String id: usersList) {
                        if (user.getId().equals(id)){
//                             if (mUsers.size() > 0){
//                                for (User user1: mUsers) {
//                                    if (!user.getId().equals(user1.getId())){
//                                        mUsers.add(user);
//                                    }
//                                }
//
//                            } else {
                                mUsers.add(user);
//                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
