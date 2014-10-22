package remoteeyes.bsp.vn.fragments;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DelayPayment;
import remateeyes.bsp.vn.dialog.DialogConfirmAccept;
import remateeyes.bsp.vn.dialog.DialogConfirmIgnore;
import remateeyes.bsp.vn.dialog.DialogEditDescription;
import remateeyes.bsp.vn.dialog.DialogEditReward;
import remateeyes.bsp.vn.dialog.DialogExplanatory;
import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.DialogReportChallenge;
import remateeyes.bsp.vn.dialog.DialogResponsePhoto;
import remateeyes.bsp.vn.dialog.DialogResponseVideo;
import remoteeyes.bsp.vn.CreateChallengeActivity;
import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.R.string;
import remoteeyes.bsp.vn.TermsAndConditionsActivity;
import remoteeyes.bsp.vn.asynctask.AcceptChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.DeleteChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.EditChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.IgnoreChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.asynctask.SavePaypalHistoryAsynctacks;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.CommentObject;
import remoteeyes.bsp.vn.model.ImageObject;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.ResponsePhotoObject;
import remoteeyes.bsp.vn.model.ResponseVideoObject;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.FacebookUtils;
import remoteeyes.bsp.vn.type.ColorChallengeType;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.Session;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
public class DetailChallengeFragment extends android.app.Fragment implements
		OnClickListener {

	TextView TvTitle_Challenge, TvDetail_Location, TvStartTime, TvDuration,
			TvReward, TvCountAccept, TvDescription, TvExplanatory_Photo,
			TvResponse_Photo, TvResponse_Video, tvTerms, tvStatusAvatar,
			tvNumImg, tvDistance, tvDurationExpired;
	ImageView ivAuthorAvatar, ivQuoteOpen, ivQuoteClose, ivReport, ivShare,
			ivImgAvatar, ivDotAvatar, ivAcceptedAvatar, ivDistance, ivDotList,
			ivCalendar, ivBell, ivFlag, ivClockExpired, ivDot2, ivCup, ivDot1;
	Button btnAccept, btnRunCamera, btnUpload, btnIgnore, btnWithraw,
			btnEditReward, btnEditDescription, btnRepaid, addExplanatory;
	ImageButton btnArrow_Explanatory_Photos;
	LinearLayout ll_body, llExplanatory, llTerms, llResponsePhotos,
			llResponseVideo, llImageAvatar;
	RelativeLayout rl_explanatory_photos, rl_response_photos,
			rl_response_videos, rl_description;
	HorizontalScrollView hs_explanatory_photos, hs_response_photos,
			hs_response_videos;
	CheckBox cbTermsDetail;
	Runnable backRunnable;
	boolean isVisible;
	View vExpandlory;
	LinearLayout llExpandlory;
	ChallengeObject ChallengeCur;

	Context context;
	Activity activity;
	public DialogEditReward dialogeditReward;
	public static String challengeUploadId = "-1";
	public static boolean isRunCamera = false;
	public static boolean isPhoto = false;
	public static String challengeIdForUpload = "-1";

	int duration_day, duration_hour, duration_minute, reward, category;
	@SuppressLint("ValidFragment")
	String starting_day, starting_hour, starting_minute;

	@SuppressLint("ValidFragment")
	public DetailChallengeFragment(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_detail_challenge,
				container, false);

		ivAuthorAvatar = (ImageView) view.findViewById(R.id.iv_author_avatar);
		tvStatusAvatar = (TextView) view.findViewById(R.id.tv_status_avatar);
		llImageAvatar = (LinearLayout) view.findViewById(R.id.ll_image_avatar);
		ivImgAvatar = (ImageView) view.findViewById(R.id.iv_image_avatar);
		tvNumImg = (TextView) view.findViewById(R.id.tv_num_image_avatar);
		ivDotAvatar = (ImageView) view.findViewById(R.id.iv_dot_avatar);
		ivAcceptedAvatar = (ImageView) view
				.findViewById(R.id.iv_accepted_avatar);
		TvCountAccept = (TextView) view.findViewById(R.id.TvCountAccept);

		TvTitle_Challenge = (TextView) view
				.findViewById(R.id.TvTitle_Challenge);
		ivShare = (ImageView) view.findViewById(R.id.iv_share_detail);
		ivDistance = (ImageView) view.findViewById(R.id.iv_distance);
		tvDistance = (TextView) view.findViewById(R.id.tv_distance);
		ivDotList = (ImageView) view.findViewById(R.id.iv_dot);
		TvDetail_Location = (TextView) view
				.findViewById(R.id.TvDetail_Location);
		TvDetail_Location.setSelected(true);
		addExplanatory = (Button) view.findViewById(R.id.btn_gray_add__detail);

		ivCalendar = (ImageView) view.findViewById(R.id.iv_calendar);
		TvStartTime = (TextView) view.findViewById(R.id.TvStartTime);
		ivBell = (ImageView) view.findViewById(R.id.iv_bell);
		ivFlag = (ImageView) view.findViewById(R.id.iv_flag);
		TvDuration = (TextView) view.findViewById(R.id.TvDuration_Time);
		ivDot1 = (ImageView) view.findViewById(R.id.iv_dot_1);
		ivCup = (ImageView) view.findViewById(R.id.iv_cup);
		TvReward = (TextView) view.findViewById(R.id.TvReward);
		ivDot2 = (ImageView) view.findViewById(R.id.iv_dot_2);
		ivClockExpired = (ImageView) view.findViewById(R.id.iv_clock_expired);
		tvDurationExpired = (TextView) view
				.findViewById(R.id.tv_duration_expired);

		btnRunCamera = (Button) view.findViewById(R.id.btn_camera);
		btnUpload = (Button) view.findViewById(R.id.btn_upload);
		btnIgnore = (Button) view.findViewById(R.id.btn_ignore);
		btnAccept = (Button) view.findViewById(R.id.btn_accept);
		btnWithraw = (Button) view.findViewById(R.id.btn_withdraw);
		btnRepaid = (Button) view.findViewById(R.id.btn_repaid);

		vExpandlory = (View) view.findViewById(R.id.v_explanatory_photos);
		llExpandlory = (LinearLayout) view.findViewById(R.id.ll_explanatory);
		TvExplanatory_Photo = (TextView) view
				.findViewById(R.id.TvExplanatory_Photo);
		TvDescription = (TextView) view.findViewById(R.id.TvDescription);
		TvDescription.setMovementMethod(new ScrollingMovementMethod());
		TvResponse_Photo = (TextView) view.findViewById(R.id.TvResponse_Photo);
		TvResponse_Video = (TextView) view.findViewById(R.id.TvResponse_Video);
		tvTerms = (TextView) view.findViewById(R.id.tv_terms_detail);

		ivQuoteOpen = (ImageView) view.findViewById(R.id.ivQuoteOpen);
		ivQuoteClose = (ImageView) view.findViewById(R.id.ivQuoteClose);
		ivReport = (ImageView) view.findViewById(R.id.iv_report);

		cbTermsDetail = (CheckBox) view.findViewById(R.id.cb_terms_detail);
		btnEditReward = (Button) view.findViewById(R.id.btn_edit_reward);
		btnEditDescription = (Button) view
				.findViewById(R.id.btn_edit_description);
		btnArrow_Explanatory_Photos = (ImageButton) view
				.findViewById(R.id.btnArrow_Explanatory_Photos);
		activity = getActivity();

		ll_body = (LinearLayout) view.findViewById(R.id.ll_body);
		llExplanatory = (LinearLayout) view
				.findViewById(R.id.ll_explanatory_photo);
		llResponsePhotos = (LinearLayout) view
				.findViewById(R.id.ll_response_photos);
		llResponseVideo = (LinearLayout) view
				.findViewById(R.id.ll_response_videos);
		llTerms = (LinearLayout) view.findViewById(R.id.ll_terms_detail);

		rl_explanatory_photos = (RelativeLayout) view
				.findViewById(R.id.rl_explanatory_photos);
		rl_response_photos = (RelativeLayout) view
				.findViewById(R.id.rl_response_photos);
		rl_response_videos = (RelativeLayout) view
				.findViewById(R.id.rl_response_videos);
		rl_description = (RelativeLayout) view
				.findViewById(R.id.rl_description);

		hs_explanatory_photos = (HorizontalScrollView) view
				.findViewById(R.id.hs_explanatory_photos);
		hs_response_photos = (HorizontalScrollView) view
				.findViewById(R.id.hs_response_photos);
		hs_response_videos = (HorizontalScrollView) view
				.findViewById(R.id.hs_response_videos);

		ivReport.setOnClickListener(this);
		btnAccept.setOnClickListener(this);
		btnIgnore.setOnClickListener(this);
		ivShare.setOnClickListener(this);
		btnRunCamera.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
		rl_explanatory_photos.setOnClickListener(this);
		rl_response_photos.setOnClickListener(this);
		rl_response_videos.setOnClickListener(this);
		btnArrow_Explanatory_Photos.setOnClickListener(this);
		btnWithraw.setOnClickListener(this);
		btnEditDescription.setOnClickListener(this);
		btnEditReward.setOnClickListener(this);
		TvDetail_Location.setSelected(true);
		TvStartTime.setSelected(true);
		TvDuration.setSelected(true);
		btnRepaid.setOnClickListener(this);
		btnRepaid.setVisibility(view.GONE);
		addExplanatory.setOnClickListener(this);

		adjustUserInterface();
		setTermAndConditionClicked();

		PayPalConfiguration configuration = new PayPalConfiguration()
				.environment(Config.CONFIG_ENVIRONMENT)
				.clientId(Config.CONFIG_CLIENT_ID)
				// The following are only used in PayPalFuturePaymentActivity.
				.merchantName("Alsighty.com")
				.merchantPrivacyPolicyUri(
						Uri.parse("https://www.example.com/privacy"))
				.merchantUserAgreementUri(
						Uri.parse("https://www.example.com/legal"));
		// paypal
		Intent intent = new Intent(context, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);

		activity.startService(intent);

		return view;
	}

	private void adjustUserInterface() {
		int widthDevice = ResizeUtils.getSizeDevice(getActivity()).x;
		int heightDevice = ResizeUtils.getSizeDevice(getActivity()).y;

		ResizeUtils.resizeImageView(ivAuthorAvatar, (int) (widthDevice * 0.17),
				(int) (widthDevice * 0.17), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01));

		ivImgAvatar.getLayoutParams().width = (int) (Config.screenWidth * 0.055);
		ivImgAvatar.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		ivDotAvatar.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDotAvatar.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ivAcceptedAvatar.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		ivAcceptedAvatar.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		ivShare.getLayoutParams().width = (int) (Config.screenWidth * 0.06);
		ivShare.getLayoutParams().height = (int) (Config.screenWidth * 0.065);

		ivDistance.getLayoutParams().width = (int) (Config.screenWidth * 0.035);
		ivDistance.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		ivDotList.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDotList.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ivCalendar.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		ivCalendar.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ivFlag.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
		ivFlag.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		ivDot1.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDot1.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ivCup.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
		ivCup.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		ivDot2.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDot2.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ivClockExpired.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		ivClockExpired.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		btnAccept.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnAccept.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		btnUpload.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnUpload.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		btnRunCamera.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnRunCamera.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		btnIgnore.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnIgnore.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		btnWithraw.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnWithraw.getLayoutParams().width = (int) (Config.screenWidth * 0.2);
		btnRepaid.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnRepaid.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		ResizeUtils.resizeImageView(ivReport, (int) (widthDevice * 0.2),
				(int) (heightDevice * 0.045), 0, 0,
				(int) (widthDevice * 0.005), (int) (widthDevice * 0.2));

		ResizeUtils.resizeButton(btnEditReward, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		ResizeUtils.resizeButton(addExplanatory, (int) (widthDevice * 0.06),
				(int) (widthDevice * 0.06), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.07), 0, (int) (widthDevice * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);
		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) addExplanatory
				.getLayoutParams();
		tmp.addRule(RelativeLayout.LEFT_OF, btnArrow_Explanatory_Photos.getId());

		ResizeUtils.resizeImageButton(btnArrow_Explanatory_Photos,
				(int) (widthDevice * 0.06), (int) (widthDevice * 0.06), 0, 0,
				0, 0, RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeHorizontalScrollView(hs_explanatory_photos,
				(int) (widthDevice * 1), (int) (widthDevice * 0.3), 0, 0, 0,
				(int) (widthDevice * 0.02));
		ResizeUtils.resizeImageView(ivQuoteOpen, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), RelativeLayout.ALIGN_PARENT_LEFT);

		ResizeUtils.resizeImageView(ivQuoteClose, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), (int) (widthDevice * 0.04),
				(int) (widthDevice * 0.02), RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeButton(btnEditDescription,
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.04),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				RelativeLayout.ALIGN_PARENT_RIGHT);

		// ResizeUtils.resizeTextView(TvDescription, widthDevice,
		// (int) (heightDevice * 0.2), 0, 0, 0, 0,
		// RelativeLayout.ALIGN_LEFT);
		// TvDescription.setPadding((int) (widthDevice * 0.09),
		// (int) (widthDevice * 0.04), (int) (widthDevice * 0.08),
		// (int) (widthDevice * 0.04));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.05);
		params.height = (int) (Config.screenWidth * 0.05);
		params.setMargins(0, (int) (Config.screenWidth * 0.005),
				(int) (Config.screenWidth * 0.01), 0);
		cbTermsDetail.setLayoutParams(params);

	}

	public void setContext(Context context) {
		this.context = context;
	}

	private String getDurationTime(ChallengeObject cha) {
		String result = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm");
			Date startDate = simpleDateFormat.parse(cha.getStartTime());
			starting_day = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
			starting_hour = String.valueOf(startDate.getHours());
			starting_minute = String.valueOf(startDate.getMinutes());
			Date now = simpleDateFormat.parse(simpleDateFormat.format(Calendar
					.getInstance().getTime()));
			long passTime = now.getTime() - startDate.getTime();
			long totalTime = (cha.getDuration_day() * 24 * 60 * 60
					+ cha.getDuration_hour() * 60 * 60 + cha
					.getDuration_minute() * 60) * 1000;
			duration_day = cha.getDuration_day();
			duration_hour = cha.getDuration_hour();
			duration_minute = cha.getDuration_minute();
			if (passTime > 0) {
				long remainTime = totalTime - passTime;
				result = formatFromMiliSecond(remainTime);
			} else {
				result = formatFromMiliSecond(totalTime);
			}

		} catch (ParseException e) {
		}

		return result;
	}

	private String formatFromMiliSecond(long second) {
		String result = "";
		if (second > 0) {
			second /= 1000;
			int days = (int) second / (60 * 60 * 24);
			second = (int) second % (60 * 60 * 24);
			int hours = (int) second / (60 * 60);
			int min = (int) second % (60 * 60) / 60;
			if (days > 0)
				result += days + "d ";
			if (hours > 0)
				result += hours + "h ";
			if (min > 0)
				result += min + "m ";
		} else {
			result = "0";
		}
		return result;
	}

	private String getStartingIn(ChallengeObject cha) {
		getDurationTime(cha);
		String result = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm");
			Date startDate = simpleDateFormat.parse(cha.getStartTime());
			Date now = simpleDateFormat.parse(simpleDateFormat.format(Calendar
					.getInstance().getTime()));
			long difference = startDate.getTime() - now.getTime();
			result = formatFromMiliSecond(difference);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void loadChallenge(final ChallengeObject Cha) {
		ChallengeCur = Cha;
		getStartingIn(Cha);
		// SHOW INFO 1

		if (Cha.isMine() && Cha.getCountAccept() == 0) {
			if (Cha.getiPublic() == 1) {
				Picasso.with(activity).load(Cha.getAvatar())
						.transform(new CircleTransform()).into(ivAuthorAvatar);
				tvStatusAvatar.setText(activity.getResources().getString(
						R.string.status_avatar_public));
				llImageAvatar.setVisibility(View.GONE);
				tvStatusAvatar.setVisibility(View.VISIBLE);
			} else {
				ivAuthorAvatar.setBackground(activity.getResources()
						.getDrawable(R.drawable.no_avatar));
				tvStatusAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setText(activity.getResources().getString(
						R.string.status_avatar_private));
				llImageAvatar.setVisibility(View.GONE);
				ivShare.setVisibility(View.INVISIBLE);
			}

		} else if (Cha.isMine() && Cha.getCountAccept() != 0) {
			if (Cha.getiPublic() == 1) {
				Picasso.with(activity).load(Cha.getAvatar())
						.transform(new CircleTransform()).into(ivAuthorAvatar);
				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);
			} else {
				ivAuthorAvatar.setBackground(activity.getResources()
						.getDrawable(R.drawable.no_avatar));
				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);
				ivShare.setVisibility(View.INVISIBLE);
			}

		} else if (!Cha.isMine()) {
			if (Cha.getiPublic() == 1) {
				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);
				Picasso.with(activity).load(Cha.getAvatar())
						.transform(new CircleTransform()).into(ivAuthorAvatar);

			} else {
				ivAuthorAvatar.setBackground(activity.getResources()
						.getDrawable(R.drawable.no_avatar));
				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);
				ivShare.setVisibility(View.INVISIBLE);

			}
		}

		if (Cha.isMine()) {
			btnAccept.setVisibility(View.INVISIBLE);
			btnIgnore.setVisibility(View.INVISIBLE);
			if (Cha.getIspaid() == 0 && Cha.getReward() != 0) {
				btnRepaid.setVisibility(View.VISIBLE);
			} else {
				btnRepaid.setVisibility(View.GONE);
			}
			
		}

		if (Cha.getIsShare() == 1 || Cha.isGlobal()) {
			ivShare.setVisibility(View.INVISIBLE);
		}

		tvNumImg.setText(Cha.getCountImage() + "");
		TvCountAccept.setText(Cha.getCountAccept() + "");

		TvTitle_Challenge.setText(Cha.getTitle());

		int distance = Cha.getLocationsList().get(0).getDistance();

		if (distance > 1000) {
			tvDistance.setText((float) Math.round((float) distance * 10 / 1000)
					/ 10 + " km");
		} else {
			tvDistance.setText(distance + " m");
		}

		if (Cha.getLocationsList().get(0).getAddress().equals("")) {
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_gray_area));
			ivDotList.setVisibility(View.GONE);
			tvDistance.setVisibility(View.GONE);
			TvDetail_Location.setText(Cha.getLocationsList().get(0).getArea());
		} else {
			TvDetail_Location.setText(Cha.getLocationsList().get(0)
					.getAddress());
		}

		TvStartTime.setText(Cha.getRemainStart());
		TvDuration.setText(Cha.getInterval());
		TvReward.setText(String.format(
				activity.getResources().getString(R.string.dola_sign_reward),
				Cha.getReward()));
		tvDurationExpired.setText(Cha.getInterval());

		// expired
		if (Cha.getIsExpired() == 1) {
			btnRepaid.setVisibility(View.GONE);
			ivImgAvatar.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_black_img));
			ivAcceptedAvatar.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.ic_small_black_check));
			ivCalendar.setVisibility(View.GONE);
			TvStartTime.setVisibility(View.GONE);
			ivBell.setVisibility(View.GONE);
			ivFlag.setVisibility(View.GONE);
			TvDuration.setVisibility(View.GONE);
			ivDot1.setVisibility(View.GONE);
			ivCup.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_black_cup));
			TvReward.setTextColor(activity.getResources().getColor(
					R.color.black));
			btnWithraw.setVisibility(View.GONE);
			btnEditDescription.setVisibility(View.GONE);
			btnEditReward.setVisibility(View.GONE);
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_black_location));
			if (!Cha.isGlobal() && ivDotList.getVisibility() == View.GONE) {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_black_area));
			}
			llTerms.setVisibility(View.GONE);
			ivReport.setVisibility(View.INVISIBLE);
			if (Cha.isGlobal()) {
				TvDetail_Location.setText(activity.getResources().getString(
						R.string.global));
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_black_earth));
			}
		} // not expired
		else {
			ivClockExpired.setVisibility(View.GONE);
			tvDurationExpired.setVisibility(View.GONE);
			ivDot2.setVisibility(View.GONE);
			if (Cha.isMine()) {
				addExplanatory.setVisibility(View.VISIBLE);
			}

			// on going
			if (Cha.getRemainStart().equals("")) {

				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_pink_location));
				if (!Cha.isGlobal() && ivDotList.getVisibility() == View.GONE) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_area));
				}
				ivCalendar.setVisibility(View.GONE);
				TvStartTime.setVisibility(View.GONE);
				ivBell.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_pink_bell));
				ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
				ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

				ivFlag.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_pink_flag));
				if (Cha.isGlobal()) {
					TvDetail_Location.setText(activity.getResources()
							.getString(R.string.global));
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));

				}
				if (Cha.isMine()) {
					llTerms.setVisibility(View.GONE);
					ivReport.setVisibility(View.INVISIBLE);
					btnWithraw.setVisibility(View.GONE);
					btnEditDescription.setVisibility(View.GONE);
					btnEditReward.setVisibility(View.GONE);
				} else if (Cha.getAccept() == ColorChallengeType.ACCEPTED_YET) {
					llTerms.setVisibility(View.GONE);

					btnAccept.setVisibility(View.VISIBLE);
					btnIgnore.setVisibility(View.VISIBLE);

				} else if (Cha.getAccept() == ColorChallengeType.ACCEPTED) {

					ivReport.setVisibility(View.INVISIBLE);
					if (UserInfo.getInstance().getId()
							.equals(Cha.getWinner_id())) {

						btnIgnore.setVisibility(View.GONE);

					} else {
						btnIgnore.setVisibility(View.VISIBLE);
					}
					if (Cha.isGlobal()) {
						if (Cha.getWinner_id() != 0) {
							btnRunCamera.setVisibility(View.GONE);
							btnUpload.setVisibility(View.GONE);
							if (UserInfo.getInstance().getId()
									.equals(Cha.getWinner_id())) {

								btnIgnore.setVisibility(View.GONE);

							} else {
								btnIgnore.setVisibility(View.VISIBLE);
							}

						} else {
							btnRunCamera.setVisibility(View.VISIBLE);
							btnUpload.setVisibility(View.VISIBLE);
						}

					}
					if (distance > 4800 && !Cha.isGlobal()
							|| Cha.getWinner_id() != 0) {
						btnRunCamera.setVisibility(View.GONE);
						btnUpload.setVisibility(View.GONE);
					}

					else {
						btnRunCamera.setVisibility(View.VISIBLE);
						btnUpload.setVisibility(View.VISIBLE);

						if (distance < 4800 && !Cha.isGlobal())
							if(!Cha.isMine())
							{ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_blue_location));}else{ivDistance.setImageDrawable(activity.getResources()
											.getDrawable(R.drawable.ic_gray_location));}
							if (distance < 4800 && !Cha.isGlobal()
									&& ivDotList.getVisibility() == View.GONE) {
								if(!Cha.isMine())
								{ivDistance.setImageDrawable(activity.getResources()
										.getDrawable(R.drawable.ic_blue_area));}else{ivDistance.setImageDrawable(activity.getResources()
												.getDrawable(R.drawable.ic_gray_area));}
							
						}
					}
					if (UserInfo.getInstance().getId()
							.equals(Cha.getWinner_id())) {

						btnIgnore.setVisibility(View.GONE);

					} else {
						btnIgnore.setVisibility(View.VISIBLE);
					}
				}
			}

			// not on going
			else {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_location));
				if (!Cha.isGlobal() && ivDotList.getVisibility() == View.GONE) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_area));
				}
				TvDuration.setText(Cha.getDuration());

				if (Cha.isGlobal()) {
					TvDetail_Location.setText(activity.getResources()
							.getString(R.string.global));
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));
				}
				if (Cha.isMine()) {
					llTerms.setVisibility(View.GONE);
					ivReport.setVisibility(View.INVISIBLE);
					btnWithraw.setVisibility(View.VISIBLE);
					btnEditDescription.setVisibility(View.VISIBLE);
					btnEditReward.setVisibility(View.VISIBLE);
					if (Cha.getIsShare() == 1) {
						btnWithraw.setVisibility(View.INVISIBLE);

					}
				} else if (Cha.getAccept() == ColorChallengeType.ACCEPTED_YET) {
					llTerms.setVisibility(View.GONE);
					btnAccept.setVisibility(View.VISIBLE);
					btnIgnore.setVisibility(View.VISIBLE);

				} else if (Cha.getAccept() == ColorChallengeType.ACCEPTED) {
					ivReport.setVisibility(View.INVISIBLE);
					llTerms.setVisibility(View.VISIBLE);
					btnIgnore.setVisibility(View.VISIBLE);

				}
			}

			if (btnEditReward.getVisibility() == View.VISIBLE) {
				ResizeUtils.resizeLinearLayout(llImageAvatar, 0, 0,
						(int) (Config.screenWidth * 0.008), 0);
			}

			// SHOW INFO 2

			reward = Cha.getReward();
			category = Cha.getCategory();
			ResizeTextView(Cha.getDescription());
			TvDescription.setText(Cha.getDescription());
			// explanatory
			if (Cha.getImagesObjectList().size() == 0) {
				if (!Cha.isMine()) {
					vExpandlory.setVisibility(View.GONE);
					llExpandlory.setVisibility(View.GONE);

				}
				hs_explanatory_photos.setVisibility(View.GONE);
			} else {
				llExplanatory.removeAllViews();
				for (int i = 0; i < Cha.getImagesObjectList().size(); i++) {
					final int pos = i;
					final int total = Cha.getImagesObjectList().size();
					if (total != 0) {
						TvExplanatory_Photo.setText(String.format(
								context.getResources().getString(
										R.string.explanatory_photo), total));
					}

					LinearLayout.LayoutParams lnAllparams = new LinearLayout.LayoutParams(
							android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
							android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					lnAllparams.height = (int) (Config.screenHeight * 0.2);
					lnAllparams.setMargins(0, 0,
							(int) (Config.screenWidth * 0.03),
							(int) (Config.screenWidth * 0.02));

					RelativeLayout rlContent = new RelativeLayout(context);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.height = (int) (Config.screenHeight * 0.2);
					rlContent.setLayoutParams(params);

					final ImageView iv = new ImageView(context);
					params = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
					// params.height = (int) (Config.screenHeight * 0.2);
					// params.width = (int) (Config.screenWidth * 0.2);
					iv.setLayoutParams(params);
					rlContent.addView(iv);
					// Picasso.with(context).load(Cha.getImagesObjectList().get(i).getUrlImage()).into(iv);
					new ImageLoaderAsyncTask(iv,
							(int) (Config.screenHeight * 0.2), false)
							.execute(Cha.getImagesObjectList().get(i)
									.getUrlImage());
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							DialogExplanatory dialog = (DialogExplanatory) DialogFactory
									.getDialog(context,
											DialogType.DIALOG_EXPLANATORY_PHOTO);
							dialog.show();

							DetailChallengeFragment.isPhoto = true;
							dialog.setListImage(Cha.getImagesObjectList());
							dialog.setTitle(String.format(
									context.getResources().getString(
											R.string.dialog_explanatory_title),
									pos + 1, total));
							Drawable image = iv.getDrawable();
							dialog.setImage(image);
							dialog.setUrlImage(Cha.getImagesObjectList()
									.get(pos).getUrlImage());
							dialog.setId(Cha.getImagesObjectList().get(pos)
									.getId());
							dialog.setIndex(pos);
							dialog.setIdChallenge(Cha.getId());
							dialog.setCommentList(Cha.getImagesObjectList()
									.get(pos).getCommentList());
						}
					});
					TextView tvCountComment = new TextView(context);
					params = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
							RelativeLayout.TRUE);
					params.addRule(RelativeLayout.CENTER_IN_PARENT,
							RelativeLayout.TRUE);
					params.setMargins(0, (int) (Config.screenHeight * 0.09), 0,
							0);
					tvCountComment.setGravity(Gravity.CENTER);
					tvCountComment.setBackground(getResources().getDrawable(
							R.drawable.comment));
					tvCountComment.setText(Cha.getImagesObjectList().get(pos)
							.getCommentList().size()
							+ "");
					tvCountComment.setTextColor(getResources().getColor(
							R.color.red));

					tvCountComment.setLayoutParams(params);
					rlContent.addView(tvCountComment);

					rlContent.setLayoutParams(lnAllparams);
					llExplanatory.addView(rlContent);
				}
			}

			// Response photos
			llResponsePhotos.removeAllViews();
			ArrayList<ResponsePhotoObject> rpList = Cha.getResponsePhotoList();
			// for (int j = rpList.size() - 1; j > -1; j--) {
			for (int j = 0; j < rpList.size(); j++) {
				final int pos = j;
				final int total = Cha.getResponsePhotoList().size();
				if (total != 0) {
					TvResponse_Photo.setText(String.format(context
							.getResources().getString(R.string.respose_photo),
							total));
				}

				LinearLayout.LayoutParams lnAllparams = new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				lnAllparams.height = (int) (Config.screenHeight * 0.2);
				lnAllparams.setMargins(0, 0, (int) (Config.screenWidth * 0.03),
						(int) (Config.screenWidth * 0.02));

				RelativeLayout rlContent = new RelativeLayout(context);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.height = (int) (Config.screenHeight * 0.2);
				rlContent.setLayoutParams(params);

				final ImageView iv = new ImageView(context);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				iv.setLayoutParams(params);
				rlContent.addView(iv);

				new ImageLoaderAsyncTask(iv, (int) (Config.screenHeight * 0.2),
						false).execute(Cha.getResponsePhotoList().get(j)
						.getUrlImage());
				final boolean isRated = rpList.get(j).isRated();
				// final boolean isWinned = rpList.get(j).isWinned();
				final int id_image_win = Integer.parseInt(Cha
						.getResponsePhotoList().get(j).getId());

				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DialogResponsePhoto dialog = (DialogResponsePhoto) DialogFactory
								.getDialog(context,
										DialogType.DIALOG_RESPONSE_PHOTO);
						dialog.show();
						DetailChallengeFragment.isPhoto = true;
						dialog.setListImage(Cha.getResponsePhotoList());
						dialog.setTitle(String.format(context.getResources()
								.getString(R.string.dialog_response_title),
								pos + 1, total));
						dialog.setActivity(activity);
						dialog.setTitleChallenge(Cha.getTitle());
						Drawable image = iv.getDrawable();
						dialog.setImage(image);
						dialog.setUrlImage(Cha.getResponsePhotoList().get(pos)
								.getUrlImage());
						dialog.setId(Cha.getResponsePhotoList().get(pos)
								.getId());
						dialog.setIdChall(Cha.getId());
						dialog.setIndex(pos);
						dialog.setType("1");
						dialog.checkVisibleButton(isRated);

						if (Cha.getWinner_id() != 0) {
							dialog.showIcSetWinner(false);
						}
						if (Cha.isMine()) {
							if (Cha.getWinner_id() == 0) {
								dialog.showIcSetWinner(true);
							}

							else if (Cha.getType_win() == 1) {
								if (Cha.getMedia_id() == id_image_win) {
									dialog.iv_winner
											.setVisibility(View.VISIBLE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								} else {
									dialog.iv_winner.setVisibility(View.GONE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								}
							}
						} else {
							if (Cha.getWinner_id() == 0) {
								dialog.showIcSetWinner(false);
							} else if (Cha.getType_win() == 1) {
								if (Cha.getMedia_id() == id_image_win) {
									dialog.iv_winner
											.setVisibility(View.VISIBLE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								} else {
									dialog.iv_winner.setVisibility(View.GONE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								}
							} else {
								dialog.iv_winner.setVisibility(View.GONE);
								dialog.showIcSetWinner(false);
							}
						}

						dialog.setRating(Cha.getResponsePhotoList().get(pos)
								.getRate());
						if (isRated) {
							dialog.setMessage(context.getResources().getString(
									R.string.dialog_response_message_rated));
						} else {
							dialog.setMessage(context
									.getResources()
									.getString(
											R.string.dialog_response_message_not_rate_yet));
							dialog.setRatingBar(isRated);
						}
						dialog.setCommentList(Cha.getResponsePhotoList()
								.get(pos).getCommentList());
					}
				});

				// RatingBar rtImage = new RatingBar(context);
				// RatingBar rtImage =
				// (RatingBar)getLayoutInflater(mArguments).inflate(R.layout.rating_image,
				// null);
				// Drawable drawStar = context.getResources().getDrawable(
				// R.drawable.custom_ratingbar);
				// rtImage.setProgressDrawable(drawStar);
				RatingBar rtImage = new RatingBar(context, null,
						android.R.attr.ratingBarStyleSmall);// R.style.foodRatingBar
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				float rating = Cha.getResponsePhotoList().get(pos).getRate();
				if (rating > 0) {
					rtImage.setRating(rating);
					rtImage.setIsIndicator(true);
				}

				rtImage.setLayoutParams(params);
				rlContent.addView(rtImage);

				TextView tvCountComment = new TextView(context);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);
				params.setMargins(0, (int) (Config.screenHeight * 0.11), 0, 0);
				tvCountComment.setGravity(Gravity.CENTER);
				tvCountComment.setBackground(getResources().getDrawable(
						R.drawable.comment));
				tvCountComment.setText(Cha.getResponsePhotoList().get(pos)
						.getCommentList().size()
						+ "");
				tvCountComment.setTextColor(getResources()
						.getColor(R.color.red));

				tvCountComment.setLayoutParams(params);
				rlContent.addView(tvCountComment);

				rlContent.setLayoutParams(lnAllparams);
				llResponsePhotos.addView(rlContent);
			}

			// Response Video
			llResponseVideo.removeAllViews();
			ArrayList<ResponseVideoObject> videoList = Cha
					.getResponseVideoList();
			for (int k = 0; k < videoList.size(); k++) {
				final int pos = k;
				final int total = Cha.getResponseVideoList().size();
				if (total != 0) {
					TvResponse_Video.setText(String.format(context
							.getResources().getString(R.string.respose_video),
							total));
				}

				LinearLayout.LayoutParams lnAllparams = new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				lnAllparams.height = (int) (Config.screenHeight * 0.2);
				lnAllparams.setMargins(0, 0, (int) (Config.screenWidth * 0.03),
						(int) (Config.screenWidth * 0.02));

				RelativeLayout rlContent = new RelativeLayout(context);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.height = (int) (Config.screenHeight * 0.2);
				rlContent.setLayoutParams(params);

				final ImageView iv = new ImageView(context);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				iv.setLayoutParams(params);
				rlContent.addView(iv);

				new ImageLoaderAsyncTask(iv, (int) (Config.screenHeight * 0.2),
						false).execute(Cha.getResponseVideoList().get(k)
						.getUrlThumb());
				final boolean isRated = videoList.get(k).isRated();

				final int id_video_win = Integer.parseInt(Cha
						.getResponseVideoList().get(k).getId());

				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DialogResponseVideo dialog = (DialogResponseVideo) DialogFactory
								.getDialog(context,
										DialogType.DIALOG_RESPONSE_VIDEO);
						dialog.show();
						DetailChallengeFragment.isPhoto = true;
						dialog.setListVideo(Cha.getResponseVideoList());
						dialog.setTitle(String.format(
								context.getResources().getString(
										R.string.dialog_response__video_title),
								pos + 1, total));
						dialog.setActivity(activity);
						dialog.setTitleChallenge(Cha.getTitle());
						Drawable image = iv.getDrawable();
						dialog.setImage(image);
						dialog.setUrlThumb(Cha.getResponseVideoList().get(pos)
								.getUrlThumb());
						dialog.setUrlVideo(Cha.getResponseVideoList().get(pos)
								.getUrlVideo());
						dialog.setId(Cha.getResponseVideoList().get(pos)
								.getId());
						dialog.setIdChall(Cha.getId());
						dialog.setIndex(pos);
						dialog.setType("2");
						dialog.checkVisibleButton(isRated);
						dialog.setRating(Cha.getResponseVideoList().get(pos)
								.getRate());

						if (Cha.getWinner_id() != 0) {
							dialog.showIcSetWinner(false);
						}
						if (Cha.isMine()) {
							if (Cha.getWinner_id() == 0) {
								dialog.showIcSetWinner(true);
							}

							else if (Cha.getType_win() == 2) {

								if (Cha.getMedia_id() == id_video_win) {
									dialog.iv_winner
											.setVisibility(View.VISIBLE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								} else {
									dialog.iv_winner.setVisibility(View.GONE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								}
							}
						} else {
							if (Cha.getWinner_id() == 0) {
								dialog.showIcSetWinner(false);
							} else if (Cha.getType_win() == 2) {
								if (Cha.getMedia_id() == id_video_win) {
									dialog.iv_winner
											.setVisibility(View.VISIBLE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								} else {
									dialog.iv_winner.setVisibility(View.GONE);
									dialog.showIcSetWinner(false);
									dialog.setWined(true);
								}
							} else {
								dialog.iv_winner.setVisibility(View.GONE);
								dialog.showIcSetWinner(false);
							}
						}

						if (isRated) {
							dialog.setMessage(context.getResources().getString(
									R.string.dialog_response_message_rated));
						} else {
							dialog.setMessage(context
									.getResources()
									.getString(
											R.string.dialog_response_message_not_rate_yet));
							dialog.setRatingBar(isRated);
						}
						dialog.setCommentList(Cha.getResponseVideoList()
								.get(pos).getCommentList());
					}
				});

				RatingBar rtImage = new RatingBar(context, null,
						android.R.attr.ratingBarStyleSmall);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				float rating = Cha.getResponseVideoList().get(pos).getRate();
				if (rating > 0) {
					rtImage.setRating(rating);
					rtImage.setIsIndicator(true);
				}
				rtImage.setLayoutParams(params);
				rlContent.addView(rtImage);

				TextView tvCountComment = new TextView(context);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);
				params.setMargins(0, (int) (Config.screenHeight * 0.11), 0, 0);
				tvCountComment.setGravity(Gravity.CENTER);
				tvCountComment.setBackground(getResources().getDrawable(
						R.drawable.comment));
				tvCountComment.setText(Cha.getResponseVideoList().get(pos)
						.getCommentList().size()
						+ "");
				tvCountComment.setTextColor(getResources()
						.getColor(R.color.red));
				tvCountComment.setLayoutParams(params);
				rlContent.addView(tvCountComment);

				int duration, sec = 0, minute = 0, hour = 0;
				String time;
				try {
					duration = Integer.parseInt(Cha.getResponseVideoList()
							.get(pos).getDuration());
					sec = duration / 1000;
					minute = sec / 60;
					hour = minute / 60;
				} catch (Exception e) {
				}
				time = hour + ":" + minute + ":" + sec;
				if (hour == 0) {
					time = minute + ":" + sec;
				}

				TextView tvDuration = new TextView(context);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
						RelativeLayout.TRUE);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				tvDuration.setGravity(Gravity.CENTER);
				tvDuration.setBackgroundColor(getResources().getColor(
						R.color.black));
				tvDuration.setText(time);
				tvDuration.setTextColor(getResources().getColor(R.color.white));
				tvDuration.setLayoutParams(params);
				rlContent.addView(tvDuration);

				ImageView ivPlay = new ImageView(context);
				params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.height = (int) (Config.screenHeight * 0.07);
				params.width = (int) (Config.screenHeight * 0.07);
				params.addRule(RelativeLayout.CENTER_IN_PARENT,
						RelativeLayout.TRUE);
				ivPlay.setImageDrawable(getResources().getDrawable(
						R.drawable.play));
				ivPlay.setLayoutParams(params);
				rlContent.addView(ivPlay);

				rlContent.setLayoutParams(lnAllparams);
				llResponseVideo.addView(rlContent);
			}
		}

	}

	private void ResizeTextView(String text) {
		int widthDevice = ResizeUtils.getSizeDevice(getActivity()).x;
		int heightDevice = ResizeUtils.getSizeDevice(getActivity()).y;

		int occurance = (text.split(" ").length - 1);
		float count = (float) 0.1;
		while (occurance > 20) {
			count += 0.1;
			occurance = occurance - 20;

		}
		String index = "" + count;

		ResizeUtils.resizeTextView(TvDescription, widthDevice,
				(int) (heightDevice * Float.parseFloat(index)), 0, 0, 0, 0,
				RelativeLayout.ALIGN_LEFT);

		ResizeUtils.resizeRelativeLayout(rl_description, widthDevice,
				(int) (heightDevice * Float.parseFloat(index)), 0, 0, 0, 0);
		TvDescription.setPadding((int) (widthDevice * 0.09),
				(int) (widthDevice * 0.04), (int) (widthDevice * 0.08),
				(int) (widthDevice * 0.04));
	}

	public static int countOccurrences(String haystack, char needle) {
		int count = 0;
		for (int i = 0; i < haystack.length(); i++) {
			if (haystack.charAt(i) == needle) {
				count++;
			}
		}
		return count;
	}

	public void addReponsePhotoVideo(String type) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.height = (int) (Config.screenHeight * 0.2);
		params.setMargins(0, 0, (int) (Config.screenWidth * 0.05), 0);
		if (type.equals("1")) {
			ImageView photo = new ImageView(context);
			photo.setLayoutParams(params);
			llResponsePhotos.addView(photo);
		} else if (type.equals("2")) {
			VideoView video = new VideoView(context);
			video.setLayoutParams(params);
			llResponseVideo.addView(video);
		}
	}

	public void setTermAndConditionClicked() {
		SpannableString ss = new SpannableString(context.getResources()
				.getString(R.string.by_submiting_this_challenge));
		ClickableSpan clickableSpan = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				context.startActivity(new Intent(context,
						TermsAndConditionsActivity.class));
			}
		};
		ss.setSpan(clickableSpan, 158, 177, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.text_blue_link)), 158, 177,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvTerms.setText(ss);
		tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public ChallengeObject getCurrentChallenge() {
		return ChallengeCur;
	}

	public void addCommentReponsePhoto(CommentObject comment, String photoID) {
		for (int i = 0; i < ChallengeCur.getResponsePhotoList().size(); i++) {
			ResponsePhotoObject rpo = ChallengeCur.getResponsePhotoList()
					.get(i);
			if (rpo.getId().equals(photoID)) {
				ChallengeCur.getResponsePhotoList().get(i).getCommentList()
						.add(comment);

			}
		}
	}

	public void addCommentReponseVideo(CommentObject comment, String videoID) {
		for (int i = 0; i < ChallengeCur.getResponseVideoList().size(); i++) {
			ResponseVideoObject rpo = ChallengeCur.getResponseVideoList()
					.get(i);
			if (rpo.getId().equals(videoID)) {
				ChallengeCur.getResponseVideoList().get(i).getCommentList()
						.add(comment);

			}
		}
	}

	public void addCommentExplanPhoto(CommentObject comment, String photoID) {
		for (int i = 0; i < ChallengeCur.getImagesObjectList().size(); i++) {
			ImageObject rpo = ChallengeCur.getImagesObjectList().get(i);
			if (rpo.getId().equals(photoID)) {
				ChallengeCur.getImagesObjectList().get(i).getCommentList()
						.add(comment);

			}
		}
	}

	public void setGone() {
		btnWithraw.setVisibility(View.GONE);
		ivShare.setVisibility(View.GONE);
	}

	public PayPalPayment challengePayment(String PaymentIntent) {
		PayPalPayment payment = new PayPalPayment(new BigDecimal(
				ChallengeCur.getReward()), "USD", TvTitle_Challenge.getText()
				.toString(), PaymentIntent);

		return payment;

	}

	public void getBuyPress() {
		PayPalPayment thingToBuy = challengePayment(PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent = new Intent(context, PaymentActivity.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		startActivityForResult(intent, Config.REQUEST_CODE_PAYMENT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		// case Config.REQUEST_CODE_PAYMENT:
		// if (resultCode == Activity.RESULT_OK) {
		// PaymentConfirmation confirm = data
		// .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
		// if (confirm != null) {
		// try {
		// Log.i(Config.TAG, confirm.toJSONObject().toString(4));
		// Log.i(Config.TAG, confirm.getPayment().toJSONObject()
		// .toString(4));
		// /**
		// * TODO: send 'confirm' (and possibly
		// * confirm.getPayment() to your server for verification
		// * or consent completion. See
		// * https://developer.paypal.com
		// * /webapps/developer/docs/integration
		// * /mobile/verify-mobile-payment/ for more details.
		// *
		// * For sample mobile backend interactions, see
		// * https://github
		// * .com/paypal/rest-api-sdk-python/tree/master
		// * /samples/mobile_backend
		// */
		//
		// ChallengeCur.setIspaid(1);
		// btnRepaid.setVisibility(View.GONE);
		// Toast.makeText(
		// context.getApplicationContext(),
		// "PaymentConfirmation info received from PayPal",
		// Toast.LENGTH_LONG).show();
		// }
		// }
		// }
		//
		case Config.REQUEST_CODE_PAYMENT:
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {

						JSONObject object1 = confirm.toJSONObject();
						JSONObject object2 = confirm.getPayment()
								.toJSONObject();
						String id_payment = ChallengeCur.getId();
						new SavePaypalHistoryAsynctacks(activity, id_payment,
								object1.toString(), object2.toString())
								.execute();

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
						ChallengeCur.setIspaid(1);
						btnRepaid.setVisibility(View.GONE);
						Toast.makeText(
								context.getApplicationContext(),
								"PaymentConfirmation info received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e(Config.TAG,
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(Config.TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(Config.TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
			break;
		case Config.REQUEST_CODE_FUTURE_PAYMENT:
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject()
								.toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(context.getApplicationContext(),
								"Future Payment code received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample",
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void onFuturePaymentPressed() {
		Intent intent = new Intent(context, PayPalFuturePaymentActivity.class);

		startActivityForResult(intent, Config.REQUEST_CODE_FUTURE_PAYMENT);
	}

	public void onFuturePaymentPurchasePressed() {
		// Get the Application Correlation ID from the SDK
		String correlationId = PayPalConfiguration
				.getApplicationCorrelationId(activity);

		Log.i("FuturePaymentExample", "Application Correlation ID: "
				+ correlationId);

		// TODO: Send correlationId and transaction details to your server for
		// processing with
		// PayPal...
		Toast.makeText(context.getApplicationContext(),
				"App Correlation ID received from SDK", Toast.LENGTH_LONG)
				.show();
	}

	public void addExplanatoryPhoto(Bitmap bitmap) {
		ImageView iv = new ImageView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.height = (int) (Config.screenHeight * 0.2);
		params.width = (int) (Config.screenHeight * 0.2 * bitmap.getWidth() / bitmap
				.getHeight());
		params.setMargins(0, 0, (int) (Config.screenWidth * 0.02), 0);
		iv.setLayoutParams(params);
		iv.setImageBitmap(bitmap);
		// .setImageDrawable(new BitmapDrawable(getResources(),
		// ExifUtil.rotateBitmap(path, bitmap)));
		llExplanatory.addView(iv);
	}

	public void addImageChallenge(String path) {
		// imagesList.add(path);
		ChallengeCur.getImagesList().add(path);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int i = v.getId();
		switch (i) {
		case R.id.btn_gray_add__detail:
			if (ChallengeCur.getImagesObjectList().size() > 9) {
				Toast.makeText(context,
						"You can only update maximum 10 photos",
						Toast.LENGTH_LONG).show();
				return;
			}

			// if (ChallengeCur.g.size() > 9) {
			// Toast.makeText(context,
			// "You can only update maximum 10 photos",
			// Toast.LENGTH_LONG).show();
			// return;
			// }
			DetailChallengeFragment.challengeIdForUpload = ChallengeCur.getId();
			((DetailChallengeActivity) context).callAddExplanatoryPhoto();
			break;
		case R.id.btn_withdraw:
			AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
			// Setting Dialog Title
			builder1.setTitle("Confirm Delete...");
			builder1.setMessage("Are you sure you want delete this?");
			// Setting Icon to Dialog
			builder1.setIcon(activity.getResources().getDrawable(
					R.drawable.ic_delete));
			builder1.setCancelable(true);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							new DeleteChallengeAsynctask(context)
									.execute(ChallengeCur.getId() + "");
							dialog.cancel();
							activity.finish();
						}
					});
			builder1.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alert11 = builder1.create();
			alert11.show();
			break;
		case R.id.btn_edit_description:
			final DialogEditDescription dialogedit = (DialogEditDescription) DialogFactory
					.getDialog(activity, DialogType.DIALOG_EDIT_DESCRIPTION);
			dialogedit.show();
			dialogedit.setCancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogedit.dismiss();
				}
			});
			dialogedit.setOkClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					EditText editText = (EditText) dialogedit
							.findViewById(R.id.et_description);
					String description = editText.getText().toString();
					new EditChallengeAsynctask(context).execute(
							ChallengeCur.getId(), Config.IdUser, description,
							"", "", "", "");
					dialogedit.dismiss();
				}
			});

			break;
		case R.id.btn_edit_reward:
			dialogeditReward = (DialogEditReward) DialogFactory.getDialog(
					activity, DialogType.DIALOG_EDIT_REWARD);
			dialogeditReward.show();
			dialogeditReward.setText(starting_day, starting_hour,
					starting_minute, duration_day, duration_hour,
					duration_minute, reward);
			dialogeditReward.setCancelClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogeditReward.dismiss();
				}
			});
			dialogeditReward.setOkClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String date = dialogeditReward.getStartTime();
					String time = dialogeditReward.getDurationTime();
					String bubget = dialogeditReward.getBudget();
					new EditChallengeAsynctask(context).execute(
							ChallengeCur.getId(), Config.IdUser, "", date,
							time, bubget, "");
					dialogeditReward.dismiss();
				}
			});
			break;
		case R.id.iv_report:
			DialogReportChallenge dialog = (DialogReportChallenge) DialogFactory
					.getDialog(activity, DialogType.DIALOG_REPORT);
			dialog.show();
			break;
		case R.id.iv_share_detail:
			if (FacebookUtils.currentSession == null)
				FacebookUtils.currentSession = Session
						.openActiveSessionFromCache(activity);
			if (FacebookUtils.currentSession != null
					&& FacebookUtils.currentSession.isOpened()) {
				FacebookUtils.publishFeedDialog(activity, ChallengeCur);
			} else {
				FacebookUtils.connectToFB(activity);
				FacebookUtils.shareLinkRunnable = new Runnable() {

					@Override
					public void run() {
						FacebookUtils.publishFeedDialog(activity, ChallengeCur);
					}
				};
			}

			break;
		case R.id.btn_ignore:
			final DialogConfirmIgnore dialogIgnore = (DialogConfirmIgnore) DialogFactory
					.getDialog(context, DialogType.DIALOG_CONFIRM_IGNORE);
			dialogIgnore.show();
			dialogIgnore.setCancelClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogIgnore.dismiss();
				}
			});
			dialogIgnore.setOkClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new IgnoreChallengeAsynctask(context).execute(
							Config.IdUser, ChallengeCur.getId() + "");
					dialogIgnore.dismiss();
					activity.finish();
				}
			});
			break;
		case R.id.iv_author_avatar:
			break;
		case R.id.btn_camera:
			DetailChallengeFragment.isRunCamera = true;
			DetailChallengeFragment.challengeIdForUpload = ChallengeCur.getId();
			try {
				((MyAreaActivity) context).callCamera();
			} catch (Exception ex) {
				((DetailChallengeActivity) context).callCamera();
			}
			break;
		case R.id.btn_upload:
			DetailChallengeFragment.challengeIdForUpload = ChallengeCur.getId();
			try {
				((MyAreaActivity) context).callUploadChallenge();
			} catch (Exception ex) {
				((DetailChallengeActivity) context).callUploadChallenge();
			}
			challengeUploadId = ChallengeCur.getId();
			break;
		case R.id.btn_accept:
			final DialogConfirmAccept dialogAccept = (DialogConfirmAccept) DialogFactory
					.getDialog(context, DialogType.DIALOG_CONFIRM_ACCEPT);
			dialogAccept.show();
			dialogAccept.setCancelClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogAccept.dismiss();
				}
			});
			dialogAccept.setOkClickListner(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!dialogAccept.isAcceptTerm()) {
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.must_agree_terms),
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						new AcceptChallengeAsynctask(context).execute(
								Config.IdUser, ChallengeCur.getId() + "");
						btnAccept.setVisibility(View.GONE);
						dialogAccept.dismiss();

					}
				}
			});

			break;

		case R.id.btnArrow_Explanatory_Photos:

			if (hs_explanatory_photos.getVisibility() == View.VISIBLE) {
				hs_explanatory_photos.setVisibility(View.GONE);
				btnArrow_Explanatory_Photos.setBackground(getResources()
						.getDrawable(R.drawable.arrow_down));
			} else {
				hs_explanatory_photos.setVisibility(View.VISIBLE);
				btnArrow_Explanatory_Photos.setBackground(getResources()
						.getDrawable(R.drawable.arrow_up));
			}
			break;
		case R.id.btn_repaid:
			if (UserInfo.getInstance().isIsdontShow() == false) {
				final DelayPayment dialogDelay = (DelayPayment) DialogFactory
						.getDialog(activity, DialogType.DIALOG_DELAY_PAYMENT);
				dialogDelay.show();
				dialogDelay.setDontShowClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dialogDelay.isIscheck() == true) {
							dialogDelay.setIscheck(false);
						} else {
							dialogDelay.setIscheck(true);
						}
						// TODO Auto-generated method stub
					}
				});
				dialogDelay.setCancelClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogDelay.dismiss();
					}
				});
				dialogDelay.setOkClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (dialogDelay.isIscheck() == true) {
							UserInfo.getInstance().setIsdontShow(true);
						} else {
							UserInfo.getInstance().setIsdontShow(false);
						}
						getBuyPress();
						dialogDelay.dismiss();
					}
				});
			} else {
				getBuyPress();
			}

			break;
		default:

			break;
		}
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
		activity.stopService(new Intent(activity, PayPalService.class));
		super.onDestroy();
	}

}
