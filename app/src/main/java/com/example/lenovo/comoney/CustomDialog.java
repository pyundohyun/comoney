package com.example.lenovo.comoney;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by lenovo on 2016-11-10.
 */
public class CustomDialog extends Dialog {


    private TextView mDateView;
    private Button mLeftButton;
    private Button mRightButton;
    private String mTitle;
    private String mDate;

    CustomDialog thisDialog = null;
    EditText detail_data;
    EditText spend_money;
    String play_type;

    private AsyncTask<String, String, String> mTask_sendData;
    private String id_receive;


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
        setContentView(R.layout.activity_custom_dialog);


        mDateView = (TextView) findViewById(R.id.txt_content);
        mLeftButton = (Button) findViewById(R.id.btn_modify);
        mRightButton = (Button) findViewById(R.id.btn_right);

        // 값가져옴 (내역, 비용)
        detail_data = (EditText) findViewById(R.id.input_data_detail);
        spend_money = (EditText) findViewById(R.id.input_spend_money);


        //드롭다운메뉴
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.play_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // 드롭다운 아이템 클릭 리스너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("선택자", parent.getItemAtPosition(position).toString());
                play_type = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "항목을 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });


            // 제목과 내용을 생성자에서 셋팅한다.
            setTitle(mTitle);
            mDateView.setText(mDate);

        // 다이얼로그 클릭 리스너
        thisDialog = this;
        // 확인버튼
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("선택자1", mDateView.getText().toString()+detail_data.getText().toString() + spend_money.getText() + play_type);

                // 데이터 받아오고 , db저장 , 밑에 리스트 뷰 뿌림
                // db 연결
                if (spend_money.getText().toString().equals("") || detail_data.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                    // 등록 db 쿼리
                    mTask_sendData = new MyAsyncTask().execute();

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

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public CustomDialog(Context context, String title,
                        String date, String id_data
                  /*     ,View.OnClickListener leftListener,
                        View.OnClickListener rightListener*/
    ) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        Log.d("title", title);
        this.mDate = date;
        Log.d("date", date);
        this.id_receive = id_data;
/*        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;*/

    }


    public void query3() {
        Connection conn = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
            Statement stmt = conn.createStatement();
            stmt.executeQuery("INSERT INTO tb_data " +
                    "(ID,PLAY_TYPE,DETAIL_SPEND,SPEND_MONEY,SPEND_DATE) " +
                    "VALUES ('" + id_receive + "', '" + play_type + "', '" + detail_data.getText() + "', '" + spend_money.getText() + "','"
                    + date_prove(mDateView.getText().toString().replaceAll("[^0-9]", "")) + "')");

            conn.close();
        } catch (Exception e) {
            Log.d("Error connection", "" + e.getMessage());
        }
    }

    private String date_prove(String s) {
    Log.d("일정 판별", String.valueOf(s.length()));

        StringBuffer sb = new StringBuffer(s);
        // 뒤만 한자리 일때
        // 201611 7자리
        // 월일에서
        if (s.length() == 7) {
            // 월이 1자리 일경우
            if(s.substring(4).length()==3)
            {
                sb.insert(4, "0");
            }
            //일이 1자리
            else{
                sb.insert(6, "0");
            }
        }

        // 앞뒤 모두 한자리 날짜일때
        else if (s.length() == 6) {
            sb.insert(4, "0");
            sb.insert(6, "0");
        }
        return String.valueOf(sb);
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        // @Override
        protected void onPreExecute() {
        }

        // @Override
        protected String doInBackground(String... params) {
            if (isCancelled())
                return (null); // don't forget to terminate this method
            query3();
            return null;
        }

        //@Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), "일정이 등록되었습니다.", Toast.LENGTH_SHORT).show();

        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
