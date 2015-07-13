package pl.asie.lib;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.asie.lib.api.AsieLibAPI;
import pl.asie.lib.api.chat.INicknameHandler;
import pl.asie.lib.api.chat.INicknameRepository;
import pl.asie.lib.api.tool.IToolRegistry;
import pl.asie.lib.chat.ChatHandler;
import pl.asie.lib.chat.NicknameNetworkHandler;
import pl.asie.lib.chat.NicknameRepository;
import pl.asie.lib.client.BlockBaseRender;
import pl.asie.lib.integration.Integration;
import pl.asie.lib.integration.tool.ToolProviders;
import pl.asie.lib.network.PacketHandler;
import pl.asie.lib.reference.Mods;
import pl.asie.lib.tweak.enchantment.EnchantmentTweak;

import java.lang.reflect.Method;
import java.util.Random;

@Mod(modid = Mods.AsieLib, name = Mods.AsieLib_NAME, version = "@VERSION@",
	dependencies = "required-after:Forge@[10.13.2.1236,);after:gregtech@[6.00.00];"
		+ "after:CoFHAPI|block@[1.7.10R1.0.0,);after:CoFHAPI|energy@[1.7.10R1.0.0,);"
		+ "after:CoFHAPI|tileentity@[1.7.10R1.0.0,);after:CoFHAPI|item@[1.7.10R1.0.0,)")
public class AsieLibMod extends AsieLibAPI {
	public Configuration config;
	public static Random rand = new Random();
	public static Logger log;
	public static ChatHandler chat;
	public static NicknameRepository nick;
	public static PacketHandler packet;

	public static boolean ENABLE_DYNAMIC_ENERGY_CALCULATION;

	@Instance(value = Mods.AsieLib)
	public static AsieLibMod instance;

	@SidedProxy(clientSide = "pl.asie.lib.ClientProxy", serverSide = "pl.asie.lib.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AsieLibAPI.instance = this;
		ToolProviders.registerToolProviders();
		log = LogManager.getLogger(Mods.AsieLib);

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		chat = new ChatHandler(config);

		if(chat.enableChatFeatures) {
			MinecraftForge.EVENT_BUS.register(chat);
		}
		MinecraftForge.EVENT_BUS.register(new AsieLibEvents());

		ENABLE_DYNAMIC_ENERGY_CALCULATION =
			config.getBoolean("enableDynamicEnergyUsageCalculation", "general", true, "If you want to disable dynamic generation of current/peak energy usage, use this.");

		if(System.getProperty("user.dir").contains(".asielauncher")) {
			log.info("Hey, you! Yes, you! Thanks for using AsieLauncher! ~asie");
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(proxy.isClient()) {
			new BlockBaseRender();
		}

		packet = new PacketHandler(Mods.AsieLib, new NetworkHandlerClient(), null);

		if(config.get("enchantments", "usefulBaneOfArthropods", false,
			"Might make Bane Of Arthropods actually useful (Experimental)").getBoolean(false)) {
			EnchantmentTweak.registerBaneEnchantment(config.getInt("baneEnchantmentID", "enchantments", 244, 0, 255,
				"The enchantment ID for the better Bane Of Arthropods"));
			EnchantmentTweak tweak = new EnchantmentTweak();
			MinecraftForge.EVENT_BUS.register(tweak);
			FMLCommonHandler.instance().bus().register(tweak);
		}

		nick = new NicknameRepository();
		MinecraftForge.EVENT_BUS.register(nick);

		NicknameNetworkHandler nicknameHandler = new NicknameNetworkHandler();
		registerNicknameHandler(nicknameHandler);
		FMLCommonHandler.instance().bus().register(nicknameHandler);

		if(config.get("tweaks", "dyeItemNamesInAnvil", true).getBoolean(true)) {
			MinecraftForge.EVENT_BUS.register(new AnvilDyeTweak());
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		config.save();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		chat.registerCommands(event);
		nick.loadNicknames();
	}

	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event) {
		nick.saveNicknames();
	}

	public void registerNicknameHandler(INicknameHandler handler) {
		if(nick != null) {
			nick.addHandler(handler);
		}
	}

	public INicknameRepository getNicknameRepository() {
		return nick;
	}

	/**
	 * Call this using {@link FMLInterModComms#sendMessage}.
	 * <p/>
	 * Example:
	 * FMLInterModComms.sendMessage("asielib", "addToolProvider", "com.example.examplemod.tool.ToolProviders.register")
	 * @see IToolRegistry
	 */
	@EventHandler
	@SuppressWarnings("unchecked")
	public void receiveIMC(FMLInterModComms.IMCEvent event) {
		ImmutableList<FMLInterModComms.IMCMessage> messages = event.getMessages();
		for(FMLInterModComms.IMCMessage message : messages) {
			if(message.key.equalsIgnoreCase("addtoolprovider") && message.isStringMessage()) {
				try {
					String methodString = message.getStringValue();
					String[] methodParts = methodString.split("\\.");
					String methodName = methodParts[methodParts.length - 1];
					String className = methodString.substring(0, methodString.length() - methodName.length() - 1);
					try {
						Class c = Class.forName(className);
						Method method = c.getDeclaredMethod(methodName, IToolRegistry.class);
						method.invoke(null, Integration.toolRegistry);
					} catch(ClassNotFoundException e) {
						log.warn("Could not find class " + className, e);
					} catch(NoSuchMethodException e) {
						log.warn("Could not find method " + methodString, e);
					} catch(Exception e) {
						log.warn("Exception while trying to call method " + methodString, e);
					}
				} catch(Exception e) {
					log.warn("Exception while trying to register a ToolProvider", e);
				}
			}
		}
	}
}
