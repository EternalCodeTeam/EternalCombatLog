package com.eternalcode.combat.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.util.CreatureButcher;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.Objects;
import org.bukkit.Location;

import java.util.List;
import org.bukkit.Server;
import org.bukkit.World;

public class WorldGuardRegionProvider implements RegionProvider {

    private final List<String> regions;
    private final boolean blockRegionsWithPvpFlag;
    private final List<String> worldNames;
    private final WorldGuard worldGuard;
    private final Server server;

    public WorldGuardRegionProvider(
        List<String> regions,
        boolean blockRegionsWithPvpFlag,
        List<String> worldNames,
        Server server
    ) {
        this.regions = regions;
        this.blockRegionsWithPvpFlag = blockRegionsWithPvpFlag;
        this.worldNames = worldNames;
        this.worldGuard = WorldGuard.getInstance();
        this.server = server;

        RegionContainer regionContainer = this.worldGuard.getPlatform().getRegionContainer();



        worldNames.forEach(worldName -> {
            if (worldName == null) {
                return;
            }
            RegionManager regions = regionContainer.get(BukkitAdapter.adapt(
                Objects.requireNonNull(this.server.getWorld(worldName))
            ));

            regions.get

        });

        this.worldGuard.getPlatform().getRegionContainer().get();


    }

    // merge regions provided by config and regions with pvp flag









//    @Override
//    public boolean isInRegion(Location location) {
//        return this.regions.stream().anyMatch(region -> this.isInCombatRegion(location, region));
//    }

//    @Override
//    public Location getRegionCenter(Location location) {
//        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
//        RegionQuery regionQuery = regionContainer.createQuery();
//        ApplicableRegionSet applicableRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));
//
//        double minX = 0;
//        double maxX = 0;
//        double minZ = 0;
//        double maxZ = 0;
//
//        for (ProtectedRegion region : applicableRegions.getRegions()) {
//            BlockVector3 min = region.getMinimumPoint();
//            BlockVector3 max = region.getMaximumPoint();
//
//            if (min.getX() < minX) {
//                minX = min.getX();
//            }
//            if (max.getX() > maxX) {
//                maxX = max.getX();
//            }
//            if (min.getZ() < minZ) {
//                minZ = min.getZ();
//            }
//            if (max.getZ() > maxZ) {
//                maxZ = max.getZ();
//            }
//        }
//
//        double x = (maxX - minX) / 2 + minX;
//        double z = (maxZ - minZ) / 2 + minZ;
//
//        return new Location(location.getWorld(), x, location.getY(), z);
//    }
//
//    private boolean isInCombatRegion(Location location, String regionName) {
//        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
//        RegionQuery regionQuery = regionContainer.createQuery();
//        ApplicableRegionSet applicableRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));
//
//        return applicableRegions.getRegions().stream().anyMatch(region -> region.getId().equalsIgnoreCase(regionName));
//    }
}
