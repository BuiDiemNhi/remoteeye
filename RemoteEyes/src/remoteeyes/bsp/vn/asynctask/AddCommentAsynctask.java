package remoteeyes.bsp.vn.asynctask;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.content.Context;
import android.os.AsyncTask;

public class AddCommentAsynctask extends AsyncTask<String, Void, String> {

	Context context;
	public AddCommentAsynctask(Context context) {
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
				Config.API_ADD_COMMENT, params[0], params[1], params[2], params[3]));
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(!result.equals("-1")){
			
		}
	}
}
