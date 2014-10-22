package remoteeyes.bsp.vn;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.DialogFilter;
import remoteeyes.bsp.vn.asynctask.AcceptedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterAcceptedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterCurrentChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterGlobalChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.FilterPostedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.GetAddressAsynctask;
import remoteeyes.bsp.vn.asynctask.GlobalChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadDetailChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadFilterChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadProfileAsynctask;
import remoteeyes.bsp.vn.asynctask.PaymentChallengeAsynctacks;
import remoteeyes.bsp.vn.asynctask.PostedChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.SavePaypalHistoryAsynctacks;
import remoteeyes.bsp.vn.asynctask.UploadPhotoAsynctask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.custom.view.BubbleChallenge;
import remoteeyes.bsp.vn.custom.view.Panel;
import remoteeyes.bsp.vn.fragments.ChallengeListFragment;
import remoteeyes.bsp.vn.fragments.CreateChallengeFragment;
import remoteeyes.bsp.vn.fragments.DetailChallengeFragment;
import remoteeyes.bsp.vn.fragments.MySupportMapFragment;
import remoteeyes.bsp.vn.fragments.TapHintFragment;
import remoteeyes.bsp.vn.model.ChallengeObject;
import remoteeyes.bsp.vn.model.PaymentItem;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.FacebookUtils;
import remoteeyes.bsp.vn.type.ColorChallengeType;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.GPSTracker;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Profile;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

