package cn.lgh.openapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lgh on 2016/8/22.
 */
public class Utils
{

	private static boolean sInit = false;
	public static float sDensity;//密度
	public static float sDensityDpi;
	public static int sScreenW;
	public static int sScreenH;
	public static float sRatio = 9f / 16;  //宽高比

	public static int sRealScreenW = 720;
	public static int sRealScreenH = 1280;
	public static float sRealDpi;//精确密度，一些假2k屏用到

	public static void init(Context context)
	{
		init(context,false);
	}

	/**
	 * 初始化获取屏幕参数
	 *
	 * @param update true:重新获取数据
	 * @return 成功/失败
	 */
	public static boolean init(Context context, boolean update) {
		boolean out = false;
		if (!sInit || update) {
			try {
				if (context != null) {
					Display display;
					DisplayMetrics dm = new DisplayMetrics();
					if (context instanceof Activity) {
						display = ((Activity) context).getWindowManager().getDefaultDisplay();
						display.getMetrics(dm);
					} else {
						WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
						display = wm.getDefaultDisplay();
						display.getMetrics(dm);
					}

					boolean initDisplayInfo = false;
					try {
						if (Build.VERSION.SDK_INT <= 27) {
							ClassLoader cl = ClassLoader.getSystemClassLoader();
							Class<?> DisplayInfoClass = cl.loadClass("android.view.DisplayInfo");
							Object o = DisplayInfoClass.newInstance();
							Field appWidth = DisplayInfoClass.getDeclaredField("appWidth");
							appWidth.setAccessible(true);
							Field appHeight = DisplayInfoClass.getDeclaredField("appHeight");
							appHeight.setAccessible(true);
							Field logicalWidth = DisplayInfoClass.getDeclaredField("logicalWidth");
							logicalWidth.setAccessible(true);
							Field logicalHeight = DisplayInfoClass.getDeclaredField("logicalHeight");
							logicalHeight.setAccessible(true);
							Class<? extends Display> aClass = display.getClass();
							Method getDisplayInfo = aClass.getDeclaredMethod("getDisplayInfo", DisplayInfoClass);
							getDisplayInfo.setAccessible(true);
							getDisplayInfo.invoke(display, o);
							sScreenW = appWidth.getInt(o);
							sScreenH = appHeight.getInt(o);
							sRealScreenW = logicalWidth.getInt(o);
							sRealScreenH = logicalHeight.getInt(o);
							initDisplayInfo = true;
						}
					} catch (Throwable th) {
						th.printStackTrace();
						initDisplayInfo = false;
					}

					if (!initDisplayInfo) {
						sScreenW = dm.widthPixels;
						sScreenH = dm.heightPixels;
						if (sScreenW > sScreenH) {
							sScreenW += sScreenH;
							sScreenH = sScreenW - sScreenH;
							sScreenW -= sScreenH;
						}

						sRealScreenW = sScreenW;
						sRealScreenH = sScreenH;

						if (android.os.Build.VERSION.SDK_INT >= 17) {
							try {
								Point p = new Point();
								display.getRealSize(p);
								sRealScreenW = p.x;
								sRealScreenH = p.y;
							} catch (Throwable t) {
								t.printStackTrace();
								try {
									Method method = Display.class.getMethod("getRealMetrics", new Class[]{DisplayMetrics.class});
									method.invoke(display, new Object[]{dm});
									sRealScreenW = dm.widthPixels;
									sRealScreenH = dm.heightPixels;
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					}

					sRatio = sScreenW * 1.0f / sScreenH;
					sDensityDpi = dm.densityDpi;
					sRealDpi = Math.min(Math.round(dm.xdpi), Math.round(dm.ydpi));//精确密度
					sDensity = dm.density;
					if (sRealScreenW > sRealScreenH) {
						sRealScreenW += sRealScreenH;
						sRealScreenH = sRealScreenW - sRealScreenH;
						sRealScreenW -= sRealScreenH;
					}

					sInit = true;
					out = true;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

		} else {
			out = true;
		}
		return out;
	}

	/**
	 * 基于720p
	 * @param pxSrc
	 * @return
	 */
	public static int getRealPixel(int pxSrc)
	{
		return px2Dpi_xhdpi(pxSrc);
	}

	public static int px2Dpi_xhdpi(int size){
		return (int) (size/2f*sDensity+0.5f);
	}
	public static int px2Dpi_xhdpi2(int size){
		return (int) (size*sRealDpi/360f+0.5f);
	}

	/**
	 * 基于1080p
	 * @param size
	 * @return
	 */
	public static int px2Dpi_xxhdpi(int size){
		return (int) (size/3f*sDensity+0.5f);
	}

	public static int px2Dpi_xxhdpi2(int size){
		return (int) (size*sRealDpi/480f+0.5f);
	}

	public static List<String> getListPicPath(Activity activity)
	{
		List<String> picPath = new ArrayList<>();

		Intent intent = activity.getIntent();//如果从外部进入APP，则实现以下方法
		if(Intent.ACTION_SEND.equals(intent.getAction()))
		{
			if(intent.getType().startsWith("image/"))
			{
				Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
				if(imageUri != null)
				{
					//处理单张图片
					Log.v("从其他APP分享的", imageUri.getPath());

					if(!imageUri.getPath().contains("external/images/media"))
						picPath.add(imageUri.getPath());
					else
					{
						String[] proj = {MediaStore.Images.Media.DATA};
						Cursor actualimagecursor = activity.managedQuery(imageUri, proj, null, null, null);
						int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						actualimagecursor.moveToFirst();
						String img_path = actualimagecursor.getString(actual_image_column_index);
						picPath.add(img_path);
					}
				}
			}
		}
		else if(Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction()))
		{
			if(intent.getType().startsWith("image/"))
			{

				List<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
				if(imageUris != null)
				{
					//处理多张图片
					for(int i = 0; i < imageUris.size(); i++)
					{

						Log.v("从其他APP分享的", imageUris.get(i).getPath());

						if(!imageUris.get(i).getPath().contains("external/images/media"))
							picPath.add(imageUris.get(i).getPath());
						else
						{
							String[] proj = {MediaStore.Images.Media.DATA};
							Cursor actualimagecursor = activity.managedQuery(imageUris.get(i), proj, null, null, null);
							int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
							actualimagecursor.moveToFirst();
							String img_path = actualimagecursor.getString(actual_image_column_index);


							picPath.add(img_path);
						}
					}
				}
			}
		}

		return picPath;
	}


	public static ShareExtraInfo getKeyValueFromJson(String objExtra)
	{
		ShareExtraInfo info = null;
		if(android.text.TextUtils.isEmpty(objExtra))
		{
			return info;
		}
		info = new ShareExtraInfo();
		try
		{
			JSONObject jsonObject = new JSONObject(objExtra);
			Iterator iterator = jsonObject.keys();
			while(iterator.hasNext())
			{
				String key = (String)iterator.next();
				info.keyList.add(key);
				Object object = jsonObject.get(key);
				info.valueList.add(object);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return info;
	}

	public static class ShareExtraInfo
	{
		public ArrayList<String> keyList = new ArrayList<>();
		public ArrayList<Object> valueList = new ArrayList<>();
	}


	/**
	 * @param data     要格式化的数据
	 * @param accuracy 要保持的精度
	 * @return
	 */
	public static float formatData(float data, int accuracy)
	{
		float seed = 1.0f;
		while(accuracy > 0)
		{
			seed *= 10;
			accuracy--;
		}
		float result = (int)(data * seed) / seed;
		return result;
	}

	/**
	 * 把指定颜色加上指定透明度
	 *
	 * @param color  颜色
	 * @param degree 透明度 0-1
	 * @return 加上透明度的颜色
	 */
	public static int getColorWithAlpha(int color, float degree)
	{
		if(color == 0)
		{
			return 0;
		}
		if(degree < 0) degree = 0;
		if(degree > 1) degree = 1;
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb((int)(255 * degree), red, green, blue);
	}

	public static void AddViewBackgroundSkinWithColor(View view, int color)
	{
		if(color != 0)
		{
			Drawable bgDrawable = view.getBackground();
			if(bgDrawable != null)
			{
				bgDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			}
		}
	}

	/**
	 * @param originColor alpha通道也要传进来
	 * @param alpha
	 * @return
	 */
	public static ColorStateList getAlphaColorStates(int originColor, float alpha)
	{
		int[][] states = new int[3][];
		//按下
		states[0] = new int[]{android.R.attr.state_pressed};
		//焦点
		states[1] = new int[]{android.R.attr.state_focused};
		states[2] = new int[]{};

		//颜色
		int a = Math.min(255, Math.max(0, (int)(alpha * 255))) << 24;
		int rgb = 0x00ffffff & originColor;
		int[] colors = new int[]{a + rgb, a + rgb, originColor};

		ColorStateList colorList = new ColorStateList(states, colors);
		return colorList;
	}
}
