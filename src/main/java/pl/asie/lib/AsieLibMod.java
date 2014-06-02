package pl.asie.lib;

import java.util.Random;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import pl.asie.lib.api.AsieLibAPI;
import pl.asie.lib.api.chat.INicknameHandler;
import pl.asie.lib.api.chat.INicknameRepository;
import pl.asie.lib.chat.ChatHandler;
import pl.asie.lib.chat.NicknameNetworkHandler;
import pl.asie.lib.chat.NicknameRepository;
import pl.asie.lib.client.BlockBaseRender;
import pl.asie.lib.network.PacketFactory;
import pl.asie.lib.shinonome.EventKeyClient;
import pl.asie.lib.shinonome.EventKeyServer;
import pl.asie.lib.shinonome.ItemKey;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="asielib", name="AsieLib", version="0.2.4")
@NetworkMod(channels={"asielib"}, clientSideRequired=true, packetHandler=NetworkHandler.class)
public class AsieLibMod extends AsieLibAPI {
	public Configuration config;
	public static Random rand = new Random();
	public static Logger log;
	public static ChatHandler chat;
	public static NicknameRepository nick;
	public static ItemKey itemKey;
	public static PacketFactory packet;
	public static EventKeyClient keyClient = new EventKeyClient();
	public static EventKeyServer keyServer = new EventKeyServer();
	public static boolean BuildCraftPresent = false;

	@Instance(value="asielib")
	public static AsieLibMod instance;
	
	@SidedProxy(clientSide="pl.asie.lib.ClientProxy", serverSide="pl.asie.lib.CommonProxy")	
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AsieLibAPI.instance = this;
		log = Logger.getLogger("asielib");
		log.setParent(FMLLog.getLogger());

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		packet = new PacketFactory("asielib");
		
		chat = new ChatHandler(config);
		nick = new NicknameRepository();
		nick.loadNicknames();
    	MinecraftForge.EVENT_BUS.register(chat);
    	MinecraftForge.EVENT_BUS.register(nick);
    	MinecraftForge.EVENT_BUS.register(new AsieLibEvents());
    	
		if(System.getProperty("user.dir").indexOf(".asielauncher") >= 0) {
			log.info("Hey, you! Yes, you! Thanks for using AsieLauncher! ~asie");
		}
		
		itemKey = new ItemKey(config.getItem("hakase", 17500).getInt());
		GameRegistry.registerItem(itemKey, "item.asietweaks.key");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(proxy.isClient()) {
			MinecraftForge.EVENT_BUS.register(keyClient);
			new BlockBaseRender();
		}
		MinecraftForge.EVENT_BUS.register(keyServer);
		
		NetworkRegistry.instance().registerConnectionHandler(new NicknameNetworkHandler());
		
		config.save();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded("ComputerCraft")) {
			CC16PeripheralHandler.registerPeripheralProvider();
		}
		if (Loader.isModLoaded("BuildCraft|Core")) {
			BuildCraftPresent = true;
		}
		
		TickRegistry.registerTickHandler(keyClient, Side.CLIENT);
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
    	chat.registerCommands(event);
	}
	
	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event) {
		nick.saveNicknames();
	}
	
	public void registerNicknameHandler(INicknameHandler handler) {
		if(nick != null)
			nick.addHandler(handler);
	}
	
	public INicknameRepository getNicknameRepository() { return nick; }
}
