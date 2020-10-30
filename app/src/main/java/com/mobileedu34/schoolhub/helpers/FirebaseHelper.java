package com.mobileedu34.schoolhub.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileedu34.schoolhub.models.User;

import java.util.Map;

public class FirebaseHelper {

    static FirebaseAuth fAuth = FirebaseAuth.getInstance();
    static FirebaseUser fUser = fAuth.getCurrentUser();
    static String USERS_COLLECTION = "users";

    public static CollectionReference getCollectionRef(String collectionName){
        return FirebaseFirestore.getInstance().collection(collectionName);
    }

    private static void refreshAuthState() {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
    }

    public static Task<Void> createUser(User user){
        refreshAuthState();
        return getCollectionRef(USERS_COLLECTION)
                .document(fUser.getUid())
                .set(user);
    }

    public static Task<Void> deleteUser (String userId) {
        return getCollectionRef(USERS_COLLECTION).document(userId).delete();
    }

    public static Task<DocumentSnapshot> getUser (String number) {
        return getCollectionRef(USERS_COLLECTION).document(number).get();
    }

    public static Task<QuerySnapshot> getUsers () {
        return getCollectionRef(USERS_COLLECTION).get();
    }

    public  static  Task<Void> updateUser(String userId, Map<String, Object> userInfo){
        return getCollectionRef(USERS_COLLECTION).document(userId).update(userInfo);
    }

    public static Task<AuthResult> signInUser(String emailAddress, String password){


        return fAuth.signInWithEmailAndPassword(
                emailAddress,
                password
        );
    }

    public static Task<AuthResult> signUpUser(String emailAddress, String password){
        return fAuth.createUserWithEmailAndPassword(
                emailAddress,
                password
        );
    }

    // Auto-generate document ID
    // String id = db.collection("collection_name").document().getId();

}
