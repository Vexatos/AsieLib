package pl.asie.lib;

import java.io.IOException;

import pl.asie.lib.network.MessageHandlerBase;
import pl.asie.lib.network.Packet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandlerClient extends MessageHandlerBase implements IMessageHandler<Packet, Packet> {
	@Override
	public void onMessage(Packet packet, MessageContext ctx, int command)
			throws IOException {
		switch(command) {
			case Packets.NICKNAME_CHANGE:
				String username = packet.readString();
				String nickname = packet.readString();
				AsieLibMod.nick.setNickname(username, nickname);
				break;
			case Packets.NANO_NANO:
				AsieLibMod.keyClient.scheduleSpin();
				break;
		}
	}
}
