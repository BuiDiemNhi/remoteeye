package remoteeyes.bsp.vn;

import java.io.InputStream;
import java.util.ArrayList;

import org.brickred.socialauth.android.SocialAuthAdapter;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.squareup.picasso.Picasso;

import remateeyes.bsp.vn.dialog.DialogFactory;
import remateeyes.bsp.vn.dialog.DialogFriendAlready;
import remateeyes.bsp.vn.dialog.DialogSendInvite;
import remoteeyes.bsp.vn.asynctask.ConvertAvatarURLAsynctask;
import remoteeyes.bsp.vn.common.CircleTransform;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.LinkedProfileItem;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.ContactDataListener;
import remoteeyes.bsp.vn.social.network.ResponseListener;
import remoteeyes.bsp.vn.social.network.SocialNetworkSupport;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.type.SocialNetworkType;
import remoteeyes.bsp.vn.uilt.ImageLoader;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
//import android.transition.Visibility;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TellAFriendActivity extends Activity {

	ImageView ivAvatar, ivPhone, ivFacebook, ivTwitter, ivGooglePlus,
			ivLinkedIn, ivInstagram, ivYoutube, ivExpandPhone,
			ivExpandFacebook, ivExpandTwitter, ivExpandGooglePlus,
			ivExpandLinkedIn, ivExpandInstagram, ivExpandYoutube;
	TextView tvTitle, tvFriendAlready, tvTmp;
	LinearLayout llPhoneFriendList, llFacebookFriendList, llTwitterFriendList,
			llGooglePlusFriendList, llLinkedInFriendList,
			llInstagramFriendList, llYoutubeFriendList, ll_phone_friend_menu,
			ll_facebook_friend_menu;
	Typeface typeface;
	ImageLoader imageLoader;
	ArrayList<Friend> friendAlreadyList = new ArrayList<Friend>();
	ArrayList<Friend> phoneFriendList = new ArrayList<Friend>();
	ArrayList<Friend> facebookFriendList = new ArrayList<Friend>();
	ArrayList<Friend> twitterFriendList = new ArrayList<Friend>();
	ArrayList<Friend> googlePlusFriendList = new ArrayList<Friend>();
	ArrayList<Friend> linkedInFriendList = new ArrayList<Friend>();
	ArrayList<Friend> instagramFriendList = new ArrayList<Friend>();
	ArrayList<Friend> youtubeFriendList = new ArrayList<Friend>();
	Context context;
	static Activity activity;
	SocialAuthAdapter adapter;
	SocialNetworkSupport socialNetworkSupport;
	public String param;
	public DialogFriendAlready dialog;
	ProgressDialog progress;
	public int number;

	ScrollView svFriend;
	int page = 0, MIN_DISTANCE = 50;
	float downX, downY, upX, upY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_tell_a_friend);
		this.context = this;
		this.activity = this;
		progress = new ProgressDialog(context);
		progress.setMessage("Loading...");
		progress.show();
		// getNumber(this.getContentResolver());

		svFriend = (ScrollView) findViewById(R.id.sv_friend);
		svFriend.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					downX = event.getX();
					downY = event.getY();
				}
				case MotionEvent.ACTION_UP: {
					upX = event.getX();
					upY = event.getY();

					float deltaX = downX - upX;
					float deltaY = downY - upY;

					// swipe horizontal?
					if (Math.abs(deltaX) > MIN_DISTANCE) {
						// left or right
						if (deltaX < 0) {
							return true;
						}
						if (deltaX > 0) {
							return true;
						}
					}
					// swipe vertical?
					if (Math.abs(deltaY) > MIN_DISTANCE) {
						// top or down
						if (deltaY < 0) {
							return true;
						}
						if (deltaY > 0) {
							page++;
							showFriendFaceBook(facebookFriendList,
									llFacebookFriendList);
							return true;
						}
					}
				}
				}
				return false;
			}
		});
		ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivPhone = (ImageView) findViewById(R.id.iv_phone);
		ivFacebook = (ImageView) findViewById(R.id.iv_facebook);
		ivTwitter = (ImageView) findViewById(R.id.iv_twitter);
		ivGooglePlus = (ImageView) findViewById(R.id.iv_googleplus);
		ivLinkedIn = (ImageView) findViewById(R.id.iv_linkedin);
		ivInstagram = (ImageView) findViewById(R.id.iv_instagram);
		ivYoutube = (ImageView) findViewById(R.id.iv_youtube);
		ivExpandPhone = (ImageView) findViewById(R.id.iv_expand_phone);
		ivExpandFacebook = (ImageView) findViewById(R.id.iv_expand_facebook);
		ivExpandTwitter = (ImageView) findViewById(R.id.iv_expand_twitter);
		ivExpandGooglePlus = (ImageView) findViewById(R.id.iv_expand_google_plus);
		ivExpandLinkedIn = (ImageView) findViewById(R.id.iv_expand_linked_in);
		ivExpandInstagram = (ImageView) findViewById(R.id.iv_expand_instagram);
		ivExpandYoutube = (ImageView) findViewById(R.id.iv_expand_youtube);
		llPhoneFriendList = (LinearLayout) findViewById(R.id.ll_phone_friend_list);
		ll_phone_friend_menu = (LinearLayout) findViewById(R.id.ll_phone_friend_menu);
		llFacebookFriendList = (LinearLayout) findViewById(R.id.ll_facebook_friend_list);
		ll_facebook_friend_menu = (LinearLayout) findViewById(R.id.ll_facebook_friend_menu);
		llTwitterFriendList = (LinearLayout) findViewById(R.id.ll_twitter_friend_list);
		llGooglePlusFriendList = (LinearLayout) findViewById(R.id.ll_google_plus_friend_list);
		llLinkedInFriendList = (LinearLayout) findViewById(R.id.ll_linked_in_friend_list);
		llInstagramFriendList = (LinearLayout) findViewById(R.id.ll_instagram_friend_list);
		llYoutubeFriendList = (LinearLayout) findViewById(R.id.ll_youtube_friend_list);
		tvFriendAlready = (TextView) findViewById(R.id.tv_friend_already);
		tvTmp = (TextView) findViewById(R.id.tv_friend_tmp);
		tvFriendAlready.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = (DialogFriendAlready) DialogFactory.getDialog(context,
						DialogType.DIALOG_FRIEND_ALREADY);
				dialog.setActivity(activity);
				dialog.show();
				dialog.addFriends(friendAlreadyList);
				// new LoadMemberAsynctask(context).execute(param, "0", "0",
				// "0",
				// "0", "0");
			}
		});

		Picasso.with(context).load(UserInfo.getInstance().getAvatar())
				.transform(new CircleTransform()).into(ivAvatar);

		adapter = new SocialAuthAdapter(new ResponseListener(this, this));
		socialNetworkSupport = new SocialNetworkSupport(context, adapter);

		// hide
		ivPhone.setVisibility(View.INVISIBLE);
		ivFacebook.setVisibility(View.INVISIBLE);
		ivTwitter.setVisibility(View.INVISIBLE);
		ivGooglePlus.setVisibility(View.INVISIBLE);
		ivLinkedIn.setVisibility(View.INVISIBLE);
		ivInstagram.setVisibility(View.INVISIBLE);
		ivYoutube.setVisibility(View.INVISIBLE);
		llTwitterFriendList.setVisibility(View.INVISIBLE);
		llGooglePlusFriendList.setVisibility(View.INVISIBLE);
		llLinkedInFriendList.setVisibility(View.INVISIBLE);
		llInstagramFriendList.setVisibility(View.INVISIBLE);
		llYoutubeFriendList.setVisibility(View.INVISIBLE);

		ivPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (llPhoneFriendList.getVisibility() == View.VISIBLE) {
					ivExpandPhone.setBackgroundResource(R.drawable.arrow_up);
					llPhoneFriendList.setVisibility(View.GONE);
				} else {
					ivExpandPhone.setBackgroundResource(R.drawable.arrow_down);
					llPhoneFriendList.setVisibility(View.VISIBLE);
				}

			}
		});
		ivFacebook.setOnClickListener(new OnClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((llPhoneFriendList.getVisibility() == View.VISIBLE)
						&& (llFacebookFriendList.getVisibility() == View.VISIBLE)) {
					ivExpandPhone.setBackgroundResource(R.drawable.arrow_up);
					llPhoneFriendList.setVisibility(View.GONE);
				} else if (llPhoneFriendList.getVisibility() == View.VISIBLE) {
					ivExpandPhone.setBackgroundResource(R.drawable.arrow_up);
					llPhoneFriendList.setVisibility(View.GONE);

					ivExpandFacebook
							.setBackgroundResource(R.drawable.arrow_down);
					llFacebookFriendList.setVisibility(View.VISIBLE);

				}

				else if (llFacebookFriendList.getVisibility() == View.VISIBLE) {
					ivExpandFacebook.setBackgroundResource(R.drawable.arrow_up);
					llFacebookFriendList.setVisibility(View.GONE);
				} else {
					ivExpandFacebook
							.setBackgroundResource(R.drawable.arrow_down);
					llFacebookFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		// getNameEmailDetails();
		getContactFriends();
		adjustUI();
		setFontTextView();
		checkIconLinkedProfile();
		expandEvent();

	}

	// public void getNameEmailDetails() {
	// // ArrayList<String> names = new ArrayList<String>();
	// ArrayList<Friend> friendList = new ArrayList<Friend>();
	// Friend friend;
	// ContentResolver cr = getContentResolver();
	// Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
	// null, null, null);
	// if (cur.getCount() > 0) {
	// while (cur.moveToNext()) {
	// String id = cur.getString(cur
	// .getColumnIndex(ContactsContract.Contacts._ID));
	//
	// Cursor cur1 = cr.query(
	// ContactsContract.CommonDataKinds.Email.CONTENT_URI,
	// null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
	// + " = ?", new String[] { id }, null);
	// while (cur1.moveToNext()) {
	// // to get the contact names
	// friend = new Friend();
	//
	// String name = cur1
	// .getString(cur1
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	// friend.setName(name);
	// Log.e("Name :", name);
	// String email = cur1
	// .getString(cur1
	// .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	// friend.setEmail(email);
	// Log.e("Email", email);
	//
	// String image_uri = cur1
	// .getString(cur1
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
	//
	// if (image_uri != null) {
	// Log.e("Image :", image_uri);
	// }
	// if (email != null) {
	//
	// // Cursor pCur = cr
	// // .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	// // null,
	// // ContactsContract.CommonDataKinds.Phone.CONTACT_ID
	// // + " = ?", new String[] { id },
	// // null);
	// // while (pCur.moveToNext()) {
	// // String phoneNumber = pCur
	// // .getString(pCur
	// // .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	// // Toast.makeText(this,
	// // name + " " + image_uri + " " + email,
	// // Toast.LENGTH_SHORT).show();
	// // }
	// // pCur.close();
	//
	// }
	//
	// }
	// cur1.close();
	// }
	// }
	//
	// }

	// public void getNumber(ContentResolver cr) {
	// String phoneNumber = "";
	// String name = "";
	// String email = "";
	// Cursor phones = cr.query(
	// ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
	// null, null);
	// // use the cursor to access the contacts
	// while (phones.moveToNext()) {
	// name = phones
	// .getString(phones
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	// // get display name
	// phoneNumber = phones
	// .getString(phones
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	// // get phone number
	// String id = phones.getString(phones
	// .getColumnIndex(ContactsContract.Contacts._ID));
	// Cursor emailCur = getContentResolver().query(
	// ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
	// ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
	// new String[] { id }, null);
	// while (emailCur.moveToNext()) {
	// email = emailCur
	// .getString(emailCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	//
	// }
	// emailCur.close();
	// }
	// Toast.makeText(this, phoneNumber + " " + name + " " + email,
	// Toast.LENGTH_SHORT).show();
	//
	// // email = phones
	// // .getString(phones
	// // .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	// // get email
	//
	// }

	// public void getListPhoneNumber() {
	// ArrayList numberList = new ArrayList();
	// Uri baseUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
	// Long.parseLong(Contacts.Data._ID));
	// Uri targetUri = Uri.withAppendedPath(baseUri,
	// Contacts.Data.CONTENT_DIRECTORY);
	// Cursor cursor = getContentResolver().query(targetUri,
	// new String[] { Phone.NUMBER },
	// Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
	// null);
	// startManagingCursor(cursor);
	// while (cursor.moveToNext()) {
	// numberList.add(cursor.getString(0));
	// }
	// cursor.close();
	// Toast.makeText(this, numberList.indexOf(0) + "", Toast.LENGTH_SHORT).s;
	// }

	public void setText(int num) {
		tvFriendAlready.setText(num + " friends ");
	}

	public void setFriendAlreadyList(ArrayList<Friend> friendAlreadyList) {
		this.friendAlreadyList = friendAlreadyList;
	}

	private void checkIconLinkedProfile() {
		UserInfo userInfo = UserInfo.getInstance();
		for (int i = 0; i < userInfo.getLinkedProfileItem().size(); i++) {
			LinkedProfileItem item = userInfo.getLinkedProfileItem().get(i);
			if (item.getName().equals(SocialNetworkType.NAME_FACEBOOK)
					&& !item.getId().equals("")) {
				ivFacebook.setVisibility(View.VISIBLE);
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.FACEBOOK);
			} else if (item.getName().equals(SocialNetworkType.NAME_TWITTER)
					&& !item.getId().equals("")) {
				ivTwitter.setVisibility(View.VISIBLE);
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.TWITTER);
			} else if (item.getName().equals(SocialNetworkType.NAME_GOOGLEPLUS)
					&& !item.getId().equals("")) {
				ivGooglePlus.setVisibility(View.VISIBLE);
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.GOOGLEPLUS);
			} else if (item.getName().equals(SocialNetworkType.NAME_LINKEDIN)
					&& !item.getId().equals("")) {
				ivLinkedIn.setVisibility(View.VISIBLE);
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.LINKEDIN);
			} else if (item.getName().equals(SocialNetworkType.NAME_INSTARGRAM)
					&& !item.getId().equals("")) {
				ivInstagram.setVisibility(View.VISIBLE);
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.INSTARGRAM);
			} else if (item.getName().equals(SocialNetworkType.NAME_YOUTUBE)
					&& !item.getId().equals("")) {
				ivYoutube.setVisibility(View.VISIBLE);
				socialNetworkSupport
						.loginSocialNetwork(SocialNetworkType.YOUTUBE);
			}
		}
		if (ivFacebook.getVisibility() == View.INVISIBLE) {
			tvFriendAlready.setVisibility(View.GONE);
			tvTmp.setVisibility(View.GONE);
			if (progress.isShowing())
				progress.dismiss();
		}
	}

	public void getContact() {
		adapter.getContactListAsync(new ContactDataListener(this));
	}

	private void adjustUI() {
		ResizeUtils.resizeImageView(ivAvatar,
				(int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageView(ivPhone, (int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.6), 0, 0, 0);

		ResizeUtils.resizeImageView(ivFacebook,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivTwitter,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivGooglePlus,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivLinkedIn,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivInstagram,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivYoutube,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandPhone,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandFacebook,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandTwitter,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandGooglePlus,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandLinkedIn,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandInstagram,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivExpandYoutube,
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);
	}

	private void setFontTextView() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}

	private void getContactFriends() {
		// Cursor cursor = this.getContentResolver().query(
		// ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// while (cursor.moveToNext()) {
		// try {
		// Friend friend = new Friend();
		// friend.setName(cursor.getString(cursor
		// .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
		// String contact_id = cursor.getString(cursor
		// .getColumnIndex(ContactsContract.Contacts._ID));
		// Uri uri = ContentUris.withAppendedId(
		// ContactsContract.Contacts.CONTENT_URI,
		// Long.parseLong(contact_id));
		// InputStream input = ContactsContract.Contacts
		// .openContactPhotoInputStream(getContentResolver(), uri);
		// if (input == null) {
		// friend.setContactPicture(BitmapFactory.decodeResource(
		// this.getResources(), R.drawable.stub));
		// } else {
		// friend.setContactPicture(BitmapFactory.decodeStream(input));
		// }
		//
		// // friend.setAvatarUrl(cursor.);
		// phoneFriendList.add(friend);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }

		ArrayList<Friend> friendList = new ArrayList<Friend>();
		Friend friend;
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));

				Cursor cur1 = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = ?", new String[] { id }, null);
				while (cur1.moveToNext()) {
					// to get the contact names
					friend = new Friend();

					String name = cur1
							.getString(cur1
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					friend.setName(name);
					Log.e("Name :", name);
					String email = cur1
							.getString(cur1
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					friend.setEmail(email);
					Log.e("Email", email);

					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI,
							Long.parseLong(id));
					// Uri image_uri = cur1
					// .getString(cur1
					// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(getContentResolver(),
									uri);
					if (input == null) {
						friend.setContactPicture(BitmapFactory.decodeResource(
								this.getResources(), R.drawable.stub));
					} else {
						friend.setContactPicture(BitmapFactory
								.decodeStream(input));
					}
					// if (image_uri != null) {
					// Log.e("Image :", image_uri);
					// }
					if (email != null) {
						phoneFriendList.add(friend);
					}
				}
				cur1.close();
			}
		}
		UserInfo info = new UserInfo().getInstance();
		if (info.getLinkedProfileItem().size() == 0) {
			showAllFriendList();
		}
	}

	public void setFacebookFriendsList(ArrayList<Friend> friendList) {
		this.facebookFriendList = friendList;
	}

	public void setTwitterFriendsList(ArrayList<Friend> friendList) {
		this.twitterFriendList = friendList;
	}

	public void setGooglePlusFriendsList(ArrayList<Friend> friendList) {
		this.googlePlusFriendList = friendList;
	}

	public void setLinkedInFriendsList(ArrayList<Friend> friendList) {
		this.linkedInFriendList = friendList;
	}

	public void setInstagramFriendsList(ArrayList<Friend> friendList) {
		this.instagramFriendList = friendList;
	}

	public void setYoutubeFriendsList(ArrayList<Friend> friendList) {
		this.youtubeFriendList = friendList;
	}

	private void showItemFriend(final Friend friend, LinearLayout llFriend) {
		LinearLayout.LayoutParams paramsLLFriend = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llFriend.setLayoutParams(paramsLLFriend);
		llFriend.setOrientation(LinearLayout.HORIZONTAL);

		ImageView ivAvatarFriend = new ImageView(this);
		LinearLayout.LayoutParams paramsIVAvatarFriend = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		paramsIVAvatarFriend.width = (int) (Config.screenWidth * 0.12);
		paramsIVAvatarFriend.height = (int) (Config.screenWidth * 0.12);
		paramsIVAvatarFriend.setMargins((int) (Config.screenWidth * 0.005),
				(int) (Config.screenWidth * 0.005),
				(int) (Config.screenWidth * 0.005),
				(int) (Config.screenWidth * 0.005));
		ivAvatarFriend.setLayoutParams(paramsIVAvatarFriend);
		if (friend.getContactPicture() == null)
			new ConvertAvatarURLAsynctask(context, ivAvatarFriend)
					.execute(friend.getAvatarUrl());
		else
			ivAvatarFriend.setImageBitmap(friend.getContactPicture());

		TextView tvNameFriend = new TextView(this);
		LinearLayout.LayoutParams paramsTVNameFriend = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		paramsTVNameFriend.setMargins((int) (Config.screenWidth * 0.01), 0, 0,
				0);
		tvNameFriend.setLayoutParams(paramsTVNameFriend);
		tvNameFriend.setText(friend.getName());
		tvNameFriend.setGravity(Gravity.CENTER_VERTICAL);

		LinearLayout llInvite = new LinearLayout(this);
		LinearLayout.LayoutParams paramsLLInvite = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		llInvite.setLayoutParams(paramsLLInvite);
		llInvite.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

		ImageView ivInvite = new ImageView(this);
		LinearLayout.LayoutParams paramsIVInvite = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		Drawable inviteDrawable = this.getResources().getDrawable(
				R.drawable.invite);
		paramsIVInvite.height = (int) (Config.screenWidth * 0.05);
		paramsIVInvite.width = (int) (Config.screenWidth * 0.05
				* inviteDrawable.getIntrinsicWidth() / inviteDrawable
				.getIntrinsicHeight());
		paramsIVInvite.setMargins(0, 0, (int) (Config.screenWidth * 0.02), 0);
		ivInvite.setLayoutParams(paramsIVInvite);
		ivInvite.setBackground(inviteDrawable);
		ivInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (friend.isFacebook()) {
					sendFriendRequest(friend.getIdSocial());
				} else {
					DialogSendInvite dialog = (DialogSendInvite) DialogFactory
							.getDialog(context, DialogType.DIALOG_SEND_INVITE);
					// String name = friend.getName();
					dialog.show();
					dialog.setContact(friend);
				}

			}
		});
		llInvite.addView(ivInvite);
		llFriend.addView(ivAvatarFriend);
		llFriend.addView(tvNameFriend);
		llFriend.addView(llInvite);
	}

	private void showFriend(ArrayList<Friend> friendList,
			LinearLayout llContainer) {
		for (int i = 0; i < friendList.size(); i++) {
			final Friend friend = friendList.get(i);
			LinearLayout llFriend = new LinearLayout(this);
			showItemFriend(friend, llFriend);
			// LinearLayout.LayoutParams paramsLLFriend = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT);
			// llFriend.setLayoutParams(paramsLLFriend);
			// llFriend.setOrientation(LinearLayout.HORIZONTAL);
			//
			// ImageView ivAvatarFriend = new ImageView(this);
			// LinearLayout.LayoutParams paramsIVAvatarFriend = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.WRAP_CONTENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT);
			// paramsIVAvatarFriend.width = (int) (Config.screenWidth * 0.12);
			// paramsIVAvatarFriend.height = (int) (Config.screenWidth * 0.12);
			// paramsIVAvatarFriend.setMargins((int) (Config.screenWidth *
			// 0.005),
			// (int) (Config.screenWidth * 0.005),
			// (int) (Config.screenWidth * 0.005),
			// (int) (Config.screenWidth * 0.005));
			// ivAvatarFriend.setLayoutParams(paramsIVAvatarFriend);
			// if (friend.getContactPicture() == null)
			// new ConvertAvatarURLAsynctask(context, ivAvatarFriend)
			// .execute(friend.getAvatarUrl());
			// else
			// ivAvatarFriend.setImageBitmap(friend.getContactPicture());
			//
			// TextView tvNameFriend = new TextView(this);
			// LinearLayout.LayoutParams paramsTVNameFriend = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.WRAP_CONTENT,
			// LinearLayout.LayoutParams.MATCH_PARENT);
			// paramsTVNameFriend.setMargins((int) (Config.screenWidth * 0.01),
			// 0,
			// 0, 0);
			// tvNameFriend.setLayoutParams(paramsTVNameFriend);
			// tvNameFriend.setText(friend.getName());
			// tvNameFriend.setGravity(Gravity.CENTER_VERTICAL);
			//
			// LinearLayout llInvite = new LinearLayout(this);
			// LinearLayout.LayoutParams paramsLLInvite = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.MATCH_PARENT);
			// llInvite.setLayoutParams(paramsLLInvite);
			// llInvite.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			//
			// ImageView ivInvite = new ImageView(this);
			// LinearLayout.LayoutParams paramsIVInvite = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.WRAP_CONTENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT);
			// Drawable inviteDrawable = this.getResources().getDrawable(
			// R.drawable.invite);
			// paramsIVInvite.height = (int) (Config.screenWidth * 0.05);
			// paramsIVInvite.width = (int) (Config.screenWidth * 0.05
			// * inviteDrawable.getIntrinsicWidth() / inviteDrawable
			// .getIntrinsicHeight());
			// paramsIVInvite.setMargins(0, 0, (int) (Config.screenWidth *
			// 0.02),
			// 0);
			// ivInvite.setLayoutParams(paramsIVInvite);
			// ivInvite.setBackground(inviteDrawable);
			// ivInvite.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (friend.isFacebook()) {
			// sendFriendRequest(friend.getIdSocial());
			// } else {
			// DialogSendInvite dialog = (DialogSendInvite) DialogFactory
			// .getDialog(context,
			// DialogType.DIALOG_SEND_INVITE);
			// String name = friend.getName();
			// dialog.show();
			// dialog.setName(name);
			// }
			//
			// }
			// });
			// llInvite.addView(ivInvite);
			//
			// llFriend.addView(ivAvatarFriend);
			// llFriend.addView(tvNameFriend);
			// llFriend.addView(llInvite);

			llContainer.addView(llFriend);

			if (i < friendList.size() - 1) {
				TextView tvLine = new TextView(this);
				LinearLayout.LayoutParams paramsTVLine = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsTVLine.height = 1;
				paramsTVLine.setMargins((int) (Config.screenWidth * 0.005), 0,
						(int) (Config.screenWidth * 0.02), 0);
				tvLine.setLayoutParams(paramsTVLine);
				tvLine.setBackgroundColor(this.getResources().getColor(
						R.color.shawdow));
				llContainer.addView(tvLine);
			}
		}
	}

	private void showFriendFaceBook(ArrayList<Friend> friendsList,
			LinearLayout llContainer) {
		int sumPage = friendsList.size() / 10;
		int du = friendsList.size() % 10;
		if (page < sumPage || (page == sumPage && du == 0)) {
			for (int i = page * 10; i < (page + 1) * 10; i++) {
				final Friend friend = friendsList.get(i);
				LinearLayout llFriend = new LinearLayout(this);
				showItemFriend(friend, llFriend);
				// LinearLayout.LayoutParams paramsLLFriend = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.MATCH_PARENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// llFriend.setLayoutParams(paramsLLFriend);
				// llFriend.setOrientation(LinearLayout.HORIZONTAL);
				//
				// ImageView ivAvatarFriend = new ImageView(this);
				// LinearLayout.LayoutParams paramsIVAvatarFriend = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// paramsIVAvatarFriend.width = (int) (Config.screenWidth *
				// 0.12);
				// paramsIVAvatarFriend.height = (int) (Config.screenWidth *
				// 0.12);
				// paramsIVAvatarFriend.setMargins(
				// (int) (Config.screenWidth * 0.005),
				// (int) (Config.screenWidth * 0.005),
				// (int) (Config.screenWidth * 0.005),
				// (int) (Config.screenWidth * 0.005));
				// ivAvatarFriend.setLayoutParams(paramsIVAvatarFriend);
				// if (friend.getContactPicture() == null)
				// new ConvertAvatarURLAsynctask(context, ivAvatarFriend)
				// .execute(friend.getAvatarUrl());
				// else
				// ivAvatarFriend.setImageBitmap(friend.getContactPicture());
				//
				// TextView tvNameFriend = new TextView(this);
				// LinearLayout.LayoutParams paramsTVNameFriend = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.MATCH_PARENT);
				// paramsTVNameFriend.setMargins(
				// (int) (Config.screenWidth * 0.01), 0, 0, 0);
				// tvNameFriend.setLayoutParams(paramsTVNameFriend);
				// tvNameFriend.setText(friend.getName());
				// tvNameFriend.setGravity(Gravity.CENTER_VERTICAL);
				//
				// LinearLayout llInvite = new LinearLayout(this);
				// LinearLayout.LayoutParams paramsLLInvite = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.MATCH_PARENT,
				// LinearLayout.LayoutParams.MATCH_PARENT);
				// llInvite.setLayoutParams(paramsLLInvite);
				// llInvite.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				//
				// ImageView ivInvite = new ImageView(this);
				// LinearLayout.LayoutParams paramsIVInvite = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// Drawable inviteDrawable = this.getResources().getDrawable(
				// R.drawable.invite);
				// paramsIVInvite.height = (int) (Config.screenWidth * 0.05);
				// paramsIVInvite.width = (int) (Config.screenWidth * 0.05
				// * inviteDrawable.getIntrinsicWidth() / inviteDrawable
				// .getIntrinsicHeight());
				// paramsIVInvite.setMargins(0, 0,
				// (int) (Config.screenWidth * 0.02), 0);
				// ivInvite.setLayoutParams(paramsIVInvite);
				// ivInvite.setBackground(inviteDrawable);
				// ivInvite.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if (friend.isFacebook()) {
				// sendFriendRequest(friend.getIdSocial());
				// } else {
				// DialogSendInvite dialog = (DialogSendInvite) DialogFactory
				// .getDialog(context,
				// DialogType.DIALOG_SEND_INVITE);
				// String name = friend.getName();
				// dialog.show();
				// dialog.setName(name);
				// }
				//
				// }
				// });
				// llInvite.addView(ivInvite);
				//
				// llFriend.addView(ivAvatarFriend);
				// llFriend.addView(tvNameFriend);
				// llFriend.addView(llInvite);

				llContainer.addView(llFriend);

				if (i < friendsList.size() - 1) {
					TextView tvLine = new TextView(this);
					LinearLayout.LayoutParams paramsTVLine = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					paramsTVLine.height = 1;
					paramsTVLine.setMargins((int) (Config.screenWidth * 0.005),
							0, (int) (Config.screenWidth * 0.02), 0);
					tvLine.setLayoutParams(paramsTVLine);
					tvLine.setBackgroundColor(this.getResources().getColor(
							R.color.shawdow));
					llContainer.addView(tvLine);
				}
			}
		}
		if (page == sumPage && du != 0) {
			for (int i = page * 10; i < (page * 10) + du; i++) {
				final Friend friend = friendsList.get(i);
				LinearLayout llFriend = new LinearLayout(this);
				showItemFriend(friend, llFriend);
				// LinearLayout.LayoutParams paramsLLFriend = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.MATCH_PARENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// llFriend.setLayoutParams(paramsLLFriend);
				// llFriend.setOrientation(LinearLayout.HORIZONTAL);
				//
				// ImageView ivAvatarFriend = new ImageView(this);
				// LinearLayout.LayoutParams paramsIVAvatarFriend = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// paramsIVAvatarFriend.width = (int) (Config.screenWidth *
				// 0.12);
				// paramsIVAvatarFriend.height = (int) (Config.screenWidth *
				// 0.12);
				// paramsIVAvatarFriend.setMargins(
				// (int) (Config.screenWidth * 0.005),
				// (int) (Config.screenWidth * 0.005),
				// (int) (Config.screenWidth * 0.005),
				// (int) (Config.screenWidth * 0.005));
				// ivAvatarFriend.setLayoutParams(paramsIVAvatarFriend);
				// if (friend.getContactPicture() == null)
				// new ConvertAvatarURLAsynctask(context, ivAvatarFriend)
				// .execute(friend.getAvatarUrl());
				// else
				// ivAvatarFriend.setImageBitmap(friend.getContactPicture());
				//
				// TextView tvNameFriend = new TextView(this);
				// LinearLayout.LayoutParams paramsTVNameFriend = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.MATCH_PARENT);
				// paramsTVNameFriend.setMargins(
				// (int) (Config.screenWidth * 0.01), 0, 0, 0);
				// tvNameFriend.setLayoutParams(paramsTVNameFriend);
				// tvNameFriend.setText(friend.getName());
				// tvNameFriend.setGravity(Gravity.CENTER_VERTICAL);
				//
				// LinearLayout llInvite = new LinearLayout(this);
				// LinearLayout.LayoutParams paramsLLInvite = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.MATCH_PARENT,
				// LinearLayout.LayoutParams.MATCH_PARENT);
				// llInvite.setLayoutParams(paramsLLInvite);
				// llInvite.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				//
				// ImageView ivInvite = new ImageView(this);
				// LinearLayout.LayoutParams paramsIVInvite = new
				// LinearLayout.LayoutParams(
				// LinearLayout.LayoutParams.WRAP_CONTENT,
				// LinearLayout.LayoutParams.WRAP_CONTENT);
				// Drawable inviteDrawable = this.getResources().getDrawable(
				// R.drawable.invite);
				// paramsIVInvite.height = (int) (Config.screenWidth * 0.05);
				// paramsIVInvite.width = (int) (Config.screenWidth * 0.05
				// * inviteDrawable.getIntrinsicWidth() / inviteDrawable
				// .getIntrinsicHeight());
				// paramsIVInvite.setMargins(0, 0,
				// (int) (Config.screenWidth * 0.02), 0);
				// ivInvite.setLayoutParams(paramsIVInvite);
				// ivInvite.setBackground(inviteDrawable);
				// ivInvite.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if (friend.isFacebook()) {
				// sendFriendRequest(friend.getIdSocial());
				// } else {
				// DialogSendInvite dialog = (DialogSendInvite) DialogFactory
				// .getDialog(context,
				// DialogType.DIALOG_SEND_INVITE);
				// String name = friend.getName();
				// dialog.show();
				// dialog.setName(name);
				// }
				//
				// }
				// });
				// llInvite.addView(ivInvite);
				//
				// llFriend.addView(ivAvatarFriend);
				// llFriend.addView(tvNameFriend);
				// llFriend.addView(llInvite);

				llContainer.addView(llFriend);

				if (i < friendsList.size() - 1) {
					TextView tvLine = new TextView(this);
					LinearLayout.LayoutParams paramsTVLine = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					paramsTVLine.height = 1;
					paramsTVLine.setMargins((int) (Config.screenWidth * 0.005),
							0, (int) (Config.screenWidth * 0.02), 0);
					tvLine.setLayoutParams(paramsTVLine);
					tvLine.setBackgroundColor(this.getResources().getColor(
							R.color.shawdow));
					llContainer.addView(tvLine);
				}
			}

		}
	}

	// http://stackoverflow.com/questions/10153175/how-to-send-app-requests-to-friends-through-facebook-android-sdk?rq=1
	// https://developers.facebook.com/docs/games/requests/v2.0
	// http://stackoverflow.com/questions/10942906/facebook-how-to-send-app-authorize-request-to-friends?rq=1
	@SuppressWarnings("deprecation")
	private static void sendFriendRequest(String friendId) {
		Facebook facebook = new Facebook(activity.getResources().getString(
				R.string.app_id));
		Bundle params = new Bundle();
		params.putString("title", "Send a Request");
		params.putString("message", "Invited you join to RemoteEyes");
		params.putString("to", friendId);
		facebook.dialog(activity, "apprequests", params, new DialogListener() {
			public void onComplete(Bundle values) {
				Toast.makeText(activity, "Send request successful",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFacebookError(FacebookError error) {
				Toast.makeText(activity, "Send request cancelled",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onError(DialogError e) {
				Toast.makeText(activity, "Send request cancelled",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				Toast.makeText(activity, "Send request cancelled",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	public void showAllFriendList() {
		if (phoneFriendList.size() > 0) {
			ivPhone.setVisibility(View.VISIBLE);
			ivExpandPhone.setBackgroundResource(R.drawable.arrow_down);
			showFriend(phoneFriendList, llPhoneFriendList);
		} else {
			ll_phone_friend_menu.setVisibility(View.GONE);
		}

		if (facebookFriendList.size() > 0) {
			ivExpandFacebook.setBackgroundResource(R.drawable.arrow_down);
			showFriendFaceBook(facebookFriendList, llFacebookFriendList);
		} else {
			ll_facebook_friend_menu.setVisibility(View.GONE);
		}
		if (twitterFriendList.size() > 0) {
			ivExpandTwitter.setBackgroundResource(R.drawable.arrow_down);
			showFriend(twitterFriendList, llTwitterFriendList);
		}
		if (googlePlusFriendList.size() > 0) {
			ivExpandGooglePlus.setBackgroundResource(R.drawable.arrow_down);
			showFriend(googlePlusFriendList, llGooglePlusFriendList);
		}
		if (linkedInFriendList.size() > 0) {
			ivExpandLinkedIn.setBackgroundResource(R.drawable.arrow_down);
			showFriend(linkedInFriendList, llLinkedInFriendList);
		}
		if (instagramFriendList.size() > 0) {
			ivExpandInstagram.setBackgroundResource(R.drawable.arrow_down);
			showFriend(instagramFriendList, llInstagramFriendList);
		}
		if (youtubeFriendList.size() > 0) {
			ivExpandYoutube.setBackgroundResource(R.drawable.arrow_down);
			showFriend(youtubeFriendList, llYoutubeFriendList);
		}
		if (progress.isShowing())
			progress.dismiss();
	}

	private void expandEvent() {
		ivExpandPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llPhoneFriendList.getVisibility() == View.VISIBLE) {
					ivExpandPhone.setBackgroundResource(R.drawable.arrow_up);
					llPhoneFriendList.setVisibility(View.GONE);
				} else {
					ivExpandPhone.setBackgroundResource(R.drawable.arrow_down);
					llPhoneFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		ivExpandFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llFacebookFriendList.getVisibility() == View.VISIBLE) {
					ivExpandFacebook.setBackgroundResource(R.drawable.arrow_up);
					llFacebookFriendList.setVisibility(View.GONE);
				} else {
					ivExpandFacebook
							.setBackgroundResource(R.drawable.arrow_down);
					llFacebookFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		ivExpandTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llTwitterFriendList.getVisibility() == View.VISIBLE) {
					ivExpandTwitter.setBackgroundResource(R.drawable.arrow_up);
					llTwitterFriendList.setVisibility(View.GONE);
				} else {
					ivExpandTwitter
							.setBackgroundResource(R.drawable.arrow_down);
					llTwitterFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		ivExpandGooglePlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llGooglePlusFriendList.getVisibility() == View.VISIBLE) {
					ivExpandGooglePlus
							.setBackgroundResource(R.drawable.arrow_up);
					llGooglePlusFriendList.setVisibility(View.GONE);
				} else {
					ivExpandGooglePlus
							.setBackgroundResource(R.drawable.arrow_down);
					llGooglePlusFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		ivExpandLinkedIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llLinkedInFriendList.getVisibility() == View.VISIBLE) {
					ivExpandLinkedIn.setBackgroundResource(R.drawable.arrow_up);
					llLinkedInFriendList.setVisibility(View.GONE);
				} else {
					ivExpandLinkedIn
							.setBackgroundResource(R.drawable.arrow_down);
					llLinkedInFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		ivExpandInstagram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llInstagramFriendList.getVisibility() == View.VISIBLE) {
					ivExpandInstagram
							.setBackgroundResource(R.drawable.arrow_up);
					llInstagramFriendList.setVisibility(View.GONE);
				} else {
					ivExpandInstagram
							.setBackgroundResource(R.drawable.arrow_down);
					llInstagramFriendList.setVisibility(View.VISIBLE);
				}
			}
		});

		ivExpandYoutube.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llYoutubeFriendList.getVisibility() == View.VISIBLE) {
					ivExpandYoutube.setBackgroundResource(R.drawable.arrow_up);
					llYoutubeFriendList.setVisibility(View.GONE);
				} else {
					ivExpandYoutube
							.setBackgroundResource(R.drawable.arrow_down);
					llYoutubeFriendList.setVisibility(View.VISIBLE);
				}
			}
		});
	}
}
