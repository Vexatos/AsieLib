package pl.asie.lib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketChannelHandler extends FMLIndexedMessageToMessageCodec<Packet> {
	private final MessageHandlerBase handlerClient, handlerServer;
	
    public PacketChannelHandler(MessageHandlerBase client, MessageHandlerBase server) {
    	this.handlerClient = client;
    	this.handlerServer = server;
    	addDiscriminator(0, Packet.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, Packet packet, ByteBuf data) throws Exception {
        packet.toBytes(data);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, Packet packet) {
        packet.fromBytes(data);
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
	        case CLIENT:
	            if(handlerClient != null)
	            	handlerClient.onMessage(packet, netHandler, Minecraft.getMinecraft().thePlayer);
	            break;
	        case SERVER:
	            if(handlerServer != null)
	            	handlerServer.onMessage(packet, netHandler, ((NetHandlerPlayServer) netHandler).playerEntity);
	            break;
	    }
    }
}
