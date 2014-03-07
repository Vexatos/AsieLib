package pl.asie.lib.chat;

import pl.asie.lib.AsieLibMod;
import pl.asie.lib.Packets;
import pl.asie.lib.api.chat.INicknameHandler;
import pl.asie.lib.network.PacketInput;
import pl.asie.lib.util.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class NicknameNetworkHandler implements INicknameHandler, IConnectionHandler {
	
	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler,
			INetworkManager manager) {
		if(player instanceof EntityPlayer)
			AsieLibMod.nick.updateNickname(((EntityPlayer)player).username);
		
		for(EntityPlayer e: PlayerUtils.iterator()) {
			if(e == null) continue;
			String username = e.username;
			String nickname = AsieLibMod.nick.getNickname(username);
			if(!nickname.equals(username))
				sendNicknamePacket(username, nickname, player);
		}
	}
	
	private void sendNicknamePacket(String realname, String nickname, Player target) {
		try {
			PacketInput packet = AsieLibMod.packet.create(Packets.NICKNAME_CHANGE)
					.writeString(realname)
					.writeString(nickname);
			if(target == null)
				AsieLibMod.packet.sendToAllPlayers(packet);
			else
				AsieLibMod.packet.sendToPlayer(target, packet);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onNicknameUpdate(String realname, String nickname) {
		sendNicknamePacket(realname, nickname, null);
	}

	// We don't care.
	
	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			INetworkManager manager, Packet1Login login) {
	}
}
