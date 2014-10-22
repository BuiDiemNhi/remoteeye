package remoteeyes.bsp.vn;

import remoteeyes.bsp.vn.asynctask.ReverseGeocodingTask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.fragments.CreateChallengeFragment;
import remoteeyes.bsp.vn.fragments.MySupportMapFragment;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.*;

public class CreateChallengeActivity extends FragmentActivity implements
		OnClickListener, LocationListener {

	ImageView ivAvatarCreate;
	TextView tvLayoutCreate, tvLayoutTitleCreate, tvLine;
	MySupportMapFragment smfMap;
	GoogleMap gMap;
	public CreateChallengeFragment fmCreateChallenge;
	public ScrollView svMyArea;
	RelativeLayout rlMapCreate;
	Context context;
	Typeface typeface;
	LinearLayout llTitle;
	private int DEFAULT_MAP_ZOOM = 15;
	public static boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.layout_create_challenge);
		this.context = this;
		ivAvatarCreate = (ImageView) findViewById(R.id.iv_avatar_create);
		tvLayoutTitleCreate = (TextView) findViewById(R.id.tv_layout_title_create);
		tvLine = (TextView) findViewById(R.id.iv_line);
		llTitle = (LinearLayout) findViewById(R.id.ll_title_create);
		fmCreateChallenge = (CreateChallengeFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fm_create_challenge);
		smfMap = (MySupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_create);
		gMap = smfMap.getMap();
		svMyArea = (ScrollView) findViewById(R.id.sv_my_area_create);
		rlMapCreate = (RelativeLayout) findViewById(R.id.rl_map_create);

		adjustUserInterface();
		setFont();
		loadAvatar();
		rlMapCreate.setVisibility(View.GONE);
		double lat = Config.lat;
		double lng = Config.lng;
		if (lat == 0.0 && lng == 0.0) {
			rlMapCreate.setVisibility(View.GONE);
		}
		addPin(lat, lng);
		setTiltCamera();
		flag = false;
		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),
				DEFAULT_MAP_ZOOM));

		((MySupportMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.map_create))
				.setListener(new MySupportMapFragment.OnTouchListener() {
					@Override
					public void onTouch() {
						svMyArea.requestDisallowInterceptTouchEvent(true);
					}
				});

		gMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub
				try {

					if (fmCreateChallenge.selectedLatLng != null) {
						gMap.clear();
						gMap.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory
								.fromBitmap(createAuthorPin()))
					.position(new LatLng(Config.lat,Config.lng))
					.anchor(0.5f, 1.0f));
						flag = true;
						gMap.animateCamera(CameraUpdateFactory
								.newLatLng(fmCreateChallenge.selectedLatLng));

						gMap.addMarker(new MarkerOptions()
								.icon(BitmapDescriptorFactory
										.fromBitmap(createPin()))
								.position(fmCreateChallenge.selectedLatLng)
								.anchor(0.5f, 1.0f));
						new ReverseGeocodingTask(context,
								fmCreateChallenge.atvPlaces,
								fmCreateChallenge.spArea)
								.execute(fmCreateChallenge.selectedLatLng);
						fmCreateChallenge
								.setLatLng(fmCreateChallenge.selectedLatLng);
						fmCreateChallenge.enableLocationOkButton(true);

					} else {
						if (flag) {
							flag = false;
							gMap.clear();
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		gMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				/*
				 * if(fmCreateChallenge.locationObjectList.size() > 0) return;
				 */
				gMap.clear();
				gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

				gMap.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory.fromBitmap(createPin()))
						.position(latLng).anchor(0.5f, 1.0f));

				// Adding Marker on the touched location with address
				try {
					new ReverseGeocodingTask(context,
							fmCreateChallenge.atvPlaces,
							fmCreateChallenge.spArea).execute(latLng);
					fmCreateChallenge.setLatLng(latLng);
					fmCreateChallenge.enableLocationOkButton(true);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		fmCreateChallenge.setContext(this);
		fmCreateChallenge.setActivity(this);
		fmCreateChallenge.setTermAndConditionClicked();
		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String run = extras.getString("new");
				if (run.equals("Global")) {
					tvLayoutTitleCreate.setText(this.getResources().getString(
							R.string.new_global_challenge_title));
					ivAvatarCreate.setBackground(this.getResources().getDrawable(R.drawable.ic_2));
					fmCreateChallenge.setGlobal();
					setMapVisible(View.GONE);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPin(double lat, double lng) {
		gMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(createPin()))
				.position(new LatLng(lat, lng)).anchor(0.5f, 1.0f));
	}

	private void setTiltCamera() {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(Config.lat, Config.lng))
				.zoom(DEFAULT_MAP_ZOOM).tilt(45F).build();

		gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
				Intent intentArea = new Intent(context, MyAreaActivity.class);
				intentArea.putExtra("run", "MyArea");
				context.startActivity(intentArea);
				
			} else if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
				Intent Global = new Intent(context, MyAreaActivity.class);
				Global.putExtra("run", "Global");
				context.startActivity(Global);
			}
			// CreateChallengeFragment.isCancel = true;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void loadAvatar() {
