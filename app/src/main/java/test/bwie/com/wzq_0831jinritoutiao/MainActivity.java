package test.bwie.com.wzq_0831jinritoutiao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import test.bwie.com.wzq_0831jinritoutiao.Fragment.newsfragment;
import test.bwie.com.wzq_0831jinritoutiao.bean.User;
import test.bwie.com.wzq_0831jinritoutiao.db.dao.SqliteDao;
import test.bwie.com.wzq_0831jinritoutiao.utils.CehuaTitle;
import test.bwie.com.wzq_0831jinritoutiao.utils.Myappliaction;
import test.bwie.com.wzq_0831jinritoutiao.view.ChannelActivity;

public class MainActivity extends AppCompatActivity {


    private TabLayout tab;
    private ViewPager vp;
    private List<Fragment> list;

    private ListView lv;
    private List<CehuaTitle> actionlist;
    private ImageView qq;
    private ImageView toggle;
    private TextView dengname;
    private ImageView phone;
    private ImageView sain;
    private TextView name;
    private ImageView title;
    private Button night;
    private ImageView pingdao;
    private SqliteDao dao;
    private List<String> titleArray;
    private List<String> url;
    private SharedPreferences sp;
    private RelativeLayout re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab = (TabLayout) findViewById(R.id.tab);
        vp = (ViewPager) findViewById(R.id.vp);
        pingdao = (ImageView) findViewById(R.id.pingdao);
        sp = getSharedPreferences("config2", MODE_PRIVATE);

        if(isOnline()){
            inset();
            initView();
        }else{
            showDialog();
        }



    }

    public boolean isOnline() {

        inset();
        initView();

        //得到一个连接管理者
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //得到联网信息
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //判断设备是否联网
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请链接网络");
        builder.setNegativeButton("否，", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inset();
                initView();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //跳转网络设置界面
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

            }
        });
        builder.create().show();
    }


    private void initView() {
        initvp();  //Viewpager
        left(); //侧滑的界面
        RightView();    //右侧滑界面

    }
    //右侧滑界面
    private void RightView() {
        dengname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SlidingMenu slidingMenu = new SlidingMenu(MainActivity.this);
                slidingMenu.setMode(SlidingMenu.RIGHT);
                slidingMenu.setBehindOffset(100);
                slidingMenu.attachToActivity(MainActivity.this,SlidingMenu.SLIDING_CONTENT);
                slidingMenu.setMenu(R.layout.right_layout);
                //点击其他登陆
                dengname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slidingMenu.toggle();
                    }
                });
            }
        });
    }

    private void initvp() {
        list = new ArrayList<Fragment>();
        for (int i=0;i< titleArray.size();i++){
            newsfragment newsfragment = new newsfragment();
            Bundle bundle = new Bundle();
            bundle.putString("text",url.get(i));
            newsfragment.setArguments(bundle);
            list.add(newsfragment);
        }
        Mypager mypager = new Mypager(getSupportFragmentManager());
        vp.setOffscreenPageLimit(titleArray.size());
        vp.setAdapter(mypager);
        tab.setupWithViewPager(vp);
        //标签的格式
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
    //频道管理
        pingdao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                startActivity(intent);
            }
        });

    }

    //viewpager做的FragmentPagerAdapter适配
    class  Mypager extends FragmentPagerAdapter{

        public Mypager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return titleArray.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArray.get(position);
        }
    }

//做的Slidingmenu 侧滑界面
    private void left() {
        final SlidingMenu slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(100);
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.menu_page);
        toggle = (ImageView) findViewById(R.id.toogle);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();

            }
        });

        phone = (ImageView) findViewById(R.id.phone);
        sain = (ImageView) findViewById(R.id.sain);
        night = (Button) findViewById(R.id.night);

        re = (RelativeLayout) findViewById(R.id.re);

        title = (ImageView) findViewById(R.id.title);
        name = (TextView) findViewById(R.id.name);
        qq = (ImageView) findViewById(R.id.qq);
        final ImageView sain = (ImageView) findViewById(R.id.sain);
        dengname = (TextView) findViewById(R.id.dengname);

        //手机短xin
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
// 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone1 = (String) phoneMap.get("phone");
//                            re.setVisibility(View.VISIBLE);
                            name.setText(phone1);
