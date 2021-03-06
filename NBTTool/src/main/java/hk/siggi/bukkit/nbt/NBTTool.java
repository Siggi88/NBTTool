/*
 * The MIT License
 *
 * Copyright 2017 Siggi.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hk.siggi.bukkit.nbt;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for NBTTool, which provides two methods -- one to get NBTUtil,
 * and another to get NBTJsonSerializer.
 *
 * @author Siggi
 */
public class NBTTool extends JavaPlugin {

	/**
	 * Bukkit calls this constructor when loading NBTTool, you should not use
	 * this constructor yourself.
	 */
	public NBTTool() {
	}

	private static NBTTool instance = null;
	private NBTUtil nbtutil = null;
	private NBTJsonSerializer serializer = null;

	/**
	 * Gets the {@link NBTUtil} to use, or null if the current server version is
	 * not supported.
	 *
	 * @return an {@link NBTUtil}
	 */
	public static NBTUtil getUtil() {
		if (instance == null) {
			return null;
		}
		return instance.nbtutil;
	}

	/**
	 * Gets the {@link NBTJsonSerializer} to use, or null if the current server
	 * version is not supported. This is not available on pre-1.8.3 servers
	 * unless you add Gson to the startup classpath, or install a plugin that
	 * has Gson at com.google.gson and not in a shaded package path.
	 *
	 * @return an {@link NBTJsonSerializer}
	 */
	public static NBTJsonSerializer getSerializer() {
		if (instance == null) {
			return null;
		}
		return instance.serializer;
	}

	/**
	 * Called by Bukkit when the plugin is enabled, you shouldn't call this
	 * method in your own plugin.
	 */
	@Override
	public void onEnable() {
		instance = this;
		enable:
		{
			try {
				nbtutil = NBTUtil.get();
				if (nbtutil == null) {
					break enable;
				}
				try {
					serializer = new NBTJsonSerializer(this, nbtutil);
				} catch (Exception e) {
					System.err.println("Gson is not available, add Gson to the classpath to enable this feature!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				break enable;
			}
			return;
		}
		System.err.println("NBTTool does not support this server version!");
		setEnabled(false);
	}

	/**
	 * Called by Bukkit when the plugin is disabled, you shouldn't call this
	 * method in your own plugin.
	 */
	@Override
	public void onDisable() {
	}
}
