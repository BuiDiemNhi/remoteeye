package remoteeyes.bsp.vn.custom.view;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.LocationObject;
import remoteeyes.bsp.vn.type.ColorChallengeType;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BubbleChallenge extends RelativeLayout implements OnClickListener {

	Context context;
	private double lat, lng;
	float reward;
	private LocationObject location;
	TextView tvBudget, tvTime, tvTitle;
	RelativeLayout rlBubbleChallenge;
	int type;
	private String challengeId;
	private int starting_on_year;
	private int starting_on_month;
	private int starting_on_day;
	private boolean isFocus;
	private int starting_on_hour;
	private int starting_on_minute;
	private int starting_on_second;
	private int duration_day;
	private int duration_hour;
	private int duration_minute;
	private int accept;
	private int publics;
	private int isExpired;
	private String title;
	private String interval;
	private String userID;
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(int isExpired) {
		this.isExpired = isExpired;
	}

	public LocationObject getLocation() {
		return location;
	}

	public void setLocation(LocationObject location) {
		this.location = location;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
	}

	public int getAccept() {
		return accept;
	}

	public void setAccept(int accept) {
		this.accept = accept;
	}

	public int getPublics() {
		return publics;
	}

	public void setPublics(int publics) {
		this.publics = publics;
	}

	public int getStarting_on_year() {
		return starting_on_year;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public void setStarting_on_year(int starting_on_year) {
		this.starting_on_year = starting_on_year;
	}

	public int getStarting_on_month() {
		return starting_on_month;
	}

	public void setStarting_on_month(int starting_on_month) {
		this.starting_on_month = starting_on_month;
	}

	public int getStarting_on_day() {
		return starting_on_day;
	}

	public void setStarting_on_day(int starting_on_day) {
		this.starting_on_day = starting_on_day;
	}

	public int getStarting_on_hour() {
		return starting_on_hour;
	}

	public void setStarting_on_hour(int starting_on_hour) {
		this.starting_on_hour = starting_on_hour;
	}

	public int getStarting_on_minute() {
		return starting_on_minute;
	}

	public void setStarting_on_minute(int starting_on_minute) {
		this.starting_on_minute = starting_on_minute;
	}

	public int getStarting_on_second() {
		return starting_on_second;
	}

	public void setStarting_on_second(int starting_on_second) {
		this.starting_on_second = starting_on_second;
	}

	public int getDuration_day() {
		return duration_day;
	}

	public void setDuration_day(int duration_day) {
		this.duration_day = duration_day;
	}

	public int getDuration_hour() {
		return duration_hour;
	}

	public void setDuration_hour(int duration_hour) {
		this.duration_hour = duration_hour;
	}

	public int getDuration_minute() {
		return duration_minute;
	}

	public void setDuration_minute(int duration_minute) {
		this.duration_minute = duration_minute;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}
	
	public BubbleChallenge(final Context context, ChallengeObject co) {
		super(context);

		this.context = context;
		this.lat = co.getLocationsList().get(0).getLat();
		this.lng = co.getLocationsList().get(0).getLng();
		this.reward = co.getReward();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		if (inflater != null)
			view = inflater.inflate(R.layout.marker_bubble_challenge, this);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);

		tvBudget = (TextView) view.findViewById(R.id.tv_bubble_budget);
		tvTime = (TextView) view.findViewById(R.id.tv_bubble_time);
		tvTitle = (TextView) view.findViewById(R.id.tv_bubble_title);

		rlBubbleChallenge = (RelativeLayout) view
				.findViewById(R.id.rl_bubble_challenge);
		rlBubbleChallenge.setOnClickListener(this);
		focusBubble();
	}

	public float getReward() {
		return reward;
	}

	public void setReward(float reward) {
		this.reward = reward;
	}

	public BubbleChallenge(final Context context) {
		super(context);

		this.context = context;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		if (inflater != null)
			view = inflater.inflate(R.layout.marker_bubble_challenge, this);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);

		tvBudget = (TextView) view.findViewById(R.id.tv_bubble_budget);
		tvTime = (TextView) view.findViewById(R.id.tv_bubble_time);
		tvTitle = (TextView) view.findViewById(R.id.tv_bubble_title);
		rlBubbleChallenge = (RelativeLayout) view
				.findViewById(R.id.rl_bubble_challenge);
		rlBubbleChallenge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MyAreaActivity) context).callDetailChallengeFragment();
			}
		});
	}

	public Bitmap getBubbleChallengeBitmap() {
		RelativeLayout rlBubbleChallenge = (RelativeLayout) findViewById(R.id.rl_bubble_challenge);
		rlBubbleChallenge.setDrawingCacheEnabled(true);
		rlBubbleChallenge.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		rlBubbleChallenge.layout(0, 0, rlBubbleChallenge.getMeasuredWidth(),
				rlBubbleChallenge.getMeasuredHeight());
		rlBubbleChallenge.buildDrawingCache(true);
		Bitmap bitmap = Bitmap
				.createBitmap(rlBubbleChallenge.getDrawingCache());
		rlBubbleChallenge.setDrawingCacheEnabled(false);
		return bitmap;
	}

	public void setColorBubble(int type) {
		this.type = type;
		if (type == ColorChallengeType.MINE) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.mine_red));
		} else if (type == ColorChallengeType.MINE_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.mine_red_dot));
		} else if (type == ColorChallengeType.MINE_EXPIRED) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.mine_gray));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.MINE_EXPIRED_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.mine_gray_dot));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.ACCEPTED) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.accepted_green));
		} else if (type == ColorChallengeType.ACCEPTED_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.accepted_green_dot));
		} else if (type == ColorChallengeType.ACCEPTED_EXPIRED) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.accepted_gray));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.ACCEPTED_EXPIRED_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.accepted_gray_dot));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.ACCEPTED_YET) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.not_accepted_blue));
		} else if (type == ColorChallengeType.ACCEPTED_YET_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.not_accepted_blue_dot));
		} else if (type == ColorChallengeType.ACCEPTED_YET_EXPIRED) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.not_accepted_gray));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.ACCEPTED_YET_EXPIRED_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.not_accepted_gray_dot));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.IGNORED) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.ignored_white));
		} else if (type == ColorChallengeType.IGNORED_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.ignored_white_dot));
		} else if (type == ColorChallengeType.IGNORED_EXPIRED) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.ignored_gray));
			tvBudget.setTextColor(getResources().getColor(
					R.color.black));
		} else if (type == ColorChallengeType.IGNORED_EXPIRED_MULTILOCATION) {
			rlBubbleChallenge.setBackground(getResources().getDrawable(
					R.drawable.ignored_gray_dot));
			tvBudget.setTextColor(getResources().getColor(
			R.color.black));
		}

	}

	public void setMessage(String Budget, String Time, String Title) {
		tvBudget.setText(Budget);
		tvTime.setText(Time);
		tvTitle.setText("''"+ Title +"''");
	}

	public void focusBubble() {
		if (isFocus){
			tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_size_bubble_focus));
			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_size_bubble_focus));
			tvBudget.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_size_bubble_focus));
		}
		else{
			tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_size_bubble_not_focus));
			tvBudget.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.text_size_bubble_not_focus));
		}
	}

	public String getDuration() {
		String result = "";
		if (duration_day > 0)
			result += duration_day + "d ";
		if (duration_hour > 0)
			result += duration_hour + "h ";
		if (duration_minute > 0)
			result += duration_minute + "m";
		return result;
	}

	public String getStartDay() {
		return starting_on_year + "-" + starting_on_month + "-"
				+ starting_on_day;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_bubble_challenge:

			break;
		default:
			break;
		}

	}
}
