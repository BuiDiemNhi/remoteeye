package remoteeyes.bsp.vn.custom.view;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.view.MotionEvent;
import android.view.View;
/** Customizing AutoCompleteTextView to return Place Description
*  corresponding to the selected item
*/
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
	// was the text just cleared?
		boolean justCleared = false;
	 
		// if not set otherwise, the default clear listener clears the text in the
		// text view
		

	 
 
    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
       
        
    }
 @Override
protected void replaceText(CharSequence text) {
	// TODO Auto-generated method stub
	super.replaceText(text);
}
 @Override
public void clearComposingText() {
	// TODO Auto-generated method stub
	super.clearComposingText();
}
    /** Returns the Place Description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
    
}