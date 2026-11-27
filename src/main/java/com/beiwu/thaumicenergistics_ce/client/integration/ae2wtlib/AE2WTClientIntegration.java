package com.beiwu.thaumicenergistics_ce.client.integration.ae2wtlib;

import appeng.client.gui.style.StyleManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import com.beiwu.thaumicenergistics_ce.menu.MenuArcaneCraftingTerminal;
import com.beiwu.thaumicenergistics_ce.menu.MenuArcaneEncodingTerminal;
import com.beiwu.thaumicenergistics_ce.registry.TECEMenuTypes;

/**
 * AE2WTLib 客户端屏幕注册的纯门面入口：仅暴露 {@link #enabled()} 与转发方法，
 * 实际引用 {@code ae2wtlib_api} 与 WT 屏幕类的逻辑隔离在私有静态内部类 {@link Impl} 中，
 * 避免缺依赖时在 {@link #enabled()} 的字节码验证阶段触发 {@code NoClassDefFoundError}。
 *
 * <p>样式也必须换一份：{@code wtlib/universal_terminal.json} 是 AE2WTLib 带的文档，
 * 它不在场时就找不到，而 {@code StyleManager} 遇到缺失的 include 是直接抛异常、
 * 不是降级。所以 wt 版样式单独成文件，只在 {@link Impl} 里加载。
 */
public final class AE2WTClientIntegration {

    private AE2WTClientIntegration() {}

    public static boolean enabled() {
        return ModList.get().isLoaded("ae2wtlib");
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        if (!enabled()) {
            return;
        }
        Impl.registerScreens(event);
    }

    private static final class Impl {

        private Impl() {}

        static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(
                    TECEMenuTypes.WIRELESS_ARCANE_CRAFTING_TERMINAL.get(),
                    (MenuArcaneCraftingTerminal menu, Inventory inventory, Component title) ->
                            new AE2WTArcaneCraftingTerminalScreen(
                                    menu,
                                    inventory,
                                    title,
                                    StyleManager.loadStyleDoc(
                                            "/screens/wireless_arcane_crafting_terminal_wt.json")));
            event.register(
                    TECEMenuTypes.WIRELESS_ARCANE_ENCODING_TERMINAL.get(),
                    (MenuArcaneEncodingTerminal menu, Inventory inventory, Component title) ->
                            new AE2WTArcaneEncodingTerminalScreen(
                                    menu,
                                    inventory,
                                    title,
                                    StyleManager.loadStyleDoc(
                                            "/screens/wireless_arcane_encoding_terminal_wt.json")));
        }
    }
}
