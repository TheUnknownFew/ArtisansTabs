package zed.artisans.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TabCreator extends DisplayEffectsScreen<TabCreator.TabCreatorContainer> {
	private static final int HOTBAR_LOC_Y = 112;
	private static final int SLOT_DIM = 18;
	private static final int X_SLOTS = 5;
	private static final int Y_SLOTS = 9;
	private static final int TOTAL_SLOTS = X_SLOTS * Y_SLOTS;
	private static final Inventory BUFFER_INV = new Inventory(TOTAL_SLOTS);
	private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");

	private boolean isFocused = false;
	private CreativeScreen creativeScreen;

	public TabCreator(final PlayerEntity player, final CreativeScreen creativeScreen) {
		super(new TabCreator.TabCreatorContainer(player), player.inventory, StringTextComponent.EMPTY);
		this.creativeScreen = creativeScreen;
		this.ySize = 136;
		this.xSize = 195;
	}

	@Override
	public void init(final Minecraft minecraft, final int width, final int height) {
		super.init(minecraft, width, height);
		this.creativeScreen.init(minecraft, width, height);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		this.container.slotClick(slotId, mouseButton, type, this.minecraft.player);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final MatrixStack matrixStack, final int x, final int y) {
		// tmp
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@OnlyIn(Dist.CLIENT)
	public static class TabCreatorContainer extends Container {
		protected TabCreatorContainer(final PlayerEntity player) {
			super(null, 0);
			for (int i = 0; i < TOTAL_SLOTS; i++) {
				final int row = i / Y_SLOTS;
				final int col = i % Y_SLOTS;
				this.addSlot(new Slot(BUFFER_INV, i, Y_SLOTS + col * SLOT_DIM, SLOT_DIM + row * SLOT_DIM));
			}
			for (int i = 0; i < Y_SLOTS; i++) {
				this.addSlot(new Slot(player.inventory, i, Y_SLOTS + i * SLOT_DIM, HOTBAR_LOC_Y));
			}
		}

		@Override
		public boolean canInteractWith(final PlayerEntity playerIn) {
			return true;
		}

		@Override
		public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
			if (slotId > 0 && slotId < TOTAL_SLOTS) {
				this.inventorySlots.get(slotId).putStack(ItemStack.EMPTY);
			}
			return super.slotClick(slotId, dragType, clickTypeIn, player);
		}
	}
}
