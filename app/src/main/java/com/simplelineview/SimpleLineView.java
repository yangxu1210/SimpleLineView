package com.simplelineview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.simplelineview.utils.DensityUtil;
import com.simplelineview.utils.LogUtil;

import java.util.ArrayList;



/**
 * Created by xjj on 2016/6/23.
 */
public class SimpleLineView extends View {
    private int mViewHeight;
    //drawBackground
    private boolean autoSetDataOfGird = true;
    private boolean autoSetGridWidth = true;
    private int dataOfAGird = 10;
    private int bottomTextHeight = 0;
    private ArrayList<String> bottomTextList = new ArrayList<>();
    private ArrayList<Float> dataList = new ArrayList<>();
    private ArrayList<Integer> xCoordinateList = new ArrayList<Integer>();
    //  private ArrayList<Float> yCoordinateList = new ArrayList<Float>();
    private ArrayList<Dot> drawDotList = new ArrayList<Dot>();;
    private Paint bottomTextPaint = new Paint();
    private Paint ycoordTextPaint = new Paint();
    private int bottomTextDescent;

    //popup
    private Paint popupTextPaint = new Paint();
    private final int bottomTriangleHeight = 12;//底三角形的高度
    //private Dot selectedDot;
    private boolean mShowYCoordinate = true;

    private int topLineLength = DensityUtil.dip2px(getContext(), 12); //顶线长度

    private int sideLineLength = DensityUtil.dip2px(getContext(),45)/3*2;// 右边间距和左边间距

 //    private int sideLineLength =0;// 右边侧线长度

    public int backgroundGridWidth = DensityUtil.dip2px(getContext(),55);//背景网格宽度,也是X轴宽度

    //Constants
    private final int popupTopPadding = DensityUtil.dip2px(getContext(),1); //popup顶部距离
    private final int popupBottomMargin = DensityUtil.dip2px(getContext(),5); // popup底部距离
    private final int bottomTextTopMargin = DensityUtil.sp2px(getContext(),1);//底部文本顶边
    private final int bottomLineLength = DensityUtil.sp2px(getContext(), 0);  //底线的长度
    private final  int DOT_INNER_CIR_RADIUS =   4; //小点和线大小
    private final int DOT_OUTER_CIR_RADIUS = DensityUtil.dip2px(getContext(),5); //点大小
    private final int MIN_TOP_LINE_LENGTH = DensityUtil.dip2px(getContext(),12);//背景线密度
    private final static int MIN_VERTICAL_GRID_NUM = 4; //纵向有几个
    private final static int MIN_HORIZONTAL_GRID_NUM = 1;
    private final static int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");  //_行_背景颜色
    private final static int BOTTOM_TEXT_COLOR = Color.parseColor("#9B9A9B");  //X轴低
    private final int YCOORD_TEXT_LEFT_MARGIN = 0;//DensityUtil.dip2px(getContext(), 10);

    private boolean  showPopup = false;
    private  Dot selectedDot = null;

    public Canvas canvas;

    OnDataListening dataListening;

    int index;//当前选中的是第几个点

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;


