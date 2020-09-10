package co.volight.necessary.text

import co.volight.cell.Cell
import net.minecraft.text.MutableText
import net.minecraft.text.Style

inline fun MutableText.withStyle(f: Cell<Style>.() -> Unit) = this.setStyle(styleOf(f))