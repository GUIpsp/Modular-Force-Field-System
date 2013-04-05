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
public class BlockMachine
{
	public static BlockMachine projector;
	public static BlockMachine extractor;
	public static BlockMachine capacitor;
	public static BlockMachine defenceStation;
	public static BlockMachine securityStation;

	public static final BlockMachine[] list = new BlockMachine[5];

	public Class<? extends TileEntity> tileEntity;
	public Class<? extends GuiScreen> gui;
	public Class<? extends Container> container;
	public Block block;

	public BlockMachine(int id, Block block, Class<? extends TileEntity> tileEntity, Class<? extends Container> container)
	{
		this.block = block;
		this.tileEntity = tileEntity;
		this.container = container;
		list[id] = this;
	}

	public static BlockMachine get(String name)
	{
		for (BlockMachine machine : list)
		{
			if (machine.block.getUnlocalizedName().equals(name))
			{
				return machine;
			}
		}

		return null;
	}

	public static BlockMachine get(TileEntity tile)
	{
		for (BlockMachine mach : list)
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
		for (BlockMachine machine : list)
		{
			GameRegistry.registerBlock(machine.block, machine.block.getUnlocalizedName());
			GameRegistry.registerTileEntity(machine.tileEntity, machine.block.getUnlocalizedName());
			ExplosionWhitelist.addWhitelistedBlock(machine.block);
		}
	}
}