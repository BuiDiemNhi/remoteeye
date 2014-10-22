package remoteeyes.bsp.vn.common;

import java.net.URI;
import java.util.ArrayList;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PaymentActivity;

import remoteeyes.bsp.vn.model.ChallengeObject;
import android.net.Uri;

public class Config {

	public static ArrayList<ChallengeObject> data = new ArrayList<ChallengeObject>();
	public static int total = 0;
	public static int screenHeight = 0;
	public static int screenWidth = 0;
	public static boolean fitnews = false;

	public static int TIME_OUT_GET_DATA = 25000;
	public static int TIME_OUT_CONNECTION = 20000;

	public static final int REQUEST_SAVE = 0;
	public static final int UPLOAD_CHALLENGE = 1;
	public static final int REQUEST_SAVE_2 = 2;
	

	public static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
	public static Uri fileUri = null;

	public static String IdUser = "";
	public static String EMAILUSER = "";
	// public static String AVATAR_URL ="";t
	public static int SOCIAL_LOGIN_TYPE = -1;
	public static double lat = 0;
	public static double lng = 0;
	public static String Pending="";
	public static String earn="";
	public static String paid="";
	public static String money="";
	public static final String DATA_FIFE = "data_file";
	public static final String USERID_KEY = "remoteeyes.bsp.vn";
	public static final String EMAIL_KEY = "Email.bsp.vn";
	public static final String AVATAR_KEY = "avatar.bsp.vn";
	public static final String IS_SOCIAL_KEY = "is_social";

	public static URI UPDATE_LOCALTION;
	public static String INTERNET_DETECT = "internet_detect";
	public static String EXTRA_ISCONNECTED = "isConnectedInternet";
	public static Boolean isConnectedInternet = true;

	// thu
	public static boolean flag_change_avatar = false;
	public static boolean flag_change_email = false;
	public static boolean flag_block_user = false;
	public static boolean flag_unblock_user = false;
	public static String id_email_other = "-1";

	// public static String SERVER="http://icu.vn/remote/api/";t->
	// public static String SERVER="http://192.168.1.106/remote_eyes/api/";
	// public static String SERVER="http://bsptest.byethost7.com/api/";
	// public static String SERVER="http://vieclamtaytrai.vn/remote/api/";
	// public static String SERVER_IMAGE = "http://vieclamtaytrai.vn/remote/";t
	//
	// public static String SERVER = "http://192.168.1.113/RemoteEyes/api/";
	// public static String SERVER_IMAGE = "http://192.168.1.113/RemoteEyes/";
	// public static String SERVER_UPLOAD_IMAGE =
	// "http://192.168.1.113/RemoteEyes";

	public static String SERVER = "http://icsmartphone.com/remote/api/";
	public static String SERVER_IMAGE = "http://icsmartphone.com/remote/";
	public static String SERVER_UPLOAD_IMAGE = "http://icsmartphone.com/remote";

	/**
	 * email=usern@bsp.vn&pass=work4smart with n = 1,2,3,4..
	 */
	public static String API_LOGIN = SERVER + "login?email=%s&pass=%s";
	public static String API_LOGIN_SOCIAL_NETWORK = SERVER
			+ "checkUser?email=%s&birthday=%s&gender=%s&avatar=%s&name=%s&type=%s&networkID=%s";
	public static String API_FORGOT_PASSWORD = SERVER
			+ "forgotpassword?email=%s";
	// ?email=%s&pass=%s&birthday=%s&gender=%s&avatar=%s&name=%s";t
	public static String API_SIGN_UP = SERVER + "register?json=";

	public static String UPLOAD_FILE = SERVER + "uploadAvatar";
	public static String API_UPLOAD_IMAGE_CHALLENG = SERVER
			+ "uploadImageChallenge";
	public static String API_UPLOAD_CHALLENG = SERVER
			+ "uploadFile?fileUpload=%s&userID=%s&challengeID=%s&typeUpload=%s&thumb=%s&duration=%s";
	public static String API_UPLOAD_VIDEO_CHALLENGE = SERVER + "uploadVideo";

	// ?lat=123&lng=23&userID=1;t
	public static String GET_MENU = SERVER + "challenge/";
	public static String API_GET_CATEGORY = SERVER + "getCategory";

