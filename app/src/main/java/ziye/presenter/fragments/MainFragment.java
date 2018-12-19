package ziye.presenter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ziye.base.BaseFragment;
import ziye.inject.module.FragmentModule;
import ziye.network.remote.SubjectManager;
import ziye.presenter.activities.MainActivity;
import ziye.views.fragviews.MainViewFrag;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public class MainFragment extends BaseFragment<MainViewFrag> {

    MainActivity activity;

    @Inject
    SubjectManager manager;

    @Override
    public Class<MainViewFrag> getRootViewClass() {
        return MainViewFrag.class;
    }

    @Override
    protected void completeInject() {
        activity = (MainActivity) getActivity();
        builder.activityComponent(activity.getActivityComponent()).build()
                .inject(this);
    }

    @Override
    public void onViewHasCreated(View view, @Nullable Bundle savedInstanceState) {
        manager.test().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e(TAG, "mainFragment success: " + o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mainFragment error: " + throwable.toString());
                    }
                });
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

}
