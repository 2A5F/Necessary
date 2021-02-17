package co.volight.necessary.commands.spawn

import co.volight.necessary.Nec
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object Names {
    const val spawn = "spawn"
}

object Spawn {
    fun reg() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                CommandManager.literal(Names.spawn)
                    .executes(::spawn)
            )
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.spawn}] registered")
    }
}

fun spawn(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player

    val world = sender.server.overworld
    val pos = world.spawnPos
    val angle = world.spawnAngle

    sender.teleport(world, pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 0f, angle)

    return 1
}