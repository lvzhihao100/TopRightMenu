package com.eqdd.toprightmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.eqdd.floatingmenu.MenuItem;
import com.eqdd.floatingmenu.TopRightMenu;
import com.eqdd.floatingmenu.TriangleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TopRightMenu mTopRightMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TriangleView viewById = (TriangleView) findViewById(R.id.tr);
        viewById.setBottomPath();

    }

    public void onClick(View v) {
        if (v.getId() == R.id.tv_click) {
            mTopRightMenu = new TopRightMenu(MainActivity.this);

//添加菜单项
            List<MenuItem> menuItems = new ArrayList<>();
            menuItems.add(new MenuItem("发起多人聊天"));
            menuItems.add(new MenuItem("加好友"));
            menuItems.add(new MenuItem("扫一扫"));

            mTopRightMenu
                    .setHeight(300)     //默认高度480
                    .setWidth(250)      //默认宽度wrap_content
                    .showIcon(false)     //显示菜单图标，默认为true
                    .dimBackground(true)        //背景变暗，默认为true
                    .needAnimationStyle(true)   //显示动画，默认为true
                    .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                    .addMenuList(menuItems)
                    .right(TopRightMenu.Align.LEFT, 30, 10)
                    .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                        @Override
                        public void onMenuItemClick(int position) {
                            Toast.makeText(MainActivity.this, "点击菜单:" + position, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .showAsDropDown(v, 100, 100);    //带偏移量
//      		.showAsDropDown(moreBtn)
        }
    }
}
