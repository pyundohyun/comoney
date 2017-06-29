package com.example.lenovo.comoney;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Created by lenovo on 2016-11-11.
 */

public class MainView extends Activity {

    Intent intent;
    TextView man_txt;
    TextView girl_txt;
    TextView input_month_income;
    TextView person2_input_month_income;
    TextView month_total_income;
    TextView month_total_spend;
    TextView month_total_extra;
    TextView input_month_spend_person;
    TextView input_month_spend_person2;
    TextView this_month_spend;
    TextView this_month_spend_person2;

    // 입금액 변경
    TextView person1_month_income;

    // 데이터 가져오는것 (나의 지출액)
    private AsyncTask<String, String, String> mTask_total_spend_data;

    // 데이터 가져오는것 (상대의 지출액)
    private AsyncTask<String, String, String> mTask_opponent_total_spend_data;





    private incomeDialog income_Dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        intent = getIntent();
        Log.d("id들어오니", intent.getExtras().getString("id"));
        //앞에서 먼저 읽고 받아옴
        Log.d("person1", intent.getExtras().getString("person1"));
        Log.d("person1", intent.getExtras().getString("person2"));
        Log.d("person1", intent.getExtras().getString("input_month_income"));
        Log.d("person1", intent.getExtras().getString("person2_input_month_income"));
        Log.d("person1", intent.getExtras().getString("person2_id"));
        Log.d("person1", intent.getExtras().getString("month_total_income"));

        // 날짜 받아옴
        Log.d("this_year", intent.getExtras().getString("this_year"));
        Log.d("this_month", intent.getExtras().getString("this_month"));

        man_txt = (TextView) findViewById(R.id.man_text);


        girl_txt = (TextView) findViewById(R.id.girl_text);
        input_month_income = (TextView) findViewById(R.id.input_month_income);
        person2_input_month_income = (TextView) findViewById(R.id.person2_input_month_income);
        month_total_income = (TextView) findViewById(R.id.month_total_income);
        month_total_spend = (TextView) findViewById(R.id.month_total_spend);
        month_total_extra = (TextView) findViewById(R.id.month_total_extra);
        input_month_spend_person = (TextView) findViewById(R.id.input_month_spend_person);
        input_month_spend_person2 = (TextView) findViewById(R.id.input_month_spend_person2);

        this_month_spend = (TextView) findViewById(R.id.this_month_spend);
        this_month_spend_person2 = (TextView) findViewById(R.id.this_month_spend_person2);

        man_txt.setText(intent.getExtras().getString("person1"));
        girl_txt.setText(intent.getExtras().getString("person2"));
        input_month_income.setText(intent.getExtras().getString("input_month_income"));
        person2_input_month_income.setText(intent.getExtras().getString("person2_input_month_income"));
        month_total_income.setText(intent.getExtras().getString("month_total_income"));

        this_month_spend.setText(intent.getExtras().getString("this_month")+"월 지출액");
        this_month_spend_person2.setText(intent.getExtras().getString("this_month")+"월 지출액");


        person1_month_income = (TextView)findViewById(R.id.person1_month_income);

        // 이번달 입금액 변경 (나)
        person1_month_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 창 띄우기
                income_Dialog = new incomeDialog(MainView.this
                        , "[입금액 변경]"
                        ,intent.getExtras().getString("id")
                        ,input_month_income
                );

                income_Dialog.show();
            }
        });

        // 데이터 가져오는것 (상대의 지출액)
        mTask_opponent_total_spend_data = new MyAsyncTask2().execute(intent.getExtras().getString("person2_id"));

        // 데이터 가져오는것 (나의 지출액)
        mTask_total_spend_data = new MyAsyncTask3().execute(man_txt.getText().toString());

    }

    // 콤마 삭제
    private int comma_remove(String s) {
        return Integer.parseInt(s.replaceAll("[^0-9]", ""));
    }

    // 천자리 콤마
    public static String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public void refresh_income() {

    }


    private class MyAsyncTask2 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("value", String.valueOf(values[0]));

            // 상대방이 값 지출 안했을때
            if(values[0] == null)
            {
                values[0] = String.valueOf(0);
                input_month_spend_person2.setText(toNumFormat(Integer.parseInt(values[0])) + "원");
            }
            else {
                input_month_spend_person2.setText(toNumFormat(Integer.parseInt(values[0])) + "원");
            }
        }

        // @Override
        protected String doInBackground(String... params) {
            Log.d("params",params[0]);
            if (isCancelled())
                return (null); // don't forget to terminate this method
            Connection conn = null;


            // 월별 지출액으로 뒤에 like
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
                Statement stmt = conn.createStatement();
                ResultSet data = stmt.executeQuery("select SUM(SPEND_MONEY) AS[TOTAL_MONEY] FROM tb_data WHERE ID ='" + params[0] + "'"
                + "AND SPEND_DATE LIKE" + "'%"+intent.getExtras().getString("this_year")+intent.getExtras().getString("this_month") +"%'");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("TOTAL_MONEY"));
                }

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {
            // Log.d("reuslt", result);
            Log.d("완료3", "완료");
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class MyAsyncTask3 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // 상대방이 값 지출 안했을때
            if(values[0] == null)
            {
                values[0] = String.valueOf(0);
                input_month_spend_person.setText(toNumFormat(Integer.parseInt(values[0])) + "원");
            }
            else {
                input_month_spend_person.setText(toNumFormat(Integer.parseInt(values[0])) + "원");
            }
        }

        // @Override
        protected String doInBackground(String... params) {

            if (isCancelled())
                return (null); // don't forget to terminate this method
            Connection conn = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
                Statement stmt = conn.createStatement();
                ResultSet data = stmt.executeQuery("select SUM(SPEND_MONEY) AS[TOTAL_MONEY] FROM tb_data WHERE ID ='" + intent.getExtras().getString("id") + "'"
                + "AND SPEND_DATE LIKE" + "'%"+intent.getExtras().getString("this_year")+date_prove(intent.getExtras().getString("this_month")) +"%'");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("TOTAL_MONEY"));
                }

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {

           //총지출 표시
            Log.d("지출1", String.valueOf(comma_remove(input_month_spend_person.getText().toString())));
            Log.d("지출2", String.valueOf(comma_remove(input_month_spend_person2.getText().toString())));


            int total_spend_money = comma_remove(input_month_spend_person.getText().toString())
            +comma_remove(input_month_spend_person2.getText().toString());

            Log.d("지출3", String.valueOf(total_spend_money));

            month_total_spend.setText("이번달 지출: "+toNumFormat(total_spend_money)+"원");

            // 총 잔액 표시
            int extra_money = comma_remove(month_total_income.getText().toString()) - comma_remove(month_total_spend.getText().toString());

            month_total_extra.setText("이번달 잔액: "+toNumFormat(extra_money)+"원");

            Log.d("완료4", "완료");
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private String date_prove(String this_month) {

        StringBuffer sb = new StringBuffer(this_month);
        // 뒤만 한자리 일때
        // 201611 7자리
        // 월일에서
        if (this_month.length() == 1) {
            // 월이 1자리 일경우
                sb.insert(0, "0");
        }

        return String.valueOf(sb);

    }

}
