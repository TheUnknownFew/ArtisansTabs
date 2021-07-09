package zed.artisans.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class TabGroup extends ItemGroup {

	private final List<ItemStack> itemsInGroup;

	public TabGroup(final String label) {
		super(label);
		this.itemsInGroup = new ArrayList<>();
	}

	@Override
	public ItemStack createIcon() {
		return null;
	}

	@Override
	public void fill(NonNullList<ItemStack> items) {
		items.addAll(this.itemsInGroup);
	}
}
