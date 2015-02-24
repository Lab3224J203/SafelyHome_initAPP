package com.demo.stust.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity{

    private static String TAG = "myTag";
    private static String str = null;
    private static String data = "";
    private static Double lat = 23.553118;
    private static Double lon = 121.0211024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{

        private JSONAsyncTask jsonAsyncTask;
        private static EditText et;
        public static Button btn;
        public static TextView tv;
        private static String strADR;
        private static String strAPIData;

        public PlaceholderFragment() {
        }

        public static String getStrADR() {
            return strADR;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            tv = (TextView) rootView.findViewById(R.id.textView);
            et = (EditText) rootView.findViewById(R.id.editText);
            btn = (Button) rootView.findViewById(R.id.button);
            btn.setOnClickListener(new myOnClickListener());
            tv.setText("請輸入縣市地址後按下Search");

//            // Get RecyclerView
//            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//            recyclerView.setHasFixedSize(true);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(new MyAdapter);
//            Log.i("Json", "OnCOver");

            return rootView;
        }


        private class myOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
//                jsonAsyncTask = new JSONAsyncTask();
//                jsonAsyncTask.execute("24.080456,120.551188");
                if (isConnected()) {
                    strADR = et.getText().toString();
                    JSONThread jsonTT = new JSONThread();
                    tv.setText("請稍候...");
//                    btn.setEnabled(false);
                }
                else
                    Toast.makeText(getActivity(), "尚未連線網際網路　\nNo network connection available.", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean isConnected(){
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }

        public static final List<String> data;
        static {
            data = new ArrayList<String>();
            data.add("浮夸 - 陈奕迅");
            data.add("好久不见 - 陈奕迅");
            data.add("时间都去哪儿了 - 王铮亮");
            data.add("董小姐 - 宋冬野");
            data.add("爱如潮水 - 张信哲");
            data.add("给我一首歌的时间 - 周杰伦");
            data.add("天黑黑 - 孙燕姿");
            data.add("可惜不是你 - 梁静茹");
            data.add("太委屈 - 陶晶莹");
            data.add("用心良苦 - 张宇");
            data.add("说谎 - 林宥嘉");
            data.add("独家记忆 - 陈小春");
        }

//        private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//            private List<String> data;
//
//            public MyAdapter(List<String> data) {
//                this.data = data;
//            }
//
//            @Override
//            public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//                // 加载Item的布局.布局中用到的真正的CardView.
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_robam_1, viewGroup, false);
//                // ViewHolder参数一定要是Item的Root节点.
//                return new ViewHolder(view);
//            }
//
//            @Override
//            public void onBindViewHolder(ViewHolder viewHolder, int i) {
//                viewHolder.text.setText(data.get(i));
//            }
//
//            @Override
//            public int getItemCount() {
//                return data.size();
//            }
//
//            static class ViewHolder extends RecyclerView.ViewHolder {
//                TextView text;
//
//                public ViewHolder(View itemView) {
//                    // super这个参数一定要注意,必须为Item的根节点.否则会出现莫名的FC.
//                    super(itemView);
//                    text = (TextView) itemView.findViewById(R.id.text);
//                }
//            }
//        }
    }
}
