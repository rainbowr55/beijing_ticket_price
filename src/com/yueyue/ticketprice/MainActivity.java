package com.yueyue.ticketprice;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yueyue.ticketprice.beans.NetResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面
 * @Description TODO
 * @author xingchao.song
 * @date 2014-11-28 上午10:16:50
 */
public class MainActivity extends Activity implements OnClickListener {

    private Button btn_start_station;
    private Button btn_end_station;
    private Button btn_calc;
    private TextView tv_price;
    private static final int SLECTSTARTSTATION = 1000;
    private static final int SLECTENDSTATION = 1001;
    private  String startStationName;
    private  String endStationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addListener();
    }

    private void addListener() {
        btn_start_station.setOnClickListener(this);
        btn_end_station.setOnClickListener(this);
        btn_calc.setOnClickListener(this);
    }

    private void initView() {
        btn_start_station = (Button) this.findViewById(R.id.btn_start_station);
        btn_end_station = (Button) this.findViewById(R.id.btn_end_station);
        btn_calc = (Button) this.findViewById(R.id.btn_calc);
        tv_price = (TextView) this.findViewById(R.id.tv_price);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_start_station:
            Intent startSation = new Intent(this, SelectStationActivity.class);
            startActivityForResult(startSation, SLECTSTARTSTATION);
            break;
        case R.id.btn_end_station:
            Intent endSation = new Intent(this, SelectStationActivity.class);
            startActivityForResult(endSation, SLECTENDSTATION);
            break;
        case R.id.btn_calc:
/*            Intent calcResult = new Intent(this, CalcResultActivity.class);
            calcResult.putExtra(CalcResultActivity.STARTFLAG, btn_start_station.getText());
            calcResult.putExtra(CalcResultActivity.ENDFLAG, btn_end_station.getText());
            startActivity(calcResult);*/
            getData();
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=200) return;
        if (requestCode == SLECTSTARTSTATION) {
            startStationName = data.getStringExtra("stationName");
            btn_start_station.setText(startStationName);
        } else if (requestCode == SLECTENDSTATION) {
            endStationName = data.getStringExtra("stationName");
            btn_end_station.setText(endStationName);
        }
    }
    
    
    
    private void getData() {
        if(startStationName==null||"".equals( startStationName)){
            Toast.makeText(this, "起始站不能为空", 0).show();
            return;
        }
        if(endStationName==null||"".equals( startStationName)){
            Toast.makeText(this, "终点站不能为空", 0).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();

                client.get("http://socketapi.duapp.com/distance/"+startStationName+"/"+endStationName+"/none?callback=jsonp2", new TextHttpResponseHandler() {
                    
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
          tv_price.setText("车费："+distanceToMoney(bean.data.distance));
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
