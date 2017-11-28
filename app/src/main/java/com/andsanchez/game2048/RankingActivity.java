package com.andsanchez.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RankingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        final ArrayList<User> users = new ArrayList<User>();
        final ListView listView = findViewById(R.id.listView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .orderBy("max", Query.Direction.DESCENDING)
            .orderBy("score", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                        User userDb = document.toObject(User.class);
                        users.add(userDb);
                    }
                    listView.setAdapter(new UserAdapter(getApplicationContext(), users));
                }
            });
    }
}
