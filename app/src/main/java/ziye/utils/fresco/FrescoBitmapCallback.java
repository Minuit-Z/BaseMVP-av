package ziye.utils.fresco;

import android.net.Uri;

/**
*@author minuit
*@time 2019/1/7 0007 14:24
*@desc
*/

public interface FrescoBitmapCallback<T> {
    void onSuccess(Uri uri, T result);

    void onFailure(Uri uri, Throwable throwable);

    void onCancel(Uri uri);
}
