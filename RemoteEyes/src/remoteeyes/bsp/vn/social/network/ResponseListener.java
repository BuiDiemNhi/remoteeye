package remoteeyes.bsp.vn.social.network;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.MyGroupActivity;
import remoteeyes.bsp.vn.NewAccountActivity;
import remoteeyes.bsp.vn.SignInActivity;
import remoteeyes.bsp.vn.TellAFriendActivity;
import remoteeyes.bsp.vn.asynctask.AddLinkedProfileAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.ISConnectInternet;
import remoteeyes.bsp.vn.type.SocialNetworkType;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class ResponseListener implements DialogListener {

	Context context;
	Activity activity;

	public ResponseListener(Context context, Activity activty) {
		this.context = context;
		this.activity = activty;
	}

	@Override
	public void onBack() {
		SignInActivity.isLoginingSocial = false;
	}

	@Override
	public void onCancel() {
		SignInActivity.isLoginingSocial = false;
	}

	@Override
	public void onComplete(Bundle arg0) {
		// TODO Auto-generated method stub
		try{
			((SignInActivity) context).getSocialProfile();
			SignInActivity.isLoginingSocial = false;
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		try{
			((MyGroupActivity) context).getFriendsList();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		try{
			((TellAFriendActivity) context).getContact();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		try{
			((NewAccountActivity) context).getSocialProfile();
			
			if(SocialNetworkSupport.SOCIAL_CONNECT_TYPE == SocialNetworkType.FACEBOOK)
				NewAccountActivity.info.setFacebookId((((NewAccountActivity) context).getProfile().getValidatedId()));
			else if(SocialNetworkSupport.SOCIAL_CONNECT_TYPE == SocialNetworkType.TWITTER)
				NewAccountActivity.info.setTwitterId((((NewAccountActivity) context).getProfile().getValidatedId()));
			else if(SocialNetworkSupport.SOCIAL_CONNECT_TYPE == SocialNetworkType.GOOGLEPLUS)
				NewAccountActivity.info.setGoogleplusId((((NewAccountActivity) context).getProfile().getValidatedId()));
			else if(SocialNetworkSupport.SOCIAL_CONNECT_TYPE == SocialNetworkType.INSTARGRAM)
				NewAccountActivity.info.setInstagramId((((NewAccountActivity) context).getProfile().getValidatedId()));
			else if(SocialNetworkSupport.SOCIAL_CONNECT_TYPE == SocialNetworkType.LINKEDIN)
				NewAccountActivity.info.setLinkedInId((((NewAccountActivity) context).getProfile().getValidatedId()));
			else if(SocialNetworkSupport.SOCIAL_CONNECT_TYPE == SocialNetworkType.YOUTUBE)
				NewAccountActivity.info.setYoutubeId((((NewAccountActivity) context).getProfile().getValidatedId()));
			
			((NewAccountActivity) context).addLinkedProfile();
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		try{
			new AddLinkedProfileAsynctask((MyAccountActivity) context).execute(
					SocialNetworkSupport.SOCIAL_CONNECT_NAME, Config.IdUser,
					((MyAccountActivity) context).getProfile().getValidatedId(), "1");
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void onError(SocialAuthError error) {
		error.printStackTrace();
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (!ISConnectInternet.isConnectedInternet(activity)) {
					ISConnectInternet.showAlertInternet(activity);
					SignInActivity.isLoginingSocial = false;
				}
			}
		});
	}
}
