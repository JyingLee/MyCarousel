package com.jying.mycarousel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
                Toast.makeText(getApplicationContext(),"图片"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
