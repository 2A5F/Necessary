package co.volight.necessary.lang

import com.google.gson.Gson
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.lang.RuntimeException

typealias TextName = String
typealias LangName = String
typealias ModName = String
typealias LangTextMap = Map<TextName, LangStr>
typealias LangMap = Map<LangName, LangTextMap>
typealias ModLangMap = MutableMap<ModName, LangMap>

object Lang {
    private val langs: ModLangMap = mutableMapOf()

    fun getStr(langName: LangName, modName: ModName, textName: TextName): LangStr? {
        return langs[modName]?.get(langName)?.get(textName)
    }

    fun reg(modname: ModName, path: String? = null) {
        val container = FabricLoader.getInstance().getModContainer(modname).orElseThrow { throw RuntimeException("Mod \"${modname}\" not loaded") }
        val file = container.getPath(path ?: "assets/${modname}/lang/").toFile()
        val langMap = loadLangMap(file)
        langs[modname] = langMap
    }

    private fun loadLangMap(folder: File): LangMap {
        val g = sequence {
            for (file in folder.listFiles()!!) {
                if (file.isDirectory) continue
                if (file.extension == "json") {
                    val textMap = loadTextMap(file)
                    yield(Pair(file.nameWithoutExtension, textMap))
                }
            }
        }
        return g.toMap()
    }

    private fun loadTextMap(file: File): LangTextMap {
        val gson = Gson()
        val strMap = gson.fromJson<Map<String, String>>(file.reader(), Map::class.java)
        return strMap.mapValues { LangStr.parse(it.value) }
    }
}