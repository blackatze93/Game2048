package com.andsanchez.game2048;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class MainActivity extends Activity {

    private TextView nameTextView;
    private ImageView photoImageView;
    private Spinner levelSpinner;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        levelSpinner = (Spinner) findViewById(R.id.levelSpinner);

        Integer[] levels = {3, 4, 5};
        levelSpinner.setAdapter(new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, levels));

        levelSpinner.setSelection(1);

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                level = 0;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();

            nameTextView.setText(name);
            Picasso.with(this).load(photoUrl).into(photoImageView);
//
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("levels").document("3").collection("users")
//                .orderBy("max", Query.Direction.DESCENDING)
//                .orderBy("score", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
//                            System.out.println(document.getData());
//                            System.out.println("GATOS GRATIS");
//                        }
//                    }
//                });
        } else {
            goLoginScreen();
        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void ranking(View view) {
        Intent intent = new Intent(this, RankingActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    public void newGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
