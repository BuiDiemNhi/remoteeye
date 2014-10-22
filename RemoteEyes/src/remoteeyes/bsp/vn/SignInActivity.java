package remoteeyes.bsp.vn;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthAdapter;

import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.DialogMessage;
import remoteeyes.bsp.vn.asynctask.ForgotPasswordAsynctask;
import remoteeyes.bsp.vn.asynctask.LoginAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.ISConnectInternet;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.ProfileDataListener;
import remoteeyes.bsp.vn.social.network.ResponseListener;
import remoteeyes.bsp.vn.social.network.SocialNetworkSupport;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.SocialNetworkType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends Activity implements OnClickListener {

	Button btnFacebook, btnTwitter, btnGooglePlus, btnInstagram, btnLinkedIn,
			btnYoutube;
	ImageView ivScrollLeft, ivScrollRight;
	EditText etEmail, etPassword;
	TextView tvForgot;
	Button btnSignIn, btnSignUp;
	SocialAuthAdapter adapter;
	SocialNetworkSupport socialNetworkSupport;
	Profile myProfile;
	HorizontalScrollView hvSocialLogin;
	Context context;
	Handler handler;
	public static boolean isLoginingSocial = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.context = this;
		handler = new Handler();
		setContentView(R.layout.layout_sign_in);
		Config.screenWidth = ResizeUtils.getSizeDevice(this).x;
		Config.screenHeight = ResizeUtils.getSizeDevice(this).y;
		generateControl();
		adjustControl();
		adapter = new SocialAuthAdapter(new ResponseListener(this, this));
		socialNetworkSupport = new SocialNetworkSupport(this, adapter);
		socialNetworkSupport.setLogoutRunnable(new Runnable() {

			@Override
			public void run() {
				socialNetworkSupport
						.logoutSocialNetwork(Config.SOCIAL_LOGIN_TYPE);
			}
		});

		if (!Config.EMAILUSER.equals(" ")) {
			etEmail.setText(Config.EMAILUSER);
		}
		autoLogin();
	}

	public void autoLogin() {
		SharedPreferences pref = this.getSharedPreferences(Config.DATA_FIFE,
				Context.MODE_PRIVATE);
		Config.IdUser = pref.getString(Config.USERID_KEY, "-1");
		Config.EMAILUSER = pref.getString(Config.EMAIL_KEY, " ");
		if (!Config.IdUser.equals("-1")) {
			if (ISConnectInternet.isConnectedInternet(this)) {
				UserInfo.getInstance().setId(Config.IdUser);
				handler.post(new Runnable() {

					@Override
					public void run() {
						Intent intentArea = new Intent(context,
								MainMenuAcitivity.class);
						context.startActivity(intentArea);
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
				UserInfo.getInstance().setiSocial(false);
			} else {
				ISConnectInternet.showAlertInternet(this);
			}
		}
	}

	public void setProfile(Profile profile) {
		this.myProfile = profile;
	}

	public void loginIntoMyArea() {
		Intent intent = new Intent(this, SplashActivity.class);
		intent.putExtra("profile", myProfile);
		startActivity(intent);
		finish();
	}

	public void getSocialProfile() {
		adapter.getUserProfileAsync(new ProfileDataListener(this));
	}

	private void generateControl() {
		btnFacebook = (Button) findViewById(R.id.btnFacebook);
		btnTwitter = (Button) findViewById(R.id.btnTwitter);
		btnGooglePlus = (Button) findViewById(R.id.btnGoogle);
		btnInstagram = (Button) findViewById(R.id.btnInstagram);
		btnLinkedIn = (Button) findViewById(R.id.btnLinkedin);
		btnYoutube = (Button) findViewById(R.id.btnYoutube);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnSignIn = (Button) findViewById(R.id.btnSignin);
		btnSignUp = (Button) findViewById(R.id.btnSignup);
		ivScrollLeft = (ImageView) findViewById(R.id.iv_scroll_left);
		ivScrollRight = (ImageView) findViewById(R.id.iv_scroll_right);
		hvSocialLogin = (HorizontalScrollView) findViewById(R.id.hv_button_login);
		tvForgot = (TextView) findViewById(R.id.tvForgot);

		btnSignIn.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);
		btnTwitter.setOnClickListener(this);
		btnGooglePlus.setOnClickListener(this);
		btnInstagram.setOnClickListener(this);
		btnLinkedIn.setOnClickListener(this);
		btnYoutube.setOnClickListener(this);
		ivScrollLeft.setOnClickListener(this);
		ivScrollRight.setOnClickListener(this);
		tvForgot.setOnClickListener(this);

		btnTwitter.setVisibility(View.GONE);
		btnGooglePlus.setVisibility(View.GONE);
		btnInstagram.setVisibility(View.GONE);
		btnLinkedIn.setVisibility(View.GONE);
		btnYoutube.setVisibility(View.GONE);
		ivScrollLeft.setVisibility(View.GONE);
		ivScrollRight.setVisibility(View.GONE);
	}

	private void adjustControl() {
		ResizeUtils.resizeButton(btnFacebook, (int) (Config.screenWidth * 0.9),
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.17), 0,
				RelativeLayout.ALIGN_PARENT_LEFT);

		ResizeUtils.resizeButton(btnTwitter, (int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12), 0,
				(int) (Config.screenWidth * 0.02), 0, 0);
		ResizeUtils.resizeButton(btnGooglePlus,
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12), 0,
				(int) (Config.screenWidth * 0.02), 0, 0);
		ResizeUtils.resizeButton(btnInstagram,
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12), 0,
				(int) (Config.screenWidth * 0.02), 0, 0);
		ResizeUtils.resizeButton(btnLinkedIn,
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12), 0,
				(int) (Config.screenWidth * 0.02), 0, 0);
		ResizeUtils.resizeButton(btnYoutube, (int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12), 0, 0, 0, 0);
		
		ResizeUtils.resizeEditText(etEmail, (int) (Config.screenWidth * 0.9),
				(int) (Config.screenWidth * 0.12), 0, 0, 0, 0);
		ResizeUtils.resizeEditText(etPassword, (int) (Config.screenWidth * 0.9),
				(int) (Config.screenWidth * 0.12), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);
		
		ResizeUtils.resizeButton(
				btnSignIn,
				(int) (Config.screenWidth * 0.3),
				(int) (Config.screenWidth * 0.3)
						* getResources().getDrawable(R.drawable.sign_in)
								.getIntrinsicHeight()
						/ getResources().getDrawable(R.drawable.sign_in)
								.getIntrinsicWidth(), 0, 0, 0, 0);
		ResizeUtils.resizeButton(
				btnSignUp,
				(int) (Config.screenWidth * 0.3),
				(int) (Config.screenWidth * 0.3)
						* getResources().getDrawable(R.drawable.sign_up)
								.getIntrinsicHeight()
						/ getResources().getDrawable(R.drawable.sign_up)
								.getIntrinsicWidth(), 0, 0, 0, 0);
		Drawable arrow_left = getResources().getDrawable(
				R.drawable.gray_left_arrow);
		ResizeUtils.resizeImageView(
				ivScrollLeft,
				(int) ((int) (Config.screenWidth * 0.12)
						* arrow_left.getIntrinsicWidth() / arrow_left
						.getIntrinsicHeight()),
				(int) (Config.screenWidth * 0.12), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);

		Drawable arrow_right = getResources().getDrawable(
				R.drawable.gray_right_arrow);
		ResizeUtils.resizeImageView(
				ivScrollRight,
				(int) ((int) (Config.screenWidth * 0.12)
						* arrow_right.getIntrinsicWidth() / arrow_right
						.getIntrinsicHeight()),
				(int) (Config.screenWidth * 0.12), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = getWidthSocialScroll();
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		hvSocialLogin.setLayoutParams(params);
	}

	private int getWidthSocialScroll() {
		double width = 0;
		double totalWidth = 0;
		double maxWidth = Config.screenWidth - Config.screenWidth * 0.24;
		while (totalWidth < maxWidth) {
			width += Config.screenWidth * 0.12;
			totalWidth = width + Config.screenWidth * 0.12;
		}
		return (int) (width - Config.screenWidth * 0.035);
	}

	private boolean checkEditText() {
		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

		if (TextUtils.isEmpty(etEmail.getText().toString())) {
			etEmail.requestFocus();
			Toast.makeText(this,
					getResources().getString(R.string.email_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(etPassword.getText().toString())) {

			etPassword.requestFocus();
			Toast.makeText(this,
					getResources().getString(R.string.password_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!etEmail.getText().toString().matches(emailPattern)) {

			etPassword.requestFocus();
			Toast.makeText(this,
					getResources().getString(R.string.email_not_formatted),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSignin:
			if (ISConnectInternet.isConnectedInternet(this)) {
				if (checkEditText()) {
					new LoginAsynctask(this, this).execute(etEmail.getText()
							.toString(), etPassword.getText().toString(), "0");
				}

			} else {
				ISConnectInternet.showAlertInternet(this);
			}
			break;
		case R.id.btnSignup:
			Intent i = new Intent(SignInActivity.this, NewAccountActivity.class);
			startActivity(i);

			break;
		case R.id.btnFacebook:
			if (!isLoginingSocial) {
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.FACEBOOK);
				isLoginingSocial = true;
				SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.FACEBOOK;
				SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_FACEBOOK;
				Config.SOCIAL_LOGIN_TYPE = SocialNetworkType.FACEBOOK;
			}
			break;
		case R.id.btnTwitter:
			if (!isLoginingSocial) {
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.TWITTER);
				isLoginingSocial = true;
				SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.TWITTER;
				SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_TWITTER;
				Config.SOCIAL_LOGIN_TYPE = SocialNetworkType.TWITTER;
			}
			break;
		case R.id.btnGoogle:
			if (!isLoginingSocial) {
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.GOOGLEPLUS);
				isLoginingSocial = true;
				SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.GOOGLEPLUS;
				SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_GOOGLEPLUS;
				Config.SOCIAL_LOGIN_TYPE = SocialNetworkType.GOOGLEPLUS;
			}
			break;
		case R.id.btnYoutube:
			if (!isLoginingSocial) {
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.YOUTUBE);
				isLoginingSocial = true;
				SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.YOUTUBE;
				SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_YOUTUBE;
				Config.SOCIAL_LOGIN_TYPE = SocialNetworkType.YOUTUBE;
			}
			break;
		case R.id.btnInstagram:
			if (!isLoginingSocial) {
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.INSTARGRAM);
				isLoginingSocial = true;
				SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.INSTARGRAM;
				SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_INSTARGRAM;
				Config.SOCIAL_LOGIN_TYPE = SocialNetworkType.INSTARGRAM;
			}
			break;
		case R.id.btnLinkedin:
			if (!isLoginingSocial) {
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.LINKEDIN);
				isLoginingSocial = true;
				SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.LINKEDIN;
				SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_LINKEDIN;
				Config.SOCIAL_LOGIN_TYPE = SocialNetworkType.LINKEDIN;
			}
			break;
		case R.id.iv_scroll_left:
			hvSocialLogin.scrollBy(-(int) (Config.screenWidth * 0.14), 0);
			break;
		case R.id.iv_scroll_right:
			hvSocialLogin.smoothScrollBy((int) (Config.screenWidth * 0.14), 0);
			break;
		case R.id.tvForgot:
			if (etEmail.getText().toString().equals("")) {
				Toast.makeText(context,
						getResources().getString(R.string.email_not_empty),
						Toast.LENGTH_SHORT).show();
				return;
			}
			final DialogMessage confirmDialog = (DialogMessage) DialogFactory
					.getDialog(this, DialogType.DIALOG_MESSAGE);
			confirmDialog.setTitle(getResources().getString(R.string.confirm));
			confirmDialog.setContent(getResources().getString(
					R.string.content_forgot_dialog));
			confirmDialog.setOkClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					new ForgotPasswordAsynctask(context).execute(etEmail
							.getText().toString());
					confirmDialog.dismiss();
				}
			});
			confirmDialog.setCancelClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					confirmDialog.dismiss();
				}
			});
			confirmDialog.show();
			break;
		default:
			break;
		}
	}
}
