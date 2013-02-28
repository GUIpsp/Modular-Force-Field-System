package mffs.common.tileentity;

import mffs.common.Fortron;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * A TileEntity that is powered by fortron.
 * 
 * @author Calclavia
 * 
 */
public abstract class TileEntityFortron extends TileEntityMFFSInventory implements ITankContainer
{
	public LiquidTank fortronTank;

	/**
	 * Liquid Functions.
	 */
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill)
	{
		if (resource.isLiquidEqual(Fortron.LIQUID_FORTRON))
		{
			return this.fortronTank.fill(resource, doFill);
		}

		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill)
	{
		return this.fill(ForgeDirection.getOrientation(tankIndex), resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return this.fortronTank.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain)
	{
		return this.drain(ForgeDirection.getOrientation(tankIndex), maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction)
	{
		return new ILiquidTank[] { this.fortronTank };
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type)
	{
		if (type.isLiquidEqual(Fortron.LIQUID_FORTRON))
		{
			return this.fortronTank;
		}

		return null;
	}
}
