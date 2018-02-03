package il.co.galex.namethatcolor.core.util

import il.co.galex.namethatcolor.core.model.Hsl
import il.co.galex.namethatcolor.core.model.Rgb

/**
 * Transforms a hexadecimal color like "#8D90A1" to an Rgb(141, 144, 161)
 */
fun String.rgb() = Rgb(
        r = this.substring(1, 3).toInt(16),
        g = this.substring(3, 5).toInt(16),
        b = this.substring(5, 7).toInt(16)
)

/**
 * Transforms an hexadecimal color like "#8D90A1" to an Hsl(231, 10, 59)
 */
fun String.hsl(): Hsl {

    val (r, g, b) = this.rgb().percent()

    val min = minOf(r, b, g)
    val max = maxOf(r, b, g)

    val l = (min + max) / 2

    if (min == max) {
        return Hsl(0, 0, l.roundTo2Decimal())
    } else {

        //If Luminance is smaller then 0.5, then Saturation = (max-min)/(max+min)
        //If Luminance is bigger then 0.5. then Saturation = ( max-min)/(2.0-max-min)
        val s = if (l < 0.5) (max - min) / (max + min) else (max - min) / (2.0 - max - min)

        //If Red is max, then Hue = (G-B)/(max-min)
        //If Green is max, then Hue = 2.0 + (B-R)/(max-min)
        //If Blue is max, then Hue = 4.0 + (R-G)/(max-min)
        var h = when (max) {
            r -> (g - b) / (max - min)
            g -> 2.0 + (b - r) / (max - min)
            b -> 4.0 + (r - g) / (max - min)
            else -> throw IllegalStateException("no way")
        }

        h *= 60
        if (h < 0) h += 360

        return Hsl(h.round(), s.roundTo2Decimal(), l.roundTo2Decimal())
    }
}

private fun Double.round() = Math.round(this).toInt()
private fun Double.roundTo2Decimal() = Math.round(this * 100.0).toInt()