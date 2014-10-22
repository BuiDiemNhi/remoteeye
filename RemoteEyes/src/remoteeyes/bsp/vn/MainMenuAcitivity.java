package remoteeyes.bsp.vn;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import vn.amobi.util.ads.AdEventInterface;
import vn.amobi.util.ads.AmobiAdView;
import vn.amobi.util.ads.AmobiAdView.WidgetSize;
import vn.amobi.util.ads.notifications.AmobiPushAd;
import remoteeyes.bsp.vn.asynctask.ConvertAvatarURLAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadProfileAsynctask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.ISConnectInternet;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import remoteeyes.bsp.vn.uilt.ResizeUtils;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuAcitivity extends Activity implements OnClickListener,
		AdEventInterface {

	Context context;
	LinearLayout ll_my_area, ll_accepted, ll_tell_a_friend, ll_global,
			ll_my_posted, ll_my_account, ll_ads;
	ImageView iv_my_area, iv_my_accepted, iv_tell_a_friend, iv_global,
			iv_my_posted, iv_my_account, ivAvatar, ivHeader;
	TextView tvChallengeNearby, tvChallengesNearBy, tvTellAFriend, tvGlobal,
			tvPosted, tvThereAreNoChallenge, tvCountReward, tvCountChallenge,
			tvMyArea, tvMyAccepted, tvMyAccount;
	Typeface typeface;
	Handler handler;
	private AmobiAdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.context = this;
		setContentView(R.layout.activity_signin_to_main_menu);

		ll_ads = (LinearLayout) findViewById(R.id.ll_ads);
		adView = (AmobiAdView) findViewById(R.id.main_menu_adView);
		if (adView != null) {
			adView.setEventListener(this);
			adView.loadAd(WidgetSize.SMALL);
			adView.setHideAfterClick(false);
			adView.scheduleRefresh(600);
		}
		AmobiPushAd pushAd = new AmobiPushAd(this);
		pushAd.sendRequest();
		handler= new Handler();
		ll_my_posted = (LinearLayout) findViewById(R.id.ll_my_posted);
		ll_my_account = (LinearLayout) findViewById(R.id.ll_my_account);
		tvMyAccount = (TextView) findViewById(R.id.tv_my_account);
		tvPosted = (TextView) findViewById(R.id.tv_my_posted);
		ll_tell_a_friend = (LinearLayout) findViewById(R.id.ll_tell_a_friend);
		tvTellAFriend = (TextView) findViewById(R.id.tv_tell_a_friend);
		ll_global = (LinearLayout) findViewById(R.id.ll_global);
		tvGlobal = (TextView) findViewById(R.id.tv_global);
		tvMyAccepted = (TextView) findViewById(R.id.tv_my_accepted);
		ll_my_area = (LinearLayout) findViewById(R.id.ll_my_area);
		tvCountReward = (TextView) findViewById(R.id.tv_count_reward);
		tvCountChallenge = (TextView) findViewById(R.id.tv_count_challenge);
		tvChallengeNearby = (TextView) findViewById(R.id.tv_challenge_nearby);
		iv_my_posted = (ImageView) findViewById(R.id.imBtn_my_posted);
		iv_my_account = (ImageView) findViewById(R.id.imBtn_my_acount);
		iv_global = (ImageView) findViewById(R.id.imBtn_global);
		iv_tell_a_friend = (ImageView) findViewById(R.id.imBtn_tell_a_friend);
		iv_my_area = (ImageView) findViewById(R.id.imBtn_my_Area);
		iv_my_accepted = (ImageView) findViewById(R.id.imBtn_my_accepted);
		ll_accepted = (LinearLayout) findViewById(R.id.ll_accepted);
		tvMyArea = (TextView) findViewById(R.id.tvMy_Area);
		ivAvatar = (ImageView) findViewById(R.id.ImvAvata_main_menu);
		ivHeader = (ImageView) findViewById(R.id.Imv_header_main_menu);
		tvChallengesNearBy = (TextView) findViewById(R.id.tv_challenges_nearby);
		tvThereAreNoChallenge = (TextView) findViewById(R.id.tv_there_are_no_challenge);

		ll_global.setOnClickListener(this);
		ll_my_account.setOnClickListener(this);
		ll_my_posted.setOnClickListener(this);
		ll_tell_a_friend.setOnClickListener(this);
		ll_my_area.setOnClickListener(this);
		ll_accepted.setOnClickListener(this);

		adjustUserInterface();
		new LoadProfileAsynctask(context, this).execute(UserInfo.getInstance()
				.getId());
		setFontTextView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.ll_my_area:
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_NEARBY;
			Config.data.clear();
			Intent intentArea = new Intent(this, MyAreaActivity.class);
			intentArea.putExtra("run", "MyArea");
			context.startActivity(intentArea);
			overridePendingTransition(0, 0);
			break;

		case R.id.ll_my_posted:
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_POSTED;
			Config.data.clear();
			Intent Posted = new Intent(this, MyAreaActivity.class);
			Posted.putExtra("run", "Posted");
			context.startActivity(Posted);
			overridePendingTransition(0, 0);
			break;

		case R.id.ll_accepted:
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED;
			Config.data.clear();
			Intent Accepted = new Intent(this, MyAreaActivity.class);
			Accepted.putExtra("run", "Accepted");
			context.startActivity(Accepted);
			overridePendingTransition(0, 0);
			break;

		case R.id.ll_global:
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_GLOBAL;
			Config.data.clear();
			Intent Global = new Intent(this, MyAreaActivity.class);
			Global.putExtra("run", "Global");
			context.startActivity(Global);
			overridePendingTransition(0, 0);
			break;

		case R.id.ll_tell_a_friend:
			handler.post(new Runnable() {

				@Override
				public void run() {
					Intent intenttellafriend = new Intent(context,
							TellAFriendActivity.class);
					context.startActivity(intenttellafriend);
					overridePendingTransition(0, 0);
				}
			});
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intentSplash = new Intent(context,
							SplashActivity.class);
					context.startActivity(intentSplash);
				}
			}, 200);
		
			break;

		case R.id.ll_my_account:
			handler.post(new Runnable() {

				@Override
				public void run() {
					Intent intentmyacc = new Intent(context,
							MyAccountActivity.class);
					context.startActivity(intentmyacc);
					overridePendingTransition(0, 0);
				}
			});
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intentSplash = new Intent(context,
							SplashActivity.class);
					context.startActivity(intentSplash);
				}
			}, 200);
			this.finish();
			break;
		default:
			break;
		}

	}

	private void setFontTextView() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		// tvLayoutTitle.setTypeface(typeface);
	}

	public void adjustUserInterface() {
		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		int heightDevice = ResizeUtils.getSizeDevice(this).y;

		ResizeUtils.resizeImageView(ivAvatar, (int) (heightDevice * 0.15),
				(int) (heightDevice * 0.15), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), (int) (heightDevice * 0.01),
				(int) (heightDevice * 0.01));

		ResizeUtils.resizeImageView(ivHeader, (int) (widthDevice * 0.7),
				(int) (heightDevice * 0.1), 0, (int) (widthDevice * 0.12),
				(int) (widthDevice * 0.04), 0);

		ResizeUtils.resizeLinearLayout(ll_my_area, (int) (widthDevice * 0.48),
				(int) (heightDevice * 0.28), (int) (widthDevice * 0.015), 0, 0,
				(int) (widthDevice * 0.01));
		ResizeUtils.resizeLinearLayout(ll_global, (int) (widthDevice * 0.48),
				(int) (heightDevice * 0.28), 0, (int) (widthDevice * 0.015), 0,
				(int) (widthDevice * 0.01));
		ResizeUtils.resizeLinearLayout(ll_my_posted,
				(int) (widthDevice * 0.48), (int) (heightDevice * 0.24), 0,
				(int) (widthDevice * 0.015), 0, (int) (widthDevice * 0.01));
		ResizeUtils.resizeLinearLayout(ll_accepted, (int) (widthDevice * 0.48),
				(int) (heightDevice * 0.24), (int) (widthDevice * 0.015), 0, 0,
				(int) (widthDevice * 0.01));
		ResizeUtils.resizeLinearLayout(ll_tell_a_friend,
				(int) (widthDevice * 0.48), (int) (heightDevice * 0.20),
				(int) (widthDevice * 0.015), 0, 0, 0);
		ResizeUtils.resizeLinearLayout(ll_my_account,
				(int) (widthDevice * 0.48), (int) (heightDevice * 0.20), 0,
				(int) (widthDevice * 0.015), 0, 0);
		ResizeUtils.resizeLinearLayout(ll_ads, (int) (widthDevice),
				(int) (heightDevice * 0.08), 0, 0, (int) (heightDevice * 0.01),
				0);

		ResizeUtils.resizeImageView(iv_my_area, (int) (widthDevice * 0.3),
				(int) (heightDevice * 0.19), (int) (widthDevice * 0.085), 0,
				(int) (heightDevice * 0.02), 0);
		ResizeUtils.resizeImageView(iv_my_accepted, (int) (widthDevice * 0.25),
				(int) (heightDevice * 0.14), (int) (widthDevice * 0.12), 0,
				(int) (heightDevice * 0.02), 0);
		ResizeUtils.resizeImageView(iv_my_account, (int) (widthDevice * 0.17),
				(int) (heightDevice * 0.12), (int) (widthDevice * 0.15), 0,
				(int) (heightDevice * 0.015), 0);
		ResizeUtils.resizeImageView(iv_my_posted, (int) (widthDevice * 0.25),
				(int) (heightDevice * 0.14), (int) (widthDevice * 0.12), 0,
				(int) (heightDevice * 0.02), 0);
		ResizeUtils.resizeImageView(iv_tell_a_friend,
				(int) (widthDevice * 0.17), (int) (heightDevice * 0.12),
				(int) (widthDevice * 0.15), 0, (int) (heightDevice * 0.015), 0);
		ResizeUtils.resizeImageView(iv_global, (int) (widthDevice * 0.3),
				(int) (heightDevice * 0.19), (int) (widthDevice * 0.085), 0,
				(int) (heightDevice * 0.02), 0);

		tvMyArea.setTextSize((float) (widthDevice * 0.040));
		tvMyArea.setGravity(Gravity.CENTER);
		tvGlobal.setTextSize((float) (widthDevice * 0.04));
		tvGlobal.setGravity(Gravity.CENTER);
		tvMyAccepted.setTextSize((float) (widthDevice * 0.035));
		tvMyAccepted.setGravity(Gravity.CENTER);
		tvPosted.setTextSize((float) (widthDevice * 0.035));
		tvPosted.setGravity(Gravity.CENTER);
		tvTellAFriend.setTextSize((float) (widthDevice * 0.033));
		tvTellAFriend.setGravity(Gravity.CENTER);
		tvMyAccount.setTextSize((float) (widthDevice * 0.033));
		tvMyAccount.setGravity(Gravity.CENTER);
	}

	public void loadAvatar() {
		MemoryCache memoryCache = new MemoryCache();
		Bitmap bitmap = memoryCache.get(Config.IdUser);
		if (bitmap != null) {
			ivAvatar.setImageBitmap(bitmap);
		} else {
			Picasso.with(this).load(UserInfo.getInstance().getAvatar())
					.transform(new CircleTransform()).into(ivAvatar);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onAdViewClose() {
		// TODO Auto-generated method stub
		adView.setVisibility(View.VISIBLE);
		if (adView.isShown() == false) {
			adView.setEnabled(true);
			adView.setVisibility(View.VISIBLE);
		} else {
			adView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onAdViewLoaded() {
		// TODO Auto-generated method stub
		/*Toast.makeText(getApplicationContext(), "Ad Loaded Successful",
				Toast.LENGTH_SHORT).show();*/
	}

	@Override
	public void onLoadAdError(ErrorCode arg0) {
		// TODO Auto-generated method stub
		/*Toast.makeText(getApplicationContext(), "Ad Loaded Fail",
				Toast.LENGTH_SHORT).show();*/
	}

}
