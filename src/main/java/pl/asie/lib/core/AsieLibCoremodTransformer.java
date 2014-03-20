package pl.asie.lib.core;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class AsieLibCoremodTransformer extends AccessTransformer {

	public AsieLibCoremodTransformer() throws IOException {
		super("asielib_at.cfg");
	}

}
