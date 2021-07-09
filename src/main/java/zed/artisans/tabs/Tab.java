package zed.artisans.tabs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Tab {

	private static final Logger LOGGER = LogManager.getLogger();
	private final String fieldMissingError;
	private final String namespace;
	private final String fileName;
	// JSON field for the tab display name.
	public static final String DISPLAY_NAME = "display_name";
	private final String groupLabel; //JSON FIELD: display_name
	// JSON field for the tab item icon nbt.
	public static final String ICON = "icon_nbt";
	private final ItemStack tabIcon;
	// Items
	public static final String ITEMS = "items";
	private final List<ItemStack> itemsInTab;
	// has search bar
	// background image
	// label color
	// slot color

	public Tab(final String namespace, final String fileIn, final JsonObject tabSpecs) {
		this.namespace = namespace;
		this.fileName = fileIn;
		this.fieldMissingError = String.format("Tab spec '%s' from namespace '%s' is missing the field '%s'.", fileIn, namespace, "%s");
		this.groupLabel = this.makeLabel(tabSpecs);
		this.tabIcon = this.makeIcon(tabSpecs);
		this.itemsInTab = this.makeItemsInTab(tabSpecs);
	}

	private String makeLabel(final JsonObject tabSpecs) {
		if (!tabSpecs.has(DISPLAY_NAME)) {
			logMissingField(DISPLAY_NAME);
			return String.format("%s.%s", this.namespace, "No_Display_Name");
		}
		return tabSpecs.get(DISPLAY_NAME).getAsString();
	}

	private ItemStack makeIcon(final JsonObject tabSpecs) {
		final ItemStack errItem = Items.BARRIER.getDefaultInstance();
		if (!tabSpecs.has(ICON)) {
			logMissingField(ICON);
			return errItem;
		}
		final String nbtString = tabSpecs.get(ICON).getAsJsonObject().toString();
		try {
			final ItemStack iconItem = ItemStack.read(JsonToNBT.getTagFromJson(nbtString));
			if (iconItem.isEmpty()) {
				return errItem;
			}
			return iconItem;
		}
		catch (CommandSyntaxException e) {
			LOGGER.error(
				String.format(
					"Icon for tab %s.%s in file %s has malformed Item NBT:%n\t%s",
					this.namespace, this.groupLabel, this.fileName, e.getMessage()
				)
			);
		}
		return errItem;
	}

	private List<ItemStack> makeItemsInTab(final JsonObject tabSpecs) {
		if (!tabSpecs.has(ITEMS)) {
			logMissingField(ITEMS);
			return new ArrayList<>();
		}
		for (final JsonElement item : tabSpecs.get(ITEMS).getAsJsonArray()) {
			item.toString();
		}

		return new ArrayList<>();
	}

	private void logMissingField(final String field) {
		final String err = String.format(fieldMissingError, field);
		LOGGER.error(err);
	}
}
