package mffs.it.gongju;

import com.google.common.collect.ObjectArrays;

public class ToolRegistry
{

	private static IMultiTool[] modes = new IMultiTool[16];

	public static void appendMode(IMultiTool tool)
	{
		modes = ObjectArrays.concat(tool, modes);
	}

	public static IMultiTool getMode(int mode)
	{
		return modes[mode];
	}

	public static int getLength()
	{
		return modes.length;
	}

}
