package mcp.mobius.talkative.api;

import java.util.Map;

import net.minecraft.util.IChatComponent;
import cpw.mods.fml.common.eventhandler.Event;

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

	public static class ServerSendChatEvent extends PrattleChatEvent {	// S->C, IChatComponent based
		public final Map<String, Boolean> flags;
		
		public ServerSendChatEvent(IChatComponent msg, String sender, String target, Map<String, Boolean> flags) {
			super(msg, sender, target);
			this.flags = flags;
		}
	}

	public static class ServerRecvChatEvent extends PrattleChatEvent {	// C->S, String based
		public final Map<String, Boolean> flags;		
		
		public ServerRecvChatEvent(IChatComponent msg, String sender, String target, Map<String, Boolean> flags) {
			super(msg, sender, target);
			this.flags = flags;			
		}
	}    

	public static class ClientSendChatEvent extends PrattleChatEvent {	// C->S, String based
		public ClientSendChatEvent(IChatComponent msg, String sender, String target) {
			super(msg, sender, target);
		}
	}            

	public static class ClientRecvChatEvent extends PrattleChatEvent {	// S->C, IChatComponent based
		public ClientRecvChatEvent(IChatComponent msg, String sender, String target) {
			super(msg, sender, target);
		}
	}        

}
