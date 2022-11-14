package org.tudalgo.algoutils.tutor.general.stringify;

import static java.lang.String.format;

public class HTML {

    private HTML() {

    }

    public static String tt(String string) {
        return "\\<samp\\>" + string + "\\</samp\\>";
    }

    public static String it(String string) {
        return "\\<i\\>" + string + "\\</i\\>";
    }

    public static String small(String string) {
        return "\\<small\\>" + string + "\\</small\\>";
    }

    public static String nobr(String string) {
        return "\\<nobr\\>" + string + "\\</nobr\\>";
    }

    public static String wbr() {
        return "\\<wbr\\>";
    }

    public static String pl4(String string) {
        return format("\\<div class=\"pl-4\"\\>%s\\</div\\>", string);
    }
}
