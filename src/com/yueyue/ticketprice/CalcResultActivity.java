package com.yueyue.ticketprice;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yueyue.ticketprice.beans.NetResponse;
import com.yueyue.ticketprice.utils.NetUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 计算和显示结果页面
 * @Description TODO
 * @author xingchao.song
 * @date 2014-11-28 上午10:16:27
 */
public class CalcResultActivity extends Activity {
    
    private TextView tv_distance;
    private String startStation = "";
    private String endStation = "";
   public static final String STARTFLAG = "start";
   public static final String ENDFLAG = "end";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_result);
        initParameters();
        initView();
        addListener();
        getData();
    }
    private void addListener() {
        

    }

    private void initView() {
        tv_distance = (TextView) this.findViewById(R.id.tv_distance);
    }

    private void initParameters() {
        startStation = getIntent().getStringExtra(STARTFLAG);
        endStation = getIntent().getStringExtra(ENDFLAG);
    }
    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();

                client.get("http://socketapi.duapp.com/distance/"+startStation+"/"+endStation+"/none?callback=jsonp2", new TextHttpResponseHandler() {
                    
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, String arg2) {
                        show(arg2);
                    }
                    
                    @Override
                    public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                        
                    }
                });


    }

    protected void show(String arg2) {
        String[] results =   arg2.split("jsonp2");
        if(results.length>2){
          String distance =   results[2];   //7.8
          distance =  distance.replace("(", "");
          distance =  distance.replace(");", "");
         
          Gson gson = new Gson();
          NetResponse bean = gson.fromJson(distance, NetResponse.class);
          tv_distance.setText("车费："+distanceToMoney(bean.data.distance));
        }
       
    }


    
    public String distanceToMoney(String distance){
    	float fDistance = Float.parseFloat(distance);
    	
    	if(fDistance>0&&fDistance<=6.0){
    		return "3元";
    	}
    	if(fDistance>6.0&&fDistance<=12.0){
    		return "4元";
    	}
    	if(fDistance>12.0&&fDistance<=22.0){
    		return "5元";
    	}
    	if(fDistance>22.0&&fDistance<=32.0){
    		return "6元";
    	}
    	if(fDistance>32.0&&fDistance<=52.0){
    		return "7元";
    	}
    	if(fDistance>52.0&&fDistance<=72.0){
    		return "8元";
    	}
    	if(fDistance>72.0&&fDistance<=92.0){
    		return "9元";
    	}
    	
    	return "计算错误";
    }

}
