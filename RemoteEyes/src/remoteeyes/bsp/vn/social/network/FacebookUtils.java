package remoteeyes.bsp.vn.social.network;

import java.util.ArrayList;
import java.util.List;

import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.asynctask.ShareChallengeAsynctask;
import remoteeyes.bsp.vn.asynctask.UploadPhotoAsynctask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.fragments.DetailChallengeFragment;
import remoteeyes.bsp.vn.model.ChallengeObject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class FacebookUtils {

	public static Session currentSession;
	public static Runnable shareLinkRunnable;

	public static void publishFeedDialog(final Activity activity,
			final ChallengeObject challenge) {
		Bundle params = new Bundle();
		params.putString("name", challenge.getTitle());

		params.putString("description", challenge.getDescription());
		params.putString("link", "mapsnap.mobi");
		params.putString("picture", "");
		if (challenge.imagesList != null) {
			if (challenge.imagesList.size() > 0) {
				params.putString("picture", challenge.imagesList.get(0));
			}
		}

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(activity, "Posted successfull ",
										Toast.LENGTH_SHORT).show();
								new ShareChallengeAsynctask(activity).execute(Config.IdUser,challenge.getId());
							} else {
								// User clicked the Cancel button
								
								Toast.makeText(
										activity.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							
							Toast.makeText(activity.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							
							Toast.makeText(activity.getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

	public static void publishImageDialog(final Activity activity,
			final String path , final String title) {
		Bundle params = new Bundle();
		params.putString("name", title);
		/*
		 * params.putString("caption",
		 * "Build great social apps and get more installs.");
		 */
		params.putString("link", "mapsnap.mobi");
		params.putString("picture", path);
		// params.putString("picture",
		// "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(activity, "Posted successfull ",
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								
								Toast.makeText(
										activity.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							
							Toast.makeText(activity.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							
							Toast.makeText(activity.getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}
	
	public static void connectToFB(Activity activity) {
		List<String> permissions = new ArrayList<String>();
		permissions.add("public_profile");
		currentSession = new Session.Builder(activity).build();
		currentSession.addCallback(sessionStatusCallback);
		Session.OpenRequest openRequest = new Session.OpenRequest(activity);
		openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		openRequest.setRequestCode(Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
		openRequest.setPermissions(permissions);
		currentSession.openForRead(openRequest);
	}

	public static Session.StatusCallback sessionStatusCallback = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	public static void onSessionStateChange(Session session,
			SessionState state, Exception exception) {
		if (state.isOpened()) {
			Session.setActiveSession(currentSession);
			if (shareLinkRunnable != null) {
				shareLinkRunnable.run();
				shareLinkRunnable = null;
			}
		}
	}
}
