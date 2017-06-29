package com.example.lenovo.comoney;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016-11-16.
 */
@SuppressLint("SetJavaScriptEnabled")
public class chartActivity extends Activity {

    WebView donut_chart;
    ArrayList<String> play_type_play_count, play_type_eat_count, play_type_rest_count;
    ArrayList<String> play_month, play_type_eat_month_toal, play_type_rest_month_toal,play_type_play_month_toal;


    Intent intent;

    String this_month;
    private String thismonth_eat_spendmoney;
    private String thismonth_play_spendmoney;
    private String thismonth_rest_spendmoney;

    private int sum_eat;
    private int sum_play;
    private int sum_rest;

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);


        intent = getIntent();
        Log.d("play_type_play_count", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_type_play_count")));
        Log.d("play_type_eat_count", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_type_eat_count")));
        Log.d("play_type_rest_count", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_type_rest_count")));
        Log.d("play_month", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_month")));
        Log.d("play_type_eat_month_toal", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_type_eat_month_toal")));
        Log.d("play_type_rest_month_toal", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_type_rest_month_toal")));
        Log.d("play_type_rest_month_toal", String.valueOf((ArrayList<String>)intent.getSerializableExtra("play_type_rest_month_toal")));
        Log.d("show_this_month", String.valueOf(intent.getStringExtra("show_this_month")));


        play_type_play_count = (ArrayList<String>)intent.getSerializableExtra("play_type_play_count");
        play_type_eat_count = (ArrayList<String>)intent.getSerializableExtra("play_type_eat_count");
        play_type_rest_count = (ArrayList<String>)intent.getSerializableExtra("play_type_rest_count");

        play_month = (ArrayList<String>)intent.getSerializableExtra("play_month");
        play_type_eat_month_toal = (ArrayList<String>)intent.getSerializableExtra("play_type_eat_month_toal");
        play_type_rest_month_toal = (ArrayList<String>)intent.getSerializableExtra("play_type_rest_month_toal");
        play_type_play_month_toal = (ArrayList<String>)intent.getSerializableExtra("play_type_play_month_toal");

        this_month = intent.getStringExtra("show_this_month");

        thismonth_eat_spendmoney = intent.getStringExtra("thismonth_eat_spendmoney");
        thismonth_play_spendmoney = intent.getStringExtra("thismonth_play_spendmoney");
        thismonth_rest_spendmoney = intent.getStringExtra("thismonth_rest_spendmoney");


        donut_chart = (WebView)findViewById(R.id.category_web_view);
        donut_chart.addJavascriptInterface(new WebAppInterface(), "Android");
        donut_chart.getSettings().setJavaScriptEnabled(true);
        donut_chart.loadUrl("file:///android_asset/donut_chart.html");

    }

    // js 파일에서 읽게 리턴해줌

    public class WebAppInterface {
        @JavascriptInterface
        public int getplay_type_eat(){

            int play_type_eat_count_sum = 0;

            for(int i=0; i<play_type_eat_count.size(); i++) {
                //Log.d("i",play_type_eat_count.get(i));
                play_type_eat_count_sum += Integer.valueOf(play_type_eat_count.get(i));
            }

            return play_type_eat_count_sum;
        };

        @JavascriptInterface
        public int getplay_type_rest(){
            int play_type_rest_count_sum = 0;

            for(int i=0; i<play_type_rest_count.size(); i++) {
                //Log.d("i",play_type_eat_count.get(i));
                play_type_rest_count_sum += Integer.valueOf(play_type_rest_count.get(i));
            }
            return play_type_rest_count_sum;
        };

        @JavascriptInterface
        public int getplay_type_play(){

            int play_type_play_count_sum = 0;

            for(int i=0; i<play_type_play_count.size(); i++) {
                //Log.d("i",play_type_eat_count.get(i));
                play_type_play_count_sum += Integer.valueOf(play_type_play_count.get(i));
            }
            return play_type_play_count_sum;
        };

        @JavascriptInterface
        // 선택 월로
        public String getdate_firstmonth(){
            for(int i=0; i<play_month.size(); i++)
            {
                if(this_month.equals(play_month.get(i)))
                {
                    return play_month.get(i);
                }
            }
            return this_month;
        };

        @JavascriptInterface

        // 앞뒤 두달로
        public String getdate_secondmonth(){
            return "total";
        };

        @JavascriptInterface
        public int eat_month_total(){

            for(int i=0; i<play_type_eat_month_toal.size(); i++)
            {
                if(thismonth_eat_spendmoney.equals(play_type_eat_month_toal.get(i)))
                {
                    return Integer.parseInt(play_type_eat_month_toal.get(i));
                }
            }
            return 0;
        };

        @JavascriptInterface
        public int eat_second_month_total(){

            int total_year_eat = 0;

            for(int i=0; i<play_type_eat_month_toal.size(); i++)
            {
                total_year_eat = total_year_eat +Integer.parseInt(play_type_eat_month_toal.get(i));
            }
            return total_year_eat;
        };

        @JavascriptInterface
        public int play_month_total(){

            for(int i=0; i<play_type_play_month_toal.size(); i++)
            {
                if(thismonth_play_spendmoney.equals(play_type_play_month_toal.get(i)))
                {
                    return Integer.parseInt(play_type_play_month_toal.get(i));
                }
            }
            return 0;

        };


        @JavascriptInterface
        public int play_second_total(){


            int total_year_play = 0;

            for(int i=0; i<play_type_play_month_toal.size(); i++)
            {
                total_year_play = total_year_play +Integer.parseInt(play_type_play_month_toal.get(i));
            }
            return total_year_play;

        };

        @JavascriptInterface
        public int rest_month_total(){

            for(int i=0; i<play_type_rest_month_toal.size(); i++)
            {
                if(thismonth_rest_spendmoney.equals(play_type_rest_month_toal.get(i)))
                {
                    return Integer.parseInt(play_type_rest_month_toal.get(i));
                }
            }
            return 0;
        };

        @JavascriptInterface
        public int rest_second_month_total(){

            int total_year_rest = 0;

            for(int i=0; i<play_type_rest_month_toal.size(); i++)
            {
                total_year_rest = total_year_rest +Integer.parseInt(play_type_rest_month_toal.get(i));
            }
            return total_year_rest;

        };

        @JavascriptInterface
        public int month_total(){

            for(int i=0; i<play_type_eat_month_toal.size(); i++)
            {
                if(thismonth_eat_spendmoney.equals(play_type_eat_month_toal.get(i)))
                {
                    sum_eat = Integer.parseInt(play_type_eat_month_toal.get(i));
                }
            }

            for(int i=0; i<play_type_play_month_toal.size(); i++)
            {
                if(thismonth_play_spendmoney.equals(play_type_play_month_toal.get(i)))
                {
                    sum_play = Integer.parseInt(play_type_play_month_toal.get(i));
                }
            }

            for(int i=0; i<play_type_rest_month_toal.size(); i++)
            {
                if(thismonth_rest_spendmoney.equals(play_type_rest_month_toal.get(i)))
                {
                    sum_rest =Integer.parseInt(play_type_rest_month_toal.get(i));
                }
            }

            int play_type_month_total_sum = sum_eat+sum_play+sum_rest;

            return play_type_month_total_sum;
        };

        @JavascriptInterface
        public int  second_month_total(){

            int play_type_second_month_total_sum;

            int total_year_rest = 0;
            int total_year_play = 0;
            int total_year_eat = 0;


            for(int i=0; i<play_type_play_month_toal.size(); i++)
            {
                total_year_play = total_year_play +Integer.parseInt(play_type_play_month_toal.get(i));
            }

            for(int i=0; i<play_type_rest_month_toal.size(); i++)
            {
                total_year_rest = total_year_rest +Integer.parseInt(play_type_rest_month_toal.get(i));
            }

            for(int i=0; i<play_type_eat_month_toal.size(); i++)
            {
                total_year_eat = total_year_eat +Integer.parseInt(play_type_eat_month_toal.get(i));
            }

            play_type_second_month_total_sum = total_year_play + total_year_rest + total_year_eat;
            return play_type_second_month_total_sum;

        };

        @JavascriptInterface
        public int  month_average(){

            return month_total()/3;
        };

        @JavascriptInterface
        public int  second_average(){

            return second_month_total()/3;
        };


    }
}