//		MemoryCache memoryCache = new MemoryCache();
//		Bitmap bitmap = memoryCache.get(Config.IdUser);
//		if (bitmap != null) {
//			ivAvatarCreate.setImageBitmap(bitmap);
//		} else {
//			Picasso.with(this).load(UserInfo.getInstance().getAvatar())
//					.transform(new CircleTransform()).into(ivAvatarCreate);
//		}
		ivAvatarCreate.setBackground(this.getResources().getDrawable(R.drawable.ic_1));
		
	}

	public GoogleMap getGoogleMap() {
		return gMap;
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
		Bitmap bitmapAuthor = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.author_marker),
				(int) (widthDevice * 0.1), (int) (widthDevice * 0.12), false);
		return bitmapAuthor;
	}

	private void setFont() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvLayoutTitleCreate.setTypeface(typeface);
	}

	private void adjustUserInterface() {

		int widthDevice = ResizeUtils.getSizeDevice(this).x;
		int heightDevice = ResizeUtils.getSizeDevice(this).y;

		ResizeUtils.resizeImageView(ivAvatarCreate, (int) (widthDevice * 0.17),
				(int) (widthDevice * 0.17), 0, 0, 0, 0);

		ResizeUtils.resizeTextView(tvLayoutTitleCreate,
				(int) (widthDevice * 0.8), (int) (widthDevice * 0.15), 0, 0, 0,
				0);
		tvLayoutTitleCreate.setTextSize((float) (widthDevice * 0.04));

		ResizeUtils.resizeLinearLayout(llTitle, widthDevice,
				(int) (widthDevice * 0.17), 0, 0, (int) (widthDevice * 0.01),
				(int) (widthDevice * 0.01));

		resizeGoogleMap(widthDevice, heightDevice);

	}

	public void setMapVisible(int v) {
		rlMapCreate.setVisibility(v);
	}

	public void setTitleAndAvatarIvisible(int v) {
		// ivAvatarCreate.setVisibility(v);
		// tvLayoutTitleCreate.setVisibility(v);
		llTitle.setVisibility(v);
		tvLine.setVisibility(v);
	}

	public void showPinOnMap(double lat, double lng) {
		gMap.clear();
		gMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(createPin()))
				.position(new LatLng(lat, lng)).anchor(0.5f, 1.0f));
	}
	public void showPinAuthorOnMap(double lat, double lng) {
		gMap.clear();
		gMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(createAuthorPin()))
				.position(new LatLng(lat, lng)).anchor(0.5f, 1.0f));
	}
	public void showPinAuthorOnMap1(double lat, double lng) {
		
		gMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(createAuthorPin()))
				.position(new LatLng(lat, lng)).anchor(0.5f, 1.0f));
	}

	public void enableScrollView(final boolean enable) {
		
		svMyArea.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
showPinAuthorOnMap(Config.lat, Config.lng);
				return !enable;
			}
		});
	}

	private void resizeGoogleMap(int widthDevice, int heightDevice) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = widthDevice;
		params.height = (int) (heightDevice * 0.35);
		params.setMargins(0, 0, 0, 0);
		smfMap.getView().setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	public void callAddExplanatoryPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, Config.REQUEST_SAVE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Config.REQUEST_SAVE:
			Bitmap bitmap = null;
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
				try {
					bitmap = resizeBitmap(path, 720, 960);
				} catch (OutOfMemoryError e) {
					System.gc();
					if (bitmap != null && bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
				}
				if (bitmap != null) {
					fmCreateChallenge.addExplanatoryPhoto(bitmap);
					fmCreateChallenge.addImageChallenge(path);
				}
			}

			break;
		}
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

	public void enableLocationOkButton(boolean b) {
		fmCreateChallenge.enableLocationOkButton(b);
	}
}
