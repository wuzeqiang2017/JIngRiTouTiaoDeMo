package test.bwie.com.wzq_0831jinritoutiao.utils;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.io.File;

/**
 * 移动1507D  武泽强
 * 2017/9/1.
 * 作用：
 */

public class Myappliaction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this, "20d0e967eb6aa", "2ff95b8641e00947fe7371c436357699");


        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i("1", "onSuccess:------------------ "+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("1", "onFailure:------------- "+s+"================"+s1);
            }
        });

        String path = Environment.getExternalStorageDirectory() + "1507D";
        File cache= new File(path);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480,800)
                .threadPoolSize(3)
                .threadPriority(100)
                .diskCache(new UnlimitedDiskCache(cache))
                .diskCacheSize(50*11024*1024)
                .memoryCacheSize(2*1024*1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();

        ImageLoader.getInstance().init(config);
    }
    public static DisplayImageOptions getOptions(){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
        return options;
    }
}


