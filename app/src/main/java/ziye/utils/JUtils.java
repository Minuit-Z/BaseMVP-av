package ziye.utils;

/**
 * @Description:工具类
 * @authors: utopia
 * @Create time: 17-1-6 上午10:03
 * @Update time: 17-1-6 上午10:03
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import ziye.MyApplication;
import ziye.inject.module.MoorNetModule;

public class JUtils {
    public static String TAG;
    public static boolean DEBUG = false;
    private static Context mApplicationContent;
    private static Toast toast;

    public static String sort="NEW";

    public static void initialize(Application app) {
        mApplicationContent = app.getApplicationContext();
    }


    public static void setDebug(boolean isDebug, String TAG) {
        JUtils.TAG = TAG;
        JUtils.DEBUG = isDebug;
    }


    public static void Log(String TAG, String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void Log(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }


    public static void Toast(String text) {
        android.widget.Toast.makeText(mApplicationContent, text, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void ToastLong(String text) {
        android.widget.Toast.makeText(mApplicationContent, text, android.widget.Toast.LENGTH_LONG).show();
    }


    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * px转dp
     */
    public static int px2dip(float pxValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 取屏幕宽度
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 取屏幕高度
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.heightPixels - getStatusBarHeight();
    }

    /**
     * 取屏幕高度包含状态栏高度
     */
    public static int getScreenHeightWithStatusBar() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 取导航栏高度
     */
    public static int getNavigationBarHeight() {
        int result = 0;
        int resourceId = mApplicationContent.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 取状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = mApplicationContent.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight() {
        int actionBarHeight = 0;

        final TypedValue tv = new TypedValue();
        if (mApplicationContent.getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, mApplicationContent.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    /**
     * 关闭输入法
     */
    public static void closeInputMethod(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            view.setEnabled(false);
            ((InputMethodManager) mApplicationContent.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_HIDDEN);
            view.setEnabled(true);
        }
    }

    /**
     * 打开输入法
     */
    public static void openInputMethod(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mApplicationContent.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 判断应用是否处于后台状态
     */
    public static boolean isBackground() {
        ActivityManager am = (ActivityManager) mApplicationContent.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mApplicationContent.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 复制文本到剪贴板
     */
    public static void copyToClipboard(String text) {
        if (Build.VERSION.SDK_INT >= 11) {
            ClipboardManager cbm = (ClipboardManager) mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
            cbm.setPrimaryClip(ClipData.newPlainText(mApplicationContent.getPackageName(), text));
        } else {
            android.text.ClipboardManager cbm = (android.text.ClipboardManager) mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
            cbm.setText(text);
        }
    }

    /**
     * 是否有网络
     */
    public static boolean isNetWorkAvilable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mApplicationContent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否开启 数据连接 true：开启 false：关闭
     */
    public static void setMobileData(boolean isEnable) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mApplicationContent.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
        method.invoke(mConnectivityManager, isEnable);
    }

    /**
     * 取APP版本号
     */
    public static int getAppVersionCode() {
        try {
            PackageManager mPackageManager = mApplicationContent.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName(), 0);
            return _info.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 取APP版本名
     */
    public static String getAppVersionName() {
        try {
            PackageManager mPackageManager = mApplicationContent.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName(), 0);
            return _info.versionName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 取APP版本名
     */
    public static String getPackageName() {
        try {
            PackageManager mPackageManager = mApplicationContent.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName(), 0);
            return _info.packageName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static String MD5(String val) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String getStringFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(mApplicationContent.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Uri getUriFromRes(int id) {
        return Uri.parse("res://"
                + mApplicationContent.getPackageName() + "/" + id);
    }

    public static void changeWindowsAlpha(Activity activity, float alpha) {

        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpha;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 根据图片路径将图片转换成Base64字符串
     */
    public static String Bitmap2StrByBase64(String path) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 将bitmap转换成Base64字符串
     */
    public static String Bitmap2StrByBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 返回数据大小 Long 转 b\kb\mb\gb...
     */
    public static String getDataSize(long size) {
        return Formatter.formatFileSize(MyApplication.getMyApplication(), Long.valueOf(size));//工具类 根据文件大小自动转化为KB, MB, GB
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        } catch (Exception e) {
            size = 0;
        }

        return size;
    }

    //主要是把含有中文的url进行uft8编码，只编码中文部分
    public static Uri urlConvert(String url) {
        if (url != null && url.length() > 10) {
            String keyWords;
            String decodeKeyWords;
            try {
                String regex = "([\u4e00-\u9fa5]+)";//正则匹配中文
                Matcher matcher = Pattern.compile(regex).matcher(url);
                if (matcher.find()) {
                    keyWords = matcher.group(0);
                    decodeKeyWords = URLEncoder.encode(keyWords, "utf8");
                    url = url.replace(keyWords, decodeKeyWords);
                }
            } catch (Exception e) {
                return Uri.parse(url);
            }
        }
        return Uri.parse(url);
    }

    public static Map<String, String> getCookie() {
        Map<String, String> cookie = new ArrayMap<>();
        List<Cookie> list = MoorNetModule.LocalCookieJar.getInstance().loadForRequest(null);
        for (int i = 0; i < list.size(); i++) {
            cookie.put("Cookie", list.get(i).toString());
        }
        return cookie;
    }

    //将2016-10-10转画为2016年10约10日
    public static String transData(String date) {
        String str[];
        if (date != null && !date.trim().equals("") && date.contains("-")) {
            str = date.split("-");
        } else {
            return "日期格式错误";
        }
        if (str.length != 3) {
            return "日期格式错误";
        }
        return str[0] + "年" + str[1] + "月" + str[2] + "日";
    }

    /**
     * Created by Administrator on 2016/9/13. 过滤输入法中的表情符号
     */

    public static InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud83e\udd10-\ud83e\udd17]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter emojiFilterWithEnter = new InputFilter() {
        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|\n|[\ud83e\udd10-\ud83e\udd17]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter emojiFilterWithChinese = new InputFilter() {


        Pattern emoji = Pattern.compile(
                "[\u4E00-\u9FA5]|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|\n|[\ud83e\udd10-\ud83e\udd17]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };

    /**
     * @param num 电话号码
     * @author 张子扬
     * @time 2017/5/18 0018 10:39
     * @desc 验证手机号码
     * <p>
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     */
    public static boolean phoneNumberMatch(String num) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        if (TextUtils.isEmpty(num)) return true;
        else return num.matches(regex);
    }

    /**
     * @param name 姓名
     * @author 张子扬
     * @time 2017/7/5 0005 10:03
     * @desc 姓名格式化检查
     */
    public static boolean expertNameMatch(String name) {
        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5]{1,20}+$";
        if (TextUtils.isEmpty(name)) return false;
        else return name.matches(regex);
    }

    /**
     * 抽象出Adapter的ViewHolder
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight();

        return totalHeight - contentHeight;
    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    //获取当前时间
    @SuppressLint("SimpleDateFormat")
    public static String getDate(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String date = sDateFormat.format(time);
        return date;
    }

    //获取当前时间
    @SuppressLint("SimpleDateFormat")
    public static String getDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String date = sDateFormat.format(new Date());
        return date;
    }

    //获取当前时间
    @SuppressLint("SimpleDateFormat")
    public static String getDate2() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String date = sDateFormat.format(new Date());
        return date;
    }

    //获取当前时间
    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long time, String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                format);
        sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String date = sDateFormat.format(time);
        return date;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    // 弹出吐司,不会重复显示
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, "MOORs: "+content, Toast.LENGTH_SHORT);
        } else {
            toast.setText("MOORs: "+content);
        }
        toast.show();
    }

    public static void setEmojiFilter(TextView view, int lenth) {
        view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(lenth), JUtils.emojiFilter});
    }

    /**
     * @param fileName 文件名
     * @author 张子扬
     * @time 2018/1/12 0012 11:01
     * @desc 获得文件后缀
     */
    public static String getDex(String fileName) {
        String dex = "";
        final int idx = fileName.lastIndexOf(".");
        if (idx < 1) {
            return "";
        }
        dex = fileName.substring(idx + 1);
        return dex;
    }

    @SuppressLint("SupportAnnotationUsage")
    public static Spanned getStringWithColor(@NonNull String[] texts, @NonNull @ColorRes String[] colors) {
        if (texts.length != colors.length) throw new IllegalArgumentException("数组长度必须匹配");
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            s.append("<font color='").append(colors[i]).append("'>").append(texts[i]).append(" </font>");
        }
        return Html.fromHtml(s.toString());
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

    public static String getMIMEType(String end) {
        final String[][] MIME_MapTable = {
                //{后缀名，MIME类型}
                {"3gp", "video/3gpp"},
                {"apk", "application/vnd.android.package-archive"},
                {"asf", "video/x-ms-asf"},
                {"avi", "video/x-msvideo"},
                {"bin", "application/octet-stream"},
                {"bmp", "image/bmp"},
                {"c", "text/plain"},
                {"class", "application/octet-stream"},
                {"conf", "text/plain"},
                {"cpp", "text/plain"},
                {"doc", "application/msword"},
                {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                {"xls", "application/vnd.ms-excel"},
                {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {"exe", "application/octet-stream"},
                {"gif", "image/gif"},
                {"gtar", "application/x-gtar"},
                {"gz", "application/x-gzip"},
                {"h", "text/plain"},
                {"htm", "text/html"},
                {"html", "text/html"},
                {"jar", "application/java-archive"},
                {"java", "text/plain"},
                {"jpeg", "image/jpeg"},
                {"jpg", "image/jpeg"},
                {"js", "application/x-javascript"},
                {"log", "text/plain"},
                {"m3u", "audio/x-mpegurl"},
                {"m4a", "audio/mp4a-latm"},
                {"m4b", "audio/mp4a-latm"},
                {"m4p", "audio/mp4a-latm"},
                {"m4u", "video/vnd.mpegurl"},
                {"m4v", "video/x-m4v"},
                {"mov", "video/quicktime"},
                {"mp2", "audio/x-mpeg"},
                {"mp3", "audio/x-mpeg"},
                {"mp4", "video/mp4"},
                {"mpc", "application/vnd.mpohun.certificate"},
                {"mpe", "video/mpeg"},
                {"mpeg", "video/mpeg"},
                {"mpg", "video/mpeg"},
                {"mpg4", "video/mp4"},
                {"mpga", "audio/mpeg"},
                {"msg", "application/vnd.ms-outlook"},
                {"ogg", "audio/ogg"},
                {"pdf", "application/pdf"},
                {"png", "image/png"},
                {"pps", "application/vnd.ms-powerpoint"},
                {"ppt", "application/vnd.ms-powerpoint"},
                {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
                {"prop", "text/plain"},
                {"rc", "text/plain"},
                {"rmvb", "audio/x-pn-realaudio"},
                {"rtf", "application/rtf"},
                {"sh", "text/plain"},
                {"tar", "application/x-tar"},
                {"tgz", "application/x-compressed"},
                {"txt", "text/plain"},
                {"wav", "audio/x-wav"},
                {"wma", "audio/x-ms-wma"},
                {"wmv", "audio/x-ms-wmv"},
                {"wps", "application/vnd.ms-works"},
                {"xml", "text/plain"},
                {"z", "application/x-compress"},
                {"zip", "application/x-zip-compressed"},
                {"", "*/*"}
        };
        String type = "*/*";
//        String fName = file.getName();
//        //获取后缀名前的分隔符"."在fName中的位置。
//        int dotIndex = fName.lastIndexOf(".");
//        if (dotIndex < 0) {
//            return type;
//        }
//        /* 获取文件的后缀名*/
//        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
}