package pl.asie.lib.gui.managed;

/**
 * @author Vexatos
 */
public abstract class GuiProviderBase implements IGuiProvider {

	protected int guiID;

	@Override
	public void setGuiID(int guiID) {
		this.guiID = guiID;
	}

	@Override
	public int getGuiID() {
		return this.guiID;
	}
}
