package me.katsumag.bedrockbases.base

import com.boydti.fawe.FaweAPI
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats
import com.sk89q.worldedit.history.UndoContext
import me.katsumag.bedrockbases.BedrockBases
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Base(private val plugin: BedrockBases, private val loc: Location, private val uuid: UUID) : Serializable {

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 836299282627
    }

   private val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    private val claimDate = dtf.format(LocalDateTime.now())
    val editSession = paste()
    val undoContext = UndoContext()

    init {
        //editSession?.changeSet?.backwardIterator()?.forEachRemaining { action -> action.undo(undoContext) }
        BaseManager.register(this)
    }

    fun getPlayerUUID(): UUID {
        return uuid
    }

    fun getPlayer(): OfflinePlayer {
        return Bukkit.getOfflinePlayer(uuid)
    }

    fun getLocation(): Location {
        return loc
    }

    fun getClaimDate(): String {
        return claimDate
    }

    private fun paste(): EditSession? {
        val file = File(plugin.dataFolder, "base.schematic")
        return ClipboardFormats.findByFile(file)?.load(file)?.paste(FaweAPI.getWorld(loc.world?.name), Vector(loc.x, loc.y, loc.z), true, true, null)
    }

}