	// public static String API_CREATE_CHALLENG = SERVER +
	// "createChallenge?json=";
	public static String API_CREATE_CHALLENG = SERVER + "createChallenge";
	// ?lat=10.833137336282476&lng=106.63435757252205;t
	public static String GET_CURC = SERVER + "currentChallenge/";
	public static String GET_FILTER_CHALLENGE = SERVER + "challenge/list";
	public static String GET_FILTER_GLOBAL_CHALLENGE = SERVER
			+ "global/challenge/filter";
	public static String GET_FILTER_ACCEPTED_CHALLENGE = SERVER
			+ "my-accept/challenge/filter";
	public static String GET_FILTER_POSTED_CHALLENGE = SERVER
			+ "my-post/challenge/filter";
	// public static String GET_CURCTD = SERVER + "currentChallengeToday";
	public static String API_CREATED_CHALLENG = SERVER
			+ "createdChallenge?userID=%s";
	public static String API_MY_AREA_HISTORY = SERVER + "historychallenge";
	// public static String API_MY_ACCEPT_CHALLENG = SERVER
	// +"myAcceptedChallenge?userID=%s";
	public static String API_MY_ACCEPT_CHALLENG_TODAY = SERVER
			+ "myAcceptedChallengeToday?userID=%s";
	public static String API_GET_POSTED_CHALLENGE = SERVER
			+ "mypostchallenge?userID=%s&page=%s";
	public static String API_GET_ACCEPTED_CHALLENGE = SERVER
			+ "myacceptchallenge?userID=%s";
	public static String API_GLOBAL_CHALLENGE = SERVER
			+ "globalchallenge?userID=%s";

	// myAcceptedChallengeToday?userID=1;t //public static String
	// API_ACCEPT_CHALLENGE = SERVER +
	// "saveacceptchallenge?userID=%s&challengeID=%s&status=1";
	public static String API_ACCEPT_CHALLENG = SERVER
			+ "acceptChallenge?userID=%s&challengeID=%s";
	public static String API_IGNORE_CHALLENG = SERVER
			+ "ignoreChallenge?userID=%s&challengeID=%s";
	public static String GET_DETAIL_CHALLENGE = SERVER
			+ "getChallenge?challengeID=%s&userID=%s";
	public static String API_IGNORE_CHALLENGE = SERVER
			+ "ignoreChallenge?userID=%s&challengeID=%s";
	public static String API_IGNORE_GLOBAL_CHALLENGE = SERVER
			+ "ignoreGlobalChallenge?userID=%s&challengeID=%s";
	public static String API_UNDO_GLOBAL_CHALLENGE = SERVER
			+ "challenge/switch/display";

	public static String API_GET_PROFILE = SERVER + "profile?id=%s";
	public static String API_EDIT_AVATAR = SERVER + "user/avatar/update";
	public static String API_CHANGE_NAME = SERVER
			+ "updatenameprofile?id=%s&name=%s";
	public static String API_CHANGE_PASSWORD = SERVER
			+ "updatepassprofile?id=%s&pass=%s";
	public static String API_ADD_OTHER_EMAIL = SERVER
			+ "addemailorther?email=%s&userID=%s";
	public static String API_SEND_ACTIVE_EMAIL = SERVER + "activeemail?id=%s";
	public static String API_REMOVE_OTHERS_EMAIL = SERVER
			+ "deleteemailorther?id=%s";
	public static String API_ADD_LINKED_PROFILE = SERVER
			+ "addlinkedprofile?name=%s&userID=%s&networkID=%s&status=%s";
	public static String API_UPDATE_LINKED_PROFILE = SERVER
			+ "updatelinkedprofile?id=%s&status=%s";
	public static String API_REMOVE_LINKED_PROFILE = SERVER
			+ "deletelinkedprofile?id=%s";
	public static String API_CHANGE_MAIN_EMAIL = SERVER
			+ "updateemailprofile?id=%s";
	// public static String API_CHANGE_PAYPAL_EMAIL = SERVER
	// + "email-paypal-profile/update?email_id=%s";
	public static String API_CHANGE_PAYPAL_EMAIL = SERVER
			+ "user-emails/update?email_id=%s&type=%s";

