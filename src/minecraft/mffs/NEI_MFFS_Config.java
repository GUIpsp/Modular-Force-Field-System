package mffs;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_MFFS_Config implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		API.hideItem(ZhuYao.bLiQiang.blockID);

		MultiItemRange blocks = new MultiItemRange();
		MultiItemRange items = new MultiItemRange();
		MultiItemRange upgrades = new MultiItemRange();
		MultiItemRange modules = new MultiItemRange();

		blocks.add(ZhuYao.bChouQi);
		blocks.add(ZhuYao.bFangYu);
		blocks.add(ZhuYao.bAnQuan);
		blocks.add(ZhuYao.bDianRong);
		blocks.add(ZhuYao.bFangYingJi);
		blocks.add(ZhuYao.blockFortronite);

		// items.add(ZhuYao.itemMultiTool); items.add(ZhuYao.itemCardEmpty);
		items.add(ZhuYao.itemForcillium);
		items.add(ZhuYao.itFortronCell);
		items.add(ZhuYao.itFocusMatix);

		upgrades.add(ZhuYao.itMDian);
		upgrades.add(ZhuYao.itemModuleSponge);
		upgrades.add(ZhuYao.itemModuleManipulator);
		upgrades.add(ZhuYao.itemModuleDisintegration);
		upgrades.add(ZhuYao.itemModuleJammer);
		upgrades.add(ZhuYao.itemModuleCamouflage);
		upgrades.add(ZhuYao.itemModuleFusion);

		upgrades.add(ZhuYao.itMDaXiao);
		upgrades.add(ZhuYao.itMDong);

		modules.add(ZhuYao.itMYuan);
		modules.add(ZhuYao.itMFang);
		modules.add(ZhuYao.itMGuan);

		API.addSetRange("MFFS.Items.Upgrades", upgrades);
		API.addSetRange("MFFS.Items.Modules", modules);
		API.addSetRange("MFFS.Items", items);
		API.addSetRange("MFFS.Blocks", blocks);
	}

	@Override
	public String getName()
	{
		return ZhuYao.NAME;
	}

	@Override
	public String getVersion()
	{
		return ZhuYao.VERSION;
	}
}