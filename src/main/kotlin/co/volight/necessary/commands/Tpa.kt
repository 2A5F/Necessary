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
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import java.util.*

data class TpaData(val id: UUID, val here: Boolean)

val TPA_TABLE: ExpireTable<UUID, TpaData> = ExpireTable(60000)

object Names {
    const val target = "target"

    const val tpa = "tpa"
    const val tpask = "tpask"

    const val tpahere = "tpahere"

    const val tpayes = "tpayes"
    const val tpaccept = "tpaccept"

    const val tpano = "tpano"
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
                literal(Names.tpahere)
                    .then(
                        argument(Names.target, EntityArgumentType.player())
                            .executes(::tapHere)
                    )
            )
            dispatcher.register(literal(Names.tpask).redirect(node))
        }
        Nec.LOGGER.info("${Nec.logName} Command [${Names.tpahere}] registered")

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



fun tpa(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player
    val target = EntityArgumentType.getPlayer(ctx, Names.target)

    TPA_TABLE[target.uuid] = TpaData(sender.uuid, false)

    sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.msg.to", { color(Formatting.GOLD) })  {
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

fun tapHere(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player
    val target = EntityArgumentType.getPlayer(ctx, Names.target)

    TPA_TABLE[target.uuid] = TpaData(sender.uuid, true)

    sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.msg.here", { color(Formatting.GOLD) })  {
        put("target") { LiteralText(target.entityName).withStyle { color(Formatting.WHITE) } }
    }

    target.sendStyledLangMsg(target.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.here", { color(Formatting.GOLD) }) {
        put("asker") { LiteralText(sender.entityName).withStyle { color(Formatting.WHITE) } }
        put("you") { target.langText(Nec.id, "${Nec.id}.tpa.req.you").withStyle { color(Formatting.GRAY) } }
        put("him") { target.langText(Nec.id, "${Nec.id}.tpa.req.him").withStyle { color(Formatting.GREEN) } }
    }

    target.sendSysMsg(
        target.tpPrefix(),
        LiteralText("       "),
        target.langText(Nec.id, "${Nec.id}.tpa.yes").withStyle {
            bold(); color(Formatting.GREEN)
            hoverEvent(HoverEvent.Action.SHOW_TEXT, target.langText(Nec.id, "${Nec.id}.tpa.yes.here.hover").withStyle {
                color(
                    Formatting.GREEN
                )
            })
            clickEvent(ClickEvent.Action.RUN_COMMAND, "/${Names.tpayes}")
        },
        LiteralText("       "),
        target.langText(Nec.id, "${Nec.id}.tpa.no").withStyle {
            bold(); color(Formatting.RED)
            hoverEvent(HoverEvent.Action.SHOW_TEXT, target.langText(Nec.id, "${Nec.id}.tpa.no.here.hover").withStyle {
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

    val targetData = TPA_TABLE.remove(sender.uuid)
    val target = if (targetData == null) null else source.minecraftServer.playerManager.getPlayer(targetData.id)

    return if (targetData == null ||target == null) {
        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id,"${Nec.id}.tpa.res.msg.nil") { color(Formatting.RED) }
        0
    } else {
        if (targetData.here) sender.tp(target) else target.tp(sender)

        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.res.msg.yes", { color(Formatting.GREEN) }) {
            put("asker") { LiteralText(target.entityName).withStyle { color(Formatting.WHITE) } }
        }

        target.sendStyledLangMsg(target.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.${if (targetData.here) "here" else "to"}.yes", { color(Formatting.GREEN) }) {
            put("target") { LiteralText(sender.entityName).withStyle { color(Formatting.WHITE) } }
        }
        1
    }
}

fun tpaNo(ctx: CommandContext<ServerCommandSource>): Int {
    val source = ctx.source
    val sender = source.player

    val targetData = TPA_TABLE.remove(sender.uuid)
    val target = if (targetData == null) null else source.minecraftServer.playerManager.getPlayer(targetData.id)

    return if (targetData == null || target == null) {
        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id,"${Nec.id}.tpa.res.msg.nil") { color(Formatting.RED) }
        0
    } else {
        sender.sendStyledLangMsg(sender.tpPrefix(), Nec.id, "${Nec.id}.tpa.res.msg.no", { color(Formatting.RED) }) {
            put("asker") { LiteralText(target.entityName).withStyle { color(Formatting.WHITE) } }
        }

        target.sendStyledLangMsg(target.tpPrefix(), Nec.id, "${Nec.id}.tpa.req.${if (targetData.here) "here" else "to"}.no", { color(Formatting.RED) }) {
            put("target") { LiteralText(sender.entityName).withStyle { color(Formatting.WHITE) } }
        }
        1
    }
}













