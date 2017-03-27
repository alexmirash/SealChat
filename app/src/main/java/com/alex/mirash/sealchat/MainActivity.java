package com.alex.mirash.sealchat;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    SlidingPaneLayout mSlidingPanel;
    ListView mMenuList;

    String[] MenuTitles = new String[]{"First Item", "Second Item", "Third Item", "Fourth Item"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSlidingPanel = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        mMenuList = (ListView) findViewById(R.id.MenuList);
//        appImage = (ImageView) findViewById(android.R.id.home);

//        TitleText = (TextView)findViewById(android.R.id.title);


        mMenuList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, MenuTitles));

        mSlidingPanel.setPanelSlideListener(panelListener);
        mSlidingPanel.setParallaxDistance(200);


//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

    }


    SlidingPaneLayout.PanelSlideListener panelListener = new SlidingPaneLayout.PanelSlideListener() {

        @Override
        public void onPanelClosed(View arg0) {
            // TODO Auto-genxxerated method stub        getActionBar().setTitle(getString(R.string.app_name));
//            appImage.animate().rotation(0);
        }

        @Override
        public void onPanelOpened(View arg0) {
            // TODO Auto-generated method stub
//            getActionBar().setTitle("Menu Titles");
//            appImage.animate().rotation(90);
        }

        @Override
        public void onPanelSlide(View arg0, float arg1) {
            // TODO Auto-generated method stub

        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mSlidingPanel.isOpen()) {
//                    appImage.animate().rotation(0);
                    mSlidingPanel.closePane();
//                    getActionBar().setTitle(getString(R.string.app_name));
                } else {
//                    appImage.animate().rotation(90);
                    mSlidingPanel.openPane();
//                    getActionBar().setTitle("Menu Titles");
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
