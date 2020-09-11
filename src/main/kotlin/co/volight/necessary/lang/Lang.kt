package co.volight.necessary.lang

import co.volight.necessary.Nec
import com.google.gson.Gson
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.*


typealias TextName = String
typealias LangName = String
typealias ModName = String
typealias LangTextMap = Map<TextName, LangStr>
typealias LangMap = Map<LangName, LangTextMap>
typealias ModLangMap = MutableMap<ModName, LangMap>

object Lang {
    const val logName = "[Nec|Lang]"
    private val langs: ModLangMap = mutableMapOf()

    fun getStr(langName: LangName, modName: ModName, textName: TextName): LangStr? {
        return langs[modName]?.get(langName)?.get(textName)
    }

    fun reg(modname: ModName, path: String? = null) {
        val container = FabricLoader.getInstance().getModContainer(modname).orElseThrow { throw RuntimeException("Mod \"${modname}\" not loaded") }
        val langDir = container.getPath(path ?: "assets/${modname}/lang/")
        try {
            val langMap = loadLangMap(modname, langDir)
            langs[modname] = langMap
            Nec.LOGGER.info("$logName Language files of mod \"${modname}\" loaded")
        } catch (e: Exception) {
            Nec.LOGGER.error("$logName Failed to load the language file of mod \"${modname}\"", e)
        }
    }

    private fun loadLangMap(modname: ModName, langDir: Path): LangMap {
        val jsonFile: PathMatcher = langDir.fileSystem.getPathMatcher("glob:**/*.json")
        return sequence {
            Files.walk(langDir).use { paths ->
                for (path in paths.filter { !Files.isDirectory(it) && jsonFile.matches(it) }) {
                    Nec.LOGGER.debug("$logName loading $path")
                    val textMap = loadTextMap(path)
                    val rawMame = path.fileName.toString()
                    val name = rawMame.substring(0, rawMame.length - 5)
                    Nec.LOGGER.info("$logName mod \"${modname}\" loaded lang \"${name}\"")
                    yield(Pair(name, textMap))
                }
            }
        }.toMap()
    }

    private fun loadTextMap(path: Path): LangTextMap {
        val gson = Gson()
        val strMap = gson.fromJson<Map<String, String>>(Files.newBufferedReader(path), Map::class.java)
        return strMap.mapValues { LangStr.parse(it.value) }
    }
}