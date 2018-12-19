package ziye.utils.app;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public class FragUtil {

    public void transFrag(AppCompatActivity activity, @IdRes int container , Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(container, fragment).commit();

    }
}
