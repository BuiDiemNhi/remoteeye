package remoteeyes.bsp.vn;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogBlockedUser;
import remateeyes.bsp.vn.dialog.DialogChangeEmail;
import remateeyes.bsp.vn.dialog.DialogConfirmIgnore;
import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.DialogHistoryPaypal;
import remateeyes.bsp.vn.dialog.DialogPaymentChallenge;
import remateeyes.bsp.vn.dialog.DialogRemoteEmail;
import remoteeyes.bsp.vn.asynctask.AddOtherEmailAsynctask;
import remoteeyes.bsp.vn.asynctask.ChangeEmailProfileAsynctask;
import remoteeyes.bsp.vn.asynctask.ChangeNameAsynctask;
import remoteeyes.bsp.vn.asynctask.ChangePasswordAsynctask;
import remoteeyes.bsp.vn.asynctask.EditAvatarAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterPostedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.GetBugetAsynstask;
import remoteeyes.bsp.vn.asynctask.GetMoneyEarnAsynstask;
import remoteeyes.bsp.vn.asynctask.GetMoneyPaidAsynstask;
import remoteeyes.bsp.vn.asynctask.HistoryPaypalAsynctacks;
import remoteeyes.bsp.vn.asynctask.IgnoreChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.asynctask.LoadProfileAsynctask;
import remoteeyes.bsp.vn.asynctask.PaymentChallengeAsynctacks;
import remoteeyes.bsp.vn.asynctask.RemoveLinkedProfileAsynctask;
import remoteeyes.bsp.vn.asynctask.RemoveOthersEmailAsynctask;
import remoteeyes.bsp.vn.asynctask.SavePaypalHistoryAsynctacks;
import remoteeyes.bsp.vn.asynctask.SendActiveEmailAsynctask;
import remoteeyes.bsp.vn.asynctask.UpdateLinkedProfileAsynctask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.custom.quickaction.ActionItem;
import remoteeyes.bsp.vn.custom.quickaction.QuickAction;
import remoteeyes.bsp.vn.custom.quickaction.QuickAction.OnActionItemClickListener;
import remoteeyes.bsp.vn.model.HistoryPaypalItem;
import remoteeyes.bsp.vn.model.LinkedProfileItem;
import remoteeyes.bsp.vn.model.OtherEmailItem;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.ResponseListener;
import remoteeyes.bsp.vn.social.network.SocialNetworkSupport;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.SocialNetworkType;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.internal.Utility;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.PaymentMethodActivity;
import com.squareup.picasso.Picasso;

public class MyAccountActivity extends Activity implements OnClickListener {
public TextView tvcountPaid,
tvCountPending, tvCountEarn;
	ImageView iv_author_avatar, iv_edit_avatar;
	TextView tv_title, tv_gender, tv_submitted, tv_responded, tv_email_others,
			tv_photo_resolution, tv_video_resolution, tv_linked_profile,
			tv_name, tv_private_infomation, tv_birthdate, tv_email,
			tv_account_blance, tv_charge, tv_withdraw, tv_history,
			tv_email_change, tvTerms2My, tv_phone_contacts, tv_email_paypal,
			tv_email_paypal_change, tvpay, tvpending, tvearn;
	Button btn_logout;
	ImageButton ib_edit_name, ib_edit_blocked_users, ib_phone, ib_facebook,
			ib_twitter, ib_home, ib_edit_password, ib_icon_paypal,
			ib_row_up_account_blance, ib_row_down_linker, ib_row_down_email,
			ib_add_linked, ib_email_add, ib_a_cong_email, ib_paypal, ib_charge,
			ib_withdraw, ib_history;
	EditText et_new_password, et_confirm_password, et_add_email, et_name;

	LinearLayout ll_public_profile, ll_private_infomation, ll_linked_header,
			ll_add_linked_profile_icon_text, ll_linked_profiles,
			ll_account_blance, ll_email_header, ll_add_linked_profile_icon,
			ll_add_email, ll_add_account_blance, ll_others_email,
			ll_other_email_text, ll_other_email_button, ll_pending, ll_earn,
			ll_pay;
	RelativeLayout rl_author_avatar, rl_charge, rl_withdraw, rl_history;
	RatingBar rbMyAccountRating;
	CheckBox cbTerms;
	LinearLayout llTerms;
	static Context context;
	public static Activity activity;
	QuickAction quickAction;
	SocialNetworkSupport socialNetworkSupport;
	SocialAuthAdapter adapter;
	Typeface typeface;
	Uri mImageCaptureUri;
	Bitmap bmAvatar;
	public String path = "";
	public static boolean isEditAvatar = false;
	public String path1 = "";
	public Bitmap bitmapImage2;
	public UserInfo userInfo;
	public static ArrayList<OtherEmailItem> otherEmailItem;
	public static boolean flag_remote_email = false;
	public static boolean flag_refesh_email_fit = false;
	public static boolean flag_load_account_fit = false;
	public static String id_payment = "";
	// paypal
	public static PayPalItem[] ArrItemPaypal;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(Config.CONFIG_ENVIRONMENT)
			.clientId(Config.CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Hipster Store")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	@Override
	public void onBackPressed() {

		// Toast.makeText(getApplicationContext(),
		// "Exit News Post Page Activity",
		// Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(MyAccountActivity.this,
				MainMenuAcitivity.class);
		this.startActivity(intent);
		this.finish();
		// moveTaskToBack(false);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_my_account);
		this.context = this;
		this.activity = this;
		iv_author_avatar = (ImageView) findViewById(R.id.iv_author_avatar);
		rl_author_avatar = (RelativeLayout) findViewById(R.id.rl_author_avatar);
		iv_edit_avatar = (ImageView) findViewById(R.id.iv_edit_avatar);
		et_name = (EditText) findViewById(R.id.et_name);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		tv_submitted = (TextView) findViewById(R.id.tv_submitted);
		tv_responded = (TextView) findViewById(R.id.tv_responded);
		tv_photo_resolution = (TextView) findViewById(R.id.tv_photo_resolution_my);
		tv_video_resolution = (TextView) findViewById(R.id.tv_video_resolution_my);
		tv_linked_profile = (TextView) findViewById(R.id.tv_linked_profile);
		tv_private_infomation = (TextView) findViewById(R.id.tv_private_infomation);
		tv_birthdate = (TextView) findViewById(R.id.tv_birthdate);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_email_paypal = (TextView) findViewById(R.id.tv_email_paypal);
		tv_account_blance = (TextView) findViewById(R.id.tv_account_blance);
		tv_email_change = (TextView) findViewById(R.id.tv_email_change);
		tv_email_paypal_change = (TextView) findViewById(R.id.tv_email_paypal_change);
		tvTerms2My = (TextView) findViewById(R.id.tv_terms2_my);
		tv_phone_contacts = (TextView) findViewById(R.id.tv_phone_contacts);
		tv_email_others = (TextView) findViewById(R.id.tv_email_others);
		tvpay = (TextView) findViewById(R.id.tv_charge);
		tvpending = (TextView) findViewById(R.id.tv_pending);
		tvearn = (TextView) findViewById(R.id.tv_withdraw);
		tvcountPaid = (TextView) findViewById(R.id.tv_count_had_charged);
		tvCountEarn = (TextView) findViewById(R.id.tv_count_had_earnd);
		tvCountPending = (TextView) findViewById(R.id.tv_count_pending);
		ib_edit_name = (ImageButton) findViewById(R.id.ib_edit_name);
		ib_edit_blocked_users = (ImageButton) findViewById(R.id.ib_edit_blocked_users);
		ib_phone = (ImageButton) findViewById(R.id.ib_phone);
		ib_home = (ImageButton) findViewById(R.id.ib_home);
		ib_icon_paypal = (ImageButton) findViewById(R.id.ib_icon_paypal);
		ib_edit_password = (ImageButton) findViewById(R.id.ib_edit_password);
		ib_row_up_account_blance = (ImageButton) findViewById(R.id.ib_row_up_account_blance);
		ib_row_down_email = (ImageButton) findViewById(R.id.ib_row_down_email);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
		et_add_email = (EditText) findViewById(R.id.et_add_email);
		tv_name = (TextView) findViewById(R.id.tv_name);

		ll_public_profile = (LinearLayout) findViewById(R.id.ll_public_profile);
		ll_private_infomation = (LinearLayout) findViewById(R.id.ll_private_infomation);
		ll_linked_header = (LinearLayout) findViewById(R.id.ll_linked_header);
		ll_linked_profiles = (LinearLayout) findViewById(R.id.ll_linked_profiles);
		ll_account_blance = (LinearLayout) findViewById(R.id.ll_account_blance);
		ll_add_account_blance = (LinearLayout) findViewById(R.id.ll_add_account_blance);
		ll_email_header = (LinearLayout) findViewById(R.id.ll_email_header);
		ll_add_email = (LinearLayout) findViewById(R.id.ll_add_email_my);
		ll_others_email = (LinearLayout) findViewById(R.id.ll_others_email);
		ll_other_email_text = (LinearLayout) findViewById(R.id.ll_other_email_text);
		ll_other_email_button = (LinearLayout) findViewById(R.id.ll_other_email_button);
		ll_add_linked_profile_icon = (LinearLayout) findViewById(R.id.ll_add_linked_profile_icon);
		ll_add_linked_profile_icon_text = (LinearLayout) findViewById(R.id.ll_add_linked_profile_icon_text);
		ll_pending = (LinearLayout) findViewById(R.id.ll_pending);
		ll_pay = (LinearLayout) findViewById(R.id.ll_charge);
		ll_earn = (LinearLayout) findViewById(R.id.ll_withdraw);
		ib_row_down_linker = (ImageButton) findViewById(R.id.ib_row_down_linker);
		ib_a_cong_email = (ImageButton) findViewById(R.id.ib_a_cong_email);
		ib_paypal = (ImageButton) findViewById(R.id.ib_paypal_new);
		ib_charge = (ImageButton) findViewById(R.id.ib_charge);
		ib_withdraw = (ImageButton) findViewById(R.id.ib_withdraw);
		ib_history = (ImageButton) findViewById(R.id.ib_history);
		ib_add_linked = (ImageButton) findViewById(R.id.ib_add_linked);
		ib_email_add = (ImageButton) findViewById(R.id.ib_email_add);
		llTerms = (LinearLayout) findViewById(R.id.ll_terms_my);

		tv_charge = (TextView) findViewById(R.id.tv_charge);
		tv_withdraw = (TextView) findViewById(R.id.tv_withdraw);
		tv_history = (TextView) findViewById(R.id.tv_history);
		cbTerms = (CheckBox) findViewById(R.id.cb_terms_my);
		rl_charge = (RelativeLayout) findViewById(R.id.rl_charge);
		rl_withdraw = (RelativeLayout) findViewById(R.id.rl_withdraw);
		rl_history = (RelativeLayout) findViewById(R.id.rl_history);

		rbMyAccountRating = (RatingBar) findViewById(R.id.rb_my_account_rating);

		iv_edit_avatar.setOnClickListener(this);
		ib_row_down_linker.setOnClickListener(this);
		ib_row_down_email.setOnClickListener(this);
		ib_row_up_account_blance.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		tvTerms2My.setOnClickListener(this);
		ib_email_add.setOnClickListener(this);
		ib_edit_name.setOnClickListener(this);
		ib_edit_password.setOnClickListener(this);
		tv_email_change.setOnClickListener(this);
		tv_email_paypal_change.setOnClickListener(this);
		ib_edit_blocked_users.setOnClickListener(this);
		ib_charge.setOnClickListener(this);
		ib_withdraw.setOnClickListener(this);
		ib_history.setOnClickListener(this);

		adjustUserInterface();
		new GetBugetAsynstask(this).execute(UserInfo.getInstance().getId());
		

		adapter = new SocialAuthAdapter(new ResponseListener(this, this));
		socialNetworkSupport = new SocialNetworkSupport(this, adapter);
		flag_refesh_email_fit = true;
		flag_load_account_fit = true;
		new LoadProfileAsynctask(this, this).execute(UserInfo.getInstance()
				.getId());

		load_resolution();
		try {
			makeQuickActionSocial();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ib_add_linked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

		// paypal
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	

	}

	public static void Load_Profile() {
		new LoadProfileAsynctask(context, activity).execute(UserInfo
				.getInstance().getId());
	}

	private void load_resolution() {
		tv_photo_resolution.setText(getSizePhoto());
		tv_video_resolution.setText(getSizeVideo());
	}

	public String getSizePhoto() {
		try {
			Camera mCamera = Camera.open();
			Camera.Parameters params = mCamera.getParameters();
			List<Size> sizes = params.getSupportedPictureSizes();
			mCamera.release();
			return sizes.get(sizes.size() - 1).width + " x "
					+ sizes.get(sizes.size() - 1).height;
		} catch (Exception ex) {
			return "Not available";
		}
	}

	public String getSizeVideo() {
		try {
			Camera mCamera = Camera.open();
			Camera.Parameters params = mCamera.getParameters();
			List<Size> sizes = params.getSupportedVideoSizes();
			mCamera.release();
			return sizes.get(sizes.size() - 1).width + " x "
					+ sizes.get(sizes.size() - 1).height;
		} catch (Exception ex) {
			return "Not available";
		}
	}


	private void setFont() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tv_title.setTypeface(typeface);
	}

