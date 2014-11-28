package com.yueyue.ticketprice.adapter;

import java.util.ArrayList;

import com.yueyue.ticketprice.SelectStationActivity;
import com.yueyue.ticketprice.beans.StationData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class StationAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private ArrayList<StationData> mLines;

	public StationAdapter(Context context, ArrayList<StationData> lines) {
		mContext = context;
		mLines = lines;
	}

	@Override
	public int getGroupCount() {

		return mLines.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return mLines.get(groupPosition).stations.size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return mLines.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mLines.get(groupPosition).stations.get(childPosition);

	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView lineName = new TextView(mContext);
		lineName.setText(mLines.get(groupPosition).xl);
		lineName.setTextSize(30);
		lineName.setTextColor(Color.GREEN);
		return lineName;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		TextView stationName = new TextView(mContext);
		stationName.setText(mLines.get(groupPosition).stations.get(childPosition));
		stationName.setClickable(false);
		stationName.setFocusable(false);
		stationName.setTextSize(25);
		return stationName;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
