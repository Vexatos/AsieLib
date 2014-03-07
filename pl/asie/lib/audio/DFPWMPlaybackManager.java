package pl.asie.lib.audio;

import java.util.HashMap;

public class DFPWMPlaybackManager {
	private final boolean isClient;
	
	public DFPWMPlaybackManager(boolean isClient) {
		this.isClient = isClient;
	}
	
	private int currentId = 0;
	private HashMap<Integer, DFPWMCodec> codecs = new HashMap<Integer, DFPWMCodec>();
	
	public int newPlayer() {
		DFPWMCodec codec = new DFPWMCodec();
		codecs.put(currentId++, codec);
		return currentId-1;
	}
	
	public void removePlayer(int id) {
		if(codecs.containsKey(id)) {
			codecs.get(id).stop();
			codecs.remove(id);
		}
	}
	
	public DFPWMCodec getPlayer(int id) {
		if(!codecs.containsKey(id)) codecs.put(id, new DFPWMCodec());
		return codecs.get(id);
	}
}
