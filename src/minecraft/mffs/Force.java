package mffs;

import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * A class to help and manage fortrons.
 * 
 * @author Calclavia
 * 
 */
public class Force
{
	public static LiquidStack LIQUID_FORTRON;

	public static LiquidStack getFortron(int amount)
	{
		LiquidStack stack = Force.LIQUID_FORTRON.copy();
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

	public static int getAmount(LiquidTank fortronTank)
	{
		if (fortronTank != null)
		{
			return getAmount(fortronTank.getLiquid());
		}

		return 0;
	}
}
