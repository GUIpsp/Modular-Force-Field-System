package mffs.nei;

import mffs.common.ModularForceFieldSystem;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_MFFS_Config implements IConfigureNEI
{

	@Override
	public void loadConfig()
	{
		API.hideItem(ModularForceFieldSystem.itemMultiTool.itemID);

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
		blocks.add(ModularForceFieldSystem.blockFortronite);

		items.add(ModularForceFieldSystem.itemMultiTool);
		items.add(ModularForceFieldSystem.itemCardEmpty);
		items.add(ModularForceFieldSystem.itemPowerCrystal);
		items.add(ModularForceFieldSystem.itemForcillium);
		items.add(ModularForceFieldSystem.itemForcilliumCell);
		items.add(ModularForceFieldSystem.itemFocusMatix);
		items.add(ModularForceFieldSystem.itemCardInfinite);

		upgrades.add(ModularForceFieldSystem.itemUpgradeBoost);
		upgrades.add(ModularForceFieldSystem.itemUpgradeRange);
		upgrades.add(ModularForceFieldSystem.itemUpgradeCapacity);

		upgrades.add(ModularForceFieldSystem.itemModuleShock);
		upgrades.add(ModularForceFieldSystem.itemModuleSponge);
		upgrades.add(ModularForceFieldSystem.itemModuleManipulator);
		upgrades.add(ModularForceFieldSystem.itemModuleDisintegration);
		upgrades.add(ModularForceFieldSystem.itemModuleDefenseeStation);
		upgrades.add(ModularForceFieldSystem.itemModuleAntibiotic);
		upgrades.add(ModularForceFieldSystem.itemModuleJammer);
		upgrades.add(ModularForceFieldSystem.itemModuleCamouflage);
		upgrades.add(ModularForceFieldSystem.itemModuleFusion);

		upgrades.add(ModularForceFieldSystem.itemModuleScale);
		upgrades.add(ModularForceFieldSystem.itemModuleStrength);

		modules.add(ModularForceFieldSystem.itemModuleSphere);
		modules.add(ModularForceFieldSystem.itemModuleCube);
		modules.add(ModularForceFieldSystem.itemModuleWall);
		modules.add(ModularForceFieldSystem.itemModuleDeflector);
		modules.add(ModularForceFieldSystem.itemModuleTube);
		modules.add(ModularForceFieldSystem.itemModuleContainment);
		modules.add(ModularForceFieldSystem.itemModeAdvancedCube);

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