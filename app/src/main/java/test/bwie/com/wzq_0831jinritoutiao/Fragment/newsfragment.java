package test.bwie.com.wzq_0831jinritoutiao.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import me.maxwin.view.XListView;
import test.bwie.com.wzq_0831jinritoutiao.R;
import test.bwie.com.wzq_0831jinritoutiao.adapter.Myadapter;
import test.bwie.com.wzq_0831jinritoutiao.bean.Bean;
import test.bwie.com.wzq_0831jinritoutiao.utils.StramTools;
import test.bwie.com.wzq_0831jinritoutiao.view.WEBDataActivity;

/**
 * 移动1507D  武泽强
 * 2017/8/31.
 * 作用：
 */

public class newsfragment extends Fragment implements XListView.IXListViewListener {

    private View view;
    private String URI;
    private Myadapter myadapter;
    private XListView xlv;
    private  boolean flag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            URI = getArguments().getString("text");
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragment1,null);
        xlv = (XListView) view.findViewById(R.id.xlistview);
        xlv.setPullLoadEnable(true);
        xlv.setXListViewListener(this);

        getData();
        return view;
    }

    private void getData(){
        GetNextData(URI);
    }

    private void GetNextData(String urll){

        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s==null){
                    return;
                }
                Gson gson = new Gson();
                Bean bean = gson.fromJson(s, Bean.class);
                final List<Bean.DataBean> list = bean.getData();

                if(myadapter==null){
                    myadapter = new Myadapter(getActivity(), list);
                    xlv.setAdapter(myadapter);
                }else {
                    myadapter.Loadmeoth(list, flag);
                    xlv.setAdapter(myadapter);
                }
                        if(list!=null && list.size()>0){

                            xlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Bean.DataBean bean1 = (Bean.DataBean) myadapter.getItem(position-1 );
                                    Intent intent= new Intent(getActivity(), WEBDataActivity.class);
                                    intent.putExtra("bundle",bean1);
                                    startActivity(intent);
                                   getActivity().overridePendingTransition(R.anim.trans_in,R.anim.trans_out);
                                }
                            });

                        }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if(code ==200){
                        InputStream is = connection.getInputStream();
                        return StramTools.GetRead(is);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(urll);
    }


    @Override
    public void onRefresh() {
        flag= true;
        getData();
        xlv.stopRefresh();

    }

    @Override
    public void onLoadMore() {
        flag = false;
        getData();
        xlv.stopLoadMore();
    }
}
