package remoteeyes.bsp.vn.adapter;

import java.util.ArrayList;

import remateeyes.bsp.vn.dialog.DialogBlockedUser;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.Friend;
import remoteeyes.bsp.vn.model.UserInfo;
import remoteeyes.bsp.vn.uilt.ImageLoader;
import remoteeyes.bsp.vn.uilt.ResizeUtils;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MemberAdapter extends ArrayAdapter<Friend> {

	Activity activity;
	ArrayList<Friend> friendList;
	int layoutId;
	ImageLoader imageLoader;
	boolean isAlreadyFriend = false;
	boolean isBlocked;
	private static final int TYPE_COUNT = 2;
	private static final int TYPE_ITEM1 = 1;
	private static final int TYPE_ITEM2 = 2;

	public MemberAdapter(Activity activity, int layoutId,
			ArrayList<Friend> objList, boolean isAlreadyFriend,
			boolean isBlocked) {
		super(activity, layoutId);
		this.activity = activity;
		this.layoutId = layoutId;
		this.friendList = objList;
		this.isAlreadyFriend = isAlreadyFriend;
		this.isBlocked = isBlocked;
		imageLoader = new ImageLoader(activity);
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {

		Friend item = getItem(position);
		return (item.getStateBlock() == true) ? TYPE_ITEM1 : TYPE_ITEM2;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getCount() {
		return friendList.size();
	}

	@Override
	public Friend getItem(int position) {
		return friendList.get(position);
	}

	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {

		convertView = activity.getLayoutInflater().inflate(layoutId, null);

		final Friend friend = friendList.get(index);

		ImageView ivAvatar = (ImageView) convertView
				.findViewById(R.id.iv_avatar);
		ImageView ivPhone = (ImageView) convertView.findViewById(R.id.iv_phone);
		ImageView ivFacebook = (ImageView) convertView
				.findViewById(R.id.iv_facebook);
		ImageView ivTwitter = (ImageView) convertView
				.findViewById(R.id.iv_twitter);
		ImageView ivGooglePlus = (ImageView) convertView
				.findViewById(R.id.iv_google_plus);
		ImageView ivLinkedIn = (ImageView) convertView
				.findViewById(R.id.iv_linked_in);
		ImageView ivInstagram = (ImageView) convertView
				.findViewById(R.id.iv_instagram);
		ImageView ivYoutube = (ImageView) convertView
				.findViewById(R.id.iv_youtube);
		final ImageView ivUnBlock = (ImageView) convertView
				.findViewById(R.id.iv_unblock);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		CheckBox cbMember = (CheckBox) convertView.findViewById(R.id.cb_member);

		switch (getItemViewType(index)) {
		case TYPE_ITEM1:

			// if (friend.getStateBlock() == true) {
			ivUnBlock.setBackgroundResource(R.drawable.unblock);
			// } else {
			// ivUnBlock.setBackgroundResource(R.drawable.unblock);
			// }

			ivUnBlock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (friend.getStateBlock() == true) {
						try {
							if (DialogBlockedUser.CheckExistBlockUser(friend
									.getId() + "")) {

								friend.setStateBlock(false);
								ivUnBlock
										.setBackgroundResource(R.drawable.block);
								if (!DialogBlockedUser
										.CheckExistUnBlockUser(friend.getId())) {
									DialogBlockedUser.arrUnBlockUser.add(friend
											.getId());
								}
								DialogBlockedUser.arrBlockUser.remove(index);
								DialogBlockedUser.friendList.remove(friend);
								DialogBlockedUser.memberAdapter
										.notifyDataSetChanged();
								DialogBlockedUser.flagBlockUser = true;
							} else {
								Toast.makeText(activity,
										"Block account does not exist!",
										Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// info.getBlock_list().remove(index);
					} else {
						try {
							friend.setStateBlock(true);
							ivUnBlock.setBackgroundResource(R.drawable.unblock);
							DialogBlockedUser.arrBlockUser.add(friend.getId());
							DialogBlockedUser.arrUnBlockUser.remove(friend
									.getId());
							DialogBlockedUser.friendList.add(index, friend);
							DialogBlockedUser.memberAdapter
									.notifyDataSetChanged();
							DialogBlockedUser.flagBlockUser = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});

			cbMember.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// is chkIos checked?
					CheckBox cb = (CheckBox) v;
					friendList.get(index).setSelect(cb.isChecked());
				}
			});
			if (friend.getContactPicture() == null)
				imageLoader.DisplayImage(friend.getAvatarUrl(), ivAvatar);
			else
				ivAvatar.setImageBitmap(friend.getContactPicture());
			tvName.setText(friend.getName());

			ResizeUtils.resizeImageView(ivAvatar,
					(int) (Config.screenWidth * 0.1),
					(int) (Config.screenWidth * 0.1), 0, 0, 0, 0);
			ivAvatar.setPadding((int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01));

			ResizeUtils.resizeImageView(ivPhone,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.02),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivFacebook,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivTwitter,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivGooglePlus,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivLinkedIn,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivInstagram,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivYoutube,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			LinearLayout.LayoutParams paramsCheckbox = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsCheckbox.width = (int) (Config.screenWidth * 0.04);
			paramsCheckbox.height = (int) (Config.screenWidth * 0.04);
			cbMember.setLayoutParams(paramsCheckbox);

			Drawable inviteDrawable = activity.getResources().getDrawable(
					R.drawable.invite);
			ResizeUtils
					.resizeImageView(
							ivUnBlock,
							(int) (Config.screenWidth * 0.05
									* inviteDrawable.getIntrinsicWidth() / inviteDrawable
									.getIntrinsicHeight()),
							(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

			if (isAlreadyFriend)
				cbMember.setVisibility(View.INVISIBLE);
			ivUnBlock.setVisibility(View.INVISIBLE);
			if (isBlocked) {
				cbMember.setVisibility(View.INVISIBLE);
				ivUnBlock.setVisibility(View.VISIBLE);
			}

			if (friend.isFacebook())
				ivFacebook.setVisibility(View.VISIBLE);
			else if (friend.isTwitter())
				ivTwitter.setVisibility(View.VISIBLE);
			else if (friend.isGoogle())
				ivGooglePlus.setVisibility(View.VISIBLE);
			else if (friend.isLinkedIn())
				ivLinkedIn.setVisibility(View.VISIBLE);
			else if (friend.isInstagram())
				ivInstagram.setVisibility(View.VISIBLE);
			else if (friend.isYoutube())
				ivYoutube.setVisibility(View.VISIBLE);
			break;

		case TYPE_ITEM2:

			// if (friend.getStateBlock() == true) {
			// ivUnBlock.setBackgroundResource(R.drawable.block);
			// } else {
			ivUnBlock.setBackgroundResource(R.drawable.block);
			// }

			ivUnBlock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (friend.getStateBlock() == true) {
						try {
							if (DialogBlockedUser.CheckExistBlockUser(friend
									.getId() + "")) {

								friend.setStateBlock(false);
								ivUnBlock
										.setBackgroundResource(R.drawable.block);
								if (!DialogBlockedUser
										.CheckExistUnBlockUser(friend.getId())) {
									DialogBlockedUser.arrUnBlockUser.add(friend
											.getId());
								}
								DialogBlockedUser.arrBlockUser.remove(index);
								DialogBlockedUser.friendList.remove(friend);
								DialogBlockedUser.memberAdapter
										.notifyDataSetChanged();
								DialogBlockedUser.flagBlockUser = true;
							} else {
								Toast.makeText(activity,
										"Block account does not exist!",
										Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// info.getBlock_list().remove(index);
					} else {
						try {
							friend.setStateBlock(true);
							ivUnBlock.setBackgroundResource(R.drawable.unblock);
							DialogBlockedUser.arrBlockUser.add(friend.getId());
							DialogBlockedUser.arrUnBlockUser.remove(friend
									.getId());
							DialogBlockedUser.friendList.add(index, friend);
							DialogBlockedUser.memberAdapter
									.notifyDataSetChanged();
							DialogBlockedUser.flagBlockUser = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});

			cbMember.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// is chkIos checked?
					CheckBox cb = (CheckBox) v;
					friendList.get(index).setSelect(cb.isChecked());
				}
			});
			if (friend.getContactPicture() == null)
				imageLoader.DisplayImage(friend.getAvatarUrl(), ivAvatar);
			else
				ivAvatar.setImageBitmap(friend.getContactPicture());
			tvName.setText(friend.getName());

			ResizeUtils.resizeImageView(ivAvatar,
					(int) (Config.screenWidth * 0.1),
					(int) (Config.screenWidth * 0.1), 0, 0, 0, 0);
			ivAvatar.setPadding((int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01),
					(int) (Config.screenWidth * 0.01));

			ResizeUtils.resizeImageView(ivPhone,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.02),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivFacebook,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivTwitter,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivGooglePlus,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivLinkedIn,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivInstagram,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			ResizeUtils.resizeImageView(ivYoutube,
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.04),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005),
					(int) (Config.screenWidth * 0.005));

			LinearLayout.LayoutParams paramsCheckbox1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsCheckbox1.width = (int) (Config.screenWidth * 0.04);
			paramsCheckbox1.height = (int) (Config.screenWidth * 0.04);
			cbMember.setLayoutParams(paramsCheckbox1);

			Drawable inviteDrawable1 = activity.getResources().getDrawable(
					R.drawable.invite);
			ResizeUtils
					.resizeImageView(
							ivUnBlock,
							(int) (Config.screenWidth * 0.05
									* inviteDrawable1.getIntrinsicWidth() / inviteDrawable1
									.getIntrinsicHeight()),
							(int) (Config.screenWidth * 0.05), 0, 0, 0, 0);

			if (isAlreadyFriend)
				cbMember.setVisibility(View.INVISIBLE);
			ivUnBlock.setVisibility(View.INVISIBLE);
			if (isBlocked) {
				cbMember.setVisibility(View.INVISIBLE);
				ivUnBlock.setVisibility(View.VISIBLE);
			}

			if (friend.isFacebook())
				ivFacebook.setVisibility(View.VISIBLE);
			else if (friend.isTwitter())
				ivTwitter.setVisibility(View.VISIBLE);
			else if (friend.isGoogle())
				ivGooglePlus.setVisibility(View.VISIBLE);
			else if (friend.isLinkedIn())
				ivLinkedIn.setVisibility(View.VISIBLE);
			else if (friend.isInstagram())
				ivInstagram.setVisibility(View.VISIBLE);
			else if (friend.isYoutube())
				ivYoutube.setVisibility(View.VISIBLE);
			break;
		}

		return convertView;
	}

}
