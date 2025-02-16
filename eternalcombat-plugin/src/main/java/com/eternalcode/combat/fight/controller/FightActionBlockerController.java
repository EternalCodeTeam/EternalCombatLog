package com.eternalcode.combat.fight.controller;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.WhitelistBlacklistMode;
import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.event.FightUntagEvent;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.List;
import java.util.UUID;

public class FightActionBlockerController implements Listener {

    private final FightManager fightManager;
    private final NotificationAnnouncer announcer;
    private final PluginConfig config;
    private final Server server;

    public FightActionBlockerController(FightManager fightManager, NotificationAnnouncer announcer, PluginConfig config, Server server) {
        this.fightManager = fightManager;
        this.announcer = announcer;
        this.config = config;
        this.server = server;
    }

    @EventHandler
    void onPlace(BlockPlaceEvent event) {
        if (!this.config.settings.shouldPreventBlockPlacing) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (!this.fightManager.isInCombat(uniqueId)) {
            return;
        }

        Block block = event.getBlock();
        int level = block.getY();

        List<Material> specificBlocksToPreventPlacing = this.config.settings.specificBlocksToPreventPlacing;

        boolean isPlacementBlocked = this.isPlacementBlocked(level);

        if (isPlacementBlocked && specificBlocksToPreventPlacing.isEmpty()) {
            event.setCancelled(true);
            this.announcer.create()
                .player(uniqueId)
                .notice(this.config.messages.blockPlacingBlockedDuringCombat)
                .placeholder("{Y}", String.valueOf(this.config.settings.blockPlacingYCoordinate))
                .placeholder("{MODE}", this.config.settings.blockPlacingModeName)
                .send();

        }

        Material blockMaterial = block.getType();
        boolean isBlockInDisabledList = specificBlocksToPreventPlacing.contains(blockMaterial);
        if (isPlacementBlocked && isBlockInDisabledList) {
            event.setCancelled(true);

            this.announcer.create()
                .player(uniqueId)
                .notice(this.config.messages.blockPlacingBlockedDuringCombat)
                .placeholder("{Y}", String.valueOf(this.config.settings.blockPlacingYCoordinate))
                .placeholder("{MODE}", this.config.settings.blockPlacingModeName)
                .send();

        }
    }

    private boolean isPlacementBlocked(int level) {
        return this.config.settings.blockPlacingMode == PluginConfig.Settings.BlockPlacingMode.ABOVE
            ? level > this.config.settings.blockPlacingYCoordinate
            : level < this.config.settings.blockPlacingYCoordinate;
    }

    @EventHandler
    void onToggleGlide(EntityToggleGlideEvent event) {
        if (!this.config.settings.shouldPreventElytraUsage) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        UUID uniqueId = player.getUniqueId();

        if (!this.fightManager.isInCombat(uniqueId)) {
            return;
        }

        if (event.isGliding()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onFly(PlayerToggleFlightEvent event) {
        if (!this.config.settings.shouldPreventFlying) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (!this.fightManager.isInCombat(uniqueId)) {
            return;
        }

        if (event.isFlying()) {
            player.setAllowFlight(false);

            event.setCancelled(true);
        }
    }

    @EventHandler
    void onUnTag(FightUntagEvent event) {
        if (!this.config.settings.shouldPreventFlying) {
            return;
        }

        UUID uniqueId = event.getPlayer();
        Player player = this.server.getPlayer(uniqueId);

        if (player == null) {
            return;
        }

        player.setAllowFlight(true);
    }

    @EventHandler
    void onDamage(EntityDamageEvent event) {
        if (!this.config.settings.shouldElytraDisableOnDamage) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        UUID uniqueId = player.getUniqueId();

        if (this.fightManager.isInCombat(uniqueId) && player.isGliding()) {
            player.setGliding(false);
        }
    }

    @EventHandler
    void onOpenInventory(InventoryOpenEvent event) {
        if (!this.config.settings.shouldPreventInventoryOpening) {
            return;
        }

        Player player = (Player) event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (!this.fightManager.isInCombat(uniqueId)) {
            return;
        }

        event.setCancelled(true);

        this.announcer.create()
            .player(uniqueId)
            .notice(this.config.messages.inventoryBlockedDuringCombat)
            .send();

    }

    @EventHandler
    void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID playerUniqueId = player.getUniqueId();

        if (!this.fightManager.isInCombat(playerUniqueId)) {
            return;
        }

        String command = event.getMessage().split(" ")[0].substring(1).toLowerCase();

        boolean isMatchCommand = this.config.settings.blockedCommands.stream()
            .anyMatch(command::startsWith);

        WhitelistBlacklistMode mode = this.config.settings.commandBlockingMode;

        boolean shouldCancel = mode.shouldBlock(isMatchCommand);

        if (shouldCancel) {
            event.setCancelled(true);
            this.announcer.create()
                .player(playerUniqueId)
                .notice(this.config.messages.commandDisabledDuringCombat)
                .send();

        }
    }
}
