package com.yarn.facebook;

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

public class LoginActivityUseFacebookInstance extends Activity {
	private Button mFacebookLoginButton;
	private Button mHttptestButton;
	private Facebook mFacebook;
	private ParameterMap mParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mFacebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
		mFacebookLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				try {
					if(UserInfo.USER_TOKEN == null){
						parseJson(signUpFacebook());
					}else{
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("LoginActivityUseFacebookInstance/onCreate", "JSON error");

				}
			}
		});
		mHttptestButton = (Button) findViewById(R.id.http_test_button);
		mHttptestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
	}

	private String signUpFacebook(){
		connectFacebookUseFacebookClass();
		mParams = new ParameterMap();
		mParams.add("app_token", "27dea54c-a635-439c-9916-4a5ba54247af");
		mParams.add("name ", FaceBookBasicInfo.FACEBOOK_NAME);
		mParams.add("offset_time", "0");
		mParams.add("email", FaceBookBasicInfo.FACEBOOK_EMAIL);
		mParams.add("password", "0");
		HttpPostAsyncTask test = new HttpPostAsyncTask(LoginActivityUseFacebookInstance.this, "http://dev.yarnthis.com/1/", "user/signup/", mParams);
		test.execute();
		return test.getResultString();
	}
	
	@SuppressWarnings("deprecation")
	public void connectFacebookUseFacebookClass() {
		try {
			mFacebook = new Facebook(FaceBookBasicInfo.FACEBOOK_APP_ID);
			FaceBookBasicInfo.FacebookInstance = mFacebook;
			if (!FaceBookBasicInfo.RetryLogin
					&& FaceBookBasicInfo.FacebookLogin == true) {
				mFacebook.setAccessToken(FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);

			} else {
				mFacebook.authorize(LoginActivityUseFacebookInstance.this,
						FaceBookBasicInfo.FACEBOOK_PERMISSIONS,
						new AuthorizationListener());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class AuthorizationListener implements DialogListener {

		public void onComplete(Bundle values) {
			Thread t = new Thread() {
				public void run() {
					try {
						String resultStr = FaceBookBasicInfo.FacebookInstance.request("me");
						JSONObject obj = new JSONObject(resultStr);
						FaceBookBasicInfo.FACEBOOK_NAME = obj.getString("name");
						FaceBookBasicInfo.FACEBOOK_EMAIL = obj.getString("email");
						FaceBookBasicInfo.FacebookLogin = true;
						FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN = FaceBookBasicInfo.FacebookInstance.getAccessToken();
					} catch (Exception e) {
						Log.e("AuthorizationListener", "JSON error");
					}
				}
			};
			t.start();
		}

		@Override
		public void onFacebookError(FacebookError e) {}

		@Override
		public void onError(DialogError e) {}

		@Override
		public void onCancel() {}
	}
	
	private void parseJson(String inputString) throws JSONException {
		JSONObject jsonObj = new JSONObject(inputString);
		String id = jsonObj.getString("id");
		String status = jsonObj.getString("status");
		String togken = jsonObj.getString("togken");
		String user_id = jsonObj.getString("user_id");
	}
	
	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode,
			Intent resultIntent) {
		super.onActivityResult(requestCode, resultCode, resultIntent);
		if (resultCode == RESULT_OK) {
			if (requestCode == 32665) {
				FaceBookBasicInfo.FacebookInstance.authorizeCallback(
						requestCode, resultCode, resultIntent);
			}
		}
	}

	private void saveProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean("FacebookLogin", FaceBookBasicInfo.FacebookLogin);
		editor.putString("FACEBOOK_ACCESS_TOKEN",
				FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);
		editor.putString("FACEBOOK_NAME", FaceBookBasicInfo.FACEBOOK_NAME);
		editor.putString("FACEBOOK_EMAIL", FaceBookBasicInfo.FACEBOOK_EMAIL);

		editor.commit();
	}

	private void loadProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);

		FaceBookBasicInfo.FacebookLogin = pref.getBoolean("FacebookLogin",
				false);
		FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN = pref.getString(
				"FACEBOOK_ACCESS_TOKEN", "");
		FaceBookBasicInfo.FACEBOOK_NAME = pref.getString("FACEBOOK_NAME", "");
		FaceBookBasicInfo.FACEBOOK_EMAIL = pref.getString("FACEBOOK_EMAIL", "");

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
