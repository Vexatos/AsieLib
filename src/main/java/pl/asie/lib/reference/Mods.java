package pl.asie.lib.reference;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

import java.util.HashMap;

/**
 * @author Vexatos
 */
public class Mods {

	//The mod itself
	public static final String
		AsieLib = "asielib",
		AsieLib_NAME = "AsieLib";

	//Other mods
	public static final String
		AE2 = "appliedenergistics2",
		IC2 = "IC2",
		IC2Classic = "IC2-Classic",
		GregTech = "gregtech",
		Mekanism = "Mekanism",
		ProjectRed = "ProjRed|Core",
		RedLogic = "RedLogic";

	//Other APIs
	public static class API {
		public static final String
			BuildCraftTools = "BuildCraftAPI|tools",
			CoFHBlocks = "CoFHAPI|block",
			CoFHEnergy = "CoFHAPI|energy",
			CoFHItems = "CoFHAPI|item",
			CoFHTileEntities = "CoFHAPI|tileentity",
			EiraIRC = "EiraIRC|API",
			EnderIOTools = "EnderIOAPI|Tools",
			OpenComputersInternal = "OpenComputersAPI|Internal";

		private static HashMap<String, ArtifactVersion> apiList;

		public static ArtifactVersion getVersion(String name) {
			if(apiList == null) {
				apiList = new HashMap<String, ArtifactVersion>();
				Iterable<? extends ModContainer> apis = ModAPIManager.INSTANCE.getAPIList();

				for(ModContainer api : apis) {
					apiList.put(api.getModId(), api.getProcessedVersion());
				}
			}

			if(apiList.containsKey(name)) {
				return apiList.get(name);
			}
			throw new IllegalArgumentException("API '" + name + "' does not exist!");
		}

		public static boolean hasVersion(String name, String version) {
			if(ModAPIManager.INSTANCE.hasAPI(name)) {
				ArtifactVersion v1 = VersionParser.parseVersionReference(name + "@" + version);
				ArtifactVersion v2 = getVersion(name);
				return v1.containsVersion(v2);
			}
			return false;
		}

		public static boolean hasAPI(String name) {
			return ModAPIManager.INSTANCE.hasAPI(name);
		}
	}

	public static boolean isLoaded(String name) {
		return Loader.isModLoaded(name);
	}

	private static boolean checkedEnergyMods = false;
	private static boolean hasEnergyMod = false;

	public static boolean hasEnergyMod() {
		if(!checkedEnergyMods) {
			hasEnergyMod = API.hasAPI(API.CoFHEnergy)
				|| isLoaded(IC2)
				|| isLoaded(IC2Classic);
			checkedEnergyMods = true;
		}
		return hasEnergyMod;
	}
}
