package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogAddMember;
import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AddMemberAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	Activity activity;

	public AddMemberAsynctask(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {		
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair(params[0], params[1]));
		JSONParser jsonParser = new JSONParser();
		String result = jsonParser.makeHttpRequest(Config.API_ADD_MEMBER,
				arrNameValuePairs);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			JSONObject jsonObject = new JSONObject(result);
			if(jsonObject.getString("status").equals("200")){
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText((MyGroupActivity) context, "Added member successful",
								   Toast.LENGTH_LONG).show();
					}
				});
				new LoadGroupByUserAsynctask(activity).execute(Config.IdUser);
			}
			else
			{
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText((MyGroupActivity) context, "Added member unsuccessful",
								   Toast.LENGTH_LONG).show();
					}
				});
				
			}
		}catch(Exception ex){}
	}
}