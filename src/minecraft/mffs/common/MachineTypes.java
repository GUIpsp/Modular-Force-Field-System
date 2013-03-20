package mffs.common;

import ic2.api.ExplosionWhitelist;
import mffs.client.shimian.GAnQuan;
import mffs.client.shimian.GChouQi;
import mffs.client.shimian.GDianRong;
import mffs.client.shimian.GFangYingQi;
import mffs.client.shimian.GFangYu;
import mffs.client.shimian.GKongZhi;
import mffs.common.container.ContainerControlSystem;
import mffs.common.container.ContainerDefenseStation;
import mffs.common.container.ContainerForcilliumExtractor;
import mffs.common.container.ContainerFortronCapacitor;
import mffs.common.container.ContainerProjector;
import mffs.common.container.ContainerSecurityStation;
import mffs.common.tileentity.TAnQuan;
import mffs.common.tileentity.TFangYingJi;
import mffs.common.tileentity.TFangYu;
import mffs.common.tileentity.TKongZhi;
import mffs.common.tileentity.TileEntityForcilliumExtractor;
import mffs.common.tileentity.TDianRong;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public enum MachineTypes
{
	Projector(TFangYingJi.class, GFangYingQi.class, ContainerProjector.class, ZhuYao.blockProjector, "KyKyFyKJK", "ByByKyBaB"),
	Extractor(TileEntityForcilliumExtractor.class, GChouQi.class, ContainerForcilliumExtractor.class, ZhuYao.blockExtractor, " C xFx G ", " E xKx J "),
	Capacitor(TDianRong.class, GDianRong.class, ContainerFortronCapacitor.class, ZhuYao.blockCapacitor, "xJxCFCxJx", "xaxEKExax"),
	DefenceStation(TFangYu.class, GFangYu.class, ContainerDefenseStation.class, ZhuYao.blockDefenceStation, " J aFa E ", " a EKE C "),
	SecurityStation(TAnQuan.class, GAnQuan.class, ContainerSecurityStation.class, ZhuYao.blockSecurityStation, "KCKCFCKJK", "CECEKECaC"),
	ControlSystem(TKongZhi.class, GKongZhi.class, ContainerControlSystem.class, ZhuYao.blockControlSystem, "aCaAFAACA", "aEaAKAAEA");

	public Class<? extends TileEntity> tileEntity;
	public Class<? extends GuiScreen> gui;
	public Class<? extends Container> container;
	public Block block;
	public String recipe_ic;
	public String recipe_ue;

	private MachineTypes(Class<? extends TileEntity> tileEntity, Class<? extends GuiScreen> gui, Class<? extends Container> container, Block block, String recipeic, String recipeue)
	{
		this.tileEntity = tileEntity;
		this.gui = gui;
		this.container = container;

		this.recipe_ic = recipeic;
		this.recipe_ue = recipeue;
		this.block = block;
	}

	public String getName()
	{
		return TranslationHelper.getLocal(this.block.getUnlocalizedName() + ".name");
	}

	public static MachineTypes get(String name)
	{
		for (MachineTypes machine : values())
		{
			if (machine.block.getUnlocalizedName().equals(name))
			{
				return machine;
			}
		}

		return null;
	}

	public static MachineTypes fromTE(TileEntity tem)
	{
		for (MachineTypes mach : values())
		{
			if (mach.tileEntity.isInstance(tem))
			{
				return mach;
			}
		}
		return null;
	}

	public static void initialize()
	{
		for (MachineTypes mach : values())
		{
			GameRegistry.registerBlock(mach.block, mach.block.getUnlocalizedName());
			GameRegistry.registerTileEntity(mach.tileEntity, mach.block.getUnlocalizedName());

			if (MFFSConfiguration.MODULE_IC2)
				MFFSRecipes.addRecipe(mach.recipe_ic, 1, 1, mach.block, null);

			if (MFFSConfiguration.MODULE_UE)
				MFFSRecipes.addRecipe(mach.recipe_ue, 1, 2, mach.block, null);

			ExplosionWhitelist.addWhitelistedBlock(mach.block);
		}
	}
}