package zed.artisans.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import zed.artisans.gui.TabCreator;

public class OpenCreatorButton extends ExtendedButton {
	private static final ITextComponent BLANK = new StringTextComponent("");
	private static final ITextComponent FOCUSED = new StringTextComponent("+");
	private static final int TAB_HEIGHT = 32;
	private static final int TAB_WIDTH = 28;
	private static final int COLUMN = 4;
	private static final int MIN_TOGGLE_HEIGHT = 5;
	private static final int MAX_TOGGLE_HEIGHT = 13;
	private static final int TOGGLE_WIDTH = 16;
	private final CreativeScreen currentCreativeScreen;

	public OpenCreatorButton(final CreativeScreen creativeScreen) {
		super(0, 0, TOGGLE_WIDTH, MIN_TOGGLE_HEIGHT, BLANK, OpenCreatorButton::switchTo);
		this.currentCreativeScreen = creativeScreen;
	}

	private static void switchTo(final Button toggle) {
		final CreativeScreen creativeScreen = ((OpenCreatorButton) toggle).currentCreativeScreen;
		creativeScreen.getMinecraft().displayGuiScreen(new TabCreator(creativeScreen.getMinecraft().player, creativeScreen));
	}

	@Override
	public void renderToolTip(final MatrixStack matrixStack, final int mouseX, final int mouseY) {
		this.currentCreativeScreen.renderTooltip(
			matrixStack,
			this.currentCreativeScreen.getMinecraft().fontRenderer.trimStringToWidth(new StringTextComponent("Open tab creator"), 50),
			mouseX, mouseY
		);
	}

	@Override
	public void renderWidget(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
		int buttonHeight = this.getHeight();
		boolean mousedOver = this.isMouseOver(mouseX, mouseY);

		this.setMessage((buttonHeight > MAX_TOGGLE_HEIGHT - 5) ? FOCUSED : BLANK);

		if (buttonHeight < MAX_TOGGLE_HEIGHT && mousedOver) {
			buttonHeight += 1;
			this.setHeight(buttonHeight);
		}
		else if (buttonHeight > MIN_TOGGLE_HEIGHT && !mousedOver) {
			buttonHeight -= 1;
			this.setHeight(buttonHeight);
		}

		this.x = (this.currentCreativeScreen.getGuiLeft() + TAB_WIDTH * COLUMN) + TAB_WIDTH / 4;
		this.y = (this.currentCreativeScreen.getGuiTop() + TAB_HEIGHT + 1) - (TAB_HEIGHT + buttonHeight);
		super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
		if (mousedOver)
			this.renderToolTip(matrixStack, mouseX, mouseY);
	}
}
