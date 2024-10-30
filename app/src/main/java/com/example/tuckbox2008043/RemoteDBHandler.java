package com.example.tuckbox2008043;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tuckbox2008043.DataModel.ConstantsNames;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.core.Query;

import java.util.HashMap;
import java.util.Map;

public class RemoteDBHandler {
    AppDataModel dataModel = null;

    public RemoteDBHandler(AppDataModel dataModel){
        this.dataModel = dataModel;
    }

    private final String USER_DATA_COLLECTION = "Users";
    private final String ADDRESS_DATA_COLLECTION = "DeliveryAddresses";

    public void insertUser(User user){
        FirebaseFirestore cloudDB = FirebaseFirestore.getInstance();
        cloudDB.collection(USER_DATA_COLLECTION)
                .document(user.getUserEmail()).set(getUserMap(user))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("USER_INSERT", "User" + user.getUserEmail() + "is inserted in cloud DB");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USER_INSERT", "USER" + user.getUserEmail() + "is not inserted in cloud DB");
                    }
                });
    }

    public void insertDeliveryAddress(DeliveryAddress address) {
        FirebaseFirestore cloudDB = FirebaseFirestore.getInstance();
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .document(address.getAddressId())
                .set(getAddressMap(address))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ADDRESS_INSERT", "Address " + address.getAddressId() + " is inserted in cloud DB");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ADDRESS_INSERT", "Address " + address.getAddressId() + " is not inserted in cloud DB");
                    }
                });
    }

    public void deleteDeliveryAddress(DeliveryAddress address) {
        FirebaseFirestore cloudDB = FirebaseFirestore.getInstance();
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .document(address.getAddressId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ADDRESS_DELETE", "Address " + address.getAddressId() + " is deleted from cloud DB");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ADDRESS_DELETE", "Failed to delete address " + address.getAddressId());
                    }
                });
    }

    private Map<String, Object> getUserMap(User user){
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantsNames.USER_ID, user.getUserId());
        map.put(ConstantsNames.USER_EMAIL, user.getUserEmail());
        map.put(ConstantsNames.USER_PASSWORD, user.getPassword());
        map.put(ConstantsNames.USER_FIRST_NAME, user.getFirstName());
        map.put(ConstantsNames.USER_LAST_NAME, user.getLastName());
        map.put(ConstantsNames.USER_MOBILE, user.getMobile());
        return map;
    }

    private Map<String, Object> getAddressMap(DeliveryAddress address) {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantsNames.ADDRESS_ID, address.getAddressId());
        map.put(ConstantsNames.ADDRESS, address.getAddress());
        map.put(ConstantsNames.ADDRESS_USER_ID, address.getUserId());
        return map;
    }
}

