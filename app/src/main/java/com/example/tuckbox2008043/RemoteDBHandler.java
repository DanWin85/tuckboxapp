package com.example.tuckbox2008043;

import static android.content.ContentValues.TAG;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.tuckbox2008043.DataModel.ConstantsNames;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.Order;
import com.example.tuckbox2008043.DataModel.OrderItem;
import com.example.tuckbox2008043.DataModel.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RemoteDBHandler {
    private AppDataModel dataModel = null;
    private final String USER_DATA_COLLECTION = "Users";
    private final String ADDRESS_DATA_COLLECTION = "DeliveryAddresses";
    private final String ORDER_DATA_COLLECTION = "Orders";
    private static final String ORDER_ITEMS_COLLECTION = "OrderItems";
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

    public void syncAddressesForUser(long userId) {
        // Get local addresses first
        List<DeliveryAddress> localAddresses = dataModel.getAddressesForUserDirect(userId);

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
                                // Check if this address already exists locally
                                boolean exists = localAddresses.stream()
                                        .anyMatch(localAddress ->
                                                localAddress.getUserId() == userIdFromDoc &&
                                                        localAddress.getAddress().equals(address)
                                        );

                                if (!exists) {
                                    DeliveryAddress deliveryAddress = new DeliveryAddress(
                                            address,
                                            userIdFromDoc
                                    );
                                    long result = dataModel.insertDeliveryAddress(deliveryAddress);
                                    if (result == -1) {
                                        Log.e(TAG, "Failed to insert synced address into local DB");
                                    } else {
                                        Log.d(TAG, "Successfully synced new address: " + address);
                                    }
                                } else {
                                    Log.d(TAG, "Address already exists locally, skipping: " + address);
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




    public void updateDeliveryAddress(DeliveryAddress address) {

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

    public void insertOrder(Order order, List<OrderItem> orderItems) {
        // Generate a unique document ID using UUID
        String documentId = UUID.randomUUID().toString();

        // Create order map
        Map<String, Object> orderMap = getOrderMap(order);
        orderMap.put("firestore_id", documentId);

        cloudDB.collection(ORDER_DATA_COLLECTION)
                .document(documentId)
                .set(orderMap)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Order " + documentId + " is inserted in cloud DB");

                    // Insert order items
                    for (OrderItem item : orderItems) {
                        String itemId = UUID.randomUUID().toString();
                        Map<String, Object> itemMap = getOrderItemMap(item);
                        itemMap.put("firestore_id", itemId);
                        itemMap.put("order_id", documentId);

                        cloudDB.collection(ORDER_ITEMS_COLLECTION)
                                .document(itemId)
                                .set(itemMap);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Order " + documentId + " is not inserted in cloud DB", e);
                });
    }


    private Order documentToOrder(DocumentSnapshot document) {
        try {
            Date orderDate = document.getDate("Order_Date");
            long cityId = document.getLong("City_ID");
            long timeSlotId = document.getLong("Time_Slot_ID");
            long userId = document.getLong("User_ID");

            return new Order(cityId, timeSlotId, userId);
        } catch (Exception e) {
            Log.e(TAG, "Error converting document to Order", e);
            return null;
        }
    }
    public void insertOrderWithItems(Order order, List<OrderItem> orderItems) {
        String orderId = UUID.randomUUID().toString();
        Map<String, Object> orderMap = getOrderMap(order);
        orderMap.put("firestore_id", orderId);

        // Insert order
        cloudDB.collection(ORDER_DATA_COLLECTION)
                .document(orderId)
                .set(orderMap)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Order " + orderId + " is inserted in cloud DB");

                    // Insert order items
                    for (OrderItem item : orderItems) {
                        String itemId = UUID.randomUUID().toString();
                        Map<String, Object> itemMap = getOrderItemMap(item);
                        itemMap.put("firestore_id", itemId);
                        itemMap.put("order_id", orderId);

                        cloudDB.collection(ORDER_ITEMS_COLLECTION)
                                .document(itemId)
                                .set(itemMap)
                                .addOnSuccessListener(unused2 -> {
                                    Log.d(TAG, "Order item " + itemId + " is inserted in cloud DB");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Failed to insert order item " + itemId, e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to insert order " + orderId, e);
                });
    }

    private void syncOrderItems(String cloudOrderId, long localOrderId, Order currentOrder) {
        cloudDB.collection(ORDER_ITEMS_COLLECTION)
                .whereEqualTo("order_id", cloudOrderId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<OrderItem> items = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        try {
                            long foodId = document.getLong(ConstantsNames.FOOD_ID);
                            int quantity = document.getLong(ConstantsNames.QUANTITY).intValue();

                            OrderItem orderItem = new OrderItem(localOrderId, foodId, quantity);
                            items.add(orderItem);
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing synced order item", e);
                        }
                    }
                    if (!items.isEmpty()) {
                        dataModel.insertOrder(currentOrder, items);
                    }
                });
    }

    public void syncOrdersForUser(long userId) {
        cloudDB.collection(ORDER_DATA_COLLECTION)
                .whereEqualTo(ConstantsNames.USER_ID, userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Found " + queryDocumentSnapshots.size() + " orders for user " + userId);

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        try {
                            Date orderDate = document.getDate(ConstantsNames.ORDER_DATE);
                            long cityId = document.getLong(ConstantsNames.CITY_ID);
                            long timeSlotId = document.getLong(ConstantsNames.TIME_SLOT_ID);

                            Order order = new Order(cityId, timeSlotId, userId);
                            order.setOrderDate(orderDate);

                            // Insert into local database
                            long orderId = dataModel.insertOrder(order);

                            if (orderId != -1) {
                                // Sync order items
                                syncOrderItems(document.getId(), orderId, order);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing synced order", e);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync orders for user " + userId, e);
                });
    }

    public void deleteUser(User user) {
        cloudDB.collection(USER_DATA_COLLECTION)
                .document(user.getUserEmail())
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "User deleted from cloud DB");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete user from cloud DB", e);
                });
    }

    public void deleteOrder(Order order) {
        cloudDB.collection(ORDER_DATA_COLLECTION)
                .whereEqualTo(ConstantsNames.USER_ID, order.getUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        document.getReference().delete();
                    }
                });
    }

    private Map<String, Object> getOrderItemMap(OrderItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantsNames.FOOD_ID, item.getFoodId());
        map.put(ConstantsNames.QUANTITY, item.getQuantity());
        return map;
    }

    private Map<String, Object> getOrderMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantsNames.ORDER_DATE, order.getOrderDate());
        map.put(ConstantsNames.CITY_ID, order.getCityId());
        map.put(ConstantsNames.TIME_SLOT_ID, order.getTimeSlotId());
        map.put(ConstantsNames.USER_ID, order.getUserId());
        return map;
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