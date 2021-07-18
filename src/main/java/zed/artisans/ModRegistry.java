package zed.artisans;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.command.CommandSource;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zed.artisans.commands.DisplayNBTCommand;
import zed.artisans.gui.button.OpenCreatorButton;
import zed.artisans.tabs.TabManager;

@Mod("artisans_tabs")
public class ModRegistry {
	private final TabManager tabManager;

	public ModRegistry() {
		this.tabManager = new TabManager();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::startClient);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::finalizeModSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void startClient(final FMLClientSetupEvent event) {
		final IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		this.tabManager.setupTabManager(resourceManager);
		((IReloadableResourceManager) resourceManager).addReloadListener(this.tabManager);
	}

	private void finalizeModSetup(final FMLLoadCompleteEvent event) {
		this.tabManager.finalizeTabManager();
	}

	@SubscribeEvent
	public void registerCommands(final RegisterCommandsEvent event) {
		final CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
		DisplayNBTCommand.register(dispatcher);
	}

	@SubscribeEvent
	public void onCreativeScreenInitialized(final GuiScreenEvent.InitGuiEvent.Post event) {
		final Screen screen = event.getGui();
		if (screen instanceof CreativeScreen) {
			event.addWidget(new OpenCreatorButton((CreativeScreen) screen));
		}
	}
}
