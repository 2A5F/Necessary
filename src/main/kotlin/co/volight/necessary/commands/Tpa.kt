package co.volight.necessary.commands

import co.volight.expire.ExpireTable
import co.volight.necessary.Nec
import co.volight.necessary.text.*
import co.volight.necessary.utils.tp
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.util.Formatting
import java.util.*

fun ServerPlayerEntity.tpPrefix(): MutableText = this.langText(Nec.id, "${Nec.id}.tpa.prefix").withStyle { color(Formatting.GRAY) }

object Names {
    const val tpa = "tpa"
    const val target = "target"
    const val tpask = "tpask"
    const val tpayes = "tpayes"
    const val tpano = "tpano"
    const val tpaccept = "tpaccept"
    const val tpdeny = "tpdeny"
    const val tpreject = "tpreject"
}

object Tpa {
    fun reg() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            val node = dispatcher.register(
                literal(Names.tpa)
                    .then(
                        argument(Names.target, EntityArgumentType.player())
                            .executes(::tpa)
                    )
            )
            dispatcher.register(literal(Names.tpask).redirect(node))
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.tpa}, ${Names.tpask}] registered")

        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            val node = dispatcher.register(
                literal(Names.tpayes)
                    .executes(::tpaYes)
            )
            dispatcher.register(literal(Names.tpaccept).redirect(node))
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.tpayes}, ${Names.tpaccept}] registered")

        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            val node = dispatcher.register(
                literal(Names.tpano)
                    .executes(::tpaNo)
            )
            dispatcher.register(literal(Names.tpdeny).redirect(node))
            dispatcher.register(literal(Names.tpreject).redirect(node))
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.tpano}, ${Names.tpdeny}, ${Names.tpreject}] registered")
    }
}

var tptable: ExpireTable<UUID, UUID> = ExpireTable(60000)

fun tpa(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player
    val target = EntityArgumentType.getPlayer(ctx, Names.target)

    tptable[target.uuid] = sender.uuid

    sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.msg", { color(Formatting.GOLD) })  {
        put("target") { LiteralText(target.entityName).withStyle { color(Formatting.WHITE) } }
    }

    target.sendStyledLangMsg(target.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.to", { color(Formatting.GOLD) }) {
        put("asker") { LiteralText(sender.entityName).withStyle { color(Formatting.WHITE) } }
        put("you") { target.langText(Nec.id, "${Nec.id}.tpa.req.you").withStyle { color(Formatting.GREEN) } }
    }

    target.sendSysMsg(
        target.tpPrefix(),
        LiteralText("       "),
        target.langText(Nec.id, "${Nec.id}.tpa.yes").withStyle {
            bold(); color(Formatting.GREEN)
            hoverEvent(HoverEvent.Action.SHOW_TEXT, target.langText(Nec.id, "${Nec.id}.tpa.yes.to.hover").withStyle {
                color(
                    Formatting.GREEN
                )
            })
            clickEvent(ClickEvent.Action.RUN_COMMAND, "/${Names.tpayes}")
        },
        LiteralText("       "),
        target.langText(Nec.id, "${Nec.id}.tpa.no").withStyle {
            bold(); color(Formatting.RED)
            hoverEvent(HoverEvent.Action.SHOW_TEXT, target.langText(Nec.id, "${Nec.id}.tpa.no.to.hover").withStyle {
                color(
                    Formatting.RED
                )
            })
            clickEvent(ClickEvent.Action.RUN_COMMAND, "/${Names.tpano}")
        },
    )

    return 1
}

fun tpaYes(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player

    val targetData = tptable.remove(sender.uuid)
    val target = if (targetData == null) null else source.minecraftServer.playerManager.getPlayer(targetData)

    return if (target == null) {
        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id,"${Nec.id}.tpa.res.msg.nil") { color(Formatting.RED) }
        0
    } else {
        target.tp(sender)

        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.res.msg.yes", { color(Formatting.GREEN) }) {
            put("asker") { LiteralText(target.entityName).withStyle { color(Formatting.WHITE) } }
        }

        target.sendStyledLangMsg(target.tpPrefix(), Nec.id, "${Nec.id}.tpa.res.to.yes", { color(Formatting.GREEN) }) {
            put("target") { LiteralText(sender.entityName).withStyle { color(Formatting.WHITE) } }
        }
        1
    }
}

fun tpaNo(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player

    val targetData = tptable.remove(sender.uuid)
    val target = if (targetData == null) null else source.minecraftServer.playerManager.getPlayer(targetData)

    return if (target == null) {
        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id,"${Nec.id}.tpa.res.msg.nil") { color(Formatting.RED) }
        0
    } else {
        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.res.msg.no", { color(Formatting.RED) }) {
            put("asker") { LiteralText(target.entityName).withStyle { color(Formatting.WHITE) } }
        }

        target.sendStyledLangMsg(target.tpPrefix(), Nec.id, "${Nec.id}.tpa.res.to.no", { color(Formatting.RED) }) {
            put("target") { LiteralText(sender.entityName).withStyle { color(Formatting.WHITE) } }
        }
        1
    }
}













