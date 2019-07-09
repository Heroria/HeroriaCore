package eu.heroria.npc;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import net.minecraft.server.v1_14_R1.Entity;

public class NpcManager {

	public NpcManager() {}
	
	public UUID newNpc(EntityType type, Location loc, String name, boolean customNameVisible, boolean AI) {
		Villager npc = (Villager) loc.getWorld().spawnEntity(loc, type);
		Entity nmsVillager = ((CraftEntity) npc).getHandle();
		nmsVillager.setCustomNameVisible(customNameVisible);
		nmsVillager.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		nmsVillager.setInvisible(false);
		npc.setCustomName(name);
		npc.setAI(AI);
		return npc.getUniqueId();
	}
}
