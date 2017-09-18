package test.bwie.com.wzq_0831jinritoutiao.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import test.bwie.com.wzq_0831jinritoutiao.R;
import test.bwie.com.wzq_0831jinritoutiao.bean.Bean;

/**
 * 移动1507D  武泽强
 * 2017/9/6.
 * 作用：
 */

public class WEBDataActivity extends Activity implements View.OnClickListener{
    private Tencent mTencent;
    private String APP_ID = "222222";
    private String SCOPE = "all";
    private ImageView kongjian;
    private ImageView shoucang;
    private ImageView haoyou;
    private String uri;
    private String title;
    private String img;
    private String content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_url);
        kongjian = (ImageView) findViewById(R.id.kongjian);
        shoucang = (ImageView) findViewById(R.id.shoucang);
        haoyou = (ImageView) findViewById(R.id.haoyou);

        kongjian.setOnClickListener(this);
        shoucang.setOnClickListener(this);
        haoyou.setOnClickListener(this);

        WebView web= (WebView) findViewById(R.id.web);
        Bean.DataBean bean = (Bean.DataBean) getIntent().getSerializableExtra("bundle");
        uri = bean.getUrl();
        title = bean.getTitle();

        WebSettings webSettings = web.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        web.loadUrl(uri);
        //设置Web视图
        web.setWebViewClient(new webViewClient ());


        mTencent = Tencent.createInstance(APP_ID,WEBDataActivity.this.getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kongjian:     //空间分享
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WEBDataActivity.this,"点击了空间分享",Toast.LENGTH_SHORT).show();
                        Bundle params = new Bundle();
                        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,"你喜欢的新闻");
                        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,uri);
                        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://p4.so.qhimgs1.com/bdr/_240_/t01c5f811d82db45e8a.jpg");
                        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "CSDN");
                        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                        mTencent.shareToQQ(WEBDataActivity.this, params, new sharelistener());
                    }
                });
                break;
            case R.id.shoucang:     //收藏
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WEBDataActivity.this,"点击了收藏",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.haoyou:   //好友分享
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WEBDataActivity.this,"点击了分享",Toast.LENGTH_SHORT).show();
                        Bundle params = new Bundle();
                        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,"你喜欢的新闻");
                        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,uri);
                        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://p4.so.qhimgs1.com/bdr/_240_/t01c5f811d82db45e8a.jpg");
                        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "CSDN");
//                        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                        mTencent.shareToQQ(WEBDataActivity.this, params, new sharelistener());
                    }
                });
                break;
        }
    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

//分享的类实现 Iuilements
    class  sharelistener implements IUiListener{
        @Override
        public void onComplete(Object o) {
            //分享成功后回调
            Toast.makeText(WEBDataActivity.this, "分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
            //分享失败后回调
        }

        @Override
        public void onCancel() {
            //取消分享后回调
        }
    };
}
