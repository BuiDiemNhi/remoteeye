package remoteeyes.bsp.vn;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.DialogReportChallenge;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.asynctask.LoadDetailChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.UploadPhotoAsynctask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.fragments.CreateChallengeFragment;
import remoteeyes.bsp.vn.fragments.DetailChallengeFragment;
import remoteeyes.bsp.vn.fragments.MySupportMapFragment;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.FacebookUtils;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class DetailChallengeActivity extends FragmentActivity implements
		OnClickListener {

	ImageView ivAvatar, ivAdd, ivBackground, ivFtBack, ivFtOption;
	TextView tvLayoutTitle, tvHintCloudlet, tvCountChallenge, tvCountReward,
			tvThereAreNoChallenge, tvChallengesNearBy, tvChallengeNearby;
	LinearLayout llMenuArea, llCountChallenge, llTitle;
	Context context;
	MySupportMapFragment smfMap;
	public DetailChallengeFragment detailChallengeFragment;
	GoogleMap gMap;
	ScrollView svMyArea;

	private int DEFAULT_MAP_ZOOM = 15;
	private UiLifecycleHelper uiHelper;
	Locale locales;
	RelativeLayout rlFooter, rlMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_my_area);
		this.context = this;
		llTitle = (LinearLayout) findViewById(R.id.ll_title);
		detailChallengeFragment = new DetailChallengeFragment(true);

		smfMap = (MySupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		gMap = smfMap.getMap();

		rlFooter = (RelativeLayout) findViewById(R.id.rl_footer);
		ivFtBack = (ImageView) findViewById(R.id.iv_ftback);
		ivFtOption = (ImageView) findViewById(R.id.iv_ftoption);
		ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
		ivAdd = (ImageView) findViewById(R.id.iv_add_challenge);
		tvLayoutTitle = (TextView) findViewById(R.id.tv_layout_title);
		tvHintCloudlet = (TextView) findViewById(R.id.tv_hint_tap_a_cluodlet);
		llMenuArea = (LinearLayout) findViewById(R.id.llMenuArea);
		ivBackground = (ImageView) findViewById(R.id.layout_bg_panel);

		tvCountChallenge = (TextView) findViewById(R.id.tv_count_challenge);
		tvCountReward = (TextView) findViewById(R.id.tv_count_reward);
		tvThereAreNoChallenge = (TextView) findViewById(R.id.tv_there_are_no_challenge);
		llCountChallenge = (LinearLayout) findViewById(R.id.ll_count_challenge);
		tvChallengesNearBy = (TextView) findViewById(R.id.tv_challenges_nearby);
		tvChallengeNearby = (TextView) findViewById(R.id.tv_challenge_nearby);
		svMyArea = (ScrollView) findViewById(R.id.sv_my_area);
		rlMap = (RelativeLayout) findViewById(R.id.rl_map);

		ivAdd.setVisibility(View.GONE);
		ivFtOption.setVisibility(View.GONE);
		llCountChallenge.setVisibility(View.GONE);
		tvThereAreNoChallenge.setVisibility(View.GONE);

		ivFtBack.setOnClickListener(this);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvLayoutTitle.setTypeface(typeface);

		adjustUserInterface();
		callDetailChallengeFragment();
		String challengeId = getIntent().getStringExtra("id");
		new LoadDetailChallengeAsynctask(this).execute(challengeId);

		loadAvatar();
		double lat = Double.parseDouble(getIntent().getStringExtra("lat"));
		double lng = Double.parseDouble(getIntent().getStringExtra("lng"));
		if (lat == 0.0 && lng == 0.0) {
			rlMap.setVisibility(View.GONE);
		}
		String area = getIntent().getStringExtra("area");
		if (!area.equals("")) {
			rlMap.setVisibility(View.GONE);
		}
		addAuthorPin(Config.lat,Config.lng);
		addPin(lat, lng);
		setTiltCamera();
		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),
				DEFAULT_MAP_ZOOM));

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
		updateTitleScreen();
	}

	public void addPin(double lat, double lng) {
		gMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(createPin()))
				.position(new LatLng(lat, lng)).anchor(0.5f, 1.0f));
	}
	public void addAuthorPin(double lat, double lng) {
		gMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(createAuthorPin()))
				.position(new LatLng(lat, lng)).anchor(0.5f, 1.0f));
	}

	public Bitmap createPin() {
		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		Bitmap bitmap = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.pin),
				(int) (widthDevice * 0.25), (int) (widthDevice * 0.12), false);
		return bitmap;
	}
	public Bitmap createAuthorPin() {
		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		Bitmap bitmap = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.author_marker),
				(int) (widthDevice * 0.1), (int) (widthDevice * 0.12), false);
		return bitmap;
	}


	private void setTiltCamera() {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(Config.lat, Config.lng))
				.zoom(DEFAULT_MAP_ZOOM).tilt(45F).build();

		gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	private void adjustUserInterface() {

		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		int heightDevice = ResizeUtils.getSizeDevice(this).y;

		ResizeUtils.resizeImageView(ivAvatar, (int) (widthDevice * 0.13),
				(int) (widthDevice * 0.13), 0, 0, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01));

		ResizeUtils.resizeTextView(tvLayoutTitle, (int) (widthDevice * 0.8),
				(int) (widthDevice * 0.13), 0, 0, 0, 0);
		tvLayoutTitle.setTextSize((float) (widthDevice * 0.05));

		ResizeUtils.resizeLinearLayout(llMenuArea, (int) (widthDevice * 0.8),
				(int) (widthDevice * 0.16), (int) (widthDevice * 0.01), 0,
				(int) (widthDevice * 0.01), (int) (widthDevice * 0.01));

		ivFtBack.setImageDrawable(this.getResources().getDrawable(
				R.drawable.ic_white_back));
		ResizeUtils.resizeImageView(ivFtBack, (int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.15), 0, 0, 0, 0,
				RelativeLayout.CENTER_HORIZONTAL,
				RelativeLayout.CENTER_IN_PARENT);

		resizeGoogleMap(widthDevice, heightDevice);

		ResizeUtils.resizeTextView(tvHintCloudlet, widthDevice,
				(int) (heightDevice * 0.08), 0, 0, 0, 0, RelativeLayout.BELOW,
				R.id.rl_map);

		ResizeUtils.resizeRelativeLayout(rlFooter, Config.screenWidth,
				(int) (Config.screenWidth * 0.17), 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_BOTTOM);
	}

	public void callDetailChallengeFragment() {
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		detailChallengeFragment.setContext(this);
		fragmentTransaction.replace(R.id.fm_selected, detailChallengeFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
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

	private void resizeGoogleMap(int widthDevice, int heightDevice) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = widthDevice;
		params.height = (int) (heightDevice * 0.6);
		params.setMargins(0, 0, 0, 0);
		params.addRule(RelativeLayout.BELOW, R.id.ll_title);
		smfMap.getView().setLayoutParams(params);
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

	public void loadCountChallenge() {
		if (getSupportFragmentManager().findFragmentById(R.id.map).isHidden()) {
			llCountChallenge.setVisibility(View.INVISIBLE);
		} else {
			llCountChallenge.setVisibility(View.VISIBLE);
		}

		int countChallenge = Integer.parseInt(getIntent().getStringExtra(
				"countChallenge"));
		int countReward = Integer.parseInt(getIntent().getStringExtra(
				"countReward"));

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
	}

	private void updateTitleScreen() {
		if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL)
			tvLayoutTitle.setText(getResources().getString(
					R.string.global_challenges));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED)
			tvLayoutTitle.setText(getResources().getString(
					R.string.my_accepted_challenges));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_POSTED)
			tvLayoutTitle.setText(getResources().getString(
					R.string.my_posted_challenges));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_AREA_HISTORY)
			tvLayoutTitle.setText(getResources().getString(
					R.string.my_area_history));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_CITY)
			tvLayoutTitle.setText(getResources().getString(R.string.my_city));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_MY_COUNTRY)
			tvLayoutTitle
					.setText(getResources().getString(R.string.my_country));
		else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY)
			tvLayoutTitle.setText(getResources().getString(R.string.my_area));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void callAddExplanatoryPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, Config.REQUEST_SAVE_2);
	}

	public Bitmap resizeBitmap(String path, int targetW, int targetH) {
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		return BitmapFactory.decodeFile(path, bmOptions);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// uiHelper.onActivityResult(requestCode, resultCode, data);
		if (FacebookUtils.currentSession != null)
			FacebookUtils.currentSession.onActivityResult(this, requestCode,
					resultCode, data);
		switch (requestCode) {
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
		case Config.REQUEST_SAVE_2:
			Bitmap bitmap = null;
			Uri mImagesaveUri = null;
			String pathimg = null;

			// if (requestCode == PICK_FROM_FILE) {
			if (data != null) {
				mImagesaveUri = data.getData();
				pathimg = getRealPathFromURI(mImagesaveUri); // from Gallery
			}

			if (pathimg == null && mImagesaveUri != null)
				pathimg = mImagesaveUri.getPath(); // from File Manager

			if (pathimg != null) {
				try {
					bitmap = resizeBitmap(pathimg, 720, 960);
				} catch (OutOfMemoryError e) {
					System.gc();
					if (bitmap != null && bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
				}
				if (bitmap != null) {

					if (!DetailChallengeFragment.challengeIdForUpload
							.equals("-1")) {
						detailChallengeFragment.addExplanatoryPhoto(bitmap);
						new UploadPhotoAsynctask(this).execute(pathimg,
								DetailChallengeFragment.challengeIdForUpload,
								"3");
					}
					// detailChallengeFragment.addImageChallenge(pathimg);
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
								DetailChallengeFragment.challengeIdForUpload);

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

	@Override
	public void onResume() {
		super.onResume();
		// uiHelper.onResume();
		if (DetailChallengeFragment.isRunCamera) {
			tvHintCloudlet.setVisibility(View.INVISIBLE);
			DetailChallengeFragment.isRunCamera = false;
			return;
		}
		try {
			if (!DetailChallengeFragment.challengeIdForUpload.equals("-1")) {
				try {
					callDetailChallengeFragment();
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_ftback:
			this.finish();

			break;
		default:
			break;
		}
	}
}
