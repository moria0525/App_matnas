package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    private GridView gv;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        imgList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gallery_grid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_gallery);
        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ManagerScreen.FB_DATABASE_PATH);

        Toast.makeText(getApplicationContext(), "ImageListActivity1", Toast.LENGTH_LONG).show();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             progressDialog.dismiss();
             Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_LONG).show();
             //Fetch image data from firebase database
             for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                 //ImageUpload class require default constructor
                 ImageUpload img = snapshot.getValue(ImageUpload.class);
                 Toast.makeText(getApplicationContext(), "loop", Toast.LENGTH_LONG).show();
                 imgList.add(img);
             }

             //Init adapter
             adapter = new ImageListAdapter(ImageListActivity.this, R.layout.image_item, imgList);
             Toast.makeText(getApplicationContext(), "adapter", Toast.LENGTH_LONG).show();
             //Set adapter for listview
            gv.setAdapter(adapter);
         }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        }
        );
    }
}


//
//

//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                progressDialog.dismiss();
//            }
//        });

// }
//}
