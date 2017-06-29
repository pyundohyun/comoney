package com.example.lenovo.comoney;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.lenovo.comoney.ContentClass.thisMonth;
import static com.example.lenovo.comoney.ContentClass.thisday;

/**
 * Created by lenovo on 2016-11-09.
 */

public class ContentClass extends AppCompatActivity implements View.OnClickListener {

    GridView mGridView;
    DateAdapter adapter;
    ArrayList<CalData> arrData;
    Calendar mCalToday;
    Calendar mCal;

    TextView maintext;
    int thisYear;
    public static int thisMonth;
    public static int thisday;

    private CustomDialog mCustomDialog;

    Intent intent;

    //리스트뷰
    private SwipeMenuListView data_list;
    private ArrayAdapter<String> data_Adapter;

    // 데이터 가져오는것
    private AsyncTask<String, String, String> mTask_data_list;


    //툴바
    Toolbar toolbar;
    private String id_gab;

    private int count;


    // 데이터 가져오는것 (파트너)
    private AsyncTask<String, String, String> mTask_Partner_data_list;

    // 데이터 가져오는것 (나의 지출액)
    private AsyncTask<String, String, String> mTask_total_spend_data;

    // 데이터 가져오는것 (상대의 지출액)
    private AsyncTask<String, String, String> mTask_opponent_total_spend_data;

    // 데이터 가져오는것 (해당월 전체 지출 내역)
    private AsyncTask<String, String, String> mTask_Total_data_list;


    //해당 월별 각각 먹자 수와 총 지출액 가져옴
    private AsyncTask<String, String, String> mTask_count_play_type_eat;

    //해당 월별 각각 놀자 수와 총 지출액 가져옴
    private AsyncTask<String, String, String> mTask_count_play_type_play;

    //해당 월별 각각 쉬자 수와 총 지출액 가져옴
    private AsyncTask<String, String, String> mTask_count_play_type_rest;

    //해당 월만 먹자,놀자,쉬자 각각 총 지출액 가져옴
    private AsyncTask<String, String, String> mTask_thismonth_play_type_eat;
    private AsyncTask<String, String, String> mTask_thismonth_play_type_play;
    private AsyncTask<String, String, String> mTask_thismonth_play_type_rest;


    //리스트에서 수정할때 DB에서도 지우는것
    private AsyncTask<String, String, String> mModify_delete;

    // db 가져온값 저장
    private String person1;
    private String person2;
    private String input_month_income;
    private String person2_input_month_income;
    private String person2_id;
    private String month_total_income;


    // 차트에 보내줄값
    private List<String> play_type_play;
    private List<String> play_type_eat;
    private List<String> play_type_rest;
    private List<String> play_type_eat_month_toal;
    private List<String> play_month;
    private List<String> play_type_rest_month_toal;
    private List<String> play_type_play_month_toal;

    // 이번달만 놀이 타입에 대한 소비액
    private String thismonth_eat_spendmoney;
    private String thismonth_play_spendmoney;
    private String thismonth_rest_spendmoney;

