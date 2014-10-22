package remoteeyes.bsp.vn;

import java.util.ArrayList;

import remateeyes.bsp.vn.dialog.DialogAddMember;
import remateeyes.bsp.vn.dialog.DialogFactory;
import remoteeyes.bsp.vn.asynctask.AddGroupAsynctask;
import remoteeyes.bsp.vn.asynctask.DeleteGroupAsynctask;
import remoteeyes.bsp.vn.asynctask.DeleteMemberInGroupAsynctask;
import remoteeyes.bsp.vn.asynctask.EditGroupByUserAsynctask;
import remoteeyes.bsp.vn.asynctask.LoadGroupByUserAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.Group;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.type.DialogType;
import remoteeyes.bsp.vn.uilt.ImageLoader;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Contacts.GroupsColumns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyGroupActivity extends Activity implements OnClickListener {

	ImageView ivAvatar, ivAddGroup;
	TextView tvTitle;
	Typeface typeface;
	ArrayList<Group> groupList = new ArrayList<Group>();
	LinearLayout llMyGroup;
	ImageLoader imageLoader;
	Context context;
	Activity activity;
	ImageView ivAdd;
	DialogAddMember dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_my_group);

		ivAvatar = (ImageView) findViewById(R.id.iv_edit_avatar);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivAddGroup = (ImageView) findViewById(R.id.iv_add_group);
		llMyGroup = (LinearLayout) findViewById(R.id.ll_my_group);	
		
		ivAddGroup.setOnClickListener(this);
		
		imageLoader = new ImageLoader(this);
		this.context = this;
		this.activity = this;
		
		adjustUserInterface();
		setFontTextView();
		//showMyGroups();
		imageLoader.DisplayImage(UserInfo.getInstance().getAvatar(), ivAvatar);
		new LoadGroupByUserAsynctask(this).execute(Config.IdUser);
	}

	public void showMyGroups() {
		llMyGroup.removeAllViews();
		for (int i = 0; i < groupList.size(); i++) {
			final Group group = groupList.get(i);

			LinearLayout llGroupName = new LinearLayout(this);
			LinearLayout.LayoutParams paramsLLGroupName = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsLLGroupName.width = (int) (Config.screenWidth);
			llGroupName.setLayoutParams(paramsLLGroupName);
			llGroupName.setBackgroundColor(this.getResources().getColor(
					R.color.menu_blue));
			llGroupName.setPadding((int) (Config.screenWidth * 0.015),
					(int) (Config.screenWidth * 0.015),
					(int) (Config.screenWidth * 0.015),
					(int) (Config.screenWidth * 0.015));

			final TextView tvGroupName = new TextView(this);
			LinearLayout.LayoutParams paramsTVGroupName = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			tvGroupName.setLayoutParams(paramsTVGroupName);
			tvGroupName.setText(group.getName());
			tvGroupName.setTextSize(TypedValue.COMPLEX_UNIT_PX, this
					.getResources().getDimension(R.dimen.text_title_xl));
			tvGroupName.setTypeface(null, Typeface.BOLD_ITALIC);
			llGroupName.addView(tvGroupName);
			
			final EditText etGroupName = new EditText(this);
			LinearLayout.LayoutParams paramsETGroupName = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			etGroupName.setLayoutParams(paramsETGroupName);
			etGroupName.setText(group.getName());
			etGroupName.setTextSize(TypedValue.COMPLEX_UNIT_PX, this
					.getResources().getDimension(R.dimen.text_title_xl));
			etGroupName.setTypeface(null, Typeface.BOLD_ITALIC);
			etGroupName.setVisibility(View.GONE);
			etGroupName.setSingleLine(true);
			llGroupName.addView(etGroupName);

			RelativeLayout rlGroupButton = new RelativeLayout(this);
			LinearLayout.LayoutParams paramsGroupButton = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			rlGroupButton.setLayoutParams(paramsGroupButton);

			RelativeLayout.LayoutParams paramsGroupEdit = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsGroupEdit.width = (int) (Config.screenWidth * 0.05);
			paramsGroupEdit.height = (int) (Config.screenWidth * 0.05);
			paramsGroupEdit.setMargins((int) (Config.screenWidth * 0.06), 0, 0,
					0);
			paramsGroupEdit.addRule(RelativeLayout.CENTER_VERTICAL);
			ImageView ivGroupEdit = new ImageView(this);
			ivGroupEdit.setLayoutParams(paramsGroupEdit);
			ivGroupEdit.setBackground(this.getResources().getDrawable(
					R.drawable.pen));
			rlGroupButton.addView(ivGroupEdit);
			ivGroupEdit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(etGroupName.getVisibility() == View.GONE){
						tvGroupName.setVisibility(View.GONE);
						etGroupName.setVisibility(View.VISIBLE);
					} else {
						tvGroupName.setText(etGroupName.getText().toString());
						tvGroupName.setVisibility(View.VISIBLE);
						etGroupName.setVisibility(View.GONE);
						try{
							new EditGroupByUserAsynctask(context).execute(group.getId(), etGroupName.getText().toString());
						} catch(Exception ex){
							ex.printStackTrace();
						}
						
					}
				}
			});

			RelativeLayout.LayoutParams paramsGroupDelete = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsGroupDelete.width = (int) (Config.screenWidth * 0.05);
			paramsGroupDelete.height = (int) (Config.screenWidth * 0.05);
			paramsGroupDelete.setMargins(0, 0,
					(int) (Config.screenWidth * 0.1), 0);
			paramsGroupDelete.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			paramsGroupDelete.addRule(RelativeLayout.CENTER_VERTICAL);
			ImageView ivGroupDelete = new ImageView(this);
			ivGroupDelete.setLayoutParams(paramsGroupDelete);
			ivGroupDelete.setBackground(this.getResources().getDrawable(
					R.drawable.red_subtract));
			rlGroupButton.addView(ivGroupDelete);
			ivGroupDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new DeleteGroupAsynctask(context).execute(group.getId());
				}
			});

			final LinearLayout llItemGroup = new LinearLayout(this);
			LinearLayout.LayoutParams paramsItemGroup = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			llItemGroup.setLayoutParams(paramsItemGroup);
			llItemGroup.setOrientation(LinearLayout.VERTICAL);
			
			RelativeLayout.LayoutParams paramsGroupExpandDown = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsGroupExpandDown.width = (int) (Config.screenWidth * 0.05);
			paramsGroupExpandDown.height = (int) (Config.screenWidth * 0.05);
			paramsGroupExpandDown.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			paramsGroupExpandDown.addRule(RelativeLayout.CENTER_VERTICAL);
			final ImageView ivGroupExpandDown = new ImageView(this);
			ivGroupExpandDown.setLayoutParams(paramsGroupExpandDown);
			ivGroupExpandDown.setBackground(this.getResources().getDrawable(
					R.drawable.arrow_down));
			rlGroupButton.addView(ivGroupExpandDown);
			ivGroupExpandDown.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(llItemGroup.getVisibility() == View.VISIBLE){
						ivGroupExpandDown.setBackgroundResource(R.drawable.arrow_up);
						llItemGroup.setVisibility(View.GONE);
					}
					else{
						ivGroupExpandDown.setBackgroundResource(R.drawable.arrow_down);
						llItemGroup.setVisibility(View.VISIBLE);
					}
					
				}
			});

			llGroupName.addView(rlGroupButton);
			llMyGroup.addView(llGroupName);
			
			for (int j = 0; j < group.getFriendOfGroupList().size(); j++) {
				final Friend friend = group.getFriendOfGroupList().get(j);

				LinearLayout llFriendItem = new LinearLayout(this);
				LinearLayout.LayoutParams paramsFriendItem = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				llFriendItem.setLayoutParams(paramsFriendItem);
				llFriendItem.setOrientation(LinearLayout.HORIZONTAL);
				llFriendItem.setGravity(Gravity.CENTER_VERTICAL);

				ImageView ivAvatar = new ImageView(this);
				LinearLayout.LayoutParams paramsAvatar = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsAvatar.width = (int) (Config.screenWidth * 0.13);
				paramsAvatar.height = (int) (Config.screenWidth * 0.13);
				ivAvatar.setLayoutParams(paramsAvatar);
				ivAvatar.setPadding((int) (Config.screenWidth * 0.01),
						(int) (Config.screenWidth * 0.01),
						(int) (Config.screenWidth * 0.01),
						(int) (Config.screenWidth * 0.01));
				
				if(friend.getContactPicture() == null)
					imageLoader.DisplayImage(friend.getAvatarUrl(), ivAvatar);
				else
					ivAvatar.setImageBitmap(friend.getContactPicture());

				llFriendItem.addView(ivAvatar);

				TextView tvFriendName = new TextView(this);
				LinearLayout.LayoutParams paramsFriendName = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				tvFriendName.setLayoutParams(paramsFriendName);
				tvFriendName.setText(friend.getName());
				tvFriendName.setTypeface(null, Typeface.BOLD);
				tvFriendName.setPadding((int) (Config.screenWidth * 0.03), 0,
						(int) (Config.screenWidth * 0.03), 0);
				llFriendItem.addView(tvFriendName);

				LinearLayout.LayoutParams paramsIcon = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsIcon.width = (int) (Config.screenWidth * 0.05);
				paramsIcon.height = (int) (Config.screenWidth * 0.05);
				paramsIcon.setMargins((int) (Config.screenWidth * 0.005), 0,
						(int) (Config.screenWidth * 0.005), 0);
				ImageView ivFacebook = new ImageView(this);
				ivFacebook.setLayoutParams(paramsIcon);
				ivFacebook.setBackground(this.getResources().getDrawable(
						R.drawable.facebook));
				ImageView ivTwitter = new ImageView(this);
				ivTwitter.setLayoutParams(paramsIcon);
				ivTwitter.setBackground(this.getResources().getDrawable(
						R.drawable.twitter));
				ImageView ivGooglePlus = new ImageView(this);
				ivGooglePlus.setLayoutParams(paramsIcon);
				ivGooglePlus.setBackground(this.getResources().getDrawable(
						R.drawable.googleplus));
				ImageView ivLinkedIn = new ImageView(this);
				ivLinkedIn.setLayoutParams(paramsIcon);
				ivLinkedIn.setBackground(this.getResources().getDrawable(
						R.drawable.linkedin));
				ImageView ivInstagram = new ImageView(this);
				ivInstagram.setLayoutParams(paramsIcon);
				ivInstagram.setBackground(this.getResources().getDrawable(
						R.drawable.instagram));
				ImageView ivYoutube = new ImageView(this);
				ivYoutube.setLayoutParams(paramsIcon);
				ivYoutube.setBackground(this.getResources().getDrawable(
						R.drawable.google));

				if (friend.isFacebook())
					llFriendItem.addView(ivFacebook);
				if (friend.isTwitter())
					llFriendItem.addView(ivTwitter);
				if (friend.isGoogle())
					llFriendItem.addView(ivGooglePlus);
				if (friend.isLinkedIn())
					llFriendItem.addView(ivLinkedIn);
				if (friend.isInstagram())
					llFriendItem.addView(ivInstagram);
				if (friend.isYoutube())
					llFriendItem.addView(ivYoutube);

				LinearLayout llDelete = new LinearLayout(this);
				LinearLayout.LayoutParams paramsLLDelete = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				llDelete.setLayoutParams(paramsLLDelete);
				llDelete.setGravity(Gravity.RIGHT);
				ImageView ivDeleteFriend = new ImageView(this);
				LinearLayout.LayoutParams paramsIVDelete = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsIVDelete.width = (int) (Config.screenWidth * 0.05);
				paramsIVDelete.height = (int) (Config.screenWidth * 0.05);
				paramsIVDelete.setMargins(0, 0,
						(int) (Config.screenWidth * 0.015), 0);
				ivDeleteFriend.setLayoutParams(paramsIVDelete);
				ivDeleteFriend.setBackground(this.getResources().getDrawable(
						R.drawable.gray_subtract));
				llDelete.addView(ivDeleteFriend);// delete mem in group
				ivDeleteFriend.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new DeleteMemberInGroupAsynctask(context).execute(group.getId(),friend.getId());
					}
				});
				llFriendItem.addView(llDelete);
				llItemGroup.addView(llFriendItem);

				TextView tvLine = new TextView(this);
				LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsLine.height = 2;
				tvLine.setLayoutParams(paramsLine);
				tvLine.setBackgroundColor(this.getResources().getColor(
						R.color.shawdow));
				
				llItemGroup.addView(tvLine);
			}

			LinearLayout llMemberAdd = new LinearLayout(this);
			LinearLayout.LayoutParams paramsLLMemmberAdd = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			llMemberAdd.setLayoutParams(paramsLLMemmberAdd);

			ImageView ivAddMember = new ImageView(this);
			LinearLayout.LayoutParams paramsAddMember = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsAddMember.width = (int) (Config.screenWidth * 0.11);
			paramsAddMember.height = (int) (Config.screenWidth * 0.11);
			paramsAddMember.setMargins((int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01), 0,
					(int) (Config.screenWidth * 0.01));
			ivAddMember.setLayoutParams(paramsAddMember);
			ivAddMember.setBackground(this.getResources().getDrawable(
					R.drawable.green_avatar));
			
			ivAddMember.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog = (DialogAddMember)DialogFactory.getDialog(context, DialogType.DIALOG_ADD_MEMBER_GROUP);
					dialog.setIdGroup(group.getId(),group.getName());
					dialog.setActivity(activity);
					dialog.show();
				}
			});

			ImageView ivAddEmail = new ImageView(this);
			LinearLayout.LayoutParams paramsAddEmail = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsAddEmail.width = (int) (Config.screenWidth * 0.11);
			paramsAddEmail.height = (int) (Config.screenWidth * 0.11);
			paramsAddEmail.setMargins((int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01), 0,
					(int) (Config.screenWidth * 0.01));
			ivAddEmail.setLayoutParams(paramsAddEmail);
			ivAddEmail.setPadding((int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01));
			ivAddEmail.setBackground(this.getResources().getDrawable(
					R.drawable.a_cong));

			llMemberAdd.addView(ivAddMember);
			llMemberAdd.addView(ivAddEmail);

			llItemGroup.addView(llMemberAdd);
			llMyGroup.addView(llItemGroup);
		}
	}
	
	private void adjustUserInterface() {
		ResizeUtils.resizeImageView(ivAvatar,
				(int) (Config.screenWidth * 0.17),
				(int) (Config.screenWidth * 0.17), 0, 0, 0, 0);

		ResizeUtils.resizeTextView(tvTitle, (int) (Config.screenWidth * 0.65),
				(int) (Config.screenWidth * 0.1), 0, 0, 0, 0);

		ResizeUtils.resizeImageView(ivAddGroup,
				(int) (Config.screenWidth * 0.135),
				(int) (Config.screenWidth * 0.155), 0,
				(int) (Config.screenWidth * 0.01),
				(int) (Config.screenWidth * 0.01), 0);
	}
	
	public void getFriendsList(){
		dialog.getFriendsList();
	}
	
	public void setFacebookFriendsList(ArrayList<Friend> friendList){
		dialog.setFriendFacebookList(friendList);
	}
	
	public void setTwitterFriendsList(ArrayList<Friend> friendList){
		dialog.setFriendTwitterList(friendList);
	}
	
	public void setGooglePlusFriendsList(ArrayList<Friend> friendList){
		dialog.setFriendGooglePlusList(friendList);
	}
	
	public void setLinkedInFriendsList(ArrayList<Friend> friendList){
		dialog.setFriendLinkedInList(friendList);
	}
	
	public void setInstagramFriendsList(ArrayList<Friend> friendList){
		dialog.setFriendInstagramList(friendList);
	}
	
	public void setYoutubeFriendsList(ArrayList<Friend> friendList){
		dialog.setFriendYoutubeList(friendList);
	}

	public void addAdreadyFriends(ArrayList<Friend> friends){
		dialog.addFriends(friends);
	}
	
	private void setFontTextView() {
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/HomeRunScript-roman.otf");
		tvTitle.setTypeface(typeface);
	}
	
	public void addGroup(Group group){
		groupList.add(group);
	}
	
	public void removeGroup(){
		groupList.clear();
	}
	
	
	
	public void editGroup(String groupId, String name){
		for (Group group : groupList) {
			if(group.getId().equals(groupId)){
				group.setName(name);
			}
		}
	}
	
	public void deleteGroup(String groupId){
		for (int i = 0; i < groupList.size(); i++) {
			if(groupList.get(i).getId().equals(groupId)){
				groupList.remove(i);
			}
		}
		showMyGroups();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_group:
			new AddGroupAsynctask(this).execute(Config.IdUser, "NewGroup");
			break;
		default:
			break;
		}
	}
}
