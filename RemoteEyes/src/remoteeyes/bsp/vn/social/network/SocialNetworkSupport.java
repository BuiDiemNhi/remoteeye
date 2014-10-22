package remoteeyes.bsp.vn.social.network;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;

import remoteeyes.bsp.vn.type.SocialNetworkType;
import android.content.Context;

public class SocialNetworkSupport {

	SocialAuthAdapter adapter;
	Context context;
	public static int SOCIAL_CONNECT_TYPE = -1;
	public static String SOCIAL_CONNECT_NAME = "";
	public static Runnable logoutRunnable = null;
		
	public SocialNetworkSupport(Context context, SocialAuthAdapter adapter){
		this.context = context;
		this.adapter = adapter;
	}
	
	public void logoutSocialNetwork(int type){
		
		if(type == SocialNetworkType.FACEBOOK){
			adapter.signOut(context, Provider.FACEBOOK.toString());
		}			
		else if(type == SocialNetworkType.TWITTER){
			adapter.signOut(context, Provider.TWITTER.toString());
		}			
		else if(type == SocialNetworkType.GOOGLEPLUS){
			adapter.signOut(context, Provider.GOOGLEPLUS.toString());
		}			
		else if(type == SocialNetworkType.INSTARGRAM){
			adapter.signOut(context, Provider.INSTAGRAM.toString());
		}			
		else if(type == SocialNetworkType.LINKEDIN){
			adapter.signOut(context, Provider.LINKEDIN.toString());
		}			
		else if(type == SocialNetworkType.YOUTUBE){
			adapter.signOut(context, Provider.GOOGLE.toString());
		}
		SOCIAL_CONNECT_TYPE = -1;
		SOCIAL_CONNECT_NAME = "";
	}
	
	public void setLogoutRunnable(Runnable runnable){
		logoutRunnable = runnable;
	}
	
	public static void runLogoutRunnable(){
		if(logoutRunnable != null){
			logoutRunnable.run();
			logoutRunnable = null;
		}			
	}
	
	public void loginSocialNetwork(int type){
		
		if(type == SocialNetworkType.FACEBOOK){
			adapter.authorize(context, Provider.FACEBOOK);
		}			
		else if(type == SocialNetworkType.TWITTER){
			adapter.authorize(context, Provider.TWITTER);
		}			
		else if(type == SocialNetworkType.GOOGLEPLUS){
			adapter.authorize(context, Provider.GOOGLEPLUS);
		}			
		else if(type == SocialNetworkType.INSTARGRAM){
			adapter.authorize(context, Provider.INSTAGRAM);
		}			
		else if(type == SocialNetworkType.LINKEDIN){
			adapter.authorize(context, Provider.LINKEDIN);
		}			
		else if(type == SocialNetworkType.YOUTUBE){
			adapter.addCallBack(Provider.GOOGLE, "http://socialauth.in/socialauthdemo");
			adapter.authorize(context, Provider.GOOGLE);
		}
	}
	
	public void getFriends(){
		adapter.getContactListAsync(new ContactDataListener(context));
	}
}
