package wolforce.blocks.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import wolforce.Util;
import wolforce.recipes.RecipeBoxer;

public class TileBoxer extends TileEntity implements ITickable {

	@Override
	public void update() {

		if (world.isRemote || !world.isBlockPowered(pos) || world.isAirBlock(pos.up()))
			return;

		Block block = Util.blockAt(world, pos.up());
		Block result = RecipeBoxer.getResult(block);
		if (result != null) {
			world.destroyBlock(pos.up(), false);
			Util.spawnItem(world, pos.up(), new ItemStack(result, 64), 0.0, 0.2, 0.0);
		}
	}
}