//                            qq.setVisibility(View.INVISIBLE);
//                            sain.setVisibility(View.INVISIBLE);
//                            phone.setVisibility(View.INVISIBLE);
//                            dengname.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                registerPage.show(MainActivity.this);
                //手机号码


            }
        });


        lv = (ListView) findViewById(R.id.lv);
        actionlist = new ArrayList<CehuaTitle>();
        actionlist.add(new CehuaTitle(R.drawable.dynamicicon_leftdrawer,"好友动态"));
        actionlist.add(new CehuaTitle(R.drawable.topicicon_profile,"我的话题"));
        actionlist.add(new CehuaTitle(R.drawable.ic_action_favor_on_normal,"收藏"));
        actionlist.add(new CehuaTitle(R.drawable.activityicon_leftdrawer,"活动"));
        actionlist.add(new CehuaTitle(R.drawable.sellicon_leftdrawer,"商城"));
        actionlist.add(new CehuaTitle(R.drawable.profile_pgc_vyellow,"反馈"));
        actionlist.add(new CehuaTitle(R.drawable.noticeable_approve_kaixin,"我要爆料"));

        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return actionlist.size();
            }

            @Override
            public Object getItem(int position) {
                return actionlist.get(position);

            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder =null;
                if(convertView==null){
                    convertView = View.inflate(MainActivity.this,R.layout.cehua_list_pager,null);
                    viewHolder = new ViewHolder();
                    viewHolder.img= (ImageView) convertView.findViewById(R.id.img);
                    viewHolder.name= (TextView) convertView.findViewById(R.id.name);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.img.setImageResource(actionlist.get(position).getImage());
                viewHolder.name.setText(actionlist.get(position).getName());
                return convertView;
            }
            class  ViewHolder{
                ImageView img;
                TextView name;
            }
        });
/**
 * 夜间，模式
 */
        final int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
            }
        });

    }

    /**
     * QQ等三方登陆
     */
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

    public void QQdenglu(View view){
        mTencent = Tencent.createInstance(APP_ID,MainActivity.this.getApplicationContext());
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(MainActivity.this,"all", mIUiListener);
    }
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG,"登录成功"+response.toString());
                        Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                        //授权为空的时候返回为空
                        if(response == null){
                            return;
                        }

                        try {
                            //返回  JSONObje  的类型 数据
                            JSONObject jo= (JSONObject) response;
                            //得到QQ的 数据  名字   头像   城市
                            String nickname = jo.getString("nickname");
                            String img = jo.getString("figureurl");
                            String city = jo.getString("city");

                            ImageLoader.getInstance().displayImage(img,title, Myappliaction.getOptions());
                            ImageLoader.getInstance().displayImage(img,toggle,Myappliaction.getOptions());
                            name.setText(nickname);
                            //把手机 和  新浪  隐藏
                            phone.setVisibility(View.INVISIBLE);
                            sain.setVisibility(View.INVISIBLE);
                            qq.setVisibility(View.INVISIBLE);
                            dengname.setVisibility(View.INVISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }


    }
    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void inset(){

        boolean flag = sp.getBoolean("flag", false);
        dao = new SqliteDao(this);
        if(flag){
            //添加数据库的数据  展示在页面
            titleArray =new  ArrayList<String>();
            url = new ArrayList<String>();
            List<User> list2 = dao.Select1();
            //遍历 User 类拿到数据
            for (User u :list2){
                //Tablayout 从数据库中拿到 title的 数据
                titleArray.add(u.getTitle());
                //接口地址 从数据库中拿到 context 的数据
                url.add(u.getContext());
            }

        }else{
            titleArray = Arrays.asList("推荐","热点","北京","视频","社会","娱乐","科技","汽车","体育","财经","军事",

                    "国际","段子","趣图","美女","健康");
            url = Arrays.asList(
                    "http://ic.snssdk.com/2/article/v25/stream/?count=20&min_behot_time=1455521444&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_hot&count=20&min_behot_time=1455521166&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_nme=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_local&count=20&min_behot_time=1455521226&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&user_city=北京&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=video&count=20&min_behot_time=1455521349&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522107&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_society&count=20&min_behot_time=1455521720&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522107&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_entertainment&count=20&min_behot_time=1455522338&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_tech&count=20&min_behot_time=1455522427&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_car&count=20&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_sports&count=20&min_behot_time=1455522629&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_finance&count=20&min_behot_time=1455522899&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_military&count=20&min_behot_time=1455522991&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_world&count=20&min_behot_time=1455523059&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=essay_joke&count=20&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=image_ppmm&count=20&min_behot_time=1455524172&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455524092&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
                    ,"http://ic.snssdk.com/2/article/v25/stream/?category=news_health&count=20&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455524092&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000",
                    "http://ic.snssdk.com/2/article/v25/stream/?category=news_world&count=20&min_behot_time=1455523059&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
            for (int i = 0; i< titleArray.size(); i++) {
                dao.add1(titleArray.get(i), url.get(i),"1");
                dao.addcache(titleArray.get(i),url.get(i));
            }
            sp.edit().putBoolean("flag",true).commit();
        }

    }
}



