package zed.artisans.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class DisplayNBTCommand {
	private DisplayNBTCommand() {}

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			Commands.literal("nbt")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(DisplayNBTCommand::sendNBT)
		);
	}

	private static int sendNBT(final CommandContext<CommandSource> source) {
		final PlayerEntity player = (PlayerEntity) source.getSource().getEntity();
		if (player != null) {
			final CompoundNBT mainHandNbt = player.getHeldItemMainhand().serializeNBT();
			player.sendMessage(
				mainHandNbt.toFormattedComponent().deepCopy()
					.modifyStyle(
						style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, mainHandNbt.toString()))
							.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to copy to clipboard.")))
					),
				player.getUniqueID());
		}
		return 1;
	}
}
