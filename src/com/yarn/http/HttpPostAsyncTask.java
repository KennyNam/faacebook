package com.yarn.http;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.yarn.facebook.FaceBookBasicInfo;

public class HttpPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private String mURL;
	private String mPath;
	private ParameterMap mParams;
	protected String mResult = null;
	
	public HttpPostAsyncTask(Context context, String URL, String path, ParameterMap params){
		this.mContext = context;
		this.mURL = URL;
		this.mPath = path;
		this.mParams = params;
	}
	public String getResult(){
		return mResult;
	}
	@Override 
	protected void onPreExecute() {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Interminate");
		mProgressDialog.setMessage("looooooading !! ");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected Boolean doInBackground(Void... param) {
		try {
			BasicHttpClient httpClient = new BasicHttpClient(mURL);
	        httpClient.setConnectionTimeout(3000);
	        HttpResponse httpResponse = httpClient.post(mPath, mParams);
	        mResult = httpResponse.getBodyAsString();
	        Log.e("testing" ,mResult);
	        return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		if(result == false) Log.e("error", "Http Error Occur");
 		super.onPostExecute(null);
	}
}
