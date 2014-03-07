package pl.asie.lib;

import java.io.IOException;

import pl.asie.lib.network.NetworkHandlerBase;
import pl.asie.lib.network.PacketOutput;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler extends NetworkHandlerBase implements IPacketHandler {
	@Override
	public void handlePacket(PacketOutput packet, int command, Player player,
			boolean isClient) throws IOException {
		switch(command) {
			case Packets.NICKNAME_CHANGE:
				String username = packet.readString();
				String nickname = packet.readString();
				if(isClient) AsieLibMod.nick.setNickname(username, nickname);
				break;
		}
	}
}
