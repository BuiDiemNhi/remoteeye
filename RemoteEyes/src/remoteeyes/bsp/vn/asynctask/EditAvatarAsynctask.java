package remoteeyes.bsp.vn.asynctask;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MainMenuAcitivity;
import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.common.UploadImageToServer;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class EditAvatarAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	ProgressDialog dialog;
	String path;
	String imageshow;
	String idUser = "";

	public EditAvatarAsynctask(Activity activity, String path, String idUser) {
		this.activity = activity;
		this.path = path;
		this.idUser = idUser;
		dialog = new ProgressDialog(activity);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {
			dialog.setMessage("Loading...");
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		// path = params[0];
		File file = new File(path);
		UploadImageToServer upload = new UploadImageToServer("avatar",
				Config.UPLOAD_FILE);
		upload.uploadFile(path, file.getName());
		if (!upload.urlImage.equals("-1") && !upload.urlImage.equals("")) {
			ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
			arrNameValuePairs.add(new BasicNameValuePair("user_id", idUser));
			arrNameValuePairs.add(new BasicNameValuePair("avatar",
					upload.urlImage));
			JSONParser jsonParser = new JSONParser();
			imageshow = Config.SERVER_UPLOAD_IMAGE + upload.urlImage;
			result = jsonParser.makeHttpRequest(Config.API_EDIT_AVATAR,
					arrNameValuePairs);

		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("status") == 200) {
				((MyAccountActivity) activity).setBitmapImage1(path,imageshow);
				Toast.makeText(activity, "Edit your avatar successful",
						Toast.LENGTH_SHORT).show();
				((MyAccountActivity) activity).path = "";
				//
				// if (Config.flag_change_avatar == true) {
				// Config.flag_change_avatar = false;
				// ((MainMenuAcitivity) activity).loadAvatar();
				// }
			} else {
				// if (Config.flag_change_avatar == true) {
				// Config.flag_change_avatar = false;
				// }
				Toast.makeText(activity,
						"Can't edit your avatar. Please try again",
						Toast.LENGTH_SHORT).show();
				((MyAccountActivity) activity).path = "";
			}
		} catch (Exception ex) {
			((MyAccountActivity) activity).path = "";
			Toast.makeText(activity,
					"Can't edit your avatar. Please try again",
					Toast.LENGTH_SHORT).show();
		}
	}

}
