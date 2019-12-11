package com.example.todolist.API;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverAPI {
    private final String clientId = "ufUuYw7O1ck_IAN8JL2L";
    private final String clientSecret = "TuheD7osPz";
    private String text;
    private String apiURL;
    private URL url;
    private HttpURLConnection curl;
    private String string;

    private String local;
    private String tstr;

    public String getClientId() {return clientId;}
    public String getClientSecret() {return clientSecret;}
    public String getLocal() {return local;}
    public String getTstr() {return tstr;}

    public NaverAPI(final String str, final String tl){
        Log.e("로컬"+local," 번역"+tstr);
        language(str);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Papago(str, tl);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("로컬" + local, " 번역" + tstr);
                    }
                }, 250);
            }
        }, 250);
    }

    public void Papago(final String str, final String tl){
        new Thread(){
            public void run(){
                try {
                    String text = URLEncoder.encode(str, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    // post request
                    String postParams = "source="+local+"&target="+tl+"&text=" + text;
                    Log.e("인튜디언노우", postParams);
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postParams);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    String sw = response.toString();
                    int start = sw.indexOf("\"translatedText\":\"")+"\"translatedText\":\"".length();
                    int end = sw.indexOf("\"",start);
                    tstr = sw.substring(start, end);
//                    tstr = response.toString();
                    Log.e("번역 확인?",tstr);
                } catch (Exception e) {
                    Log.e("번역 오류", e.toString());
                }
            }
        }.start();
    }
    public void language(final String str) {
        new Thread(){
            public void run(){
                try {
                    String query = URLEncoder.encode(str, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    // post request
                    String postParams = "query=" + query;
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postParams);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    String sw = response.toString();
                    int start = sw.indexOf("\"langCode\" : \"")+"\"langCode\" : \"".length();
                    int end = sw.indexOf("\"",start);
                    local = sw.substring(start, end);
                    Log.e("언어 국가", local);
                } catch (Exception e) {
                    Log.e("언어 오류", e.toString());
                }
            }
        }.start();

    }
}