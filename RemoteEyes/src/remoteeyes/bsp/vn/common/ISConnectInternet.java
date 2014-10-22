package remoteeyes.bsp.vn.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

public class ISConnectInternet {
	
	public static boolean isConnectedInternet(Activity act) {
		ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {

			return true;
		} else {

			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public static void  showAlertInternet(Activity act) {
		final AlertDialog alertDialog = new AlertDialog.Builder(act).create();

		// Setting Dialog Title
		alertDialog.setTitle("Could not connect to internet");

		// Setting Dialog Message
		alertDialog.setMessage("Please, check your internet connection!");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				// Toast.makeText(getApplicationContext(), "You clicked on OK",
				// Toast.LENGTH_SHORT).show();
				alertDialog.dismiss();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
