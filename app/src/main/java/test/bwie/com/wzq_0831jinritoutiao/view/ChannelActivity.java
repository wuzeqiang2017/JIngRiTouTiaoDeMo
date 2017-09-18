package test.bwie.com.wzq_0831jinritoutiao.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import test.bwie.com.wzq_0831jinritoutiao.MainActivity;
import test.bwie.com.wzq_0831jinritoutiao.R;
import test.bwie.com.wzq_0831jinritoutiao.adapter.ChannelAdapter;
import test.bwie.com.wzq_0831jinritoutiao.bean.User;
import test.bwie.com.wzq_0831jinritoutiao.db.dao.SqliteDao;

public class ChannelActivity extends AppCompatActivity {

    private List<String> list1;
    private List<String> list2;
    private GridView grid1;
    private GridView grid2;
    private Button returm;
    private SQLiteDatabase db;
    private SqliteDao dao;
    private ChannelAdapter adapter1;
    private ChannelAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        dao = new SqliteDao(this);

        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();

        returm = (Button) findViewById(R.id.returm);
        grid1 = (GridView) findViewById(R.id.grid1);
        grid2 = (GridView) findViewById(R.id.grid2);

//        MySqliteOpenHelper mysqlite = new MySqliteOpenHelper(this);
//        db = mysqlite.getWritableDatabase();

        List<User> que1= dao.Select1();
        for (int i= 0; i<que1.size();i++){
            list1.add(que1.get(i).getTitle());
        }

        List<User> que2 = dao.Select2();
        for(int i= 0;i<que2.size();i++){
            list2.add(que2.get(i).getTitle());
        }

        adapter1 = new ChannelAdapter(list1,this);
        adapter2 = new ChannelAdapter(list2,this);

        grid1.setAdapter(adapter1);
        grid2.setAdapter(adapter2);
        System.out.println(list1);

        grid1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(list1.get(position));

                dao.up2(list1.get(position));
                String  item1= (String) adapter1.getItem(position);
                list1.remove(position);
                adapter1.notifyDataSetChanged();

                list2.add(item1);
                adapter2.notifyDataSetChanged();

            }
        });
        grid2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dao.up1(list2.get(position));
                String item2= (String) adapter2.getItem(position);
                list2.remove(position);
                adapter2.notifyDataSetChanged();

                list1.add(item2);
                adapter1.notifyDataSetChanged();
            }
        });
        returm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ChannelActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
