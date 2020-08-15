package me.katsumag.bedrockbases.base

import com.sk89q.worldedit.history.UndoContext
import me.katsumag.bedrockbases.BedrockBases
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

object BaseManager {

    private var _baseList = mutableSetOf<Base>()
    val baseList: List<Base>
        get() = _baseList.toList()
    private val undoContext = UndoContext()
    private val dataFile = File(JavaPlugin.getPlugin(BedrockBases::class.java).dataFolder, "data.dat")

    init {
        if (! dataFile.exists()) dataFile.createNewFile()
    }

    fun hasBase(player: UUID): Boolean {
        return _baseList.any {it.getPlayer().uniqueId == player}
    }

    fun register(base: Base) {
        _baseList.add(base)
    }

    fun remove(base: Base) {
        _baseList.remove(base)
        base.editSession.forEach { action -> action.undo(undoContext) }
    }

    fun getBase(player: UUID): Base? {
        if (!hasBase(player)) return null;
        return _baseList.stream().filter { base -> base.getPlayerUUID() == player }.findFirst().get()
    }

    fun collidesWithBase(loc: Location): Boolean {
        val locations = _baseList.map { Location.deserialize(it.serializedLocation) }
        return locations.any { location -> location.distanceSquared(loc) < 400 }
    }

    fun saveEditSessions() {
        dataFile.outputStream().use { ObjectOutputStream(it).use { it.writeObject(_baseList) } }
    }

     fun getAndLoadSavedEditSessions() {
         _baseList = try {
             dataFile.inputStream().use { ObjectInputStream(it).use { it.readObject() as MutableSet<Base> } }
         } catch (exception: Exception) {
             exception.printStackTrace()
             mutableSetOf()
         }
    }

    fun getWithinRange(loc: Location): Base {
        return _baseList.first {
            loc.distanceSquared(Location.deserialize(it.serializedLocation)) < 400
        }
    }

}