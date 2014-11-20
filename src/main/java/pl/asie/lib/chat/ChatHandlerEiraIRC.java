package pl.asie.lib.chat;

import cpw.mods.fml.common.Optional;
import net.blay09.mods.eirairc.api.event.RelayChat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class ChatHandlerEiraIRC {
	@Optional.Method(modid="EiraIRC|API")
	protected static void eiraircRelay(EntityPlayerMP player, String username,
			String message) {
		MinecraftForge.EVENT_BUS.post(new RelayChat(new CommandSenderDummy(player, username), message));
	}
}
