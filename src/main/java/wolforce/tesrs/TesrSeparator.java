package wolforce.tesrs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wolforce.Util;
import wolforce.blocks.BlockSeparator;
import wolforce.tile.TileSeparator;
import static net.minecraft.util.EnumFacing.*;

import net.minecraft.block.Block;

public class TesrSeparator extends TileEntitySpecialRenderer<TileSeparator> {

	@Override
	public void render(TileSeparator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		IItemHandler itemh = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		ItemStack stack = itemh.getStackInSlot(0);

		// double i = -.0625 * Util.getNrForDebugFromHand(te.getWorld(), x, y, z);
		// double j = -.0625 * Util.getNrForDebugFromHand2(te.getWorld(), x, y, z);

		if (stack != null && Util.canRenderTESR(te)) {
			EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockSeparator.FACING);
			double d = .25;
			double dx = facing == EnumFacing.WEST ? d : (facing == EnumFacing.EAST ? -d : 0);
			double dz = facing == EnumFacing.NORTH ? d : (facing == EnumFacing.SOUTH ? -d : 0);
			double ryy = facing == NORTH ? 180 : facing == SOUTH ? 0 : facing == WEST ? 90 : 270;
			double sy = Block.getBlockFromItem(stack.getItem()).equals(Blocks.AIR) ? 1 : .125;
			Util.renderItem(0, 0, te.getWorld(), stack, x + dx, y + .125, z + dz, 90, 0, ryy, 1, 1, sy);
		}
	}
}
