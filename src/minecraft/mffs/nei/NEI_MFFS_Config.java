package mffs.nei;

import mffs.common.ModularForceFieldSystem;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_MFFS_Config implements IConfigureNEI
{
	public void loadConfig()
	{
		API.hideItem(ModularForceFieldSystem.itemSwitch.itemID);
		API.hideItem(ModularForceFieldSystem.itemFieldTeleporter.itemID);
		API.hideItem(ModularForceFieldSystem.itemMFDidtool.itemID);
		API.hideItem(ModularForceFieldSystem.MFFSitemManuelBook.itemID);

		API.hideItem(ModularForceFieldSystem.MFFSitemfc.itemID);
		API.hideItem(ModularForceFieldSystem.MFFSItemIDCard.itemID);
		API.hideItem(ModularForceFieldSystem.MFFSItemSecLinkCard.itemID);
		API.hideItem(ModularForceFieldSystem.blockForceField.blockID);
		API.hideItem(ModularForceFieldSystem.MFFSAccessCard.itemID);
		API.hideItem(ModularForceFieldSystem.MFFSitemDataLinkCard.itemID);

		MultiItemRange blocks = new MultiItemRange();
		MultiItemRange items = new MultiItemRange();
		MultiItemRange upgrades = new MultiItemRange();
		MultiItemRange modules = new MultiItemRange();

		blocks.add(ModularForceFieldSystem.blockConverter);
		blocks.add(ModularForceFieldSystem.blockExtractor);
		blocks.add(ModularForceFieldSystem.blockDefenceStation);
		blocks.add(ModularForceFieldSystem.blockSecurityStation);
		blocks.add(ModularForceFieldSystem.blockCapacitor);
		blocks.add(ModularForceFieldSystem.blockProjector);
		blocks.add(ModularForceFieldSystem.blockSecurityStorage);
		blocks.add(ModularForceFieldSystem.blockMonaziteOre);

		items.add(ModularForceFieldSystem.itemWrench);
		items.add(ModularForceFieldSystem.MFFSitemMFDdebugger);
		items.add(ModularForceFieldSystem.MFFSitemcardempty);
		items.add(ModularForceFieldSystem.itemPowerCrystal);
		items.add(ModularForceFieldSystem.itemForcicium);
		items.add(ModularForceFieldSystem.itemForcicumCell);
		items.add(ModularForceFieldSystem.itemFocusMatix);
		items.add(ModularForceFieldSystem.MFFSitemInfinitePowerCard);

		upgrades.add(ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		upgrades.add(ModularForceFieldSystem.MFFSitemupgradecaprange);
		upgrades.add(ModularForceFieldSystem.MFFSitemupgradecapcap);

		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionZapper);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionSubwater);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionDome);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionCutter);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionDefenceStation);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionMoobEx);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionCamouflage);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionFieldFusion);

		upgrades.add(ModularForceFieldSystem.itemModuleDistance);
		upgrades.add(ModularForceFieldSystem.itemModuleStrength);

		modules.add(ModularForceFieldSystem.itemModuleSphere);
		modules.add(ModularForceFieldSystem.itemModuleCube);
		modules.add(ModularForceFieldSystem.itemModuleWall);
		modules.add(ModularForceFieldSystem.itemModuleDeflector);
		modules.add(ModularForceFieldSystem.itemModuleTube);
		modules.add(ModularForceFieldSystem.itemModuleContainment);
		modules.add(ModularForceFieldSystem.itemModuleAdvancedCube);

		API.addSetRange("MFFS.Items.Upgrades", upgrades);
		API.addSetRange("MFFS.Items.Modules", modules);
		API.addSetRange("MFFS.Items", items);
		API.addSetRange("MFFS.Blocks", blocks);
	}

	public String getName()
	{
		return ModularForceFieldSystem.NAME;
	}

	public String getVersion()
	{
		return ModularForceFieldSystem.VERSION;
	}
}