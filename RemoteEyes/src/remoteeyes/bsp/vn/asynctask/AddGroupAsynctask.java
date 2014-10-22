package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.json.JSONObject;

import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.Group;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AddGroupAsynctask extends AsyncTask<String, Void, String> {

	Context context;

	public AddGroupAsynctask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		String result = jsonParser.getStringFromUrl(String.format(
				Config.API_ADD_GROUP, params[0], params[1]));
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("0")){
			Toast.makeText(context, context.getResources().getString(R.string.already_group), Toast.LENGTH_SHORT).show();
		}
		else if(result.equals("-1")){
			Toast.makeText(context, context.getResources().getString(R.string.add_group_failed), Toast.LENGTH_SHORT).show();
		} else {
			try{
				JSONObject jsonObject = new JSONObject(result);
				Group group = new Group();
				group.setFriendOfGroupList(new ArrayList<Friend>());
				group.setName(jsonObject.getString("name"));
				group.setId(jsonObject.getString("ID"));
				((MyGroupActivity)context).addGroup(group);
				((MyGroupActivity)context).showMyGroups();
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
