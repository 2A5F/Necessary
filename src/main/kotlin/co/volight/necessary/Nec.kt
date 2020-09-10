package co.volight.necessary

import co.volight.necessary.commands.Tpa
import co.volight.necessary.events.PlayerLangInfo
import co.volight.necessary.lang.Lang
import org.apache.logging.log4j.LogManager

object Nec {
    const val id = Necessary.id
    val LOGGER = Necessary.LOGGER
}

object Necessary {
    const val id = "necessary"
    val LOGGER = LogManager.getLogger()!!
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
}