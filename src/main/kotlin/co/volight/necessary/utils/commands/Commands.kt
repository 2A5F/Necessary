package co.volight.necessary.utils

import co.volight.necessary.commands.tpa.Names
import co.volight.necessary.commands.tpa.tpa
import co.volight.necessary.utils.CmdReg.Companion.cmdReg
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class CmdReg<T : ArgumentBuilder<ServerCommandSource, T>>(val builder: T) {

    companion object {
        inline fun <T: ArgumentBuilder<ServerCommandSource, T>> T.cmdReg(f: CmdReg<T>.() -> Unit): T {
            return CmdReg(this).apply(f).builder
        }
    }

    fun literal(literal: String) {
        builder.then(CommandManager.literal(literal))
    }

    inline fun literal(literal: String, f: CmdReg<LiteralArgumentBuilder<ServerCommandSource>>.() -> Unit) {
        builder.then(CommandManager.literal(literal).cmdReg(f))
    }

    fun <A> argument(name: String, type: ArgumentType<A>) {
        builder.then(CommandManager.argument(name, type))
    }

    inline fun <A> argument(name: String, type: ArgumentType<A>, f: CmdReg<RequiredArgumentBuilder<ServerCommandSource, A>>.() -> Unit) {
        builder.then(CommandManager.argument(name, type).cmdReg(f))
    }

    fun executes(command: Command<ServerCommandSource>) {
        builder.executes(command)
    }
}

inline fun CommandDispatcher<ServerCommandSource>.reg(literal: String, f: CmdReg<LiteralArgumentBuilder<ServerCommandSource>>.() -> Unit): LiteralCommandNode<ServerCommandSource> {
    return this.register(CommandManager.literal(literal).cmdReg(f))
}

fun test() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        val node = dispatcher.reg(Names.tpa) {
            argument(Names.target, EntityArgumentType.player()) {
                executes(::tpa)
            }
        }
    }

    CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        val node = dispatcher.register(
            CommandManager.literal(Names.tpa)
                .then(
                    CommandManager.argument(Names.target, EntityArgumentType.player())
                        .executes(::tpa)
                )
        )
        dispatcher.register(CommandManager.literal(Names.tpask).redirect(node))
    }
}