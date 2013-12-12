package com.yarn.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

public class LoginActivity extends Activity {
	private Button mFacebookLoginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mFacebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
		mFacebookLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
//				Facebook mFacebook = new Facebook(appId);
				connectFacebook();
			}
		});
	}
	
	public void connectFacebook() {
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {

				if (session.isOpened()) {
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										Log.e("facebook", user.getFirstName());
										Log.e("facebook", user.getFirstName());
										Log.e("facebook", user.getFirstName());
										Log.e("facebook", user.getFirstName());
										Log.e("facebook", user.getId());
										Log.e("facebook", user.getLastName());
										Log.e("facebook", user.getUsername());
									}
								}
							});
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		connectFacebook();
	}
}
