package remateeyes.bsp.vn.dialog;

import java.util.ArrayList;

import org.brickred.socialauth.android.SocialAuthAdapter;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.adapter.MemberAdapter;
import remoteeyes.bsp.vn.asynctask.AddMemberAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.LinkedProfileItem;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.social.network.ResponseListener;
import remoteeyes.bsp.vn.social.network.SocialNetworkSupport;
import remoteeyes.bsp.vn.type.SocialNetworkType;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogFriendAlready extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	ImageView ivPhone, ivFacebook, ivTwitter, ivLinkedIn, ivInstagram,
			ivGooglePlus, ivYoutube;
	Button btnOk;
	TextView tvTitle;
	ListView lvFriend;
	Activity activity;
	SocialAuthAdapter adapter;
	SocialNetworkSupport socialNetworkSupport;
	ArrayList<Friend> friendList = new ArrayList<Friend>();
	ArrayList<Friend> friendFacebookList = new ArrayList<Friend>();
	ArrayList<Friend> friendTwitterList = new ArrayList<Friend>();
	ArrayList<Friend> friendGooglePlusList = new ArrayList<Friend>();
	ArrayList<Friend> friendLinkedInList = new ArrayList<Friend>();
	ArrayList<Friend> friendInstagramList = new ArrayList<Friend>();
	ArrayList<Friend> friendYoutubeList = new ArrayList<Friend>();

	public DialogFriendAlready(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_friend_already);

		ivPhone = (ImageView) findViewById(R.id.iv_phone);
		ivFacebook = (ImageView) findViewById(R.id.iv_facebook);
		ivTwitter = (ImageView) findViewById(R.id.iv_twitter);
		ivGooglePlus = (ImageView) findViewById(R.id.iv_googleplus);
		ivLinkedIn = (ImageView) findViewById(R.id.iv_linkedin);
		ivInstagram = (ImageView) findViewById(R.id.iv_instagram);
		ivYoutube = (ImageView) findViewById(R.id.iv_youtube);
		btnOk = (Button) findViewById(R.id.btn_ok_friend);
		lvFriend = (ListView) findViewById(R.id.lv_friend);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		adapter = new SocialAuthAdapter(new ResponseListener(context, activity));
		socialNetworkSupport = new SocialNetworkSupport(context, adapter);

		ivTwitter.setVisibility(View.GONE);
		ivGooglePlus.setVisibility(View.GONE);
		ivLinkedIn.setVisibility(View.GONE);
		ivInstagram.setVisibility(View.GONE);
		ivYoutube.setVisibility(View.GONE);

		adjustDialog();
		checkIconLinkedProfile();
		btnOk.setOnClickListener(this);
	}

	private void checkIconLinkedProfile() {
		UserInfo userInfo = UserInfo.getInstance();
		for (int i = 0; i < userInfo.getLinkedProfileItem().size(); i++) {
			LinkedProfileItem item = userInfo.getLinkedProfileItem().get(i);
			if (item.getName().equals(SocialNetworkType.NAME_FACEBOOK)
					&& !item.getId().equals("")) {
				ivFacebook.setVisibility(View.VISIBLE);
//				socialNetworkSupport
//						.loginSocialNetwork(SocialNetworkType.FACEBOOK);
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
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private void adjustDialog() {
		ResizeUtils.resizeImageView(ivPhone, (int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivFacebook,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivTwitter,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivGooglePlus,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivLinkedIn,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivInstagram,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		ResizeUtils.resizeImageView(ivYoutube,
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.08),
				(int) (Config.screenWidth * 0.01), 0, 0, 0);

		Drawable okDrawable = context.getResources().getDrawable(
				R.drawable.ok_button);
		ResizeUtils.resizeButton(
				btnOk,
				(int) (Config.screenWidth * 0.1),
				(int) (Config.screenWidth * 0.1
						* okDrawable.getIntrinsicHeight() / okDrawable
						.getIntrinsicWidth()), 0, 0, 0, 0);

		if (friendList.size() > 5) {
			LinearLayout.LayoutParams paramsLVMember = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsLVMember.height = (int) (Config.screenWidth * 0.6);
			lvFriend.setLayoutParams(paramsLVMember);
		}
	}
	public void setFriendFacebookList(ArrayList<Friend> friendList) {
		this.friendFacebookList = friendList;
	}

	public void setFriendTwitterList(ArrayList<Friend> friendList) {
		this.friendTwitterList = friendList;
	}

	public void setFriendGooglePlusList(ArrayList<Friend> friendList) {
		this.friendGooglePlusList = friendList;
	}

	public void setFriendLinkedInList(ArrayList<Friend> friendList) {
		this.friendLinkedInList = friendList;
	}

	public void setFriendInstagramList(ArrayList<Friend> friendList) {
		this.friendInstagramList = friendList;
	}

	public void setFriendYoutubeList(ArrayList<Friend> friendList) {
		this.friendYoutubeList = friendList;
	}

	public void addFriends(ArrayList<Friend> friends) {
		for (int i = 0; i < friends.size(); i++) {
			friendList.add(friends.get(i));
		}
		MemberAdapter adapter = new MemberAdapter(activity,
				R.layout.item_add_member, friendList, true, false);
		lvFriend.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok_friend:
			dismiss();
			break;
		default:
			break;
		}
	}

}
