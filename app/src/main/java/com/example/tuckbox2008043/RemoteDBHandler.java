package com.example.tuckbox2008043;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.tuckbox2008043.DataModel.ConstantsNames;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class RemoteDBHandler {
    private AppDataModel dataModel = null;
    private final String USER_DATA_COLLECTION = "Users";
    private final String ADDRESS_DATA_COLLECTION = "DeliveryAddresses";
    private FirebaseFirestore cloudDB;

    public RemoteDBHandler(AppDataModel dataModel) {
        this.dataModel = dataModel;
        this.cloudDB = FirebaseFirestore.getInstance();
    }

    public void insertUser(User user) {
        cloudDB.collection(USER_DATA_COLLECTION)
                .document(user.getUserEmail())
                .set(getUserMap(user))
                .addOnSuccessListener(unused -> {
                    Log.d("USER_INSERT", "User " + user.getUserEmail() + " is inserted in cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e("USER_INSERT", "User " + user.getUserEmail() + " is not inserted in cloud DB", e);
                });
    }

    public void insertDeliveryAddress(DeliveryAddress address) {
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .document(address.getAddressId())
                .set(getAddressMap(address))
                .addOnSuccessListener(unused -> {
                    Log.d("ADDRESS_INSERT", "Address " + address.getAddressId() + " is inserted in cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e("ADDRESS_INSERT", "Address " + address.getAddressId() + " is not inserted in cloud DB", e);
                });
    }

    public void deleteDeliveryAddress(DeliveryAddress address) {
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .document(address.getAddressId())
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d("ADDRESS_DELETE", "Address " + address.getAddressId() + " is deleted from cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e("ADDRESS_DELETE", "Failed to delete address " + address.getAddressId(), e);
                });
    }

    public void syncAddressesForUser(long userId) {
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .whereEqualTo(ConstantsNames.ADDRESS_USER_ID, userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        DeliveryAddress address = new DeliveryAddress(
                                document.getString(ConstantsNames.ADDRESS_ID),
                                document.getString(ConstantsNames.ADDRESS),
                                document.getLong(ConstantsNames.ADDRESS_USER_ID)
                        );
                        // Insert into Room DB - this will trigger LiveData update
                        dataModel.insertDeliveryAddress(address);
                    }
                    Log.d("ADDRESS_SYNC", "Addresses synced for user " + userId);
                })
                .addOnFailureListener(e -> {
                    Log.e("ADDRESS_SYNC", "Failed to sync addresses for user " + userId, e);
                });
    }


    public void updateDeliveryAddress(DeliveryAddress address) {
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .document(address.getAddressId())
                .update(getAddressMap(address))
                .addOnSuccessListener(unused -> {
                    Log.d("ADDRESS_UPDATE", "Address " + address.getAddressId() + " is updated in cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e("ADDRESS_UPDATE", "Failed to update address " + address.getAddressId(), e);
                });
    }

    private Map<String, Object> getUserMap(User user) {
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