package me.katsumag.bedrockbases.base

import me.katsumag.bedrockbases.BedrockBases
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import java.util.stream.Collectors

object BaseManager {

    var BASE_LIST = mutableSetOf<Base>()
    private val dataFile = File(JavaPlugin.getPlugin(BedrockBases::class.java).dataFolder, "data.dat")

    init {
        if (! dataFile.exists()) dataFile.mkdir()
    }

    fun hasBase(player: UUID): Boolean {
        BASE_LIST.forEach { if (it.getPlayer().uniqueId == player) return true }
        return false
    }

    fun register(base: Base) {
        BASE_LIST.add(base)
    }

    fun remove(base: Base) {
        BASE_LIST.remove(base)
        base.editSession.changeSet?.backwardIterator()?.forEachRemaining { action -> action.undo(base.undoContext) }
    }

    fun getBase(player: UUID): Base? {
        if (!hasBase(player)) return null;
        return BASE_LIST.stream().filter { base -> base.getPlayerUUID() == player }.findFirst().get()
    }

    fun collidesWithBase(loc: Location): Boolean {
        val locations = BASE_LIST.map { it.getLocation() }
        locations.forEach { location ->
            run {
                if (location.distance(loc) < 20) {
                    return true;
                }
            }
        }
        return false;
    }

    fun saveEditSessions() {
        dataFile.outputStream().use { ObjectOutputStream(it).use { it.writeObject(BASE_LIST) } }
    }

    private fun getSavedEditSessions(): MutableSet<Base> {
        return try {
            dataFile.inputStream().use { ObjectInputStream(it).use { it.readObject() as MutableSet<Base> } }
        } catch (exception: Exception) {
            mutableSetOf()
        }
    }

    fun loadEditSessions() {
        getSavedEditSessions().let { BASE_LIST = it }
    }

    fun getWithinRange(loc: Location): Base {
        return BASE_LIST.first {
            loc.distance(it.getLocation()) < 20
        }
    }

}