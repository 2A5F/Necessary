package co.volight.necessary.utils.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

fun <T : ArgumentBuilder<ServerCommandSource, T>> T.literal(literal: String): T {
    return this.then(CommandManager.literal(literal))
}

inline fun <T : ArgumentBuilder<ServerCommandSource, T>> T.literal(literal: String, f: LiteralArgumentBuilder<ServerCommandSource>.() -> Unit): T {
    return this.then(CommandManager.literal(literal).apply(f))
}

fun <T : ArgumentBuilder<ServerCommandSource, T>, A> T.argument(name: String, type: ArgumentType<A>): T {
    return this.then(CommandManager.argument(name, type))
}

inline fun <T : ArgumentBuilder<ServerCommandSource, T>, A> T.argument(name: String, type: ArgumentType<A>, f: RequiredArgumentBuilder<ServerCommandSource, A>.() -> Unit): T {
    return this.then(CommandManager.argument(name, type).apply(f))
}

inline fun CommandDispatcher<ServerCommandSource>.reg(literal: String, f: LiteralArgumentBuilder<ServerCommandSource>.() -> Unit): LiteralCommandNode<ServerCommandSource> {
    return this.register(CommandManager.literal(literal).apply(f))
}

