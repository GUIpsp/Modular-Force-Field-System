package mffs;

import ic2.api.ExplosionWhitelist;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * A class to store basic machine information.
 * 
 * @author Calclavia
 * 
 */
public class JiQi
{
	public static JiQi fangYingJi;
	public static JiQi chouQi;
	public static JiQi dianRong;
	public static JiQi fangYu;
	public static JiQi anQuan;

	public static final JiQi[] list = new JiQi[5];

	public Class<? extends TileEntity> tileEntity;
	public Class<? extends GuiScreen> gui;
	public Class<? extends Container> container;
	public Block block;

	public JiQi(int id, Block block, Class<? extends TileEntity> tileEntity, Class<? extends Container> container)
	{
		this.block = block;
		this.tileEntity = tileEntity;
		this.container = container;
		list[id] = this;
	}

	public static JiQi get(String name)
	{
		for (JiQi machine : list)
		{
			if (machine.block.getUnlocalizedName().equals(name))
			{
				return machine;
			}
		}

		return null;
	}

	public static JiQi get(TileEntity tile)
	{
		for (JiQi mach : list)
		{
			if (mach.tileEntity.isInstance(tile))
			{
				return mach;
			}
		}
		return null;
	}

	public static void initialize()
	{
		for (JiQi machine : list)
		{
			GameRegistry.registerBlock(machine.block, machine.block.getUnlocalizedName());
			GameRegistry.registerTileEntity(machine.tileEntity, machine.block.getUnlocalizedName());
			ExplosionWhitelist.addWhitelistedBlock(machine.block);
		}
	}
}