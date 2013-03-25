package mffs;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_MFFS_Config implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		API.hideItem(ZhuYao.itemMultiTool.itemID);
		API.hideItem(ZhuYao.blockForceField.blockID);

		MultiItemRange blocks = new MultiItemRange();
		MultiItemRange items = new MultiItemRange();
		MultiItemRange upgrades = new MultiItemRange();
		MultiItemRange modules = new MultiItemRange();

		blocks.add(ZhuYao.blockExtractor);
		blocks.add(ZhuYao.blockDefenceStation);
		blocks.add(ZhuYao.blockSecurityStation);
		blocks.add(ZhuYao.blockCapacitor);
		blocks.add(ZhuYao.blockProjector);
		blocks.add(ZhuYao.blockFortronite);

		// items.add(ZhuYao.itemMultiTool); items.add(ZhuYao.itemCardEmpty);
		items.add(ZhuYao.itemForcillium);
		items.add(ZhuYao.itemFortronCell);
		items.add(ZhuYao.itemFocusMatix);

		upgrades.add(ZhuYao.itemModuleShock);
		upgrades.add(ZhuYao.itemModuleSponge);
		upgrades.add(ZhuYao.itemModuleManipulator);
		upgrades.add(ZhuYao.itemModuleDisintegration);
		upgrades.add(ZhuYao.itemModuleJammer);
		upgrades.add(ZhuYao.itemModuleCamouflage);
		upgrades.add(ZhuYao.itemModuleFusion);

		upgrades.add(ZhuYao.itemModuleScale);
		upgrades.add(ZhuYao.itemModuleTranslation);

		modules.add(ZhuYao.itemModuleSphere);
		modules.add(ZhuYao.itemModuleCube);
		modules.add(ZhuYao.itemModuleTube);

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