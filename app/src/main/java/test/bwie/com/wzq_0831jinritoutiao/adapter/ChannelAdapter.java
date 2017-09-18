package test.bwie.com.wzq_0831jinritoutiao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import test.bwie.com.wzq_0831jinritoutiao.R;

/**
 * 移动1507D  武泽强
 * 2017/9/6.
 * 作用：
 */

public class ChannelAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;

    public ChannelAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.channel_item,null);
        TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
        tv_item.setText(list.get(position));
        return view;
    }
}
