package com.example.sda_a5_real_estate_app_jasminekhalimova;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class ListingsFragment extends Fragment {
    public static final String TAG = "TAG";
    TextView aType,propAddress,fprice,propDesc,propArea,adverName,adverEmail;
    FirebaseFirestore fStore;
    String productID;
    String userID;
    FirebaseAuth fAuth;
    ImageView displayImageView;
    StorageReference storageReference;

    ArrayList<Product> mArrayList = new ArrayList<>();

    public ListingsFragment(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        aType = view.findViewById(R.id.adTitle);
        fprice = view.findViewById(R.id.price);
        propAddress = view.findViewById(R.id.adCity);
        propArea = view.findViewById(R.id.adArea);
        propDesc = view.findViewById(R.id.adDescrip);
        adverName = view.findViewById(R.id.advertiserName);
        adverEmail = view.findViewById(R.id.advertiserEmail);
        displayImageView = view.findViewById(R.id.displayImageView);


        fStore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        // Retrieving image from firebase storage
        StorageReference profileRef = storageReference.child("house1.jpeg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(displayImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        // Fetching User details from firebase cloud storage
        fStore.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG,"onSuccess: LIST EMPTY");
                    return;
                } else {
                    // Convert the whole Query Snapshot to a list
                    // of objects directly! No need to fetch each
                    // document.
                    List<Product> types = documentSnapshots.toObjects(Product.class);

                    // Add all to your list
                    mArrayList.addAll(types);
                    Log.d(TAG, "onSuccess: " + mArrayList);
                    //todo display all products
                    Product product = mArrayList.get(0);
                    aType.setText(product.getTitle());
                    fprice.setText(product.getPrice());
                    propAddress.setText(product.getCity());
                    propArea.setText(product.getArea());
                    propDesc.setText(product.getDescription());
                    adverName.setText(product.getAdvertiserName());
                    adverEmail.setText(product.getAdvertiserEmail());
                }
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure: " + e.toString());
                }
            });

        return view;
    }
}
