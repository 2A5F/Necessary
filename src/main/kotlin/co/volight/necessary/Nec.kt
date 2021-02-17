package co.volight.necessary

import co.volight.necessary.commands.suicide.Suicide
import co.volight.necessary.commands.tpa.Tpa
import co.volight.necessary.events.PlayerLangInfo
import co.volight.necessary.lang.Lang
import org.apache.logging.log4j.LogManager

object Nec {
    const val id = "necessary"
    const val logName = "[Nec]"
    val LOGGER = LogManager.getLogger(id)!!
}

fun init() {
    Lang.reg(Nec.id)
    regEvents()
    regCommands()
}

fun regEvents() {
    PlayerLangInfo.regEvent()
}

fun regCommands() {
    Tpa.reg()
    Suicide.reg()
}