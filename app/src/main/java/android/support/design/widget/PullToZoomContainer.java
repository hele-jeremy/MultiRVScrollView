package android.support.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lsjwzh.widget.multirvcontainer.MultiRVScrollView;

public class PullToZoomContainer extends MultiRVScrollView {
  private static final String TAG = PullToZoomContainer.class.getSimpleName();
  private int headerHeight;

  public PullToZoomContainer(Context context) {
    super(context);
  }

  public PullToZoomContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PullToZoomContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }


  @Override
  public void stopNestedScroll() {
    super.stopNestedScroll();
    Log.d(TAG, "stopNestedScroll:");
    View headerView = findHeaderView();
    View otherView = findOtherView();
    headerView.animate().scaleY(1).scaleX(1).start();
    otherView.animate().translationY(0).start();
  }

  @Override
  public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    super.onNestedPreScroll(target, dx, dy, consumed);
    Log.d(TAG, "dy:" + dy + " consumed:" + consumed[1]);
    if (dy < -7 && getScrollY() == 0 && consumed[1] == 0) {
      consumed[1] = 7 + dy;
    }
  }

  @Override
  public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    Log.d(TAG, "dyConsumed:" + dyConsumed + " dyUnconsumed:" + dyUnconsumed);
    if (dyUnconsumed != 0 && getScrollY() == 0) {
      View headerView = findHeaderView();
      View otherView = findOtherView();
      if (headerView != null) {
        int height = headerView.getHeight();
        int targetHeight = headerHeight - dyUnconsumed;//Math.max(headerHeight, headerHeight - dyUnconsumed);
        float scale = targetHeight * 1f / height;
        headerView.setScaleY(Math.max(1, scale));
        headerView.setScaleX(Math.max(1, scale));
        headerView.setPivotY(0f);
        headerHeight = targetHeight;
        float translationY = otherView.getTranslationY() - dyUnconsumed;
        Log.d(TAG, "scaleY:" + scale);
        if (translationY > 0) {
          otherView.setTranslationY(translationY);
          return;
        }
      }
    }
    super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    Log.d(TAG, "dispatchDraw:");
    super.dispatchDraw(canvas);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Log.d(TAG, "onDraw:");
    super.onDraw(canvas);
  }

  @Override
  public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
    Log.d(TAG, "onNestedPreFling:" + velocityY);
    return super.onNestedPreFling(target, velocityX, velocityY);
  }

  @Override
  protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    Log.d(TAG, "onOverScrolled scrollY:" + scrollY + " clampedY:" + clampedY);
    super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    if (clampedY) {
      if (getScrollY() == 0) {
        View headerView = findHeaderView();
        View otherView = findOtherView();
        if (headerView != null) {
          int height = headerView.getHeight();
          int targetHeight = Math.max(headerHeight, headerHeight + 6);
          float scale = targetHeight * 1f / height;
          headerView.setScaleY(Math.max(1, scale));
          headerView.setScaleX(Math.max(1, scale));
          headerView.setPivotY(0f);
          headerHeight = targetHeight;
          float translationY = otherView.getTranslationY() + 6;
          Log.d(TAG, "scaleY:" + scale);
          if (translationY > 0) {
            otherView.setTranslationY(translationY);
          }
        }
      }
    }
  }

  @Override
  public boolean startNestedScroll(int axes) {
    View headerView = findHeaderView();
    headerHeight = headerView.getHeight();
    boolean b = super.startNestedScroll(axes);
    Log.d(TAG, "startNestedScroll:" + b);
    return b;
  }

  @Override
  public void onStopNestedScroll(View target) {
    Log.d(TAG, "onStopNestedScroll");
    super.onStopNestedScroll(target);
  }

  View findHeaderView() {
    return ((ViewGroup) getChildAt(0)).getChildAt(0);
  }


  View findOtherView() {
    return ((ViewGroup) getChildAt(0)).getChildAt(1);
  }
}
