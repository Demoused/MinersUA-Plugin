package org.topua.mc.minersUA.armors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NightVisionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {

            ItemStack nightVisionGlasses = new ItemStack(Material.CARVED_PUMPKIN);
            ItemMeta meta = nightVisionGlasses.getItemMeta();
            meta.setCustomModelData(100); // Встановлюємо CustomModelData
            meta.setDisplayName("§aОкуляри нічного бачення"); // Додаємо ім'я (опціонально)
            nightVisionGlasses.setItemMeta(meta);

            player.getInventory().addItem(nightVisionGlasses);
            player.sendMessage("§aВи отримали окуляри нічного бачення!");
            return true;
        } else {
            sender.sendMessage("Ця команда може бути виконана тільки гравцем.");
            return false;
        }
    }
}