public class MyAreaActivity extends FragmentActivity implements
		OnClickListener, OnTouchListener, OnFocusChangeListener {

	ImageView ivAvatar, ivAdd, ivAddGlobal, ivBackground;
	TextView tvLayoutTitle, tvHintCloudlet, tvCountReward, tvCountChallenge,tv$upaid,tvCountunpaid,tvUnpaid,
			tvChallengeNearby, tvChallengesNearBy, tvThereAreNoChallenge,
			tvCountryLocation, tvCityLocation;
	MySupportMapFragment smfMap;
	GoogleMap gMap;
	Fragment fmSelected;
	ScrollView svMyArea;
	RelativeLayout rlMap;
	Context context;
	public DetailChallengeFragment detailChallengeFragment;
	public ChallengeListFragment challengeListFragment;
	Map<String, BubbleChallenge> bubbleChallengeMap;
	Map<String, BubbleChallenge> todayChallengeMap;
	Map<String, BubbleChallenge> mineChallengeMap;
	Map<String, BubbleChallenge> ignoredChallengeMap;
	LinearLayout llCountChallenge, llMenuArea;
	int countChallenge, countReward;
	TapHintFragment tapHint;
	public boolean isChooseLocation = false;
	Typeface typeface;
	ImageLoader imageLoader;
	Profile myProfile;
	private int DEFAULT_MAP_ZOOM = 15;
	ArrayList<ChallengeObject> currentChallengeList;
	Panel pnMainMenu, pnLocation;
	View vMainMenu, vMenuLocation;
	GPSTracker gps;
	private UiLifecycleHelper uiHelper;
	boolean isMine, isUpcoming, isIgnore;
	public static int page = 1;
	public boolean[] tmp = { true, true, false, false, false,true, false, false,
			true, false };

	RadioButton rbChallengesNearByMenu, rbMyCityMenu, rbMyCountryMenu;
	CheckBox cbShowTodayMenu, cbShowMineMenu, cbShowIgnoredMenu, cbShowHistory;
	TextView tvMyAreaHistoryMenu, tvMyPostedMenu, tvMyAcceptedMenu,
			tvGlobalChallengesMenu, tvMyGroups, tvTellAFriendMenu,
			tvMyAccountMenu, tvName;
	ImageView ivIconNearBy, ivIconMyCity, ivIconMyCountry, ivIconHistory,
			ivIconPosted, ivIconAccepted, ivIconGlobal, ivMapArea, ivRefesh,
			ivShowList, ivFtOption, ivFtBack, ivFtInfo;
	Locale locales;
	RelativeLayout rlFooter, rlContent;
	public static boolean isEnable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = 1;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_my_area);
		this.context = this;
		rlFooter = (RelativeLayout) findViewById(R.id.rl_footer);
		rlContent = (RelativeLayout) findViewById(R.id.rl_content);
		ivFtOption = (ImageView) findViewById(R.id.iv_ftoption);
		ivFtBack = (ImageView) findViewById(R.id.iv_ftback);
		ivFtInfo = (ImageView) findViewById(R.id.iv_ftinfo);

		ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
		ivAdd = (ImageView) findViewById(R.id.iv_add_challenge);
		ivAddGlobal = (ImageView) findViewById(R.id.iv_add_global_challenge);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvLayoutTitle = (TextView) findViewById(R.id.tv_layout_title);
		tvHintCloudlet = (TextView) findViewById(R.id.tv_hint_tap_a_cluodlet);
		tvCountReward = (TextView) findViewById(R.id.tv_count_reward);
		tvCountChallenge = (TextView) findViewById(R.id.tv_count_challenge);
		tvChallengeNearby = (TextView) findViewById(R.id.tv_challenge_nearby);
		tvChallengesNearBy = (TextView) findViewById(R.id.tv_challenges_nearby);
		tvThereAreNoChallenge = (TextView) findViewById(R.id.tv_there_are_no_challenge);
		tv$upaid=(TextView) findViewById(R.id.tv_$_unpaid);
		tvCountunpaid=(TextView) findViewById(R.id.tv_count_unpaid);
		tvUnpaid=(TextView) findViewById(R.id.tv_unpaid);

		fmSelected = (Fragment) getSupportFragmentManager().findFragmentById(
				R.id.fm_selected);
		smfMap = (MySupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		gMap = smfMap.getMap();
		svMyArea = (ScrollView) findViewById(R.id.sv_my_area);
		rlMap = (RelativeLayout) findViewById(R.id.rl_map);
		llCountChallenge = (LinearLayout) findViewById(R.id.ll_count_challenge);
		llMenuArea = (LinearLayout) findViewById(R.id.llMenuArea);

		pnMainMenu = (Panel) findViewById(R.id.pn_main_menu);
		vMainMenu = (View) findViewById(R.id.layout_main_menu);
		tvMyAccountMenu = (TextView) vMainMenu.findViewById(R.id.tv_my_account);
		rbChallengesNearByMenu = (RadioButton) vMainMenu
				.findViewById(R.id.rb_challenges_nearby);
		rbMyCityMenu = (RadioButton) vMainMenu.findViewById(R.id.rb_my_city);
		rbMyCountryMenu = (RadioButton) vMainMenu
				.findViewById(R.id.rb_my_country);
		cbShowTodayMenu = (CheckBox) vMainMenu.findViewById(R.id.cb_show_today);
		cbShowMineMenu = (CheckBox) vMainMenu.findViewById(R.id.cb_show_mine);
		cbShowIgnoredMenu = (CheckBox) vMainMenu
				.findViewById(R.id.cb_show_ignored);
		cbShowHistory = (CheckBox) vMainMenu.findViewById(R.id.cb_show_history);
		tvMyAreaHistoryMenu = (TextView) vMainMenu
				.findViewById(R.id.tv_my_area_history);
		tvMyPostedMenu = (TextView) vMainMenu.findViewById(R.id.tv_my_posted);
		tvMyAcceptedMenu = (TextView) vMainMenu
				.findViewById(R.id.tv_my_accepted);
		tvGlobalChallengesMenu = (TextView) vMainMenu
				.findViewById(R.id.tv_global_challenges);
		tvMyGroups = (TextView) vMainMenu.findViewById(R.id.tv_my_groups);
		tvTellAFriendMenu = (TextView) vMainMenu
				.findViewById(R.id.tv_tell_a_friend);
		ivBackground = (ImageView) findViewById(R.id.layout_bg_panel);
		ivIconNearBy = (ImageView) vMainMenu.findViewById(R.id.iv_icon_nearby);
		ivIconMyCity = (ImageView) vMainMenu.findViewById(R.id.iv_icon_my_city);
		ivIconMyCountry = (ImageView) vMainMenu
				.findViewById(R.id.iv_icon_my_country);
		ivIconHistory = (ImageView) vMainMenu
				.findViewById(R.id.iv_icon_history);
		ivIconPosted = (ImageView) vMainMenu.findViewById(R.id.iv_icon_posted);
		ivIconAccepted = (ImageView) vMainMenu
				.findViewById(R.id.iv_icon_accepted);
		ivIconGlobal = (ImageView) vMainMenu.findViewById(R.id.iv_icon_global);
		ivMapArea = (ImageView) vMainMenu.findViewById(R.id.iv_mapArea);
		ivRefesh = (ImageView) vMainMenu.findViewById(R.id.iv_refesh);
		ivShowList = (ImageView) vMainMenu.findViewById(R.id.iv_show_list);

		pnLocation = (Panel) findViewById(R.id.pn_menu_location);
		vMenuLocation = (View) findViewById(R.id.layout_menu_locaton);
		tvCountryLocation = (TextView) vMenuLocation
				.findViewById(R.id.tv_country_location);
		tvCityLocation = (TextView) vMenuLocation
				.findViewById(R.id.tv_city_location);

		rlFooter.setOnTouchListener(this);
		ivAdd.setOnClickListener(this);
		ivAddGlobal.setOnClickListener(this);
		ivAvatar.setOnClickListener(this);
		ivRefesh.setOnClickListener(this);
		ivShowList.setOnClickListener(this);
		tvMyAccountMenu.setOnClickListener(this);
		bubbleChallengeMap = new HashMap<String, BubbleChallenge>();
		todayChallengeMap = new HashMap<String, BubbleChallenge>();
		mineChallengeMap = new HashMap<String, BubbleChallenge>();
		ignoredChallengeMap = new HashMap<String, BubbleChallenge>();
		rbChallengesNearByMenu.setOnClickListener(this);
		rbMyCityMenu.setOnClickListener(this);
		rbMyCountryMenu.setOnClickListener(this);
		cbShowTodayMenu.setOnClickListener(this);
		cbShowMineMenu.setOnClickListener(this);
		cbShowIgnoredMenu.setOnClickListener(this);
		cbShowHistory.setOnClickListener(this);
		tvMyAreaHistoryMenu.setOnClickListener(this);
		tvMyPostedMenu.setOnClickListener(this);
		tvMyAcceptedMenu.setOnClickListener(this);
		tvGlobalChallengesMenu.setOnClickListener(this);
		tvMyGroups.setOnClickListener(this);
		tvTellAFriendMenu.setOnClickListener(this);
		tvName.setOnClickListener(this);
		llCountChallenge.setVisibility(View.INVISIBLE);
		ivFtOption.setOnClickListener(this);
		ivFtBack.setOnClickListener(this);
		ivFtInfo.setOnClickListener(this);
		ivBackground.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				showMainMenu();
				return false;
			}
		});

		// edit
		String run;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			run = extras.getString("run");
			if (run.equals("Posted")) {
				ivAdd.setVisibility(View.GONE);

				// new PostedChallengeAsynctask(this).execute(Config.IdUser,
				// page
				// + "");
				new FilterPostedChallengeAsynctask(this).execute(Config.IdUser,
						String.valueOf(true), String.valueOf(false),
						String.valueOf(true), "", Config.lat + "", Config.lng
								+ "",String.valueOf(false));
				new PaymentChallengeAsynctacks(this, UserInfo.getInstance().getId()).execute();
				tv$upaid.setVisibility(View.VISIBLE);
				tvCountunpaid.setVisibility(View.VISIBLE);
				tvUnpaid.setVisibility(View.VISIBLE);
				
				// updateTapHint();
			}
			if (run.equals("Accepted")) {
				ivAdd.setVisibility(View.GONE);
				// new AcceptedChallengeAsynctask(this).execute(Config.IdUser);

				new FilterAcceptedChallengeAsynctask(this).execute(
						Config.IdUser, String.valueOf(true),
						String.valueOf(false), String.valueOf(true), "",
						Config.lat + "", Config.lng + "");
				// updateTapHint();
			}
			if (run.equals("Global")) {
				ivAddGlobal.setVisibility(View.VISIBLE);
				ivAdd.setVisibility(View.GONE);
				// new GlobalChallengeAsynctask(this).execute(Config.IdUser);
				new FilterGlobalChallengeAsynctask(this).execute(Config.IdUser,
						String.valueOf(false), String.valueOf(true),
						String.valueOf(false), String.valueOf(false),
						String.valueOf(true), "", page + "");
				// updateTapHint();
			}
			if (run.equals("MyArea")) {
				// new CurrentChallengeAsynctask(context).execute(Config.lat +
				// "",
				// Config.lng + "", Config.IdUser,
				// ShowingChallengeType.STATUS_SHOW_CURRENT + "");

				// gps = new GPSTracker(MyAreaActivity.this);
				// if (gps.canGetLocation()) {
				// Config.lat = gps.getLatitude();
				// Config.lng = gps.getLongitude();
				// } else {
				// gps.showSettingsAlert();
				// }

				new FilterCurrentChallengeAsynctask(this).execute(
						Config.IdUser, Config.lat + "", Config.lng + "",
						String.valueOf(false), String.valueOf(true),
						String.valueOf(false), String.valueOf(false),
						String.valueOf(true), "");
				// updateTapHint();
			}
		}
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
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);

		this.startService(intent);
		updateTitleScreen();
		setMapVisible(View.GONE);
		adjustUserInterface();
		setFontTextView();
		currentChallengeList = new ArrayList<ChallengeObject>();
		uiHelper = new UiLifecycleHelper(this,
				FacebookUtils.sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);

		gps = new GPSTracker(MyAreaActivity.this);
		if (gps.canGetLocation()) {
			Config.lat = gps.getLatitude();
			Config.lng = gps.getLongitude();
		} else {
			gps.showSettingsAlert();
		}

		// new LoadProfileAsynctask(context,
		// this).execute(UserInfo.getInstance()
		// .getId());
		loadAvatar();

		setTiltCamera();
		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				Config.lat, Config.lng), DEFAULT_MAP_ZOOM));

		((MySupportMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.map))
				.setListener(new MySupportMapFragment.OnTouchListener() {

					@Override
					public void onTouch() {
						svMyArea.requestDisallowInterceptTouchEvent(true);
					}
				});

		gMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {

			}
		});

		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				if (checkMyLocationMarkerClick(marker))
					return true;
				// bigger
				Iterator<Entry<String, BubbleChallenge>> iterator = bubbleChallengeMap
						.entrySet().iterator();
				while (iterator.hasNext()) {
					BubbleChallenge bc = (BubbleChallenge) iterator.next()
							.getValue();
					if (bc.getChallengeId().equals(marker.getTitle())) {
						bc.setFocus(true);
					} else {
						bc.setFocus(false);
					}
				}
				gMap.clear();
				showChallengeAround(bubbleChallengeMap);
				callDetailChallengeFragment();
				try {
					new LoadDetailChallengeAsynctask(context).execute(marker
							.getTitle());
				} catch (Exception ex) {
				}

				return true;
			}
		});

	}

	public void refreshChallengeMap() {
		new LoadChallengeAsynctask(context).execute(Config.lat + "", Config.lng
				+ "", Config.IdUser, ShowingChallengeType.CHALLENGE_SHOW_NEARBY
				+ "");
	}

	public void refreshChallengeMapAfterAccept(String id) {
		Iterator<Entry<String, BubbleChallenge>> iterator = bubbleChallengeMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			BubbleChallenge bc = (BubbleChallenge) iterator.next().getValue();
			if (bc.getChallengeId().equals(id)) {
				bc.setColorBubble(ColorChallengeType.ACCEPTED);
				bc.setAccept(ColorChallengeType.ACCEPTED);
			}
		}
		gMap.clear();
		showChallengeAround(bubbleChallengeMap);
	}

	public void refreshChallengeAfterAccept() {
		new GlobalChallengeAsynctask((MyAreaActivity) context)
				.execute(Config.IdUser);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	public void refreshChallengeMapAfterIgnored(String id) {
		bubbleChallengeMap.remove(id);
		gMap.clear();
		showChallengeAround(bubbleChallengeMap);

		TapHintFragment tapHintFragment = new TapHintFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.fm_selected, tapHintFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		// hide
		if (fmSelected.isVisible() || fmSelected != null)
			tvHintCloudlet.setVisibility(View.VISIBLE);
		if (DetailChallengeFragment.isRunCamera) {
			DetailChallengeFragment.isRunCamera = false;
			return;
		}
		if (DetailChallengeFragment.isPhoto) {
			DetailChallengeFragment.isPhoto = false;
			return;
		}
		try {
			if (MyAccountActivity.isEditAvatar) {
				MyAccountActivity.isEditAvatar = false;
				new LoadProfileAsynctask(context, this).execute(UserInfo
						.getInstance().getId());
			}
		} catch (Exception e) {
		}
		try {
			if (!DetailChallengeFragment.challengeIdForUpload.equals("-1")) {
				try {
					// callDetailChallengeFragment();
					new LoadDetailChallengeAsynctask(context)
							.execute(DetailChallengeFragment.challengeIdForUpload);
				} catch (Exception ex) {
				}

				DetailChallengeFragment.challengeIdForUpload = "-1";
				return;
			}
		} catch (Exception ex) {
		}
		if (CreateChallengeFragment.isCancel) {
			CreateChallengeFragment.isCancel = false;
			return;
		}
		if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
			// new CurrentChallengeAsynctask((MyAreaActivity) context).execute(
			// Config.lat + "", Config.lng + "", Config.IdUser,
			// ShowingChallengeType.STATUS_SHOW_CURRENT + "");
			new FilterCurrentChallengeAsynctask(this).execute(Config.IdUser,
					Config.lat + "", Config.lng + "", String.valueOf(false),
					String.valueOf(true), String.valueOf(false),
					String.valueOf(false), String.valueOf(true), "");
			// updateTapHint();
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
			// new PostedChallengeAsynctask(this)
			// .execute(Config.IdUser, page + "");
			new FilterPostedChallengeAsynctask(this).execute(Config.IdUser,
					String.valueOf(true), String.valueOf(false),
					String.valueOf(true), "", Config.lat + "", Config.lng + "",String.valueOf(false));
			new PaymentChallengeAsynctacks(this, UserInfo.getInstance().getId()).execute();
			// updateTapHint();
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
			// new GlobalChallengeAsynctask((MyAreaActivity) context)
			// .execute(Config.IdUser);
			new FilterGlobalChallengeAsynctask(this).execute(Config.IdUser,
					String.valueOf(false), String.valueOf(true),
					String.valueOf(false), String.valueOf(false),
					String.valueOf(true), "", page + "");
			// updateTapHint();
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
			// new AcceptedChallengeAsynctask((MyAreaActivity) context)
			// .execute(Config.IdUser);
			new FilterAcceptedChallengeAsynctask(this).execute(Config.IdUser,
					String.valueOf(true), String.valueOf(false),
					String.valueOf(true), "", Config.lat + "", Config.lng + "");
			// updateTapHint();

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	private void setTiltCamera() {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(Config.lat, Config.lng))
				.zoom(DEFAULT_MAP_ZOOM).tilt(45F).build();

		gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

	private boolean checkMyLocationMarkerClick(Marker marker) {
		return false;
	}

	private boolean checkMyChallengeClick(Marker marker) {
		try {
			BubbleChallenge b = bubbleChallengeMap.get(marker.getTitle());
			if (b.getType() == ColorChallengeType.MINE)
				return true;
			return false;
		} catch (Exception ex) {
			return true;
		}
	}

	public ImageView getAvatar() {
		return ivAvatar;
	}

	public GoogleMap getGoogleMap() {
		return gMap;
	}

	public void showChallengeAround(
			Map<String, BubbleChallenge> bubbleChallengeMap) {
		gMap.clear();
		if (getSupportFragmentManager().findFragmentById(R.id.map).isHidden()) {
			llCountChallenge.setVisibility(View.INVISIBLE);
		} else {
			llCountChallenge.setVisibility(View.VISIBLE);
		}
		countChallenge = 0;
		countReward = 0;
		setMyGPSLocation(gps.getLocation());
		this.bubbleChallengeMap = bubbleChallengeMap;

		Iterator<Entry<String, BubbleChallenge>> iterator = bubbleChallengeMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			BubbleChallenge bc = (BubbleChallenge) iterator.next().getValue();
			if (bc.getLat() != 0 && bc.getLng() != 0) {
				setBubbleChallenge(bc);
				if (bc.getType() != ColorChallengeType.MINE) {
					countChallenge++;
					countReward += bc.getReward();
				}
			}
		}

		if (countChallenge == 0) {
			tvThereAreNoChallenge.setVisibility(View.VISIBLE);
			tvThereAreNoChallenge.setSelected(true);
			llCountChallenge.setVisibility(View.GONE);
		} else if (countChallenge == 1) {
			tvThereAreNoChallenge.setVisibility(View.GONE);
			llCountChallenge.setVisibility(View.VISIBLE);
			tvChallengesNearBy.setVisibility(View.GONE);
			tvChallengeNearby.setVisibility(View.VISIBLE);
		} else {
			tvThereAreNoChallenge.setVisibility(View.GONE);
			llCountChallenge.setVisibility(View.VISIBLE);
			tvChallengesNearBy.setVisibility(View.VISIBLE);
			tvChallengeNearby.setVisibility(View.GONE);
		}

		tvCountReward.setText(countReward + " ");
		tvCountChallenge.setText(countChallenge + " ");
		// tvChallengesNearBy.setText(tvCountReward+"in"+tvCountChallenge+"challenges nearby");
	}

	public void setMyGPSLocation(Location location) {
		if (location != null) {
			gMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromBitmap(createPin()))
					.position(
							new LatLng(location.getLatitude(), location
									.getLongitude())).anchor(0.5f, 1.0f));
		}
	}

	private void setBubbleChallenge(BubbleChallenge bc) {
		bc.focusBubble();
		MarkerOptions marker = new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(bc
						.getBubbleChallengeBitmap()))
				.position(new LatLng(bc.getLat(), bc.getLng()))
				.anchor(0.5f, 1.0f).title(bc.getChallengeId());

		gMap.addMarker(marker);
	}

	public Bitmap createPin() {
		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		Bitmap bitmap = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.pin),
				(int) (widthDevice * 0.25), (int) (widthDevice * 0.12), false);
		return bitmap;
	}

	private void setFontTextView() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvLayoutTitle.setTypeface(typeface);
	}

	private void adjustUserInterface() {

		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		int heightDevice = ResizeUtils.getSizeDevice(this).y;

		ResizeUtils.resizeImageView(ivAvatar, (int) (widthDevice * 0.17),
				(int) (widthDevice * 0.17), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01));

		ResizeUtils.resizeImageView(ivAdd, (int) (widthDevice * 0.14),
				(int) (widthDevice * 0.17), 0, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), 0);

		ResizeUtils.resizeImageView(ivAddGlobal, (int) (widthDevice * 0.14),
				(int) (widthDevice * 0.17), 0, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01), 0);

		ResizeUtils.resizeTextView(tvLayoutTitle, (int) (widthDevice * 0.75),
				(int) (widthDevice * 0.13), -(int) (widthDevice * 0.03), 0, 0,
				0);
		tvLayoutTitle.setTextSize((float) (widthDevice * 0.05));

		ResizeUtils.resizeLinearLayout(llMenuArea, (int) (widthDevice * 0.65),
				(int) (widthDevice * 0.18), (int) (widthDevice * 0.01), 0,
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01));

		ResizeUtils.resizeRelativeLayout(rlFooter, Config.screenWidth,
				(int) (Config.screenWidth * 0.17), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_BOTTOM);

		ResizeUtils.resizeImageView(ivFtOption,
				(int) (Config.screenWidth * 0.14),
				(int) (Config.screenWidth * 0.157),
				(int) (Config.screenWidth * 0.05), 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.CENTER_IN_PARENT);
		ResizeUtils.resizeImageView(ivFtInfo, (int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.15),
				(int) (Config.screenWidth * 0.85), 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.CENTER_IN_PARENT);

		resizeGoogleMap(widthDevice, heightDevice);

		ResizeUtils.resizeTextView(tvHintCloudlet, widthDevice,
				(int) (heightDevice * 0.1), 0, 0, 0, 0, RelativeLayout.BELOW,
				R.id.rl_map);

		// Adjust main menu
		LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.6);
		params.height = (int) (Config.screenHeight * 0.6);
		vMainMenu.setLayoutParams(params);

		params = new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = (int) (Config.screenWidth * 0.4);
		params.height = (int) (Config.screenHeight * 0.12);
		vMenuLocation.setLayoutParams(params);

		ResizeUtils.resizeImageView(ivIconNearBy,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0);
		ResizeUtils.resizeImageView(ivIconMyCity,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0);
		ResizeUtils.resizeImageView(ivIconMyCountry,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0);
		ResizeUtils.resizeImageView(ivIconHistory,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0);
		ResizeUtils.resizeImageView(ivIconPosted,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.07), 0, 0, 0, 0);
		ResizeUtils.resizeImageView(ivIconAccepted,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.07), 0, 0, 0, 0);
		ResizeUtils.resizeImageView(ivIconGlobal,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.07), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivMapArea,
				(int) (Config.screenWidth * 0.09),
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.015), 0,
				(int) (Config.screenWidth * 0.01), 0);
		ResizeUtils.resizeImageView(ivRefesh,
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.15), 0,
				(int) (Config.screenWidth * 0.015), 0);
		ResizeUtils.resizeImageView(ivShowList,
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.07),
				(int) (Config.screenWidth * 0.02), 0,
				(int) (Config.screenWidth * 0.01), 0);

	}

	public void setMapVisible(int v) {
		rlMap.setVisibility(v);
	}

	public void enableScrollView(final boolean enable) {
		svMyArea.smoothScrollTo(0, 0);
		svMyArea.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return !enable;
			}
		});
	}

	public ScrollView getScrollView() {
		return svMyArea;
	}

	public void scrollTo(final int iLocation) {
		svMyArea.postDelayed(new Runnable() {

			@Override
			public void run() {
				svMyArea.smoothScrollBy(0, iLocation);
			}
		}, 100);
	}

	public void enableAdd(boolean b) {
		if (b)
			ivAdd.setVisibility(View.VISIBLE);
		else
			ivAdd.setVisibility(View.INVISIBLE);
	}

	private void resizeGoogleMap(int widthDevice, int heightDevice) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = widthDevice;
		params.height = (int) (heightDevice * 0.8);
		params.setMargins(0, 0, 0, 0);
		params.addRule(RelativeLayout.BELOW, R.id.ll_title);
		smfMap.getView().setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_ftback:
			this.finish();
			break;
		case R.id.iv_ftoption:
			DialogFilter dialogFilter = (DialogFilter) DialogFactory.getDialog(
					context, DialogType.DIALOG_FILTER);
			dialogFilter.show();
			dialogFilter.setActivity(this);
			dialogFilter.setIsCheck();
			break;
		case R.id.iv_ftinfo:
			/*final AlertDialog alertDialog = new AlertDialog.Builder(context)
					.create();

			// Setting Dialog Title
			alertDialog.setTitle("Information for your locaton");

			// get information location
			Geocoder geocoder;
			List<Address> addresses = null;
			geocoder = new Geocoder(this, Locale.getDefault());
			try {
				addresses = geocoder.getFromLocation(Config.lat, Config.lng, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (addresses != null) {
				String city = addresses.get(0).getAdminArea();
				String country = addresses.get(0).getCountryName();
				String district = addresses.get(0).getSubAdminArea();
				if (district == null) {
					district = addresses.get(0).getLocality();
				}
				try {
					if (city.contains("City")) {
						String[] tmp = city.split("City");
						city = tmp[0];
					}
				} catch (Exception ex) {
				}
				try {
					if (district.contains("District")) {
						String[] tmp = district.split("District");
						district = tmp[0];
					}
				} catch (Exception e) {
				}
				// Setting Dialog Message
				alertDialog.setMessage("City: " + city + "\n" + "Country: "
						+ country + "\n" + "District: " + district);
			}

			// Setting Icon to Dialog
			alertDialog.setIcon(getResources().getDrawable(
					R.drawable.ic_info_alert));

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();*/
			new GetAddressAsynctask(context ).execute(Config.lat+"",Config.lng+"");
			break;
		case R.id.iv_refesh:
			cbShowHistory.setChecked(false);
			cbShowIgnoredMenu.setChecked(false);
			cbShowMineMenu.setChecked(false);
			cbShowTodayMenu.setChecked(false);
			tvChallengeNearby.setText(getResources().getString(
					R.string.challenge_nearby));
			tvChallengesNearBy.setText(getResources().getString(
					R.string.challenges_nearby));
			ivAdd.setVisibility(View.VISIBLE);
			rbChallengesNearByMenu.setChecked(true);
			rbMyCityMenu.setChecked(false);
			rbMyCountryMenu.setChecked(false);
			showMainMenu();
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_NEARBY;
			new LoadChallengeAsynctask(context).execute(Config.lat + "",
					Config.lng + "", Config.IdUser,
					ShowingChallengeType.STATUS_SHOW_CURRENT + "");

			break;
		case R.id.iv_show_list:
			if (pnLocation.isOpen()) {
				pnLocation.setOpen(false, true);
			} else {
				pnLocation.setOpen(true, true);
			}
			break;
		case R.id.iv_add_challenge:
			Intent createIntent = new Intent(MyAreaActivity.this,
					CreateChallengeActivity.class);
			startActivity(createIntent);
			this.finish();
			break;
		case R.id.iv_add_global_challenge:
			Intent createGlobalIntent = new Intent(MyAreaActivity.this,
					CreateChallengeActivity.class);
			createGlobalIntent.putExtra("new", "Global");
			startActivity(createGlobalIntent);
			this.finish();
			break;
		case R.id.iv_avatar:
			// showMainMenu();
			break;
		case R.id.tv_my_account:
			Intent i = new Intent(MyAreaActivity.this, MyAccountActivity.class);
			startActivity(i);
			showMainMenu();
			break;
		case R.id.rb_challenges_nearby:
			tvChallengeNearby.setText(getResources().getString(
					R.string.challenge_nearby));
			tvChallengesNearBy.setText(getResources().getString(
					R.string.challenges_nearby));
			ivAdd.setVisibility(View.VISIBLE);
			rbChallengesNearByMenu.setChecked(true);
			rbMyCityMenu.setChecked(false);
			rbMyCountryMenu.setChecked(false);
			showMainMenu();
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_NEARBY;
			new LoadChallengeAsynctask(context).execute(Config.lat + "",
					Config.lng + "", Config.IdUser,
					ShowingChallengeType.STATUS_SHOW_CURRENT + "");

			break;
		case R.id.rb_my_city:
			ivAdd.setVisibility(View.VISIBLE);
			rbChallengesNearByMenu.setChecked(false);
			rbMyCityMenu.setChecked(true);
			rbMyCountryMenu.setChecked(false);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_MY_CITY;
			new LoadChallengeAsynctask(context).execute(Config.lat + "",
					Config.lng + "", Config.IdUser,
					ShowingChallengeType.CHALLENGE_SHOW_MY_CITY + "");
			showMainMenu();
			break;
		case R.id.rb_my_country:
			ivAdd.setVisibility(View.VISIBLE);
			rbChallengesNearByMenu.setChecked(false);
			rbMyCityMenu.setChecked(false);
			rbMyCountryMenu.setChecked(true);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_MY_COUNTRY;
			new LoadChallengeAsynctask(context).execute(Config.lat + "",
					Config.lng + "", Config.IdUser,
					ShowingChallengeType.CHALLENGE_SHOW_MY_COUNTRY + "");
			showMainMenu();
			break;
		case R.id.tv_name:
			ivAdd.setVisibility(View.VISIBLE);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_OPTION;
			if (!cbShowMineMenu.isChecked() && !cbShowTodayMenu.isChecked()
					&& !cbShowIgnoredMenu.isChecked()
					&& !cbShowHistory.isChecked()) {
				showMainMenu();
				return;
			}
			new LoadFilterChallengeAsynctask(context).execute(Config.IdUser,
					Config.lat + "", Config.lng + "",
					String.valueOf(cbShowMineMenu.isChecked()),
					String.valueOf(cbShowTodayMenu.isChecked()),
					String.valueOf(cbShowIgnoredMenu.isChecked()),
					String.valueOf(cbShowHistory.isChecked()));
			showMainMenu();
			break;
		case R.id.cb_show_today:
		case R.id.cb_show_mine:
		case R.id.cb_show_ignored:
		case R.id.cb_show_history:
			// showChallengeMap(bubbleChallengeMap);
			break;
		case R.id.tv_my_area_history:
			ivAdd.setVisibility(View.VISIBLE);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_MY_AREA_HISTORY;
			new LoadFilterChallengeAsynctask(context).execute(Config.IdUser,
					Config.lat + "", Config.lng + "", "false", "false",
					"false", "true");
			showMainMenu();
			break;
		case R.id.tv_my_posted:
			ivAdd.setVisibility(View.GONE);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_POSTED;
			
			new PostedChallengeAsynctask(this).execute(Config.IdUser);
			tv$upaid.setVisibility(View.VISIBLE);
			tvCountunpaid.setVisibility(View.VISIBLE);
			tvUnpaid.setVisibility(View.VISIBLE);
			showMainMenu();
			break;
		case R.id.tv_my_accepted:
			ivAdd.setVisibility(View.GONE);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED;
			new AcceptedChallengeAsynctask(this).execute(Config.IdUser);
			showMainMenu();
			break;
		case R.id.tv_global_challenges:
			ivAdd.setVisibility(View.GONE);
			ShowingChallengeType.STATUS_SHOW_CURRENT = ShowingChallengeType.CHALLENGE_SHOW_GLOBAL;
			new GlobalChallengeAsynctask(this).execute(Config.IdUser);
			showMainMenu();
			break;
		case R.id.tv_my_groups:
			Intent iMyGroup = new Intent(this, MyGroupActivity.class);
			startActivity(iMyGroup);
			showMainMenu();
			break;
		case R.id.tv_tell_a_friend:
			Intent iTellAGroup = new Intent(this, TellAFriendActivity.class);
			startActivity(iTellAGroup);
			showMainMenu();
			break;
		default:
			break;
		}
	}

	public void callDetailChallengeFragment() {
		android.app.FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		detailChallengeFragment = new DetailChallengeFragment(false);
		detailChallengeFragment.setContext(this);
		fragmentTransaction.replace(R.id.fm_selected, detailChallengeFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public void setMyAreaTitle() {
		tvLayoutTitle.setText(getResources().getString(R.string.my_area));
		llCountChallenge.setVisibility(View.VISIBLE);
	}

	public void callUploadChallenge() {
		Intent intent = new Intent();
		intent.setType("*/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, Config.UPLOAD_CHALLENGE);
	}

	public void callCamera() {
		Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Config.fileUri = Uri.fromFile(getOutputPhotoFile());
		intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Config.fileUri);
		startActivityForResult(intentCamera, Config.CAPTURE_IMAGE_ACTIVITY_REQ);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data);
		if (FacebookUtils.currentSession != null)
			FacebookUtils.currentSession.onActivityResult(this, requestCode,
					resultCode, data);
		switch (requestCode) {
		case Config.REQUEST_CODE_PAYMENT_area:
			if (resultCode == RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						JSONObject object1 = confirm.toJSONObject();
						JSONObject object2 = confirm.getPayment()
								.toJSONObject();

						String id_payment = DetailChallengeFragment.challengeIdForUpload;

						new SavePaypalHistoryAsynctacks(this, id_payment,
								object1.toString(), object2.toString())
								.execute();
						Log.i(Config.TAG, confirm.toJSONObject().toString(4));
						Log.i(Config.TAG, confirm.getPayment().toJSONObject()
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
						if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
							for (ChallengeObject co : currentChallengeList) {
								if (co.getId().equals(id_payment)) {
									co.setIspaid(1);
								}
							}
						}
						if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
							new FilterGlobalChallengeAsynctask(this).execute(
									Config.IdUser, String.valueOf(false),
									String.valueOf(true),
									String.valueOf(false),
									String.valueOf(false),
									String.valueOf(true), "", page + "");
							{
								for (ChallengeObject co : currentChallengeList) {
									if (co.getId().equals(id_payment)) {
										co.setIspaid(1);
									} else {
										Toast.makeText(
												this.getApplicationContext(),
												"fail", Toast.LENGTH_LONG)
												.show();
									}
								}
							}
						}
						if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
							new FilterPostedChallengeAsynctask(this).execute(
									Config.IdUser, String.valueOf(true),
									String.valueOf(false),
									String.valueOf(true), "", Config.lat + "",
									Config.lng + "",String.valueOf(false));
							{
								for (ChallengeObject co : currentChallengeList) {
									if (co.getId().equals(id_payment)) {
										co.setIspaid(1);
									}
								}
							}
						}

						Toast.makeText(
								this.getApplicationContext(),
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
		case Config.REQUEST_CODE_FUTURE_PAYMENT_area:
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
						Toast.makeText(this.getApplicationContext(),
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
			break;
		case Config.UPLOAD_CHALLENGE:
			Uri mImageCaptureUri = null;
			String path = null;

			// if (requestCode == PICK_FROM_FILE) {
			if (data != null) {
				mImageCaptureUri = data.getData();
				path = getRealPathFromURI(mImageCaptureUri); // from Gallery
			}

			if (path == null && mImageCaptureUri != null)
				path = mImageCaptureUri.getPath(); // from File Manager

			if (path != null) {
				if (!DetailChallengeFragment.challengeIdForUpload.equals("-1")) {
					new UploadPhotoAsynctask(this).execute(path,
							DetailChallengeFragment.challengeIdForUpload, "0");
				}
			}

			break;
		case Config.CAPTURE_IMAGE_ACTIVITY_REQ:
			if (resultCode == RESULT_OK) {
				Uri photoUri = null;
				String imgPath = null;
				// A known bug here! The image should have saved in fileUri
				photoUri = Config.fileUri;
				// if (requestCode == PICK_FROM_FILE) {
				imgPath = getRealPathFromURI(photoUri); // from Gallery

				if (imgPath == null && photoUri != null)
					imgPath = photoUri.getPath(); // from File Manager

				if (imgPath != null) {
					if (!DetailChallengeFragment.challengeIdForUpload
							.equals("-1")) {
						new UploadPhotoAsynctask(this).execute(imgPath,
								DetailChallengeFragment.challengeIdForUpload,
								"0");

					}
				} else {
					photoUri = data.getData();
					Toast.makeText(this,
							"Image saved successfully in: " + data.getData(),
							Toast.LENGTH_LONG).show();
				}
				new LoadDetailChallengeAsynctask(this)
						.execute(DetailChallengeFragment.challengeIdForUpload);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Call out for image capture failed!",
						Toast.LENGTH_LONG).show();
			}
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

	private void showMainMenu() {
		if (pnMainMenu.isOpen()) {
			pnMainMenu.setOpen(false, true);
			pnLocation.setOpen(false, true);
			findViewById(R.id.layout_bg_panel).setVisibility(View.GONE);
			smfMap.getView().setEnabled(true);
			ivAdd.setEnabled(true);
			enableScrollView(true);
		} else {
			pnMainMenu.setOpen(true, true);
			findViewById(R.id.layout_bg_panel).setVisibility(View.VISIBLE);
			smfMap.getView().setEnabled(false);
			ivAdd.setEnabled(false);
			enableScrollView(false);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			// if (pnMainMenu.isOpen()) {
			// pnMainMenu.setOpen(false, true);
			// pnLocation.setOpen(false, true);
			// findViewById(R.id.layout_bg_panel).setVisibility(View.GONE);
			// smfMap.getView().setEnabled(true);
			// ivAdd.setEnabled(true);
			// enableScrollView(true);
			// } else {
			// finish();
			// }
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showChallengeList(ArrayList<ChallengeObject> objList) {
		setMapVisible(View.GONE);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		ChallengeListFragment challengeListFragment = new ChallengeListFragment();
		TapHintFragment taphintd = new TapHintFragment();
		challengeListFragment.setActivity(this);
		if (objList.size() == 0) {
			fragmentTransaction.replace(R.id.fm_selected, taphintd);
		} else {
			fragmentTransaction
					.replace(R.id.fm_selected, challengeListFragment);
		}
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		challengeListFragment.setActivity(this);
		challengeListFragment.setAdapter(objList);
		tvChallengeNearby.setText(getResources().getString(
				R.string.budgetchallenge));
		tvChallengesNearBy.setText(getResources().getString(
				R.string.budgetchallenges));
		// challengeListFragment.setLayout(rlContent);
		// updateTitleScreen();
		int countCha = 0, countRew = 0,countUnpaid=0;
new PaymentChallengeAsynctacks(this, UserInfo.getInstance().getId()).execute();
		
		ArrayList<PaymentItem> paymentItemList= UserInfo.getInstance().getPaymentList();
		if(paymentItemList.size()!=0){
			for(int i=0;i<paymentItemList.size();i++){
				PaymentItem co1= paymentItemList.get(i);
				countUnpaid+=Integer.parseInt(co1.getBudget());
				
			}
			
		}
		for (int i = 0; i < objList.size(); i++) {
			ChallengeObject co = objList.get(i);
			countCha++;
			countRew += co.getReward();
		}
		// if (Config.data.size() == Config.total) {
		// countCha--;
		// }
		if (objList.size() >= 5) {
			countCha--;
		}
		updateCountChallenge(countCha, countRew,countUnpaid);

	}

	public void showChallengeMap(Map<String, BubbleChallenge> bubbleChallengeMap) {
		try {
			this.bubbleChallengeMap = bubbleChallengeMap;
			Map<String, BubbleChallenge> showTodayList = new HashMap<String, BubbleChallenge>();
			Map<String, BubbleChallenge> showMineList = new HashMap<String, BubbleChallenge>();
			Map<String, BubbleChallenge> showIgnoredList = new HashMap<String, BubbleChallenge>();
			Map<String, BubbleChallenge> showList = new HashMap<String, BubbleChallenge>();

			setMapVisible(View.VISIBLE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			TapHintFragment tapHintFragment = new TapHintFragment();

			fragmentTransaction.replace(R.id.fm_selected, tapHintFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

			int countCha = 0, countRew = 0,countUnpaid=0;
			new PaymentChallengeAsynctacks(this, UserInfo.getInstance().getId()).execute();
			
			ArrayList<PaymentItem> paymentItemList= UserInfo.getInstance().getPaymentList();
			if(paymentItemList.size()!=0){
				for(int i=0;i<paymentItemList.size();i++){
					PaymentItem co1= paymentItemList.get(i);
					countUnpaid+=Integer.parseInt(co1.getBudget());
					
				}
				
			}
			
			Iterator<Entry<String, BubbleChallenge>> iterator = bubbleChallengeMap
					.entrySet().iterator();
			while (iterator.hasNext()) {
				BubbleChallenge bc = (BubbleChallenge) iterator.next()
						.getValue();
				if (bc.getLat() != 0 && bc.getLng() != 0) {
					if (bc.getType() != ColorChallengeType.MINE) {
						countCha++;
						countRew += bc.getReward();
					}
				}

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Calendar cal = Calendar.getInstance();
				String today = dateFormat.format(cal.getTime()) + "";
				if (today.equals(bc.getStarting_on_year() + "/"
						+ bc.getStarting_on_month() + "/"
						+ bc.getStarting_on_day())) {
					showTodayList.put(bc.getChallengeId(), bc);
				}
				if (bc.getType() == ColorChallengeType.MINE) {
					showMineList.put(bc.getChallengeId(), bc);
				}
				if (bc.getType() == ColorChallengeType.IGNORED) {
					showIgnoredList.put(bc.getChallengeId(), bc);
				}

				updateTitleScreen();
			}

			if (cbShowTodayMenu.isChecked()) {
				Iterator<Entry<String, BubbleChallenge>> iteratorToday = bubbleChallengeMap
						.entrySet().iterator();
				while (iteratorToday.hasNext()) {
					BubbleChallenge bcToday = (BubbleChallenge) iteratorToday
							.next().getValue();
					showList.put(bcToday.getChallengeId(), bcToday);
				}
			}
			if (cbShowMineMenu.isChecked()) {
				Iterator<Entry<String, BubbleChallenge>> iteratorMine = bubbleChallengeMap
						.entrySet().iterator();
				while (iteratorMine.hasNext()) {
					BubbleChallenge bcMine = (BubbleChallenge) iteratorMine
							.next().getValue();
					showList.put(bcMine.getChallengeId(), bcMine);
				}
			}
			if (cbShowIgnoredMenu.isChecked()) {
				Iterator<Entry<String, BubbleChallenge>> iteratorIgnored = bubbleChallengeMap
						.entrySet().iterator();
				while (iteratorIgnored.hasNext()) {
					BubbleChallenge bcIgnored = (BubbleChallenge) iteratorIgnored
							.next().getValue();
					showList.put(bcIgnored.getChallengeId(), bcIgnored);
				}
			}

			showChallengeAround(bubbleChallengeMap);
			updateCountChallenge(countCha, countRew,countUnpaid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setCurrentChallengeList(ArrayList<ChallengeObject> challengeList) {
		this.currentChallengeList = challengeList;
	}

	public void removeChalllenge(String id) {
		for (ChallengeObject co : currentChallengeList) {
			if (co.getId().equals(id)) {
				currentChallengeList.remove(co);
				return;
			}
		}
		Iterator<Entry<String, BubbleChallenge>> iterator = bubbleChallengeMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			BubbleChallenge bc = (BubbleChallenge) iterator.next().getValue();
			if (bc.getChallengeId().equals(id)) {
				bubbleChallengeMap.remove(id);
			}
		}
	}

	public void refreshChallengeList() {
		if (rlMap.getVisibility() == View.VISIBLE)
			showChallengeAround(bubbleChallengeMap);
		else
			showChallengeList(currentChallengeList);
	}

	private void updateTitleScreen() {
		if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
			// tvHintCloudlet.setText(getResources().getText(
			// R.string.tap_hint_global));
			tvLayoutTitle.setText(getResources().getString(
					R.string.global_challenges));
			ResizeUtils.resizeImageView(ivFtBack,
					(int) (Config.screenWidth * 0.1),
					(int) (Config.screenWidth * 0.15),
					(int) (Config.screenWidth * 0.85), 0, 0, 0,
					RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.CENTER_IN_PARENT);
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
			// tvHintCloudlet.setText(getResources().getText(
			// R.string.tap_hint_accept));
			tvLayoutTitle.setText(getResources().getString(
					R.string.my_accepted_challenges));
			ResizeUtils.resizeImageView(ivFtBack,
					(int) (Config.screenWidth * 0.1),
					(int) (Config.screenWidth * 0.15),
					(int) (Config.screenWidth * 0.85), 0, 0, 0,
					RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.CENTER_IN_PARENT);
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED) {
			// tvHintCloudlet.setText(getResources().getText(
			// R.string.tap_hint_posted));
			tvLayoutTitle.setText(getResources().getString(
					R.string.my_posted_challenges));
			ResizeUtils.resizeImageView(ivFtBack,
					(int) (Config.screenWidth * 0.1),
					(int) (Config.screenWidth * 0.15),
					(int) (Config.screenWidth * 0.85), 0, 0, 0,
					RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.CENTER_IN_PARENT);
			tv$upaid.setVisibility(View.VISIBLE);
			tvCountunpaid.setVisibility(View.VISIBLE);
			tvUnpaid.setVisibility(View.VISIBLE);
		} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_AREA_HISTORY)
			tvLayoutTitle.setText(getResources().getString(
					R.string.my_area_history));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_CITY)
			tvLayoutTitle.setText(getResources().getString(R.string.my_city));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_COUNTRY)
			tvLayoutTitle
					.setText(getResources().getString(R.string.my_country));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
			// tvHintCloudlet.setText(getResources().getString(
			// R.string.tap_hint_erea));
			tvLayoutTitle.setText(getResources().getString(R.string.my_area));
			ivFtInfo.setVisibility(View.VISIBLE);
			ResizeUtils.resizeImageView(ivFtBack,
					(int) (Config.screenWidth * 0.1),
					(int) (Config.screenWidth * 0.15), 0,
					(int) (Config.screenWidth * 0.9), 0, 0,
					RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.CENTER_IN_PARENT);
		}
	}

	public void updateCountChallenge(int countChallenge, int countReward, int CountUnpaid) {
		if (countChallenge == 1 || countChallenge == 0) {
			tvThereAreNoChallenge.setVisibility(View.GONE);
			llCountChallenge.setVisibility(View.VISIBLE);
			tvChallengesNearBy.setVisibility(View.GONE);
			tvChallengeNearby.setVisibility(View.VISIBLE);
		} else {
			tvThereAreNoChallenge.setVisibility(View.GONE);
			llCountChallenge.setVisibility(View.VISIBLE);
			tvChallengesNearBy.setVisibility(View.VISIBLE);
			tvChallengeNearby.setVisibility(View.GONE);
		}

		tvCountReward.setText(countReward + " ");
		tvCountChallenge.setText(countChallenge + " ");
		tvCountunpaid.setText(CountUnpaid+" ");
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// New touch started
			isEnable = true;
			break;
		case MotionEvent.ACTION_MOVE:// Finger is moving
			isEnable = true;
			break;
		case MotionEvent.ACTION_UP:// Finger went up
			isEnable = true;
			break;
		default:
			isEnable = false;
			break;
		}
		return false;
	}

	// public void updateTapHint() {
	// if (countChallenge == 0) {
	// FragmentTransaction fragmentTransaction = getSupportFragmentManager()
	// .beginTransaction();
	// ChallengeListFragment challengeListFragment = new
	// ChallengeListFragment();
	// TapHintFragment taphindt = new TapHintFragment();
	//
	// fragmentTransaction.hide(challengeListFragment);
	// fragmentTransaction.show(taphindt);
	// fragmentTransaction.addToBackStack(null);
	// fragmentTransaction.commit();
	//
	// }
	//
	// }

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_footer:
			if (hasFocus) {
				isEnable = true;
			} else {
				isEnable = false;
			}
			break;
		default:
			break;
		}
	}

}
