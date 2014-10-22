package remoteeyes.bsp.vn.asynctask;

import org.json.JSONObject;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DeleteGroupAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	String groupId;

	public DeleteGroupAsynctask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		String url = Config.API_DELETE_GROUP_BY_USER + "/" + params[0];
		String result = jsonParser.getStringFromUrl(url);
		groupId = params[0];
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			JSONObject jsonObject = new JSONObject(result);
			if(jsonObject.getString("status").equals("200")){
				((MyGroupActivity)context).deleteGroup(groupId);
			}
			else{
				Toast.makeText(context, context.getResources().getString(R.string.delete_group_failed), Toast.LENGTH_SHORT).show();
			}
		}catch(Exception ex){}
	}
}