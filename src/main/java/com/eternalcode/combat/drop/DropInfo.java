package com.eternalcode.combat.drop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DropInfo {

    private final Player player;
    private final Player killer;

    private List<ItemStack> droppedItems;
    private int droppedExp;

    private DropInfo(Player player, Player killer, List<ItemStack> droppedItems, int droppedExp) {
        this.player = player;
        this.killer = killer;

        this.droppedItems = droppedItems;
        this.droppedExp = droppedExp;
    }

    public List<ItemStack> getDroppedItems() {
        return new ArrayList<>(this.droppedItems);
    }

    public void setDroppedItems(List<ItemStack> droppedItems) {
        this.droppedItems = droppedItems;
    }

    public Player getPlayer() {
        return this.player;
    }

    public @Nullable Player getKiller() {
        return this.killer;
    }

    public boolean hasKiller() {
        return this.killer != null;
    }

    public int getDroppedExp() {
        return this.droppedExp;
    }

    public void setDroppedExp(int droppedExp) {
        this.droppedExp = droppedExp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Player player;
        private Player killer;
        private List<ItemStack> droppedItems;
        private int droppedExp;

        public Builder player(@NotNull Player player) {
            this.player = player;
            return this;
        }

        public Builder killer(@Nullable Player killer) {
            this.killer = killer;
            return this;
        }

        public Builder droppedItems(@NotNull List<ItemStack> droppedItems) {
            this.droppedItems = droppedItems;
            return this;
        }

        public Builder droppedExp(int droppedExp) {
            this.droppedExp = droppedExp;
            return this;
        }

        public DropInfo build() {
            return new DropInfo(
              this.player,
              this.killer,
              this.droppedItems,
              this.droppedExp
            );
        }
    }
}
