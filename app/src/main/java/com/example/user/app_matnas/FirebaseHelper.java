package com.example.user.app_matnas;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/*
* This class help to access data in Firebase
*
 */

public class FirebaseHelper {
    public static DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    public static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static String DB_ACTIVITIES = "activities";
    public static String DB_PROJECTS = "projects";
    public static String DB_TEAM = "team";
    public static String DB_WORKSHOP = "workShop";
    public static String DB_BUSINESS = "business";
    public static String DB_NEWS = "news";
    public static String ST_STORAGE_PROJECT = "logoProjects";
    public static String ST_STORAGE_TEAM = "team";
    public static String ST_STORAGE_BUSINESS = "business";
    public static String DB_IMAGE = "image";
    public static final int GALLERY_CODE = 1;
    public static final int REQUEST_CODE = 1234;
}
