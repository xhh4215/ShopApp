package com.example.hi.refresh;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import com.example.hi.refresh.HiOverView.HiRefreshState;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/***
 * @author 栾桂明
 * @desc 下拉刷新组件
 */
public class HiRefreshLayout extends FrameLayout implements IHiRefresh {

    private HiRefreshState mState;
    /***
     * 检测手势
     */
    private GestureDetector mGestureDetector;

    /***
     * 自动滚动
     */
    private AutoScroller mAutoScroller;
    /***
     * 下拉刷新的监听
     */
    private IHiRefresh.HiRefreshListener mRefreshListener;
    /***
     * 下拉刷新的头部
     */
    protected HiOverView mHiOverView;
    /***
     * 下拉最后的y轴的坐标
     */
    private int mLastY;
    /***
     * 下拉刷新的时候是否禁止滚动
     */
    protected boolean disableRefreshScroll;


    public HiRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }


    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    /***
     * 下拉刷新控件的初始化工作
     */
    private void init() {
        //初始化手势处理对象
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
        //初始化自动滚动对象
        mAutoScroller = new AutoScroller();
    }

    /***
     * 设置下拉刷新是否禁止滚动
     * @param disableRefreshScroll 是否禁止滚动
     */
    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    /***
     * 刷新结束之后的处理逻辑
     */
    @Override
    public void refreshFinished() {
        //获取显示的headView
        final View head = getChildAt(0);
        //回调headView的结束方法
        mHiOverView.onFinish();
        //设置headView的状态为初始状态
        mHiOverView.setState(HiRefreshState.STATE_INIT);
        //获取headView的位置
        final int bottom = head.getBottom();
        //bottom>0说明headView的位置不对应该滚动获取
        if (bottom > 0) {
            recover(bottom);
        }
        //设置当前的刷新的View的状态为初始状态
        mState = HiRefreshState.STATE_INIT;
    }

    /****
     * 设置下拉刷新的
     * @param hiRefreshListener 刷新的监听器
     */
    @Override
    public void setRefreshListener(HiRefreshListener hiRefreshListener) {
        this.mRefreshListener = hiRefreshListener;
    }

    /***
     * 设置下拉刷新的headView
     * @param hiOverView
     */
    @Override
    public void setRefreshOverView(HiOverView hiOverView) {
        if (this.mHiOverView != null) {
            removeView(mHiOverView);
        }
        this.mHiOverView = hiOverView;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mHiOverView, 0, layoutParams);
    }

    /***
     * 通过手势处理对象对Touch事件进行处理
     */
    HiGestureListener gestureListener = new HiGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //横向滑动 或者滑动被禁止 不做处理
            if (Math.abs(distanceX) > Math.abs(distanceY) || mRefreshListener != null && !mRefreshListener.enableRefresh()) {
                return false;
            }
            //刷新的时候是否禁止滚动
            if (disableRefreshScroll && mState == HiRefreshState.STATE_REFRESH) {
                return true;
            }
            View head = getChildAt(0);
            //查找可以滚动的子控件
            View child = HiScrollerUtil.findScrollableChild(HiRefreshLayout.this);
            //如果列表发生滚动就不做处理
            if (HiScrollerUtil.childScrolled(child)) {
                return false;
            }
            //还没有刷新  头部还没达到可刷新距离 头部已经划出
            if ((mState != HiRefreshState.STATE_REFRESH || head.getBottom() <= mHiOverView.mPullRefreshHeight) && (head.getBottom() > 0 || distanceY <= 0.0f)) {
                //还在滑动中
                if (mState != HiRefreshState.STATE_OVER_RELEASE) {
                    int speed;
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        speed = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        speed = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    //如果是正在刷新状态 ，则不可以在滑动的时候改变状态
                    boolean bool = moveDown(speed, true);
                    mLastY = (int) (-distanceY);
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };


    /***
     * 监听手势
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //事件分发处理
        if (!mAutoScroller.isFinished()) {
            return false;
        }
        //获取下拉刷新组件的headView
        View head = getChildAt(0);
        //检测滑动是不是已经松开手
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            //headView被下拉出来  分为 1 还没滑到刷新的位置  2  滑过了刷新位置
            if (head.getBottom() > 0) {
                //如果没有正在刷新 则需要针对1 ，2 两种情况 让headView回弹
                if (mState != HiRefreshState.STATE_REFRESH) {//非正在刷新
                    recover(head.getBottom());
                    return false;
                }
            }
            //下拉最后的y轴的坐标
            mLastY = 0;
        }
        //获取手势检测对事件的处理情况  是否消费
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        //此处可以理解为是下拉的过程中 没有触发刷新的阶段
        if ((consumed || (mState != HiRefreshState.STATE_INIT && mState != HiRefreshState.STATE_REFRESH)) && head.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);//让父类接收不到这个事件
            return super.dispatchTouchEvent(ev);
        }
        //消费了事件则不在进行事件的分发 在当前的View消费了事件
        if (consumed) {
            return true;
        } else {
            //否则继续进行事件的分发
            return super.dispatchTouchEvent(ev);
        }

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == HiRefreshState.STATE_REFRESH) {
                head.layout(0, mHiOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mHiOverView.mPullRefreshHeight);
                child.layout(0, mHiOverView.mPullRefreshHeight, right, mHiOverView.mPullRefreshHeight + child.getMeasuredHeight());

            } else {
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }
            View other;
            for (int i = 2; i < getChildCount(); ++i) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }

    /****
     * 下拉控件拉出来之后的回弹的处理
     * @param dis
     */
    private void recover(int dis) {
        //超出刷新位置的回弹处理
        if (mRefreshListener != null && dis > mHiOverView.mPullRefreshHeight) {
            //滚动到指定位置  dis-mHiOverView.mPullRefreshHeight 触发刷新的位置
            mAutoScroller.recover(dis - mHiOverView.mPullRefreshHeight);
            //设置状态为刷新状态
            mState = HiRefreshState.STATE_OVER_RELEASE;
            //没有超出刷新位置的回弹处理
        } else {
            mAutoScroller.recover(dis);
        }
    }


    /***
     * 根据偏移量移动header和child
     * @param offsetY 偏移量
     * @param auto 是否自动触发
     * @return
     */
    private boolean moveDown(int offsetY, boolean auto) {
        View header = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {//异常
            offsetY = -child.getTop();
            //将header和child移动到原来的位置
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != HiRefreshState.STATE_REFRESH) {
                mState = HiRefreshState.STATE_INIT;
            }
            //下拉刷新中
        } else if (mState == HiRefreshState.STATE_REFRESH && child.getTop() > mHiOverView.mPullRefreshHeight) {
            //如果正在下拉刷新中 则禁止下拉
            return false;

        } else if (childTop <= mHiOverView.mPullRefreshHeight) {//还没超出设定的刷新距离
            if (mHiOverView.getState() != HiRefreshState.STATE_VISIBLE && auto) {//从头部开始显示
                mHiOverView.onVisible();
                mHiOverView.setState(HiRefreshState.STATE_VISIBLE);
                mState = HiRefreshState.STATE_VISIBLE;
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mHiOverView.mPullRefreshHeight && mState == HiRefreshState.STATE_OVER_RELEASE) {
                refresh();
            }

        } else {
            if (mHiOverView.getState() != HiRefreshState.STATE_OVER && auto) {
                //超出刷新位置
                mHiOverView.onOver();
                mHiOverView.setState(HiRefreshState.STATE_OVER);
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mHiOverView != null) {
            mHiOverView.onScroll(header.getBottom(), mHiOverView.mPullRefreshHeight);
        }
        return true;
    }


    /***
     * 开始刷新
     */
    private void refresh() {
        if (mRefreshListener != null) {
            mState = HiRefreshState.STATE_REFRESH;
            mHiOverView.onRefresh();
            mHiOverView.setState(HiRefreshState.STATE_REFRESH);
            mRefreshListener.onRefresh();
        }

    }


    /**
     * 借助Scroller实现视图的自动滚动
     * https://juejin.im/post/5c7f4f0351882562ed516ab6
     */
    private class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//还未滚动完成
                moveDown(mLastY - mScroller.getCurrY(), false);
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }

    }
}
