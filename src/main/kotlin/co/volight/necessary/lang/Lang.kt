package co.volight.necessary.lang

import co.volight.necessary.Nec
import com.google.gson.Gson
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PathMatcher


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
        val langDir = container.getPath(path ?: "assets/${modname}/lang/")
        try {
            val langMap = loadLangMap(langDir)
            langs[modname] = langMap
            Nec.LOGGER.info("Language files of mod \"${modname}\" loaded")
        } catch (e: Exception) {
            Nec.LOGGER.error("Failed to load the language file of mod \"${modname}\"", e)
        }
    }

    private val jsonFile: PathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.json")
    private fun loadLangMap(langDir: Path): LangMap {
        val g = sequence {
            Files.walk(langDir).use { paths ->
                for (path in paths.filter(Files::isRegularFile).filter(jsonFile::matches)) {
                    val textMap = loadTextMap(path)
                    val name = path.fileName.toString()
                    yield(Pair(name.substring(0, name.length - 5), textMap))
                }
            }
        }
        return g.toMap()
    }

    private fun loadTextMap(path: Path): LangTextMap {
        val gson = Gson()
        val strMap = gson.fromJson<Map<String, String>>(Files.newBufferedReader(path), Map::class.java)
        return strMap.mapValues { LangStr.parse(it.value) }
    }
}