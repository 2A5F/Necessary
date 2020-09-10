package co.volight.necessary.text

import co.volight.necessary.Nec
import co.volight.necessary.events.PlayerLangInfo
import co.volight.necessary.lang.*
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import java.lang.RuntimeException

fun ServerPlayerEntity.langText(modName: ModName, textName: TextName): MutableText = this.langText(modName, textName, mapOf())

fun ServerPlayerEntity.langText(modName: ModName, textName: TextName, args: Map<String, () -> Text>): MutableText {
    val langName = PlayerLangInfo.get(this)
    val str = Lang.getStr(langName, modName, textName)
    if (str == null) {
        Nec.LOGGER.info("The language \"${langName}\" used by player ${this.entityName} does not exist")
        return LiteralText(textName)
    }
    val texts = str.strs.map {
        when (it) {
            is LangStrType.Str -> LiteralText(it.str)
            is LangStrType.Arg -> args[it.arg]?.let { it() } ?: LiteralText("{{${it.arg}}}")
            else -> throw RuntimeException("Never Branch")
        }
    }
    var root: MutableText = LiteralText("")
    for (text in texts) {
        root = root.append(text)
    }
    return root
}

inline fun ServerPlayerEntity.langText(modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
    val map = mutableMapOf<String, () -> Text>()
    map.args()
    return this.langText(modName, textName, map)
}

fun langText(langName: LangName, modName: ModName, textName: TextName) : MutableText = langText(langName, modName, textName, mapOf())

fun langText(langName: LangName, modName: ModName, textName: TextName, args: Map<String, () -> Text>) : MutableText {
    val str = Lang.getStr(langName, modName, textName)
    if (str == null) {
        Nec.LOGGER.info("The language \"${langName}\" does not exist")
        return LiteralText(textName)
    }
    val texts = str.strs.map {
        when (it) {
            is LangStrType.Str -> LiteralText(it.str)
            is LangStrType.Arg -> args[it.arg]?.let { it() } ?: LiteralText("{{${it.arg}}}")
            else -> throw RuntimeException("Never Branch")
        }
    }
    var root: MutableText = LiteralText("")
    for (text in texts) {
        root = root.append(text)
    }
    return root
}

fun langText(langName: LangName, modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) : MutableText {
    val map = mutableMapOf<String, () -> Text>()
    map.args()
    return langText(langName, modName, textName, map)
}








