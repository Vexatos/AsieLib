package pl.asie.lib.util;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PlayerIterator implements Iterable<EntityPlayer>, Iterator<EntityPlayer> {
	private int currentWorld = 0;
	private Iterator<EntityPlayer> currentIterator;
	private MinecraftServer server;
	
	public PlayerIterator() {
		server = MinecraftServer.getServer();
		updateWorld();
	}
	
	private void updateWorld() {
		currentIterator = server.worldServers[currentWorld].playerEntities.iterator();
	}
	
	private boolean canIncrement() {
		return (currentWorld+1) < server.worldServers.length;
	}
	
	@Override
	public boolean hasNext() {
		return canIncrement() || currentIterator.hasNext();
	}

	@Override
	public EntityPlayer next() {
		if(!currentIterator.hasNext()) {
			if(!canIncrement()) return null;
			else {
				currentWorld++;
				updateWorld();
				return next();
			}
		} else return currentIterator.next();
	}

	@Override
	public void remove() {
		throw new RuntimeException("Cannot remove players from server via PlayerIterator!");
	}

	@Override
	public Iterator<EntityPlayer> iterator() {
		return new PlayerIterator();
	}
}
