package co.volight.necessary.commands.tp

import co.volight.necessary.Nec
import co.volight.necessary.utils.tp
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object Names {
    const val target = "target"

    const val tphere = "tphere"

    const val tpall = "tpall"
}

object Tp {
    fun reg() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                CommandManager.literal(Names.tphere)
                    .requires { it.hasPermissionLevel(2) }
                    .then(
                        CommandManager.argument(Names.target, EntityArgumentType.player())
                            .executes(::tpHere)
                    )
            )
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.tphere}] registered")

        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                CommandManager.literal(Names.tpall)
                    .requires { it.hasPermissionLevel(2) }
                    .executes(::tpAll)
            )
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.tpall}] registered")
    }
}

fun tpHere(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player
    val target = EntityArgumentType.getPlayer(ctx, Names.target)

    if (target.uuid == sender.uuid) return 1;

    target.tp(sender)

    return 1
}

fun tpAll(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player

    for (target in sender.server.playerManager.playerList) {
        if (target.uuid == sender.uuid) continue

        target.tp(sender)
    }

    return 1
}