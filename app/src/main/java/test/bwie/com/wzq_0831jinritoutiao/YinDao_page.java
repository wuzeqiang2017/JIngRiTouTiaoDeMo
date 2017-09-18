package test.bwie.com.wzq_0831jinritoutiao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动1507D  武泽强
 * 2017/8/31.
 * 作用：
 */

public class YinDao_page extends AppCompatActivity {
    private ViewPager vp;
    private List<View> list;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(YinDao_page.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    private List<RadioButton> rs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yindao_page);
        vp = (ViewPager) findViewById(R.id.vp);
        init();

    }
    //点击作跳转
    public void intoPlay(View view){
        Intent intent = new Intent(YinDao_page.this,MainActivity.class);
        startActivity(intent);
        finish();
    }



    private void init() {
        SharedPreferences sp = getSharedPreferences("conifg", MODE_PRIVATE);
        boolean flag = sp.getBoolean("flag", false);
        rs = new ArrayList<RadioButton>();
        RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
        RadioButton radio2 = (RadioButton) findViewById(R.id.radio2);
        RadioButton radio3 = (RadioButton) findViewById(R.id.radio3);
        rs.add(radio1);
        rs.add(radio2);
        rs.add(radio3);

        list = new ArrayList<View>();
        list.add(View.inflate(this,R.layout.yindao_page1,null));
        list.add(View.inflate(this,R.layout.yindao_page2,null));
        View vp3 = View.inflate(this, R.layout.yindao_page3, null);
        list.add(vp3);
        vp.setAdapter(new Mypager());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                SetRadioColor(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(flag){
            setContentView(R.layout.yindao_page2);
            new Thread(){
                @Override
                public void run() {
                    handler.sendEmptyMessageDelayed(0,3000);
                }
            }.start();
        }else{
            sp.edit().putBoolean("flag",true).commit();
        }

    }

    public void SetRadioColor(int index){
        for (int i=0;i<rs.size();i++){
            if(i==index){
                rs.get(i).setChecked(true);
            }else{
                rs.get(i).setChecked(false);
            }
        }
    }
    class Mypager extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }
}
