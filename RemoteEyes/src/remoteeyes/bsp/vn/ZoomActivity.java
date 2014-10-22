package remoteeyes.bsp.vn;

import remoteeyes.bsp.vn.common.TouchImageView;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ZoomActivity extends Activity {

	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("url");
		}
		TouchImageView img = new TouchImageView(this);
		try{
		Picasso.with(this).load(url).placeholder(R.drawable.none)
				.error(R.drawable.ic_launcher).into(img);
		}catch(Exception e){}
		setContentView(img);
	}

}

