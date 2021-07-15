package zed.artisans.tabs;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Tab {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final ItemStack ERR_ITEM = Items.BARRIER.getDefaultInstance();
	private final String fieldMissingError;
	private final String namespace;
	private final String fileName;

	// JSON field for the tab display name.
	public static final String DISPLAY_NAME = "display_name";
	private final String groupLabel;

	// JSON field for the tab item icon nbt.
	public static final String ICON = "icon_nbt";
	private final ItemStack tabIcon;

	// JSON field for the array of items in tab.
	public static final String ITEMS = "items";
	private final List<ItemStack> itemsInTab;

	// JSON field setting the tab as searchable or not.
	public static final String SEARCH = "search";
	private final boolean canSearch;

	// background image

	// JSON field for the color of the label text for tab.
	public static final String LABEL_COLOR = "label_color";
	private final int labelColor;

	// JSON field for the color of slots for tab.
	public static final String SLOT_COLOR = "slot_color";
	private final int slotColor;

	static {
		CompoundNBT loreTag = new CompoundNBT();
		ListNBT lore = new ListNBT();
		lore.addAll(
			Lists.newArrayList(
				StringNBT.valueOf("{\"text\":\"Error occurred with this item:\",\"color\":\"red\"}"),
				StringNBT.valueOf("{\"text\":\"Malformed JSON or Invalid Item\",\"color\":\"red\"}")
			)
		);
		loreTag.put("Lore", lore);
		ERR_ITEM.setTagInfo("display", loreTag);
	}

	public Tab(final String namespace, final String fileIn, final JsonObject tabSpecs) {
		this.namespace = namespace;
		this.fileName = fileIn;
		this.fieldMissingError = String.format("Tab spec '%s' from namespace '%s' is missing the field '%s'.", fileIn, namespace, "%s");
		this.groupLabel = this.ifExistsElse(this::setLabel, DISPLAY_NAME, String.format("%s.%s", this.namespace, "no_name"), tabSpecs);
		this.tabIcon = this.ifExistsElse(this::setIcon, ICON, ERR_ITEM, tabSpecs);
		this.itemsInTab = this.ifExistsElse(this::setItemsInTab, ITEMS, new ArrayList<>(), tabSpecs);
		this.canSearch = this.ifExistsElse(this::setCanSearch, SEARCH, false, tabSpecs);
		this.labelColor = this.ifExistsElse(this::setLabelColor, LABEL_COLOR, 4210752, tabSpecs);
		this.slotColor = this.ifExistsElse(this::setSlotColor, SLOT_COLOR, -2130706433, tabSpecs);
	}

	private <R> R ifExistsElse(final Function<JsonElement, R> doIf, final String jsonField, final R elseVal, final JsonObject tabSpecs) {
		if (tabSpecs.has(jsonField)) {
			return doIf.apply(tabSpecs.get(jsonField));
		}
		logMissingField(jsonField);
		return elseVal;
	}

	private ItemStack fromNbt(final String itemNbt) {
		try {
			final ItemStack itemStack = ItemStack.read(JsonToNBT.getTagFromJson(itemNbt));
			if (itemStack.isEmpty())
				return ERR_ITEM;
			return itemStack;
		} catch (CommandSyntaxException e) {
			LOGGER.error(
				String.format(
					"Icon for tab %s.%s in file %s has malformed Item NBT:%n\t%s",
					this.namespace, this.groupLabel, this.fileName, e.getMessage()
				)
			);
			return ERR_ITEM;
		}
	}

	private String setLabel(final JsonElement labelField) {
		return labelField.getAsString();
	}

	private ItemStack setIcon(final JsonElement iconField) {
		return this.fromNbt(iconField.toString());
	}

	private List<ItemStack> setItemsInTab(final JsonElement itemsField) {
		final List<ItemStack> items = new ArrayList<>();
		for (final JsonElement itemNbt : itemsField.getAsJsonArray()) {
			items.add(this.fromNbt(itemNbt.toString()));
		}
		return items;
	}

	private boolean setCanSearch(final JsonElement searchField) {
		return searchField.getAsBoolean();
	}

	private int setLabelColor(final JsonElement labelColorField) {
		return labelColorField.getAsInt();
	}

	private int setSlotColor(final JsonElement slotColorField) {
		return slotColorField.getAsInt();
	}

	public String getNamespace() {
		return namespace;
	}

	public String getFileName() {
		return fileName;
	}

	public String getGroupLabel() {
		return groupLabel;
	}

	public ItemStack getTabIcon() {
		return tabIcon;
	}

	public List<ItemStack> getItemsInTab() {
		return itemsInTab;
	}

	public boolean isCanSearch() {
		return canSearch;
	}

	public int getLabelColor() {
		return labelColor;
	}

	public int getSlotColor() {
		return slotColor;
	}

	private void logMissingField(final String field) {
		final String err = String.format(this.fieldMissingError, field);
		LOGGER.error(err);
	}
}
