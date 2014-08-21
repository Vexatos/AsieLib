package pl.asie.lib.api.provider;

public interface IBundledRedstoneProvider {
	public boolean canConnectTo(int side, int face);
	public byte[] getBundledOutput(int side, int face);
	public void onBundledInputChange(int side, int face, byte[] data);
}