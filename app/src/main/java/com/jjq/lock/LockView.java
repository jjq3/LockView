package com.jjq.lock;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * 锁屏左右滑动控件
 * Created by jiangjieqiang on 2017/1/10.
 */

public class LockView extends RelativeLayout {

    private static final String TAG = "MainActivity";

    private ImageView homeImageView;         //home
    private ImageView zuoImageView;          //home左边的左箭头
    private ImageView youImageView;          //home右边的右箭头
    private ImageView leftImageView;         //左边去赚钱的imageView
    private ImageView rightImageView;        //右边解锁的imageView

    private int width;                       //屏幕宽度
    private int mx, my;
    private int top;
    private int bottom;
    private int left;
    private int right;
    private int leftImageView_left;
    private int rightImageView_right;
    private boolean left_flag = false;
    private boolean right_flag = false;
    private int leftImageView_right;
    private int rightImageView_left;
    private Context mContext;

    private LockViewClickListener lockViewClickListener;

    //home键按下的坐标
    private float homeDownX, homeDownY;
    //home键抬起的坐标
    private float homeUpX, homeUpY;

    public interface LockViewClickListener{
        public void clickRight();
        public void clickLeft();
        public void clickHome();
    }

    public void setOnLockViewClickListener(LockViewClickListener lockViewClickListener){
        this.lockViewClickListener = lockViewClickListener;
    }

    public LockView(Context context) {
        super(context);
        mContext = context;
        width = MeasureUtil.getWidth(context);
        LayoutInflater.from(context).inflate(R.layout.layout_lock_view, this);
        initViews();
        addListener();
    }

    public LockView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;
        width = MeasureUtil.getWidth(context);
        LayoutInflater.from(context).inflate(R.layout.layout_lock_view, this);
        initViews();
        addListener();
    }

    private void addListener() {
        homeImageView.setOnTouchListener(new OnTouchListener() {
            private int height;
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        homeDownX = event.getRawX();
                        homeDownY = event.getRawY();
                        homeImageView.setImageResource(R.mipmap.tongqian_pressed);
                        leftImageView.setImageResource(R.mipmap.xiazaizhong);
                        rightImageView.setImageResource(R.mipmap.yaoshizhong);

                        // leftImageView_left:32         rightImageView_right:688
                        zuoImageView.setVisibility(View.VISIBLE);
                        youImageView.setVisibility(View.VISIBLE);
                        leftImageView_left = leftImageView.getLeft();
                        leftImageView_right = leftImageView.getRight();
                        rightImageView_right = rightImageView.getRight();
                        rightImageView_left = rightImageView.getLeft();
                        Log.i(TAG, "leftImageView_left:" + leftImageView_left + "rightImageView_right:" + rightImageView_right);
                        Log.i(TAG, "event.getRawX()" + event.getRawX() + "event.getRawY()" + event.getRawY());
                        Log.i(TAG, "event.getX()" + event.getX() + "event.getY()" + event.getY());
                        height = (int) (event.getRawY() - 50);

                        System.out.println("v.getTop()" + v.getTop() + "v.getBottom()" + v.getBottom() + "v.getLeft()" + v.getLeft() + "v.getRight" + v.getRight());
                        left = v.getLeft();
                        right = v.getRight();
                        top = v.getTop();
                        bottom = v.getBottom();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("----------event.getRawX()" + event.getRawX() + "event.getRawY()" + event.getRawY());
                        System.out.println("----------event.getX()" + event.getX() + "event.getY()" + event.getY());
                        mx = (int) (event.getRawX());
                        my = (int) (event.getRawY() - 50);
                        Log.i(TAG, "  mx " + mx + "   my" + my + "  img.getWidth()/2" + homeImageView.getWidth() / 2 + "   img.getHeight()/2" + zuoImageView.getHeight() / 2);
                        if (mx < width / 2) {
                            if (mx < leftImageView_right) {
                                v.layout(leftImageView_left, top, leftImageView_right, bottom);
                                left_flag = true;
                            } else {
                                v.layout(mx - homeImageView.getWidth() / 2, top, mx + homeImageView.getWidth() / 2, bottom);
                                left_flag = false;
                            }

                        } else if (mx > width / 2) {
                            if ((mx + homeImageView.getWidth() / 2) < rightImageView_right) {
                                v.layout(mx - homeImageView.getWidth() / 2, top, mx + homeImageView.getWidth() / 2, bottom);
                                Log.i(TAG, "  int l " + (mx - homeImageView.getWidth() / 2) + "   int top" + (my - homeImageView.getHeight() / 2) + "    " +
                                        "int right" + (mx + zuoImageView.getWidth() / 2)
                                        + "   int bottom" + (my + homeImageView.getHeight() / 2));
                            }

                            if (mx > rightImageView_left) {
                                v.layout(rightImageView_left, top, rightImageView_right, bottom);
                                right_flag = true;
                            } else {
                                v.layout(mx - homeImageView.getWidth() / 2, top, mx + homeImageView.getWidth() / 2, bottom);
                                right_flag = false;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        homeImageView.setImageResource(R.mipmap.home);
                        homeUpX = event.getRawX();
                        homeUpY = event.getRawY();
                        zuoImageView.setVisibility(View.GONE);
                        youImageView.setVisibility(View.GONE);
                        if (Math.abs(homeUpX - homeDownX) > 3){
                            //拖拽
                            leftImageView.setImageResource(R.mipmap.xiazai);
                            rightImageView.setImageResource(R.mipmap.yaoshi);
                            if (right_flag) {
                                lockViewClickListener.clickRight();
                                rightImageView.setImageResource(R.mipmap.yaoshiok);
                            } else if (left_flag) {
                                lockViewClickListener.clickLeft();
                                leftImageView.setImageResource(R.mipmap.xiazaiok);
                            }
                            right_flag = false;
                            left_flag = false;
                            v.layout(left, top, right, bottom);
                        }else {
                            //点击
                            lockViewClickListener.clickHome();
                        }
                        break;
                }
                return true;
            }

        });
    }

    private void initViews() {
        leftImageView = (ImageView) findViewById(R.id.image_lock_view_left);
        rightImageView = (ImageView) findViewById(R.id.image_lock_view_right);
        homeImageView = (ImageView) findViewById(R.id.image_lock_view_drag);
        zuoImageView = (ImageView) findViewById(R.id.image_lock_view_zuo);
        youImageView = (ImageView) findViewById(R.id.image_lock_view_you);
    }

}
