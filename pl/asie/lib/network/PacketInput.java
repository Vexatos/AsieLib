package pl.asie.lib.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

public class PacketInput {
	private final String channel;
	private final DataOutputStream write;
	private final ByteArrayOutputStream writeByteArray;
	private final Gson gson = new Gson();
	
	protected PacketInput(String channel) {
		this.channel = channel;
		this.writeByteArray = new ByteArrayOutputStream();
		this.write = new DataOutputStream(this.writeByteArray);
	}
	
	public Packet250CustomPayload toPacket() {
		return new Packet250CustomPayload(this.channel, this.toByteArray());
	}
	
	public byte[] toByteArray() {
		try {
			write.flush();
		} catch(Exception e) { e.printStackTrace(); }
		return writeByteArray.toByteArray();
	}
	
	// Custom write instructions
	
	public PacketInput writeTileLocation(TileEntity te) throws IOException, RuntimeException {
		if(te.worldObj == null) throw new RuntimeException("World does not exist!");
		if(te.isInvalid()) throw new RuntimeException("TileEntity is invalid!");
		write.writeInt(te.worldObj.provider.dimensionId);
		write.writeInt(te.xCoord);
		write.writeInt(te.yCoord);
		write.writeInt(te.zCoord);
		return this;
	}
	
	public PacketInput writeByteArray(byte[] array) throws IOException, RuntimeException {
		if(array.length > 65535) throw new RuntimeException("Invalid array size!");
		write.writeShort(array.length);
		write.write(array);
		return this;
	}
	
	public PacketInput writeByteArrayData(byte[] array) throws IOException {
		write.write(array);
		return this;
	}
	
	public PacketInput writeJSON(Object o) throws IOException, RuntimeException {
		this.writeString(gson.toJson(o));
		return this;
	}
	
	public PacketInput writeJSON(Object o, Type type) throws IOException, RuntimeException {
		this.writeString(gson.toJson(o, type));
		return this;
	}
	
	public PacketInput writeJSON(Object o, Class type) throws IOException, RuntimeException {
		this.writeString(gson.toJson(o, type));
		return this;
	}
	
	// Forwarding all write instructions I care about

	public PacketInput writeByte(byte v) throws IOException {
		write.writeByte(v);
		return this;
	}
	
	public PacketInput writeBoolean(boolean v) throws IOException {
		write.writeBoolean(v);
		return this;
	}
	
	public PacketInput writeString(String s) throws IOException {
		write.writeUTF(s);
		return this;
	}
	
	public PacketInput writeShort(short v) throws IOException {
		write.writeShort(v);
		return this;
	}
	
	public PacketInput writeInt(int v) throws IOException {
		write.writeInt(v);
		return this;
	}
	
	public PacketInput writeDouble(long v) throws IOException {
		write.writeLong(v);
		return this;
	}
	
	public PacketInput writeFloat(long v) throws IOException {
		write.writeLong(v);
		return this;
	}
	
	public PacketInput writeLong(long v) throws IOException {
		write.writeLong(v);
		return this;
	}
}