    // 툴바에 서브 icon 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // 툴바에 서브 icon 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("뭐가 클릭 되냐", String.valueOf(item));
        if (String.valueOf(item).equals("통계 보기")) {
            Log.d("item 클릭 되냐", String.valueOf(item));
            Log.d("item 클릭 되냐", String.valueOf(play_type_eat));
            Log.d("item 클릭 되냐", String.valueOf(play_type_eat_month_toal));
            Log.d("item 클릭 되냐", String.valueOf(play_month));
            Log.d("item 클릭 되냐", String.valueOf(play_type_play));
            Log.d("item 클릭 되냐", "" + thisYear + thisMonth);
            Log.d("item 클릭 되냐", "" + thismonth_eat_spendmoney);
            Log.d("item 클릭 되냐", "" + thismonth_play_spendmoney);

            if(thismonth_rest_spendmoney == null)
            {
                thismonth_rest_spendmoney = String.valueOf(0);
            }
            Log.d("item 클릭 되냐", "" + thismonth_rest_spendmoney);



            Intent intent = new Intent(ContentClass.this, chartActivity.class);
            intent.putExtra("play_type_play_count", (Serializable) play_type_play);
            intent.putExtra("play_type_eat_count", (Serializable) play_type_eat);
            intent.putExtra("play_type_rest_count", (Serializable) play_type_rest);
            intent.putExtra("play_month", (Serializable) play_month);
            intent.putExtra("show_this_month", "" + thisYear + Redata(String.valueOf(thisMonth)));
            intent.putExtra("play_type_eat_month_toal", (Serializable) play_type_eat_month_toal);
            intent.putExtra("play_type_rest_month_toal", (Serializable) play_type_rest_month_toal);
            intent.putExtra("play_type_play_month_toal", (Serializable) play_type_play_month_toal);

            intent.putExtra("thismonth_eat_spendmoney", thismonth_eat_spendmoney);
            intent.putExtra("thismonth_play_spendmoney",thismonth_play_spendmoney);
            intent.putExtra("thismonth_rest_spendmoney",thismonth_rest_spendmoney);

            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        // Calendar 객체 생성
        mCalToday = Calendar.getInstance();
        mCal = Calendar.getInstance();


        // 이동세팅
        thisYear = mCal.get(Calendar.YEAR);
        thisMonth = mCal.get(Calendar.MONTH) + 1;
        thisday = mCal.get(Calendar.DATE);
        Log.d("thisday", String.valueOf(thisday));


        Log.d("YEAR", String.valueOf(mCal.get(Calendar.YEAR)));
        Log.d("MONTH", String.valueOf(mCal.get(Calendar.MONTH) + 1));

        // 달력 세팅
        /*setCalendarDate(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH) + 1);*/
        setCalendarDate(thisMonth);

        findViewById(R.id.prev).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);

        intent = getIntent();
        Log.d("id", intent.getExtras().getString("id"));


        id_gab = intent.getExtras().getString("id");
        data_list = (SwipeMenuListView) findViewById(R.id.list_data);
        data_Adapter = new ArrayAdapter<String>(ContentClass.this, R.layout.list_view_custom);

        // 동적 리스트 (차트값 전송하기 위한 데이터들)
        play_type_rest = new ArrayList<String>();
        play_type_eat = new ArrayList<String>();
        play_month = new ArrayList<String>();
        play_type_eat_month_toal = new ArrayList<String>();
        play_type_play = new ArrayList<String>();
        play_type_play_month_toal = new ArrayList<String>();
        play_type_rest_month_toal = new ArrayList<String>();

        // 툴바
        initToolBar();

        // 파트너 이름,ID,현재가격 받아올것 ,db 접속 조인 쿼리 실행
        mTask_Partner_data_list = new MyAsyncTask2().execute();

        // 해당 월에 전체 내역서 리스트 뿌림
        mTask_Total_data_list = new MyAsyncTask3().execute();


        //해당 월에 항목 수 가져옴
        mTask_count_play_type_eat = new MyAsyncTask4().execute();

        mTask_count_play_type_play = new MyAsyncTask5().execute();

