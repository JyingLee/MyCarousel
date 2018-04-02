package com.jying.mycarousel;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jying.mycarousel.Interface.OnImageClickListaner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Carousel carousel;
    private List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carousel = findViewById(R.id.test);
        lists.add("http://img0.imgtn.bdimg.com/it/u=2743962233,869647381&fm=27&gp=0.jpg");
        lists.add("http://img4.imgtn.bdimg.com/it/u=1370574542,486920420&fm=27&gp=0.jpg");
        lists.add("http://img4.imgtn.bdimg.com/it/u=819488109,1179022423&fm=27&gp=0.jpg");
        lists.add("http://img0.imgtn.bdimg.com/it/u=1326322923,3315473125&fm=27&gp=0.jpg");
        lists.add("http://img0.imgtn.bdimg.com/it/u=733474615,2489633519&fm=27&gp=0.jpg");
        carousel.setUrlImage(lists);
        carousel.setOnImageClickListener(new OnImageClickListaner() {
            @Override
            public void onImageClick(int position) {
                Toast.makeText(MainActivity.this, "图片" + position, Toast.LENGTH_SHORT).show();
            }
        });
        fullScreen(MainActivity.this);
    }
    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

}
