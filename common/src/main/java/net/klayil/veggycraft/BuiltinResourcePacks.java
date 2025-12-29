package net.klayil.veggycraft;

public class BuiltinResourcePacks {
    public static void init(BuiltinPackRegister impl) {
        BuiltinPackRegister register = impl;
        register.registerBuiltinPacks();
    }

    private BuiltinResourcePacks() {}
}
