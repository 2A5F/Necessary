package co.volight.utils

fun<T> List<T>.tryGet(index: Int): T? = if (index >= this.size) null else this[index]

fun<T> List<T>.subLast(index: Int): List<T> = this.subList(index, this.size)