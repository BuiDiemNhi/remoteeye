package remoteeyes.bsp.vn.asynctask;

import java.net.URLEncoder;

import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class EditGroupByUserAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	String groupId, newName;

	public EditGroupByUserAsynctask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		String result = "";
		try{
			String url = String.format(
					Config.API_UPDATE_GROUP_BY_USER, params[0], URLEncoder.encode(params[1], "UTF-8"));
			result = jsonParser.getStringFromUrl(url);
		} catch(Exception ex){
			ex.printStackTrace();
		}
		groupId = params[0];
		newName = params[1];
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("0")){
			Toast.makeText(context, context.getResources().getString(R.string.already_group), Toast.LENGTH_SHORT).show();
		} else if(result.equals("-1")){
			Toast.makeText(context, context.getResources().getString(R.string.add_group_failed), Toast.LENGTH_SHORT).show();
		} else if(result.equals("1")){
			((MyGroupActivity)context).editGroup(groupId, newName);
		}
	}
}
