package me.croshaw.firetweaks.registry;

import me.croshaw.firetweaks.event.AttackEntityCallback;
import me.croshaw.firetweaks.handlers.AttackEntityHandler;
import me.croshaw.firetweaks.handlers.ServerTickHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class EventsRegistry {
    public static void registry() {
        AttackEntityCallback.EVENT.register(AttackEntityHandler::handle);
        ServerTickEvents.END_WORLD_TICK.register(ServerTickHandler::handle);
    }
}
