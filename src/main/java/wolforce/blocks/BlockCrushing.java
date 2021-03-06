package wolforce.blocks;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wolforce.recipes.RecipeCrushing;

public class BlockCrushing extends BlockFalling {

	public BlockCrushing(String name) {
		super();
		setUnlocalizedName(name);
		setRegistryName(name);
		setSoundType(SoundType.STONE);
		setHardness(1f);
		setHarvestLevel("pickaxe", -1);
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {

		List<EntityItem> entities = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));
		HashMap<ItemStack, Integer> results = new HashMap();
		for (EntityItem entityItem : entities) {
			Iterable<ItemStack> result = RecipeCrushing.getResult(entityItem.getItem());
			if (result != null) {
				entityItem.setDead();
				for (ItemStack itemStack : result) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemStack));
				}
			}
		}

	}
}
