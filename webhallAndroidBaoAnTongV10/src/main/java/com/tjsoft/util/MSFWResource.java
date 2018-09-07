package com.tjsoft.util;

import android.content.Context;

/**
 * 使用反射的方式获取资源id
 * @author Administrator
 *
 */
public class MSFWResource {

//	public static int getResourseIdByName(String packageName, String className,
//			String name)
//	{
//		Class r = null;
//		int id = 0;
//		try
//		{
//			r = Class.forName(packageName + ".R");
//
//			Class[] classes = r.getClasses();
//			Class desireClass = null;
//
//			for (int i = 0; i < classes.length; i++)
//			{
//				if (classes[i].getName().split("\\$")[1].equals(className))
//				{
//					desireClass = classes[i];
//					break;
//				}
//			}
//
//			if (desireClass != null)
//				id = desireClass.getField(name).getInt(desireClass);
//		} catch (ClassNotFoundException e)
//		{
//			e.printStackTrace();
//		} catch (IllegalArgumentException e)
//		{
//			e.printStackTrace();
//		} catch (SecurityException e)
//		{
//			e.printStackTrace();
//		} catch (IllegalAccessException e)
//		{
//			e.printStackTrace();
//		} catch (NoSuchFieldException e)
//		{
//			e.printStackTrace();
//		}
//
//		return id;
//
//	}
	
	public static int getResourseIdByName(Context context, String className,
			String name)
	{
//		int resourcesId = 0;
//		switch (themeType) {
//		case TYPE_DRAWABLE:
//			resourcesId = mResource.getIdentifier(name, className,
//					packageName);
//			break;
//		case TYPE_STRING:
//			resourcesId = mResource.getIdentifier(item_name, "string",
//					mCurrentPackageName);
//			break;
//		case TYPE_DIMENS:
//			resourcesId = mResource.getIdentifier(item_name, "dimens",
//					mCurrentPackageName);
//			break;
//		}
//		
//		return resourcesId;

		return context.getResources().getIdentifier(name, className,
				context.getPackageName());

	}

}
