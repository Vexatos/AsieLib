package pl.asie.lib.audio;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DFPWMCodec extends DFPWM {
	public int lastPacketId;
	private int receivedPackets;

	private boolean isInitializedClient = false;
	private IntBuffer source;
	private ArrayList<IntBuffer> buffersPlayed;
	
	public DFPWMCodec() {
		super();
		lastPacketId = -9000;
		receivedPackets = 0;
	}
	
	@SideOnly(Side.CLIENT)
	public void reset() {
		buffersPlayed = new ArrayList<IntBuffer>();
		lastPacketId = -9000;
		receivedPackets = 0;
		if(source != null) stopClient();
	}
	
	@SideOnly(Side.CLIENT)
	public boolean initClient() {
		source = BufferUtils.createIntBuffer(1);
		AL10.alGenSources(source);
		
		if(AL10.alGetError() != AL10.AL_NO_ERROR)
		      return false;

	    this.isInitializedClient = true;
	    
	    return (AL10.alGetError() == AL10.AL_NO_ERROR);
	}
	
	@SideOnly(Side.CLIENT)
	public void playPacket(byte[] data, int x, int y, int z) {
		if(!isInitializedClient || source == null) {
			reset();
			initClient();
		}
		FloatBuffer sourcePos = (FloatBuffer)(BufferUtils.createFloatBuffer(3).put(new float[] { x, y, z }).rewind());
		FloatBuffer sourceVel = (FloatBuffer)(BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind());
		IntBuffer buffer;
		
		// Prepare buffers
		int processed = AL10.alGetSourcei(source.get(0), AL10.AL_BUFFERS_PROCESSED);
		if (processed > 0) {
			buffer = BufferUtils.createIntBuffer(1);
			AL10.alGenBuffers(buffer);
			AL10.alSourceUnqueueBuffers(source.get(0), buffer);
		} else {
			buffer = BufferUtils.createIntBuffer(1);
			AL10.alGenBuffers(buffer);
		}
		
		// Calculate distance
		Vec3 pos = Minecraft.getMinecraft().thePlayer.getPosition(1.0f);
		double dx = pos.xCoord - x;
		double dy = pos.yCoord - y;
		double dz = pos.zCoord - z;
				
		float distance = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
		float gain = distance >= 20.0f ? 0.0f : (distance <= 4.0f ? 1.0f : 1.0f - ((distance - 4.0f) / 16.0f));

		// Set settings
		AL10.alSourcei(source.get(0), AL10.AL_LOOPING, AL10.AL_FALSE);
	    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
	    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     gain);
	    AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
	    AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );
	    AL10.alSourcef(source.get(0), AL10.AL_ROLLOFF_FACTOR, 0.0f);
	    
	    // Play audio
	    AL10.alBufferData(buffer.get(0), AL10.AL_FORMAT_MONO8, (ByteBuffer)(BufferUtils.createByteBuffer(data.length).put(data).flip()), 32768);
	    AL10.alSourceQueueBuffers(source.get(0), buffer);
	    
	    int state = AL10.alGetSourcei(source.get(0), AL10.AL_SOURCE_STATE);
	    if(receivedPackets > 3 && gain > 0.0f && state != AL10.AL_PLAYING) AL10.alSourcePlay(source.get(0));
	    else if(receivedPackets <= 3 || gain == 0.0f) AL10.alSourcePause(source.get(0));
	    
	    receivedPackets++;
	    
	    buffersPlayed.add(buffer);
	}
	
	@SideOnly(Side.CLIENT)
	private void stopClient() {
		AL10.alSourceStop(source.get(0));
		AL10.alDeleteSources(source);
		int count = 0;
		for(IntBuffer b: buffersPlayed) {
			b.rewind();
			for(int i = 0; i < b.limit(); i++) {
				int buffer = b.get(i);
				if(AL10.alIsBuffer(buffer)) {
					AL10.alDeleteBuffers(buffer);
					count++;
				}
			}
		}
		buffersPlayed.clear();
		System.out.println("Cleaned " + count + " buffers.");
	}
	
	public void stop() {
		if(source != null) {
			stopClient();
		}
	    this.isInitializedClient = false;
	}
}
