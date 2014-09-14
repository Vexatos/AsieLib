package pl.asie.lib.core;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class AsieLibCoremodTransformer extends AccessTransformer {

	public AsieLibCoremodTransformer() throws IOException {
		super("asielib_at.cfg");
	}

}
