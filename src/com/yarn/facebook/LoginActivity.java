package com.yarn.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.android.Facebook;

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
				connectFacebook()
			}
		});
	}

	private void connectFacebook() {
        try {
            Facebook mFacebook = new Facebook(BasicInfo.FACEBOOK_APP_ID);
            BasicInfo.FacebookInstance = mFacebook;
 
            if (!BasicInfo.RetryLogin && BasicInfo.FacebookLogin == true) {
                mFacebook.setAccessToken(BasicInfo.FACEBOOK_ACCESS_TOKEN);
 
                showUserTimeline();
            } else {
                mFacebook.authorize(this, BasicInfo.FACEBOOK_PERMISSIONS, new AuthorizationListener());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
}
