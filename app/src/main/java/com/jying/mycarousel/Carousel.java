package com.jying.mycarousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jying.mycarousel.Interface.OnImageClickListaner;

import java.util.List;

/**
 * Created by Jying on 2018/3/28.
 */

public class Carousel extends RelativeLayout {
    private boolean mPointsVisible = true;//指示点是否设置可见
    private int mPointsPosition;//指示点设置位置
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    private RelativeLayout pointsLayout;//底部容器
    private Drawable mPointsBackground;//指示点的背景
    private LinearLayout pointsLinearLayout;//指示点的背景布局
    private ViewPager viewPager;
    private LayoutParams linearRules;//指示器位置规则
    private List<String> urlImages;//网络图片
    private List<Integer> localImages;//本地图片
    private boolean isUrlImage;//判断是不是网络图片
    public OnImageClickListaner onImageClickListaner;//提供对外图片监听接口
    private int currentPosition;//指示点现在的位置
    private int dian = R.drawable.dian; //指示点
    private CarouselAdapter carouselAdapter;
    private boolean isAutoPlay;//监听是否在自动滑动

    public Carousel(Context context) {
        this(context, null);
    }

    public Carousel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Carousel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            currentPosition++;
            viewPager.setCurrentItem(currentPosition);
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Carousel);
        mPointsPosition = typedArray.getInt(R.styleable.Carousel_points_position, CENTER);
        mPointsVisible = typedArray.getBoolean(R.styleable.Carousel_points_visibility, true);
        mPointsBackground = typedArray.getDrawable(R.styleable.Carousel_points_background);
        typedArray.recycle();//资源回收
        setLayout(context);//设置布局控件
    }

    private void setLayout(Context context) {
        if (mPointsBackground != null) {
            pointsLayout.setBackground(mPointsBackground);//设置指示点布局的背景图片
        }
        viewPager = new ViewPager(context);
        addView(viewPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pointsLayout = new RelativeLayout(context);
        pointsLayout.setPadding(10, 15, 10, 15);
        LayoutParams pointsLayoutRules = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pointsLayoutRules.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(pointsLayout, pointsLayoutRules);//把指示点布局add进父布局
        pointsLinearLayout = new LinearLayout(context);
        linearRules = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pointsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        pointsLayout.addView(pointsLinearLayout, linearRules);
        if (mPointsPosition == CENTER) { //设置指示点位置
            linearRules.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (mPointsPosition == LEFT) {
            linearRules.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (mPointsPosition == RIGHT) {
            linearRules.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        if (pointsLayout != null) {//设置是否可见
            if (mPointsVisible) {
                pointsLayout.setVisibility(View.VISIBLE);
            } else {
                pointsLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 对外接口设置指示点是都可见
     *
     * @param flag
     */
    public void setmPointsVisible(boolean flag) {
        if (pointsLayout != null) {//设置是否可见
            if (flag) {
                pointsLayout.setVisibility(View.VISIBLE);
            } else {
                pointsLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 对外接口设置指示点的位置
     *
     * @param position
     */
    public void setPointsPosition(int position) {
        if (position == CENTER) { //设置指示点位置
            linearRules.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (position == LEFT) {
            linearRules.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (position == RIGHT) {
            linearRules.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
    }

    public void setUrlImage(List<String> urlImages) {
        isUrlImage = true;
        this.urlImages = urlImages;
        initViewPager();
    }

    public void setLocalImages(List<Integer> localImages) {
        this.localImages = localImages;
        isUrlImage = false;
        initViewPager();
    }

    private void initViewPager() {
        addPoints();//画指示点
        carouselAdapter = new CarouselAdapter();
        viewPager.setAdapter(carouselAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(1, false);
        int size = isUrlImage ? urlImages.size() : localImages.size();
        if (size > 1) {
            autoPlay();
        }
    }

    private void autoPlay() {
        isAutoPlay = true;
        if (isAutoPlay) {
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    private void stopPlay() {
        isAutoPlay = false;
        handler.removeMessages(0);
    }

    private void addPoints() {
        LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        pointsLinearLayout.removeAllViews();
        int size = isUrlImage ? urlImages.size() : localImages.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(dian);
            imageView.setLayoutParams(layoutParams);
            pointsLinearLayout.addView(imageView);//把点加进布局中
        }
        switchPoints(0);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //正在滑动时调用
        }

        @Override
        public void onPageSelected(int position) {
            //滑动结束后调用
            currentPosition = position;
            switchPoints(getPosition(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //滑动时状态的监听 state=0什么也没做  1正在滑动 2滑动完毕
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int current = viewPager.getCurrentItem();
                int lastPage = carouselAdapter.getCount() - 1;//adapter多加两页后的最后一页
                if (current == 0) {
                    viewPager.setCurrentItem(lastPage - 1, false);//false表示立即滑到页面，true表示平滑到页面
                } else if (current == lastPage) {
                    viewPager.setCurrentItem(1, false);
                }
            }
        }
    };

    private void switchPoints(int position) {
        for (int i = 0; i < pointsLinearLayout.getChildCount(); i++) {//先把指示点颜色去掉
            pointsLinearLayout.getChildAt(i).setEnabled(false);
        }
        pointsLinearLayout.getChildAt(position).setEnabled(true);
    }

    private class CarouselAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (isUrlImage) {
                return urlImages.size() + 2;
            } else {
                return localImages.size() + 2;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);//设置图片样式
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListaner != null) {
                        onImageClickListaner.onImageClick(getPosition(position));
                    }
                }
            });
            if (isUrlImage) {
                Glide.with(getContext()).load(urlImages.get(getPosition(position))).into(imageView);
            } else {
                imageView.setImageResource(localImages.get(getPosition(position)));
            }
            container.addView(imageView);//把imageview放进容器
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * adapter中返回的图片数量默认加2,多出的两页是头和尾两页,真实地图片显示从第1页开始
     * 根据adapter传过来的位置计算出图片在数组里的真实位置
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        int truePosition;
        if (isUrlImage) {
            truePosition = (position - 1) % urlImages.size();
            if (truePosition < 0) {
                truePosition = truePosition + urlImages.size();
            }
        } else {
            truePosition = (position - 1) % localImages.size();
            if (truePosition < 0) {
                truePosition = truePosition + localImages.size();
            }
        }
        return truePosition;
    }


    public void setOnImageClickListener(OnImageClickListaner onImageClickListener) {
        this.onImageClickListaner = onImageClickListener;
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        if (isAutoPlay) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: //当触摸轮播图停止自动滑动
                    stopPlay();
                    break;
                case MotionEvent.ACTION_OUTSIDE://当手指抬起开始自动轮播
                    autoPlay();
                    break;
            }
        }
        return super.dispatchHoverEvent(event);
    }
}
