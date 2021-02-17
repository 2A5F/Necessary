package co.volight.necessary.commands.suicide

import co.volight.necessary.Nec
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object Names {
    const val suicide = "suicide"
}

object Suicide {
    fun reg() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                CommandManager.literal(Names.suicide)
                    .executes(::suicide)
            )
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.suicide}] registered")
    }
}

fun suicide(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player

    sender.kill()

    return 1;
}
