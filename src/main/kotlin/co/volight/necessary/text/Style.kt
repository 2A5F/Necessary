package co.volight.necessary.text

import co.volight.cell.Cell
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Style
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting

inline fun styleOf(f: Cell<Style>.() -> Unit): Style {
    val style = Cell(Style.EMPTY)
    style.f()
    return style.value
}

fun Cell<Style>.color(formatting: Formatting) {
    this.value = this.value.withColor(formatting)
}

fun Cell<Style>.color(textColor: TextColor) {
    this.value = this.value.withColor(textColor)
}

fun Cell<Style>.bold() {
    this.value = this.value.withBold(true)
}

fun Cell<Style>.bold(bold: Boolean) {
    this.value = this.value.withBold(bold)
}

fun Cell<Style>.italic() {
    this.value = this.value.withItalic(true)
}

fun Cell<Style>.italic(italic: Boolean) {
    this.value = this.value.withItalic(italic)
}

fun Cell<Style>.hoverEvent(hoverEvent: HoverEvent) {
    this.value = this.value.withHoverEvent(hoverEvent)
}

fun <T> Cell<Style>.hoverEvent(action: HoverEvent.Action<T> , obj: T) {
    this.value = this.value.withHoverEvent(HoverEvent(action, obj))
}

fun Cell<Style>.clickEvent(clickEvent: ClickEvent) {
    this.value = this.value.withClickEvent(clickEvent)
}

fun Cell<Style>.clickEvent(action: ClickEvent.Action, string: String) {
    this.value = this.value.withClickEvent(ClickEvent(action, string))
}