package com.example.tuckbox2008043;

import static android.content.ContentValues.TAG;

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
import java.util.UUID;

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
        // Generate a unique document ID using UUID
        String documentId = UUID.randomUUID().toString();

        // Add the document ID to the address map
        Map<String, Object> addressMap = getAddressMap(address);
        addressMap.put("firestore_id", documentId);

        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .document(documentId)  // Use the generated UUID as document ID
                .set(addressMap)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Address " + documentId + " is inserted in cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Address " + documentId + " is not inserted in cloud DB", e);
                });
    }

    public void deleteDeliveryAddress(DeliveryAddress address) {
        // Query to find the document with matching address and userId
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .whereEqualTo(ConstantsNames.ADDRESS, address.getAddress())
                .whereEqualTo(ConstantsNames.ADDRESS_USER_ID, address.getUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        document.getReference().delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d(TAG, "Address deleted from cloud DB: " + document.getId());
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Failed to delete address: " + document.getId(), e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to query address for deletion", e);
                });
    }

    public void syncAddressesForUser(long userId) {
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .whereEqualTo(ConstantsNames.ADDRESS_USER_ID, userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Found " + queryDocumentSnapshots.size() + " addresses for user " + userId);

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        try {
                            String address = document.getString(ConstantsNames.ADDRESS);
                            Long userIdFromDoc = document.getLong(ConstantsNames.ADDRESS_USER_ID);

                            if (address != null && userIdFromDoc != null) {
                                // Create new address object
                                DeliveryAddress deliveryAddress = new DeliveryAddress(
                                        address,
                                        userIdFromDoc
                                );

                                // Insert into local database
                                long result = dataModel.insertDeliveryAddress(deliveryAddress);
                                if (result == -1) {
                                    Log.e(TAG, "Failed to insert synced address into local DB");
                                } else {
                                    Log.d(TAG, "Successfully synced address: " + address);
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing synced address", e);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync addresses for user " + userId, e);
                });
    }


    public void updateDeliveryAddress(DeliveryAddress address) {
        // Query to find the document with matching address and userId
        cloudDB.collection(ADDRESS_DATA_COLLECTION)
                .whereEqualTo(ConstantsNames.ADDRESS, address.getAddress())
                .whereEqualTo(ConstantsNames.ADDRESS_USER_ID, address.getUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        document.getReference().update(getAddressMap(address))
                                .addOnSuccessListener(unused -> {
                                    Log.d(TAG, "Address updated in cloud DB: " + document.getId());
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Failed to update address: " + document.getId(), e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to query address for update", e);
                });
    }

    public void updateUser(User user) {
        cloudDB.collection(USER_DATA_COLLECTION)
                .document(user.getUserEmail())
                .set(getUserMap(user))
                .addOnSuccessListener(unused -> {
                    Log.d("USER_UPDATE", "User " + user.getUserEmail() + " is updated in cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e("USER_UPDATE", "Failed to update user " + user.getUserEmail(), e);
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
        map.put(ConstantsNames.ADDRESS, address.getAddress());
        map.put(ConstantsNames.ADDRESS_USER_ID, address.getUserId());
        return map;
    }
}