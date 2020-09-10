package co.volight.expire

import java.io.Serializable
import java.util.*

class ExpireTable<K, V> : MutableMap<K, V>, Cloneable, Serializable {
    private val table = mutableMapOf<K, ExpireBox<V>>()
    var defaultTimeOut: Long = 0
    var autoUpdate: Long = 1800000

    constructor() {
        init()
    }

    constructor(defaultTimeOut: Long) {
        this.defaultTimeOut = defaultTimeOut
        init()
    }

    constructor(defaultTimeOut: Long, autoUpdate: Long) {
        this.defaultTimeOut = defaultTimeOut
        this.autoUpdate = autoUpdate
        init()
    }

    private fun init() {
        update()
    }

    @Synchronized
    fun update() {
        if (autoUpdate > 0) {
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    update()
                }
            }, autoUpdate)
        }
        if (table.isEmpty()) return
        table.entries.removeIf { entry: Map.Entry<K, ExpireBox<V>> -> entry.value.isExpired }
    }

    fun setAutoUpdateTime(time: Long) {
        autoUpdate = time
    }

    @get:Synchronized
    override val size: Int get() {
        if (table.isEmpty()) return 0
        val iter: MutableIterator<Map.Entry<K, ExpireBox<V>>> = table.entries.iterator()
        var c = 0
        while (iter.hasNext()) {
            val entry = iter.next()
            if (entry.value.isExpired) {
                iter.remove()
                continue
            }
            ++c
        }
        return c
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    @Synchronized
    override fun containsKey(key: K): Boolean {
        val v = table[key] ?: return false
        if (v.isExpired) {
            table.remove(key)
            return false
        }
        return true
    }

    @Deprecated("")
    override fun containsValue(value: V): Boolean {
        throw UnsupportedOperationException()
    }

    @Synchronized
    override operator fun get(key: K): V? {
        val v = table[key] ?: return null
        if (v.isExpired) {
            table.remove(key)
            return null
        }
        return v.value
    }

    override fun put(key: K, value: V): V? {
        return put(key, value, defaultTimeOut)
    }

    @Synchronized
    fun put(key: K, value: V, timeOut: Long): V? {
        val box = ExpireBox(value, timeOut)
        val rep = table.put(key, box) ?: return null
        return rep.value
    }

    fun uptime(key: K): Boolean {
        return uptime(key, defaultTimeOut)
    }

    @Synchronized
    fun uptime(key: K, timeOut: Long): Boolean {
        val box = table[key] ?: return false
        box.timeOut = timeOut
        return true
    }

    @Synchronized
    fun reset(key: K) {
        val box = table[key] ?: return
        box.time = System.currentTimeMillis()
    }

    @Synchronized
    override fun remove(key: K): V? {
        val rep = table.remove(key) ?: return null
        return rep.value
    }

    @Synchronized
    override fun putAll(from: Map<out K, V>) {
        for ((key, value) in from) {
            put(key, value)
        }
    }

    @Synchronized
    override fun clear() {
        table.clear()
    }

    @get:Synchronized
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() {
        val set = mutableSetOf<MutableMap.MutableEntry<K, V>>()
        val iter: MutableIterator<Map.Entry<K, ExpireBox<V>>> = table.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next()
            if (entry.value.isExpired) {
                iter.remove()
                continue
            }
            set.add(object : MutableMap.MutableEntry<K, V> {
                @get:Synchronized
                override val key: K get() = entry.key

                @get:Synchronized
                override val value: V get() = entry.value.value

                @Synchronized
                override fun setValue(newValue: V): V {
                    val va = entry.value.value
                    entry.value.value = newValue
                    return va
                }
            })
        }
        return set
    }

    @get:Synchronized
    override val keys: MutableSet<K> get() {
        val set = mutableSetOf<K>()
        val iter: MutableIterator<Map.Entry<K, ExpireBox<V>>> = table.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next()
            if (entry.value.isExpired) {
                iter.remove()
                continue
            }
            set.add(entry.key)
        }
        return set
    }

    @get:Synchronized
    override val values: MutableCollection<V> get() {
        val list = mutableListOf<V>()
        val iter: MutableIterator<Map.Entry<K, ExpireBox<V>>> = table.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next()
            if (entry.value.isExpired) {
                iter.remove()
                continue
            }
            list.add(entry.value.value)
        }
        return list
    }

    class ExpireBox<V>(var value: V, var time: Long, var timeOut: Long) : Cloneable, Serializable {
        constructor(value: V, timeOut: Long) : this(value, System.currentTimeMillis(), timeOut) {}

        val isExpired: Boolean get() = time + timeOut < System.currentTimeMillis()
    }
}
