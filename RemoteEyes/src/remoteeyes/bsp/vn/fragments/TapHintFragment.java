package remoteeyes.bsp.vn.fragments;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TapHintFragment extends Fragment {
	TextView taphint;
	Activity activity;
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_hint_tap, container, false);
		taphint=(TextView) view.findViewById(R.id.tv_hint_tap_a_cluodlet);
		if(ShowingChallengeType.STATUS_SHOW_CURRENT==ShowingChallengeType.CHALLENGE_SHOW_GLOBAL)
		{taphint.setText(getActivity().getResources().getString(
				R.string.tap_hint_global));}
		else if(ShowingChallengeType.STATUS_SHOW_CURRENT==ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED)
			
		{taphint.setText(getActivity().getResources().getString(
				R.string.tap_hint_accept));}
else if(ShowingChallengeType.STATUS_SHOW_CURRENT==ShowingChallengeType.CHALLENGE_SHOW_NEARBY)
			
		{taphint.setText(getActivity().getResources().getString(
				R.string.tap_hint_erea));}
else if(ShowingChallengeType.STATUS_SHOW_CURRENT==ShowingChallengeType.CHALLENGE_SHOW_POSTED)
	
{taphint.setText(getActivity().getResources().getString(
		R.string.tap_hint_posted));}
	    return view;
		
	}
}
