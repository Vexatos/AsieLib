package pl.asie.lib.util;

import java.util.ArrayList;
import java.util.logging.Logger;

import pl.asie.lib.api.IModIntegration;
import cpw.mods.fml.common.Loader;

public class ModIntegrationHandler {
	public enum Stage {
		PRE_INIT,
		INIT,
		POST_INIT
	}
	
	private final ArrayList<IModIntegration> modIntegrators;
	private final Logger log;
	
	public ModIntegrationHandler(Logger log) {
		modIntegrators = new ArrayList<IModIntegration>();
		this.log = log;
	}

	public void add(IModIntegration integrator) {
		modIntegrators.add(integrator);
	}

	public boolean verify(String[] deps) {
		for(String mod: deps) {
			if(!Loader.isModLoaded(mod)) return false;
		}
		return true;
	}

	public void init(Stage stage) {
		for(IModIntegration integrator: modIntegrators) {
			if(verify(integrator.getDependencies())) {
				log.info("Loading "+integrator.getName()+" integration...");
				switch(stage) {
					case PRE_INIT: integrator.preInit(); break;
					case INIT: integrator.init(); break;
					case POST_INIT: integrator.postInit(); break;
				}
			}
		}
	}
}