        mTask_count_play_type_rest = new MyAsyncTask6().execute();


    }

    private void setCalendarDate(int month) {
        arrData = new ArrayList<CalData>();

        if (month == 0) {
// 이전 해에 대한 연 및 월처리
            thisMonth = 12;
            --thisYear;
            mCal.set(thisYear, 11, 1);
        } else if (month == 13) {
// 다음해에 대한 연도 및 월 처리
            thisMonth = 1;
            ++thisYear;
            mCal.set(thisYear, 0, 1);
        } else {
// 1일에 맞는 요일을 세팅하기 위한 설정
            mCal.set(thisYear, month - 1, 1);
        }

        int thisStartDay = mCal.get(Calendar.DAY_OF_WEEK);
        if (thisStartDay != 1) {
            for (int i = 0; i < thisStartDay - 1; i++) {
                arrData.add(null);
            }
        }

        if (month == 13) {
            month = 0;
        } else if (month == 0) {
            month = 11;
        } else {
            --month;
        }

        mCal.set(thisYear, month, 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mCalToday.set(thisYear, month, (i + 1));
            arrData.add(new CalData((i + 1), mCalToday.get(Calendar.DAY_OF_WEEK)));
        }

        adapter = new DateAdapter(this, arrData);

        mGridView = (GridView) findViewById(R.id.calGrid);
        mGridView.setAdapter(adapter);

        // 날짜 버튼누르면
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 이동했을때 null 카운트 갯수 초기화
                count = 0;

                Log.d("position", String.valueOf(position));
                Log.d("id", String.valueOf(id));
                //전체 뿌리는 갯수

                // arrData 안에 null 갯수만 파악 해서 처음 postion에서 더해주면됨
                Log.d("arrData", String.valueOf(arrData));
                for (int i = 0; i < arrData.size(); i++) {
                    if (arrData.get(i) == null) {
                        count = count + 1;
                    }
                }
                // 다이얼로그 창 띄우기
                mCustomDialog = new CustomDialog(ContentClass.this
                        , "[일정 입력]"
                        // 여기 날짜를 넣어보자
                        , "" + thisYear + "년" + thisMonth + "월" + (position - count + 1) + "일"
                        //, "지출 내용을 입력해 주세요"
                        // 아이디도 여기서 보냄
                        , intent.getExtras().getString("id")
                );

                mCustomDialog.show();
            }
        });
        maintext = (TextView) findViewById(R.id.maintext);
        maintext.setText(thisYear + "/" + thisMonth);


        // 날짜 넘길때마다 해당 월데이터 받아오기 위함 쓰레드 여기에 씀
        mTask_thismonth_play_type_eat = new MyAsyncTask8().execute();
        mTask_thismonth_play_type_play = new MyAsyncTask9().execute();
        mTask_thismonth_play_type_rest = new MyAsyncTask10().execute();

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CO Money");
        setSupportActionBar(toolbar);
        // 아이콘 세팅

        toolbar.setNavigationIcon(R.drawable.couple);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("가니?", id_gab);
                // 툴바보낼때 아이디 같이 넘김
                Intent intent = new Intent(ContentClass.this, MainView.class);
                intent.putExtra("id", id_gab);
                intent.putExtra("person1", person1);
                intent.putExtra("person2", person2);
                intent.putExtra("input_month_income", input_month_income);
                intent.putExtra("person2_input_month_income", person2_input_month_income);
                intent.putExtra("person2_id", person2_id);
                intent.putExtra("month_total_income", month_total_income);


                // 보낼때 날짜 구분해주려면 날짜도 같이 보낼것
                intent.putExtra("this_year", String.valueOf(thisYear));
                intent.putExtra("this_month", String.valueOf(thisMonth));


                startActivity(intent);
            }
        });
    }


    /*public void setCalendarDate(int year, int month) {

        Log.d("year,month", String.valueOf(year) + String.valueOf(month));

        arrData = new ArrayList<CalData>();

        // 1일에 맞는 요일을 세팅하기 위한 설정
        mCalToday.set(mCal.get(Calendar.YEAR), month - 1, 1);

        int startday = mCalToday.get(Calendar.DAY_OF_WEEK);
        if (startday != 1) {
            for (int i = 0; i < startday - 1; i++) {
                arrData.add(null);
            }
        }

        // 요일은 +1해야 되기때문에 달력에 요일을 세팅할때에는 -1 해준다.
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mCalToday.set(mCal.get(Calendar.YEAR), month - 1, (i + 1));
            arrData.add(new CalData((i + 1), mCalToday.get(Calendar.DAY_OF_WEEK)));
        }

        adapter = new DateAdapter(ContentClass.this, arrData);

        mGridView = (GridView) findViewById(R.id.calGrid);
        mGridView.setAdapter(adapter);

        // 날짜 버튼누르면
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 이동했을때 null 카운트 갯수 초기화
                count = 0;

                Log.d("position", String.valueOf(position));
                Log.d("id", String.valueOf(id));
                //전체 뿌리는 갯수

                // arrData 안에 null 갯수만 파악 해서 처음 postion에서 더해주면됨
                Log.d("arrData", String.valueOf(arrData));
                for(int i =0; i< arrData.size(); i++){
                    if (arrData.get(i) == null) {
                        count = count + 1;
                    }
                }

*//*
                // 미리 다뿌리니니까필요 없을것 같음
                // 일정에 대한 리스트 가져오기 db , 아이디와 날짜로 검색
                mTask_data_list = new MyAsyncTask().execute(intent.getExtras().getString("id"), "" + thisYear + thisMonth + (position-count+1));
*//*


            }
        });


        maintext = (TextView) findViewById(R.id.maintext);
        maintext.setText(year + "/" + month);
    }*/


    // 스택갱신
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onClick(View v) {

        // 시작하기전에 한번 지우고 시작해야 중복안됨
        data_Adapter.clear();

        switch (v.getId()) {
            case R.id.prev:
               /* if (thisMonth > 1) {
                    thisMonth--;
                    setCalendarDate(thisYear, thisMonth);
                } else {
                    thisYear--;
                    thisMonth = 12;
                    setCalendarDate(thisYear, thisMonth);
                }*/
                if (thisMonth > 1) {
                    thisMonth--;
                    setCalendarDate(thisMonth);
                } else {
                    thisMonth = 0;
                    setCalendarDate(thisMonth);
                }
                // 달이동시 해당 월 전체 내역서 리스트 뿌림
                mTask_Total_data_list = new MyAsyncTask3().execute();

                break;
            case R.id.next:
                /*if (thisMonth < 12) {
                    thisMonth++;
                    setCalendarDate(thisYear, thisMonth);
                } else {
                    thisYear++;
                    thisMonth = 1;
                    setCalendarDate(thisYear, thisMonth);
                }*/
                if (thisMonth < 12) {
                    thisMonth++;
                    setCalendarDate(thisMonth);
                } else {
                    thisMonth = 13;
                    setCalendarDate(thisMonth);
                }

                // 달이동시 해당 월 전체 내역서 리스트 뿌림
                mTask_Total_data_list = new MyAsyncTask3().execute();
                break;
        }
    }

