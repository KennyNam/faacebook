package com.yarn.facebook;
 
import java.text.SimpleDateFormat;
 
import android.graphics.Bitmap;
import com.facebook.android.Facebook;

public class FaceBookBasicInfo {
  
	private FaceBookBasicInfo() {}
	
    public static final int REQ_CODE_FACEBOOK_LOGIN = 1001;
 
    public static boolean FacebookLogin = false;
    public static boolean RetryLogin = false;
 
    public static Facebook FacebookInstance = null;
 
    public static String[] FACEBOOK_PERMISSIONS = {"publish_stream", "read_stream", "user_photos", "email"};
 
    public static String FACEBOOK_ACCESS_TOKEN = "";
    public static String FACEBOOK_APP_ID = "660054474025835";
    public static String FACEBOOK_API_KEY = "";
    public static String FACEBOOK_APP_SECRET = "7bfecb806558c7c0bcdd543d833e7b97";
 
    public static String FACEBOOK_NAME = "";
    public static String FACEBOOK_EMAIL = "";
 
    public static SimpleDateFormat OrigDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
 
    public static Bitmap BasicPicture = null;
}
