package remoteeyes.bsp.vn.social.network;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import remoteeyes.bsp.vn.SignInActivity;
import remoteeyes.bsp.vn.asynctask.LoginSocialNetworkAsynctask;
import remoteeyes.bsp.vn.common.Config;
import android.content.Context;

public class ProfileDataListener implements SocialAuthListener<Profile>{

	Context context;
	
	public ProfileDataListener(Context context){
		this.context = context;
	}
	
	@Override
	public void onError(SocialAuthError arg0) {
	}

	@Override
	public void onExecute(String arg0, Profile profile) {		
		((SignInActivity)context).setProfile(profile);
		String networdID = profile.getValidatedId();
		int type = Config.SOCIAL_LOGIN_TYPE;
		String email = profile.getEmail() == null ? "Null" : profile.getEmail();
		String avatar = profile.getProfileImageURL() == null ? "http://www.imageurlhost.com/images/s8x32j8hib8l2zt67y9.png" : profile.getProfileImageURL();		
		String name = profile.getDisplayName() == null ? "Null" : profile.getDisplayName();
		String gender = profile.getGender() == null ? "2" : (profile.getGender().equals("male") ? "0" : "1");
		String birthday = "";
		try{
			birthday = profile.getDob().getYear() + "-" + profile.getDob().getMonth() + "-" + profile.getDob().getDay();
		} catch(Exception ex){
		}
		new LoginSocialNetworkAsynctask(context).execute(email, birthday, gender, avatar, name, type + "", networdID);
	}

}
