package remoteeyes.bsp.vn.asynctask;

import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.MainMenuAcitivity;
import remoteeyes.bsp.vn.SplashActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class LoginAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	Activity activity;
	Handler handler;

	public LoginAsynctask(Context context, Activity activity) {
		// TODO Auto-generated constructor stub

		this.context = context;
		this.activity = activity;
		handler = new Handler();
	}

	// run UI before
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	// run UI alter
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		UserInfo info = UserInfo.getInstance();
		try{
			JSONObject jsonObject = new JSONObject(result);
			info.setId(jsonObject.getString("ID"));
			info.setEmail(jsonObject.getString("email"));
			info.setGender(Integer.parseInt(jsonObject.getString("gender")));
			info.setBirth_year(Integer.parseInt(jsonObject.getString("birthday").substring(0, 3)));
			info.setBirth_month(Integer.parseInt(jsonObject.getString("birthday").substring(5, 6)));
			info.setBirth_day(Integer.parseInt(jsonObject.getString("birthday").substring(8, 9)));
			info.setAvatar(jsonObject.getString("avatar"));
			info.setName(jsonObject.getString("name"));
			info.setBalance(Float.parseFloat(jsonObject.getString("balance")));
			
		} catch(Exception ex){
			ex.printStackTrace();
			info = null;
			Toast.makeText(context, "Your login attempt was not successful. Please check your email address or password", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!info.getId().equalsIgnoreCase("-1")) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Intent intentArea = new Intent(context,
							MainMenuAcitivity.class);
					context.startActivity(intentArea);
					activity.overridePendingTransition(0,0);
				}
			});
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent intentSplash = new Intent(context, SplashActivity.class);
					context.startActivity(intentSplash);
				}
			}, 100);
			
			logIn(info.getId().trim(), info.getEmail());			
			UserInfo.getInstance().setiSocial(false);
			activity.finish();
		} else {
			Toast.makeText(context, "Your login attempt was not successful. Please check your email address or password", Toast.LENGTH_SHORT).show();			
		}
	}

	@Override
	protected String doInBackground(String... params) {

		JSONParser jsonParser = new JSONParser();
		String email = params[0].toString().trim();
		String pass = params[1].toString().trim();
		int isSocial = Integer.parseInt(params[2].toString().trim());
		String get_Url = String.format(Config.API_LOGIN, email, pass, isSocial);
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
		editor.putBoolean(Config.IS_SOCIAL_KEY, false);
		Config.IdUser = userID;
		Config.EMAILUSER = email;
		editor.commit();
	}
}
