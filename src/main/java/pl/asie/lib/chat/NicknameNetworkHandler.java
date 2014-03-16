package pl.asie.lib.chat;

import pl.asie.lib.AsieLibMod;
import pl.asie.lib.Packets;
import pl.asie.lib.api.chat.INicknameHandler;
import pl.asie.lib.network.Packet;
import pl.asie.lib.util.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

public class NicknameNetworkHandler implements INicknameHandler {
	
	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayer)
			AsieLibMod.nick.updateNickname(((EntityPlayer)event.player).getCommandSenderName());
		
		for(Object o: MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(o == null || !(o instanceof EntityPlayer)) continue;
			EntityPlayer e = (EntityPlayer)o;
			String username = e.getCommandSenderName();
			String nickname = AsieLibMod.nick.getNickname(username);
			if(!nickname.equals(username))
				sendNicknamePacket(username, nickname, event.player);
		}
	}
	
	private void sendNicknamePacket(String realname, String nickname, EntityPlayer target) {
		try {
			Packet packet = AsieLibMod.packet.create(Packets.NICKNAME_CHANGE)
					.writeString(realname)
					.writeString(nickname);
			if(target == null)
				AsieLibMod.packet.sendToAll(packet);
			else
				AsieLibMod.packet.sendTo(packet, (EntityPlayerMP)target);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onNicknameUpdate(String realname, String nickname) {
		sendNicknamePacket(realname, nickname, null);
	}
}
