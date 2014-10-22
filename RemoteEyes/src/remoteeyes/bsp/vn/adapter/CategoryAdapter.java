package remoteeyes.bsp.vn.adapter;

import java.util.ArrayList;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.model.CategoryObject;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<String> {

	private Activity activity;
	ArrayList<CategoryObject> categoryList;

	public CategoryAdapter(Activity activity, int resource,
			ArrayList<CategoryObject> categoryList) {
		super(activity, resource);
		this.activity = activity;
		this.categoryList = categoryList;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.item_category, parent, false);
		}
		// put the data in it
		String item = categoryList.get(position).getName();
		if (item != null) {
			TextView text1 = (TextView) row.findViewById(R.id.tv_category);
			text1.setText(item);
		}

		return row;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.item_category, parent, false);
		}
		// put the data in it
		String item = categoryList.get(position).getName();
		if (item != null) {
			TextView text1 = (TextView) row.findViewById(R.id.tv_category);
			text1.setText(item);
		}

		return row;
	}
}
