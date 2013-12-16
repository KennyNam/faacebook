package com.yarn.facebook;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.turbomanage.httpclient.ParameterMap;
import com.yarn.http.HttpPostAsyncTask;

public class FaceBookLoginActivity extends Activity {
	private Button mConnectFacebookButton;
	private Button mSignInButton;
	private Facebook mFacebook;
	private HttpPostAsyncTask mHttpPostAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mConnectFacebookButton = (Button) findViewById(R.id.facebook_connect_button);
		mConnectFacebookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				try {
					connectFacebookUseFacebookClass();
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("LoginActivityUseFacebookInstance/onCreate", "JSON error");
				}
			}
		});
		mSignInButton = (Button) findViewById(R.id.sign_in_button);
		mSignInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				connectFacebookUseFacebookClass();
			}
		});
		
	}
	
	private void signUpWithFacebook() throws InterruptedException, ExecutionException, JSONException{
		String url = Constant.URL_DOMAIN;
		String path = Constant.USER + "connect_facebook/?" + "app_token=" + Constant.APP_TOKEN;
		
		ParameterMap params = new ParameterMap()
        		.add("name ", FaceBookBasicInfo.FACEBOOK_NAME)
        		.add("offset_time", "-8")
        		.add("access_token", FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN)
        		.add("facebook_id", FaceBookBasicInfo.FACEBOOK_ID)
        		.add("image_url", FaceBookBasicInfo.FACEBOOK_IMAGE_URL)
        		.add("email", FaceBookBasicInfo.FACEBOOK_EMAIL);
		
		Log.e("name", FaceBookBasicInfo.FACEBOOK_NAME);
		Log.e("access_token", FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);
		Log.e("facebook_id", FaceBookBasicInfo.FACEBOOK_ID);
		Log.e("image_url", FaceBookBasicInfo.FACEBOOK_IMAGE_URL);
		Log.e("email", FaceBookBasicInfo.FACEBOOK_EMAIL);
		
		mHttpPostAsyncTask = new HttpPostAsyncTask (FaceBookLoginActivity.this, url, path, params);
		mHttpPostAsyncTask.execute();
	}
	
	@SuppressWarnings("deprecation")
	public void connectFacebookUseFacebookClass() {
		try {
			mFacebook = new Facebook(FaceBookBasicInfo.FACEBOOK_APP_ID);
			FaceBookBasicInfo.FacebookInstance = mFacebook;
			if (!FaceBookBasicInfo.RetryLogin
					&& FaceBookBasicInfo.FacebookLogin == true) {
				mFacebook.setAccessToken(FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);
				signUpWithFacebook();
			} else {
				mFacebook.authorize(FaceBookLoginActivity.this,
						FaceBookBasicInfo.FACEBOOK_PERMISSIONS,
						new AuthorizationListener());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class AuthorizationListener implements DialogListener {

		public void onComplete(Bundle values) {
			Thread faceBookLoginTread = new Thread() {
				public void run() {
					try {
						String resultStr = FaceBookBasicInfo.FacebookInstance.request("me");
						JSONObject obj = new JSONObject(resultStr);
						Log.e("facebook", obj.toString());
						FaceBookBasicInfo.FACEBOOK_NAME = obj.getString("name");
						FaceBookBasicInfo.FACEBOOK_EMAIL = obj.getString("email");
						FaceBookBasicInfo.FACEBOOK_ID = obj.getString("id");
						FaceBookBasicInfo.FACEBOOK_IMAGE_URL = "http://graph.facebook.com/"+FaceBookBasicInfo.FACEBOOK_ID+"/picture?type=large";
						FaceBookBasicInfo.FacebookLogin = true;
						FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN = FaceBookBasicInfo.FacebookInstance.getAccessToken();
						signUpWithFacebook();
					} catch (Exception e) {
					}
				}
			};
			faceBookLoginTread.start();
		}

		@Override
		public void onFacebookError(FacebookError e) {}

		@Override
		public void onError(DialogError e) {}

		@Override
		public void onCancel() {}
	}
	
	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode,
			Intent resultIntent) {
		super.onActivityResult(requestCode, resultCode, resultIntent);
		if (resultCode == RESULT_OK) {
			if (requestCode == 32665) {
				FaceBookBasicInfo.FacebookInstance.authorizeCallback(requestCode, resultCode, resultIntent);
			}
		}
	}

	private void saveProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("FacebookLogin", FaceBookBasicInfo.FacebookLogin);
		editor.putString("FACEBOOK_ACCESS_TOKEN", FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);
		editor.putString("FACEBOOK_NAME", FaceBookBasicInfo.FACEBOOK_NAME);
		editor.putString("FACEBOOK_EMAIL", FaceBookBasicInfo.FACEBOOK_EMAIL);
		editor.putString("FACEBOOK_ID", FaceBookBasicInfo.FACEBOOK_ID);
		editor.putString("FACEBOOK_IMAGE_URL", FaceBookBasicInfo.FACEBOOK_IMAGE_URL);
		editor.commit();
	}

	private void loadProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);
		FaceBookBasicInfo.FacebookLogin = pref.getBoolean("FacebookLogin", false);
		FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN = pref.getString("FACEBOOK_ACCESS_TOKEN", "");
		FaceBookBasicInfo.FACEBOOK_NAME = pref.getString("FACEBOOK_NAME", "");
		FaceBookBasicInfo.FACEBOOK_EMAIL = pref.getString("FACEBOOK_EMAIL", "");
		FaceBookBasicInfo.FACEBOOK_ID = pref.getString("FACEBOOK_ID", "");
		FaceBookBasicInfo.FACEBOOK_IMAGE_URL = pref.getString("FACEBOOK_IMAGE_URL", "");
	}

	protected void onPause() {
		super.onPause();
		saveProperties();
	}

	protected void onResume() {
		super.onResume();
		loadProperties();

	}
}
