package com.example.sda_a5_real_estate_app_jasminekhalimova;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Author: Jasmine Khalimova
 * Post an Ad Form
 *
 */
public class AdsFragment extends Fragment {

    public static final String TAG = "TAG";
    //Declaring variables
    EditText aType,propType,propAddress,beNumber,baNumber,
            fprice,priceT,adverName,adverEmail,adverPhone;
    Button postBtn;
    EditText cancel;
    FirebaseFirestore fStore;
    String productID;
    String userID;
    FirebaseAuth fAuth;

    public AdsFragment(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ads, container, false);

        aType = view.findViewById(R.id.adType);
        propType = view.findViewById(R.id.propertyType);
        propAddress = view.findViewById(R.id.propertyAddress);
        beNumber = view.findViewById(R.id.bedNumber);
        baNumber = view.findViewById(R.id.bathNumber);
        fprice = view.findViewById(R.id.price);
        priceT = view.findViewById(R.id.priceType);
        adverName = view.findViewById(R.id.advertiserName);
        adverEmail = view.findViewById(R.id.advertiserEmail);
        adverPhone = view.findViewById(R.id.advertiserPhone);

        postBtn = view.findViewById(R.id.postBtn);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        final FirebaseUser user = fAuth.getCurrentUser();

        // Fetching User details from firebase cloud storage
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    adverName.setText(documentSnapshot.getString("fName"));
                    adverEmail.setText(documentSnapshot.getString("email"));
                }
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String adType = aType.getText().toString().trim();
                final String propertyType = propType.getText().toString().trim();
                final String propertyAddress = propAddress.getText().toString().trim();
                final String bedNumber = beNumber.getText().toString().trim();
                final String bathNumber = baNumber.getText().toString().trim();
                final String price = fprice.getText().toString().trim();
                final String priceType = priceT.getText().toString().trim();
                final String advertiserName = adverName.getText().toString().trim();
                final String advertiserEmail = adverEmail.getText().toString().trim();
                final String advertiserPhone = adverPhone.getText().toString().trim();

                // Setting up validations
                if(TextUtils.isEmpty(adType)){
                    aType.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(propertyType)){
                    propType.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(propertyAddress)){
                    propAddress.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(bedNumber)){
                    beNumber.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(bathNumber)){
                    baNumber.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(price)){
                    fprice.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(priceType)){
                    priceT.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(advertiserEmail)){
                    adverEmail.setError("Field is required.");
                    return;
                }

                // Saving to Database
                DocumentReference documentReference = fStore.collection("products").document();
                Map<String,Object> product = new HashMap<>();
                product.put("AdType",adType);
                product.put("PropertyType",propertyType);
                product.put("Address",propertyAddress);
                product.put("Beds",bedNumber);
                product.put("Baths",bathNumber);
                product.put("Price",price);
                product.put("PriceType",priceType);
                product.put("AdvertiserUserId",userID);
                product.put("AdvertiserName",advertiserName);
                product.put("AdvertiserEmail",advertiserEmail);
                product.put("AdvertiserPhone",advertiserPhone);

                documentReference.set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: product is created for "+ productID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                startActivity(new Intent(getContext(),MainActivity.class));
            }
        });

        return view;
    }
}
