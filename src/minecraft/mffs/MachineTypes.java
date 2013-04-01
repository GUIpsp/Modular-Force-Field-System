package mffs;

import ic2.api.ExplosionWhitelist;
import mffs.jiqi.t.TAnQuan;
import mffs.jiqi.t.TChouQi;
import mffs.jiqi.t.TDianRong;
import mffs.jiqi.t.TFangYingJi;
import mffs.jiqi.t.TFangYu;
import mffs.rongqi.CAnQuan;
import mffs.rongqi.CChouQi;
import mffs.rongqi.CDianRong;
import mffs.rongqi.CFangYingJi;
import mffs.rongqi.CFangYu;
import mffs.shimian.GAnQuan;
import mffs.shimian.GChouQi;
import mffs.shimian.GDianRong;
import mffs.shimian.GFangYingQi;
import mffs.shimian.GFangYu;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public enum MachineTypes
{
	Projector(TFangYingJi.class, GFangYingQi.class, CFangYingJi.class, ZhuYao.bFangYingJi, "KyKyFyKJK", "ByByKyBaB"),
	Extractor(TChouQi.class, GChouQi.class, CChouQi.class, ZhuYao.bChouQi, " C xFx G ", " E xKx J "),
	Capacitor(TDianRong.class, GDianRong.class, CDianRong.class, ZhuYao.bDianRong, "xJxCFCxJx", "xaxEKExax"),
	DefenceStation(TFangYu.class, GFangYu.class, CFangYu.class, ZhuYao.bFangYu, " J aFa E ", " a EKE C "),
	SecurityStation(TAnQuan.class, GAnQuan.class, CAnQuan.class, ZhuYao.bAnQuan, "KCKCFCKJK", "CECEKECaC");

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

	public static MachineTypes get(TileEntity tile)
	{
		for (MachineTypes mach : values())
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
		for (MachineTypes machine : values())
		{
			ExplosionWhitelist.addWhitelistedBlock(machine.block);
		}
	}
}