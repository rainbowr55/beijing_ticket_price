package com.yueyue.ticketprice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yueyue.ticketprice.adapter.StationAdapter;
import com.yueyue.ticketprice.beans.StationData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
/**
 * 选择站点
 * @Description TODO
 * @author xingchao.song
 * @date 2014-11-28 上午10:16:13
 */
public class SelectStationActivity extends Activity {
	private ExpandableListView elv_station_select;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_station);
		initView();
		addListener();
		initData();
	}

	private void initData() {
		String stationList = getFromAssets("xljson");
		Gson gson = new Gson();
		final ArrayList<StationData> lines = gson.fromJson(stationList, new TypeToken<ArrayList<StationData>>() {
		}.getType());
		
		for(StationData station:lines){
		    station.stations =  new ArrayList( Arrays.asList(station.zd.split(",")));
		}
		StationAdapter adapter = new StationAdapter(this,lines);
		elv_station_select.setAdapter(adapter);
		elv_station_select.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String stationName = lines.get(groupPosition).stations.get(childPosition);
				Intent resultIntent = new Intent(SelectStationActivity.this,MainActivity.class);
				resultIntent.putExtra("stationName", stationName);
				SelectStationActivity.this.setResult(200, resultIntent);
				SelectStationActivity.this.finish();
				return true;
			}
		});

	}

	private void addListener() {

	}

	private void initView() {

		elv_station_select = (ExpandableListView) this
				.findViewById(R.id.elv_station_select);

	}

	public String getFromAssets(String fileName) {
		String Result = "";
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result;
	}

}
