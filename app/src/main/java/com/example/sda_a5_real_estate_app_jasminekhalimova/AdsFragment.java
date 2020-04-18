package com.example.sda_a5_real_estate_app_jasminekhalimova;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Author: Jasmine Khalimova
 * Post an Ad Form
 *
 */
public class AdsFragment extends Fragment {

    public static final int GALLERY_REQUEST_CODE = 105;

    public static final String TAG = "TAG";
    //Declaring variables
    EditText aType,propAddress,fprice,propDesc,propArea,adverName,adverEmail;
    Button postBtn, galleryBtn;
    FirebaseFirestore fStore;
    String userID;
    FirebaseAuth fAuth;
    ImageView displayImageView;
    StorageReference storageReference;

    public AdsFragment(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ads, container, false);

        aType = view.findViewById(R.id.adTitle);
        fprice = view.findViewById(R.id.price);
        propAddress = view.findViewById(R.id.adCity);
        propArea = view.findViewById(R.id.adArea);
        propDesc = view.findViewById(R.id.adDescrip);
        adverName = view.findViewById(R.id.advertiserName);
        adverEmail = view.findViewById(R.id.advertiserEmail);

        galleryBtn = view.findViewById(R.id.galleryBtn);
        displayImageView = view.findViewById(R.id.displayImageView);

        postBtn = view.findViewById(R.id.postBtn);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        final FirebaseUser user = fAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();


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

                final String adTitle = aType.getText().toString().trim();
                final String price = fprice.getText().toString().trim();
                final String adCity = propAddress.getText().toString().trim();
                final String adArea = propArea.getText().toString().trim();
                final String adDescrip = propDesc.getText().toString().trim();
                final String advertiserName = adverName.getText().toString().trim();
                final String advertiserEmail = adverEmail.getText().toString().trim();

                // Setting up validations
                if(TextUtils.isEmpty(adTitle)){
                    aType.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(price)){
                    fprice.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(adCity)){
                    propAddress.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(adArea)){
                    propArea.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(adDescrip)){
                    propDesc.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(advertiserName)){
                    adverName.setError("Field is required.");
                    return;
                }
                if(TextUtils.isEmpty(advertiserEmail)){
                    adverEmail.setError("Field is required.");
                    return;
                }

                // Saving to Database
                DocumentReference documentReference = fStore.collection("products").document();
                Map<String,Object> product = new HashMap<>();
                product.put("Title",adTitle);
                product.put("Price",price);
                product.put("City",adCity);
                product.put("Area",adArea);
                product.put("Description",adDescrip);
                product.put("AdvertiserUserId",userID);
                product.put("AdvertiserName",advertiserName);
                product.put("AdvertiserEmail",advertiserEmail);

                documentReference.set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: product is created for ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ListingsFragment()).commit();
            }
        });

        //Opening Gallery
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // choose from gallery
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    Uri imageUri = data.getData();
                    String imageFileName = "JPEG_" + "."+getFileExt(imageUri);
                    Log.d(TAG, "onActivityResult: Gallery Image Uri:  " +  imageFileName);
                    displayImageView.setImageURI(imageUri);
                    uploadImageToFirebase(imageFileName, imageUri);
                }
            }
        }
    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    //uploading to firebase
    private void uploadImageToFirebase(String name, Uri imageUri) {
        StorageReference fileRef = storageReference.child("products/" + name);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(),"Image Selected", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}