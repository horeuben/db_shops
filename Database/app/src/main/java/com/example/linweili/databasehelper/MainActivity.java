package com.example.linweili.databasehelper;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mShopsDatabaseReference;
    private DatabaseReference mCompaniesDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;

    private FirebaseAuth mFirebaseAuth;




    private EditText shopname;
    private EditText description;
    private EditText pictureurl;
    private EditText Lat;
    private EditText Lng;
    private Button submit;

    private EditText comapnyname;
    private EditText description2;
    private EditText pictureurl2;
    private EditText barcode;
    private Button submit2;

    private Button queryshop_btn;
    private Button querycompany_btn;
    private Button queryallShop;
    private Button increaseFactor_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mShopsDatabaseReference = mFireBaseDatabase.getReference().child("shops");
        mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
        mUsersDatabaseReference = mFireBaseDatabase.getReference().child("users");

        //Toast.makeText(this,mFirebaseAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_SHORT).show();
        shopname = (EditText) findViewById(R.id.shopname);
        pictureurl = (EditText) findViewById(R.id.pictureurl);
        description = (EditText) findViewById(R.id.description);
        Lat = (EditText) findViewById(R.id.lat);
        Lng = (EditText) findViewById(R.id.lng);
        submit = (Button)  findViewById(R.id.submit);
        comapnyname = (EditText) findViewById(R.id.companyname);
        pictureurl2 = (EditText) findViewById(R.id.pictureurl2);
        description2 = (EditText) findViewById(R.id.description2);
        barcode = (EditText) findViewById(R.id.barcode);
        submit2 = (Button) findViewById(R.id.submit2);
        queryshop_btn = (Button) findViewById(R.id.queryshop);
        querycompany_btn = (Button) findViewById(R.id.querycompany);
        queryallShop = (Button) findViewById(R.id.all_shop);
        increaseFactor_btn = (Button) findViewById(R.id.increase);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shop shop = new Shop(shopname.getText().toString(), description.getText().toString(), pictureurl.getText().toString()
                ,Lat.getText().toString(), Lng.getText().toString());
                mShopsDatabaseReference.push().setValue(shop);
            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Company company = new Company(comapnyname.getText().toString(), description2.getText().toString()
                ,pictureurl2.getText().toString(), barcode.getText().toString());
                mCompaniesDatabaseReference.push().setValue(company);
            }
        });

        queryallShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShopsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Log.e("linwei",dataSnapshot.toString());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Shop shop = data.getValue(Shop.class);
                            Log.e("linwei",shop.shopname);
                            //the shop is each shop you have in the database
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        //this is how you retrive data from shops
        queryshop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query shop_query = mShopsDatabaseReference.orderByChild("shopname").equalTo("Gom");
                shop_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data :dataSnapshot.getChildren()) {
                                Shop shop = data.getValue(Shop.class);
                                Log.e("linwei",shop.description);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        //this is how you retrive data from companies
        querycompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query shop_query = mCompaniesDatabaseReference.orderByChild("barcode").equalTo("10");
                shop_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data :dataSnapshot.getChildren()) {
                                Company company = data.getValue(Company.class);
                                Log.e("linwei",company.description);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        mFirebaseAuth = FirebaseAuth.getInstance();
        final String uid = mFirebaseAuth.getUid();
        final String username = mFirebaseAuth.getCurrentUser().getDisplayName();
        increaseFactor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(uid)) {
                            //Log.e("linwei",dataSnapshot.child(uid).child("impactFactor").getValue().toString());
                            double pre_impact = Double.valueOf( dataSnapshot.child(uid).child("impactFactor").getValue().toString());
                            mUsersDatabaseReference.child(uid).child("impactFactor").setValue(pre_impact + 0.5);
                        } else {
                            mUsersDatabaseReference.child(uid).setValue(new User(username,"0.5"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
