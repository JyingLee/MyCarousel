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
        lists.add("http://img.tupianzj.com/uploads/allimg/150905/9-150Z5215644.jpg");
        lists.add("http://img.tupianzj.com/uploads/allimg/150907/9-150ZH11524.jpg");
        lists.add("http://img.tupianzj.com/uploads/allimg/150907/9-150ZH11524-50.jpg");
        lists.add("http://img.tupianzj.com/uploads/allimg/150907/9-150ZH11524-51.jpg");
        lists.add("http://img.tupianzj.com/uploads/allimg/150907/9-150ZH11525.jpg");
        carousel.setUrlImage(lists);
        carousel.setOnImageClickListener(new OnImageClickListaner() {
            @Override
            public void onImageClick(int position) {
                Toast.makeText(getApplicationContext(),"图片"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