	public static String API_CHANGE_PAYPAL_EMAIL2 = SERVER
			+ "main-paypal-email/change/";

	// api/user-emails/update email_id type

	public static String API_BLOCK_USER = SERVER + "users/block"; // user_id = 1
																	// ( thang
																	// block)
																	// ,blocked_user_id
																	// = 3,4,5(
																	// nhung
																	// thang bi
																	// block)
	// http://icsmartphone.com/remote/api/users/unblock user_id ,
	// blocked_user_id
	public static String API_GET_CUR_LOCATION= SERVER+ "location/get-full-address";
	public static String API_UNBLOCK_USER = SERVER + "users/unblock";
	public static String API_GET_BUGET=SERVER+ "money/budget-statistic/%s";

	// http://icsmartphone.com/remote/api/user/by-email?email=user1@bsp.vn
	public static String API_GET_USERINFO_FROM_EMAIL = SERVER
			+ "user/by-email?email=%s";
	// list not payment
	public static String API_GET_LIST_CHALLENGE_NOT_PAYMENT = SERVER
			+ "unpaid-challenge/list/"; // userID

	// Save paypal history
	public static String API_SAVE_PAYPAL_HISTORY = SERVER
			+ "save-paypal-history"; // info , item_list , id_user

	// http://icsmartphone.com/remote/api/paypal-history-by-user/1
	// get Paypal history
	public static String API_GET_PAYPAL_HISTORY = SERVER
			+ "paypal-history-by-user/"; // id_user

	// http://icu.vn/remote/api/getMessage?challengeID=2 ;
	public static String API_SHOW_MESSAGE = SERVER
			+ "getMessage?challengeID=%s";
	public static String API_DELETE_CHALLENGE = SERVER + "delChallenge?id=%s";
	public static String API_SHARE_CHALLENGE = SERVER + "share/challenge";
	public static String API_EDIT_CHALLENGE = SERVER + "challenge/edit";

	public static String API_ADD_COMMENT = SERVER
			+ "comment?userID=%s&objectID=%s&content=%s&type=%s";
	public static String API_RATING = SERVER
			+ "rating?objectID=%s&userID=%s&num=%s&type=%s";

	public static String API_ADD_GROUP = SERVER
			+ "createGroup?userCreateID=%s&name=%s";
	public static String API_LOAD_GROUP_BY_USER = SERVER
			+ "ShowGroupByID?id=%s";
	public static String API_UPDATE_GROUP_BY_USER = SERVER
			+ "updateGroup?id=%s&name=%s";
	public static String API_DELETE_GROUP_BY_USER = SERVER + "deleteGroup";
	public static String API_LOAD_MEMBER = SERVER + "users/detail";
	public static String API_ADD_MEMBER = SERVER + "group/members/add";
	public static String API_DELETE_MEMBER = SERVER + "deleteMemberGroup";
	public static String API_CHALLENGE_WINNER = SERVER + "set-challenge-winner";
	// ui_challenge_id=14&ui_user_id=1&ui_media_id=1&ui_type_win=0

	public static final String TAG = "paymentExample";
	public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	// public static final String CONFIG_CLIENT_ID =
	public static final String CONFIG_CLIENT_ID = "AS36jBA2CrVf_fn1f4rWlP8DvyDJEBBCUPNZDZPf-E50xXp3RlQvTgOzOHPU";
	// "Aetb6RA5SgTGxlde_nMdCbdcYpexmkiXc7BnuwFUGqal7DlSf5G3uv9hcwgK";
	// public static final String CONFIG_CLIENT_ID =
	// "AV0sMRAWDndyyKuHvzcbBkSvfJe74U_d9SJWys7Xyfq25emqiKKbn4QKIwUY";

	public static final int REQUEST_CODE_PAYMENT = 1;
	public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	// public static final String CONFIG_RECEIVER_EMAIL =
	public static final int REQUEST_CODE_PAYMENT_area = 3;
	public static final int REQUEST_CODE_FUTURE_PAYMENT_area = 4;
	public static final String CONFIG_RECEIVER_EMAIL = "buidiemnhi@gmail.com";
	// "buidiemnhi-facilitator@gmail.com";
	// public static final String CONFIG_RECEIVER_EMAIL =
	// "letter.remoteeye-facilitator@gmail.com";

}
