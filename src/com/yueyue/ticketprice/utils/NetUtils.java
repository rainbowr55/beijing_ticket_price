package com.yueyue.ticketprice.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.widget.Toast;

public class NetUtils {
    private Activity context;

    public NetUtils(Activity context) {
        this.context = context;
        // temp = checkConnectivity();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    // public static HttpPost createHttpPostAndConfig(String url, String json) {
    // HttpPost httpPost = new HttpPost(url);
    //
    // if (config.getCookieMap() != null && config.getCookieMap().size() > 0) {
    //
    // httpPost.setHeader(HTTP_COOKIE, getCookieInfo(config.getCookieMap()));
    // BDebug.w("HttpUtils", getCookieInfo(config.getCookieMap()) + "***********" + url);
    //
    // }
    //
    // httpPost.setHeader(HTTP_USER_AGENT, HTTP_USER_AGENT_MESSAGE);
    //
    // List<NameValuePair> params = new ArrayList<NameValuePair>();
    // params.add(new BasicNameValuePair(HTTP_POST_BODY, json));
    //
    // try {
    // httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // }
    //
    // return httpPost;
    // }

    public synchronized static String getResponse(Context context, String path, String xml)
            throws SocketTimeoutException, IOException {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

   

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);// 允许输出
        conn.setDoInput(true);
        conn.setUseCaches(false);// 不使用缓存
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        conn.setRequestProperty("X-ClientType", "2");// 发送自定义的头信息

        if (conn.getResponseCode() != 200)
            throw new RuntimeException("请求url失败");

        InputStream is = conn.getInputStream();// 获取返回数据

        /*
         * // 使用输出流来输出字符(可选) ByteArrayOutputStream out = new ByteArrayOutputStream(); byte[] buf = new byte[1024]; int
         * len; while ((len = is.read(buf)) != -1) { out.write(buf, 0, len); } String string = out.toString("UTF-8");
         * System.out.println(string); out.close();
         */
        return Stream2String(is);
    }

    /**
     * 处理POST请求
     * 
     * @param context
     * @param _postData
     * @return
     */
    public String createPostConnection(String url, List<NameValuePair> _postData) {

        // if (isNetworkConnected()) {
        String s = null;
        HttpPost httppost = new HttpPost(url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(_postData, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httppost);
            showToast("code~~" + httpResponse.getStatusLine().getStatusCode(), Gravity.CENTER);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.i("~~~~~~111~~~~result~~~~~~~~", httpResponse.toString());
                // byte[] result = EntityUtils.toByteArray(httpResponse.getEntity());
                // s = new String(result);
                // Log.i("~~~~~~~~~~result~~~~~~~~", s);
                // showToast("result ~~~~~~" +s,Gravity.CENTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Exception ~~~~~~", Gravity.CENTER);
        }
        // return "1";
        return s;
        // } else {
        // showToast("检测到无网络活动状态",Gravity.CENTER);
        // }
        // return null;

    }

    public void showToast(final String text, final int position) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.setGravity(position, 0, 0);

                toast.show();
            }
        });
    }

    public static InputStream String2InputStream(String str) {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    /**
     * PULL解析XML
     * 
     * @author: guoxiuli
     * @Date: 2012-9-14 上午11:26:11
     */
    public List<String> parseXML(List<String> itemList, InputStream inStream) throws XmlPullParserException,
            IOException {

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();// 产生第一个事件
        List<String> respList = new ArrayList<String>();
        System.out.print("respList" + "<----" + respList + "------>");
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:
                break;
            case XmlPullParser.START_TAG:
                String name = parser.getName();// 获取解析器当前指向的元素的名称
                // 如果说itemList的值包含name，将他们添加到respList里
                if (itemList.contains(name)) {
                    respList.add(parser.nextText());
                }
                break;

            default:
                break;
            }
            eventType = parser.next();// 进入下一个元素并触发相应事件
        }
        inStream.close();
        return respList;
    }

    /**
     * PULL解析XML
     * 
     * @author: guoxiuli
     * @Date: 2012-9-14 上午11:26:11
     */
    public HashMap<Object, Object> parseXML2(InputStream inStream, String... strings) throws XmlPullParserException,
            IOException {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();// 产生第一个事件
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:

                break;
            case XmlPullParser.START_TAG:
                String name = parser.getName();// 获取解析器当前指向的元素的名称
                // 如果说itemList的值包含name，将他们添加到respList里
                String text = parser.nextText();
                hashMap.put(name, text);
                break;

            default:
                break;
            }
            eventType = parser.next();// 进入下一个元素并触发相应事件
        }
        inStream.close();
        return hashMap;
    }

    public static String Stream2String(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 16 * 1024); // 强制缓存大小为16KB，一般Java类默认为8KB
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) { // 处理换行符
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        // showToast("Stream2String~~~~~~"+ sb.toString(),Gravity.CENTER);
        return sb.toString();
    }

    /**
     * json转化成list（适用于非列表的）
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public List<String> Json2StringList(InputStream is, String... strings) throws JSONException {
        String json = Stream2String(is);
        List<String> list = new ArrayList<String>();
        for (String arg : strings) {
            list.add(new JSONObject(json).getString(arg));
        }
        return list;
    }

    /**
     * json转化成list（适用于非列表的）
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static HashMap<Object, Object> JsonString2HashMap(String json, String... strings) throws JSONException {
        HashMap<Object, Object> list = new HashMap<Object, Object>();
        for (String arg : strings) {
            list.put(arg, new JSONObject(json).getString(arg));
        }
        return list;
    }

    /**
     * json转化成list（适用于非列表的）
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static List<String> Json2StringList2(String json, String... strings) throws JSONException {
        List<String> list = new ArrayList<String>();
        for (String arg : strings) {
            list.add(new JSONObject(json).getString(arg));
        }
        return list;
    }
}