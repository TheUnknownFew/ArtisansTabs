package zed.artisans.tabs;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TabData {

	private static final Logger LOGGER = LogManager.getLogger();

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

//	// background image TODO: Add this
//	public static final String BACKGROUND = "background";
//	private final ResourceLocation backgroundImage;
//
//	//tab image TODO: Add this
//	public static final String TAB_IMAGE = "tab_img";
//	private final ResourceLocation tabImage;

	// JSON field for the color of the label text for tab.
	public static final String LABEL_COLOR = "label_color";
	private final int labelColor;

	// JSON field for the color of slots for tab.
	public static final String SLOT_COLOR = "slot_color";
	private final int slotColor;

	public static final Codec<ItemStack> ITEM_CODEC = CompoundNBT.CODEC.xmap(ItemStack::read, IForgeItemStack::serializeNBT);
	public static final Codec<TabData> CODEC = RecordCodecBuilder.create(
		tabInstance -> tabInstance.group(
			Codec.STRING.fieldOf(DISPLAY_NAME).forGetter(i -> i.groupLabel),
			ITEM_CODEC.fieldOf(ICON).forGetter(i -> i.tabIcon),
			ITEM_CODEC.listOf().fieldOf(ITEMS).forGetter(i -> i.itemsInTab),
			Codec.BOOL.optionalFieldOf(SEARCH, false).forGetter(i -> i.canSearch),
			Codec.INT.optionalFieldOf(LABEL_COLOR, 4210752).forGetter(i -> i.labelColor),
			Codec.INT.optionalFieldOf(SLOT_COLOR, -2130706433).forGetter(i -> i.slotColor)
		).apply(tabInstance, TabData::new)
	);

	private TabData(final String groupLabel, final ItemStack tabIcon, final List<ItemStack> itemsInTab, final boolean canSearch, final int labelColor, final int slotColor) {
		this.groupLabel = groupLabel;
		this.tabIcon = tabIcon;
		this.itemsInTab = itemsInTab;
		this.canSearch = canSearch;
		this.labelColor = labelColor;
		this.slotColor = slotColor;
	}

	public String getLabel() {
		return this.groupLabel;
	}

	public ItemStack getIcon() {
		return this.tabIcon;
	}

	public List<ItemStack> getItemsInTab() {
		return this.itemsInTab;
	}

	public boolean hasSearchBar() {
		return this.canSearch;
	}

	public int getLabelColor() {
		return this.labelColor;
	}

	public int getSlotColor() {
		return this.slotColor;
	}

	public static TabData parseFromJson(final JsonElement e) {
		return CODEC.decode(JsonOps.INSTANCE, e).getOrThrow(false, LOGGER::error).getFirst();
	}

	public JsonElement toJson() {
		return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, LOGGER::error);
	}
}
