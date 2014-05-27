package pl.asie.lib.audio;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.Vec3;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StreamingAudioPlayer extends DFPWM {
	public int lastPacketId;
	private int receivedPackets;

	private boolean isInitializedClient = false;
	private IntBuffer source;
	private ArrayList<IntBuffer> buffersPlayed;
	private int SAMPLE_RATE;
	private final int BUFFER_PACKETS, FORMAT;
	private float volume = 1.0F;
	private float distance = 24.0F;
	
	public StreamingAudioPlayer(int sampleRate, boolean sixteenBit, boolean stereo, int bufferPackets) {
		super();
		lastPacketId = -9000;
		receivedPackets = 0;
		BUFFER_PACKETS = bufferPackets;
		SAMPLE_RATE = sampleRate;
		if(sixteenBit) {
			FORMAT = stereo ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
		} else {
			FORMAT = stereo ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_MONO8;
		}
	}
	
	public void setDistance(float dist) {
		this.distance = dist;
	}
	
	public void setVolume(float vol) {
		this.volume = vol;
	}
	
	public void setSampleRate(int rate) {
		SAMPLE_RATE = rate;
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
	public float getDistance(int x, int y, int z) {
		Vec3 pos = Minecraft.getMinecraft().thePlayer.getPosition(1.0f);
		double dx = pos.xCoord - x;
		double dy = pos.yCoord - y;
		double dz = pos.zCoord - z;
				
		float distance = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
		return distance;
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
		float distance = getDistance(x, y, z);
		float gain = distance >= 20.0f ? 0.0f : (distance <= 4.0f ? 1.0f : 1.0f - ((distance - 4.0f) / 16.0f));
		gain *= Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MUSIC);
				
		// Set settings
		AL10.alSourcei(source.get(0), AL10.AL_LOOPING, AL10.AL_FALSE);
	    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
	    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     gain);
	    AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
	    AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );
	    AL10.alSourcef(source.get(0), AL10.AL_ROLLOFF_FACTOR, 0.0f);
	    
	    // Play audio
	    AL10.alBufferData(buffer.get(0), FORMAT, (ByteBuffer)(BufferUtils.createByteBuffer(data.length).put(data).flip()), SAMPLE_RATE);
	    AL10.alSourceQueueBuffers(source.get(0), buffer);
	    
	    int state = AL10.alGetSourcei(source.get(0), AL10.AL_SOURCE_STATE);
	    if(receivedPackets > BUFFER_PACKETS && state != AL10.AL_PLAYING) AL10.alSourcePlay(source.get(0));
	    else if(receivedPackets <= BUFFER_PACKETS) AL10.alSourcePause(source.get(0));
	    
	    receivedPackets++;
	    
	    buffersPlayed.add(buffer);
	}
	
	@SideOnly(Side.CLIENT)
	private void stopClient() {
		AL10.alSourceStop(source.get(0));
		AL10.alDeleteSources(source);
		int count = 0;
		if(buffersPlayed != null) {
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
	}
	
	public void stop() {
		if(source != null) {
			stopClient();
		}
	    this.isInitializedClient = false;
	}
}