    /**
     * 设置滚动接口
     * @param dataListening
     */
    public void setDataListening(OnDataListening dataListening){
        this.dataListening = dataListening;
    }


    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for(Dot dot : drawDotList){
                dot.update();
                if(!dot.isAtRest()){
                    needNewFrame = true;
                }
            }
            if (needNewFrame) {
                postDelayed(this, 0);
            }
            invalidate();
        }
    };

    public SimpleLineView(Context context){
        this(context,null);
    }

    public SimpleLineView(Context context, AttributeSet attrs){
        super(context, attrs);
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(DensityUtil.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(DensityUtil.sp2px(getContext(),12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);

        ycoordTextPaint.setAntiAlias(true);
        ycoordTextPaint.setTextSize(DensityUtil.sp2px(getContext(),12));
        ycoordTextPaint.setTextAlign(Paint.Align.LEFT);
        ycoordTextPaint.setStyle(Paint.Style.FILL);
        ycoordTextPaint.setColor(BOTTOM_TEXT_COLOR);


    }

    /**
     * dataList will be reset when called is method.
     * @param bottomTextList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomTextList){
        this.dataList = null;
        this.bottomTextList = bottomTextList;

        Rect r = new Rect();
        int longestWidth = 0;
        String longestStr = "";
        bottomTextDescent = 0;
        for(String s:bottomTextList){

            if(!TextUtils.isEmpty(s)){
                bottomTextPaint.getTextBounds(s,0,s.length(),r);
                if(bottomTextHeight<r.height()){
                    bottomTextHeight = r.height();
                }
                if(autoSetGridWidth&&(longestWidth<r.width())){
                    longestWidth = r.width();
                    longestStr = s;
                }
                if(bottomTextDescent<(Math.abs(r.bottom))){
                    bottomTextDescent = Math.abs(r.bottom);
                }
            }
        }

        if(autoSetGridWidth){
            if(backgroundGridWidth<longestWidth){
              //  backgroundGridWidth = longestWidth+(int)bottomTextPaint.measureText(longestStr,0,1); //计算X线的

                backgroundGridWidth = DensityUtil.getScreenWidth(getContext())/7;//屏幕的七分之一
            }

            if(sideLineLength<longestWidth/2){
                sideLineLength = longestWidth/2;
            }
        }

        refreshXCoordinateList(getHorizontalGridNum());
    }

    /**
     *
     * @param dataList The Integer ArrayList for showing,
     *                 dataList.size() must < bottomTextList.size()
     */
    public void setDataList(ArrayList<Float> dataList){
        this.dataList = dataList;
        if(dataList.size() > bottomTextList.size()){
            throw new RuntimeException("dacer.LineView error:" +
                    " dataList.size() > bottomTextList.size() !!!");
        }
        if(autoSetDataOfGird){
            float biggestData = 0;
            for(Float i:dataList){
                if(biggestData<i){
                    biggestData = i;
                }
            }
            dataOfAGird = 1;
            while(biggestData/10 > dataOfAGird){
                dataOfAGird *= 10;
            }
        }


        refreshAfterDataChanged();

        setMinimumWidth(0); // It can help the LineView reset the Width,它可以帮助LineView重置宽度,
        // I don't know the better way..我不知道更好的方法. .
        postInvalidate();
    }

    public void setShowYCoordinate(boolean showYCoordinate) { //显示Y坐标
        mShowYCoordinate = showYCoordinate;
    }

    private void refreshAfterDataChanged(){
        float verticalGridNum = getVerticalGridlNum();
        refreshTopLineLength(verticalGridNum);
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
    }

    private float getVerticalGridlNum(){
        float verticalGridNum = MIN_VERTICAL_GRID_NUM;
        if(dataList != null && !dataList.isEmpty()){
            for(Float integer:dataList){
                if(verticalGridNum<(integer+1)){
                    verticalGridNum = integer+1;
                }
            }
        }
        return verticalGridNum;
    }

    private int getHorizontalGridNum(){
        int horizontalGridNum = bottomTextList.size()-1;
        if(horizontalGridNum<MIN_HORIZONTAL_GRID_NUM){
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }

    private void refreshXCoordinateList(int horizontalGridNum){
        xCoordinateList.clear();
        for(int i=0;i<(horizontalGridNum+1);i++){
            xCoordinateList.add(sideLineLength + backgroundGridWidth*i);
        }

    }

    private void refreshYCoordinateList(float verticalGridNum){
//        yCoordinateList.clear();
//        for(int i=0;i<(verticalGridNum+1);i++){
//            LogUtil.e("8888添加值",i+"");
//            float y =  topLineLength + ((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin- bottomLineLength-bottomTextDescent)*i/(verticalGridNum));
//            LogUtil.e("Y8888的全部值",y+"");
//            yCoordinateList.add(y);
//        }
    }

    private void refreshDrawDotList(float verticalGridNum){
        if(dataList != null && !dataList.isEmpty()){
            int drawDotSize = drawDotList.isEmpty()? 0:drawDotList.size();
            for(int i=0;i<dataList.size();i++){
                float x = xCoordinateList.get(i);

                //计算Y的值(方法1)
                int t2 = DensityUtil.dip2px(getContext(),78);//120 60  点的范围高度
                int t88 = DensityUtil.dip2px(getContext(),40);//45 29  距离底部

                float t1 = dataList.get(i);
                int y =  (int)t1*t2/(int)verticalGridNum;
                y = t2-y+t88;

             //   if(dataList.size()==1){ //如果只有一个数据时居中
            //        y =  DensityUtil.dip2px(App.getContext(),50);//90  180
           //     }

                if(i>drawDotSize-1){
                    drawDotList.add(new Dot(x, 0, x, y, dataList.get(i)));
                }else{
                    drawDotList.set(i, drawDotList.get(i).setTargetData(x,y,dataList.get(i)));
                }
            }
            int temp = drawDotList.size() - dataList.size();
            for(int i=0; i<temp; i++){
                drawDotList.remove(drawDotList.size()-1);
            }
        }
        removeCallbacks(animator);
        post(animator);
    }

    private void refreshTopLineLength(float verticalGridNum){
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        // But this code not so good.
        if((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin)/
                (verticalGridNum+2)<getPopupHeight()){
            topLineLength = getPopupHeight()+DOT_OUTER_CIR_RADIUS+DOT_INNER_CIR_RADIUS+2;
        }else{
            topLineLength = MIN_TOP_LINE_LENGTH;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;

        // drawBackgroundLines(canvas);

        if (mStyle == Linestyle.Curve)
        {
            drawScrollLine(canvas);
        }
        else
        {
            drawLines(canvas);
        }

        for(Dot dot : drawDotList){
            dot.select = false;
            //	drawPopup(canvas, String.valueOf(dot.data), dot.getPoint());
        }

        if(showPopup && selectedDot != null){ ////画选中的点
            selectedDot.select = true;

            int i = (int) selectedDot.data;
            float i2 = selectedDot.data-i;

         //   LogUtil.e("i2===",i2+"");
            String value = "";
            if(i2>0){
                value= String.valueOf(selectedDot.data);
            }else{
                value = String.valueOf(i);
            }

        //    LogUtil.e("线上的数据:",value);
            drawPopup(canvas,value, selectedDot.getPoint());

//            if(value.contains(".")){
//                String[] values = value.split(".");
//                LogUtil.e("小数点后面的数是",values[1]);
//                int num = Integer.valueOf(values[1]);
//                if(num>0){
//                    drawPopup(canvas,value, selectedDot.getPoint());
//                }else{
//                    drawPopup(canvas, values[0], selectedDot.getPoint());
//                }
//            }else{
//                drawPopup(canvas,value, selectedDot.getPoint());
//            }
        }
        //画全部点
        drawDots(canvas);

        if(dataListening!=null){
            LogUtil.e("回调index",index+"");
            dataListening.getData(bottomTextList.get(index),dataList.get(index),index);
        }else{
            LogUtil.e("dataListening====","null");
        }

        // for(Dot dot : drawDotList){  //画全部点
        //	drawPopup(canvas, String.valueOf(dot.data), dot.getPoint());
        // }
    }

    /**
     *
     * @param canvas  The canvas you need to draw on.
     * @param point   The Point consists of the x y coordinates from left bottom to right top.
     *                Like is              3
     *                2
     *                1
     *                0 1 2 3 4 5
     */
    private void drawPopup(Canvas canvas, String num, Point point){

        if(Float.MIN_VALUE==Float.valueOf(num)){
          //  LogUtil.e("不显标签",num);
            return;
        }

        boolean singularNum = (num.length() == 1);
        int sidePadding = DensityUtil.dip2px(getContext(),singularNum? 8:6);
        int sidePadding2 = DensityUtil.dip2px(getContext(),5);
        int x = point.x;
        if(mShowYCoordinate == true)
            x += YCOORD_TEXT_LEFT_MARGIN;

        int y = point.y-DensityUtil.dip2px(getContext(),5);
    //    LogUtil.e("point.y===",point.y+"");
     //   LogUtil.e("point.x===",point.x+"");

        //.e("x===",x+"");

        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(num,0,num.length(),popupTextRect);

        int left= x-popupTextRect.width()/2-sidePadding;
        int right =  x + popupTextRect.width()/2+sidePadding;
        int top;
        int bottom ;

//        if(dataList.size()==1){
//            //top = (y-bottomTriangleHeight-popupBottomMargin)*2+ DensityUtil.dip2px(getContext(),5);
//            bottom = (y+popupTopPadding-popupBottomMargin) *2 + DensityUtil.dip2px(getContext(),10);
//            top = bottom - DensityUtil.dip2px(getContext(),16);
//        }else{
//            top = y - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin;
//            bottom = y+popupTopPadding-popupBottomMargin;
//        }

        top = y - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin-sidePadding2;
        bottom = y+popupTopPadding-popupBottomMargin+sidePadding2;

//        LogUtil.e("矩形的位置l",left+"");
//        LogUtil.e("矩形的位置t",top+"");
//        LogUtil.e("矩形的位置r",right+"");
//        LogUtil.e("矩形的位置b",bottom+"");

        //矩形的位置  画图片
        Rect r = new Rect(left,top-sidePadding2, right, bottom-sidePadding2);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.shujubeijing);
        byte chunk[] = bmp.getNinePatchChunk();
        NinePatchDrawable popup = new NinePatchDrawable(bmp, chunk, new Rect(), null);
        popup.setBounds(r);
        popup.draw(canvas);

        //画文字
//        if(dataList.size()==1){
//            canvas.drawText(num, x, (y-bottomTriangleHeight-popupBottomMargin)*2+ DensityUtil.dip2px(getContext(),16), popupTextPaint);
//        }else{
//            canvas.drawText(num, x, y-bottomTriangleHeight-popupBottomMargin, popupTextPaint);
//        }

        canvas.drawText(num, x, y-bottomTriangleHeight-popupBottomMargin-sidePadding2, popupTextPaint);
    }

    private int getPopupHeight(){
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9",0,1,popupTextRect);
        Rect r = new Rect(-popupTextRect.width()/2,
                - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                + popupTextRect.width()/2,
                +popupTopPadding-popupBottomMargin);
        return r.height();
    }

    private void drawDots(Canvas canvas){
        Paint bigCirPaint = new Paint();
        bigCirPaint.setAntiAlias(true);

        bigCirPaint.setColor(Color.parseColor("#FFFFFF"));//白点
        Paint smallCirPaint = new Paint(bigCirPaint);
        smallCirPaint.setColor(Color.parseColor("#85c942")); //小点
        if(drawDotList!=null && !drawDotList.isEmpty()){

            for(int i = 0;i<drawDotList.size();i++){
                Dot dot  =  drawDotList.get(i);
                float x = dot.x;
                if(mShowYCoordinate == true) x += YCOORD_TEXT_LEFT_MARGIN;

                int dotSize=  DOT_INNER_CIR_RADIUS;
                if(dot.select){ //选择的点
                    dotSize =DOT_OUTER_CIR_RADIUS;
                }

                if(dataList.get(i)!=Float.MIN_VALUE){
                    canvas.drawCircle(x,dot.y,DOT_OUTER_CIR_RADIUS,bigCirPaint);
                    canvas.drawCircle(x,dot.y,dotSize,smallCirPaint);
                }

//                if(dataList.get(i)!=Float.MIN_VALUE){
//                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.data_dian);
//                    canvas.drawBitmap(bmp,x-bmp.getWidth()/2,dot.y-bmp.getHeight()/2,bigCirPaint);
//                }
            }
        }
    }


    //画折线
    private void drawLines(Canvas canvas){
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#ababab"));
        linePaint.setStrokeWidth(DOT_INNER_CIR_RADIUS);


        for(int i=0; i<drawDotList.size()-1; i++){
            float x1 = drawDotList.get(i).x;
            float x2 = drawDotList.get(i+1).x;
            if(mShowYCoordinate == true) {
                x1 += YCOORD_TEXT_LEFT_MARGIN;
                x2 += YCOORD_TEXT_LEFT_MARGIN;
            }
            if(dataList.get(i)!=Float.MIN_VALUE&&dataList.get(i+1)!=Float.MIN_VALUE){
                canvas.drawLine(x1, drawDotList.get(i).y, x2, drawDotList.get(i+1).y, linePaint);
            }
        }
    }

    //画曲线
    private void drawScrollLine(Canvas canvas){
        mPoints = getPoints();
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ababab"));
        mPaint.setStrokeWidth(DOT_INNER_CIR_RADIUS);
        mPaint.setStyle(Paint.Style.STROKE);

        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
          //  Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
           // p4.y = endp.y;
           // p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
           // path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y); // (x1,y1) 为控制点，(x2,y2)为控制点，(x3,y3) 为结束点。

            path.cubicTo(p3.x, p3.y, p3.x, endp.y, endp.x, endp.y); // (x1,y1) 为控制点，(x2,y2)为控制点，(x3,y3) 为结束点。
            canvas.drawPath(path, mPaint);
        }
    }

    private Point[] getPoints()
    {
        Point[] points = new Point[dataList.size()];
        for (int i = 0; i < dataList.size(); i++)
        {
            //int ph = bheight - (int) (bheight * (dataList.get(i) / maxValue));

            points[i] = new Point((int)drawDotList.get(i).x,(int)drawDotList.get(i).y);
        }
        return points;
    }

    private void drawBackgroundLines(Canvas canvas){
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(DensityUtil.dip2px(getContext(),1f));
//        paint.setColor(BACKGROUND_LINE_COLOR);
//
//        //draw vertical lines  画竖线
//        for(int i=0;i<xCoordinateList.size();i++){
//        	int x = xCoordinateList.get(i);
//        	if(mShowYCoordinate == true) x += YCOORD_TEXT_LEFT_MARGIN;
//            canvas.drawLine(x, 0, x,
//                    mViewHeight - bottomTextTopMargin - bottomTextHeight-bottomTextDescent,
//                    paint);
//        }
//
//        //draw vertical lines  画横线
//        for(int i=0;i<yCoordinateList.size();i++){
//            if((yCoordinateList.size()-1-i)%dataOfAGird == 0){
//                float y = yCoordinateList.get(i);
//                canvas.drawLine(0, y, getWidth(), y, paint);
//
//                if(mShowYCoordinate == true)
//                	canvas.drawText(String.valueOf(yCoordinateList.size()-i-1), 0, y, ycoordTextPaint);
//            }
//        }

        //draw bottom text 画下的文本
   /*     if(bottomTextList != null){
            for(int i=0;i<bottomTextList.size();i++){
            	int x = sideLineLength+backgroundGridWidth*i;
            	if(mShowYCoordinate == true) x += YCOORD_TEXT_LEFT_MARGIN;
                canvas.drawText(bottomTextList.get(i), x,
                        mViewHeight-bottomTextDescent, bottomTextPaint);
            }
        }*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        // refreshAfterDataChanged();
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureWidth(int measureSpec){
        int horizontalGridNum = getHorizontalGridNum();
        int preferred = backgroundGridWidth*horizontalGridNum+sideLineLength*2;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec){
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //触摸时先隐藏所有的点

        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        Region r = new Region();
        int width = backgroundGridWidth/2;
        if(drawDotList != null || !drawDotList.isEmpty()){

            for (int i =0;i<drawDotList.size();i++){
                //for(Dot dot : drawDotList){
                if(dataList.get(i)!=Float.MIN_VALUE){
                    Dot dot = drawDotList.get(i);
                    r.set((int)dot.x-width,(int)dot.y- width,(int)dot.x+width,(int)dot.y+width);

                    if (r.contains(point.x,point.y) && event.getAction() == MotionEvent.ACTION_DOWN){
                        selectedDot = dot;
                        //LogUtil.e("ACTION_DOWN","点按下");
                    }else if (event.getAction() == MotionEvent.ACTION_UP){
                        if (r.contains(point.x,point.y)){
                            showPopup = true;
                          //  LogUtil.e("ACTION_UP","点抬起");
                            index = i;
                        }
                    }
                }else{
                 //   LogUtil.e("ACTION_UP","无效");
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            postInvalidate();
        }
        return true;
    }


    private float updateSelf(float origin, float target, float velocity){
        if (origin < target) {
            origin += velocity;
        } else if (origin > target){
            origin-= velocity;
        }
        if(Math.abs(target-origin)<velocity){
            origin = target;
        }
        return origin;
    }

    public class Dot{
        float x;
        float y;
        float data;
        float targetX;
        float targetY;
        float velocity = DensityUtil.dip2px(getContext(),20);

        public boolean select = false;//有没有被选中 (自行加的)
        Dot(float x,float y,float targetX,float targetY,float data){
            this.x = x;
            this.y = y;
            setTargetData(targetX, targetY,data);
        }

        Point getPoint(){
            return new Point((int)x,(int)y);
        }

        Dot setTargetData(float targetX,float targetY,float data){
            this.targetX = targetX;
            this.targetY = targetY;
            this.data = data;
            return this;
        }

        boolean isAtRest(){
            return (x==targetX)&&(y==targetY);
        }

        void update(){
            x = updateSelf(x, targetX, velocity);
            y = updateSelf(y, targetY, velocity);
        }

        public void setSelect(boolean select){
            this.select = select;
        }
    }


    //返回每个点的距离
    public int getDistance(){
        return backgroundGridWidth;
    }

    //返回点列表
    public ArrayList<Dot> getDrawDotList(){
        return  drawDotList;
    }

    //画某一个点
    public void drawPopup(int index){
        if(index<0){
            return;
        }
        LogUtil.e("drawPopup",index+"");
        this.index = index;
        selectedDot = drawDotList.get(index);
        showPopup = true;
        postInvalidate();
    }

    public interface  OnDataListening{
        void getData(String time, Float data, int index);
    }


    public void setLinestyle( Linestyle style){
        mStyle = style;
    }

    private Linestyle mStyle = Linestyle.Line;
    enum Linestyle
    {
        Line,// 折线
        Curve//曲线
    }

}

