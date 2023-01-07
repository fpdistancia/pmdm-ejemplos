package dam.pmdm.bolaslocas.util;

import android.graphics.Color;

public class Colores {

    private static final String [] COLORES = {
            "#c0c0c0", "#dcdcdc", "#2f4f4f", "#556b2f", "#8b4513", "#6b8e23", "#a0522d", "#a52a2a", "#2e8b57", "#228b22",
            "#7f0000", "#191970", "#006400", "#808000", "#483d8b", "#b22222", "#5f9ea0", "#778899", "#3cb371", "#bc8f8f",
            "#663399", "#b8860b", "#bdb76b", "#008b8b", "#cd853f", "#4682b4", "#d2691e", "#9acd32", "#20b2aa", "#cd5c5c",
            "#00008b", "#4b0082", "#32cd32", "#daa520", "#8fbc8f", "#800080", "#b03060", "#d2b48c", "#48d1cc", "#66cdaa",
            "#9932cc", "#ff0000", "#ff4500", "#ff8c00", "#ffa500", "#ffd700", "#6a5acd", "#ffff00", "#c71585", "#0000cd",
            "#7cfc00", "#40e0d0", "#00ff00", "#9400d3", "#ba55d3", "#00fa9a", "#8a2be2", "#00ff7f", "#4169e1", "#e9967a",
            "#dc143c", "#00ffff", "#00bfff", "#f4a460", "#9370db", "#0000ff", "#a020f0", "#f08080", "#adff2f", "#ff6347",
            "#da70d6", "#d8bfd8", "#b0c4de", "#ff7f50", "#ff00ff", "#1e90ff", "#db7093", "#f0e68c", "#fa8072", "#eee8aa",
            "#ffff54", "#6495ed", "#dda0dd", "#add8e6", "#87ceeb", "#ff1493", "#7b68ee", "#ffa07a", "#afeeee", "#ee82ee",
            "#98fb98", "#87cefa", "#7fffd4", "#ffdead", "#ff69b4", "#ffe4c4", "#ffc0cb", "#FFFACD", "#FAEBD7", "#7FFF00"
    };
    
    public static int getPredefinido() {
        return Color.parseColor(COLORES[(int) (Math.random() * COLORES.length - 1)]);
    }

    public static int get() {
        return Color.rgb((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static int get(float alpha) {
        return Color.argb(alpha, (float) Math.random(), (float) Math.random(), (float) Math.random());
    }

}
