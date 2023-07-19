package jshdesktop.lua;

import com.hk.lua.LuaLibrary;

public class JSHDesktopLuaLibrary {
	public static final LuaLibrary<LuaDesktopLibrary> DESKTOP = new LuaLibrary<>("Desktop", LuaDesktopLibrary.class);
}
