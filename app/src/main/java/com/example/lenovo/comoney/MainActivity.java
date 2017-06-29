package com.example.lenovo.comoney;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

    public class MainActivity extends Activity {

    Button Login_btn;
    Button Account_btn;
    EditText select_id;
    EditText select_password;

    private AsyncTask<String, String, String> mTask_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login_btn = (Button) findViewById(R.id.login_btn);
        Account_btn = (Button) findViewById(R.id.account_btn);

        select_id = (EditText) findViewById(R.id.id_textview);
        select_password = (EditText) findViewById(R.id.passwd_textview);


        //로그인
        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 판별 후 다음창
                mTask_login = new MyAsyncTask().execute(select_id.getText().toString(),select_password.getText().toString());

            }
        });

        // 계정생성
        Account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 가입하기 넘어가기
                Intent intent = new Intent(MainActivity.this, AccountClass.class);
                startActivity(intent);
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }

        // @Override
        protected String doInBackground(String... params) {
            Log.d("params", params[1]);

            if (isCancelled())
                return (null); // don't forget to terminate this method
            Connection conn = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
                Statement stmt = conn.createStatement();
                ResultSet data = stmt.executeQuery("SELECT PASSWORD FROM tb_account WHERE ID = '"+params[0] +"'");
                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    //Log.d("tag", String.valueOf(data.getString("PASSWORD")));

                    Log.d("PASSWORD", data.getString("PASSWORD"));
                    // 패스워드 같으면
                    if (params[1].equals(data.getString("PASSWORD"))) {
                        return "success";
                    }
                    // 패스원드 다르면 fail
                    else {
                        return "fail";
                    }
                }
                // 아이디 , 패스워드 판별

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }

        //@Override
        protected void onPostExecute(String result) {
            Log.d("reuslt", result);
            switch (result) {
                case "success":
                    Toast.makeText(MainActivity.this, "반갑습니다", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, ContentClass.class);
                    // 데이터도 넘김
                    intent.putExtra("id",select_id.getText().toString());
                    startActivity(intent);
                    break;
                case "fail":
                    Toast.makeText(MainActivity.this, "잘못된 비밀번호 입니다.", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
