package ziye.utils.app;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class MyRecyclerManager extends LinearLayoutManager {


    public MyRecyclerManager(Context context) {
        super(context);
    }

    public MyRecyclerManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyRecyclerManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // 禁用滑动来适应scrollView
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
