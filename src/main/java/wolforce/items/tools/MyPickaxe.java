package wolforce.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import wolforce.Util;

public class MyPickaxe extends ItemPickaxe {

	private ItemStack repairIngot;

	public MyPickaxe(String name, ToolMaterial mat, Item repairIngot) {
		super(mat);
		this.repairIngot = new ItemStack(repairIngot);
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if (Util.isValid(repairIngot) && net.minecraftforge.oredict.OreDictionary.itemMatches(repairIngot, repair, false))
			return true;
		return super.getIsRepairable(toRepair, repair);
	}
}
