package remoteeyes.bsp.vn.asynctask;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import remoteeyes.bsp.vn.NewAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.SignInActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.common.UploadImageToServer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SignUpAsynctask extends AsyncTask<Void, Void, Void> {

	Context context;
	Activity activity;
	ProgressDialog dialog;

	public SignUpAsynctask(Context context, Activity activity, String path,
			String email, String name, String password, String birthday,
			String gender) {
		this.context = context;
		this.activity = activity;
		this.dialog = new ProgressDialog(context);
		
		NewAccountActivity.info.setAvatar(path);
		NewAccountActivity.info.setEmail(email);
		NewAccountActivity.info.setName(name);
		NewAccountActivity.info.setPassword(password);
		NewAccountActivity.info.setBirthday(birthday);
		NewAccountActivity.info.setGender(gender);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		((NewAccountActivity)context).finish();
	}

	@Override
	protected Void doInBackground(Void... params) {
		File file = new File(NewAccountActivity.info.getAvatar());
		UploadImageToServer upload = new UploadImageToServer("avatar",
				Config.UPLOAD_FILE);
		upload.uploadFile(NewAccountActivity.info.getAvatar(), file.getName());

		if (!upload.urlImage.equals("-1")) {
			JSONObject jsonProfile = new JSONObject();
			try{
				jsonProfile.put("avatar", upload.urlImage);
				jsonProfile.put("birthday", NewAccountActivity.info.getBirthday());
				jsonProfile.put("email", NewAccountActivity.info.getEmail());
				jsonProfile.put("gender", NewAccountActivity.info.getGender());
				jsonProfile.put("name", NewAccountActivity.info.getName());
				jsonProfile.put("pass", NewAccountActivity.info.getPassword());
				
				JSONObject jsonLinked = new JSONObject();
				jsonLinked.put("facebook", NewAccountActivity.info.getFacebookId());
				jsonLinked.put("google", NewAccountActivity.info.getGoogleplusId());
				jsonLinked.put("instargram", NewAccountActivity.info.getInstagramId());
				jsonLinked.put("linked", NewAccountActivity.info.getLinkedInId());
				jsonLinked.put("twitter", NewAccountActivity.info.getTwitterId());
				jsonLinked.put("youtube", NewAccountActivity.info.getYoutubeId());
				
				jsonProfile.put("profile", jsonLinked);
				
			} catch(Exception ex){
				
			}
			
			JSONParser jsonParser = new JSONParser();
			String result = "";
			Log.e("Text", "Register json: " + jsonProfile.toString());
			try {
				String url = Config.API_SIGN_UP + URLEncoder.encode(jsonProfile.toString(), "UTF-8");
				result = jsonParser.getStringFromUrl(url);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			if (!result.trim().equalsIgnoreCase("-1") && !result.isEmpty()) {

				Config.EMAILUSER = NewAccountActivity.info.getEmail();
				Intent intent = new Intent(context, SignInActivity.class);
				context.startActivity(intent);
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.register_successfully),
								Toast.LENGTH_SHORT).show();
					}
				});

			}
			else {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.email_already_exist),
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		} else {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.register_failed),
							Toast.LENGTH_SHORT).show();
				}
			});

		}
		return null;
	}
}
