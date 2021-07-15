package zed.artisans;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zed.artisans.commands.DisplayNBTCommand;
import zed.artisans.tabs.TabGroupManager;

@Mod("artisans_tabs")
public class ModRegistry {
	private final TabGroupManager tabGroupManager;

	public ModRegistry() {
		this.tabGroupManager = new TabGroupManager();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::startClient);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::finalizeModSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void startClient(final FMLClientSetupEvent event) {
		IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		this.tabGroupManager.setupTabManager(resourceManager);
		((IReloadableResourceManager) resourceManager).addReloadListener(this.tabGroupManager);
	}

	private void finalizeModSetup(final FMLLoadCompleteEvent event) {
		this.tabGroupManager.finalizeTabManager();
	}

	@SubscribeEvent
	public void registerCommands(final RegisterCommandsEvent event) {
		DisplayNBTCommand.register(event.getDispatcher());
	}
}