/*    private class MyAsyncTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

            // 시작하기전에 한번 지우고 시작해야 중복안됨
            data_Adapter.clear();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // 리스트뷰로 뿌림
            // 리스트 뷰
            Log.d("value", values[0]);
            // 데이터 추가
            data_Adapter.add(values[0]);
            data_list.setAdapter(data_Adapter);
        }

        // @Override
        protected String doInBackground(String... params) {
            Log.d("params0", params[0]);
            Log.d("params1", params[1]);

            if (isCancelled())
                return (null); // don't forget to terminate this method
            Connection conn = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
                Statement stmt = conn.createStatement();
                ResultSet data = stmt.executeQuery("SELECT * FROM tb_data WHERE ID = '" + params[0] + "'" + "AND SPEND_DATE ='" + params[1] + "'");
                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    Log.d("tag", String.valueOf(data.getString("PLAY_TYPE")) + String.valueOf(data.getString("DETAIL_SPEND"))
                            + String.valueOf(data.getString("SPEND_MONEY")));

                    // 중간과정으로 거침
                    publishProgress(String.valueOf(data.getString("PLAY_TYPE")) + "/" + String.valueOf(data.getString("DETAIL_SPEND"))
                            + "/" + toNumFormat(Integer.parseInt(data.getString("SPEND_MONEY"))) + "원");
                    // 여기서 받을때마다 리스트 뿌림

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
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }*/


    // 천자리 콤마 찍어주는 매소드
    public static String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }


    private class MyAsyncTask2 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            person1 = values[0];
            person2 = values[1];
            input_month_income = toNumFormat(Integer.parseInt(values[2])) + "원";
            person2_input_month_income = toNumFormat(Integer.parseInt(values[3])) + "원";
            person2_id = values[4];
            // 총합
            month_total_income = "이번달 수입: " + toNumFormat(Integer.valueOf(values[2]) + Integer.valueOf(values[3])) + "원";

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
                ResultSet data = stmt.executeQuery("select a.NAME as[person1], b.NAME as[person2] , a.MONTH_MONEY as [person1_money] , b.MONTH_MONEY as[person2_money] , b.ID as [person2_ID]\n" +
                        "from tb_account \n" +
                        "as a \n" +
                        "  INNER JOIN tb_account AS B \n" +
                        "  ON a.NAME = b.PARTNER\n" +
                        "  WHERE a.ID = '" + intent.getExtras().getString("id") + "'");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("person1"), data.getString("person2"), data.getString("person1_money"), data.getString("person2_money"), data.getString("person2_ID"));
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
            Log.d("완료1", "완료");
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class MyAsyncTask3 extends AsyncTask<String, String, String> {
        private ModifyCustomDialog mModifyDialog;

        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("value[0]", values[0]);
            data_Adapter.add(values[0]);
            data_list.setAdapter(data_Adapter);
        }

        // @Override
        protected String doInBackground(String... params) {

            if (isCancelled())
                return (null); // don't forget to terminate this method
            Connection conn = null;

            // 오름차순 정렬
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
                Statement stmt = conn.createStatement();
                ResultSet data = stmt.executeQuery("\n" +
                        "select PLAY_TYPE, DETAIL_SPEND, SPEND_MONEY, SPEND_DATE FROM tb_data WHERE ID ='" + intent.getExtras().getString("id")
                        + "' AND SPEND_DATE LIKE '%" + String.valueOf(thisYear) + Redata(String.valueOf(thisMonth)) + "%'" + " ORDER BY SPEND_DATE ASC");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress("[" + Redata(data.getString("SPEND_DATE")) + "]" + "\n" + data.getString("PLAY_TYPE") + "/" + data.getString("DETAIL_SPEND") + "/"
                            + toNumFormat(Integer.parseInt(data.getString("SPEND_MONEY"))) + "원");
                }

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {

            // 스와이프 리스트뷰라 자체로 스와이프가 됨 그래서 craete 할때 아예 만들어 줘야함
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem modifyItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    modifyItem.setBackground(new ColorDrawable(Color.rgb(245, 170,
                            7)));
                    // set item width
                    modifyItem.setWidth(dp2px(70));
                    // set item icon
                    modifyItem.setIcon(R.drawable.modifyicon);
                    // set item title fontsize
                    modifyItem.setTitleSize(18);
                    // set item title font color
                    modifyItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(modifyItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(222,
                            0, 33)));
                    // set item width
                    deleteItem.setWidth(dp2px(70));
                    // set a icon
                    deleteItem.setIcon(R.drawable.trash);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };

            data_list.setMenuCreator(creator);

            // 스와이프 리스튜 뷰에서 아이템 클릭에 대한 이벤트 처리 swipe
            data_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    Log.d("index", String.valueOf(index));

                    switch (index) {
                        // 수정
                        case 0:
                            modify(data_Adapter.getItem(position), position);
                            //리스트 갱신
                            data_Adapter.notifyDataSetChanged();
                            break;
                        // 삭제
                        case 1:
                            // db에서 삭제하는 매소드
                            delete(data_Adapter.getItem(position));
                            data_Adapter.remove(data_Adapter.getItem(position));
                            data_Adapter.notifyDataSetChanged();
                            break;
                    }

                    return false;
                }
            });

    /*        // 리스트뷰 뿌리이후에 스와이프로 지우는매소드 , 이자체로 쓰면 그냥 스와이프 동시에 삭제됨
            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(data_list,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {
                                    Log.d("스와이프되냐?","스와이프되냐?");


                                    return true;
                                }

                                @Override
                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        data_Adapter.remove(data_Adapter.getItem(position));
                                    }
                                    data_Adapter.notifyDataSetChanged();
                                }
                            });
            data_list.setOnTouchListener(touchListener);
            data_list.setOnScrollListener(touchListener.makeScrollListener());*/
        }

        // 삭제 하는 매소드
        private void delete(String item) {
            Log.d("deleteitem", item);

            Log.d("item", item);

            //문자열 잘라서 대입
            int moneyDetailindex = item.indexOf("/", 14);
            int spendmoneyIndex = item.indexOf("원", moneyDetailindex);

            Log.d("dateitem", item.substring(1, 9));
            Log.d("playtypeitem", item.substring(11, 13));
            Log.d("spendDetail", item.substring(14, moneyDetailindex));
            Log.d("spendmoney", item.substring(moneyDetailindex + 1, spendmoneyIndex));

            //DB 접속해서 지울것
            mModify_delete = new MyAsyncTask7().execute(item.substring(1, 9), item.substring(11, 13), item.substring(14, moneyDetailindex), item.substring(moneyDetailindex + 1, spendmoneyIndex));
        }

        // 수정하는 매소드
        @SuppressLint("LongLogTag")
        private void modify(String item, int position) {
            Log.d("item", item);

            //문자열 잘라서 대입
            int moneyDetailindex = item.indexOf("/", 14);
            Log.d("moneyDetailindex", String.valueOf(moneyDetailindex));

            int spendmoneyIndex = item.indexOf("원", moneyDetailindex);
            Log.d("spendmoneyIndex", String.valueOf(spendmoneyIndex));


            Log.d("dateitem", item.substring(1, 9));
            Log.d("playtypeitem", item.substring(11, 13));
            Log.d("spendDetail", item.substring(14, moneyDetailindex));
            Log.d("spendmoney", item.substring(moneyDetailindex + 1));


            // 다이얼로그 창 띄우기
            // 이게 일단 원래 데이터이고 이걸 보내고, 다이얼로그에서 맨처음 세팅 이전데이터, 이후에 입력값 받아서 수정 누를때 바뀌는걸 다시 받아와서 변수로 만들고
            // 바뀔 데이터로 두고 비교
            // 바뀔때 텍스트 가져와서 db에서 수정을 함 원래 데이터는 전역변수, 현재 바뀐것 새로운 변수 인풋값 받음

            mModifyDialog = new ModifyCustomDialog(ContentClass.this
                    , "[일정 수정]"
                    , "" + item.substring(1, 9).substring(0, 4) + "년" + item.substring(1, 9).substring(4, 6) + "월" + item.substring(1, 9).substring(6, 8) + "일"
                    , item.substring(11, 13)
                    , item.substring(14, moneyDetailindex)
                    , item.substring(moneyDetailindex + 1, spendmoneyIndex)
                    , intent.getExtras().getString("id")
            );

            mModifyDialog.show();
        }

        // 스와이프 헀을때 박스사이즈
        private int dp2px(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    getResources().getDisplayMetrics());
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    // 날짜중에 자릿수 0 없는것 포함
    private String Redata(String spend_date) {
        Log.d("spend_date", String.valueOf(spend_date.length()));
        StringBuffer sb = new StringBuffer(spend_date);
        if (spend_date.length() == 7) {
            sb.insert(6, "0");
        }
        // 월이 한자리 날짜일때
        else if (spend_date.length() == 1) {
            sb.insert(0, "0");
        }
        return String.valueOf(sb);
    }

    private class MyAsyncTask4 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Log.d("value[0]",values[0]);
            play_type_eat.add(values[0]);
            play_month.add(values[1]);
            play_type_eat_month_toal.add(values[2]);

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
                ResultSet data = stmt.executeQuery("  SELECT LEFT(SPEND_DATE,6) AS MONTH, SUM(SPEND_MONEY) AS MONTH_TOTAL, count(*)as Play_count_eat " +
                        "FROM tb_data WHERE id='" + intent.getExtras().getString("id") + "' AND PLAY_TYPE='먹자' GROUP BY  \n" +
                        "LEFT(SPEND_DATE,6);");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("Play_count_eat"), data.getString("MONTH"), data.getString("MONTH_TOTAL"));
                }

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

    private class MyAsyncTask5 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Log.d("value[0222111]",values[0]);
            play_type_rest.add(values[0]);
            play_type_rest_month_toal.add(values[2]);
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
                ResultSet data = stmt.executeQuery("SELECT LEFT(SPEND_DATE,6) AS MONTH, SUM(SPEND_MONEY) AS MONTH_TOTAL, count(*)as Play_count_rest " +
                        "FROM tb_data WHERE id='" + intent.getExtras().getString("id") + "' AND PLAY_TYPE='쉬자' GROUP BY  \n" +
                        "   LEFT(SPEND_DATE,6);");
                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("Play_count_rest"), data.getString("MONTH"), data.getString("MONTH_TOTAL"));
                }

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

    private class MyAsyncTask6 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Log.d("value[0111111]",values[0]);
            play_type_play.add(values[0]);
            play_type_play_month_toal.add(values[2]);
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
                ResultSet data = stmt.executeQuery("SELECT LEFT(SPEND_DATE,6) AS MONTH, SUM(SPEND_MONEY) AS MONTH_TOTAL, count(*)as Play_count_play " +
                        "FROM tb_data WHERE id='" + intent.getExtras().getString("id") + "' AND PLAY_TYPE='놀자' GROUP BY  \n" +
                        "   LEFT(SPEND_DATE,6);");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("Play_count_play"), data.getString("MONTH"), data.getString("MONTH_TOTAL"));
                }

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

    private class MyAsyncTask7 extends AsyncTask<String, String, String> {

        // @Override
        protected String doInBackground(String... params) {

            if (isCancelled())
                return (null); // don't forget to terminate this method
            Connection conn = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://218.55.79.53/intern_edu", "sa", "mcncadmin");
                Statement stmt = conn.createStatement();
                ResultSet data = stmt.executeQuery("DELETE FROM tb_data  " +
                        "WHERE SPEND_DATE ='" + params[0] + "' AND ID ='" + intent.getExtras().getString("id") + "' AND PLAY_TYPE='" + params[1]
                        + "' AND DETAIL_SPEND='" + params[2] + "' AND SPEND_MONEY='" + remove_comma(params[3]) + "' ");

                conn.close();
            } catch (Exception e) {
                Log.d("Error connection", "" + e.getMessage());
            }

            return null;
        }


        //@Override
        protected void onPostExecute(String result) {
            Toast.makeText(ContentClass.this, "일정이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        //@Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    // 천자리콤마삭제
    private String remove_comma(String param) {
        return param.replaceAll("\\,", "");
    }


    private class MyAsyncTask8 extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            thismonth_eat_spendmoney = values[0];
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

                ResultSet data = stmt.executeQuery("\n" +
                        "   SELECT CASE WHEN MAX(A.SPEND_MONEY) IS NULL THEN 0 ELSE SUM(A.SPEND_MONEY) END AS MONTH_TOTAL \n" +
                        "   FROM tb_data A \n" +
                        "   WHERE id='"+intent.getExtras().getString("id")+"' AND PLAY_TYPE='먹자' AND SPEND_DATE LIKE '%"+thisYear + Redata(String.valueOf(thisMonth))+"%' ");


                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("MONTH_TOTAL"));
                }


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

    private class MyAsyncTask9 extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("value!!!!!!",values[0]);
            thismonth_play_spendmoney = values[0];
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
                ResultSet data = stmt.executeQuery("\n" +
                        "   SELECT CASE WHEN MAX(A.SPEND_MONEY) IS NULL THEN 0 ELSE SUM(A.SPEND_MONEY) END AS MONTH_TOTAL \n" +
                        "   FROM tb_data A \n" +
                        "   WHERE id='"+intent.getExtras().getString("id")+"' AND PLAY_TYPE='놀자' AND SPEND_DATE LIKE '%"+thisYear + Redata(String.valueOf(thisMonth))+"%' ");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("MONTH_TOTAL"));
                }

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

    private class MyAsyncTask10 extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            thismonth_rest_spendmoney = values[0];
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
                // case문으로 값있으면 총합 없으면 0대체
                ResultSet data = stmt.executeQuery("\n" +
                        "   SELECT CASE WHEN MAX(A.SPEND_MONEY) IS NULL THEN 0 ELSE SUM(A.SPEND_MONEY) END AS MONTH_TOTAL \n" +
                        "   FROM tb_data A \n" +
                        "   WHERE id='"+intent.getExtras().getString("id")+"' AND PLAY_TYPE='쉬자' AND SPEND_DATE LIKE '%"+thisYear + Redata(String.valueOf(thisMonth))+"%' ");

                while (data.next()) {
                    // 여기서 한번 더 컬럼명으로 가져옴
                    publishProgress(data.getString("MONTH_TOTAL"));
                }

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
}

