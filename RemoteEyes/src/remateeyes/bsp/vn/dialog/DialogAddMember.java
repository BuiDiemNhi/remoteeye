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
public class DialogAddMember extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	ImageView ivPhone, ivFacebook, ivTwitter, ivLinkedIn, ivInstagram,
			ivGooglePlus, ivYoutube;
	Button btnOk, btnCancel;
	TextView tvTitle;
	ListView lvMember;
	Activity activity;
	ArrayList<Friend> friendList = new ArrayList<Friend>();
	SocialAuthAdapter adapter;
	SocialNetworkSupport socialNetworkSupport;
	ArrayList<String> idFriend = new ArrayList<String>();
	ArrayList<Friend> friendFacebookList = new ArrayList<Friend>();
	ArrayList<Friend> friendTwitterList = new ArrayList<Friend>();
	ArrayList<Friend> friendGooglePlusList = new ArrayList<Friend>();
	ArrayList<Friend> friendLinkedInList = new ArrayList<Friend>();
	ArrayList<Friend> friendInstagramList = new ArrayList<Friend>();
	ArrayList<Friend> friendYoutubeList = new ArrayList<Friend>();
	String idGroup , nameGroup;

	public DialogAddMember(Context context) {
		super(context);
		this.context = context;
	}

	public void setIdGroup(String id, String name) {
		idGroup = id;
		nameGroup = name;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_add_member_group);

		ivPhone = (ImageView) findViewById(R.id.iv_phone_member);
		ivFacebook = (ImageView) findViewById(R.id.iv_facebook_memmber);
		ivTwitter = (ImageView) findViewById(R.id.iv_twitter_member);
		ivGooglePlus = (ImageView) findViewById(R.id.iv_googleplus_member);
		ivLinkedIn = (ImageView) findViewById(R.id.iv_linkedin_member);
		ivInstagram = (ImageView) findViewById(R.id.iv_instagram_member);
		ivYoutube = (ImageView) findViewById(R.id.iv_youtube_member);
		btnOk = (Button) findViewById(R.id.btn_ok_member);
		btnCancel = (Button) findViewById(R.id.btn_cancel_member);
		lvMember = (ListView) findViewById(R.id.lv_member);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		adapter = new SocialAuthAdapter(new ResponseListener(context, activity));
		socialNetworkSupport = new SocialNetworkSupport(context, adapter);
		
		String title = "Add member to '"+ nameGroup +"'";
		tvTitle.setText(title);
		
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		adjustDialog();
		checkIconLinkedProfile();
		}

	private void checkIconLinkedProfile() {
		UserInfo userInfo = UserInfo.getInstance();
		for (int i = 0; i < userInfo.getLinkedProfileItem().size(); i++) {
			LinkedProfileItem item = userInfo.getLinkedProfileItem().get(i);
			if (item.getName().equals(SocialNetworkType.NAME_FACEBOOK)
					&& !item.getId().equals("")) {
				ivFacebook.setVisibility(View.VISIBLE);
				socialNetworkSupport.loginSocialNetwork(SocialNetworkType.FACEBOOK);
			} else if (item.getName().equals(SocialNetworkType.NAME_TWITTER)
					&& !item.getId().equals("")) {
				ivTwitter.setVisibility(View.VISIBLE);
				socialNetworkSupport.loginSocialNetwork(SocialNetworkType.TWITTER);
			} else if (item.getName().equals(SocialNetworkType.NAME_GOOGLEPLUS)
					&& !item.getId().equals("")) {
				ivGooglePlus.setVisibility(View.VISIBLE);
				socialNetworkSupport.loginSocialNetwork(SocialNetworkType.GOOGLEPLUS);
			} else if (item.getName().equals(SocialNetworkType.NAME_LINKEDIN)
					&& !item.getId().equals("")) {
				ivLinkedIn.setVisibility(View.VISIBLE);
				socialNetworkSupport.loginSocialNetwork(SocialNetworkType.LINKEDIN);
			} else if (item.getName().equals(SocialNetworkType.NAME_INSTARGRAM)
					&& !item.getId().equals("")) {
				ivInstagram.setVisibility(View.VISIBLE);
				socialNetworkSupport.loginSocialNetwork(SocialNetworkType.INSTARGRAM);
			} else if (item.getName().equals(SocialNetworkType.NAME_YOUTUBE)
					&& !item.getId().equals("")) {
				ivYoutube.setVisibility(View.VISIBLE);
				socialNetworkSupport.loginSocialNetwork(SocialNetworkType.YOUTUBE);
			}
		}
	}
	
	public void getFriendsList(){
		socialNetworkSupport.getFriends();
	}
	
	public void setFriendFacebookList(ArrayList<Friend> friendList){
		this.friendFacebookList = friendList;
	}
	
	public void setFriendTwitterList(ArrayList<Friend> friendList){
		this.friendTwitterList = friendList;
	}
	
	public void setFriendGooglePlusList(ArrayList<Friend> friendList){
		this.friendGooglePlusList = friendList;
	}
	
	public void setFriendLinkedInList(ArrayList<Friend> friendList){
		this.friendLinkedInList = friendList;
	}
	
	public void setFriendInstagramList(ArrayList<Friend> friendList){
		this.friendInstagramList = friendList;
	}
	
	public void setFriendYoutubeList(ArrayList<Friend> friendList){
		this.friendYoutubeList = friendList;
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
			paramsLVMember.height = (int) (Config.screenWidth * 0.6);
			lvMember.setLayoutParams(paramsLVMember);
		}
	}
	
	public void addFriends(ArrayList<Friend> friends){
		for(int i = 0; i < friends.size(); i++){
			friendList.add(friends.get(i));
		}
		MemberAdapter adapter = new MemberAdapter(activity,
				R.layout.item_add_member, friendList, false, false); 
		lvMember.setAdapter(adapter);
		adapter.notifyDataSetChanged();			
	}
	 
	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok_member:
			for (int i = 0; i < friendList.size(); i++) {
				Friend country = friendList.get(i);
				if (country.getSelect()) {
					idFriend.add(country.getId());
				}
			}
			String temp = idFriend.toString();
			temp = temp.replace("[", "");
			temp = temp.replace("]", "");
			new AddMemberAsynctask(context, activity).execute(idGroup, temp);
			dismiss();
			break;
		case R.id.btn_cancel_member:
			dismiss();
			break;
		default:
			break;
		}

	}
}
