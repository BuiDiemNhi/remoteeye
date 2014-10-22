package remoteeyes.bsp.vn.social.network;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Context;

public class RegisterProfileListener implements SocialAuthListener<Profile>{

	Context context;
	
	public RegisterProfileListener(Context context){
		this.context = context;
	}
	
	@Override
	public void onError(SocialAuthError arg0) {
	}

	@Override
	public void onExecute(String arg0, Profile profile) {		
		
	}

}
