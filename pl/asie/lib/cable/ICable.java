package pl.asie.lib.cable;

public interface ICable {
	public int getNetworkID();
	public void onAdded(int id);
	public void onRemoved(int id);
}
