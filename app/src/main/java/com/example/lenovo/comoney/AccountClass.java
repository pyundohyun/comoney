package com.example.lenovo.comoney;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
/**
 * Created by lenovo on 2016-11-09.
 */

public class AccountClass extends MainActivity {

    Button make_account_btn;
    EditText id;
    EditText password;
    EditText name;
    EditText partner;
    EditText month_money;
    CheckBox checkBox;
    Button search_partner;

    private AsyncTask<String, String, String> mTask;

    // 카카오톡 링크 이용
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private final String imageSrc = "http://cfile6.uf.tistory.com/image/244EEC39566FB8AC11196B";
    private final String siteUrl = "http://naver.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_account);

        id = (EditText) findViewById(R.id.insert_id);
        password = (EditText) findViewById(R.id.insert_passwd);
        name = (EditText) findViewById(R.id.insert_name);
        partner = (EditText) findViewById(R.id.insert_partner);
        month_money = (EditText) findViewById(R.id.month_money);

        make_account_btn = (Button) findViewById(R.id.make_account);
        checkBox = (CheckBox)findViewById(R.id.chbox);

        search_partner = (Button)findViewById(R.id.search_partner);


        // 가입하기 버튼
        make_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db 타기
                // 여기서 패스워드 String 아니면 걸러야함 매칭
                Pattern p = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
                Matcher m = p.matcher(password.getText());
                if (m.find()){
                    // 일정도 잘못되면
                    if(month_money.getText().toString().length()<2){
                        Toast.makeText(AccountClass.this, "잘못된 입금액입니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // 개인정보 동의 안했을때 가입안됨
                    else if(checkBox.isChecked()==false){
                        Toast.makeText(AccountClass.this, "개인정보보호 및 이용약관에 동의해주세요", Toast.LENGTH_LONG).show();
                    }
                    else {
                        mTask = new MyAsyncTask().execute();
                    }
                }
                else{
                    Toast.makeText(AccountClass.this, "잘못된 비밀번호 설정입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d("??", String.valueOf(checkBox.isChecked()));
            }
        });

        // 상대방 초대 버튼
        search_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 카카오 링크 이용
                try {
                    kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
                    kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                    kakaoTalkLinkMessageBuilder.addText("당신을 CoMoney로 초대합니다!");
                    kakaoTalkLinkMessageBuilder.addImage(imageSrc, 100, 100);
                    // 여기 웹사이트를 적고 아까 개발자 웹 사이트 에서 일치시켜줘야함
                    kakaoTalkLinkMessageBuilder.addWebButton("앱 다운로드!", siteUrl);
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), AccountClass.this);
                } catch (KakaoParameterException e) {
                    Log.d("error", e.getMessage());
                }
            }
        });
    }

    public void query2() {
        Connection conn = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
            Statement stmt = conn.createStatement();
            stmt.executeQuery("INSERT INTO tb_account " +
                    "(ID,PASSWORD,NAME,PARTNER,MONTH_MONEY) " +
                    "VALUES ('" + id.getText() + "', '" + password.getText() + "', '" + name.getText() + "', '" + partner.getText() + "' , '" + month_money.getText() + "')");


            conn.close();
        } catch (Exception e) {
            Log.d("Error connection", "" + e.getMessage());
        }
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        // @Override
        protected void onPreExecute() {
        }

        // @Override
        protected String doInBackground(String... params) {
            if (isCancelled())
                return (null); // don't forget to terminate this method
            query2();
            return null;
        }

        //@Override
        protected void onPostExecute(String result) {
            Toast.makeText(AccountClass.this, "가입이 완료 되었습니다", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AccountClass.this, MainActivity.class);
            startActivity(intent);
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
