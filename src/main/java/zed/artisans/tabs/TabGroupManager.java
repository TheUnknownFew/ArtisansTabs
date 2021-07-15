package zed.artisans.tabs;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.EmptyProfiler;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public class TabGroupManager extends JsonReloadListener {
	// Resource Pack Structure:
	// assets/artisans_tabs/lang/en_us.lang # Contains the translations for the tab names
	// assets/artisans_tabs/tabs/tabs.json  # Contains teh json specifications for creative tabs in the resource pack.

	private static final Logger LOGGER = LogManager.getLogger();
	private Map<ResourceLocation, JsonElement> loadedTabPacks;
	private int defaultTabCount;
	private boolean isSetup;
	private boolean isFinalized;


	public TabGroupManager() {
		// super param #1: GSON Builder, super param #2: Resource folder location
		super((new GsonBuilder()).setPrettyPrinting().create(), "tabset");
		this.defaultTabCount = -1;
		this.isSetup = false;
		this.isFinalized = false;
	}

	@Override
	protected void apply(final Map<ResourceLocation, JsonElement> objectIn, final IResourceManager resourceManagerIn, final IProfiler profilerIn) {
		this.loadedTabPacks = objectIn;
		if (isFinalized)
			this.makeTabs();
	}

	public void setupTabManager(final IResourceManager resourceManager) {
		if (!isSetup) {
			this.apply(super.prepare(resourceManager, EmptyProfiler.INSTANCE), resourceManager, EmptyProfiler.INSTANCE);
			this.isSetup = true;
		}
	}

	public void finalizeTabManager() {
		if (!isFinalized) {
			this.defaultTabCount = ItemGroup.getGroupCountSafe();
			this.makeTabs();
			this.isFinalized = true;
		}
	}

	private void makeTabs() {
		resetTabsToDefault(this.defaultTabCount);
		for (final Entry<ResourceLocation, JsonElement> tabSpec : this.loadedTabPacks.entrySet()) {
			final ResourceLocation key = tabSpec.getKey();
			new TabGroup(new Tab(key.getNamespace(), key.getPath(), tabSpec.getValue().getAsJsonObject()));
		}
	}

	private static synchronized void resetTabsToDefault(final int defaultTabCount) {
		ItemGroup.GROUPS = Arrays.copyOfRange(ItemGroup.GROUPS, 0, defaultTabCount);
	}
}
