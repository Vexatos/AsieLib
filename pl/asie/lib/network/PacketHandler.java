package pl.asie.lib.network;

import java.io.IOException;

import pl.asie.lib.util.PlayerUtils;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.network.INetHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class PacketHandler extends SimpleNetworkWrapper {
	public PacketHandler(String channelName) {
		super(channelName);
	}

	public Packet create() {
		return new Packet();
	}
	
	public Packet create(int prefix) throws IOException {
		return new Packet().writeShort((short)prefix);
	}
	
	public <REQ extends IMessage, REPLY extends IMessage> void registerClient(Class<? extends IMessageHandler<REQ, REPLY>> handler) {
		this.registerMessage(handler, (Class<REQ>)Packet.class, (byte)1, Side.CLIENT);
	}

	public <REQ extends IMessage, REPLY extends IMessage> void registerServer(Class<? extends IMessageHandler<REQ, REPLY>> handler) {
		this.registerMessage(handler, (Class<REQ>)Packet.class, (byte)1, Side.SERVER);
	}

	public void sendToAllAround(Packet packet, TileEntity entity,
			double d) {
		this.sendToAllAround(packet, new TargetPoint(entity.getWorldObj().provider.dimensionId, entity.xCoord, entity.yCoord, entity.zCoord, d));
	}
}
