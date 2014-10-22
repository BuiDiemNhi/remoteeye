package remoteeyes.bsp.vn.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogChallengePrivacy;
import remateeyes.bsp.vn.dialog.DialogFactory;
import remoteeyes.bsp.vn.CreateChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.TermsAndConditionsActivity;
import remoteeyes.bsp.vn.adapter.DayArrayAdapter;
import remoteeyes.bsp.vn.asynctask.CreateChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.DeleteChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadCategory;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.Get_LatLong_FromAddress;
import remoteeyes.bsp.vn.common.PlaceDetailsJSONParser;
import remoteeyes.bsp.vn.common.PlaceJSONParser;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.LocationObject;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.internal.ac;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class CreateChallengeFragment extends Fragment implements
		OnClickListener {
	public AutoCompleteTextView atvPlaces;

	DownloadTask placesDownloadTask;
	DownloadTask placeDetailsDownloadTask;
	ParserTask placesParserTask;
	ParserTask placeDetailsParserTask;
	ImageView ivBlueEye, ivQuoteOpen, ivQuoteClose, ivLocationShawdow,
			ivTimeShawdow, ivDetailLocatonShadow, ivDistance, ivCalendar,
			ivDot1, ivFlag, ivDot2, ivCup, ivAddress, ivArea, ivC, ivF, ivR;
	Button btnSubmit, btnCancel, btnGrayAdd, btnLocationOk, btnLocationCancel,
			btnTimeCancel, btnTimeOk, btnAllAboveCancel, btnAllAboveOk,
			btnDetailLocationOk, btnDetailLocationLeft, btnDetailLocationRight,
			btnEditLocation, btnEditReward, btnCharge, btnSearch;
	EditText etDescription, etTitleChallenge, etDurationDay, etDurationHour,
			etDurationMinute, etTimeReward;
	public EditText etArea;
	// public EditText tvAddress;
	HorizontalScrollView hsExplanatoryPhotos;
	TextView tvDialogLocationTitle, tvStartDay, tvStartHour, tvStartMinute,
			tvLocation, tvBalance, tvDetailCountry, tvDetailCity,
			tvDetailAddress, tvTerms, tvStartingIn, tvDuration, tvReward,
			tvPleaseSelect, tvStatus, tvGlobal, tvAdressLable;// tvLine
	ChallengeObject challengeObject;
	Context context;
	Activity activity;
	String location_address, location_city, location_country;
	int duration_day, duration_hour, duration_minute, reward;
	String starting_day, starting_hour, starting_minute;
	LatLng latLng;
	LinearLayout llStartingIn, llInputLocation, llOkCancel, llAllAbove,
			llLocationInfo, llExplanatoryPhotos;
	View dialogLocation, dialogTime, dialogDetailLocation;
	RelativeLayout rlLocationDialog, rlTimeDialog, rlDetailLocation;
	CheckBox cbTerms;
	RadioButton rbAddress, rbArea;
	public Spinner spArea;
	public ArrayList<LocationObject> locationObjectList;
	ArrayList<String> imagesList;
	public LatLng selectedLatLng;
	int locationIndex = -1;
	boolean isAddressLevel = false;
	boolean isGlobal = false;
	private int DEFAULT_MAP_ZOOM = 14;
	public static boolean isCancel = false;
	Typeface typeface;
	final int PLACES = 0;
	final int PLACES_DETAILS = 1;
	public static final String YOUR_API_KEY = "AIzaSyBkwoRU80qm6LkZrKRthm5yIx9cCnwGlns";

	public void setContext(Context context) {
		this.context = context;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_create_challenge,
				container, false);

		ivBlueEye = (ImageView) view.findViewById(R.id.iv_blue_eye);
		ivC = (ImageView) view.findViewById(R.id.iv_c);
		ivF = (ImageView) view.findViewById(R.id.iv_f);
		ivR = (ImageView) view.findViewById(R.id.iv_r);
		tvGlobal = (TextView) view.findViewById(R.id.tv_global_create);
		tvGlobal.setVisibility(View.GONE);
		ivAddress = (ImageView) view.findViewById(R.id.iv_distance_location);
		ivArea = (ImageView) view.findViewById(R.id.iv_distance_area);
		ivDistance = (ImageView) view.findViewById(R.id.iv_distance_create);
		ivCalendar = (ImageView) view.findViewById(R.id.iv_calendar_create);
		ivDot1 = (ImageView) view.findViewById(R.id.iv_dot_create_1);
		ivFlag = (ImageView) view.findViewById(R.id.iv_flag_create);
		ivDot2 = (ImageView) view.findViewById(R.id.iv_dot_create_2);
		ivCup = (ImageView) view.findViewById(R.id.iv_cup_create);
		ivQuoteOpen = (ImageView) view.findViewById(R.id.quote_open);
		ivQuoteClose = (ImageView) view.findViewById(R.id.quote_close);
		btnGrayAdd = (Button) view.findViewById(R.id.btn_gray_add);
		etDescription = (EditText) view.findViewById(R.id.et_description);
		etTitleChallenge = (EditText) view
				.findViewById(R.id.et_title_challenge);
		tvStatus = (TextView) view.findViewById(R.id.tv_status_avatar_create);
		tvStartingIn = (TextView) view.findViewById(R.id.tv_starting_in);
		tvDuration = (TextView) view.findViewById(R.id.tv_duration);
		tvReward = (TextView) view.findViewById(R.id.tv_reward);
		tvPleaseSelect = (TextView) view.findViewById(R.id.tv_please_select);
		tvLocation = (TextView) view.findViewById(R.id.tv_location);
		btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		hsExplanatoryPhotos = (HorizontalScrollView) view
				.findViewById(R.id.hs_explanatory_photos);
		llStartingIn = (LinearLayout) view.findViewById(R.id.ll_starting_in);
		rlLocationDialog = (RelativeLayout) view
				.findViewById(R.id.rl_location_dialog);
		rlTimeDialog = (RelativeLayout) view.findViewById(R.id.rl_time_dialog);
		rlDetailLocation = (RelativeLayout) view
				.findViewById(R.id.rl_detail_location_dialog);
		// location
		dialogLocation = (View) view.findViewById(R.id.location_dialog);
		tvAdressLable = (TextView) dialogLocation
				.findViewById(R.id.tv_adress_lable);
		spArea = (Spinner) dialogLocation.findViewById(R.id.sp_area);
		atvPlaces = (AutoCompleteTextView) dialogLocation
				.findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);
		// tvAddress = (EditText)
		// dialogLocation.findViewById(R.id.tv_address_dialog);
		btnLocationCancel = (Button) dialogLocation
				.findViewById(R.id.btn_location_cancel);
		btnLocationOk = (Button) dialogLocation
				.findViewById(R.id.btn_location_ok);
		llAllAbove = (LinearLayout) dialogLocation
				.findViewById(R.id.ll_all_above);
		llInputLocation = (LinearLayout) dialogLocation
				.findViewById(R.id.ll_input_location);
		btnAllAboveCancel = (Button) dialogLocation
				.findViewById(R.id.btn_all_above_cancel);
		btnAllAboveOk = (Button) dialogLocation
				.findViewById(R.id.btn_all_above_ok);
		llOkCancel = (LinearLayout) dialogLocation
				.findViewById(R.id.ll_ok_cancel);
		rbAddress = (RadioButton) dialogLocation.findViewById(R.id.rb_address);
		rbArea = (RadioButton) dialogLocation.findViewById(R.id.rb_area);
		tvDialogLocationTitle = (TextView) dialogLocation
				.findViewById(R.id.tv_dialog_location_title);
		ivLocationShawdow = (ImageView) view
				.findViewById(R.id.iv_location_shawdow);
		ivTimeShawdow = (ImageView) view.findViewById(R.id.iv_time_shawdow);
		cbTerms = (CheckBox) view.findViewById(R.id.cb_terms);
		dialogTime = (View) view.findViewById(R.id.time_dialog);
		btnTimeCancel = (Button) dialogTime.findViewById(R.id.btn_time_cancel);
		btnTimeOk = (Button) dialogTime.findViewById(R.id.btn_time_ok);
		tvStartDay = (TextView) dialogTime.findViewById(R.id.tv_start_day);
		tvStartHour = (TextView) dialogTime.findViewById(R.id.tv_start_hour);
		tvStartMinute = (TextView) dialogTime
				.findViewById(R.id.tv_start_minute);
		llLocationInfo = (LinearLayout) dialogLocation
				.findViewById(R.id.ll_location_info);
		btnSearch = (Button) dialogLocation.findViewById(R.id.btn_search);
		etDurationDay = (EditText) dialogTime.findViewById(R.id.et_time_day);
		etDurationHour = (EditText) dialogTime.findViewById(R.id.et_time_hour);
		etDurationHour.setFilters(new InputFilter[] { new InputFilterMinMax(
				"0", "23") });
		etDurationMinute = (EditText) dialogTime
				.findViewById(R.id.et_time_minute);
		etDurationMinute.setFilters(new InputFilter[] { new InputFilterMinMax(
				"0", "59") });
		etTimeReward = (EditText) dialogTime.findViewById(R.id.et_time_reward);
		tvBalance = (TextView) dialogTime.findViewById(R.id.tv_balance);
		tvBalance.setText(UserInfo.getInstance().getBalance() + "");
		tvBalance.setVisibility(View.GONE);
		llExplanatoryPhotos = (LinearLayout) view
				.findViewById(R.id.ll_explanatory_photos);
		dialogDetailLocation = (View) view
				.findViewById(R.id.detail_location_dialog);
		tvDetailAddress = (TextView) dialogDetailLocation
				.findViewById(R.id.tv_detail_address);
		tvDetailCountry = (TextView) dialogDetailLocation
				.findViewById(R.id.tv_detail_country);
		tvDetailCity = (TextView) dialogDetailLocation
				.findViewById(R.id.tv_detail_city);
		ivDetailLocatonShadow = (ImageView) view
				.findViewById(R.id.iv_detail_location_shawdow);
		btnDetailLocationLeft = (Button) dialogDetailLocation
				.findViewById(R.id.btn_detail_location_left);
		btnDetailLocationRight = (Button) dialogDetailLocation
				.findViewById(R.id.btn_detail_location_right);
		btnDetailLocationOk = (Button) dialogDetailLocation
				.findViewById(R.id.btn_detail_ok);
		tvTerms = (TextView) view.findViewById(R.id.tv_terms);
		btnEditLocation = (Button) view.findViewById(R.id.btn_edit_location);
		btnEditLocation.setVisibility(View.GONE);
		btnEditReward = (Button) view.findViewById(R.id.btn_edit_reward);
		btnEditReward.setVisibility(View.GONE);
		btnCharge = (Button) dialogTime.findViewById(R.id.btn_charge_new);

		btnCancel.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btnLocationCancel.setOnClickListener(this);
		btnLocationOk.setOnClickListener(this);
		btnTimeCancel.setOnClickListener(this);
		btnTimeOk.setOnClickListener(this);
		btnAllAboveCancel.setOnClickListener(this);
		btnAllAboveOk.setOnClickListener(this);
		tvStartDay.setOnClickListener(this);
		tvStartHour.setOnClickListener(this);
		tvStartMinute.setOnClickListener(this);
		btnGrayAdd.setOnClickListener(this);
		ivBlueEye.setOnClickListener(this);
		btnDetailLocationOk.setOnClickListener(this);
		btnDetailLocationLeft.setOnClickListener(this);
		btnDetailLocationRight.setOnClickListener(this);
		cbTerms.setOnClickListener(this);
		btnEditLocation.setOnClickListener(this);
		btnEditReward.setOnClickListener(this);
		btnCharge.setOnClickListener(this);
		btnCharge.setVisibility(View.GONE);
		rbAddress.setOnClickListener(this);
		rbArea.setOnClickListener(this);
		tvLocation.setSelected(true);
		atvPlaces.setSelected(true);
		btnSearch.setOnClickListener(this);
		tvLocation.setOnClickListener(this);
		tvStartingIn.setOnClickListener(this);
		tvDuration.setOnClickListener(this);
		tvReward.setOnClickListener(this);

		locationObjectList = new ArrayList<LocationObject>();
		imagesList = new ArrayList<String>();

		loadAvatar();
		challengeObject = new ChallengeObject();

		atvPlaces.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			protected Object clone() throws CloneNotSupportedException {
				// TODO Auto-generated method stub
				return super.clone();
			}

			@Override
			public boolean equals(Object o) {
				// TODO Auto-generated method stub
				return super.equals(o);
			}

			@Override
			protected void finalize() throws Throwable {
				// TODO Auto-generated method stub
				super.finalize();
			}

			@Override
			public int hashCode() {
				// TODO Auto-generated method stub
				return super.hashCode();
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return super.toString();
			}

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (atvPlaces.getText().toString() == "") {

					((CreateChallengeActivity) context).showPinOnMap(
							Config.lat, Config.lng);
					((CreateChallengeActivity) context).getGoogleMap()
							.moveCamera(
									CameraUpdateFactory.newLatLngZoom(
											new LatLng(Config.lat, Config.lng),
											14));

				}
				return false;
			}
		});

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Creating a DownloadTask to download Google Places matching
				// "s"

				placesDownloadTask = new DownloadTask(PLACES);

				// Getting url to the Google Places Autocomplete api
				String url = getAutoCompleteUrl(s.toString());

				// Start downloading Google Places
				// This causes to execute doInBackground() of DownloadTask class
				placesDownloadTask.execute(url);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				// TODO Auto-generated method stub
				// Creating a DownloadTask to download Google Places matching
				// "s"
				// placesDownloadTask = new DownloadTask(PLACES);
				// // Getting url to the Google Places Autocomplete api
				// String url = getAutoCompleteUrl(s.toString());
				//
				// // Start downloading Google Places
				// // This causes to execute doInBackground() of DownloadTask
				// class
				// placesDownloadTask.execute(url);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// Creating a DownloadTask to download Google Places matching
				// "s"
				// placesDownloadTask = new DownloadTask(PLACES);
				// Getting url to the Google Places Autocomplete api
				// String url = getAutoCompleteUrl(s.toString());

				// Start downloading Google Places
				// This causes to execute doInBackground() of DownloadTask class
				// placesDownloadTask.execute(url);

			}
		});
		atvPlaces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long id) {

				ListView lv = (ListView) arg0;
				SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

				HashMap<String, String> hm = (HashMap<String, String>) adapter
						.getItem(index);

				// Creating a DownloadTask to download Places details of the
				// selected place
				placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

				// Getting url to the Google Places details api
				String url = getPlaceDetailsUrl(hm.get("reference"));

				// Start downloading Google Place Details
				// This causes to execute doInBackground() of DownloadTask class
				placeDetailsDownloadTask.execute(url);
				btnLocationOk.setEnabled(true);

			}
		});
		adjustUserInterface();
		return view;
	}

	private String getAutoCompleteUrl(String place) {

		// Obtain browser key from https://code.google.com/apis/console
		// String key = "key=AIzaSyAYPvTUcuJxDsYPw-PIIZrHwsg0W1jkaFQ";
		String key = "key=AIzaSyBkwoRU80qm6LkZrKRthm5yIx9cCnwGlns";

		// place to be be searched
		String input = "input=" + place;

		// place type to be searched
		String types = "types=geocode";

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = input + "&" + types + "&" + sensor + "&" + key;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
				+ output + "?" + parameters;

		return url;
	}

	private String getPlaceDetailsUrl(String ref) {

		// Obtain browser key from https://code.google.com/apis/console
		// String key = "key=AIzaSyAYPvTUcuJxDsYPw-PIIZrHwsg0W1jkaFQ";
		String key = "key=AIzaSyBkwoRU80qm6LkZrKRthm5yIx9cCnwGlns";

		// reference of place
		String reference = "reference=" + ref;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = reference + "&" + sensor + "&" + key;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/details/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private void loadAvatar() {
		MemoryCache memoryCache = new MemoryCache();
		Bitmap bitmap = memoryCache.get(Config.IdUser);
		if (bitmap != null) {
			ivBlueEye.setImageBitmap(bitmap);
		} else {
			Picasso.with(activity).load(UserInfo.getInstance().getAvatar())
					.transform(new CircleTransform()).into(ivBlueEye);
		}
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public void setStartTimeFromWheel(String day, String hour, String minute) {
		tvStartDay.setText(day);
		tvStartHour.setText(hour);
		tvStartMinute.setText(minute);
		starting_day = day;
		starting_hour = hour;
		starting_minute = minute;
	}

	public void setStartingIn(String startingTime, String reward) {
		tvStartingIn.setText(starting_day + " " + starting_hour + " "
				+ starting_minute);
		tvDuration.setText(duration_day + " " + duration_hour + " "
				+ duration_minute + ", ");
		tvReward.setText(reward);
	}

	private String getStartTime() {
		if (tvStartDay.equals("Today"))
			return DayArrayAdapter.dayList.get(0);
		else if (tvStartDay.equals("Tomorrow")) {
			return DayArrayAdapter.dayList.get(1);
		} else
			return tvStartDay.getText().toString();
	}

	private String getDurationTime() {
		if (TextUtils.isEmpty(etDurationDay.getText().toString()))
			etDurationDay.setText("0");
		if (TextUtils.isEmpty(etDurationHour.getText().toString()))
			etDurationHour.setText("0");
		if (TextUtils.isEmpty(etDurationMinute.getText().toString()))
			etDurationMinute.setText("0");
		duration_day = Integer.parseInt(etDurationDay.getText().toString());
		duration_hour = Integer.parseInt(etDurationHour.getText().toString());
		duration_minute = Integer.parseInt(etDurationMinute.getText()
				.toString());
		return etDurationDay.getText().toString() + "d "
				+ etDurationHour.getText().toString() + "h "
				+ etDurationMinute.getText().toString() + "m";
	}

	private void adjustUserInterface() {
		int widthDevice = ResizeUtils.getSizeDevice(this.getActivity()).x;
		int heightDevice = ResizeUtils.getSizeDevice(this.getActivity()).y;

		ResizeUtils.resizeImageView(ivBlueEye, (int) (widthDevice * 0.2),
				(int) (widthDevice * 0.2), 0, 0, (int) (widthDevice * 0.02), 0,
				RelativeLayout.CENTER_IN_PARENT);

		ResizeUtils.resizeButton(btnSubmit, (int) (widthDevice * 0.2),
				(int) (heightDevice * 0.04), 0, 0, (int) (widthDevice * 0.03),
				0, RelativeLayout.ALIGN_PARENT_LEFT);

		ResizeUtils.resizeButton(btnCancel, (int) (widthDevice * 0.2),
				(int) (heightDevice * 0.04), 0, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.03), 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeTextView(tvLocation, (int) (widthDevice * 0.6),
				(int) (heightDevice * 0.052), 0, 0, 0, 0,
				RelativeLayout.CENTER_IN_PARENT);
		ResizeUtils.resizeTextView(tvStatus, (int) (widthDevice * 0.2),
				(int) (heightDevice * 0.09), (int) (widthDevice * 0.04), 0, 0,
				0, RelativeLayout.ALIGN_PARENT_BOTTOM);
		tvStatus.setPadding(0, (int) (heightDevice * 0.055), 0, 0);

		ResizeUtils.resizeImageView(ivDistance, (int) (widthDevice * 0.035),
				(int) (widthDevice * 0.045), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		ResizeUtils.resizeImageView(ivAddress, (int) (widthDevice * 0.05),
				(int) (widthDevice * 0.045), 0, 0, 0, 0, 1f, Gravity.CENTER);
		ResizeUtils.resizeImageView(ivArea, (int) (widthDevice * 0.05),
				(int) (widthDevice * 0.045), 0, 0, 0, 0, 1f, Gravity.CENTER);

		ResizeUtils.resizeImageView(ivC, (int) (widthDevice * 0.06),
				(int) (widthDevice * 0.055), 0, 0, 0,
				(int) (widthDevice * 0.08), 1f, Gravity.CENTER);
		ResizeUtils.resizeImageView(ivF, (int) (widthDevice * 0.06),
				(int) (widthDevice * 0.055), 0, 0, 0,
				(int) (widthDevice * 0.08), 1f, Gravity.CENTER);
		ResizeUtils.resizeImageView(ivR, (int) (widthDevice * 0.055),
				(int) (widthDevice * 0.05), 0, 0, 0,
				(int) (widthDevice * 0.08), 1f, Gravity.CENTER);

		ResizeUtils.resizeImageView(ivCalendar, (int) (widthDevice * 0.05),
				(int) (widthDevice * 0.045), 0, (int) (widthDevice * 0.02), 0,
				0, 0.25f, Gravity.BOTTOM);
		ResizeUtils.resizeImageView(ivDot1, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), 0, (int) (widthDevice * 0.01), 0,
				0, 0.1f, Gravity.BOTTOM);
		ResizeUtils.resizeImageView(ivFlag, (int) (widthDevice * 0.045),
				(int) (widthDevice * 0.045), 0, (int) (widthDevice * 0.02), 0,
				0, 0.25f, Gravity.BOTTOM);
		ResizeUtils.resizeImageView(ivDot2, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), 0, (int) (widthDevice * 0.01), 0,
				0, 0.1f, Gravity.BOTTOM);
		ResizeUtils.resizeImageView(ivCup, (int) (widthDevice * 0.045),
				(int) (widthDevice * 0.045), 0, (int) (widthDevice * 0.02), 0,
				0, 0.25f, Gravity.BOTTOM);

		ResizeUtils.resizeImageView(ivQuoteOpen, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), RelativeLayout.ALIGN_PARENT_LEFT);

		ResizeUtils.resizeImageView(ivQuoteClose, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.02), (int) (widthDevice * 0.02),
				(int) (widthDevice * 0.03), RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.ALIGN_PARENT_RIGHT);

		// ResizeUtils.resizeEditText(tvAddress, (int) (widthDevice * 0.3),(int)
		// (heightDevice * 0.2), 0, 0, 0, 0);

		ResizeUtils.resizeEditText(etDescription, widthDevice,
				(int) (heightDevice * 0.2), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_TOP);
		etDescription.setPadding((int) (widthDevice * 0.1),
				(int) (widthDevice * 0.02), (int) (widthDevice * 0.1),
				(int) (widthDevice * 0.02));

		ResizeUtils.resizeButton(btnGrayAdd, (int) (widthDevice * 0.06),
				(int) (widthDevice * 0.06), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeHorizontalScrollView(hsExplanatoryPhotos,
				widthDevice, (int) (heightDevice * 0.2),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01));

		ResizeUtils.resizeImageView(ivLocationShawdow, widthDevice,
				(int) (heightDevice * 0.9), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_TOP);
		ResizeUtils.resizeImageView(ivTimeShawdow, widthDevice,
				(int) (heightDevice * 0.9), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_TOP);
		ResizeUtils.resizeImageView(ivDetailLocatonShadow, widthDevice,
				(int) (heightDevice * 0.9), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_TOP);

		resizeLocationDialog();

		resizeTimeDialog();

		Drawable okDrawable = getResources().getDrawable(R.drawable.ok_button);
		ResizeUtils.resizeButton(
				btnTimeOk,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		Drawable cancelDrawable = getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btnTimeCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(btnLocationOk,
				(int) (Config.screenWidth * 0.1), (int) (Config.screenWidth
						* 0.1 * okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btnLocationCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(btnAllAboveOk,
				(int) (Config.screenWidth * 0.1), (int) (Config.screenWidth
						* 0.1 * okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btnAllAboveCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		resizeDetialtLocationDialog();

		ResizeUtils.resizeButton(btnDetailLocationOk,
				(int) (Config.screenWidth * 0.1), (int) (Config.screenWidth
						* 0.1 * okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.05);
		params.height = (int) (Config.screenWidth * 0.05);
		params.setMargins(0, (int) (Config.screenWidth * 0.005),
				(int) (Config.screenWidth * 0.01), 0);
		cbTerms.setLayoutParams(params);

		ResizeUtils.resizeButton(btnEditLocation, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), RelativeLayout.ALIGN_PARENT_RIGHT);

		ResizeUtils.resizeButton(btnEditReward, (int) (widthDevice * 0.07),
				(int) (widthDevice * 0.07), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01));
		ResizeUtils
				.resizeButton(btnSearch, (int) (widthDevice * 0.045),
						(int) (widthDevice * 0.045), 0,
						(int) (widthDevice * 0.1), 0, 0);

		ResizeUtils.resizeButton(btnCharge, (int) (widthDevice * 0.08),
				(int) (widthDevice * 0.08), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01));

		// tvAddress.getLayoutParams().height = (int) (Config.screenWidth *
		// 0.09);
		// tvAddress.setPadding(0, 0, 0, (int) (Config.screenWidth * 0.002));
	}

	private void resizeLocationDialog() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.9);
		params.height = (int) (Config.screenHeight * 0.5);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.setMargins(0, (int) (Config.screenHeight * 0.03), 0, 0);
		dialogLocation.setLayoutParams(params);

		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params1.width = (int) (Config.screenWidth * 0.45);
		params1.height = (int) (Config.screenHeight * 0.06);
		atvPlaces.setLayoutParams(params1);

		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params3.width = (int) (Config.screenWidth * 0.048);
		params3.height = (int) (Config.screenWidth * 0.048);
		rbAddress.setLayoutParams(params3);
		rbArea.setLayoutParams(params3);

	}

	private void resizeTimeDialog() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.9);
		params.height = (int) (Config.screenHeight * 0.55);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.setMargins(0, (int) (Config.screenHeight * 0.03), 0, 0);
		dialogTime.setLayoutParams(params);
	}

	private void resizeDetialtLocationDialog() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.9);
		params.height = (int) (Config.screenHeight * 0.48);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.setMargins(0, (int) (Config.screenHeight * 0.05), 0, 0);
		dialogDetailLocation.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		switch (v.getId()) {
		case R.id.btn_cancel:
			AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
			// Setting Dialog Title
			builder1.setTitle("Confirm Cacel...");
			builder1.setMessage("Are you sure to cancel and not proceed to create this challenge? ");
			// Setting Icon to Dialog
			builder1.setIcon(activity.getResources().getDrawable(
					R.drawable.ic_delete));
			builder1.setCancelable(true);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
								Intent intentArea = new Intent(context,
										MyAreaActivity.class);
								intentArea.putExtra("run", "MyArea");
								context.startActivity(intentArea);
							} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
								Intent Global = new Intent(context,
										MyAreaActivity.class);
								Global.putExtra("run", "Global");
								context.startActivity(Global);
							}
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
			break;
		case R.id.btn_submit:
			if (!validateInputChallenge())
				return;
			if (tvStatus.getText().equals(
					activity.getResources().getString(
							R.string.status_avatar_private))) {
				challengeObject.setiPublic(0);
			}
			challengeObject.setTitle(etTitleChallenge.getText().toString());
			Calendar newCalendar = (Calendar) (Calendar.getInstance()).clone();
			DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
			if (tvStartDay.getText().equals("Today")) {
				starting_day = formatDay.format(newCalendar.getTime());
			} else if (tvStartDay.getText().equals("Tomorrow")) {
				newCalendar.roll(Calendar.DAY_OF_YEAR, 1);
				starting_day = formatDay.format(newCalendar.getTime());
			} else {
				starting_day = tvStartDay.getText().toString();
			}
			challengeObject.setStarting_on_year(Integer.parseInt(starting_day
					.substring(0, 4)));
			challengeObject.setStarting_on_month(Integer.parseInt(starting_day
					.substring(5, 7)));
			challengeObject.setStarting_on_day(Integer.parseInt(starting_day
					.substring(8, 10)));
			challengeObject.setStarting_on_hour(Integer.parseInt(tvStartHour
					.getText().toString()));
			challengeObject.setStarting_on_minute(Integer
					.parseInt(tvStartMinute.getText().toString()));
			challengeObject.setStarting_on_second(0);
			challengeObject.setDuration_day(Integer.parseInt(etDurationDay
					.getText().toString()));
			challengeObject.setDuration_hour(Integer.parseInt(etDurationHour
					.getText().toString()));
			challengeObject.setDuration_minute(Integer
					.parseInt(etDurationMinute.getText().toString()));
			challengeObject.setCategory(1);
			challengeObject.setDescription(etDescription.getText().toString());
			challengeObject.setReward(reward);
			challengeObject.setImagesList(imagesList);
			if (isGlobal)
				addGlobalLocation();
			challengeObject.setLocationsList(locationObjectList);

			new CreateChallengeAsynctask(context).execute(challengeObject);
			/*
			 * fragmentTransaction.replace(R.id.fm_selected, new
			 * TapHintFragment(), "TapHintFragment");
			 * fragmentTransaction.addToBackStack(null);
			 * fragmentTransaction.commit();
			 */
			((CreateChallengeActivity) context).getGoogleMap().clear();
			// new LoadChallengeAsynctask(context).execute(Config.lat + "",
			// Config.lng + "", Config.IdUser);
			// activity.finish();

			break;
		case R.id.tv_location:
			((CreateChallengeActivity) context).setMapVisible(View.VISIBLE);
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.GONE);
			if (locationIndex == -1) {
				locationIndex++;
				((CreateChallengeActivity) context).showPinOnMap(Config.lat,
						Config.lng);
				((CreateChallengeActivity) context).getGoogleMap().moveCamera(
						CameraUpdateFactory.newLatLngZoom(new LatLng(
								Config.lat, Config.lng), 14));
			}
			loadLocationToDialog(locationObjectList.size() - 1);
			refreshNavigationBar();
			showLocationDialog();
			if (locationObjectList.size() == 0)
				((CreateChallengeActivity) context).enableScrollView(false);
			enableBelowScreen(false);
			tvLocation.setEnabled(false);
			refreshEditTextAddress();
			break;
		case R.id.tv_reward:
			
		case R.id.tv_duration:
		
		case R.id.tv_starting_in:
			typeface = Typeface.createFromAsset(activity.getAssets(),
					"fonts/HomeRunScript-roman.otf");
			tvPleaseSelect.setTypeface(typeface);
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.GONE);
			showTimeDialog();
			enableBelowScreen(false);
			if (locationObjectList.size() == 0)
				((CreateChallengeActivity) context).enableScrollView(false);
			break;
		case R.id.btn_location_cancel:
			((CreateChallengeActivity) context).showPinOnMap(Config.lat,
					Config.lng);
			((CreateChallengeActivity) context).getGoogleMap().moveCamera(
					CameraUpdateFactory.newLatLngZoom(new LatLng(
							Config.lat, Config.lng), 14));
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.VISIBLE);
			tvLocation.setEnabled(true);
			hideLocationDialog();
			enableBelowScreen(true);
			((CreateChallengeActivity) context).enableScrollView(true);
			if (tvLocation.getText().equals("")) {
				locationObjectList.clear();
				resetLocationDialog();
				tvLocation.setText("");
				refreshEditTextAddress();
			} else {
				resetLocationDialog();
				refreshEditTextAddress();
			}
			break;
		case R.id.btn_location_ok:
			locationObjectList.clear();
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.VISIBLE);
			if (isGlobal) {
				hideLocationDialog();
				tvLocation.setText("Global");
				locationObjectList.clear();
				resetLocationDialog();
				return;
			}
			// ((CreateChallengeActivity) context).setMapVisible(View.GONE);
			((CreateChallengeActivity) context).enableScrollView(true);
			enableBelowScreen(true);
			addLocation();

			tvLocation.setEnabled(true);

			hideLocationDialog();
			resetLocationDialog();
			refreshNavigationBar();

			if (locationObjectList.size() > 0) {
				locationIndex = 0;
				// showDetailLocationDialog();
				//
				if (rbAddress.isChecked()) {
					
					
					((CreateChallengeActivity) context).getGoogleMap().clear();
					
					((CreateChallengeActivity) context).showPinAuthorOnMap(Config.lat, Config.lng);
					((CreateChallengeActivity) context).getGoogleMap()
							.moveCamera(
									CameraUpdateFactory.newLatLngZoom(
											new LatLng(locationObjectList.get(
													locationIndex).getLat(),
													locationObjectList.get(
															locationIndex)
															.getLng()),
											DEFAULT_MAP_ZOOM));
					Log.d("Test", locationIndex
							+ " - "
							+ locationObjectList.get(locationIndex)
									.getAddress());

					((CreateChallengeActivity) context)
							.getGoogleMap()
							.addMarker(
									new MarkerOptions()
											.icon(BitmapDescriptorFactory
													.fromBitmap(((CreateChallengeActivity) context)
															.createPin()))
											.position(selectedLatLng)
											.anchor(0.5f, 1.0f));
					((CreateChallengeActivity) context)
					.getGoogleMap()
					.addMarker(
							new MarkerOptions()
									.icon(BitmapDescriptorFactory
											.fromBitmap(((CreateChallengeActivity) context)
													.createPin()))
									.position(selectedLatLng)
									.anchor(0.5f, 1.0f));
					
					
				} else {
					((CreateChallengeActivity) context).getGoogleMap().clear();
					((CreateChallengeActivity) context).getGoogleMap()
							.moveCamera(
									CameraUpdateFactory.newLatLngZoom(
											new LatLng(locationObjectList.get(
													locationIndex).getLat(),
													locationObjectList.get(
															locationIndex)
															.getLng()),
											DEFAULT_MAP_ZOOM));
					Log.d("Test", locationIndex
							+ " - "
							+ locationObjectList.get(locationIndex)
									.getAddress());
				}
				//
			}

			break;
		case R.id.btn_time_cancel:
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.VISIBLE);
			hideTimeDialog();
			enableBelowScreen(true);
			break;
		case R.id.btn_time_ok:
			if (!validateInputTime())
				return;
			tvStartingIn.setText(getStartTime());
			tvDuration.setText(getDurationTime());
			tvReward.setText(etTimeReward.getText().toString());
			reward = Integer.parseInt(etTimeReward.getText().toString());
			hideTimeDialog();
			enableBelowScreen(true);
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.VISIBLE);
			break;
		// case R.id.cb_make_it_global:
		// /*
		// * llLocationInfo.setVisibility(View.GONE);
		// * llMakeItGlobal.setVisibility(View.GONE);
		// * llOkCancel.setVisibility(View.GONE);
		// * llAllAbove.setVisibility(View.VISIBLE);
		// * llOkCancel.setVisibility(View.GONE);
		// * cbMakeItGlobal.setChecked(false);
		// * rlLocationDialog.setVisibility(View.VISIBLE);
		// * ((CreateChallengeActivity) context).setMapVisible(View.VISIBLE);
		// * if (locationObjectList.size() == 0) ((CreateChallengeActivity)
		// * context).enableScrollView(false); ((CreateChallengeActivity)
		// * context).getGoogleMap().clear();
		// *
		// * // Resize dialog RelativeLayout.LayoutParams params = new
		// * RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT,
		// * LayoutParams.WRAP_CONTENT); params.width = (int)
		// * (Config.screenWidth * 0.8); params.height = (int)
		// * (Config.screenHeight * 0.25);
		// * params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		// * params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// * params.setMargins(0, (int) (Config.screenHeight * 0.05), 0, 0);
		// * dialogLocation.setLayoutParams(params);
		// */
		// hideLocationDialog();
		// cbMakeItGlobal.setChecked(false);
		// tvLocation.setText("Global");
		// locationObjectList.clear();
		// resetLocationDialog();
		// isGlobal = true;
		// ((CreateChallengeActivity) context)
		// .setTitleAndAvatarIvisible(View.VISIBLE);
		// break;
		case R.id.btn_all_above_cancel:
			llAllAbove.setVisibility(View.GONE);
			resetLocationDialog();
			rlLocationDialog.setVisibility(View.VISIBLE);
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.VISIBLE);
			((CreateChallengeActivity) context).setMapVisible(View.GONE);
			((CreateChallengeActivity) context).enableScrollView(true);
			((CreateChallengeActivity) context).getGoogleMap().clear();

			// Resize dialog
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.width = (int) (Config.screenWidth * 0.8);
			params.height = (int) (Config.screenHeight * 0.5);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.setMargins(0, (int) (Config.screenHeight * 0.05), 0, 0);
			dialogLocation.setLayoutParams(params);
			refreshEditTextAddress();
			break;
		case R.id.btn_all_above_ok:
			locationIndex++;
			addLocationFromMap();
			refreshEditTextAddress();
			break;
		case R.id.tv_start_day:
		case R.id.tv_start_hour:
		case R.id.tv_start_minute:
			Dialog dialog = DialogFactory.getDialog(context,
					DialogType.DIALOG_WHEEL_DAY);
			dialog.show();
			break;
		case R.id.btn_gray_add:
			if (imagesList.size() > 9) {
				Toast.makeText(context,
						"You can only update maximum 10 photos",
						Toast.LENGTH_LONG).show();
				return;
			}
			((CreateChallengeActivity) context).callAddExplanatoryPhoto();
			break;
		case R.id.iv_blue_eye:
			final DialogChallengePrivacy dialogChallengePrivacy = (DialogChallengePrivacy) DialogFactory
					.getDialog(context, DialogType.DIALOG_CHALLENGE_PRIVACY);
			dialogChallengePrivacy.show();
			dialogChallengePrivacy
					.setCancelClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialogChallengePrivacy.dismiss();
						}
					});
			dialogChallengePrivacy.setOkClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!dialogChallengePrivacy.isAcceptTerm()) {
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.must_agree_terms),
								Toast.LENGTH_SHORT).show();
						return;
					}
					Drawable d = ivBlueEye.getDrawable();
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					Bitmap bm = doGreyscale(bitmap);
					ivBlueEye.setImageBitmap(bm);
					tvStatus.setText(activity.getResources().getString(
							R.string.status_avatar_private));
					dialogChallengePrivacy.dismiss();
				}
			});

			// new AddMemberAsynctask(context).execute("20", "127,129");
			break;
		case R.id.btn_detail_ok:
			hideDetailLocationDialog();
			((CreateChallengeActivity) context)
					.setTitleAndAvatarIvisible(View.VISIBLE);
			((CreateChallengeActivity) context).setMapVisible(View.GONE);
			((CreateChallengeActivity) context).enableScrollView(true);
			enableBelowScreen(true);
			break;
		case R.id.btn_detail_location_left:
			locationIndex--;
			loadLocationToDialog(locationIndex);
			refreshNavigationBar();
			refreshEditTextAddress();
			//
			((CreateChallengeActivity) context).getGoogleMap().clear();
			((CreateChallengeActivity) context).getGoogleMap().animateCamera(
					CameraUpdateFactory.newLatLngZoom(new LatLng(
							locationObjectList.get(locationIndex).getLat(),
							locationObjectList.get(locationIndex).getLng()),
							DEFAULT_MAP_ZOOM));
			Log.d("Test",
					locationIndex
							+ " - "
							+ locationObjectList.get(locationIndex)
									.getAddress());
			((CreateChallengeActivity) context)
					.getGoogleMap()
					.addMarker(
							new MarkerOptions()
									.icon(BitmapDescriptorFactory
											.fromBitmap(((CreateChallengeActivity) context)
													.createPin()))
									.position(selectedLatLng)
									.anchor(0.5f, 1.0f));
			//
			break;
		case R.id.btn_detail_location_right:
			locationIndex++;
			loadLocationToDialog(locationIndex);
			refreshNavigationBar();
			refreshEditTextAddress();
			//
			((CreateChallengeActivity) context).getGoogleMap().clear();
			((CreateChallengeActivity) context).getGoogleMap().animateCamera(
					CameraUpdateFactory.newLatLngZoom(new LatLng(
							locationObjectList.get(locationIndex).getLat(),
							locationObjectList.get(locationIndex).getLng()),
							DEFAULT_MAP_ZOOM));
			Log.d("Test",
					locationIndex
							+ " - "
							+ locationObjectList.get(locationIndex)
									.getAddress());
			((CreateChallengeActivity) context)
					.getGoogleMap()
					.addMarker(
							new MarkerOptions()
									.icon(BitmapDescriptorFactory
											.fromBitmap(((CreateChallengeActivity) context)
													.createPin()))
									.position(selectedLatLng)
									.anchor(0.5f, 1.0f));
			//
			break;
		case R.id.cb_terms:
			if (cbTerms.isChecked())
				tvTerms.setTextColor(getResources().getColor(R.color.black));
			else
				tvTerms.setTextColor(getResources().getColor(R.color.red));
			break;
		case R.id.rb_address:
			locationObjectList.clear();
			rbAddress.setChecked(true);
			rbArea.setChecked(false);
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_blue_location));
			isGlobal = false;
			((CreateChallengeActivity) context).showPinAuthorOnMap1(
					Config.lat, Config.lng);
			
			
			
			break;
		case R.id.rb_area:
			locationObjectList.clear();
			rbAddress.setChecked(false);
			rbArea.setChecked(true);
			ivDistance.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ic_blue_area));
			isGlobal = false;
			break;
		default:
			break;
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

	public void addImageChallenge(String path) {
		imagesList.add(path);
	}

	private void showLocationDialog() {
		rlLocationDialog.setVisibility(View.VISIBLE);
		loadLocationToDialog(locationIndex);
		((CreateChallengeActivity) context).svMyArea.scrollTo(0, 0);
		// ((CreateChallengeActivity) context).enableScrollView(false);
	}

	private void showDetailLocationDialog() {
		rlDetailLocation.setVisibility(View.VISIBLE);
		loadLocationToDialog(locationIndex);
		// ((CreateChallengeActivity) context).enableScrollView(false);
	}

	public void enableLocationOkButton(boolean b) {
		btnLocationOk.setEnabled(b);
	}

	private void hideLocationDialog() {
		rlLocationDialog.setVisibility(View.GONE);
		// ((CreateChallengeActivity) context).setMapVisible(View.GONE);
		((CreateChallengeActivity) context).enableScrollView(true);
		enableBelowScreen(true);
	}

	private void hideDetailLocationDialog() {
		rlDetailLocation.setVisibility(View.GONE);
		// ((CreateChallengeActivity) context).setMapVisible(View.GONE);
		((CreateChallengeActivity) context).enableScrollView(true);
		enableBelowScreen(true);
	}

	private void showTimeDialog() {
		rlTimeDialog.setVisibility(View.VISIBLE);
		if (locationObjectList.size() == 0)
			((CreateChallengeActivity) context).enableScrollView(false);
		refreshNavigationBar();
	}

	private void hideTimeDialog() {
		rlTimeDialog.setVisibility(View.GONE);
		((CreateChallengeActivity) context).enableScrollView(true);
		enableBelowScreen(true);
	}

	private boolean validateLocationDialog() {
		if (!rbAddress.isChecked() && !rbArea.isChecked()) {
			Toast.makeText(context,
					context.getResources().getString(R.string.address_or_area),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (atvPlaces.getText().equals("")
				&& spArea.getAdapter().getCount() == 0) {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.please_choose_a_location),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private boolean validateInputChallenge() {
		if (TextUtils.isEmpty(etTitleChallenge.getText().toString())) {
			Toast.makeText(context,
					getResources().getString(R.string.title_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(tvLocation.getText().toString())) {
			Toast.makeText(context,
					getResources().getString(R.string.location_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (tvStartingIn.getText().toString().equals("           ")) {
			Toast.makeText(context,
					getResources().getString(R.string.starting_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (tvDuration.getText().toString().equals("           ")) {
			Toast.makeText(context,
					getResources().getString(R.string.duration_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (tvReward.getText().toString().equals("           ")) {
			Toast.makeText(context,
					getResources().getString(R.string.reward_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(etDescription.getText().toString())) {
			Toast.makeText(context,
					getResources().getString(R.string.description_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// if (imagesList.isEmpty()) {
		// Toast.makeText(context,
		// getResources().getString(R.string.explanatory_not_empty),
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }

		if (!cbTerms.isChecked()) {
			Toast.makeText(context,
					getResources().getString(R.string.must_agree_terms),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private boolean validateInputTime() {
		if (TextUtils.isEmpty(etDurationDay.getText().toString())
				&& TextUtils.isEmpty(etDurationHour.getText().toString())
				&& TextUtils.isEmpty(etDurationMinute.getText().toString())) {
			Toast.makeText(context,
					getResources().getString(R.string.duration_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(etTimeReward.getText().toString())) {
			Toast.makeText(context,
					getResources().getString(R.string.reward_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			int iReward = Integer.parseInt(etTimeReward.getText().toString());
			if (iReward < 0) {
				Toast.makeText(context,
						getResources().getString(R.string.reward_not_empty),
						Toast.LENGTH_SHORT).show();
				return false;
			}
			if (iReward > UserInfo.getInstance().getBalance()) {
				Toast.makeText(context,
						getResources().getString(R.string.not_enough_money),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		if (Integer.parseInt(etTimeReward.getText().toString()) > UserInfo
				.getInstance().getBalance()) {
			Toast.makeText(context,
					getResources().getString(R.string.not_enough_money),
					Toast.LENGTH_SHORT).show();
			etTimeReward.setText("");
			return false;
		}
		return true;
	}

	private void enableBelowScreen(boolean b) {
		ivBlueEye.setEnabled(b);
		etTitleChallenge.setEnabled(b);
		btnSubmit.setEnabled(b);
		btnCancel.setEnabled(b);
		tvLocation.setEnabled(b);
		llStartingIn.setEnabled(b);
		etDescription.setEnabled(b);
		btnGrayAdd.setEnabled(b);
		cbTerms.setEnabled(b);
	}

	private void loadLocationToDialog(int index) {
		if (locationIndex == -1)
			return;
		if (locationObjectList.size() > 0) {
			LocationObject location = locationObjectList.get(index);
			atvPlaces.setText(location.getAddress());
			tvDetailAddress.setText(location.getAddress());
			tvDetailCountry.setText(location.getArea());
			ArrayList<String> areaList = new ArrayList<String>();
			areaList.add(location.getArea());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					R.layout.item_category, R.id.tv_category, areaList);
			spArea.setAdapter(adapter);
		} else {
			atvPlaces.setText("");
			tvDetailAddress.setText("");
			tvDetailCountry.setText("");
			tvDetailCity.setText("");
		}
		setLocatioDialogTitle();
	}

	private void setLocatioDialogTitle() {
		// if (locationObjectList.size() > 1)
		// tvDialogLocationTitle.setText(getResources().getString(
		// R.string.choose_location)
		// + " (" + (locationIndex + 1) + ")");
		// else
		tvDialogLocationTitle.setText(getResources().getString(
				R.string.choose_location));
		typeface = Typeface.createFromAsset(activity.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvDialogLocationTitle.setTypeface(typeface);
		tvDialogLocationTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				getResources().getDimension(R.dimen.text_title_xxl));
		tvDialogLocationTitle.setTextColor(getResources().getColor(
				R.color.blue_title_my_area));
	}

	private void addLocation() {
		if (!validateLocationDialog())
			return;
		if (!checkExistsLocation()) {
			addLocationFromDialog();
		} else {
			locationIndex--;
		}
		Log.d("Test", "locationIndex: " + locationIndex);
		atvPlaces.setText("");
		refreshEditTextAddress();
		setLocatioDialogTitle();
	}

	private void addGlobalLocation() {
		locationObjectList.clear();
		LocationObject locationObject = new LocationObject();
		locationObject.setAddress("");
		locationObject.setArea("");
		locationObject.setLat(0);
		locationObject.setLng(0);
		locationObjectList.add(locationObject);
	}

	private boolean checkExistsLocation() {
		return false;
	}

	private void removeLocation() {
		if (locationIndex < 0 || locationObjectList.size() == 0)
			return;
		if (locationObjectList.size() == 1) {
			locationObjectList.clear();
			resetLocationDialog();
			locationIndex = -1;
		} else {
			locationObjectList.remove(locationIndex);
			loadLocationToDialog(locationIndex == (locationObjectList.size() - 1) ? locationIndex--
					: locationIndex++);
		}
		refreshNavigationBar();
	}

	private void resetLocationDialog() {
		atvPlaces.setText("");
		atvPlaces.setFocusable(true);
		llLocationInfo.setVisibility(View.VISIBLE);
		llOkCancel.setVisibility(View.VISIBLE);
		spArea.setAdapter(new ArrayAdapter<String>(context,
				R.layout.item_category, R.id.tv_category,
				new ArrayList<String>()));
	}

	private void addLocationFromDialog() {
		String sLocation = "";
		LocationObject locationObject = new LocationObject();
		if (rbAddress.isChecked()) {
			locationObject.setAddress(atvPlaces.getText().toString());
			locationObject.setArea("");
			sLocation = atvPlaces.getText().toString();
		} else if (rbArea.isChecked()) {
			locationObject.setAddress("");
			String area = spArea.getSelectedItem().toString();
			for (int i = spArea.getSelectedItemPosition() + 1; i < spArea
					.getCount(); i++) {
				area += ", " + spArea.getItemAtPosition(i).toString();
			}
			locationObject.setArea(area);
			sLocation = area;
		}
		try {
			locationObject.setLat(selectedLatLng.latitude);
			locationObject.setLng(selectedLatLng.longitude);
		} catch (Exception e) {
			locationObject.setLat(selectedLatLng.latitude);
			locationObject.setLng(selectedLatLng.longitude);
		}

		locationObjectList.add(locationObject);

		tvLocation.setText(sLocation);
		resetLocationDialog();
	}

	private void addLocationFromMap() {
		if (atvPlaces.getText().toString().equals("")) {
			Toast.makeText(
					context,
					getResources().getString(R.string.please_choose_a_location),
					Toast.LENGTH_SHORT).show();
			return;
		}
		isAddressLevel = true;

		enableBelowScreen(false);
		btnLocationOk.setEnabled(true);

		// etCountry.setFocusable(false);
		// etCity.setFocusable(false);
		// etAddress.setFocusable(false);
		llAllAbove.setVisibility(View.GONE);
		llLocationInfo.setVisibility(View.VISIBLE);
		llOkCancel.setVisibility(View.VISIBLE);
		((CreateChallengeActivity) context).setMapVisible(View.GONE);

		addLocationFromDialog();
		refreshEditTextAddress();

		// Resize dialog
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.8);
		params.height = (int) (Config.screenHeight * 0.5);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.setMargins(0, (int) (Config.screenHeight * 0.05), 0, 0);
		dialogLocation.setLayoutParams(params);
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
		llExplanatoryPhotos.addView(iv);
	}

	private void refreshNavigationBar() {

		if (locationObjectList.size() == 0 || locationObjectList.size() == 1) {
			btnDetailLocationLeft.setVisibility(View.GONE);
			btnDetailLocationRight.setVisibility(View.GONE);
			return;
		}
		if (locationIndex == 0) {
			btnDetailLocationLeft.setVisibility(View.GONE);
			btnDetailLocationRight.setVisibility(View.VISIBLE);
		} else if (locationIndex == locationObjectList.size() - 1) {
			btnDetailLocationLeft.setVisibility(View.VISIBLE);
			btnDetailLocationRight.setVisibility(View.GONE);
		} else {
			btnDetailLocationLeft.setVisibility(View.VISIBLE);
			btnDetailLocationRight.setVisibility(View.VISIBLE);
		}
	}

	private void refreshEditTextAddress() {
		if (locationObjectList.size() <= 0) {
			atvPlaces.setText("");
			atvPlaces.setFocusable(true);
			spArea.setAdapter(new ArrayAdapter<String>(context,
					R.layout.item_category, R.id.tv_category,
					new ArrayList<String>()));
		} else {
			if (isAddressLevel) {
				if (locationIndex < 0)
					locationIndex = 0;
				if (locationIndex > locationObjectList.size() - 1)
					locationIndex = locationObjectList.size() - 1;
			} else {
				if (locationIndex < 0)
					locationIndex = 0;
				if (locationIndex > locationObjectList.size() - 1)
					locationIndex = locationObjectList.size() - 1;
			}
		}
	}

	public static Bitmap doGreyscale(Bitmap src) {
		// constant factors
		final double GS_RED = 0.299;
		final double GS_GREEN = 0.587;
		final double GS_BLUE = 0.114;

		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				src.getConfig());
		// pixel information
		int A, R, G, B;
		int pixel;

		// get image size
		int width = src.getWidth();
		int height = src.getHeight();

		// scan through every single pixel
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// take conversion up to one single value
				R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public void setGlobal() {
		tvLocation.setClickable(false);

		tvLocation.setText(activity.getResources().getString(R.string.global));
		tvLocation.setTextColor(activity.getResources().getColor(
				R.color.gray_text));

		ResizeUtils.resizeTextView(tvLocation,
				(int) (Config.screenWidth * 0.14),
				(int) (Config.screenHeight * 0.052),
				(int) (Config.screenWidth * 0.02), 0, 0, 0,
				RelativeLayout.RIGHT_OF, R.id.iv_distance_create);
		btnEditLocation.setVisibility(View.GONE);
		ivDistance.setImageDrawable(activity.getResources().getDrawable(
				R.drawable.ic_gray_earth));
		isGlobal = true;
	}

	public class InputFilterMinMax implements InputFilter {

		private int min, max;

		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			try {
				int input = Integer.parseInt(dest.toString()
						+ source.toString());
				if (isInRange(min, max, input))
					return null;
			} catch (NumberFormatException nfe) {
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	// Fetches data from url passed

	private class DownloadTask extends AsyncTask<String, Void, String> {

		private int downloadType = 0;

		// Constructor
		public DownloadTask(int type) {
			this.downloadType = type;
		}

		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			switch (downloadType) {
			case PLACES:
				// Creating ParserTask for parsing Google Places
				placesParserTask = new ParserTask(PLACES);

				// Start parsing google places json data
				// This causes to execute doInBackground() of ParserTask class
				placesParserTask.execute(result);

				break;

			case PLACES_DETAILS:
				// Creating ParserTask for parsing Google Places
				placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

				// Starting Parsing the JSON string
				// This causes to execute doInBackground() of ParserTask class
				placeDetailsParserTask.execute(result);
			}
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		int parserType = 0;

		public ParserTask(int type) {
			this.parserType = type;
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<HashMap<String, String>> list = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				switch (parserType) {
				case PLACES:
					PlaceJSONParser placeJsonParser = new PlaceJSONParser();
					// Getting the parsed data as a List construct
					list = placeJsonParser.parse(jObject);
					break;
				case PLACES_DETAILS:
					PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
					// Getting the parsed data as a List construct
					list = placeDetailsJsonParser.parse(jObject);

				}

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			switch (parserType) {
			case PLACES:
				String[] from = new String[] { "description" };
				int[] to = new int[] { android.R.id.text1 };

				// Creating a SimpleAdapter for the AutoCompleteTextView
				SimpleAdapter adapter = new SimpleAdapter(context, result,
						android.R.layout.simple_list_item_1, from, to);

				// Setting the adapter
				atvPlaces.setAdapter(adapter);
				break;
			case PLACES_DETAILS:

				HashMap<String, String> hm = result.get(0);

				// Getting latitude from the parsed data
				double latitude = Double.parseDouble(hm.get("lat"));

				// Getting longitude from the parsed data
				double longitude = Double.parseDouble(hm.get("lng"));

				// Getting reference to the SupportMapFragment of the //
				// activity_main.xml

				((CreateChallengeActivity) context).getGoogleMap();
				// Getting GoogleMap from SupportMapFragment

				// LatLng point = new LatLng(latitude, longitude);
				selectedLatLng = new LatLng(latitude, longitude);

				CameraUpdate cameraPosition = CameraUpdateFactory
						.newLatLng(selectedLatLng);
				CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(5);

				// ((CreateChallengeActivity) context).getGoogleMap().clear();
				((CreateChallengeActivity) context).getGoogleMap().moveCamera(
						cameraPosition);
				((CreateChallengeActivity) context).getGoogleMap()
						.animateCamera(cameraZoom);
				((CreateChallengeActivity) context)
						.getGoogleMap()
						.addMarker(
								new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromBitmap(((CreateChallengeActivity) context)
														.createPin()))
										.position(selectedLatLng)
										.anchor(0.5f, 1.0f));

				((CreateChallengeActivity) context).getGoogleMap()
						.animateCamera(
								CameraUpdateFactory.newLatLngZoom(
										selectedLatLng, DEFAULT_MAP_ZOOM));

				// Showing the user input location in the Google Map
				/*
				 * googleMap.moveCamera(cameraPosition);
				 * googleMap.animateCamera(cameraZoom);
				 * 
				 * MarkerOptions options = new MarkerOptions();
				 * options.position(point); options.title("Position");
				 * options.snippet
				 * ("Latitude:"+latitude+",Longitude:"+longitude);
				 * 
				 * // Adding the marker in the Google Map
				 * googleMap.addMarker(options);
				 */

				break;
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

}
