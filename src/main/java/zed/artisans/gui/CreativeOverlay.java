package zed.artisans.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class CreativeOverlay extends Widget {
	public CreativeOverlay(int x, int y, int width, int height, ITextComponent title) {
		super(x, y, width, height, title);
	}

//	private static final ITextComponent BLANK = new StringTextComponent("");
//	private static final ITextComponent FOCUSED = new StringTextComponent("+");
//	private static final int TAB_HEIGHT = 32;
//	private static final int TAB_WIDTH = 28;
//	private static final int COLUMN = 4;
//	private static final int MIN_TOGGLE_HEIGHT = 5;
//	private static final int MAX_TOGGLE_HEIGHT = 13;
//	private static final int TOGGLE_WIDTH = 16;
//	private final ExtendedButton toggleCreator;
//
//	public CreativeOverlay() {
//		super(0, 0, 0, 0, BLANK);
//		this.toggleCreator = new ExtendedButton(0, 0, TOGGLE_WIDTH, MIN_TOGGLE_HEIGHT, BLANK, this::switchTo);
//	}
//
//	private void switchTo(final Button toggle) {
//		Minecraft.getInstance().displayGuiScreen(this.tabCreator);
//	}
//
//	@Override
//	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//		int buttonHeight = this.toggleCreator.getHeight();
//		boolean mousedOver = this.toggleCreator.isMouseOver(mouseX, mouseY);
//
//		if (buttonHeight > MAX_TOGGLE_HEIGHT - 5) {
//			this.toggleCreator.setMessage(FOCUSED);
//		}
//		else {
//			this.toggleCreator.setMessage(BLANK);
//		}
//
//		if (buttonHeight < MAX_TOGGLE_HEIGHT && mousedOver) {
//			buttonHeight += 1;
//			toggleCreator.setHeight(buttonHeight);
//		}
//		else if (buttonHeight > MIN_TOGGLE_HEIGHT && !mousedOver) {
//			buttonHeight -= 1;
//			toggleCreator.setHeight(buttonHeight);
//		}
//
//		this.toggleCreator.x = (currentCreativeScreen.getGuiLeft() + TAB_WIDTH * COLUMN) + TAB_WIDTH / 4;
//		this.toggleCreator.y = (currentCreativeScreen.getGuiTop() + TAB_HEIGHT + 1) - (TAB_HEIGHT + buttonHeight);
//		this.toggleCreator.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
//	}
}
