package demo.viewpager_slidingindicator;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 4;
    private View slideTrack, slideIndicator;
    private int slideIndicatorWidth, marginLeft;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /*
    initialising views
     */
    private void initView() {
        slideTrack = findViewById(R.id.slide_track);
        slideIndicator = findViewById(R.id.slide_indicator);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        showScrollIndicator(PAGE_COUNT);
    }

    /*
    Call this function to set sliding indicator initial layout params
     */
    private void showScrollIndicator(final int totalFragment) {
        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                float slideTrackWidth = slideTrack.getWidth();
                slideIndicatorWidth = (int) (slideTrackWidth / totalFragment);

                FrameLayout.LayoutParams layoutParamsTrack = (FrameLayout.LayoutParams) slideTrack.getLayoutParams();
                FrameLayout.LayoutParams layoutParamsIndicator = (FrameLayout.LayoutParams) slideIndicator.getLayoutParams();

                marginLeft = layoutParamsTrack.leftMargin;
                layoutParamsIndicator.width = slideIndicatorWidth;
                layoutParamsIndicator.leftMargin = marginLeft + slideIndicatorWidth * viewPager.getCurrentItem();

                slideIndicator.setLayoutParams(layoutParamsIndicator);
            }
        });
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) slideIndicator.getLayoutParams();

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /*
            Below code has been used for showing scrolling action by setting left margin of sliding indicator
             */
            int checkOffset = Float.compare(0, positionOffset);
            if (checkOffset == 0) {
                layoutParams.leftMargin = marginLeft + slideIndicatorWidth * position;
            } else {
                layoutParams.leftMargin = marginLeft + (int) (slideIndicatorWidth * (position + positionOffset));
            }
            slideIndicator.setLayoutParams(layoutParams);

            /*
            -----------------------------------------------------------------------------------------------------
             */
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            EmptyFragment emptyFragment = new EmptyFragment();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.title), getString(R.string.fragment) + (position + 1));
            emptyFragment.setArguments(bundle);
            return emptyFragment;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
