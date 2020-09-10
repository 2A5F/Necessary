package co.volight.necessary.utils

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ChunkTicketType
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import java.util.*

fun ServerPlayerEntity.tp(target: ServerPlayerEntity) {
    val d = target.x
    val e = target.y
    val f = target.z
    val g = target.yaw
    val h = target.pitch
    val set: Set<PlayerPositionLookS2CPacket.Flag> = EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag::class.java)

    val world = target.world as ServerWorld
    val chunkPos = ChunkPos(BlockPos(d, e, f))
    world.chunkManager.addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, this.entityId)
    this.stopRiding()
    if (this.isSleeping) {
        this.wakeUp(true, true)
    }

    if (world === this.world) {
        this.networkHandler.teleportRequest(d, e, f, g, h, set)
    } else {
        this.teleport(world, d, e, f, g, h)
    }

    this.setHeadYaw(g)
}