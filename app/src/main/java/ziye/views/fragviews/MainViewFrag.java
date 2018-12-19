package ziye.views.fragviews;

import android.support.v7.app.AppCompatActivity;

import ziye.basemvp.mvp.view.BaseViewImpl;
import ziye.ziye_mvp_master.R;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public class MainViewFrag extends BaseViewImpl{
    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {

    }
}
