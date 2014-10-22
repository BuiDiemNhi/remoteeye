package remoteeyes.bsp.vn.asynctask;

import java.io.File;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.common.UploadImageToServer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

public class AddPhotoImageAsynctacks extends AsyncTask<String, Void, String> {
	Activity activity;
	String typeFile;
	Bitmap bm;

	public AddPhotoImageAsynctacks(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String path = params[0];
		String ext = path.substring(path.lastIndexOf("."));
		UploadImageToServer upload = null;
		File file = new File(path);
		int msec = 0;
		if (ext.equals(".jpg") || ext.equals(".png")) {
			typeFile = "1";
			upload = new UploadImageToServer("image",
					Config.API_UPLOAD_IMAGE_CHALLENG);

			upload.uploadFile(path, file.getName());
			String result = "";
			if (!upload.urlImage.equals("-1")) {
				JSONParser jsonParser = new JSONParser();
				String pathth = upload.urlImage;
				String thumb = "";

				String duration = String.valueOf(msec);
				String url = String.format(Config.API_UPLOAD_CHALLENG, pathth,
						Config.IdUser, params[1], typeFile, thumb, duration);
				result = jsonParser.getStringFromUrl(url);
			}

			return result;
		} else {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(
							activity,
							activity.getResources().getString(
									R.string.file_not_support),
							Toast.LENGTH_SHORT).show();
					return;
				}
			});
		}
		return null;
	}

}
