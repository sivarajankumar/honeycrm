package honeycrm.client.misc;

import java.io.Serializable;

public class PluginClassBytecode implements Serializable {
	private static final long serialVersionUID = -3472564561754199391L;
	private String className;
	private byte[] bytecode;

	public PluginClassBytecode() {
	}

	public PluginClassBytecode(final String className, final byte[] bytecode) {
		this.className = className;
		this.bytecode = bytecode;
	}

	public String getClassName() {
		return className;
	}

	public byte[] getBytecode() {
		return bytecode;
	}
}
