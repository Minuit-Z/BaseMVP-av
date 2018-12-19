package ziye.views.atyviews;

import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ziye.basemvp.mvp.view.BaseViewImpl;
import ziye.ziye_mvp_master.R;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public class MainView extends BaseViewImpl {
    @BindView(R.id.frame)
    protected FrameLayout layout;

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {

    }
}
