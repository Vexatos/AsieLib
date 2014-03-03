package pl.asie.lib.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import pl.asie.lib.AsieLibMod;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketOutput {
	private final Packet250CustomPayload src;
	private final DataInputStream read;
	private final Gson gson = new Gson();
	
	public PacketOutput(Packet250CustomPayload packet) {
		this.src = packet;
		this.read = new DataInputStream(new ByteArrayInputStream(packet.data));
	}
	
	// Custom read functions
	
	public TileEntity readTileEntity() throws IOException {
		World world = null;
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		if(AsieLibMod.proxy.isClient()) {
			if(Minecraft.getMinecraft().theWorld.provider.dimensionId != dimensionId) {
				return null;
			} else world = Minecraft.getMinecraft().theWorld;
		} else {
			world = MinecraftServer.getServer().worldServerForDimension(dimensionId);
		}
		return world.getBlockTileEntity(x, y, z);
	}
	
	public byte[] readByteArray() throws IOException {
		int size = read.readUnsignedShort();
		byte[] data = new byte[size];
		read.read(data, 0, size);
		return data;
	}
	
	public Object readJSON(Type t) throws IOException {
		return gson.fromJson(read.readUTF(), t);
	}
	
	public Object readJSON(Class t) throws IOException {
		return gson.fromJson(read.readUTF(), t);
	}
	
	// Forwarding existing read functions
	
	public byte readByte() throws IOException {
		return read.readByte();
	}
	
	public short readShort() throws IOException {
		return read.readShort();
	}
	
	public byte readSignedByte() throws IOException {
		return read.readByte();
	}
	
	public short readSignedShort() throws IOException {
		return read.readShort();
	}
	
	public int readUnsignedByte() throws IOException {
		return read.readUnsignedByte();
	}
	
	public int readUnsignedShort() throws IOException {
		return read.readUnsignedShort();
	}
	
	public int readInt() throws IOException {
		return read.readInt();
	}
	
	public long readLong() throws IOException {
		return read.readLong();
	}
	
	public double readDouble() throws IOException {
		return read.readDouble();
	}
	
	public float readFloat() throws IOException {
		return read.readFloat();
	}
	
	public String readString() throws IOException {
		return read.readUTF();
	}
}
