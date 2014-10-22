package remoteeyes.bsp.vn;

import remoteeyes.bsp.vn.common.Config;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {

	Handler handler;
	Context context;
	Activity activity;
	ImageView ivLogo;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.layout_splash);
		handler = new Handler();
		context = this;
		activity =this;
		ivLogo = (ImageView)findViewById(R.id.iv_logo);
		ivLogo.setPadding(0, 0, 0, (int)(Config.screenHeight*0.1));
		Drawable dwLogo = getResources().getDrawable(R.drawable.logo);
		ivLogo.getLayoutParams().width = (int)(Config.screenWidth*0.7);
		ivLogo.getLayoutParams().height = (int)(Config.screenWidth*0.7*dwLogo.getIntrinsicHeight()/dwLogo.getIntrinsicWidth());
		ivLogo.setPadding(0, 0, 0, (int)(Config.screenHeight*0.2));
		
		StartAnimations();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				activity.finish();
			}
		}, 6000);		
	}

	private void StartAnimations() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_alpha);
		anim.reset();
		LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
		l.clearAnimation();
		l.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.splash_translate);
		anim.reset();
		ivLogo.clearAnimation();
		ivLogo.startAnimation(anim);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
