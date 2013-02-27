package mffs.common;

import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * A class to help and manage fortrons.
 * 
 * @author Calclavia
 * 
 */
public class Fortron
{
	public static LiquidStack LIQUID_FORTRON;

	public static LiquidStack getFortron(int amount)
	{
		LiquidStack stack = Fortron.LIQUID_FORTRON.copy();
		stack.amount = amount;
		return stack;
	}

	public static int getAmount(LiquidStack liquidStack)
	{
		if (liquidStack != null)
		{
			return liquidStack.amount;
		}
		return 0;
	}

	public static double getAmount(LiquidTank fortronTank)
	{
		if (fortronTank != null)
		{
			return getAmount(fortronTank.getLiquid());
		}
		
		return 0;
	}
}
