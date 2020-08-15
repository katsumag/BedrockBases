package me.katsumag.bedrockbases.base

import com.sk89q.worldedit.history.change.BlockChange
import com.sk89q.worldedit.history.change.Change
import me.katsumag.bedrockbases.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.io.File
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Base(val serializedLocation: Map<String, Any>, private val uuid: UUID) : Serializable {

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 836299282627
    }

    private val claimDate = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now())
    val editSession = paste().asSequence().toMutableList().filterIsInstance().map { BlockChange(it.position.toLocation().serialize(), it.previous.asString, it.current.asString) }

    init {
        BaseManager.register(this)
    }

    fun getPlayerUUID(): UUID {
        return uuid
    }

    fun getPlayer(): OfflinePlayer {
        return Bukkit.getOfflinePlayer(uuid)
    }

    fun getClaimDate(): String {
        return claimDate
    }

    private fun paste(): Iterator<Change> {
        return File(getPlugin().dataFolder, "BedrockBase.schem").loadAndPasteSchematic(Location.deserialize(serializedLocation)).changeSet.backwardIterator()
    }

}