	private void adjustUserInterface() {

		ResizeUtils.resizeRelativeLayout(rl_author_avatar,
				(int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.17), 0, 0, 0, 0);

		ResizeUtils.resizeImageView2(iv_edit_avatar,
				(int) (Config.screenWidth * 0.055),
				(int) (Config.screenWidth * 0.055), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		ResizeUtils.resizeTextView(tv_title, (int) (Config.screenWidth * 0.77),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.025), 0,
				(int) (Config.screenWidth * 0.005), 0,
				RelativeLayout.CENTER_VERTICAL);

		ResizeUtils.resizeButton(btn_logout, (int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.13),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.02),
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_edit_name,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);
		ResizeUtils.resizeRelativeLayout(rl_charge,
				(int) (Config.screenWidth * 0.25),
				(int) (Config.screenHeight * 0.18),
				(int) (Config.screenWidth * 0.02),
				(int) (Config.screenWidth * 0.01), 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);
		ResizeUtils.resizeRelativeLayout(rl_withdraw,
				(int) (Config.screenWidth * 0.25),
				(int) (Config.screenHeight * 0.18),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01), 0, 0,
				RelativeLayout.CENTER_IN_PARENT);
		ResizeUtils.resizeRelativeLayout(rl_history,
				(int) (Config.screenWidth * 0.25),
				(int) (Config.screenHeight * 0.18),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01), 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) rl_withdraw
				.getLayoutParams();
		tmp.addRule(RelativeLayout.RIGHT_OF, rl_charge.getId());
		RelativeLayout.LayoutParams tmp1 = (RelativeLayout.LayoutParams) rl_history
				.getLayoutParams();
		tmp1.addRule(RelativeLayout.RIGHT_OF, rl_withdraw.getId());

		ResizeUtils.resizeImageButton(ib_edit_blocked_users,
				(int) (Config.screenWidth * 0.065),
				(int) (Config.screenWidth * 0.065),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_row_down_linker,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_home,
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageButton(ib_icon_paypal,
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.04),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageButton(ib_row_down_email,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_edit_password,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.ALIGN_PARENT_TOP);

		ResizeUtils.resizeImageButton(ib_row_up_account_blance,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_phone,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.00),
				(int) (Config.screenWidth * 0.00),
				(int) (Config.screenWidth * 0.00),
				(int) (Config.screenWidth * 0.00));

		ResizeUtils.resizeImageButton(ib_add_linked,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_paypal,
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageButton(ib_charge,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_TOP);

		ResizeUtils.resizeImageButton(ib_withdraw,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_TOP);

		ResizeUtils.resizeImageButton(ib_history,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_TOP);

		ResizeUtils.resizeEditText(et_new_password,
				(int) (Config.screenWidth * 0.5),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeEditText(et_confirm_password,
				(int) (Config.screenWidth * 0.5),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_LEFT);

		ResizeUtils.resizeImageButton(ib_a_cong_email,
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.12),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageButton(ib_email_add,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.ALIGN_PARENT_TOP);

		ResizeUtils.resizeEditText(et_add_email,
				(int) (Config.screenWidth * 0.5),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.CENTER_VERTICAL);

		ResizeUtils.resizeEditText(et_name, (int) (Config.screenWidth * 0.5),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_BOTTOM);
		/*
		 * ResizeUtils.resizeLinearLayout(ll_earn,
		 * (int)(Config.screenWidth*0.2), (int)(Config.screenHeight*0.02), 0, 0,
		 * 0, 0); ResizeUtils.resizeLinearLayout(ll_pay,
		 * (int)(Config.screenWidth*0.2), (int)(Config.screenHeight*0.02), 0, 0,
		 * 0, 0); ResizeUtils.resizeLinearLayout(ll_pending,
		 * (int)(Config.screenWidth*0.2), (int)(Config.screenHeight*0.02), 0, 0,
		 * 0, 0); ResizeUtils.re
		 */
		rbMyAccountRating.getLayoutParams().height = ((Drawable) (getResources()
				.getDrawable(R.drawable.star_gray))).getIntrinsicHeight();

		tv_email.getLayoutParams().width = (int) (Config.screenWidth * 0.35);
		tv_email.setSelected(true);

		tv_email_paypal.getLayoutParams().width = (int) (Config.screenWidth * 0.35);
		tv_email_paypal.setSelected(true);

		cbTerms.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		cbTerms.getLayoutParams().height = (int) (Config.screenWidth * 0.05);

		llTerms.getLayoutParams().width = (int) (Config.screenWidth * 0.9);
		llTerms.getLayoutParams().height = (int) (Config.screenWidth * 0.07);
	}

	public void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int id = v.getId();
		switch (id) {
		case R.id.iv_edit_avatar:
			Intent intentAvatar = new Intent();
			intentAvatar.setType("image/*");
			intentAvatar.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentAvatar, Config.REQUEST_SAVE);
			isEditAvatar = true;
			break;
		case R.id.ib_row_down_linker:
			if (ll_linked_profiles.getVisibility() == View.GONE) {
				tv_phone_contacts.setVisibility(View.VISIBLE);
				ib_row_down_linker.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.arrow_up));
				ll_linked_profiles.setVisibility(View.VISIBLE);
				ll_add_linked_profile_icon.setVisibility(View.GONE);
				ll_add_linked_profile_icon_text.setVisibility(View.VISIBLE);
			} else {
				ll_linked_profiles.setVisibility(View.GONE);
				tv_phone_contacts.setVisibility(View.GONE);
				ib_row_down_linker.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.arrow_down));
				ll_add_linked_profile_icon.setVisibility(View.VISIBLE);
				ll_add_linked_profile_icon_text.setVisibility(View.GONE);
			}

			break;
		case R.id.ib_row_down_email:
			try {
				if (ll_add_email.getVisibility() == View.GONE) {
					tv_email_change.setVisibility(View.VISIBLE);
					if (!userInfo.getEmail_paypal().toString().trim()
							.equals("")) {
						ib_icon_paypal.setVisibility(View.VISIBLE);
						tv_email_paypal_change.setVisibility(View.VISIBLE);
					}
					// tv_email_paypal_change.setVisibility(View.VISIBLE);
					// tv_email_paypal_change.setVisibility(View.VISIBLE);
					ib_row_down_email.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.arrow_up));
					ll_add_email.setVisibility(View.VISIBLE);
				} else {
					ll_add_email.setVisibility(View.GONE);
					tv_email_change.setVisibility(View.GONE);
					tv_email_paypal_change.setVisibility(View.GONE);
					ib_row_down_email.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.arrow_down));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.ib_row_up_account_blance:
			if (ll_add_account_blance.getVisibility() == View.GONE) {

				ll_add_account_blance.setVisibility(View.VISIBLE);
				ib_row_up_account_blance.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.arrow_up));
			} else {

				ll_add_account_blance.setVisibility(View.GONE);
				ib_row_up_account_blance.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.arrow_down));
			}
			break;
		case R.id.btn_logout:
			SharedPreferences pref = this.getSharedPreferences(
					Config.DATA_FIFE, Context.MODE_PRIVATE);
			Editor edit = pref.edit();
			edit.putString(Config.USERID_KEY, "-1");
			edit.putString(Config.EMAIL_KEY, " ");
			edit.putString(Config.AVATAR_KEY, "");
			edit.commit();

			// Sign out social
			try {

				try {
					Session session = Session.getActiveSession();
					session.closeAndClearTokenInformation();
					Utility.clearFacebookCookies(this);
				} catch (Exception e) {
					e.printStackTrace();
				}

				SocialNetworkSupport.runLogoutRunnable();
				// callFacebookLogout(context);
				if (Session.getActiveSession().getAccessToken() != null) {
					Session.getActiveSession().closeAndClearTokenInformation();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Intent intent = new Intent(this, SignInActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
			// | Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			this.finish();

			break;
		case R.id.tv_terms2_my:
			Intent termsIntent = new Intent(this,
					TermsAndConditionsActivity.class);
			startActivity(termsIntent);
			break;
		case R.id.ib_email_add:
			if (!checkingEmailAdd())
				return;
			new AddOtherEmailAsynctask(this).execute(et_add_email.getText()
					.toString().trim(), Config.IdUser);
			hideKeyboard();
			break;
		case R.id.ib_edit_name:
			if (et_name.getVisibility() == View.GONE) {
				et_name.setVisibility(View.VISIBLE);
				et_name.setText(tv_name.getText());
				tv_name.setVisibility(View.GONE);
			} else {
				// UserInfo userInfo = UserInfo.getInstance();
				if (!et_name.getText().toString().trim()
						.equals(userInfo.getName().toString().trim())) {
					new ChangeNameAsynctask(this, Config.IdUser, et_name
							.getText().toString().trim()).execute();
				} else {
					et_name.setVisibility(View.GONE);
					// et_name.setText(tv_name.getText());
					tv_name.setVisibility(View.VISIBLE);
					Toast.makeText(this,
							"Please enter the information you want to change!",
							Toast.LENGTH_SHORT).show();
				}
			}
			hideKeyboard();
			break;
		case R.id.ib_edit_password:
			if (!checkingPasswordMatch())
				return;
			new ChangePasswordAsynctask(this).execute(Config.IdUser,
					et_new_password.getText().toString().trim());
			et_new_password.setText("");
			et_confirm_password.setText("");
			hideKeyboard();
			break;
		case R.id.tv_email_change:
			if (UserInfo.getInstance().getOthersEmail().size() > 0
					|| !UserInfo.getInstance().getEmail_paypal().equals(""))
				showOthersEmailListDialog(1);
			else
				Toast.makeText(
						this,
						getResources().getString(R.string.not_have_other_email),
						Toast.LENGTH_SHORT).show();
			break;

		case R.id.tv_email_paypal_change:
			if (UserInfo.getInstance().getOthersEmail().size() > 0
					|| !UserInfo.getInstance().getEmail_paypal().equals(""))
				showOthersEmailListDialog(2);

			else
				Toast.makeText(
						this,
						getResources().getString(R.string.not_have_other_email),
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.ib_edit_blocked_users:
			DialogBlockedUser dialogBlockedUser = (DialogBlockedUser) DialogFactory
					.getDialog(context, DialogType.DIALOG_BLOCKED_USER);
			dialogBlockedUser.setActivity(this);
			dialogBlockedUser.show();
			break;
		case R.id.ib_charge:
			DialogPaymentChallenge dialogPaymentChallenge = (DialogPaymentChallenge) DialogFactory
					.getDialog(context, DialogType.DIALOG_PAYMENT_CHALLENGE);
			dialogPaymentChallenge.setActivity(this);
			dialogPaymentChallenge.show();
			// onBuyPressed();
			break;
		case R.id.ib_withdraw:
			onFuturePaymentPressed();
			break;
		case R.id.ib_history:
			DialogHistoryPaypal dialogHistoryPaypal = (DialogHistoryPaypal) DialogFactory
					.getDialog(context, DialogType.DIALOG_HISTORY_PAYPAL);
			dialogHistoryPaypal.setActivity(this);
			dialogHistoryPaypal.show();
			// onFuturePaymentPurchasePressed();
			break;
		default:
			break;
		}

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// CreateChallengeFragment.isCancel = true;
	// finish();
	// Intent i = new Intent(this, MyAreaActivity.class);
	// startActivity(i);
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	public void updateNameAfterEdit(boolean isSuccess) {
		if (isSuccess) {
			tv_name.setText(et_name.getText().toString().trim());
			tv_name.setVisibility(View.VISIBLE);
			et_name.setVisibility(View.GONE);
		} else {
			tv_name.setVisibility(View.VISIBLE);
			et_name.setVisibility(View.GONE);
		}
	}

	public void resetAddEmailEditText() {
		et_add_email.setText("");
	}

	public void addOtherEmailToUserInfo(String id) {
		OtherEmailItem item = new OtherEmailItem();
		item.setEmail(et_add_email.getText().toString().trim());
		item.setId(id);
		item.setActive(false);
		UserInfo.getInstance().getOthersEmail().add(item);
	}

	public void removeEmailFromUserInfo(String id) {
		if (otherEmailItem == null) {
			otherEmailItem = UserInfo.getInstance().getOthersEmail();
		}
		for (int i = 0; i < otherEmailItem.size(); i++) {
			if (otherEmailItem.get(i).getId().equals(id)) {
				otherEmailItem.remove(i);

			}
		}
		UserInfo.getInstance().setOthersEmail(otherEmailItem);
	}

	public void refreshOthersEmail() {
		userInfo = UserInfo.getInstance();
		ll_others_email.removeAllViews();
		ll_other_email_text.removeAllViews();
		ll_other_email_button.removeAllViews();
		SpannableString spanString;
		// final ArrayList<OtherEmailItem>
		if (flag_refesh_email_fit == true) {
			flag_refesh_email_fit = false;
			otherEmailItem = userInfo.getOthersEmail();
		} else {
			if (otherEmailItem == null) {
				otherEmailItem = userInfo.getOthersEmail();
			}
		}

		for (int i = 0; i < otherEmailItem.size(); i++) {
			// Add email
			TextView tvOtherEmailTextView = new TextView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.width = (int) (Config.screenWidth * 0.35);
			tvOtherEmailTextView.setLayoutParams(params);
			tvOtherEmailTextView.setEllipsize(TruncateAt.MARQUEE);
			tvOtherEmailTextView.setGravity(Gravity.CENTER_VERTICAL);
			tvOtherEmailTextView.setMarqueeRepeatLimit(-1);
			tvOtherEmailTextView.setSingleLine(true);
			tvOtherEmailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.text_title_l));
			tvOtherEmailTextView.setTypeface(null, Typeface.ITALIC);
			tvOtherEmailTextView.setSelected(true);
			spanString = new SpannableString(otherEmailItem.get(i).getEmail());
			spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
			tvOtherEmailTextView.setText(spanString);
			// ll_others_email.addView(tvOtherEmailTextView);

			// Add text
			TextView tvText = new TextView(this);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT);
			params.width = (int) (Config.screenWidth * 0.18);
			tvText.setLayoutParams(params);
			tvText.setGravity(Gravity.CENTER);
			tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_title_l));
			tvText.setTypeface(null, Typeface.ITALIC);
			tvText.setTextColor(getResources().getColor(R.color.text_blue_link));

			// Add text remote
			TextView tvTextRemote = new TextView(this);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT);
			params.width = (int) (Config.screenWidth * 0.18);
			tvTextRemote.setLayoutParams(params);
			tvTextRemote.setGravity(Gravity.CENTER);
			tvTextRemote.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_title_l));
			tvTextRemote.setTypeface(null, Typeface.ITALIC);
			tvTextRemote.setTextColor(getResources().getColor(
					R.color.text_blue_link));

			// ImageButton imgRemote = new ImageButton(this);
			// imgRemote = new ImageButton(this);
			// params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.MATCH_PARENT);
			// params.width = (int) (Config.screenWidth * 0.06);
			// params.height = (int) (Config.screenWidth * 0.06);
			// imgRemote.setLayoutParams(params);
			// imgRemote.setRight(1);

			if (otherEmailItem.get(i).isActive()) {
				spanString = new SpannableString(getResources().getString(
						R.string.my_account_text_remove));
				spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
						0);
				tvText.setText(spanString);
				tvOtherEmailTextView.setTextColor(getResources().getColor(
						R.color.text_blue_link));
				// imgRemote.setColorFilter(R.color.white);
				// imgRemote.setVisibility(View.INVISIBLE);
				tvTextRemote.setVisibility(View.INVISIBLE);
				// imgRemote.setClickable(false);
				final int pos = i;
				tvText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						RemoteEmail(otherEmailItem.get(pos).getId());
						// new RemoveOthersEmailAsynctask(context)
						// .execute(othersEmail.get(pos).getId());
					}
				});
			} else {
				spanString = new SpannableString(getResources().getString(
						R.string.my_account_text_resend));
				spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
						0);
				tvText.setText(spanString);
				tvOtherEmailTextView.setTextColor(getResources().getColor(
						R.color.text_hint));
				// imgRemote.setBackgroundResource(R.drawable.red_close);
				spanString = new SpannableString(getResources().getString(
						R.string.my_account_text_remove));
				spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
						0);
				tvTextRemote.setText(spanString);
				final int pos = i;
				tvText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new SendActiveEmailAsynctask(context)
								.execute(otherEmailItem.get(pos).getId());
					}
				});

				// Add Button remote

				tvTextRemote.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// new RemoveOthersEmailAsynctask(context)
						// .execute(othersEmail.get(pos).getId());
						RemoteEmail(otherEmailItem.get(pos).getId());

					}
				});

			}
			ll_others_email.addView(tvOtherEmailTextView);
			ll_other_email_text.addView(tvText);
			ll_other_email_button.addView(tvTextRemote);

			// ll_other_email_text.addView(imgRemote);
		}

		// tv_email_others.setText(String.format(
		// getResources().getString(R.string.my_account_text_others),
		// otherEmailItem.size()));
		// userInfo = userInfo;
		if (userInfo.getEmail_paypal().equals("")
				|| userInfo.getEmail_paypal().equals(
						userInfo.getEmail().toString().trim())) {
			tv_email_others.setText(String.format(
					getResources().getString(R.string.my_account_text_others),
					otherEmailItem.size()));
		} else {
			tv_email_others.setText(String.format(
					getResources().getString(R.string.my_account_text_others),
					otherEmailItem.size() + 1));
		}
		// if (userInfo.getEmail_paypal().equals(userInfo.getEmail())) {
		// tv_email_others.setText(String.format(
		// getResources().getString(R.string.my_account_text_others),
		// otherEmailItem.size()));
		// } else if (userInfo.getEmail_paypal().equals("")
		// && !(userInfo.getEmail_paypal().equals(userInfo.getEmail()))) {
		// tv_email_others.setText(String.format(
		// getResources().getString(R.string.my_account_text_others),
		// otherEmailItem.size() + 1));
		//
		// }
	}

	private void RemoteEmail(final String email) {

		// TODO Auto-generated method stub
		final DialogRemoteEmail dialogRemote = (DialogRemoteEmail) DialogFactory
				.getDialog(this, DialogType.DIALOG_REMOTE_EMAIL);
		dialogRemote.show();
		dialogRemote.setCancelClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogRemote.dismiss();
			}
		});
		dialogRemote.setOkClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RemoveOthersEmailAsynctask(context).execute(email);
				dialogRemote.dismiss();
			}
		});

		// DialogInterface.OnClickListener dialogClickListener = new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// switch (which) {
		// case DialogInterface.BUTTON_POSITIVE:
		// // Yes button clicked
		// new RemoveOthersEmailAsynctask(context).execute(email);
		//
		// break;
		//
		// case DialogInterface.BUTTON_NEGATIVE:
		//
		// break;
		// }
		// }
		// };
		//
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setMessage("Are you sure you want to delete this  email ?");
		// // .setPositiveButton("Yes", dialogClickListener)
		// // .setNegativeButton("No", dialogClickListener).show();
		//
		// // AlertDialog.Builder builder = new
		// AlertDialog.Builder(getActivity());
		// builder.setPositiveButton("yes", null);
		// builder.setNegativeButton("no", null);
		// // builder.setCancelable(false);
		// final AlertDialog dialog = builder.create();
		// dialog.setOnShowListener(new OnShowListener() {
		//
		// @Override
		// public void onShow(DialogInterface dialogInterface) {
		//
		// Button button2 = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		// Drawable drawable2 = getResources().getDrawable(
		// R.drawable.cancel_button);
		// // set the bounds to place the drawable a bit right
		// // drawable2.setBounds(
		// // (int) (drawable2.getIntrinsicWidth() * 0.5),
		// // (int) (drawable2.getIntrinsicWidth() * 0.5),
		// // (int) (drawable2.getIntrinsicWidth() * 0.5),
		// // (int) (drawable2.getIntrinsicWidth() * 0.5));
		// button2.setCompoundDrawables(drawable2, null, null, null);
		// button2.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });
		//
		// Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		// Drawable drawable = getResources().getDrawable(
		// R.drawable.btn_ok);
		// // set the bounds to place the drawable a bit right
		//
		// // drawable.setBounds((int) (drawable.getIntrinsicWidth() *
		// // 0.3),
		// // (int) (drawable.getIntrinsicWidth() * 0.01),
		// // (int) (drawable.getIntrinsicWidth() * 1.5),
		// // (int) (drawable.getIntrinsicWidth() * 1.2));
		// button.setCompoundDrawables(drawable, null, null, null);
		// // button.setWidth((int) (Config.screenWidth * 0.03));
		// // button.setHeight((int) (Config.screenWidth * 0.03));
		// centerImageAndTextInButton(button);
		// button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// new RemoveOthersEmailAsynctask(context).execute(email);
		// dialog.dismiss();
		// }
		// });
		//
		// }
		// });
		//
		// dialog.show();
		// // Retrieve the button
		// Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		// // Customize the button
		// button.setTextColor(Color.WHITE);
		// button.setTypeface(Typeface.DEFAULT_BOLD);

	}

	public static void centerImageAndTextInButton(Button button) {
		Rect textBounds = new Rect();
		// Get text bounds
		CharSequence text = button.getText();
		if (text != null && text.length() > 0) {
			TextPaint textPaint = button.getPaint();
			textPaint.getTextBounds(text.toString().trim(), 0, text.length(),
					textBounds);
		}
		// Set left drawable bounds
		Drawable leftDrawable = button.getCompoundDrawables()[0];
		if (leftDrawable != null) {
			Rect leftBounds = leftDrawable.copyBounds();
			int width = button.getWidth()
					- (button.getPaddingLeft() + button.getPaddingRight());
			int leftOffset = (width - (textBounds.width() + leftBounds.width()) - button
					.getCompoundDrawablePadding())
					/ 2
					- button.getCompoundDrawablePadding();
			leftBounds.offset(leftOffset, 0);
			leftDrawable.setBounds(leftBounds);
		}
	}

	private boolean checkingEmailAdd() {
		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		if (et_add_email.getText().toString().trim().equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.email_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!et_add_email.getText().toString().trim().matches(emailPattern)) {
			Toast.makeText(this,
					getResources().getString(R.string.email_not_formatted),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (et_add_email.getText().toString().trim()
				.equals(userInfo.getEmail().toString().trim())) {
			Toast.makeText(this,
					getResources().getString(R.string.email_is_exists),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		for (int i = 0; i < otherEmailItem.size(); i++) {
			if (otherEmailItem.get(i).getEmail()
					.equals(et_add_email.getText().toString().trim())) {
				Toast.makeText(this,
						getResources().getString(R.string.email_is_exists),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	private boolean checkingPasswordMatch() {
		if (et_new_password.getText().toString().trim().equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.password_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!et_new_password.getText().toString().trim()
				.equals(et_confirm_password.getText().toString().trim())) {
			Toast.makeText(this,
					getResources().getString(R.string.password_dont_match),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void showOthersEmailListDialog(final int type) {
		userInfo = UserInfo.getInstance();
		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		ArrayList<LinearLayout> emailList = new ArrayList<LinearLayout>();
		final ArrayList<RadioButton> radioHomeList = new ArrayList<RadioButton>();
		final ArrayList<RadioButton> radioPapalList = new ArrayList<RadioButton>();
		int count = 0;

		if (!userInfo.getEmail_paypal().equals("")) {

			if (type == 1) {
				if (!CheckExistEmailLike2(userInfo.getEmail_paypal().toString()
						.trim())
						&& !(userInfo.getEmail_paypal().toString().trim()
								.equals(userInfo.getEmail().toString().trim()))) {
					OtherEmailItem item1 = new OtherEmailItem();
					item1.setActive(true);
					item1.setEmail(userInfo.getEmail_paypal().toString().trim());
					item1.setId(userInfo.getEmail_paypal().toString().trim());
					otherEmailItem.add(item1);
					flag_remote_email = true;
				}
			} else if (type == 2) {
				if (!CheckExistEmailLike2(userInfo.getEmail().toString().trim())
						&& !(userInfo.getEmail_paypal().toString().trim()
								.equals(userInfo.getEmail().toString().trim()))) {
					OtherEmailItem item2 = new OtherEmailItem();
					item2.setActive(true);
					item2.setEmail(userInfo.getEmail().toString().trim());
					item2.setId(userInfo.getEmail().toString().trim());
					otherEmailItem.add(item2);
					flag_remote_email = true;
				}
			}

		}
		int lenghtEmailOther = otherEmailItem.size();

		for (int i = 0; i < lenghtEmailOther; i++) {
			//
			RadioButton radioHome = new RadioButton(this);
			LinearLayout _llHome = new LinearLayout(this);
			LinearLayout.LayoutParams _paramsHome = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			_paramsHome.width = (int) (Config.screenWidth * 0.12);
			_paramsHome.height = (int) (Config.screenWidth * 0.05);
			_llHome.setLayoutParams(_paramsHome);
			_llHome.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			_llHome.setPadding(0, 0, 10, 0);
			LinearLayout.LayoutParams paramsHome = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsHome.width = (int) (Config.screenWidth * 0.04);
			paramsHome.height = (int) (Config.screenWidth * 0.04);
			radioHome.setLayoutParams(paramsHome);
			radioHome.setButtonDrawable(getResources().getDrawable(
					R.drawable.shape_null));
			radioHome.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.custom_radio_button));
			_llHome.addView(radioHome);
			radioHomeList.add(radioHome);
			//
			LinearLayout _llPaypal = new LinearLayout(this);
			RadioButton radioPaypal = new RadioButton(this);
			LinearLayout.LayoutParams _paramsPaypal = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			_paramsPaypal.width = (int) (Config.screenWidth * 0.12);
			_paramsPaypal.height = (int) (Config.screenWidth * 0.05);
			_llPaypal.setLayoutParams(_paramsPaypal);
			_llPaypal.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			_llPaypal.setPadding(0, 0, 10, 0);

			LinearLayout.LayoutParams paramsPaypal = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsPaypal.width = (int) (Config.screenWidth * 0.04);
			paramsPaypal.height = (int) (Config.screenWidth * 0.04);
			radioPaypal.setLayoutParams(paramsPaypal);
			radioPaypal.setButtonDrawable(getResources().getDrawable(
					R.drawable.shape_null));
			radioPaypal.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.custom_radio_button));
			_llPaypal.addView(radioPaypal);
			radioPapalList.add(radioPaypal);

			OtherEmailItem item = userInfo.getOthersEmail().get(i);

			if (item.isActive()) {
				count++;
				LinearLayout container = new LinearLayout(this);
				LinearLayout.LayoutParams paramsContainer = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				container.setLayoutParams(paramsContainer);

				LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsText.width = (int) (Config.screenWidth * 0.4);
				paramsText.height = (int) (Config.screenWidth * 0.07);
				TextView text = new TextView(this);
				text.setLayoutParams(paramsText);
				SpannableString span = new SpannableString(item.getEmail());
				span.setSpan(new UnderlineSpan(), 0, span.length(), 0);
				text.setText(span);
				text.setTextColor(getResources().getColor(
						R.color.text_blue_link));
				text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
						.getDimension(R.dimen.text_title_l));
				text.setEllipsize(TruncateAt.MARQUEE);
				text.setMarqueeRepeatLimit(-1);
				text.setSingleLine(true);
				text.setSelected(true);
				container.addView(text);
				// RadioButton radioHome = new RadioButton(this);
				// LinearLayout _llHome = new LinearLayout(this);
				// LinearLayout.LayoutParams _paramsHome = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.MATCH_PARENT);
				// _paramsHome.width = (int) (Config.screenWidth * 0.12);
				// _paramsHome.height = (int) (Config.screenWidth * 0.05);
				// _llHome.setLayoutParams(_paramsHome);
				// _llHome.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				// _llHome.setPadding(0, 0, 10, 0);
				// LinearLayout.LayoutParams paramsHome = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// paramsHome.width = (int) (Config.screenWidth * 0.04);
				// paramsHome.height = (int) (Config.screenWidth * 0.04);
				// radioHome.setLayoutParams(paramsHome);
				// radioHome.setButtonDrawable(getResources().getDrawable(
				// R.drawable.shape_null));
				// radioHome.setBackground(getResources().getDrawable(
				// R.drawable.custom_radio_button));
				// _llHome.addView(radioHome);
				// radioHomeList.add(radioHome);
				final int pos = i;
				radioHome.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int lenght = radioHomeList.size();
						for (int j = 0; j < lenght; j++) {
							radioHomeList.get(j).setChecked(false);
						}
						// int vt = pos - 1;
						radioHomeList.get(pos).setChecked(true);
					}
				});
				container.addView(_llHome);
				// LinearLayout _llPaypal = new LinearLayout(this);
				// LinearLayout.LayoutParams _paramsPaypal = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.MATCH_PARENT);
				// _paramsPaypal.width = (int) (Config.screenWidth * 0.12);
				// _paramsPaypal.height = (int) (Config.screenWidth * 0.05);
				// _llPaypal.setLayoutParams(_paramsPaypal);
				// _llPaypal.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL
				// | Gravity.CENTER_HORIZONTAL);
				// _llPaypal.setPadding(0, 0, 10, 0);
				// RadioButton radioPaypal = new RadioButton(this);
				// LinearLayout.LayoutParams paramsPaypal = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// paramsPaypal.width = (int) (Config.screenWidth * 0.04);
				// paramsPaypal.height = (int) (Config.screenWidth * 0.04);
				// radioPaypal.setLayoutParams(paramsPaypal);
				// radioPaypal.setButtonDrawable(getResources().getDrawable(
				// R.drawable.shape_null));
				// radioPaypal.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.custom_radio_button));
				// _llPaypal.addView(radioPaypal);
				// radioPapalList.add(radioPaypal);
				radioPaypal.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (radioPapalList.get(pos).isChecked()) {
							radioPapalList.get(pos).setChecked(false);
						}
						int lenght = radioPapalList.size();
						for (int j = 0; j < lenght; j++) {
							radioPapalList.get(j).setChecked(false);
						}
						radioPapalList.get(pos).setChecked(true);
					}
				});
				container.addView(_llPaypal);
				emailList.add(container);
			}
		}

		if (count == 0) {
			Toast.makeText(this,
					getResources().getString(R.string.not_have_other_email),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (lenghtEmailOther == count) {
			Config.flag_change_email = true;
		} else {
			Config.flag_change_email = false;
		}
		final DialogChangeEmail dialog = (DialogChangeEmail) DialogFactory
				.getDialog(context, DialogType.DIALOG_CHANGE_EMAIL);
		dialog.show();
		dialog.setOkClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int lenght = otherEmailItem.size();
				// int lenght = radioHomeList.size();
				for (int j = 0; j < lenght; j++) {

					if (!isNumeric(otherEmailItem.get(j).getId())) {
						if (radioHomeList.get(j).isChecked()
								&& radioPapalList.get(j).isChecked()) {
							if (type == 1) {
								new ChangeEmailProfileAsynctask(context, 0,
										type).execute(otherEmailItem.get(j)
										.getId());
							} else if (type == 2) {
								new ChangeEmailProfileAsynctask(context, 1,
										type).execute(otherEmailItem.get(j)
										.getId());
							}
							// if
							// (otherEmailItem.get(j).getId().toString().trim()
							// .equals(userInfo.getEmail())) {
							// new ChangeEmailProfileAsynctask(context, 1,
							// type).execute(otherEmailItem.get(j)
							// .getId());
							// }
							// if
							// (otherEmailItem.get(j).getId().toString().trim()
							// .equals(userInfo.getEmail_paypal())) {
							// new ChangeEmailProfileAsynctask(context, 0,
							// type).execute(otherEmailItem.get(j)
							// .getId());
							// }

						} else if (radioHomeList.get(j).isChecked()) {
							if (type == 2) {
								Toast.makeText(activity,
										"can not change your own email",
										Toast.LENGTH_SHORT).show();
								if (flag_remote_email == true) {
									flag_remote_email = false;
									otherEmailItem.remove(otherEmailItem.size() - 1);
									UserInfo.getInstance().setOthersEmail(
											otherEmailItem);
								}
							} else {
								new ChangeEmailProfileAsynctask(context, 0,
										type).execute(otherEmailItem.get(j)
										.getId());
							}
						} else if (radioPapalList.get(j).isChecked()) {
							if (type == 1) {
								Toast.makeText(activity,
										"can not change your own email",
										Toast.LENGTH_SHORT).show();
								if (flag_remote_email == true) {
									flag_remote_email = false;
									otherEmailItem.remove(otherEmailItem.size() - 1);
									UserInfo.getInstance().setOthersEmail(
											otherEmailItem);
								}
							} else {
								new ChangeEmailProfileAsynctask(context, 1,
										type).execute(otherEmailItem.get(j)
										.getId());
							}
						}
					} else {

						if (radioHomeList.get(j).isChecked()
								&& radioPapalList.get(j).isChecked()) {
							new ChangeEmailProfileAsynctask(context, 2, 0)
									.execute(otherEmailItem.get(j).getId());
						} else if (radioHomeList.get(j).isChecked()) {

							new ChangeEmailProfileAsynctask(context, 0, 0)
									.execute(otherEmailItem.get(j).getId());
						} else if (radioPapalList.get(j).isChecked()) {
							new ChangeEmailProfileAsynctask(context, 1, 0)
									.execute(otherEmailItem.get(j).getId());
						}
					}
				}
				dialog.dismiss();
			}
		});
		dialog.setCancelClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (type == 1
				// && !UserInfo.getInstance().getEmail_paypal().equals("")
				// && !(CheckExistEmailLike2(UserInfo.getInstance()
				// .getEmail_paypal().toString()))
				// && !(UserInfo.getInstance().getEmail_paypal()
				// .toString().equals(UserInfo.getInstance()
				// .getEmail().toString()))) {
				// otherEmailItem.remove(otherEmailItem.size() - 1);
				// } else if (type == 2
				// && !UserInfo.getInstance().getEmail_paypal().equals("")
				// && !(CheckExistEmailLike2(UserInfo.getInstance()
				// .getEmail().toString()))
				// && !(UserInfo.getInstance().getEmail_paypal()
				// .toString().equals(UserInfo.getInstance()
				// .getEmail().toString()))) {
				// otherEmailItem.remove(otherEmailItem.size() - 1);
				// } else
				if (flag_remote_email == true) {
					flag_remote_email = false;
					otherEmailItem.remove(otherEmailItem.size() - 1);
					UserInfo.getInstance().setOthersEmail(otherEmailItem);
				}
				dialog.dismiss();

				// if (!CheckExistEmailLike2(info.getEmail_paypal().toString())
				// && !(info.getEmail_paypal().toString().equals(info
				// .getEmail().toString()))) {
			}
		});
		dialog.makeEmailData(emailList);
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public boolean CheckExistEmailLike(String email) {
		userInfo = UserInfo.getInstance();
		// ArrayList<OtherEmailItem> others = info.getOthersEmail();
		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		OtherEmailItem emailItem;
		for (int i = 0; i < otherEmailItem.size(); i++) {
			emailItem = otherEmailItem.get(i);
			if (emailItem.isActive()
					&& emailItem.getEmail().toString().trim().equals(email)) {
				return true;
			}
		}
		return false;
	}

	public boolean CheckExistEmailLike2(String email) {
		userInfo = UserInfo.getInstance();
		// ArrayList<OtherEmailItem> others = info.getOthersEmail();
		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		OtherEmailItem emailItem;
		for (int i = 0; i < otherEmailItem.size(); i++) {
			emailItem = otherEmailItem.get(i);
			if (emailItem.getEmail().toString().trim().equals(email)) {
				return true;
			}
		}
		return false;
	}

	public void updateMainEmailAfterChange(String id, int type_api) {
		// userInfo = UserInfo.getInstance();
		userInfo = UserInfo.getInstance();
		// ArrayList<OtherEmailItem> others = info.getOthersEmail();
		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		for (int i = 0; i < otherEmailItem.size(); i++) {
			if (otherEmailItem.get(i).getId().equals(id)) {
				if (type_api == 0) {
					String _email = tv_email.getText().toString().trim();
					tv_email.setText(otherEmailItem.get(i).getEmail());
					if (!(userInfo.getEmail_paypal().equals(userInfo.getEmail()
							.toString().trim()))) {
						if (!CheckExistEmailLike(_email)
								|| CheckExistEmailLike(_email)
								&& flag_remote_email == true) {
							otherEmailItem.get(i).setEmail(_email);
						}
					} else if (!CheckExistEmailLike(_email)
							&& userInfo.getEmail_paypal().equals(
									userInfo.getEmail().toString().trim())) {
						Config.id_email_other = otherEmailItem.get(i).getId();
						otherEmailItem.remove(i);
						flag_remote_email = false;
					}
					userInfo.setOthersEmail(otherEmailItem);
					userInfo.setEmail(tv_email.getText().toString().trim());

				} else if (type_api == 1) {
					String _email = tv_email.getText().toString().trim();
					tv_email.setText(id);
					ib_icon_paypal.setVisibility(View.VISIBLE);
					tv_email_paypal_change.setVisibility(View.VISIBLE);
					if (!CheckExistEmailLike(_email)
							&& !(userInfo.getEmail_paypal().equals(userInfo
									.getEmail()))) {
						if (!Config.id_email_other.equals("-1")) {
							String id_other = (Integer
									.parseInt(Config.id_email_other) + 1) + "";
							otherEmailItem.get(i).setId(id_other);
						}
						otherEmailItem.get(i).setEmail(_email);
					}
					userInfo.setOthersEmail(otherEmailItem);
					userInfo.setEmail(tv_email.getText().toString().trim());

				} else if (type_api == 2) {
					String _email = tv_email_paypal.getText().toString().trim();
					tv_email_paypal.setText(id);
					ib_icon_paypal.setVisibility(View.VISIBLE);
					tv_email_paypal_change.setVisibility(View.VISIBLE);
					if (!CheckExistEmailLike(_email)
							&& !(userInfo.getEmail_paypal().equals(userInfo
									.getEmail()))) {
						if (!Config.id_email_other.equals("-1")) {
							String id_other = (Integer
									.parseInt(Config.id_email_other) + 1) + "";
							otherEmailItem.get(i).setId(id_other);
						}
						otherEmailItem.get(i).setEmail(_email);
					}
					userInfo.setOthersEmail(otherEmailItem);
					userInfo.setEmail_paypal(tv_email_paypal.getText()
							.toString().trim());
					// else if (!CheckExistEmailLike(_email)
					// && UserInfo.getInstance().getEmail_paypal()
					// .equals(UserInfo.getInstance().getEmail())) {
					// otherEmailItem.remove(i);
					// }

				}

			}
		}
		if (flag_remote_email == true && type_api == 0) {
			flag_remote_email = false;
			otherEmailItem.remove(otherEmailItem.size() - 1);
			userInfo.setOthersEmail(otherEmailItem);
		}
	}

	public void updateMainPaypalEmailAfterChange(String id) {
		userInfo = UserInfo.getInstance();
		// ArrayList<OtherEmailItem> others = info.getOthersEmail();
		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		for (int i = 0; i < otherEmailItem.size(); i++) {
			if (otherEmailItem.get(i).getId().equals(id)) {
				String _email1 = tv_email.getText().toString().trim();
				String _email2 = tv_email_paypal.getText().toString().trim();
				tv_email.setText(otherEmailItem.get(i).getEmail());
				tv_email_paypal.setText(otherEmailItem.get(i).getEmail());
				if (!CheckExistEmailLike(_email1)) {
					if (!Config.id_email_other.equals("-1")) {
						String id_other = (Integer
								.parseInt(Config.id_email_other) + 1) + "";
						otherEmailItem.get(i).setId(id_other);
						Config.id_email_other = id_other;
					}
					otherEmailItem.get(i).setEmail(_email1);
				}
				if (!_email1.toString().trim()
						.equals(_email2.toString().trim())) {
					OtherEmailItem emailItem = new OtherEmailItem();
					emailItem.setActive(true);
					emailItem.setEmail(_email2);
					if (!Config.id_email_other.equals("-1")) {
						String id_other = (Integer
								.parseInt(Config.id_email_other) + 2) + "";
						emailItem.setId(id_other);
						Config.id_email_other = id_other;
					}
					otherEmailItem.add(emailItem);
				}

				userInfo.setEmail(tv_email.getText().toString().trim());
				userInfo.setEmail_paypal(tv_email_paypal.getText().toString()
						.trim());
				userInfo.setOthersEmail(otherEmailItem);
			}
		}
		if (flag_remote_email == true) {
			flag_remote_email = false;
			for (int i = 0; i < otherEmailItem.size(); i++) {
				if (!isNumeric(otherEmailItem.get(i).getId())) {
					otherEmailItem.remove(i);
					userInfo.setOthersEmail(otherEmailItem);
				}
			}

		}
	}

	public void updatePaypalEmailAfterChange(String id, int type_api) {
		// ArrayList<OtherEmailItem> others;
		userInfo = UserInfo.getInstance();
		if (otherEmailItem == null) {
			otherEmailItem = userInfo.getOthersEmail();
		}
		for (int i = 0; i < otherEmailItem.size(); i++) {
			if (otherEmailItem.get(i).getId().equals(id)) {
				if (type_api == 0) {
					String _email = tv_email_paypal.getText().toString().trim();
					tv_email_paypal.setText(otherEmailItem.get(i).getEmail());
					ib_icon_paypal.setVisibility(View.VISIBLE);
					tv_email_paypal_change.setVisibility(View.VISIBLE);
					if (!(userInfo.getEmail_paypal().equals(userInfo.getEmail()
							.toString().trim()))) {
						if (!CheckExistEmailLike(_email)
								|| CheckExistEmailLike(_email)
								&& flag_remote_email == true)
							otherEmailItem.get(i).setEmail(_email);
					} else if (!CheckExistEmailLike(_email)
							&& userInfo.getEmail_paypal().equals(
									userInfo.getEmail().toString().trim())) {
						Config.id_email_other = otherEmailItem.get(i).getId();
						otherEmailItem.remove(i);
						flag_remote_email = false;
					}
					userInfo.setOthersEmail(otherEmailItem);
					userInfo.setEmail_paypal(tv_email_paypal.getText()
							.toString().trim());
				} else if (type_api == 1) {
					String _email = tv_email.getText().toString().trim();
					tv_email.setText(id);
					ib_icon_paypal.setVisibility(View.VISIBLE);
					tv_email_paypal_change.setVisibility(View.VISIBLE);
					if (!CheckExistEmailLike(_email)
							&& !(userInfo.getEmail_paypal().equals(userInfo
									.getEmail()))) {
						if (!Config.id_email_other.equals("-1")) {
							String id_other = (Integer
									.parseInt(Config.id_email_other) + 1) + "";
							otherEmailItem.get(i).setId(id_other);
							// Config.id_email_other = id_other;
						}
						otherEmailItem.get(i).setEmail(_email);
					}
					userInfo.setOthersEmail(otherEmailItem);
					userInfo.setEmail(tv_email.getText().toString().trim());
				} else if (type_api == 2) {
					String _email = tv_email_paypal.getText().toString().trim();
					tv_email_paypal.setText(id);
					ib_icon_paypal.setVisibility(View.VISIBLE);
					tv_email_paypal_change.setVisibility(View.VISIBLE);
					if (!CheckExistEmailLike(_email)
							&& !(userInfo.getEmail_paypal().equals(userInfo
									.getEmail()))) {
						if (!Config.id_email_other.equals("-1")) {
							String id_other = (Integer
									.parseInt(Config.id_email_other) + 1) + "";
							otherEmailItem.get(i).setId(id_other);
							// Config.id_email_other = id_other;
						}
						otherEmailItem.get(i).setEmail(_email);
					}
					userInfo.setOthersEmail(otherEmailItem);
					userInfo.setEmail_paypal(tv_email_paypal.getText()
							.toString().trim());
					// else if (!CheckExistEmailLike(_email)
					// && UserInfo.getInstance().getEmail_paypal()
					// .equals(UserInfo.getInstance().getEmail())) {
					// otherEmailItem.remove(i);
					// }

				}
				// if (!_email.equals("11")) {
				// others.get(i).setEmail(_email);
				// }
			}
		}

		if (flag_remote_email == true && type_api == 0) {
			flag_remote_email = false;
			otherEmailItem.remove(otherEmailItem.size() - 1);
			userInfo.setOthersEmail(otherEmailItem);
		}
		// if (type_api != 0) {
		// MyAccountActivity.otherEmailItem
		// .remove(MyAccountActivity.otherEmailItem.size() - 1);
		// } else {
		// if (!UserInfo.getInstance().getEmail_paypal().equals("")) {
		// MyAccountActivity.otherEmailItem
		// .remove(MyAccountActivity.otherEmailItem.size() - 1);
		// }
		// }
	}

	private Drawable getIconSocial(Drawable drawable) {
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06), false);
		return new BitmapDrawable(getResources(), bitmapResized);
	}

	private void makeQuickActionSocial() {
		ActionItem facebookItem = new ActionItem(SocialNetworkType.FACEBOOK,
				"", getIconSocial(getResources().getDrawable(
						R.drawable.facebook)));
		ActionItem twitterItem = new ActionItem(SocialNetworkType.TWITTER, "",
				getIconSocial(getResources().getDrawable(R.drawable.twitter)));
		ActionItem googlePlusItem = new ActionItem(
				SocialNetworkType.GOOGLEPLUS, "", getIconSocial(getResources()
						.getDrawable(R.drawable.googleplus)));
		ActionItem instargramItem = new ActionItem(
				SocialNetworkType.INSTARGRAM, "", getIconSocial(getResources()
						.getDrawable(R.drawable.instagram)));
		ActionItem linkedInItem = new ActionItem(SocialNetworkType.LINKEDIN,
				"", getIconSocial(getResources().getDrawable(
						R.drawable.linkedin)));
		ActionItem youtubeItem = new ActionItem(SocialNetworkType.YOUTUBE, "",
				getIconSocial(getResources().getDrawable(R.drawable.google)));

		quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		quickAction.addActionItem(facebookItem);

		// quickAction.addActionItem(twitterItem);v->
		// quickAction.addActionItem(googlePlusItem);
		// quickAction.addActionItem(instargramItem);
		// quickAction.addActionItem(linkedInItem);
		// quickAction.addActionItem(youtubeItem);v

		quickAction
				.setOnActionItemClickListener(new OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						addLinkedSocial(actionId);
					}
				});
	}

	public Profile getProfile() {
		return adapter.getUserProfile();
	}

	private void addLinkedSocial(int type) {
		if (type == SocialNetworkType.FACEBOOK) {
			socialNetworkSupport.loginSocialNetwork(SocialNetworkType.FACEBOOK);
			SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.FACEBOOK;
			SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_FACEBOOK;
		} else if (type == SocialNetworkType.TWITTER) {
			socialNetworkSupport.loginSocialNetwork(SocialNetworkType.TWITTER);
			SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.TWITTER;
			SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_TWITTER;
		} else if (type == SocialNetworkType.GOOGLEPLUS) {
			socialNetworkSupport
					.loginSocialNetwork(SocialNetworkType.GOOGLEPLUS);
			SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.GOOGLEPLUS;
			SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_GOOGLEPLUS;
		} else if (type == SocialNetworkType.INSTARGRAM) {
			socialNetworkSupport
					.loginSocialNetwork(SocialNetworkType.INSTARGRAM);
			SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.INSTARGRAM;
			SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_INSTARGRAM;
		} else if (type == SocialNetworkType.LINKEDIN) {
			socialNetworkSupport.loginSocialNetwork(SocialNetworkType.LINKEDIN);
			SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.LINKEDIN;
			SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_LINKEDIN;
		} else if (type == SocialNetworkType.YOUTUBE) {
			socialNetworkSupport.loginSocialNetwork(SocialNetworkType.YOUTUBE);
			SocialNetworkSupport.SOCIAL_CONNECT_TYPE = SocialNetworkType.YOUTUBE;
			SocialNetworkSupport.SOCIAL_CONNECT_NAME = SocialNetworkType.NAME_YOUTUBE;
		}
	}

	public void addLinkedProfileToUserInfo(String id) {
		LinkedProfileItem item = new LinkedProfileItem();
		item.setId(id);
		item.setName(SocialNetworkSupport.SOCIAL_CONNECT_NAME);
		item.setUserID(Config.IdUser);
		item.setStatus("1");
		if (!checkingExistsLinkedProfile(item))
			UserInfo.getInstance().getLinkedProfileItem().add(item);
	}

	private boolean checkingExistsLinkedProfile(LinkedProfileItem item) {
		ArrayList<LinkedProfileItem> items = UserInfo.getInstance()
				.getLinkedProfileItem();
		for (int i = 0; i < items.size(); i++) {
			if (item.getName().equals(items.get(i).getName())
					&& items.get(i).getStatus().equals("1")) {
				Toast.makeText(
						this,
						getResources().getString(
								R.string.already_linked_profile),
						Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		return false;
	}

	public void unlinkLinkedProfile(String id) {
		ArrayList<LinkedProfileItem> items = UserInfo.getInstance()
				.getLinkedProfileItem();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId().equals(id)) {
				items.remove(i);
			}
		}
	}

	public void refreshLinkedSocialList() {
		ll_add_linked_profile_icon.removeAllViews();
		ll_add_linked_profile_icon_text.removeAllViews();
		ArrayList<LinkedProfileItem> items = UserInfo.getInstance()
				.getLinkedProfileItem();
		SpannableString spanString;
		tv_linked_profile
				.setText(String.format(
						getResources().getString(
								R.string.my_account_text_linked_profiles),
						items.size()));
		int count = 1;
		for (int i = 0; i < items.size(); i++) {

			final LinkedProfileItem item = items.get(i);

			count++;
			// header
			ImageButton ibIcon = new ImageButton(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.width = (int) (Config.screenWidth * 0.06);
			params.height = (int) (Config.screenWidth * 0.06);
			params.setMargins((int) (Config.screenHeight * 0.005), 0, 0, 0);
			ibIcon.setLayoutParams(params);
			ibIcon.setBackgroundDrawable(getSocialIcon(item.getName()));
			ll_add_linked_profile_icon.addView(ibIcon);
			// if (item.getStatus().equals("1")) {
			// ll_add_linked_profile_icon.setVisibility(View.VISIBLE);
			// } else {
			// ll_add_linked_profile_icon.setVisibility(View.GONE);
			// }

			// content
			LinearLayout llContainer = new LinearLayout(this);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(0, (int) (Config.screenWidth * 0.005), 0, 0);
			llContainer.setLayoutParams(params);
			llContainer.setOrientation(LinearLayout.HORIZONTAL);
			llContainer.setGravity(Gravity.LEFT);

			ImageButton ibIconText = new ImageButton(this);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.width = (int) (Config.screenWidth * 0.06);
			params.height = (int) (Config.screenWidth * 0.06);
			ibIconText.setLayoutParams(params);
			ibIconText.setBackgroundDrawable(getSocialIcon(item.getName()));
			if (item.getStatus().equals("1")) {
				ibIconText.setVisibility(View.VISIBLE);
			} else {
				ibIconText.setVisibility(View.INVISIBLE);
			}
			llContainer.addView(ibIconText);

			TextView tvName = new TextView(this);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.width = (int) (Config.screenWidth * 0.3);
			tvName.setLayoutParams(params);
			tvName.setPadding(
					(int) (getResources().getDimension(R.dimen.padding_profile)),
					0, 0, 0);
			tvName.setText(item.getName());
			tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (getResources().getDimension(R.dimen.text_title_l)));
			tvName.setTypeface(null, Typeface.ITALIC);
			if (item.getStatus().equals("1")) {
				tvName.setVisibility(View.VISIBLE);
			} else {
				tvName.setVisibility(View.INVISIBLE);
			}
			llContainer.addView(tvName);

			TextView tvHide = new TextView(this);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			tvHide.setLayoutParams(params);
			// tvHide.setPadding(0,
			// (int) (getResources().getDimension(R.dimen.padding_s)), 0,
			// 0);
			tvHide.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (getResources().getDimension(R.dimen.text_title_l)));
			tvHide.setTypeface(null, Typeface.ITALIC);
			tvHide.setTextColor(getResources().getColor(R.color.text_blue_link));
			if (item.getStatus().equals("1"))
				spanString = new SpannableString(getResources().getString(
						R.string.my_account_text_hide));
			else
				spanString = new SpannableString(getResources().getString(
						R.string.my_account_text_unhide));
			spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
			tvHide.setText(spanString);
			llContainer.addView(tvHide);
			final String status = item.getStatus();
			tvHide.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// item.setStatus("0");
					if (status.equals("1")) {
						item.setStatus("0");
					} else {
						item.setStatus("1");
					}

					new UpdateLinkedProfileAsynctask(context).execute(
							item.getId(), item.getStatus());
				}
			});

			final TextView tvUnlink = new TextView(this);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			tvUnlink.setLayoutParams(params);
			// tvUnlink.setPadding(0, 0,
			// (int) (getResources().getDimension(R.dimen.padding_s)), 0);
			tvUnlink.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(int) (getResources().getDimension(R.dimen.text_title_l)));
			tvUnlink.setTypeface(null, Typeface.ITALIC);
			tvUnlink.setTextColor(getResources().getColor(
					R.color.text_blue_link));
			spanString = new SpannableString(getResources().getString(
					R.string.my_account_text_unlink));
			spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
			tvUnlink.setText(spanString);
			tvUnlink.setGravity(Gravity.RIGHT);
			llContainer.addView(tvUnlink);
			tvUnlink.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (item.getId() != null) {
						new RemoveLinkedProfileAsynctask(context).execute(item
								.getId());
					} else {
						refreshLinkedSocialList();
					}
				}
			});

			ll_add_linked_profile_icon_text.addView(llContainer);
		}
		tv_linked_profile.setText(String.format(
				getResources().getString(
						R.string.my_account_text_linked_profiles), count));
	}

	private Drawable getSocialIcon(String type) {
		if (type.equals(SocialNetworkType.NAME_FACEBOOK)) {
			return getResources().getDrawable(R.drawable.facebook);
		} else if (type.equals(SocialNetworkType.NAME_TWITTER)) {
			return getResources().getDrawable(R.drawable.twitter);
		} else if (type.equals(SocialNetworkType.NAME_GOOGLEPLUS)) {
			return getResources().getDrawable(R.drawable.googleplus);
		} else if (type.equals(SocialNetworkType.NAME_INSTARGRAM)) {
			return getResources().getDrawable(R.drawable.instagram);
		} else if (type.equals(SocialNetworkType.NAME_LINKEDIN)) {
			return getResources().getDrawable(R.drawable.linkedin);
		} else if (type.equals(SocialNetworkType.NAME_YOUTUBE)) {
			return getResources().getDrawable(R.drawable.google);
		}
		return null;
	}

	public void loadAccountInformation() {
		SpannableString spanString;
		userInfo = UserInfo.getInstance();
		if (flag_load_account_fit == true) {
			flag_load_account_fit = false;
			if (userInfo.getId_email_other() > 0) {
				Config.id_email_other = userInfo.getId_email_other() + "";
			}
		}
		MemoryCache memoryCache = new MemoryCache();
		Bitmap bitmap = memoryCache.get(Config.IdUser);
		if (bitmap != null) {
			Bitmap bitmapImage2 = Bitmap.createScaledBitmap(bitmap,
					(int) (Config.screenWidth * 0.17),
					(int) (Config.screenWidth * 0.17), true);

			iv_author_avatar.setImageBitmap(bitmapImage2);
		} else {
			// new ImageLoaderAsyncTask(iv_author_avatar,
			// (int) (Config.screenWidth * 0.17), true).execute(userInfo
			// .getAvatar());

			Picasso.with(this)
					.load(userInfo.getAvatar())
					.resize((int) (ResizeUtils.getSizeDevice(this).x * 0.17),
							(int) (ResizeUtils.getSizeDevice(this).x * 0.17))
					.transform(new CircleTransform()).into(iv_author_avatar);

			// InputStream input = null;
			// // String url;
			// try {
			// // url = new URL(userInfo.getAvatar().toString());
			// try {
			// input = new java.net.URL(userInfo.getAvatar().toString())
			// .openStream();
			// } catch (MalformedURLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 2;
			// bitmap = BitmapFactory.decodeStream(input, null, options);
			// memoryCache = new MemoryCache();
			// memoryCache.put(Config.IdUser, bitmap);
			// Picasso.with(this)
			// .load(userInfo.getAvatar())
			// .resize((int) (ResizeUtils.getSizeDevice(this).x * 0.17),
			// (int) (ResizeUtils.getSizeDevice(this).x * 0.17))
			// .into(iv_author_avatar);
			// } catch (OutOfMemoryError e) {
			// System.gc();
			// e.printStackTrace();
			// if (bitmap != null && bitmap.isRecycled()) {
			// bitmap.recycle();
			// // bitmap = null;
			// }
			// }

		}

		spanString = new SpannableString(userInfo.getBalance() + "");
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		tv_account_blance.setText(spanString);

		spanString = new SpannableString(userInfo.getBirthday());
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		tv_birthdate.setText(spanString);

		spanString = new SpannableString(userInfo.getEmail().toString().trim());
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		tv_email.setText(spanString);

		// spanString = new SpannableString(userInfo.getEmail_paypal());
		if (!userInfo.getEmail_paypal().toString().trim().equals("")) {
			ib_icon_paypal.setVisibility(View.VISIBLE);
			// tv_email_paypal_change.setVisibility(View.VISIBLE);
			// spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
			// 0);
			tv_email_paypal.setText(userInfo.getEmail_paypal().toString()
					.trim());

		} else {
			ib_icon_paypal.setVisibility(View.GONE);
			tv_email_paypal.setText("");
			tv_email_paypal_change.setVisibility(View.GONE);
		}

		spanString = new SpannableString(userInfo.getGender());
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		tv_gender.setText(spanString);

		spanString = new SpannableString(userInfo.getName());
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		tv_name.setText(spanString);

		tv_submitted.setText(userInfo.getTotal_posted() + "  ");
		tv_responded.setText(userInfo.getTotal_responded() + "  ");

		int sumRating = userInfo.getTotal_rating_image()
				+ userInfo.gettotal_rating_video();
		if (sumRating == 0 && userInfo.getTotal_responded() == 0) {
			rbMyAccountRating.setRating(0.0f);
		} else {
			float rating = (float) ((float) sumRating / (float) userInfo
					.getTotal_responded());
			rbMyAccountRating.setRating(rating);
		}

	}

	public boolean setBitmapImage1(String path, String imageshow) {
		path1 = path;
		boolean flag = false;
		// Bitmap bitmapImage = null;
		try {
			if (!path.toString().trim().equals("")) {
				// bitmapImage = BitmapFactory.decodeFile(path1);
				Picasso.with(this)
						.load(imageshow)
						.resize((int) (ResizeUtils.getSizeDevice(this).x * 0.17),
								(int) (ResizeUtils.getSizeDevice(this).x * 0.17))
						.transform(new CircleTransform())
						.into(iv_author_avatar);
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
		// if (bitmapImage != null) {
		// // arrayList_image_acaption.add(path);
		// // resizeImage(bitmapImage);
		// // imageshare = path;
		//
		// // Log.e("Test", "path " + " : " + path1);
		//
		// bitmapImage2 = Bitmap.createScaledBitmap(bitmapImage,
		// (int) (Config.screenWidth * 0.17),
		// (int) (Config.screenWidth * 0.17), true);
		// iv_author_avatar.setImageBitmap(bitmapImage2);
		// return true;
		// } else {
		// return false;
		// }

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Config.REQUEST_SAVE:
			Bitmap bitmap = null;
			if (data != null) {
				mImageCaptureUri = data.getData();
				path = getRealPathFromURI(mImageCaptureUri); // from Gallery
			}

			if (path == null && mImageCaptureUri != null)
				path = mImageCaptureUri.getPath(); // from File Manager

			if (path != null && !path.equals("")) {
				// bitmap = BitmapFactory.decodeFile(path);
				// iv_author_avatar.setImageDrawable(new
				// BitmapDrawable(bitmap));
				// bmAvatar = bitmap;

				new EditAvatarAsynctask(this, path, Config.IdUser).execute();

			} else {
				Toast.makeText(this, "Image failed!", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case Config.REQUEST_CODE_PAYMENT:
			if (resultCode == PaymentActivity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						JSONObject object1 = confirm.toJSONObject();
						JSONObject object2 = confirm.getPayment()
								.toJSONObject();
						new SavePaypalHistoryAsynctacks(this, id_payment,
								object1.toString(), object2.toString())
								.execute();
						ArrItemPaypal = new PayPalItem[0];
						id_payment = "";
						Log.e(Config.TAG, confirm.toJSONObject().toString(4));
						Log.e(Config.TAG, confirm.getPayment().toJSONObject()
								.toString(4));

						/**
						 * TODO: send 'confirm' (and possibly
						 * confirm.getPayment() to your server for verification
						 * or consent completion. See
						 * https://developer.paypal.com
						 * /webapps/developer/docs/integration
						 * /mobile/verify-mobile-payment/ for more details.
						 * 
						 * For sample mobile backend interactions, see
						 * https://github
						 * .com/paypal/rest-api-sdk-python/tree/master
						 * /samples/mobile_backend
						 */
						Toast.makeText(
								getApplicationContext(),
								"PaymentConfirmation info received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e(Config.TAG,
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == PaymentActivity.RESULT_CANCELED) {
				Log.i(Config.TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(Config.TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
			// else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
			// Toast.makeText(this, "Paymnet Failed", Toast.LENGTH_LONG)
			// .show();
			// String errorID = data
			// .getStringExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			// String errorMessage = data
			// .getStringExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			// }

			break;
		case Config.REQUEST_CODE_FUTURE_PAYMENT:
			if (resultCode == PaymentActivity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.e("FuturePaymentExample", auth.toJSONObject()
								.toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(getApplicationContext(),
								"Future Payment code received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample",
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == PaymentActivity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
			break;
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private void getId_Payment() {
		for (int i = 0; i < ArrItemPaypal.length; i++) {
			id_payment += ArrItemPaypal[i].getSku();
			if (i < ArrItemPaypal.length - 1) {
				id_payment += ",";
			}
		}
	}

	// paypal
	public void onBuyPressed() {
		/*
		 * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
		 * Change PAYMENT_INTENT_SALE to PAYMENT_INTENT_AUTHORIZE to only
		 * authorize payment and capture funds later.
		 * 
		 * Also, to include additional payment details and an item list, see
		 * getStuffToBuy() below.
		 */

		// It's important to repeat the clientId here so that the SDK has it if
		// Android restarts your
		// app midway through the payment UI flow.

		// PayPalPayment thingToBuy =
		// getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

		if (ArrItemPaypal.length > 0) {
			// PayPalPayment thingToBuy =
			// getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
			getId_Payment();
			PayPalPayment stuffToBuy = getStuffToBuy(
					PayPalPayment.PAYMENT_INTENT_SALE, ArrItemPaypal);

			Intent intent = new Intent(this, PaymentActivity.class);

			intent.putExtra(PaymentActivity.EXTRA_PAYMENT, stuffToBuy);

			startActivityForResult(intent, Config.REQUEST_CODE_PAYMENT);
		} else {
			Toast.makeText(activity, "not product selected ! ",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onFuturePaymentPressed() {
		Intent intent = new Intent(this, PayPalFuturePaymentActivity.class);

		startActivityForResult(intent, Config.REQUEST_CODE_FUTURE_PAYMENT);
	}

	public void onFuturePaymentPurchasePressed() {
		// Get the Application Correlation ID from the SDK
		String correlationId = PayPalConfiguration
				.getApplicationCorrelationId(this);

		Log.e("FuturePaymentExample", "Application Correlation ID: "
				+ correlationId);

		// TODO: Send correlationId and transaction details to your server for
		// processing with
		// PayPal...
		Toast.makeText(getApplicationContext(),
				"App Correlation ID received from SDK", Toast.LENGTH_LONG)
				.show();
	}

	private PayPalPayment getThingToBuy(String paymentIntent) {
		return new PayPalPayment(new BigDecimal("1.75"), "USD",
				"hipster jeans", paymentIntent);
	}

	/*
	 * This method shows use of optional payment details and item list.
	 */
	private PayPalPayment getStuffToBuy(String paymentIntent, PayPalItem[] items) {
		// PayPalItem[] items2 = {
		// new PayPalItem("old jeans with holes", 2, new BigDecimal(
		// "87.50"), "USD", "sku-12345678"),
		// new PayPalItem("free rainbow patch", 1, new BigDecimal("0.00"),
		// "USD", "sku-zero-price"),
		// new PayPalItem(
		// "long sleeve plaid shirt (no mustache included)", 6,
		// new BigDecimal("37.99"), "USD", "sku-33333") };

		// Total money challenge
		BigDecimal subtotal = PayPalItem.getItemTotal(items);
		// shipping costs
		BigDecimal shipping = new BigDecimal("0");
		// tax costs
		BigDecimal tax = new BigDecimal("0");

		PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
				shipping, subtotal, tax);
		// get Total money
		BigDecimal amount = subtotal.add(shipping).add(tax);
		String title = getTitleItems(items);
		PayPalPayment payment = new PayPalPayment(amount, "USD", title,
				paymentIntent);
		return payment.items(items).paymentDetails(paymentDetails);
	}

	private String getTitleItems(PayPalItem[] items) {
		String title = "";
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (title.equals("")) {
					title += items[i].getName();
				} else {
					title += " ," + items[i].getName();
				}
			}
		}
		return title;
	}

	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

		/**
		 * TODO: Send the authorization response to your server, where it can
		 * exchange the authorization code for OAuth access and refresh tokens.
		 * 
		 * Your server must then store these tokens, so that your server code
		 * can execute payments for this user in the future.
		 * 
		 * A more complete example that includes the required app-server to
		 * PayPal-server integration is available from
		 * https://github.com/paypal/
		 * rest-api-sdk-python/tree/master/samples/mobile_backend
		 */

	}

	@Override
	public void onDestroy() {
		// Stop service when done
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
	// https://www.paypalobjects.com/webstatic/en_US/developer/docs/pdf/pp_mpl_developer_guide_and_reference_android.pdf
	// http://www.androiddevelopersolutions.com/2012/09/paypal-integration-in-android.html
}
