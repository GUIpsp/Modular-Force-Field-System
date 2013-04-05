package mffs;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_MFFS_Config implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		API.hideItem(ModularForceFieldSystem.bLiQiang.blockID);

		MultiItemRange blocks = new MultiItemRange();
		MultiItemRange items = new MultiItemRange();
		MultiItemRange upgrades = new MultiItemRange();
		MultiItemRange modules = new MultiItemRange();

		blocks.add(ModularForceFieldSystem.bChouQi);
		blocks.add(ModularForceFieldSystem.bFangYu);
		blocks.add(ModularForceFieldSystem.bAnQuan);
		blocks.add(ModularForceFieldSystem.bDianRong);
		blocks.add(ModularForceFieldSystem.bFangYingJi);
		blocks.add(ModularForceFieldSystem.blockFortronite);

		// items.add(ZhuYao.itemMultiTool); items.add(ZhuYao.itemCardEmpty);
		items.add(ModularForceFieldSystem.itemForcillium);
		items.add(ModularForceFieldSystem.itFortronCell);
		items.add(ModularForceFieldSystem.itFocusMatix);

		upgrades.add(ModularForceFieldSystem.itMDian);
		upgrades.add(ModularForceFieldSystem.itemModuleSponge);
		upgrades.add(ModularForceFieldSystem.itemModuleManipulator);
		upgrades.add(ModularForceFieldSystem.itemModuleDisintegration);
		upgrades.add(ModularForceFieldSystem.itemModuleJammer);
		upgrades.add(ModularForceFieldSystem.itWeiZhuang);
		upgrades.add(ModularForceFieldSystem.itemModuleFusion);

		upgrades.add(ModularForceFieldSystem.itMDaXiao);
		upgrades.add(ModularForceFieldSystem.itMDong);

		modules.add(ModularForceFieldSystem.itMYuan);
		modules.add(ModularForceFieldSystem.itMFang);
		modules.add(ModularForceFieldSystem.itMGuan);

		API.addSetRange("MFFS.Items.Upgrades", upgrades);
		API.addSetRange("MFFS.Items.Modules", modules);
		API.addSetRange("MFFS.Items", items);
		API.addSetRange("MFFS.Blocks", blocks);
	}

	@Override
	public String getName()
	{
		return ModularForceFieldSystem.NAME;
	}

	@Override
	public String getVersion()
	{
		return ModularForceFieldSystem.VERSION;
	}
}