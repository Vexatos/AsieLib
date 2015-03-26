package mcp.mobius.talkative.api;

import java.util.Map;

import net.minecraft.util.IChatComponent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class PrattleChatEvent extends Event{

	private final IChatComponent originalMsg;
	public  IChatComponent       displayMsg;
	public  final String         sender;
	public  final String         target;  //Can be a player or a channel; #first char indicates channel

	private PrattleChatEvent(IChatComponent msg, String sender, String target) {
		this.originalMsg = msg.createCopy();
		this.displayMsg  = msg.createCopy();
		this.sender      = sender;
		this.target      = target;
	}

	public final IChatComponent getOriginalMsg() {
		return originalMsg.createCopy();
	}

	// Note: on server side, if you require the EntityPlayerMP, try the following:
	// FMLCommonHandler.instance().getSidedDelegate().getServer().getConfigurationManager().func_152612_a(event.sender);

	// Fired when the server sends a message to a client
	public static class ServerSendChatEvent extends PrattleChatEvent {
		public final Map<String, Boolean> flags;

		public ServerSendChatEvent(IChatComponent msg, String sender, String target, Map<String, Boolean> flags) {
			super(msg, sender, target);
			this.flags = flags;
		}
	}

	// Fired when the server receives a message from a client
	public static class ServerRecvChatEvent extends PrattleChatEvent {
		public final Map<String, Boolean> flags;		

		public ServerRecvChatEvent(IChatComponent msg, String sender, String target, Map<String, Boolean> flags) {
			super(msg, sender, target);
			this.flags = flags;			
		}
	}    

	// Fired when a client sends a message to the server
	public static class ClientSendChatEvent extends PrattleChatEvent {
		public ClientSendChatEvent(IChatComponent msg, String sender, String target) {
			super(msg, sender, target);
		}
	}            

	// Fired when a client receives a message from the server
	public static class ClientRecvChatEvent extends PrattleChatEvent {
		public ClientRecvChatEvent(IChatComponent msg, String sender, String target) {
			super(msg, sender, target);
		}
	}        

}
