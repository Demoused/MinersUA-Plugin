package org.topua.mc.minersUA;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.topua.mc.minersUA.armors.NightVisionCommand;
import org.topua.mc.minersUA.armors.NightVisionHead;

public final class MinersUA extends JavaPlugin {
    private static MinersUA instance;
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();
    public MinersUA() {}

    @Override
    public void onEnable() {
        WelcomeLogo(true);

        Server server = Bukkit.getServer();
        server.getPluginManager().registerEvents(new NightVisionHead(this),this);

        getCommand("nightvision").setExecutor(new NightVisionCommand());
    }

    @Override
    public void onDisable() {
        WelcomeLogo(false);
    }

    private void WelcomeLogo(boolean start) {
        String plugin = this.getDescription().getName();
        String version = this.getDescription().getVersion();
        String colorCode = start ? "§a" : "§c";
        String statusMessage = start ? " йде працювати!" : " Відлинює!";

        console.sendMessage(" ");
        console.sendMessage(colorCode + "    __  ____                      __  _____ ");
        console.sendMessage(colorCode + "   /  |/  (_)___  ___  __________/ / / /   |");
        console.sendMessage(colorCode + "  / /|_/ / / __ \\/ _ \\/ ___/ ___/ / / / /| |");
        console.sendMessage(colorCode + " / /  / / / / / /  __/ /  (__  ) /_/ / ___ |");
        console.sendMessage(colorCode + "/_/  /_/_/_/ /_/\\___/_/  /____/\\____/_/  |_|");
        console.sendMessage(colorCode + "       " + plugin + (start ? " v" + version : "") + statusMessage);
        console.sendMessage(" ");
    }

    public static MinersUA getInstance() {
        return instance;
    }
}
