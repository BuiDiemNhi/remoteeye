package remoteeyes.bsp.vn.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DayArrayAdapter extends AbstractWheelTextAdapter {

	private final int daysCount = 50;
	Calendar calendar;
	public static ArrayList<String> dayList;
	
	public DayArrayAdapter(Context context, Calendar calendar) {
		super(context, R.layout.wheel_time_day, NO_RESOURCE);
		this.calendar = calendar;
		setItemTextResource(R.id.time_monthday);
		dayList = new ArrayList<String>();
		Calendar newCalendar = (Calendar)calendar.clone();
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
		dayList.add(formatDay.format(newCalendar.getTime()));
		newCalendar.roll(Calendar.DAY_OF_YEAR, 1);
		dayList.add(formatDay.format(newCalendar.getTime()));
		for(int i = 2; i < daysCount; i++){
			newCalendar.roll(Calendar.DAY_OF_YEAR, 1);
			String value = formatDay.format(newCalendar.getTime());
			dayList.add(value);
		}
	}
	
	@Override
	public View getItem(int index, View cachedView, ViewGroup parent){
		
		Calendar newCalendar = (Calendar)calendar.clone();
		newCalendar.roll(Calendar.DAY_OF_YEAR, index);
		
		View view = super.getItem(index, cachedView, parent);
		TextView weekday = (TextView)view.findViewById(R.id.time_weekday);
		if(index == 0 || index == 1){
			weekday.setText("");
		} else {
			DateFormat format = new SimpleDateFormat("EEE");
			weekday.setText(format.format(newCalendar.getTime()));
		}
		
		TextView monthday = (TextView)view.findViewById(R.id.time_monthday);
		if(index == 0){
			monthday.setText("Today");
			monthday.setTextColor(0xFF0000F0);			
		} else if(index == 1){
			monthday.setText("Tomorrow");
			monthday.setTextColor(0xFF0000F0);	
		} else {
			DateFormat format = new SimpleDateFormat("MMM d");
			monthday.setText(format.format(newCalendar.getTime()));
			monthday.setTextColor(0xFF111111);
		}
		return view;
	}

	@Override
	public int getItemsCount() {
		return daysCount;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return dayList.get(index);
	}

	public String getText(int index){
		return getItemText(index).toString();
	}
}
