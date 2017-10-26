package com.eqdd.floatingmenu;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Bro0cL on 2016/12/26.
 */
public class TopRightMenu {
    private Activity mContext;
    private PopupWindow mPopupWindow;
    private RecyclerView mRecyclerView;
    private View content;

    private TRMenuAdapter mAdapter;
    private List<MenuItem> menuItemList;

    private static final int DEFAULT_HEIGHT = 480;
    private int popHeight = DEFAULT_HEIGHT;
    private int popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;
    private boolean showIcon = true;
    private boolean dimBackground = true;
    private boolean needAnimationStyle = true;

    private static final int DEFAULT_ANIM_STYLE = R.style.TRM_ANIM_STYLE;
    private int animationStyle;

    private float alpha = 0.75f;
    private int csw;
    private TriangleView arrow;

    public TopRightMenu(Activity context) {
        this.mContext = context;
        init();
    }

    private void init() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        csw = dm.widthPixels;
        content = LayoutInflater.from(mContext).inflate(R.layout.trm_popup_menu, null);
        mRecyclerView = (RecyclerView) content.findViewById(R.id.trm_recyclerview);
        arrow = (TriangleView) content.findViewById(R.id.iv_arrow);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        menuItemList = new ArrayList<>();
        mAdapter = new TRMenuAdapter(mContext, this, menuItemList, showIcon);
    }

    private PopupWindow getPopupWindow() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(content);
        mPopupWindow.setHeight(popHeight);
        mPopupWindow.setWidth(popWidth);
        if (needAnimationStyle) {
            mPopupWindow.setAnimationStyle(animationStyle <= 0 ? DEFAULT_ANIM_STYLE : animationStyle);
        }

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (dimBackground) {
                    setBackgroundAlpha(alpha, 1f, 300);
                }
            }
        });

        mAdapter.setData(menuItemList);
        mAdapter.setShowIcon(showIcon);
        mRecyclerView.setAdapter(mAdapter);
        return mPopupWindow;
    }

    public TopRightMenu setHeight(int height) {
        if (height <= 0 && height != RecyclerView.LayoutParams.MATCH_PARENT
                && height != RecyclerView.LayoutParams.WRAP_CONTENT) {
            this.popHeight = DEFAULT_HEIGHT;
        } else {
            this.popHeight = height * csw / 640;
        }
        return this;
    }

    public TopRightMenu setWidth(int width) {
        if (width <= 0 && width != RecyclerView.LayoutParams.MATCH_PARENT) {
            this.popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;
        } else {

            this.popWidth = width * csw / 640;
        }
        return this;
    }

    /**
     * 是否显示菜单图标
     *
     * @param show
     * @return
     */
    public TopRightMenu showIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    /**
     * 添加单个菜单
     *
     * @param item
     * @return
     */
    public TopRightMenu addMenuItem(MenuItem item) {
        menuItemList.add(item);
        return this;
    }

    /**
     * 添加多个菜单
     *
     * @param list
     * @return
     */
    public TopRightMenu addMenuList(List<MenuItem> list) {
        menuItemList.addAll(list);
        return this;
    }

    /**
     * 是否让背景变暗
     *
     * @param b
     * @return
     */
    public TopRightMenu dimBackground(boolean b) {
        this.dimBackground = b;
        return this;
    }

    /**
     * 否是需要动画
     *
     * @param need
     * @return
     */
    public TopRightMenu needAnimationStyle(boolean need) {
        this.needAnimationStyle = need;
        return this;
    }

    /**
     * 设置动画
     *
     * @param style
     * @return
     */
    public TopRightMenu setAnimationStyle(int style) {
        this.animationStyle = style;
        return this;
    }

    public TopRightMenu setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mAdapter.setOnMenuItemClickListener(listener);
        return this;
    }

    public TopRightMenu showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
        return this;
    }

    public TopRightMenu showAsDropDown(View anchor, int xoff, int yoff) {
        if (mPopupWindow == null) {
            getPopupWindow();
        }
        if (!mPopupWindow.isShowing()) {
            xoff = xoff * csw / 640;
            yoff = yoff * csw / 640;
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
            if (dimBackground) {
                setBackgroundAlpha(1f, alpha, 240);
            }
        }
        return this;
    }

    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                mContext.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public TopRightMenu align(Align align) {
        if (align == Align.LEFT) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            arrow.setLayoutParams(layoutParams);
        } else if (align == Align.RIGHT) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            arrow.setLayoutParams(layoutParams);
        } else if (align == Align.CENTER) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            arrow.setLayoutParams(layoutParams);
        }
        return this;
    }

    public TopRightMenu top(Align align, int leftMargin, int rightMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
        if (align == Align.LEFT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (align == Align.RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        layoutParams.leftMargin = dip2px(mContext, leftMargin);
        layoutParams.rightMargin = dip2px(mContext, rightMargin);
        arrow.setLayoutParams(layoutParams);
        return this;
    }

    public TopRightMenu down(Align align, int leftMargin, int rightMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
        if (align == Align.LEFT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (align == Align.RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.leftMargin = dip2px(mContext, leftMargin);
        layoutParams.rightMargin = dip2px(mContext, rightMargin);
        arrow.setLayoutParams(layoutParams);
        arrow.setBottomPath();
        return this;
    }

    public TopRightMenu left(Align align, int leftMargin, int rightMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
        if (align == Align.LEFT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (align == Align.RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.bottomMargin = dip2px(mContext, leftMargin);
        layoutParams.topMargin = dip2px(mContext, rightMargin);
        arrow.setLayoutParams(layoutParams);
        arrow.setLeftPath();
        return this;
    }
    public TopRightMenu right(Align align, int leftMargin, int rightMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
        if (align == Align.LEFT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (align == Align.RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else {
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.topMargin = dip2px(mContext, leftMargin);
        layoutParams.bottomMargin = dip2px(mContext, rightMargin);
        arrow.setLayoutParams(layoutParams);
        arrow.setRightPath();
        return this;
    }


    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position);
    }

    public enum Align {
        LEFT,
        RIGHT,
        CENTER
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
