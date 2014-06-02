package pl.asie.lib.api;

public interface IModIntegration {
	public String[] getDependencies();
	public String getName();
	public void preInit();
	public void init();
	public void postInit();
}
