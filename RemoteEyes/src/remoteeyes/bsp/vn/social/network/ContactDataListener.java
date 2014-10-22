package remoteeyes.bsp.vn.social.network;

import java.util.ArrayList;
import java.util.List;

import org.brickred.socialauth.Contact;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Context;
import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.TellAFriendActivity;
import remoteeyes.bsp.vn.asynctask.LoadMemberAsynctask;
import remoteeyes.bsp.vn.model.Friend;

public class ContactDataListener implements SocialAuthListener<List<Contact>> {

	Context context;
	ArrayList<Friend> phoneFriendList = new ArrayList<Friend>();
	ArrayList<Friend> facebookFriendList = new ArrayList<Friend>();
	ArrayList<Friend> twitterFriendList = new ArrayList<Friend>();
	ArrayList<Friend> googlePlusFriendList = new ArrayList<Friend>();
	ArrayList<Friend> linkedInFriendList = new ArrayList<Friend>();
	ArrayList<Friend> instagramFriendList = new ArrayList<Friend>();
	ArrayList<Friend> youtubeFriendList = new ArrayList<Friend>();

	public ContactDataListener(Context context) {
		this.context = context;
	}

	@Override
	public void onExecute(String provider, List<Contact> t) {

		List<Contact> contactsList = t;
		ArrayList<Friend> friendList = new ArrayList<Friend>();
		String param = "";

		if (contactsList != null && contactsList.size() > 0) {
			for (int i = 0; i < contactsList.size(); i++) {
				Contact contact = contactsList.get(i);
				Friend friend = new Friend();
				friend.setName(contact.getDisplayName());
				friend.setAvatarUrl(contact.getProfileImageURL());
				friend.setIdSocial(contact.getId());
				if (provider.equals(Provider.FACEBOOK.toString())) {
					friend.setFacebook(true);
					facebookFriendList.add(friend);
				} else if (provider.equals(Provider.TWITTER.toString())) {
					friend.setTwitter(true);
					twitterFriendList.add(friend);
				} else if (provider.equals(Provider.GOOGLEPLUS.toString())) {
					friend.setGoogle(true);
					googlePlusFriendList.add(friend);
				} else if (provider.equals(Provider.LINKEDIN.toString())) {
					friend.setLinkedIn(true);
					linkedInFriendList.add(friend);
				} else if (provider.equals(Provider.INSTAGRAM.toString())) {
					friend.setInstagram(true);
					instagramFriendList.add(friend);
				} else if (provider.equals(Provider.GOOGLE.toString())) {
					friend.setYoutube(true);
					youtubeFriendList.add(friend);
				}
				friendList.add(friend);// list friend
				param += contact.getId();
				if (i < contactsList.size() - 1) {
					param += ",";
				}
			}
			try {
				if ((MyGroupActivity) context != null) {
					if (provider.equals(Provider.FACEBOOK.toString())) {
						((MyGroupActivity) context)
								.setFacebookFriendsList(friendList);
						new LoadMemberAsynctask(context).execute(param, "0",
								"0", "0", "0", "0");
					} else if (provider.equals(Provider.TWITTER.toString())) {
						((MyGroupActivity) context)
								.setTwitterFriendsList(friendList);
						new LoadMemberAsynctask(context).execute("0", param,
								"0", "0", "0", "0");
					} else if (provider.equals(Provider.GOOGLEPLUS.toString())) {
						((MyGroupActivity) context)
								.setGooglePlusFriendsList(friendList);
						new LoadMemberAsynctask(context).execute("0", "0",
								param, "0", "0", "0");
					} else if (provider.equals(Provider.LINKEDIN.toString())) {
						((MyGroupActivity) context)
								.setLinkedInFriendsList(friendList);
						new LoadMemberAsynctask(context).execute("0", "0", "0",
								param, "0", "0");
					} else if (provider.equals(Provider.INSTAGRAM.toString())) {
						((MyGroupActivity) context)
								.setInstagramFriendsList(friendList);
						new LoadMemberAsynctask(context).execute("0", "0", "0",
								"0", param, "0");
					} else if (provider.equals(Provider.GOOGLE.toString())) {
						((MyGroupActivity) context)
								.setYoutubeFriendsList(friendList);
						new LoadMemberAsynctask(context).execute("0", "0", "0",
								"0", "0", param);
					}
				}
			} catch (Exception ex) {
			}
			try {
				if ((TellAFriendActivity) context != null) {
					((TellAFriendActivity) context)
							.setFacebookFriendsList(facebookFriendList);
					new LoadMemberAsynctask(context).execute(param, "0", "0", "0",
							"0", "0");
					((TellAFriendActivity) context).param = param;
					((TellAFriendActivity) context)
							.setTwitterFriendsList(twitterFriendList);
					((TellAFriendActivity) context)
							.setInstagramFriendsList(instagramFriendList);
					((TellAFriendActivity) context)
							.setLinkedInFriendsList(linkedInFriendList);
					((TellAFriendActivity) context)
							.setGooglePlusFriendsList(googlePlusFriendList);
					((TellAFriendActivity) context)
							.setYoutubeFriendsList(youtubeFriendList);
					((TellAFriendActivity) context).showAllFriendList();
					
				}
			} catch (Exception ex) {
			}

		} else {
			// Contact List Empty
		}

	}

	@Override
	public void onError(SocialAuthError e) {

	}
}