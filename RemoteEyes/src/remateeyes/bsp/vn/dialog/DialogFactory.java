package remateeyes.bsp.vn.dialog;

import remoteeyes.bsp.vn.type.DialogType;
import android.app.Dialog;
import android.content.Context;

public class DialogFactory {

	public static Dialog getDialog(Context context, int type) {
		switch (type) {
		case DialogType.DIALOG_MESSAGE:
			return new DialogMessage(context);
		case DialogType.DIALOG_WHEEL_DAY:
			return new WheelDayDialog(context);
		case DialogType.DIALOG_REPORT:
			return new DialogReportChallenge(context);
		case DialogType.DIALOG_NEW_REPORT:
			return new DialogNewReportChallenge(context);
		case DialogType.DIALOG_WHEEL_BIRTHDAY:
			return new WheelBirthdayDialog(context);
		case DialogType.DIALOG_CHANGE_EMAIL:
			return new DialogChangeEmail(context);
		case DialogType.DIALOG_CONFIRM_ACCEPT:
			return new DialogConfirmAccept(context);
		case DialogType.DIALOG_CONFIRM_IGNORE:
			return new DialogConfirmIgnore(context);
		case DialogType.DIALOG_EXPLANATORY_PHOTO:
			return new DialogExplanatory(context);
		case DialogType.DIALOG_RESPONSE_PHOTO:
			return new DialogResponsePhoto(context);
		case DialogType.DIALOG_ADD_MEMBER_GROUP:
			return new DialogAddMember(context);
		case DialogType.DIALOG_SEND_INVITE:
			return new DialogSendInvite(context);
		case DialogType.DIALOG_FRIEND_ALREADY:
			return new DialogFriendAlready(context);
		case DialogType.DIALOG_BLOCKED_USER:
			return new DialogBlockedUser(context);
		case DialogType.DIALOG_CHALLENGE_PRIVACY:
			return new DialogChallengePrivacy(context);
		case DialogType.DIALOG_EDIT_DESCRIPTION:
			return new DialogEditDescription(context);
		case DialogType.DIALOG_EDIT_REWARD:
			return new DialogEditReward(context);
		case DialogType.DIALOG_RESPONSE_VIDEO:
			return new DialogResponseVideo(context);
		case DialogType.DIALOG_FILTER:
			return new DialogFilter(context);
		case DialogType.DIALOG_REMOTE_EMAIL:
			return new DialogRemoteEmail(context);
		case DialogType.DIALOG_PAYMENT_CHALLENGE:
			return new DialogPaymentChallenge(context);
		case DialogType.DIALOG_HISTORY_PAYPAL:
			return new DialogHistoryPaypal(context);
		case DialogType.DIALOG_DELAY_PAYMENT:
			return new DelayPayment(context);

		}
		return null;
	}
}
