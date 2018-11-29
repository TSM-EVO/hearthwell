package wolforce.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IShearable;
import scala.collection.parallel.ParIterableLike.Drop;
import wolforce.Main;
import wolforce.MyItem;
import wolforce.Util;

public class ItemDisplacer extends MyItem {

	private boolean powered;

	public ItemDisplacer(String name, boolean powered, String... lore) {
		super(name, lore);
		this.powered = powered;
		setMaxStackSize(1);
		setMaxDamage(64);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20;
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		RayTraceResult raytraceresult = this.rayTrace(world, player, true);

		if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
			return new ActionResult<>(EnumActionResult.FAIL, stack);

		BlockPos pos = raytraceresult.getBlockPos();

		if (!world.isBlockModifiable(player, pos))
			return new ActionResult<>(EnumActionResult.FAIL, stack);

		Block block = world.getBlockState(pos).getBlock();
		if (canDisplace(world, player, pos)) {

			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {

		if (!(entityLiving instanceof EntityPlayer))
			return stack;

		EntityPlayer player = (EntityPlayer) entityLiving;
		RayTraceResult raytraceresult = this.rayTrace(world, player, true);

		if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
			return stack;

		BlockPos pos = raytraceresult.getBlockPos();

		if (!world.isBlockModifiable(player, pos))
			return stack;

		IBlockState state = world.getBlockState(pos);
		if (canDisplace(world, player, pos)) {

			player.playSound(SoundEvents.BLOCK_LAVA_POP, 1f, 1f);

			//
			// if (!world.isRemote) {
			// ItemStack drop = getSilkTouchDrop(state);
			// // new ItemStack(state.getBlock(), 1,
			// state.getBlock().getMetaFromState(state));
			// Util.spawnItem(world, pos, drop);

			if (state.getBlock() instanceof IShearable && ((IShearable) state.getBlock()).isShearable(stack, world, pos))
				for (ItemStack a : ((IShearable) state.getBlock()).onSheared(stack, world, pos, 0)) {
					if (!world.isRemote)
						Util.spawnItem(world, pos, a);
				}
			else
				state.getBlock().harvestBlock(world, player, pos, state, null, stack);

			world.destroyBlock(pos, false);
			stack.damageItem(1, player);
			// }

			player.getFoodStats().addExhaustion(-5);
		}
		return stack;
	}

	private boolean canDisplace(World world, EntityPlayer player, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().equals(Blocks.OBSIDIAN) || //
				state.getBlock().equals(Blocks.GLASS) || //
				state.getBlock().equals(Blocks.GLASS_PANE) || //
				state.getBlock().equals(Blocks.STAINED_GLASS) || //
				state.getBlock().equals(Blocks.STAINED_GLASS_PANE) || //
				(powered && // when powered can also silk harvest and shear
						(state.getBlock().canSilkHarvest(world, pos, world.getBlockState(pos), player)
								|| state.getBlock() instanceof IShearable));
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return false;
	}

}