package remoteeyes.bsp.vn.asynctask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

public class DownloadImageAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	public DownloadImageAsynctask(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		dialog.setMessage("Loading...");
		dialog.show();
	}
	
	@Override
	protected void onPostExecute(String result){
		super.onPostExecute(result);
		if(dialog.isShowing())
			dialog.dismiss();
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			File dir =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"RemoteEyes");
			if (!dir.exists()) {
				dir.mkdir();
			}
			String fileName = params[0]
					.substring(params[0].lastIndexOf("/"));
            URL url = new URL(params[0]);
            //you can write here any link
            File file = new File(dir,fileName);
            HttpURLConnection ucon = (HttpURLConnection)url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
    } catch (IOException e) {
    }
		return null;
	}

}
