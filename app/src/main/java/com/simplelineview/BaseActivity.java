package com.simplelineview;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

/**
 * @author: xjj
 * @date: 2016/11/9 0009
 * @email: xiejiajinxjj@163.com
 */


public class BaseActivity extends AppCompatActivity {

  // public static long activity_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * 通过xml查找相应的ID，通用方法
     */
    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    /**
     * 通过xml查找相应的ID，通用方法
     */
    protected <T extends View> T $(View view,@IdRes int id) {
        return (T) view.findViewById(id);
    }



    protected void fullscreen(boolean enable) {

        if (enable) { // 隐藏状态栏

            WindowManager.LayoutParams lp = getWindow().getAttributes();

            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;

            getWindow().setAttributes(lp);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else { //显示状态栏

            WindowManager.LayoutParams lp = getWindow().getAttributes();

            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().setAttributes(lp);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

    }

}
