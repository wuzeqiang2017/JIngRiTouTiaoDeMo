package test.bwie.com.wzq_0831jinritoutiao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import test.bwie.com.wzq_0831jinritoutiao.R;
import test.bwie.com.wzq_0831jinritoutiao.bean.Bean;

/**
 * 移动1507D  武泽强
 * 2017/9/4.
 * 作用：
 */

public class Myadapter extends BaseAdapter {
    private Context context;
    private List<Bean.DataBean> list;
    private final DisplayImageOptions options;

    public Myadapter(Context context, List<Bean.DataBean> list) {
        this.list = list;
        this.context =context;
        options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error)
                    .showImageOnLoading(R.mipmap.loading)
                    .build();

    }
    public void Loadmeoth(List<Bean.DataBean> datas,boolean flag){

        for (int i = datas.size()-1; i >-1 ; i--) {
            if(flag){
                list.add(0,datas.get(i));
            }else{
                list.add(datas.get(i));
            }
        }

        notifyDataSetChanged();

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
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getVideo_detail_info()==null){
            if (list.get(position).getImage_list() == null){
                return 0; //没有图片
            }else if (list.get(position).getImage_list().size() == 3){
                return 1;  //三张图片
            }else{
                return 2; //一张图片
            }
        }else{
            return 3; //视屏
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        int type = getItemViewType(position);
        if(convertView == null){
            viewHolder=new ViewHolder();
            switch (type){
                case  0:
                    convertView = View.inflate(context, R.layout.fragment_not,null);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.source = (TextView) convertView.findViewById(R.id.source);

                    break;
                case  1:
                    convertView = View.inflate(context, R.layout.fragment_three_img,null);
                    viewHolder.image1 = (ImageView) convertView.findViewById(R.id.image1);
                    viewHolder.image2 = (ImageView) convertView.findViewById(R.id.image2);
                    viewHolder.image3 = (ImageView) convertView.findViewById(R.id.image3);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.content);

                    break;
                case  2:
                    convertView = View.inflate(context, R.layout.fragment_item,null);
                    viewHolder.image1= (ImageView) convertView.findViewById(R.id.image1);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.content);

                    break;
                case 3:
                    convertView = View.inflate(context, R.layout.fragment_voideo,null);
                    viewHolder.image1= (ImageView) convertView.findViewById(R.id.image1);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.source = (TextView) convertView.findViewById(R.id.source);
                    break;
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        switch (type){
            case  0:
                viewHolder.name.setText(list.get(position).getTitle());
                viewHolder.source.setText(list.get(position).getLabel()+ list.get(position).getSource()+"评论："+list.get(position).getComment_count() );
                break;
            case  1:
                viewHolder.name.setText(list.get(position).getTitle());
                ImageLoader.getInstance().displayImage(list.get(position).getImage_list().get(0).getUrl(),viewHolder.image1,options);
                ImageLoader.getInstance().displayImage(list.get(position).getImage_list().get(1).getUrl(),viewHolder.image2,options);
                ImageLoader.getInstance().displayImage(list.get(position).getImage_list().get(2).getUrl(),viewHolder.image3,options);
                viewHolder.content.setText(list.get(position).getSource()+" 评论  " + list.get(position).getComment_count() );

                break;
            case 2:
                viewHolder.name.setText(list.get(position).getTitle());
                ImageLoader.getInstance().displayImage(list.get(position).getMiddle_image().getUrl(),viewHolder.image1,options);
                viewHolder.content.setText(list.get(position).getSource()+" 评论：" + list.get(position).getComment_count() );

                break;
            case 3:
                viewHolder.name.setText(list.get(position).getTitle());
                viewHolder.source.setText(list.get(position).getSource()+" 评论：" + list.get(position).getComment_count());
                ImageLoader.getInstance().displayImage(list.get(position).getVideo_detail_info().getDetail_video_large_image().getUrl(),viewHolder.image1,options);
                break;
        }
        return convertView;
    }
    class  ViewHolder{
        ImageView image1;
        TextView name;
        ImageView image2;
        ImageView image3;
        TextView source;
        TextView content;
    }
}
