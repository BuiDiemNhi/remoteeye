package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;
import java.util.Collections;

import org.brickred.socialauth.android.SocialAuthAdapter.Provider;

import com.google.android.gms.common.data.Freezable;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.MemberAdapter;
import remoteeyes.bsp.vn.asynctask.BlockUserAsynctacks;
import remoteeyes.bsp.vn.asynctask.GetInfoUserFromEmailAsynctacks;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.BlockUserItem;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.LinkedProfileItem;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogBlockedUser extends Dialog implements
		android.view.View.OnClickListener {

	static Context context;
	Button btnOk, btnCancel;
	TextView tvTitle;
	static ListView lvMember;
	static Activity activity;
	public static ArrayList<Friend> friendList = new ArrayList<Friend>();
	ImageView ivAddIcon, ivAdd;
	public static EditText etEmail;
	public static String BlockUser = "";
	public static String UnBlockUser = "";
	public static ArrayList<String> arrBlockUser = new ArrayList<String>();
	public static ArrayList<String> arrUnBlockUser = new ArrayList<String>();
	public static boolean flagBlockUser = false;
	public static MemberAdapter memberAdapter;

	public DialogBlockedUser(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_blocked_users);

		btnOk = (Button) findViewById(R.id.btn_ok_member);
		btnCancel = (Button) findViewById(R.id.btn_cancel_member);
		lvMember = (ListView) findViewById(R.id.lv_member);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivAddIcon = (ImageView) findViewById(R.id.iv_add_icon);
		ivAdd = (ImageView) findViewById(R.id.iv_add);
		etEmail = (EditText) findViewById(R.id.et_add_email);

		// if (friendList.size() == 0) {
		loadData(friendList);
		// }

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Should create new adapter in here, not in Background Thread
				try {
					memberAdapter = new MemberAdapter(activity,
							R.layout.item_add_member, friendList, false, true);

					lvMember.setAdapter(memberAdapter);
					// memberAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// memberAdapter.notifyDataSetChanged();
			}
		});
		// memberAdapter = new MemberAdapter(activity, R.layout.item_add_member,
		// friendList, false, true);
		// lvMember.setAdapter(memberAdapter);

		setFontTextView();
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		adjustDialog();
	}

	private void setFontTextView() {
		Typeface typeface = Typeface.createFromAsset(activity.getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private void adjustDialog() {
		Drawable cancelDrawable = context.getResources().getDrawable(
				R.drawable.cancel_button);
		ResizeUtils.resizeButton(
				btnCancel,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		ResizeUtils.resizeButton(
				btnOk,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* cancelDrawable.getIntrinsicHeight() / cancelDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		if (friendList.size() > 5) {
			LinearLayout.LayoutParams paramsLVMember = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsLVMember.height = (int) (Config.screenWidth * 0.5);
			lvMember.setLayoutParams(paramsLVMember);
		}

		ResizeUtils.resizeImageView(ivAddIcon,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.05),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));

		ResizeUtils.resizeImageView(ivAdd, (int) (Config.screenWidth * 0.06),
				(int) (Config.screenWidth * 0.06), 0, 0, 0, 0);

		ResizeUtils.resizeEditText(etEmail, (int) (Config.screenWidth * 0.6),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01));
	}

	public static void RefeshListBlockUser() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Should create new adapter in here, not in Background Thread
				try {
					memberAdapter = new MemberAdapter(activity,
							R.layout.item_add_member, friendList, false, true);

					lvMember.setAdapter(memberAdapter);
					memberAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// memberAdapter.notifyDataSetChanged();
			}
		});

	}

	public static boolean CheckExistFriend(Friend friend) {

		for (int i = 0; i < friendList.size(); i++) {
			if (friend.getEmail().trim().toString()
					.equals(friendList.get(i).getEmail().trim().toString())) {
				return true;
			}
		}
		return false;
	}

	public boolean CheckExistBlockUser(ArrayList<String> arrUnBlockUser) {
		boolean result;
		UserInfo info = new UserInfo().getInstance();
		ArrayList<BlockUserItem> arrBlockList = new ArrayList<BlockUserItem>();
		arrBlockList = info.getBlock_list();
		ArrayList<String> arrUnBlockUser2 = new ArrayList<String>();
		for (int i = 0; i < arrBlockList.size(); i++) {
			arrUnBlockUser2.add(arrBlockList.get(i).getId());
		}
		result = equalLists(arrUnBlockUser, arrUnBlockUser2);
		return result;
	}

	public boolean equalLists(ArrayList<String> one, ArrayList<String> two) {
		if (one == null && two == null) {
			return true;
		}

		if ((one == null && two != null) || one != null && two == null
				|| one.size() != two.size()) {
			return false;
		}

		// to avoid messing the order of the lists we will use a copy
		// as noted in comments by A. R. S.
		one = new ArrayList<String>(one);
		two = new ArrayList<String>(two);

		Collections.sort(one);
		Collections.sort(two);
		return one.equals(two);
	}

	public static boolean CheckExistBlockUser(String id) {
		UserInfo info = new UserInfo().getInstance();
		ArrayList<BlockUserItem> arrBlockList = new ArrayList<BlockUserItem>();
		arrBlockList = info.getBlock_list();
		for (int i = 0; i < arrBlockList.size(); i++) {

			if (id.equals(arrBlockList.get(i).getId())) {
				return true;
			}
		}
		return false;
	}

	public static boolean CheckExistUnBlockUser(String id) {
		UserInfo info = new UserInfo().getInstance();
		int lenght = arrUnBlockUser.size();
		for (int i = 0; i < lenght; i++) {
			if (id.equals(arrUnBlockUser.get(i))) {
				return true;
			}
		}
		return false;
	}

	public String getArrBlockUser(ArrayList<String> arrBlockUser) {
		String block = "";
		for (int i = 0; i < arrBlockUser.size(); i++) {
			block += arrBlockUser.get(i);
			if (i < arrBlockUser.size() - 1) {
				block += ",";
			}
		}
		return block;
	}

	private void loadData(ArrayList<Friend> friendList) {
		UserInfo info = new UserInfo().getInstance();
		ArrayList<BlockUserItem> arrBlockList = new ArrayList<BlockUserItem>();
		arrBlockList = info.getBlock_list();
		int lenght = arrBlockList.size();
		this.arrBlockUser.clear();
		this.friendList.clear();

		// int lenght = friendList.size();
		Friend friend;
		BlockUserItem item;
		for (int i = 0; i < lenght; i++) {
			friend = new Friend();
			item = arrBlockList.get(i);
			arrBlockUser.add(item.getId());
			friend.setAvatarUrl(item.getAvatar().toString().trim());
			friend.setName(item.getName().toString().trim());
			friend.setEmail(item.getEmail().toString().trim());
			friend.setId(item.getId());
			friend.setIdSocial(item.getId());
			friend.setStateBlock(true);
			if (item.getLinkedProfileList().size() > 0) {
				friend.setFacebook(true);
			} else {
				friend.setFacebook(false);
			}

			// ArrayList<LinkedProfileItem> arrayLinked = new
			// ArrayList<LinkedProfileItem>();
			// arrayLinked = item.getLinkedProfileList();
			// LinkedProfileItem profileItem;
			// for (int j = 0; j < arrayLinked.size(); j++) {
			// profileItem = arrayLinked.get(i);
			// if (profileItem.getName().equals(Provider.FACEBOOK.toString())) {
			// friend.setFacebook(true);
			// }
			// else if (profileItem.getName().equals(
			// Provider.TWITTER.toString())) {
			// friend.setTwitter(true);
			//
			// } else if (profileItem.getName().equals(
			// Provider.GOOGLEPLUS.toString())) {
			// friend.setGoogle(true);
			//
			// } else if (profileItem.getName().equals(
			// Provider.LINKEDIN.toString())) {
			// friend.setLinkedIn(true);
			//
			// } else if (profileItem.getName().equals(
			// Provider.INSTAGRAM.toString())) {
			// friend.setInstagram(true);
			//
			// } else if (profileItem.getName().equals(
			// Provider.GOOGLE.toString())) {
			// friend.setYoutube(true);
			// }
			// }

			friendList.add(friend);
			// Friend friend = new Friend();
			// friend.setAvatarUrl("http://u.sera.to/users/75/16575/avatar_mid_1341602772.jpeg");
			// friend.setFacebook(true);
			// friend.setGoogle(true);
			// friend.setInstagram(true);
			// friend.setLinkedIn(true);
			// friend.setTwitter(true);
			// friend.setYoutube(true);
			// friend.setName("Candy Rush");
			// friendList.add(friend);
		}
	}

	private boolean CheckInfomation() {
		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		if (!etEmail.getText().toString().matches(emailPattern)) {
			Toast.makeText(
					activity,
					activity.getResources().getString(
							R.string.email_not_formatted), Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (etEmail.getText().toString().equals("")) {
			etEmail.findFocus();
			Toast.makeText(
					activity,
					activity.getResources().getString(R.string.email_not_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// public static void Dismiss(Activity activity) {
	// ((DialogInterface) activity).dismiss();
	// }
	public void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok_member:

			Config.flag_block_user = CheckExistBlockUser(arrBlockUser);
			Config.flag_unblock_user = arrUnBlockUser.size() > 0 ? true : false;
			boolean flag = false;
			if (arrUnBlockUser.size() > 0) {
				UnBlockUser = getArrBlockUser(arrUnBlockUser);
				UserInfo info = new UserInfo().getInstance();
				// if (!CheckExistBlockUser(arrBlockUser)) {
				new BlockUserAsynctacks(activity, 2, info.getId(), UnBlockUser)
						.execute();

				if (Config.flag_block_user == true) {
					Config.flag_block_user = false;
					dismiss();
				}

				if (DialogBlockedUser.flagBlockUser == true
						&& Config.flag_block_user == false
						&& arrBlockUser.size() == 0) {
					flag = true;
					dismiss();
				}
				// } else {
				// Toast.makeText(activity, "Block account does not exist!",
				// Toast.LENGTH_SHORT).show();
				// }
				// if (arrBlockUser.size() == 0 || flagBlockUser == false) {
				// dismiss();
				// }
				// if (friendList.size() <= info.getBlock_list().size()
				// && flagBlockUser == true) {
				// if (!(arrBlockUser.size() > 0 && flagBlockUser == true)) {
				// dismiss();
				// }
				// }
			}
			if (arrBlockUser.size() > 0 && flagBlockUser == true) {
				flagBlockUser = false;
				BlockUser = getArrBlockUser(arrBlockUser);
				UserInfo info = new UserInfo().getInstance();
				new BlockUserAsynctacks(activity, 1, info.getId(), BlockUser)
						.execute();
				dismiss();

			} else {
				if (flag == false) {
					Toast.makeText(activity,
							"Please enter your information needs change!",
							Toast.LENGTH_SHORT).show();
				} else {
					flag = false;
				}
			}
			hideKeyboard();
			break;
		case R.id.btn_cancel_member:
			arrUnBlockUser.clear();
			arrBlockUser.clear();
			UnBlockUser = "";
			BlockUser = "";
			dismiss();
			break;
		case R.id.iv_add:
			if (!CheckInfomation()) {
				return;
			}
			new GetInfoUserFromEmailAsynctacks(activity, etEmail.getText()
					.toString().trim()).execute();
			break;
		default:
			break;
		}
	}
}
