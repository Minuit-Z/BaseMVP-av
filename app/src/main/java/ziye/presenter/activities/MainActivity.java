package ziye.presenter.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ziye.base.BaseActivity;
import ziye.network.remote.SubjectManager;
import ziye.presenter.fragments.MainFragment;
import ziye.utils.app.FragUtil;
import ziye.views.atyviews.MainView;
import ziye.ziye_mvp_master.R;

public class MainActivity extends BaseActivity<MainView> {

    @Inject
    SubjectManager manager;

    MainFragment fragment;

    @Override
    public Class<MainView> getRootViewClass() {
        return MainView.class;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void doOnCreate() {
        activityComponent.inject(this);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        manager.test()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e(TAG, "mainactivity: success" + o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mainactivity: error" + throwable.toString());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);
//        try {
//            AssetManager assetManager = this.getAssets();
//            InputStream in = assetManager.open("welcome_default.jpg");
//            splashImg.setImageDrawable(InputStream2Drawable(in));
//            animWelcomeImage();
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        fragment=new MainFragment();
        FragUtil utils=new FragUtil();
        utils.transFrag(this, R.id.frame,fragment);
    }
}
