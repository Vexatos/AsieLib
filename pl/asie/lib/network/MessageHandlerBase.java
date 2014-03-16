package pl.asie.lib.network;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class MessageHandlerBase implements IMessageHandler<Packet, Packet> {

	public abstract void onMessage(Packet packet, MessageContext ctx, int command) throws IOException;
	
	@Override
	public Packet onMessage(Packet packet, MessageContext ctx) {
		try {
			onMessage(packet, ctx, packet.readUnsignedShort());
		} catch(IOException e) {
			e.printStackTrace();
		}
		return packet;
	}

}
