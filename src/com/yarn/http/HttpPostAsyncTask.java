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

public class HttpPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private ParameterMap mParams;
	private String mURL;
	private String mPath;
	private String mResult = null;
	
	public HttpPostAsyncTask(Context context, String URL, String path, ParameterMap params){
		this.mContext = context;
		this.mURL = URL;
		this.mParams = params;
		this.mPath = path;
	}
	
	public String getResultString() {
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
		BasicHttpClient httpClient = new BasicHttpClient(mURL);
        httpClient.addHeader("name", "value");
        httpClient.setConnectionTimeout(3000);
        HttpResponse httpResponse = httpClient.get(mPath, mParams);
        mResult = httpResponse.toString();
        Log.e("testing" ,httpResponse.toString());
        return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result == true) mProgressDialog.dismiss();
		super.onPostExecute(null);
	}

}
