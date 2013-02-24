package mffs.nei;

import mffs.common.ModularForceFieldSystem;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_MFFS_Config implements IConfigureNEI
{
	public void loadConfig()
	{
		API.hideItem(ModularForceFieldSystem.itemMultiToolSwitch.itemID);
		API.hideItem(ModularForceFieldSystem.itemMultiToolFieldTeleporter.itemID);
		API.hideItem(ModularForceFieldSystem.itemMultiToolID.itemID);
		API.hideItem(ModularForceFieldSystem.itemMultiToolManual.itemID);

		API.hideItem(ModularForceFieldSystem.itemCardPowerLink.itemID);
		API.hideItem(ModularForceFieldSystem.itemCardID.itemID);
		API.hideItem(ModularForceFieldSystem.itemCardSecurityLink.itemID);
		API.hideItem(ModularForceFieldSystem.blockForceField.blockID);
		API.hideItem(ModularForceFieldSystem.itemCardAccess.itemID);
		API.hideItem(ModularForceFieldSystem.itemCardDataLink.itemID);

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

		items.add(ModularForceFieldSystem.itemMultiToolWrench);
		items.add(ModularForceFieldSystem.itemCardEmpty);
		items.add(ModularForceFieldSystem.itemPowerCrystal);
		items.add(ModularForceFieldSystem.itemForcicium);
		items.add(ModularForceFieldSystem.itemForcicumCell);
		items.add(ModularForceFieldSystem.itemFocusMatix);
		items.add(ModularForceFieldSystem.itemCardInfinite);

		upgrades.add(ModularForceFieldSystem.itemUpgradeBoost);
		upgrades.add(ModularForceFieldSystem.itemUpgradeRange);
		upgrades.add(ModularForceFieldSystem.itemUpgradeCapacity);

		upgrades.add(ModularForceFieldSystem.itemOptionShock);
		upgrades.add(ModularForceFieldSystem.itemOptionSponge);
		upgrades.add(ModularForceFieldSystem.itemOptionFieldManipulator);
		upgrades.add(ModularForceFieldSystem.itemOptionCutter);
		upgrades.add(ModularForceFieldSystem.itemOptionDefenseeStation);
		upgrades.add(ModularForceFieldSystem.itemOptionAntibiotic);
		upgrades.add(ModularForceFieldSystem.itemOptionJammer);
		upgrades.add(ModularForceFieldSystem.itemOptionCamouflage);
		upgrades.add(ModularForceFieldSystem.itemOptionFieldFusion);

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