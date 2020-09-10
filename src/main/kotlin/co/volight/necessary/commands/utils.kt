package co.volight.necessary.commands

import co.volight.cell.Cell
import co.volight.necessary.lang.ModName
import co.volight.necessary.lang.TextName
import co.volight.necessary.text.langText
import co.volight.necessary.text.withStyle
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Util

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

