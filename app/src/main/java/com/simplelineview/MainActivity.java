package com.simplelineview;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplelineview.bean.HealthDataInfo;
import com.simplelineview.utils.DensityUtil;
import com.simplelineview.utils.JsonUtil;
import com.simplelineview.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    ImageView im_fanhui;
    TextView tv_Overall_title;

    TextView tv_title__1;
    TextView tv_title__2;
    TextView tv_title__3;

    ObservableScrollView scrollView__1;
    ObservableScrollView scrollView__2;
    ObservableScrollView scrollView__3;

    SimpleLineView line_view__1;
    SimpleLineView line_view__2;
    SimpleLineView line_view__3;

    View ll_week__1;
    View ll_week__2;
    View ll_week__3;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        initListener();

        initData();
    }


    private void initData() {

        //模拟Json数据

        String json = "{\n" +
                "    \"body\": [\n" +
                "        {\n" +
                "            \"bid\": 61,\n" +
                "            \"datalist\": [\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-10\",\n" +
                "                    \"value\": \"5\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-11\",\n" +
                "                    \"value\": \"8\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-12\",\n" +
                "                    \"value\": \"\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-13\",\n" +
                "                    \"value\": \"18\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-14\",\n" +
                "                    \"value\": \"28\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-15\",\n" +
                "                    \"value\": \"38\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"3\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"linetype\": 0,\n" +
                "            \"title\": \"每次啪啪多久/分钟\",\n" +
                "            \"type\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"bid\": 60,\n" +
                "            \"datalist\": [\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-10\",\n" +
                "                    \"value\": \"40\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-11\",\n" +
                "                    \"value\": \"50\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-12\",\n" +
                "                    \"value\": \"20\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-13\",\n" +
                "                    \"value\": \"60\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-14\",\n" +
                "                    \"value\": \"70\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-15\",\n" +
                "                    \"value\": \"90\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"30\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"linetype\": 0,\n" +
                "            \"title\": \"每次叫喊的声音/分贝\",\n" +
                "            \"type\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"bid\": 62,\n" +
                "            \"datalist\": [\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-10\",\n" +
                "                    \"value\": \"27.1\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-11\",\n" +
                "                    \"value\": \"39.1\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-12\",\n" +
                "                    \"value\": \"55.5\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-13\",\n" +
                "                    \"value\": \"44.8\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-14\",\n" +
                "                    \"value\": \"99.8\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-15\",\n" +
                "                    \"value\": \"84\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"66.74\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"38\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"35.5\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"33.8\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"25.4\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"0\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"2017-07-16\",\n" +
                "                    \"value\": \"1\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"linetype\": 1,\n" +
                "            \"title\": \"最近这些天的性褔指数\",\n" +
                "            \"type\": 1\n" +
                "        }\n" +
                "    ],\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"status\": 1\n" +
                "}";

        LogUtil.e("json", json);

        HealthDataInfo info = JsonUtil.parseJsonToBean(json, HealthDataInfo.class);

        if (info == null) {
            LogUtil.e("折线图info", "JSON解析失败");
        } else if (info.body != null && info.body.size() > 0) {
            for (int i = 0; i < info.body.size(); i++) {
                int index = -1;
                List<HealthDataInfo.DatalistBean> datalist = info.body.get(i).datalist;
                for (int j = 0; j < datalist.size(); j++) {
                    if (!TextUtils.isEmpty(datalist.get(j).value)) {
                        index = j;
                    }
                }
                LogUtil.e("放进去", index + "");
                info.body.get(i).pointNum = index;
            }

            final HealthDataInfo.BodyBean bodyBean1 = info.body.get(0);
            final HealthDataInfo.BodyBean bodyBean2 = info.body.get(1);
            final HealthDataInfo.BodyBean bodyBean3 = info.body.get(2);

            initLineData1(bodyBean1);
            initLineData2(bodyBean2);
            initLineData3(bodyBean3);

        } else {
            //ToastUtil.show(this,info.msg);
        }

    }


    private void initListener() {
        im_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {

        tv_Overall_title = $(R.id.tv_Overall_title);
        im_fanhui = $(R.id.im_fanhui);
        tv_Overall_title.setText("简单点");

        tv_title__1 = $(R.id.tv_title__1);
        tv_title__2 = $(R.id.tv_title__2);
        tv_title__3 = $(R.id.tv_title__3);

        scrollView__1 = $(R.id.scrollView__1);
        scrollView__2 = $(R.id.scrollView__2);
        scrollView__3 = $(R.id.scrollView__3);

        line_view__1 = $(R.id.line_view__1);
        line_view__2 = $(R.id.line_view__2);
        line_view__3 = $(R.id.line_view__3);

        ll_week__1 = $(R.id.ll_week__1);
        ll_week__2 = $(R.id.ll_week__2);
        ll_week__3 = $(R.id.ll_week__3);

    }

    private void initLineData1(final HealthDataInfo.BodyBean bodyBean) {
        ArrayList<Float> datas = new ArrayList<>();

        ArrayList<String> strList = new ArrayList<String>();//放时间

        for (int i = 0; i < bodyBean.datalist.size(); i++) {
            if (TextUtils.isEmpty(bodyBean.datalist.get(i).value)) {
                datas.add(Float.MIN_VALUE);
                strList.add("");
            } else {
                datas.add(Float.valueOf(bodyBean.datalist.get(i).value));
                strList.add(bodyBean.datalist.get(i).time);
            }
        }

        ll_week__1.setVisibility(View.VISIBLE);
        line_view__1.setBottomTextList(strList);
        line_view__1.setDataList(datas);
        tv_title__1.setText(bodyBean.title);

        if (bodyBean.linetype == 0) { //曲线
            line_view__1.setLinestyle(SimpleLineView.Linestyle.Line);
        } else {
            line_view__1.setLinestyle(SimpleLineView.Linestyle.Curve);
        }

        line_view__1.setDataListening(new SimpleLineView.OnDataListening() {
            @Override
            public void getData(String time, Float data, final int index) {
                //   LogUtil.e("选中的是第几个==",index+"");
                bodyBean.pointNum = index;
            }
        });

        LogUtil.e("取出来", bodyBean.pointNum + "");
        line_view__1.drawPopup(bodyBean.pointNum);
    }


    private void initLineData2(final HealthDataInfo.BodyBean bodyBean) {
        ArrayList<Float> datas = new ArrayList<>();

        ArrayList<String> strList = new ArrayList<String>();//放时间

        for (int i = 0; i < bodyBean.datalist.size(); i++) {
            if (TextUtils.isEmpty(bodyBean.datalist.get(i).value)) {
                datas.add(Float.MIN_VALUE);
                strList.add("");
            } else {
                datas.add(Float.valueOf(bodyBean.datalist.get(i).value));
                strList.add(bodyBean.datalist.get(i).time);
            }
        }

        ll_week__2.setVisibility(View.VISIBLE);
        line_view__2.setBottomTextList(strList);
        line_view__2.setDataList(datas);
        tv_title__2.setText(bodyBean.title);

        if (bodyBean.linetype == 0) { //曲线
            line_view__2.setLinestyle(SimpleLineView.Linestyle.Line);
        } else {
            line_view__2.setLinestyle(SimpleLineView.Linestyle.Curve);
        }

        line_view__2.setDataListening(new SimpleLineView.OnDataListening() {
            @Override
            public void getData(String time, Float data, final int index) {
                //   LogUtil.e("选中的是第几个==",index+"");
                bodyBean.pointNum = index;
            }
        });

        LogUtil.e("取出来", bodyBean.pointNum + "");
        line_view__2.drawPopup(bodyBean.pointNum);
    }


    int distance = 8;
    int middlePointNum = 0;

    private void initLineData3(final HealthDataInfo.BodyBean bodyBean) {

        ArrayList<Float> datas = new ArrayList<>();

        ArrayList<String> strList = new ArrayList<String>();//放时间

        for (int i = 0; i < bodyBean.datalist.size(); i++) {
            if (TextUtils.isEmpty(bodyBean.datalist.get(i).value)) {
                datas.add(Float.MIN_VALUE);
                strList.add("");
            } else {
                datas.add(Float.valueOf(bodyBean.datalist.get(i).value));
                strList.add(bodyBean.datalist.get(i).time);
            }
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        int l = DensityUtil.getScreenWidth(this) / 2 - DensityUtil.dp2px(this, 30);//40
        int r = DensityUtil.getScreenWidth(this) / 2 - DensityUtil.dp2px(this, 30);//20
        params.setMargins(l, 0, r, 0);
        line_view__3.setLayoutParams(params);
        bodyBean.pointNum = 0;
        ll_week__3.setVisibility(View.INVISIBLE);



        //滑动监听
        scrollView__3.setOnScrollListener(new ObservableScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollX, int total) {


                int point_num = -8;//每屏有多少个点
                int shieldingWidth = DensityUtil.getScreenWidth(MainActivity.this);

                distance = line_view__3.getDistance();
                point_num = shieldingWidth / distance + 1;
                int leif = shieldingWidth / 2;
                double hua = (double) (scrollX - leif) / (double) distance;

                if (hua > 0) {
                    hua += 0.5;
                } else {
                    hua -= 0.5;
                }
                middlePointNum = (int) hua + point_num / 2; //当前滑出屏的是第几个 + 一屏个数的中间数 == 当前的中间点
            }

            @Override
            public void onScrollChanged(ObservableScrollView.ScrollType scrollType) {

                if (scrollType == ObservableScrollView.ScrollType.IDLE) {
                    //	Log.e("msg","滑动停止");

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //计算出当前一屏中最中间的那一个点,并显示出来

                            LogUtil.e("开始画点啦", middlePointNum + "");

                            if (middlePointNum >= line_view__3.getDrawDotList().size()) {
                                middlePointNum = line_view__3.getDrawDotList().size() - 1;
                            } else if (middlePointNum < 0) {
                                middlePointNum = 0;
                            }
                            LogUtil.e("真正画点啦", middlePointNum + "");
                            line_view__3.drawPopup(middlePointNum);
                        }
                    }, 50);

                } else if (scrollType == ObservableScrollView.ScrollType.TOUCH_SCROLL) {
                    //   Log.e("msg","手指滑动");
                } else if (scrollType == ObservableScrollView.ScrollType.FLING) {
                    //Log.e("msg","滑动中");
                } else {
                    // Log.e("msg","不知状态");
                }
            }
        });


        tv_title__3.setText(bodyBean.title);

        if (bodyBean.linetype == 0) { //曲线
            line_view__3.setLinestyle(SimpleLineView.Linestyle.Line);
        } else {
            line_view__3.setLinestyle(SimpleLineView.Linestyle.Curve);
        }

        line_view__3.setDataListening(new SimpleLineView.OnDataListening() {
            @Override
            public void getData(String time, Float data, final int index) {
                //   LogUtil.e("选中的是第几个==",index+"");
                bodyBean.pointNum = index;
            }
        });




        //数据监听
        line_view__3.setDataListening(new SimpleLineView.OnDataListening() {
            @Override
            public void getData(String time, Float data, final int index) {
                LogUtil.e("选中time", time + "");
                LogUtil.e("选中data", data + "");
                LogUtil.e("选中的是第几个==", index + "");
                mHandler.postDelayed((new Runnable() {
                    @Override
                    public void run() {
                        distance = line_view__3.getDistance();
                        LogUtil.e("前distance===", distance + "");
                        int x = distance * index;
                        LogUtil.e("后distance===", distance + "");
                        LogUtil.e("index===", index + "");
                        LogUtil.e("www===", scrollView__3.getChildAt(0).getMeasuredWidth() + "");
                        scrollView__3.smoothScrollTo(x, 0);
                    }
                }), 50);
            }
        });

        line_view__3.setBottomTextList(strList);
        line_view__3.setDataList(datas);
        line_view__3.drawPopup(bodyBean.pointNum);

    }

}
