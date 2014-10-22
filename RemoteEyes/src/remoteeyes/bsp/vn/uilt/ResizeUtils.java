package remoteeyes.bsp.vn.uilt;

import android.app.Activity;
import android.graphics.Point;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResizeUtils {

	public static void resizeButton(Button button, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		button.setLayoutParams(params);
	}

	public static void resizeButton(Button button, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom,
			int alignParent) {

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		params.addRule(alignParent);
		button.setLayoutParams(params);
	}

	public static void resizeCheckbox(CheckBox checkbox, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		checkbox.setLayoutParams(params);
	}
	
	public static void resizeImageButton(ImageButton button, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom, int alignParent) {

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		params.addRule(alignParent);
		button.setLayoutParams(params);
	}

	public static void resizeImageButton(ImageButton button, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom, int alignParent1, int alignParent2) {

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		params.addRule(alignParent1);
		params.addRule(alignParent2);
		button.setLayoutParams(params);
	}

	public static void resizeImageButton(ImageButton button, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		button.setLayoutParams(params);
	}

	public static void resizeTextView(TextView textview, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		textview.setLayoutParams(params);
	}

	public static void resizeTextView(TextView textview, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom,
			int alignParent) {

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		params.addRule(alignParent);
		textview.setLayoutParams(params);
	}

	public static void resizeTextView(TextView textview, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom,
			int besideOf, int viewId) {

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		params.addRule(besideOf, viewId);
		textview.setLayoutParams(params);
	}

	public static void resizeImageView(ImageView imageView, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		imageView.setLayoutParams(params);
	}
	
	public static void resizeImageView(ImageView imageView, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom,float weight,int gravity) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.weight = weight;
		params.gravity = gravity;
		imageView.setLayoutParams(params);
	}

	public static void resizeImageView(ImageView imageView, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom,int alignParent) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.addRule(alignParent);
		imageView.setLayoutParams(params);
	}

	public static void resizeImageView2(ImageView imageView, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom, int alignParent, int alignParent2) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.addRule(alignParent);
		params.addRule(alignParent2);
		imageView.setLayoutParams(params);
	}

	public static void resizeImageView(ImageView imageView, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom, int alignParent, int secondAlignParent) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.addRule(alignParent);
		params.addRule(secondAlignParent);
		imageView.setLayoutParams(params);
	}

	public static void resizeEditText(EditText editText, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		editText.setLayoutParams(params);
	}

	public static void resizeEditText(EditText editText, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom,
			int alignParent) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.addRule(alignParent);
		editText.setLayoutParams(params);
	}

	public static void resizeHorizontalScrollView(
			HorizontalScrollView horizontalScrollView, int width, int height,
			int marginLeft, int marginRight, int marginTop, int marginBottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		horizontalScrollView.setLayoutParams(params);
	}

	public static void resizeLinearLayout(LinearLayout linearLayout, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		linearLayout.setLayoutParams(params);
	}
	
	public static void resizeLinearLayout(LinearLayout linearLayout, int marginLeft, int marginRight, int marginTop,
			int marginBottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		linearLayout.setLayoutParams(params);
	}

	public static void resizeRelativeLayout(RelativeLayout relativeLayout,
			int width, int height, int marginLeft, int marginRight,
			int marginTop, int marginBottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		relativeLayout.setLayoutParams(params);
	}

	public static void resizeRelativeLayout(RelativeLayout relativeLayout,
			int width, int height, int marginLeft, int marginRight,
			int marginTop, int marginBottom, int alignParent) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		params.addRule(alignParent);
		relativeLayout.setLayoutParams(params);
	}
	
	public static void resizeRadioButton(RadioButton radio, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom,float weight,int gravity) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.weight = weight;
		params.gravity = gravity;
		radio.setLayoutParams(params);
	}
	
	public static void resizeCheckBox(CheckBox checkbox, int width,
			int height, int marginLeft, int marginRight, int marginTop,
			int marginBottom,float weight,int gravity) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (width != -1)
			params.width = width;
		if (height != -1)
			params.height = height;
		params.setMargins(marginLeft, marginTop, marginTop, marginBottom);
		params.weight = weight;
		params.gravity = gravity;
		checkbox.setLayoutParams(params);
	}
	
	public static Point getSizeDevice(Activity activity) {
		Point size = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(size);
		return size;
	}

}
