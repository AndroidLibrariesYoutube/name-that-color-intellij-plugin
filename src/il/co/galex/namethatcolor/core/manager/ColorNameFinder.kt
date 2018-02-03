package il.co.galex.namethatcolor.core.manager

import il.co.galex.namethatcolor.core.util.colorsNames
import il.co.galex.namethatcolor.core.exception.ColorNotFoundException
import il.co.galex.namethatcolor.core.model.Color
import il.co.galex.namethatcolor.core.util.hsl
import il.co.galex.namethatcolor.core.util.rgb

typealias HexColor = String

/**
 * Class which loads all the hex codes and names and prepare the RGB and HSL values to be searched for an exact or closest match
 */
object ColorNameFinder {

    private var colors: List<Color> = colorsNames.map { entry -> Color(entry.key, entry.value, "#${entry.key}".rgb(), "#${entry.key}".hsl()) }

    /**
     * look for the name of an hexadecimal color
     */
    fun name(color: HexColor): String {

        var cup = color
        if (!cup.startsWith("#")) cup = "#$cup"

        if (cup.length != 4 && cup.length != 7) {
            throw IllegalArgumentException("Invalid Color: $color")
        } else if (cup.length == 4) {
            cup = "#" + cup[1] + cup[1] + cup[2] + cup[2] + cup[3] + cup[3]
        }

        val (r, g, b) = cup.rgb()
        val (h, s, l) = cup.hsl()

        var cl = -1
        var df = -1

        colors.forEachIndexed { index, col ->

            if(cup == col.hexCode) return col.name
            else {
                val ndf1 = Math.pow((r - col.rgb.r).toDouble(), 2.0).toInt() +  Math.pow((g - col.rgb.g).toDouble(), 2.0).toInt() + Math.pow((b - col.rgb.b).toDouble(), 2.0).toInt()
                val ndf2 = Math.pow((h - col.hsl.h).toDouble(), 2.0).toInt() +  Math.pow((s - col.hsl.s).toDouble(), 2.0).toInt() + Math.pow((l - col.hsl.l).toDouble(), 2.0).toInt()
                val ndf = ndf1 + ndf2 * 2
                if(df < 0 || df > ndf)
                {
                    df = ndf
                    cl = index
                }
            }
        }

        // if not found a close by one, we return an error
        if(cl < 0 ) throw ColorNotFoundException()
        // if found, return the name
        return colors[cl].name
    }
}
