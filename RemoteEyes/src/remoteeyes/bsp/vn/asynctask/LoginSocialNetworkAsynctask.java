package remoteeyes.bsp.vn.asynctask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.spec.EncodedKeySpec;

import org.json.JSONObject;

import remoteeyes.bsp.vn.MainMenuAcitivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.SignInActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginSocialNetworkAsynctask extends
		AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;

	public LoginSocialNetworkAsynctask(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;
		this.dialog = new ProgressDialog(context);
	}

	// run UI before
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}

	// run UI alter
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		UserInfo info = UserInfo.getInstance();
		Log.e("Test", "result: " + result);
		try{
			JSONObject jsonObject = new JSONObject(result);
			info.setId(jsonObject.getString("ID"));
			info.setEmail(jsonObject.getString("email"));
			info.setGender(Integer.parseInt(jsonObject.getString("gender")));
			try{
				info.setBirth_year(Integer.parseInt(jsonObject.getString("birthday").substring(0, 3)));
				info.setBirth_month(Integer.parseInt(jsonObject.getString("birthday").substring(5, 6)));
				info.setBirth_day(Integer.parseInt(jsonObject.getString("birthday").substring(8, 9)));
			}catch(Exception ex){
				info.setBirth_year(0);
				info.setBirth_month(0);
				info.setBirth_day(0);		
			}
			info.setAvatar(jsonObject.getString("avatar"));
			info.setName(jsonObject.getString("name"));
			info.setBalance(Float.parseFloat(jsonObject.getString("balance")));
			
		} catch(Exception ex){
			ex.printStackTrace();
			Toast.makeText(context, "You must register an account!", Toast.LENGTH_SHORT).show();
			return;
		}
				
		if (!info.getId().equalsIgnoreCase("-1")) {
			Toast.makeText(context, info.getId(), Toast.LENGTH_LONG).show();
			Intent intent = new Intent(context, MainMenuAcitivity.class);
			context.startActivity(intent);
			logIn(info.getId().trim(), info.getEmail());
			((SignInActivity)context).finish();
			UserInfo.getInstance().setiSocial(true);
		} else {
			Toast.makeText(context, "Your login attempt was not successful. Please check your email address or password", Toast.LENGTH_SHORT).show();		
		}
	}

	@Override
	protected String doInBackground(String... params) {

		JSONParser jsonParser = new JSONParser();
		String email = params[0].toString().trim();
		String birthday = params[1].toString().trim();
		String gender = params[2].toString().trim();
		String avatar = params[3].toString().trim();
		String name = params[4].toString().trim();
		String type = params[5].toString().trim();
		String networdID = params[6].toString().trim();
		String get_Url = null;
		try {
			get_Url = String.format(Config.API_LOGIN_SOCIAL_NETWORK, URLEncoder.encode(email, "UTF-8"),
					URLEncoder.encode(birthday, "UTF-8"),
					URLEncoder.encode(gender, "UTF-8"),
					URLEncoder.encode(avatar, "UTF-8"), 
					URLEncoder.encode(name, "UTF-8"),
					URLEncoder.encode(type, "UTF-8"),
					URLEncoder.encode(networdID, "UTF-8"));
			Log.e("Test", "URL" + get_Url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = jsonParser.getStringFromUrl(get_Url);

		return result;
	}

	private void logIn(String userID, String email) {
		SharedPreferences pref = (SharedPreferences) context
				.getSharedPreferences(Config.DATA_FIFE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(Config.USERID_KEY, userID);
		editor.putString(Config.EMAIL_KEY, email);
		editor.putString(Config.AVATAR_KEY, UserInfo.getInstance().getAvatar());
		editor.putBoolean(Config.IS_SOCIAL_KEY, true);
		//Config.AVATAR_URL = UserInfo.getInstance().getAvatar();		
		Config.IdUser = userID;
		//Config.EMAILUSER = email;
		editor.commit();
	}
}
