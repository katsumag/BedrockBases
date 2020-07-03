package me.katsumag.bedrockbases.base

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.stream.Collectors

object BaseManager {

    private val BASE_LIST = mutableSetOf<Base>()

    fun hasBase(player: Player): Boolean {
        BASE_LIST.forEach { if (it.getPlayer().uniqueId == player.uniqueId) return true }
        return false
    }

    fun register(base: Base) {
        BASE_LIST.add(base)
    }

    fun remove(base: Base) {
        BASE_LIST.remove(base)
    }

    fun getBase(player: Player): Base? {
        if (!hasBase(player)) return null;
        return BASE_LIST.stream().filter { base -> base.getPlayerUUID() == player.uniqueId }.findFirst().get()
    }

    fun collidesWithBase(loc: Location): Boolean {
        val locations = BASE_LIST.stream().map { location -> location.getLocation() }.collect(Collectors.toUnmodifiableSet())
        locations.forEach { location ->
            run {
                if (location.distance(loc) < 20) {
                    return true;
                }
            }
        }
        return false;
    }

}