package pl.asie.lib;

import pl.asie.lib.packet.PacketOutput;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload _packet, Player player) {
		if(!_packet.channel.equals("asielib")) return;
		PacketOutput packet = new PacketOutput(_packet);
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		
		try {
			int command = packet.readUnsignedShort();
			switch(command) {
				case Packets.NICKNAME_CHANGE:
					String username = packet.readString();
					String nickname = packet.readString();
					if(side == Side.CLIENT) AsieLibMod.nick.setNickname(username, nickname);
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
