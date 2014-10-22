package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DeleteMemberInGroupAsynctask extends AsyncTask<String,Void,String> {

	Context context;
	
	
	public DeleteMemberInGroupAsynctask(Context context) {
		this.context = context;
	}

	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair("group_id", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("user_id", params[1]));
		JSONParser jsonParser = new JSONParser();

		String result = jsonParser.makeHttpRequest(Config.API_DELETE_MEMBER,
				arrNameValuePairs);
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			JSONObject jsonObject = new JSONObject(result);
			if(jsonObject.getString("status").equals("200")){
				Toast.makeText(context, "Deleted member successful", Toast.LENGTH_SHORT).show();
				new LoadGroupByUserAsynctask((MyGroupActivity)context).execute(Config.IdUser);
			}
			else
			{
				Toast.makeText(context, "Deleted member unsuccessful", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception ex){}
	}

}
