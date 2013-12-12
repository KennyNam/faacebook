package com.yarn.facebook;

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

public class LoginActivityUseFacebookInstance extends Activity {
	private Button mFacebookLoginButton;
	private Facebook mFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mFacebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
		mFacebookLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				connectFacebookUseFacebookClass();
			}
		});
		
		
		
	}

	@SuppressWarnings("deprecation")
	public void connectFacebookUseFacebookClass() {
		try {
			mFacebook = new Facebook(FaceBookBasicInfo.FACEBOOK_APP_ID);
			FaceBookBasicInfo.FacebookInstance = mFacebook;
			if (!FaceBookBasicInfo.RetryLogin
					&& FaceBookBasicInfo.FacebookLogin == true) {
				Log.e("facebooktest", "1");
				mFacebook
						.setAccessToken(FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);

			} else {
				Log.e("facebooktest", "2");
				mFacebook.authorize(LoginActivityUseFacebookInstance.this,
						FaceBookBasicInfo.FACEBOOK_PERMISSIONS,
						new AuthorizationListener());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class AuthorizationListener implements DialogListener {

		public void onCancel() {

		}

		public void onComplete(Bundle values) {
			Thread t = new Thread() {
				public void run() {
					String resultStr;
					try {
						resultStr = FaceBookBasicInfo.FacebookInstance
								.request("me");
						JSONObject obj = new JSONObject(resultStr);
						FaceBookBasicInfo.FACEBOOK_NAME = obj.getString("name");
						FaceBookBasicInfo.FacebookLogin = true;
						FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN = FaceBookBasicInfo.FacebookInstance
								.getAccessToken();
						Log.e("FACEBOOK_ACCESS_TOKEN",
								FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN);
						Log.e("FACEBOOK_NAME", FaceBookBasicInfo.FACEBOOK_NAME);
						Log.e("FACEBOOK_NAME", obj.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
		}

		@Override
		public void onFacebookError(FacebookError e) {

		}

		@Override
		public void onError(DialogError e) {

		}

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

		editor.commit();
	}

	private void loadProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);

		FaceBookBasicInfo.FacebookLogin = pref.getBoolean("FacebookLogin",
				false);
		FaceBookBasicInfo.FACEBOOK_ACCESS_TOKEN = pref.getString(
				"FACEBOOK_ACCESS_TOKEN", "");
		FaceBookBasicInfo.FACEBOOK_NAME = pref.getString("FACEBOOK_NAME", "");

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
