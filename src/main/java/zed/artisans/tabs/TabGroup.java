package zed.artisans.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TabGroup extends ItemGroup {
	private final TabData tabData;

	public TabGroup(final TabData tabData) {
		super(tabData.getLabel());
		this.tabData = tabData;
	}

	@Override
	public ItemStack createIcon() {
		return this.tabData.getIcon();
	}

	@Override
	public void fill(NonNullList<ItemStack> items) {
		items.addAll(this.tabData.getItemsInTab());
	}

	@Override
	public boolean hasSearchBar() {
		return this.tabData.hasSearchBar();
	}

	@Override
	public int getLabelColor() {
		return this.tabData.getLabelColor();
	}

	@Override
	public int getSlotColor() {
		return this.tabData.getSlotColor();
	}
}
