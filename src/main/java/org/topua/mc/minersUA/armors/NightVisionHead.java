package org.topua.mc.minersUA.armors;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.topua.mc.minersUA.MinersUA;

import java.util.ArrayList;
import java.util.List;

public class NightVisionHead implements Listener {
    private final MinersUA plugin;

    public NightVisionHead(MinersUA plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack currentHelmet = player.getInventory().getHelmet();
        ItemStack clickedItem = event.getCurrentItem();

        // Перевіряємо, чи гравець намагається зняти окуляри нічного бачення
        if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getSlot() == 39 // Slot 39 - це шолом
                && clickedItem != null && clickedItem.getType() == Material.CARVED_PUMPKIN && clickedItem.getItemMeta().getCustomModelData() == 100
                && player.hasMetadata("night_vision_glasses_toggling")) {

            // Якщо окуляри в процесі перемикання, скасовуємо подію
            event.setCancelled(true);
        } else if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getSlot() == 39 // Slot 39 - це шолом
                && currentHelmet != null && currentHelmet.getType() == Material.CARVED_PUMPKIN && currentHelmet.getItemMeta().getCustomModelData() == 100) {

            // Якщо окуляри були зняті або змінені на інші, вимикаємо нічний режим
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.sendMessage("§cОкуляри нічного бачення вимкнено");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 0.6f, 0.8f);

            }

            // Оновлення опису окулярів
            if (clickedItem != null && clickedItem.getType() == Material.CARVED_PUMPKIN && clickedItem.getItemMeta().getCustomModelData() == 100) {
                ItemMeta meta = clickedItem.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add("§7Коли на голові:");
                lore.add("§9-20% урону");
                meta.setLore(lore);
                clickedItem.setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet(); // Отримуємо предмет на голові

        if (helmet != null && helmet.getType() == Material.CARVED_PUMPKIN && helmet.getItemMeta().getCustomModelData() == 100) {
            event.setCancelled(true); // Скасовуємо переміщення предмета між руками
            ItemMeta meta = helmet.getItemMeta();

            // Перевірка стану окулярів
            if (player.hasMetadata("night_vision_glasses_toggling")) {
                // Окуляри вже в процесі перемикання, ігноруємо подію
                return;
            }
            player.setMetadata("night_vision_glasses_toggling", new FixedMetadataValue(this.plugin, true)); // Встановлюємо стан "перемикання"

            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                // Відтворення звуків вимкнення протягом 2 секунд, оновлюючи позицію
                BukkitTask task = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 10.0f, 0.7f); // Знижений тон
                }, 30L);
                // Затримка перед вимкненням
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1.0f, 0.8f);
                    player.sendMessage("§cОкуляри нічного бачення вимкнено");

                    // Додавання опису
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    lore.add("§7Коли на голові:");
                    lore.add("§9-20% урону");
                    meta.setLore(lore);
                    helmet.setItemMeta(meta);
                    task.cancel();
                    player.removeMetadata("night_vision_glasses_toggling", this.plugin); // Знімаємо стан "перемикання"
                }, 40L); // 40 ticks = 2 секунда затримки
            } else {
                // Відтворення звуків вмикання протягом 2 секунд, оновлюючи позицію
                BukkitTask task = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 10.0f, 0.7f); // Знижений тон
                }, 30L);
                // Затримка перед ввімкненням
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                    player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 1.0f);
                    player.sendMessage("§aОкуляри нічного бачення ввімкнено");

                    // Додавання опису
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Нічне бачення");
                    lore.add(" ");
                    lore.add("§7Коли надягнуто:");
                    lore.add("§9-20% урону");
                    meta.setLore(lore);
                    helmet.setItemMeta(meta);
                    task.cancel();
                    player.removeMetadata("night_vision_glasses_toggling", this.plugin); // Знімаємо стан "перемикання"
                }, 40L); // 40 ticks = 2 секунда затримки
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST) // Використовуємо найвищий пріоритет
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            double damage = event.getDamage(); // Отримуємо початкову кількість урону

            // Логіка захисту окулярів
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet != null && helmet.getType() == Material.CARVED_PUMPKIN && helmet.getItemMeta().getCustomModelData() == 100) {
                double newDamage = damage * 0.8; // Зменшуємо пошкодження на 20%
                event.setDamage(newDamage); // Змінюємо початкове значення урону
                damage = newDamage; // Оновлюємо змінну damage для виведення в action bar
            }

            // Виводимо повідомлення в action bar
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent("§cОтримано урону: " + String.format("%.1f", damage)));
        }
    }



}
