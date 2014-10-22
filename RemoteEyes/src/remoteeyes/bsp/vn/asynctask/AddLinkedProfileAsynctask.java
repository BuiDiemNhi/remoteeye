package remoteeyes.bsp.vn.asynctask;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AddLinkedProfileAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	ProgressDialog dialog;
	String id;

	public AddLinkedProfileAsynctask(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		JSONParser jsonParser = new JSONParser();
		
		String url = String.format(
				Config.API_ADD_LINKED_PROFILE, params[0], params[1], params[2], params[3]);
		String result = jsonParser.getStringFromUrl(url);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		try{
			JSONObject json = new JSONObject(result);
			if (json.getString("status").equals("200")) {
				try{
					id = json.getString("data");
					((MyAccountActivity) context).addLinkedProfileToUserInfo(id);
					((MyAccountActivity) context).refreshLinkedSocialList();
				} catch(Exception ex){}
			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.toast_add_linked_social_failed),
						Toast.LENGTH_SHORT).show();
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
