package remoteeyes.bsp.vn;

import java.util.List;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthAdapter;

import com.squareup.picasso.Picasso;

import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.WheelBirthdayDialog;
import remoteeyes.bsp.vn.asynctask.SignUpAsynctask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.custom.quickaction.ActionItem;
import remoteeyes.bsp.vn.custom.quickaction.QuickAction;
import remoteeyes.bsp.vn.custom.quickaction.QuickAction.OnActionItemClickListener;
import remoteeyes.bsp.vn.model.RegisterInfo;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.RegisterProfileListener;
import remoteeyes.bsp.vn.social.network.ResponseListener;
import remoteeyes.bsp.vn.social.network.SocialNetworkSupport;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.SocialNetworkType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewAccountActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	ImageView iv_edit_avatar, iv_avatar;
	TextView tv_title, tv_name, tv_gender_female, tv_gender_male,
			tv_photo_resolution, tv_video_resolution, tvTerms1New, tvTerms2New,
			tvYear, tvMonth, tvDay;
	RadioButton rb_gender_male, rb_gender_female;
	Button btn_register;
	ImageButton ib_edit_avatar, ib_edit_name, ib_phone_contacts, ib_add_phone,
			ib_edit_password;
	EditText et_name, et_email, et_new_password, et_confirm_password;
	RelativeLayout rl_author_avatar;
	Uri mImageCaptureUri;
	Bitmap bmAvatar;
	String path = "";
	Context context;
	CheckBox cbTerms;
	LinearLayout llTerms, llLinkedProfile;
	public static RegisterInfo info = new RegisterInfo();
	QuickAction quickAction;
	SocialNetworkSupport socialNetworkSupport;
	SocialAuthAdapter adapter;
	Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_new_account);
		this.context = this;
		et_name = (EditText) findViewById(R.id.et_name);
		iv_edit_avatar = (ImageView) findViewById(R.id.iv_edit_avatar);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_gender_female = (TextView) findViewById(R.id.tv_gender_female);
		tv_gender_male = (TextView) findViewById(R.id.tv_gender_male);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_video_resolution = (TextView) findViewById(R.id.tv_video_resolution);
		tv_photo_resolution = (TextView) findViewById(R.id.tv_photo_resolution);
		btn_register = (Button) findViewById(R.id.btn_register);
		tvTerms1New = (TextView) findViewById(R.id.tv_terms1_new);
		tvTerms2New = (TextView) findViewById(R.id.tv_terms2_new);
		tvYear = (TextView) findViewById(R.id.tv_year);
		tvMonth = (TextView) findViewById(R.id.tv_month);
		tvDay = (TextView) findViewById(R.id.tv_day);

		ib_edit_name = (ImageButton) findViewById(R.id.ib_edit_name);
		ib_phone_contacts = (ImageButton) findViewById(R.id.ib_phone_contacts);
		ib_add_phone = (ImageButton) findViewById(R.id.ib_add_phone);
		ib_edit_password = (ImageButton) findViewById(R.id.ib_edit_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_email = (EditText) findViewById(R.id.et_email);

		rb_gender_male = (RadioButton) findViewById(R.id.rb_gender_male);
		rb_gender_female = (RadioButton) findViewById(R.id.rb_gender_female);
		rl_author_avatar = (RelativeLayout) findViewById(R.id.rl_author_avatar);
		cbTerms = (CheckBox) findViewById(R.id.cb_terms_new);
		llTerms = (LinearLayout) findViewById(R.id.ll_terms);
		llLinkedProfile = (LinearLayout) findViewById(R.id.ll_linked_profile);

		iv_edit_avatar.setOnClickListener(this);
		ib_edit_name.setOnClickListener(this);
		ib_add_phone.setOnClickListener(this);
		ib_edit_password.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		cbTerms.setOnClickListener(this);
		tvTerms2New.setOnClickListener(this);
		tvYear.setOnClickListener(this);
		tvMonth.setOnClickListener(this);
		tvDay.setOnClickListener(this);

		rb_gender_male.setOnCheckedChangeListener(this);
		rb_gender_female.setOnCheckedChangeListener(this);

		adjustUserInterface();
		setFont();
		load_resolution();

		adapter = new SocialAuthAdapter(new ResponseListener(this, this));
		socialNetworkSupport = new SocialNetworkSupport(this, adapter);
		makeQuickActionSocial();
	}

	private void load_resolution() {
		tv_photo_resolution.setText(getSizePhoto());
		tv_video_resolution.setText(getSizeVideo());
	}

	private void setFont() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tv_title.setTypeface(typeface);
	}

	private void adjustUserInterface() {

		int widthDevice = ResizeUtils.getSizeDevice(this).x;

		ResizeUtils.resizeRelativeLayout(rl_author_avatar,
				(int) (widthDevice * 0.17), (int) (widthDevice * 0.17), 0, 0,
				0, 0);

		ResizeUtils.resizeImageView2(iv_edit_avatar,
				(int) (widthDevice * 0.055), (int) (widthDevice * 0.055), 0, 0,
				0, 0, RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		ResizeUtils.resizeTextView(tv_title, (int) (widthDevice * 0.62),
				(int) (widthDevice * 0.1), (int) (widthDevice * 0.025), 0,
				(int) (widthDevice * 0.005), 0, RelativeLayout.CENTER_VERTICAL);

		ResizeUtils.resizeButton(btn_register, (int) (widthDevice * 0.17),
				(int) (widthDevice * 0.18), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeEditText(et_name, (int) (Config.screenWidth * 0.5),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		ResizeUtils.resizeImageButton(ib_edit_name,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_add_phone,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeImageButton(ib_phone_contacts,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageButton(ib_edit_password,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeEditText(et_email, (int) (Config.screenWidth * 0.5),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

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
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		cbTerms.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		cbTerms.getLayoutParams().height = (int) (Config.screenWidth * 0.05);

		llTerms.getLayoutParams().width = (int) (Config.screenWidth * 0.9);
		llTerms.getLayoutParams().height = (int) (Config.screenWidth * 0.08);
	}

	public String getSizePhoto() {
		try {
			Camera mCamera = Camera.open();
			Camera.Parameters params = mCamera.getParameters();
			List<Size> sizes = params.getSupportedPictureSizes();
			mCamera.release();
			for (int i = 0; i < sizes.size(); i++) {
				Log.e("Test",
						"Photo: " + sizes.get(i).width + " x "
								+ sizes.get(i).height);
			}
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
			for (int i = 0; i < sizes.size(); i++) {
				Log.e("Test",
						"Video: " + sizes.get(i).width + " x "
								+ sizes.get(i).height);
			}
			return sizes.get(sizes.size() - 1).width + " x "
					+ sizes.get(sizes.size() - 1).height;
		} catch (Exception ex) {
			return "Not available";
		}

	}

	private String getBirthday() {
		return tvYear.getText() + "-" + tvMonth.getText() + "-"
				+ tvDay.getText();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.iv_edit_avatar:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, Config.REQUEST_SAVE);
			break;
		case R.id.ib_edit_name:
			et_name.setText("");
			et_name.findFocus();
			break;
		case R.id.ib_add_phone:
			quickAction.show(v);
			break;
		case R.id.ib_edit_password:

			break;
		case R.id.btn_register:
			if (!checkInfomation())
				return;
			new SignUpAsynctask(this, this, path,
					et_email.getText().toString(), et_name.getText().toString()
							.trim(), et_new_password.getText().toString(),
					getBirthday().trim(), rb_gender_male.isChecked() ? "1"
							: "0").execute();
			break;
		case R.id.cb_terms_new:
			if (cbTerms.isChecked()) {
				tvTerms1New
						.setTextColor(getResources().getColor(R.color.black));
			} else {
				tvTerms1New.setTextColor(getResources().getColor(R.color.red));
			}
			break;
		case R.id.tv_terms2_new:
			Intent termsIntent = new Intent(this,
					TermsAndConditionsActivity.class);
			startActivity(termsIntent);
			break;
		case R.id.tv_year:
		case R.id.tv_month:
		case R.id.tv_day:
			final WheelBirthdayDialog dialog = (WheelBirthdayDialog) DialogFactory
					.getDialog(context, DialogType.DIALOG_WHEEL_BIRTHDAY);
			dialog.setOkClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tvYear.setText(dialog.getYear() + "");
					tvMonth.setText(String.format("%02d", dialog.getMonth()));
					tvDay.setText(String.format("%02d", dialog.getDay()));
					dialog.dismiss();
				}
			});
			dialog.setCancelClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			break;
		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Config.REQUEST_SAVE:
			Bitmap bitmap = null;

			// if (requestCode == PICK_FROM_FILE) {
			if (data != null) {
				mImageCaptureUri = data.getData();
				path = getRealPathFromURI(mImageCaptureUri); // from Gallery
			}

			if (path == null && mImageCaptureUri != null)
				path = mImageCaptureUri.getPath(); // from File Manager

			if (path != null) {
				bitmap = BitmapFactory.decodeFile(path);
				iv_avatar.setImageBitmap(getCircleBitmap(bitmap));
				bmAvatar = bitmap;
			}
			break;

		}
	}

	private Bitmap getCircleBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
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

	private boolean checkInfomation() {
		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		if (bmAvatar == null) {
			Toast.makeText(this,
					getResources().getString(R.string.avatar_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_name.getText().toString().equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.name_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_email.getText().toString().equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.email_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!et_email.getText().toString().matches(emailPattern)) {
			Toast.makeText(this,
					getResources().getString(R.string.email_not_formatted),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_new_password.getText().toString().equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.password_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!et_new_password.getText().toString()
				.equals(et_confirm_password.getText().toString())) {
			Toast.makeText(this,
					getResources().getString(R.string.password_dont_match),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!cbTerms.isChecked()) {
			Toast.makeText(this,
					getResources().getString(R.string.must_agree_terms),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void makeQuickActionSocial() {
		ActionItem facebookItem = new ActionItem(SocialNetworkType.FACEBOOK,
				"", getIconSocial(getResources().getDrawable(
						R.drawable.facebook)));
		// ActionItem twitterItem = new ActionItem(SocialNetworkType.TWITTER,
		// "", getIconSocial(getResources().getDrawable(R.drawable.twitter)));
		// ActionItem googlePlusItem = new
		// ActionItem(SocialNetworkType.GOOGLEPLUS, "",
		// getIconSocial(getResources().getDrawable(R.drawable.googleplus)));
		// ActionItem instargramItem = new
		// ActionItem(SocialNetworkType.INSTARGRAM, "",
		// getIconSocial(getResources().getDrawable(R.drawable.instagram)));
		// ActionItem linkedInItem = new ActionItem(SocialNetworkType.LINKEDIN,
		// "", getIconSocial(getResources().getDrawable(R.drawable.linkedin)));
		// ActionItem youtubeItem = new ActionItem(SocialNetworkType.YOUTUBE,
		// "", getIconSocial(getResources().getDrawable(R.drawable.google)));

		quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		quickAction.addActionItem(facebookItem);
		// quickAction.addActionItem(twitterItem);
		// quickAction.addActionItem(googlePlusItem);
		// quickAction.addActionItem(instargramItem);
		// quickAction.addActionItem(linkedInItem);
		// quickAction.addActionItem(youtubeItem);

		quickAction
				.setOnActionItemClickListener(new OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						addLinkedSocial(actionId);
					}
				});
	}

	private Drawable getIconSocial(Drawable drawable) {
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06), false);
		return new BitmapDrawable(getResources(), bitmapResized);
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

	public void getSocialProfile() {
		adapter.getUserProfileAsync(new RegisterProfileListener(this));
	}

	public Profile getProfile() {
		return adapter.getUserProfile();
	}

	public void addLinkedProfile() {

		ImageButton ibIconText = new ImageButton(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.06);
		params.height = (int) (Config.screenWidth * 0.06);
		params.setMargins(0, 0, (int) (Config.screenWidth * 0.01), 0);
		ibIconText.setLayoutParams(params);
		ibIconText
				.setBackground(getSocialIcon(SocialNetworkSupport.SOCIAL_CONNECT_TYPE));
		llLinkedProfile.addView(ibIconText);
	}

	private Drawable getSocialIcon(int type) {
		if (type == SocialNetworkType.FACEBOOK) {
			return getResources().getDrawable(R.drawable.facebook);
		} else if (type == SocialNetworkType.TWITTER) {
			return getResources().getDrawable(R.drawable.twitter);
		} else if (type == SocialNetworkType.GOOGLEPLUS) {
			return getResources().getDrawable(R.drawable.googleplus);
		} else if (type == SocialNetworkType.INSTARGRAM) {
			return getResources().getDrawable(R.drawable.instagram);
		} else if (type == SocialNetworkType.LINKEDIN) {
			return getResources().getDrawable(R.drawable.linkedin);
		} else if (type == SocialNetworkType.YOUTUBE) {
			return getResources().getDrawable(R.drawable.google);
		}
		return null;
	}
}
