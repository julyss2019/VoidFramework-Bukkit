package com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.Serializable;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializable implements Serializable<Location> {
    @Override
    public Location get(@NonNull Section parent, @NonNull String path) {
        Section locSection = parent.getSection(path);
        String worldId = locSection.getString("world");
        World world = Bukkit.getWorld(worldId);

        if (world == null) {
            throw new RuntimeException("world " + worldId + " not exists");
        }

        return new Location(Bukkit.getWorld(worldId),
                locSection.getDouble("x"),
                locSection.getDouble("y"),
                locSection.getDouble("z"),
                locSection.getFloat("yaw"),
                locSection.getFloat("pitch"));
    }

    @Override
    public void set(@NonNull Section parent, @NonNull String path, @Nullable Location loc) {
        if (loc == null) {
            parent.setByBukkit(path, null);
            return;
        }

        Section locSection = parent.getOrCreateSection(path);

        locSection.setByBukkit(path, loc.getWorld().getName());
        locSection.setByBukkit(path, loc.getWorld().getName());
    }
}
