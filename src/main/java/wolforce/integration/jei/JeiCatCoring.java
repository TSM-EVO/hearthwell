package wolforce.integration.jei;

import static net.minecraft.init.Blocks.BROWN_MUSHROOM_BLOCK;
import static net.minecraft.init.Blocks.MELON_BLOCK;
import static net.minecraft.init.Blocks.RED_MUSHROOM_BLOCK;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;
import wolforce.Main;
import wolforce.Util;
import wolforce.blocks.BlockCore;
import wolforce.integration.jei.JeiCatCoring.JeiRecCoring;
import wolforce.recipes.Iri;
import wolforce.recipes.RecipeCoring;

public class JeiCatCoring<T extends JeiRecCoring> implements IRecipeCategory<JeiRecCoring> {

	public static final String UID_CORING_STONE = Main.MODID + ".coring.stone";
	public static final String UID_CORING_HEAT = Main.MODID + ".coring.heat";
	public static final String UID_CORING_GREEN = Main.MODID + ".coring.green";
	public static final String UID_CORING_SENTIENT = Main.MODID + ".coring.sentient";

	static final ResourceLocation TEX = Util.res("textures/gui/coring.png");
	static IDrawableStatic back;

	private IDrawable icon;
	private BlockCore core;

	public JeiCatCoring(IJeiHelpers helpers, BlockCore core) {

		final IGuiHelper gui = helpers.getGuiHelper();

		this.core = core;
		back = gui.drawableBuilder(TEX, 0, 0, 62, 90).setTextureSize(62, 90).build();
		icon = gui.createDrawableIngredient(new ItemStack(core));
	}

	@Override
	public String getUid() {
		if (core == Main.core_stone)
			return UID_CORING_STONE;
		if (core == Main.core_heat)
			return UID_CORING_HEAT;
		if (core == Main.core_green)
			return UID_CORING_GREEN;
		return UID_CORING_SENTIENT;
	}

	@Override
	public String getTitle() {
		return "Coring Recipes";
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public String getModName() {
		return Main.MODID;
	}

	@Override
	public IDrawable getBackground() {
		return back;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JeiRecCoring recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, false, 22, 64);
		stacks.init(1, true, 8, 8);
		stacks.init(2, true, 36, 8);
		stacks.init(3, true, 8, 34);
		stacks.set(ingredients);
	}

	//

	//

	//

	//

	//

	public static List<JeiRecCoring> getAllRecipes(BlockCore core) {
		List<JeiRecCoring> list = new LinkedList<JeiRecCoring>();
		for (Entry<Item, RecipeCoring> entry : RecipeCoring.getRecipeList(core)) {
			JeiRecCoring recipe = new JeiRecCoring(core, entry.getKey(), entry.getValue().consumes, entry.getValue().result);
			System.out.println("added jei core " + core.getLocalizedName() + " recipe: " + recipe);
			list.add(recipe);
		}
		// SPECIAL CASE
		if (core == Main.core_green)
			list.add(new JeiRecCoring(core, Main.shard_p, new Iri[] { new Iri(MELON_BLOCK) },
					new Iri[] { new Iri(BROWN_MUSHROOM_BLOCK), new Iri(RED_MUSHROOM_BLOCK) }));
		return list;
	}

	public static class JeiRecCoring implements IRecipeWrapper {

		private List<ItemStack> shard;
		private List<ItemStack> consumes;
		private List<ItemStack> coretype;
		private List<ItemStack> out;

		public JeiRecCoring(Block coretype, Item shard, Iri[] consumes, Iri result) {
			this(coretype, shard, consumes, new Iri[] { result });
		}

		public JeiRecCoring(Block coretype, Item shard, Iri[] consumes, Iri[] result) {
			this.coretype = new LinkedList<>();
			this.coretype.add(new ItemStack(coretype));
			this.shard = new LinkedList<>();
			this.shard.add(new ItemStack(shard));

			this.consumes = new LinkedList<>();
			for (Iri iri : consumes)
				this.consumes.add(new ItemStack(iri.item, 1, iri.meta));

			this.out = new LinkedList<>();
			for (Iri iri : result)
				this.out.add(new ItemStack(iri.item));
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			List<List<ItemStack>> ins = new ArrayList<>(4);
			ins.add(coretype);
			ins.add(shard);
			ins.add(consumes);
			ingredients.setInputLists(ItemStack.class, ins);
			ingredients.setOutput(ItemStack.class, out);
		}

		@Override
		public String toString() {
			return "[ " + shard.get(0).getUnlocalizedName() + " -> " + out.get(0).getUnlocalizedName() + " consuming "
					+ consumes.size() + " ]";
		}
	}

}