// GridView와 연결해주기위한 어댑터 구성
class DateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CalData> arrData;
    private LayoutInflater inflater;
    private Context mContext;
    private CustomDialog mCustomDialog;

    Calendar bigyo_mCal;
    int bigyo_thisMonth;

    public DateAdapter(Context c, ArrayList<CalData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public DateAdapter(Context context) {
        mContext = context;

    }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        bigyo_mCal = Calendar.getInstance();
        bigyo_thisMonth = bigyo_mCal.get(Calendar.MONTH) + 1;

        //Log.d("ㅎ", String.valueOf(position));

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.viewitem, parent, false);
        }

        TextView ViewText = (TextView) convertView.findViewById(R.id.ViewText);
        //Log.d("position", String.valueOf(position));
        if (arrData.get(position) == null) {
            ViewText.setText("");
        } else {
            ViewText.setText(arrData.get(position).getDay() + "");
            if (arrData.get(position).getDayofweek() == 1) {
                ViewText.setTextColor(Color.RED);
            } else if (arrData.get(position).getDayofweek() == 7) {
                ViewText.setTextColor(Color.BLUE);
            }
            // 오늘날짜에 표시
            else if (arrData.get(position).getDay() == thisday && bigyo_thisMonth == thisMonth) {
                ViewText.setBackgroundColor(Color.parseColor("#FF6666"));
                ViewText.setTextColor(Color.WHITE);
            } else {
                ViewText.setTextColor(Color.BLACK);
            }
        }
        //클릭하면

        return convertView;

    }

    private class CustomDialog extends Dialog {

        String mTitle;
        String mContent;
        TextView mContentView;

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

            mContentView = (TextView) findViewById(R.id.txt_content);

            // 제목과 내용을 생성자에서 셋팅한다.
            // 여기서 다이얼로그 옵션 세팅
            setTitle(mTitle);

            // 백그라운드 투명도
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            mContentView.setText(mContent);
        }

        // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
        public CustomDialog(Context context,
                            String title,
                            String content) {
            super(context, android.R.style.Theme);
            this.mTitle = title;
            this.mContent = content;
        }
    }
}