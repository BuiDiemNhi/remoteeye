package remoteeyes.bsp.vn.adapter;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.facebook.Session;
import com.google.android.gms.internal.c;
import com.paypal.android.sdk.ac;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.squareup.picasso.Picasso;

import remateeyes.bsp.vn.dialog.DelayPayment;
import remateeyes.bsp.vn.dialog.DialogConfirmAccept;
import remateeyes.bsp.vn.dialog.DialogConfirmIgnore;
import remateeyes.bsp.vn.dialog.DialogFactory;
import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.asynctask.AcceptChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.ConvertAvatarURLAsynctask;
import remoteeyes.bsp.vn.asynctask.DeleteChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.IgnoreChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.fragments.ChallengeListFragment;
import remoteeyes.bsp.vn.fragments.DetailChallengeFragment;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.FacebookUtils;
import remoteeyes.bsp.vn.type.ColorChallengeType;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ChallengeListAdapter extends ArrayAdapter<ChallengeObject> {

	Activity activity;
	ArrayList<ChallengeObject> objList;
	int layoutId;
	Locale locales;
	public int reward;
	public String nameCha;
	public Button btnRepaid, btnAccept;
	ChallengeListFragment ChallengeListFragment = new ChallengeListFragment();

	public ChallengeListAdapter(Activity activity, int layoutId,
			ArrayList<ChallengeObject> objList) {
		super(activity, layoutId);
		this.activity = activity;
		this.objList = objList;
		this.layoutId = layoutId;
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
		Intent intent = new Intent(activity, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);

		activity.startService(intent);
	}

	public void appendItems(ArrayList<ChallengeObject> items) {
		this.objList.addAll(items);
		notifyDataSetChanged();
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

	private String getDurationTime(ChallengeObject cha) {
		String result = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm");
			Date startDate = simpleDateFormat.parse(cha.getStartTime());
			Date now = simpleDateFormat.parse(simpleDateFormat.format(Calendar
					.getInstance().getTime()));
			long passTime = now.getTime() - startDate.getTime();
			long totalTime = (cha.getDuration_day() * 24 * 60 * 60
					+ cha.getDuration_hour() * 60 * 60 + cha
					.getDuration_minute() * 60) * 1000;
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

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		convertView = activity.getLayoutInflater().inflate(layoutId, null);
		final ChallengeObject co = objList.get(index);
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
		Intent intent = new Intent(activity, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);

		activity.startService(intent);

		LinearLayout llAll = (LinearLayout) convertView
				.findViewById(R.id.ll_all_list);
		LinearLayout llLeft = (LinearLayout) convertView
				.findViewById(R.id.ll_left_list);
		LinearLayout llRight = (LinearLayout) convertView
				.findViewById(R.id.ll_right_list);
		// avatar
		ImageView ivAuthorAvatar = (ImageView) convertView
				.findViewById(R.id.iv_author_avatar_list);
		ResizeUtils.resizeImageView(ivAuthorAvatar,
				(int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		TextView tvStatusAvatar = (TextView) convertView
				.findViewById(R.id.tv_status_avatar_list);

		LinearLayout llImageAvatar = (LinearLayout) convertView
				.findViewById(R.id.ll_image_avatar_list);

		ImageView ivImgAvatar = (ImageView) convertView
				.findViewById(R.id.iv_image_avatar_list);
		ivImgAvatar.getLayoutParams().width = (int) (Config.screenWidth * 0.055);
		ivImgAvatar.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvNumImg = (TextView) convertView
				.findViewById(R.id.tv_num_image_avatar_list);

		ImageView ivDotAvatar = (ImageView) convertView
				.findViewById(R.id.iv_dot_avatar_list);
		ivDotAvatar.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDotAvatar.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ImageView ivAcceptedAvatar = (ImageView) convertView
				.findViewById(R.id.iv_accepted_avatar_list);
		ivAcceptedAvatar.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		ivAcceptedAvatar.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvNumAccepted = (TextView) convertView
				.findViewById(R.id.tv_num_accepted_avatar_list);

		// info1
		TextView tvName = (TextView) convertView
				.findViewById(R.id.tv_title_list);

		ImageView ivShare = (ImageView) convertView
				.findViewById(R.id.iv_share_list);
		ivShare.getLayoutParams().width = (int) (Config.screenWidth * 0.06);
		ivShare.getLayoutParams().height = (int) (Config.screenWidth * 0.06);

		// info2
		ImageView ivDistance = (ImageView) convertView
				.findViewById(R.id.iv_distance_list);
		ivDistance.getLayoutParams().width = (int) (Config.screenWidth * 0.035);
		ivDistance.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvDistance = (TextView) convertView
				.findViewById(R.id.tv_distance_list);

		ImageView ivDotList = (ImageView) convertView
				.findViewById(R.id.iv_dot_list);
		ivDotList.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDotList.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		TextView tvAddress = (TextView) convertView
				.findViewById(R.id.tv_address_list);
		tvAddress.setSelected(true);

		// info3
		ImageView ivCalendar = (ImageView) convertView
				.findViewById(R.id.iv_calendar_list);
		ivCalendar.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		ivCalendar.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvStart = (TextView) convertView
				.findViewById(R.id.tv_start_day_list);

		ImageView ivBell = (ImageView) convertView
				.findViewById(R.id.iv_bell_list);
		ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ImageView ivFlag = (ImageView) convertView
				.findViewById(R.id.iv_flag_list);
		ivFlag.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
		ivFlag.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvDuration = (TextView) convertView
				.findViewById(R.id.tv_duration_list);

		ImageView ivDot1 = (ImageView) convertView
				.findViewById(R.id.iv_dot_list_1);
		ivDot1.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDot1.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ImageView ivCup = (ImageView) convertView
				.findViewById(R.id.iv_cup_list);
		ivCup.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
		ivCup.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvBudget = (TextView) convertView
				.findViewById(R.id.tv_budget_list);

		ImageView ivDot2 = (ImageView) convertView
				.findViewById(R.id.iv_dot_list_2);
		ivDot2.getLayoutParams().width = (int) (Config.screenWidth * 0.01);
		ivDot2.getLayoutParams().height = (int) (Config.screenWidth * 0.01);

		ImageView ivClockExpired = (ImageView) convertView
				.findViewById(R.id.iv_clock_expired_list);
		ivClockExpired.getLayoutParams().width = (int) (Config.screenWidth * 0.05);
		ivClockExpired.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

		TextView tvDurationExpired = (TextView) convertView
				.findViewById(R.id.tv_duration_expired_list);

		// info4
		btnRepaid = (Button) convertView.findViewById(R.id.btn_repaid_list);
		btnRepaid.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnRepaid.getLayoutParams().width = (int) (Config.screenWidth * 0.2);
		btnAccept = (Button) convertView.findViewById(R.id.btn_accept_list);
		btnAccept.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnAccept.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		Button btnUpload = (Button) convertView
				.findViewById(R.id.btn_upload_list);
		btnUpload.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnUpload.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		Button btnRunCamera = (Button) convertView
				.findViewById(R.id.btn_camera_list);
		btnRunCamera.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnRunCamera.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		Button btnIgnore = (Button) convertView
				.findViewById(R.id.btn_ignore_list);
		btnIgnore.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnIgnore.getLayoutParams().width = (int) (Config.screenWidth * 0.2);

		Button btnWithraw = (Button) convertView
				.findViewById(R.id.btn_withdraw_list);
		btnWithraw.getLayoutParams().height = (int) (Config.screenHeight * 0.04);
		btnWithraw.getLayoutParams().width = (int) (Config.screenWidth * 0.22);

		// SHOW ITEM SPECIAL
		reward = co.getReward();
		nameCha = tvName.getText().toString();
		if (co.getId().equals("-1")) {
			llLeft.getLayoutParams().height = (int) (Config.screenWidth * 0.17);
			llRight.getLayoutParams().height = (int) (Config.screenWidth * 0.17);
			llAll.setVisibility(View.INVISIBLE);

		}

		// SHOW INFO
		if (co.getiPublic() == 1) {
			Picasso.with(activity).load(co.getAvatar())
					.transform(new CircleTransform()).into(ivAuthorAvatar);
		} else {
			ivAuthorAvatar.setBackground(activity.getResources().getDrawable(
					R.drawable.no_avatar));
		}
		if (co.isMine()) {
			btnAccept.setVisibility(View.GONE);
			btnIgnore.setVisibility(View.GONE);
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_gray_location));
			if (!co.isGlobal() && ivDotList.getVisibility() == View.GONE) {
				ivDistance.setImageDrawable(activity.getResources()

				.getDrawable(R.drawable.ic_gray_area));
			}
			if (co.isGlobal()) {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_earth));
			}
			if (!co.isGlobal()) {
			
			if (co.getIspaid() == 0 && co.getReward() > 0) {
				btnRepaid.setVisibility(View.VISIBLE);
			} else {
				btnRepaid.setVisibility(View.GONE);
			}
		}

		if (co.isMine() && co.getCountAccept() == 0) {
			if (co.getiPublic() == 1) {
				tvStatusAvatar.setText(activity.getResources().getString(
						R.string.status_avatar_public));
				llImageAvatar.setVisibility(View.GONE);
				tvStatusAvatar.setVisibility(View.VISIBLE);

			} else {
				tvStatusAvatar.setText(activity.getResources().getString(
						R.string.status_avatar_private));
				llImageAvatar.setVisibility(View.GONE);
				ivShare.setVisibility(View.INVISIBLE);
			}

		}
		if (co.isMine() && co.getCountAccept() != 0) {
			ivAuthorAvatar.setClickable(false);
			if (co.getiPublic() == 1) {
				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);

			} else {

				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);
				ivShare.setVisibility(View.INVISIBLE);
			}

		}
		if (!co.isMine()) {
			if (co.getiPublic() == 1) {
				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);

			} else {

				llImageAvatar.setVisibility(View.VISIBLE);
				tvStatusAvatar.setVisibility(View.GONE);
				ivShare.setVisibility(View.INVISIBLE);

			}
		}
		if (co.getIsExpired() == 1) {
			btnRepaid.setVisibility(View.GONE);
		}
		tvNumImg.setText(co.getCountImage() + "");
		tvNumAccepted.setText(co.getCountAccept() + "");
		tvName.setText(co.getTitle());

		int distance = co.getLocationsList().get(0).getDistance();

		if (distance > 1000) {
			tvDistance.setText((float) Math.round((float) distance * 10 / 1000)
					/ 10 + " km");
		} else {
			tvDistance.setText(distance + " m");
		}

		tvStart.setText(co.getRemainStart());
		tvDuration.setText(co.getInterval());
		tvBudget.setText(String.format(
				activity.getResources().getString(R.string.dola_sign_reward),
				co.getReward()));
		tvDurationExpired.setText(co.getInterval());

		// if (co.getiPublic() == 1) {
		// tvStatusAvatar.setVisibility(View.GONE);
		// } else {
		// tvStatusAvatar.setText(activity.getResources().getString(
		// R.string.status_avatar_private));
		// llImageAvatar.setVisibility(View.GONE);
		// ivShare.setVisibility(View.INVISIBLE);
		//
		// }

		if (co.getLocationsList().get(0).getAddress().equals("")) {
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_gray_area));
			ivDotList.setVisibility(View.GONE);
			tvDistance.setVisibility(View.GONE);
			tvAddress.setText(co.getLocationsList().get(0).getArea());
		} else {
			tvAddress.setText(co.getLocationsList().get(0).getAddress());
		}

		// SHOW GLOBAL
		if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {

			ivShare.setVisibility(View.INVISIBLE);
			ivDotList.setVisibility(View.GONE);
			tvDistance.setVisibility(View.GONE);
			tvAddress.setText(activity.getResources()
					.getString(R.string.global));

			// expired
			if (co.getIsExpired() == 1) {
				ivCalendar.setVisibility(View.GONE);
				tvStart.setVisibility(View.GONE);
				ivBell.setVisibility(View.GONE);
				ivFlag.setVisibility(View.GONE);
				tvDuration.setVisibility(View.GONE);
				ivDot1.setVisibility(View.GONE);
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_earth));
				ivCup.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_black_cup));
				tvBudget.setTextColor(activity.getResources().getColor(
						R.color.black));
				ivImgAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_black_img));
				ivAcceptedAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_small_black_check));
				btnRepaid.setVisibility(View.GONE);
			} // unexpired
			else {

				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_earth));
				ivClockExpired.setVisibility(View.GONE);
				tvDurationExpired.setVisibility(View.GONE);
				ivDot2.setVisibility(View.GONE);
				// ongoing

				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_blue_earth));
				if (co.isMine()) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));
				}
				if (co.getRemainStart().equals("")) {
					ivCalendar.setVisibility(View.GONE);
					tvStart.setVisibility(View.GONE);
					ivBell.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_bell));
					ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
					ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

					ivFlag.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_flag));
					if (!co.isMine()) {
						if (co.getAccept() == ColorChallengeType.ACCEPTED_YET) {

							btnAccept.setVisibility(View.VISIBLE);
							btnIgnore.setVisibility(View.VISIBLE);

						} else if (co.getAccept() == ColorChallengeType.ACCEPTED) {
							if (co.getWinner_id() != 0) {
								btnRunCamera.setVisibility(View.GONE);
								btnUpload.setVisibility(View.GONE);
								if (UserInfo.getInstance().getId()
										.equals(co.getWinner_id())) {
									btnIgnore.setVisibility(View.GONE);
								} else {
									btnIgnore.setVisibility(View.VISIBLE);
								}
							} else {
								btnRunCamera.setVisibility(View.VISIBLE);
								btnUpload.setVisibility(View.VISIBLE);
								btnIgnore.setVisibility(View.VISIBLE);
							}
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_blue_earth));
						}
						if (UserInfo.getInstance().getId()
								.equals(co.getWinner_id())) {
							btnIgnore.setVisibility(View.GONE);
						} else {
							btnIgnore.setVisibility(View.VISIBLE);
						}
					}

				}// not ongoing
				else {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));

					tvDuration.setText(co.getDuration());
					if (co.isMine()) {
						btnWithraw.setVisibility(View.VISIBLE);
					} else {
						if (co.getAccept() == ColorChallengeType.ACCEPTED_YET) {

							btnAccept.setVisibility(View.VISIBLE);

						} else if (co.getAccept() == ColorChallengeType.ACCEPTED)

							btnAccept.setVisibility(View.GONE);

						btnIgnore.setVisibility(View.VISIBLE);

					}
				}
			}
		}// SHOW ACCEPTED
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
			// share

			if (co.getIsShare() == 1 || co.isGlobal()) {
				ivShare.setVisibility(View.INVISIBLE);
			}

			// expired
			if (co.getIsExpired() == 1) {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_location));
				if (!co.isGlobal() && ivDotList.getVisibility() == View.GONE) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_area));
				}
				if (co.isGlobal()) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));
					ivDotList.setVisibility(View.GONE);
					tvDistance.setVisibility(View.GONE);
					tvAddress.setText(activity.getResources().getString(
							R.string.global));
				}
				ivCalendar.setVisibility(View.GONE);
				tvStart.setVisibility(View.GONE);
				ivBell.setVisibility(View.GONE);
				ivFlag.setVisibility(View.GONE);
				tvDuration.setVisibility(View.GONE);
				ivDot1.setVisibility(View.GONE);
				ivCup.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_black_cup));
				tvBudget.setTextColor(activity.getResources().getColor(
						R.color.black));
				tvDurationExpired.setText(co.getInterval());
				ivImgAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_black_img));
				ivAcceptedAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_small_black_check));
			} // unexpired
			else {
				if (co.isGlobal()) {
					ivDotList.setVisibility(View.GONE);
					tvDistance.setVisibility(View.GONE);
					tvAddress.setText(activity.getResources().getString(
							R.string.global));
				}
				ivClockExpired.setVisibility(View.GONE);
				tvDurationExpired.setVisibility(View.GONE);
				ivDot2.setVisibility(View.GONE);
				// ongoing

				if (co.getRemainStart().equals("")) {

					ivCalendar.setVisibility(View.GONE);
					tvStart.setVisibility(View.GONE);

					if (co.isGlobal()) {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_blue_earth));

					}

					if (!co.isGlobal()) {
						if (ivDotList.getVisibility() == View.GONE) {
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_pink_area));
						} else
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_pink_location));
					} else {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_blue_earth));
					}
					ivBell.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_bell));
					ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
					ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.045);
					ivFlag.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_flag));
					if (co.getAccept() == ColorChallengeType.ACCEPTED) {
						if ((distance > 4800 && !co.isGlobal())
								|| (co.getWinner_id() != 0)) {
							btnRunCamera.setVisibility(View.GONE);
							btnUpload.setVisibility(View.GONE);
							if (UserInfo.getInstance().getId()
									.equals(co.getWinner_id())) {
								btnIgnore.setVisibility(View.GONE);
							}

						}

						else {
							btnRunCamera.setVisibility(View.VISIBLE);
							btnUpload.setVisibility(View.VISIBLE);
							btnIgnore.setVisibility(View.VISIBLE);
						}
						if (!co.isGlobal()) {
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_blue_location));
							if (ivDotList.getVisibility() == View.GONE) {
								ivDistance.setImageDrawable(activity
										.getResources().getDrawable(
												R.drawable.ic_blue_area));
							}
						} else {
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_blue_earth));
						}

					}
					if (UserInfo.getInstance().getId()
							.equals(co.getWinner_id())) {
						btnIgnore.setVisibility(View.GONE);
					} else {
						btnIgnore.setVisibility(View.VISIBLE);
					}

				}// not ongoing
				else {

					tvDuration.setText(co.getDuration());

					if (!co.isGlobal()) {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_gray_location));
						if (ivDotList.getVisibility() == View.GONE) {
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_gray_area));
						}
					} else {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_gray_earth));
					}

					btnIgnore.setVisibility(View.VISIBLE);

				}
			}
		}// SHOW POSTED
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
			// share

			if (co.getIsShare() == 1) {
				ivShare.setVisibility(View.INVISIBLE);
				btnWithraw.setVisibility(View.INVISIBLE);
			}

			if (co.isGlobal()) {
				ivShare.setVisibility(View.INVISIBLE);
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_earth));
			}
			// expired
			if (co.getIsExpired() == 1) {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_location));
				if (!co.isGlobal() && ivDotList.getVisibility() == View.GONE) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_area));
				}
				if (co.isGlobal()) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));
					ivDotList.setVisibility(View.GONE);
					tvDistance.setVisibility(View.GONE);
					tvAddress.setText(activity.getResources().getString(
							R.string.global));
				}
				ivCalendar.setVisibility(View.GONE);
				tvStart.setVisibility(View.GONE);
				ivBell.setVisibility(View.GONE);
				ivFlag.setVisibility(View.GONE);
				tvDuration.setVisibility(View.GONE);
				ivDot1.setVisibility(View.GONE);
				ivCup.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_black_cup));
				tvBudget.setTextColor(activity.getResources().getColor(
						R.color.black));
				tvDurationExpired.setText(co.getInterval());
				ivImgAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_black_img));
				ivAcceptedAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_small_black_check));
			} // unexpired
			else {
				if (co.isGlobal()) {

					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_earth));
					ivDotList.setVisibility(View.GONE);
					tvDistance.setVisibility(View.GONE);
					tvAddress.setText(activity.getResources().getString(
							R.string.global));
				}
				ivClockExpired.setVisibility(View.GONE);
				tvDurationExpired.setVisibility(View.GONE);
				ivDot2.setVisibility(View.GONE);
				// ongoing
				if (co.getRemainStart().equals("")) {

					if (!co.isGlobal()) {

						if (ivDotList.getVisibility() == View.GONE)
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_gray_area));
						else
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_gray_location));
					}
					ivCalendar.setVisibility(View.GONE);
					tvStart.setVisibility(View.GONE);
					ivBell.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_bell));
					ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
					ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.045);
					ivFlag.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_flag));
				}// not ongoing
				else {

					tvDuration.setText(co.getDuration());
					if (co.isGlobal()) {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_gray_earth));
					} else if (!co.isGlobal()
							&& ivDotList.getVisibility() == View.GONE) {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_gray_area));
					} else {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_gray_location));
					}
					btnWithraw.setVisibility(View.VISIBLE);
				}
			}

		} // SHOW NEARBY
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
			// share
			if (co.getiPublic() == 1) {
				Picasso.with(activity).load(co.getAvatar())
						.transform(new CircleTransform()).into(ivAuthorAvatar);
			} else {
				ivAuthorAvatar.setBackground(activity.getResources()
						.getDrawable(R.drawable.no_avatar));
			}

			if (co.getIsShare() == 1) {
				ivShare.setVisibility(View.INVISIBLE);
			}
			// expired
			if (co.getIsExpired() == 1) {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_location));
				if (!co.isGlobal() && ivDotList.getVisibility() == View.GONE) {
					ivDistance.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_gray_area));
				}
				ivCalendar.setVisibility(View.GONE);
				tvStart.setVisibility(View.GONE);
				ivBell.setVisibility(View.GONE);
				ivFlag.setVisibility(View.GONE);
				tvDuration.setVisibility(View.GONE);
				ivDot1.setVisibility(View.GONE);
				ivCup.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_black_cup));
				tvBudget.setTextColor(activity.getResources().getColor(
						R.color.black));
				ivImgAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_black_img));
				ivAcceptedAvatar.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_small_black_check));
			} // unexpired
			else {

				ivClockExpired.setVisibility(View.GONE);
				tvDurationExpired.setVisibility(View.GONE);
				ivDot2.setVisibility(View.GONE);
				// ongoing
				if (co.getRemainStart().equals("")) {

					if (!co.isGlobal()) {

						if (ivDotList.getVisibility() == View.GONE) {
							if (!co.isMine()) {
								ivDistance.setImageDrawable(activity
										.getResources().getDrawable(
												R.drawable.ic_pink_area));
							} else {
								ivDistance.setImageDrawable(activity
										.getResources().getDrawable(
												R.drawable.ic_gray_area));
							}
						} else {
							if (!co.isMine()) {
								ivDistance.setImageDrawable(activity
										.getResources().getDrawable(
												R.drawable.ic_pink_location));
							} else {
								ivDistance.setImageDrawable(activity
										.getResources().getDrawable(
												R.drawable.ic_gray_location));
							}
						}
					} else {
						ivDistance.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.ic_blue_earth));
						if (co.isMine()) {
							ivDistance.setImageDrawable(activity.getResources()
									.getDrawable(R.drawable.ic_gray_earth));
						}
					}

					ivCalendar.setVisibility(View.GONE);
					tvStart.setVisibility(View.GONE);
					ivBell.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_bell));
					ivBell.getLayoutParams().width = (int) (Config.screenWidth * 0.045);
					ivBell.getLayoutParams().height = (int) (Config.screenWidth * 0.045);

					ivFlag.setImageDrawable(activity.getResources()
							.getDrawable(R.drawable.ic_pink_flag));
					if (!co.isMine()) {
						if (co.getAccept() == ColorChallengeType.ACCEPTED_YET) {

							btnAccept.setVisibility(View.VISIBLE);
							btnIgnore.setVisibility(View.VISIBLE);

						}
						if (co.getAccept() == ColorChallengeType.ACCEPTED) {
							btnAccept.setVisibility(View.GONE);
							if (distance > 4800 || co.getWinner_id() != 0) {

								btnRunCamera.setVisibility(View.GONE);
								btnUpload.setVisibility(View.GONE);
								if (UserInfo.getInstance().getId()
										.equals(co.getWinner_id())) {
									btnIgnore.setVisibility(View.GONE);
								}
							} else {
								btnRunCamera.setVisibility(View.VISIBLE);
								btnUpload.setVisibility(View.VISIBLE);
								btnIgnore.setVisibility(View.VISIBLE);
							}
							if (!co.isGlobal()) {
								if (ivDotList.getVisibility() == View.GONE) {
									if (co.isMine()) {
										ivDistance
												.setImageDrawable(activity
														.getResources()
														.getDrawable(
																R.drawable.ic_gray_area));
									} else {
										ivDistance
												.setImageDrawable(activity
														.getResources()
														.getDrawable(
																R.drawable.ic_blue_area));
									}
								} else {
									if (co.isMine()) {
										ivDistance
												.setImageDrawable(activity
														.getResources()
														.getDrawable(
																R.drawable.ic_gray_location));
									} else {
										ivDistance
												.setImageDrawable(activity
														.getResources()
														.getDrawable(
																R.drawable.ic_blue_location));
									}
								}
							} else {
								if (co.isMine()) {
									ivDistance.setImageDrawable(activity
											.getResources().getDrawable(
													R.drawable.ic_gray_earth));
								} else {
									ivDistance.setImageDrawable(activity
											.getResources().getDrawable(
													R.drawable.ic_blue_earth));
								}
							}
							if (UserInfo.getInstance().getId()
									.equals(co.getWinner_id())) {
								btnIgnore.setVisibility(View.GONE);
							} else {
								btnIgnore.setVisibility(View.VISIBLE);
							}
						}
					}
				}

			}
		}
		// not ongoing
		else {
			tvDuration.setText(co.getDuration());
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_gray_location));
			if (!co.isGlobal() && ivDotList.getVisibility() == View.GONE) {
				ivDistance.setImageDrawable(activity.getResources()

				.getDrawable(R.drawable.ic_gray_area));
			}
			if (co.isGlobal()) {
				ivDistance.setImageDrawable(activity.getResources()
						.getDrawable(R.drawable.ic_gray_earth));
			}

			if (co.isMine()) {
				btnWithraw.setVisibility(View.VISIBLE);
				ivDistance.setImageDrawable(activity.getResources().getDrawable(
						R.drawable.ic_gray_location));
				if (!co.isGlobal() && ivDotList.getVisibility() == View.GONE) {
					ivDistance.setImageDrawable(activity.getResources()

					.getDrawable(R.drawable.ic_gray_area));
				}
				if (co.getIsShare() == 1) {
					btnWithraw.setVisibility(View.INVISIBLE);
				}
			} else {
				if (co.getAccept() == ColorChallengeType.ACCEPTED_YET) {
					btnAccept.setVisibility(View.VISIBLE);
					btnIgnore.setVisibility(View.VISIBLE);
				} else if (co.getAccept() == ColorChallengeType.ACCEPTED)
					btnIgnore.setVisibility(View.VISIBLE);
			}
		}

		// BUTTON
		btnWithraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
								new DeleteChallengeAsynctask(activity)
										.execute(co.getId() + "");
								dialog.cancel();
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
			}
		});

		ivShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FacebookUtils.currentSession == null)
					FacebookUtils.currentSession = Session
							.openActiveSessionFromCache(activity);
				if (FacebookUtils.currentSession == null
						|| !FacebookUtils.currentSession.isOpened()) {
					FacebookUtils.connectToFB(activity);
					FacebookUtils.shareLinkRunnable = new Runnable() {

						@Override
						public void run() {
							FacebookUtils.publishFeedDialog(activity, co);
						}
					};
				} else
					FacebookUtils.publishFeedDialog(activity, co);
			}
		});

		btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final DialogConfirmAccept dialogAccept = (DialogConfirmAccept) DialogFactory
						.getDialog(activity, DialogType.DIALOG_CONFIRM_ACCEPT);
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
									activity,
									activity.getResources().getString(
											R.string.must_agree_terms),
									Toast.LENGTH_SHORT).show();
							return;
						}
						btnAccept.setVisibility(View.INVISIBLE);
						new AcceptChallengeAsynctask(activity).execute(
								Config.IdUser, co.getId() + "");

						dialogAccept.dismiss();

					}
				});
			}
		});

		btnRunCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DetailChallengeFragment.isRunCamera = true;
				DetailChallengeFragment.challengeIdForUpload = co.getId();
				Intent intentCamera = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				Config.fileUri = Uri.fromFile(getOutputPhotoFile());
				intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Config.fileUri);
				activity.startActivityForResult(intentCamera,
						Config.CAPTURE_IMAGE_ACTIVITY_REQ);
			}
		});

		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DetailChallengeFragment.challengeIdForUpload = co.getId();
				Intent intent = new Intent();
				intent.setType("*/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				activity.startActivityForResult(intent, Config.UPLOAD_CHALLENGE);
			}
		});

		btnIgnore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final DialogConfirmIgnore dialogIgnore = (DialogConfirmIgnore) DialogFactory
						.getDialog(activity, DialogType.DIALOG_CONFIRM_IGNORE);
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
						new IgnoreChallengeAsynctask(activity).execute(
								Config.IdUser, co.getId() + "");
						dialogIgnore.dismiss();
					}
				});
			}
		});
		btnRepaid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DetailChallengeFragment.challengeIdForUpload = co.getId();
				reward = co.getReward();
				nameCha = co.getTitle().toString();
				if (UserInfo.getInstance().isIsdontShow() == false) {
					final DelayPayment dialogDelay = (DelayPayment) DialogFactory
							.getDialog(activity,
									DialogType.DIALOG_DELAY_PAYMENT);
					dialogDelay.show();
					dialogDelay.setDontShowClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (dialogDelay.isIscheck() == true) {
								dialogDelay.setIscheck(false);
							} else {
								dialogDelay.setIscheck(true);
							}
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

			}
		});}
		return convertView;
		
	}

	public PayPalPayment challengePayment(String PaymentIntent) {
		PayPalPayment payment = new PayPalPayment(new BigDecimal(reward),
				"USD", nameCha, PaymentIntent);

		return payment;

	}

	public void getBuyPress() {
		PayPalPayment thingToBuy = challengePayment(PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent = new Intent(activity, PaymentActivity.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		activity.startActivityForResult(intent,
				Config.REQUEST_CODE_PAYMENT_area);

	}

	public void onFuturePaymentPressed() {
		Intent intent = new Intent(activity, PayPalFuturePaymentActivity.class);

		activity.startActivityForResult(intent,
				Config.REQUEST_CODE_FUTURE_PAYMENT_area);
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
		Toast.makeText(activity.getApplicationContext(),
				"App Correlation ID received from SDK", Toast.LENGTH_LONG)
				.show();
	}

	private File getOutputPhotoFile() {

		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"RemoteEyes");
		if (!dir.exists()) {
			dir.mkdir();
		}
		String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", locales.UK)
				.format(new Date());
		return new File(dir.getPath() + File.separator + "IMG_" + timeStamp
				+ ".jpg");
	}

	@Override
	public int getCount() {
		return objList.size();
	}

	@Override
	public ChallengeObject getItem(int position) {
		return objList.get(position);
	}

}
