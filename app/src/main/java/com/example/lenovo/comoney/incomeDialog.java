package com.example.lenovo.comoney;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Created by lenovo on 2016-11-16.
 */
public class incomeDialog extends Dialog {


    private final String mTitle;
    private final String id;
    private final TextView input_month_income;
    TextView title;
    EditText income_month_reinput;
    private Button mLeftButton;
    private Button mRightButton;


    incomeDialog thisDialog = null;


    // 입금액 변경
    private AsyncTask<String, String, String> Change_input_month_income;

    private AsyncTask<String, String, String> Change_input_month_income_refresh;

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public incomeDialog(Context context, String title, String id, TextView input_month_income) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.id = id;
        this.input_month_income = input_month_income;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;

        // 여기서 다이얼로그 크기 설정 안하면 전체로 잡힘 필수!!
        lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_income_dialog);

        title = (TextView)findViewById(R.id.txt_content);
        title.setText(mTitle);

        income_month_reinput = (EditText)findViewById(R.id.income_month_reinput);
        income_month_reinput.getText();

        mLeftButton = (Button) findViewById(R.id.btn_modify);
        mRightButton = (Button) findViewById(R.id.btn_right);

        // 다이얼로그 클릭 리스너
        thisDialog = this;


        // 확인버튼
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("선택자222", income_month_reinput.getText().toString());
                Change_input_month_income = new MyAsyncTask1().execute(income_month_reinput.getText().toString(),id);

                // 갱신
                Change_input_month_income_refresh  =  new MyAsyncTask2().execute(id);
                thisDialog.dismiss();
            }
        });

        // 취소버튼
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisDialog.dismiss();
            }
        });

    }


    private class MyAsyncTask1 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

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
                ResultSet data = stmt.executeQuery("UPDATE tb_account SET MONTH_MONEY ='"+ params[0] +"' WHERE ID='"+ params[1] +"' ");

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class MyAsyncTask2 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("value2222",values[0]);
            input_month_income.setText(toNumFormat(Integer.parseInt(values[0]))+"원");
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
                ResultSet data = stmt.executeQuery("SELECT MONTH_MONEY FROM tb_account WHERE ID='"+ params[0] +"' ");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("MONTH_MONEY"));
                }

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(),"갱신되었습니다.",Toast.LENGTH_SHORT).show();
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    // 천자리 콤마
    public static String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

}
