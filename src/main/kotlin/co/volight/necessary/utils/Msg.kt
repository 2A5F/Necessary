package co.volight.necessary.utils

import co.volight.cell.Cell
import co.volight.necessary.Nec
import co.volight.necessary.lang.ModName
import co.volight.necessary.lang.TextName
import co.volight.necessary.text.color
import co.volight.necessary.text.langText
import co.volight.necessary.text.withStyle
import net.minecraft.entity.Entity
import net.minecraft.network.MessageType
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Util

fun ServerPlayerEntity.tpPrefix(): MutableText = this.langText(Nec.id, "${Nec.id}.tpa.prefix").withStyle { color( Formatting.GRAY) }

fun Packet<*>.sendTo(player: ServerPlayerEntity) {
    player.networkHandler.sendPacket(this)
}

fun Text.intoSysMsgPack(): GameMessageS2CPacket {
    return GameMessageS2CPacket(this, MessageType.SYSTEM, Util.NIL_UUID)
}

fun prefixMsg(prefix: Text, text: Text): Text {
    return LiteralText("").append(prefix).append(LiteralText(" ")).append(text)
}

fun prefixMsg(prefix: Text, root: MutableText, vararg texts: Text): Text {
    var root_ = root
    for (text in texts) {
        root_ = root_.append(text)
    }
    return LiteralText("").append(prefix).append(LiteralText(" ")).append(root_)
}

fun ServerPlayerEntity.langMsg(modName: ModName, textName: TextName): Text {
    return this.langText(modName, textName, mapOf())
}

fun ServerPlayerEntity.langMsg(prefix: Text, modName: ModName, textName: TextName): Text {
    return prefixMsg(prefix, this.langText(modName, textName, mapOf()))
}

fun ServerPlayerEntity.langMsg(modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit): Text {
    return this.langText(modName, textName, args)
}

fun ServerPlayerEntity.langMsg(prefix: Text, modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit): Text {
    return prefixMsg(prefix, this.langText(modName, textName, args))
}

fun ServerPlayerEntity.styledLangMsg(modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit): Text {
    return this.langText(modName, textName, mapOf()).withStyle(style)
}

fun ServerPlayerEntity.styledLangMsg(prefix: Text, modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit): Text {
    return prefixMsg(prefix, this.langText(modName, textName, mapOf()).withStyle(style))
}

fun ServerPlayerEntity.styledLangMsg(modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit, args: MutableMap<String, () -> Text>.() -> Unit): Text {
    return this.langText(modName, textName, args).withStyle(style)
}

fun ServerPlayerEntity.styledLangMsg(prefix: Text, modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit, args: MutableMap<String, () -> Text>.() -> Unit): Text {
    return prefixMsg(prefix, this.langText(modName, textName, args).withStyle(style))
}

fun Entity.sendSysMsg(text: Text) {
    this.sendSystemMessage(text, Util.NIL_UUID)
}

fun Entity.sendSysMsg(prefix: Text, text: Text) {
    this.sendSysMsg(LiteralText("").append(prefix).append(LiteralText(" ")).append(text))
}

fun Entity.sendSysMsg(prefix: Text, root: MutableText, vararg texts: Text) {
    var root_ = root
    for (text in texts) {
        root_ = root_.append(text)
    }
    this.sendSysMsg(LiteralText("").append(prefix).append(LiteralText(" ")).append(root_))
}

fun ServerPlayerEntity.sendLangMsg(modName: ModName, textName: TextName) {
    this.sendSysMsg(this.langText(modName, textName, mapOf()))
}

fun ServerPlayerEntity.sendLangMsg(prefix: Text, modName: ModName, textName: TextName) {
    this.sendSysMsg(prefix, this.langText(modName, textName, mapOf()))
}

inline fun ServerPlayerEntity.sendLangMsg(modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) {
    this.sendSysMsg(this.langText(modName, textName, args))
}

inline fun ServerPlayerEntity.sendLangMsg(prefix: Text, modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) {
    this.sendSysMsg(prefix, this.langText(modName, textName, args))
}

inline fun ServerPlayerEntity.sendStyledLangMsg(modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit) {
    this.sendSysMsg(this.langText(modName, textName, mapOf()).withStyle(style))
}

inline fun ServerPlayerEntity.sendStyledLangMsg(prefix: Text, modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit) {
    this.sendSysMsg(prefix, this.langText(modName, textName, mapOf()).withStyle(style))
}


inline fun ServerPlayerEntity.sendStyledLangMsg(modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit, args: MutableMap<String, () -> Text>.() -> Unit) {
    this.sendSysMsg(this.langText(modName, textName, args).withStyle(style))
}

inline fun ServerPlayerEntity.sendStyledLangMsg(prefix: Text, modName: ModName, textName: TextName, style: Cell<Style>.() -> Unit, args: MutableMap<String, () -> Text>.() -> Unit) {
    this.sendSysMsg(prefix, this.langText(modName, textName, args).withStyle(style))
}

