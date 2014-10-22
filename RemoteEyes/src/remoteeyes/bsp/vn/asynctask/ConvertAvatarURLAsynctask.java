package remoteeyes.bsp.vn.asynctask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.squareup.picasso.Picasso;

import remoteeyes.bsp.vn.common.CircleTransform;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ConvertAvatarURLAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ImageView ivImg;
	
	public ConvertAvatarURLAsynctask(Context context, ImageView iv) {
		this.ivImg = iv;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		URL url;
	    String newLocation = null;
	    try {
	        url = new URL(params[0]);
	        HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        newLocation = connection.getHeaderField("Location");
	    } catch (MalformedURLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    return newLocation;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Picasso.with(context).load(result)
		.transform(new CircleTransform()).into(ivImg);
	}
}

