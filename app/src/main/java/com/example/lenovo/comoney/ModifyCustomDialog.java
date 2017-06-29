package com.example.lenovo.comoney;

import android.app.Dialog;
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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by lenovo on 2016-11-25.
 */
public class ModifyCustomDialog extends Dialog {


    private TextView mDateView;
    private Button mModifyButton;
    private Button mRightButton;
    private String mTitle;
    private String mDate;

    ModifyCustomDialog thisDialog = null;
    EditText detail_data;
    EditText spend_money;
    String play_type;

    private AsyncTask<String, String, String> mTask_ModifyData;
    private String id_receive;

    // 다시 받아오는 현재 값
    private String now_detail_money;
    private String now_spend_money;
    private String nowdate;
    private String now_play_type;
    private String now_id;

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
        setContentView(R.layout.activity_custom_modify_dialog);


        mDateView = (TextView) findViewById(R.id.txt_content);
        mModifyButton = (Button) findViewById(R.id.btn_modify);
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


        // 현재 표현되야할 내용
        detail_data.setText(now_detail_money);
        spend_money.setText(now_spend_money);
        mDateView.setText(nowdate);

        // 다이얼로그 클릭 리스너
        thisDialog = this;
        // 수정버튼
        mModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("바뀔 키워드", detail_data.getText().toString() + spend_money.getText() + play_type);

                // 데이터 받아오고 , db저장 , 밑에 리스트 뷰 뿌림
                // db 연결
                if (spend_money.getText().toString().equals("") || detail_data.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("바뀔 키워드", now_play_type + "vs" + play_type);

                if (now_play_type.equals(play_type)) {
                    // 등록 db 쿼리
                    mTask_ModifyData = new ModifyCustomDialog.MyAsyncTask().execute(now_id, now_play_type, now_detail_money, now_spend_money, nowdate,
                            detail_data.getText().toString(), spend_money.getText().toString());

                    thisDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "지출 항목을 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
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


    // 오버라이드 이 생성자는 수정할때의 생성자
    public ModifyCustomDialog(ContentClass contentClass, String s, String date, String play_type, String detail_money, String spend_money, String id) {
        super(contentClass, android.R.style.Theme_Translucent_NoTitleBar);

        this.nowdate = date;
        this.now_detail_money = detail_money;
        this.now_spend_money = spend_money;
        this.now_play_type = play_type;
        this.now_id = id;
    }


    private class MyAsyncTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

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
                ResultSet data = stmt.executeQuery("UPDATE tb_data SET PLAY_TYPE='" + play_type + "', DETAIL_SPEND='" + params[5] + "', SPEND_MONEY='" + remove_comma(params[6]) + "'" +
                        "WHERE SPEND_DATE ='" + datesort(params[4]) + "' AND ID ='" + params[0] + "' AND PLAY_TYPE='" + params[1] + "' AND DETAIL_SPEND='" + params[2]
                        + "' AND SPEND_MONEY='" + remove_comma(params[3]) + "'");

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), "수정이 완료 되었습니다", Toast.LENGTH_SHORT).show();
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private String remove_comma(String param) {
        return param.replaceAll("\\,", "");
    }

    private String datesort(String param) {
        Log.d("datesort", param.substring(0, 4) + param.substring(5, 7) + param.substring(8, 10));
        return param.substring(0, 4) + param.substring(5, 7) + param.substring(8, 10);
    }
}
