package pl.asie.lib.network;

import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public abstract class NetworkHandlerBase implements IPacketHandler {
	public abstract void handlePacket(PacketOutput packet, int command, Player player, boolean isClient) throws IOException;
	
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload _packet, Player player) {
		PacketOutput packet = new PacketOutput(_packet);
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		try {
			handlePacket(packet, packet.readUnsignedShort(), player, (side == Side.CLIENT));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
