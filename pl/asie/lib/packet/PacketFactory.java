package pl.asie.lib.packet;

import java.io.IOException;

import pl.asie.lib.util.PlayerUtils;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class PacketFactory {
	private final String channel;
	
	public PacketFactory(String prefix) {
		this.channel = prefix;
	}
	
	public PacketInput create() {
		return new PacketInput(channel);
	}
	
	public PacketInput create(int prefix) throws IOException {
		return new PacketInput(channel).writeShort((short)prefix);
	}
	
	// Boilerplate methods from PacketDispatcher and whatnot
	
	public void sendToPlayer(Player player, PacketInput packet) {
		PacketDispatcher.sendPacketToPlayer(packet.toPacket(), player);
	}
	
	public void sendToPlayer(String player, PacketInput packet) throws RuntimeException {
		Player playerE = (Player)PlayerUtils.find(player);
		if(playerE == null) throw new RuntimeException("Player '"+player+"' not found!");
		this.sendToPlayer(playerE, packet);
	}
	
	public void sendToServer(PacketInput packet) {
		PacketDispatcher.sendPacketToServer(packet.toPacket());
	}
	
	public void sendToAllPlayers(PacketInput packet) {
		PacketDispatcher.sendPacketToAllPlayers(packet.toPacket());
	}
	
	public void sendToAllNear(PacketInput packet, int dimId, double x, double y, double z, double range) {
		PacketDispatcher.sendPacketToAllAround(x, y, z, range, dimId, packet.toPacket());
	}
	
	public void sendToAllNear(PacketInput packet, TileEntity te, double range) {
		this.sendToAllNear(packet, te.worldObj.provider.dimensionId, te.xCoord, te.yCoord, te.zCoord, range);
	}
	
	public void sendToAllInDimension(PacketInput packet, int dimId) {
		PacketDispatcher.sendPacketToAllInDimension(packet.toPacket(), dimId);
	}
}
