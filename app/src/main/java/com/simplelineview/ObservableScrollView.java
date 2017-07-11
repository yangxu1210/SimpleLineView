package com.simplelineview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/*
 * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听，
 * 我们很自然的想到在onTouchEvent()方法中实现对滚动Y轴进行监听
 * ScrollView的滚动Y值进行监听
 */
public class ObservableScrollView extends HorizontalScrollView {
	private OnScrollListener onScrollListener;  
    /** 
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较 
     */

    /**
     * 当前滚动状态
     */
    public enum ScrollType{IDLE,TOUCH_SCROLL,FLING};   //IDLE 滚动停止  TOUCH_SCROLL 手指拖动滚动  FLING滚动
    private ScrollType scrollType =  ScrollType.IDLE;
    private int lastScrollX;
	public ObservableScrollView(Context context) {
		super(context, null);
	}
	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	/** 
     * 设置滚动接口 
     * @param onScrollListener 
     */
	public void setOnScrollListener(OnScrollListener onScrollListener){
		this.onScrollListener = onScrollListener;
	}
	/** 
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 
     */  
    private Handler handler = new Handler() {
  
        public void handleMessage(android.os.Message msg) {  
            int scrollX = ObservableScrollView.this.getScrollX();
              
            //此时的距离和记录下的距离不相等，在隔10毫秒给handler发送消息
            if(lastScrollX != scrollX){  
                lastScrollX = scrollX;
               // Log.e("msg","还在滚动");
                scrollType = ScrollType.FLING;
                if(onScrollListener != null){
                    onScrollListener.onScrollChanged(scrollType);
                }
                handler.sendMessageDelayed(handler.obtainMessage(), 10);
            }else{
               // Log.e("msg","停止滚动");
                scrollType = ScrollType.IDLE;
                if(onScrollListener != null){
                    onScrollListener.onScrollChanged(scrollType);
                }
            }
            if(onScrollListener != null){  
                onScrollListener.onScroll(scrollX,getChildAt(0).getMeasuredWidth());
            }
        };
    };
    /** 
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候， 
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候， 
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理 
     * MyScrollView滑动的距离 
     */ 
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){  
            onScrollListener.onScroll(lastScrollX = this.getScrollX(),this.getChildAt(0).getMeasuredWidth());
        }  
        switch(ev.getAction()){  
        case MotionEvent.ACTION_UP:
             handler.sendMessageDelayed(handler.obtainMessage(), 20);
            break;
            case MotionEvent.ACTION_MOVE:

                scrollType = ScrollType.TOUCH_SCROLL;
                if(onScrollListener != null){
                    onScrollListener.onScrollChanged(scrollType);
                }

                int scrollX=this.getScrollX();
                int width=this.getWidth();
                int scrollViewMeasuredWidth =this.getChildAt(0).getMeasuredWidth();


                if(scrollX==0){
                 //   Log.e("msg","滑动到了顶端 view.getScrollX()="+scrollX);
                }else if(scrollX + width == scrollViewMeasuredWidth){
                //   Log.e("msg","滑动到了底端view.getScrollX()="+scrollX);
                }
                break;
        }
		return super.onTouchEvent(ev);
	}

	/** 
     * 滚动的回调接口 
     */  
    public interface OnScrollListener{  
        /** 
         * 回调方法， 返回MyScrollView滑动的Y方向距离 
         */  
         void onScroll(int scrollX, int total);

        //滑动监听
         void onScrollChanged(ScrollType scrollType);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
