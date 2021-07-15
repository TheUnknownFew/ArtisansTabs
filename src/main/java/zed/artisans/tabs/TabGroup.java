package zed.artisans.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TabGroup extends ItemGroup {

	private final Tab tab;

	public TabGroup(final Tab tab) {
		super(tab.getGroupLabel());
		this.tab = tab;
	}

	@Override
	public ItemStack createIcon() {
		return tab.getTabIcon();
	}

	@Override
	public void fill(NonNullList<ItemStack> items) {
		items.addAll(this.tab.getItemsInTab());
	}

	@Override
	public boolean hasSearchBar() {
		return tab.isCanSearch();
	}

	@Override
	public int getLabelColor() {
		return tab.getLabelColor();
	}

	@Override
	public int getSlotColor() {
		return tab.getSlotColor();
	}
}
