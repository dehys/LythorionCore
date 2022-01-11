import com.dehys.lythorioncore.object.tag.TagManager;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Test {

    enum ColorCodes {
        BLACK("&0"),
        DARK_BLUE("&1"),
        DARK_GREEN("&2"),
        DARK_AQUA("&3"),
        DARK_RED("&4"),
        DARK_PURPLE("&5"),
        GOLD("&6"),
        GRAY("&7"),
        DARK_GRAY("&8"),
        BLUE("&9"),
        GREEN("&a"),
        AQUA("&b"),
        RED("&c"),
        LIGHT_PURPLE("&d"),
        YELLOW("&e"),
        WHITE("&f");

        private String code;

        ColorCodes(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    enum FormatCodes{
        BOLD("&l"),
        ITALIC("&o"),
        STRIKETHROUGH("&m"),
        UNDERLINE("&n");

        private String code;

        FormatCodes(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static void main(String[] args) {
        String hello = "hello:";
        String intro = hello.split(":", 2)[1];

        if (intro.isBlank()) System.out.println("blank");

        System.out.println(intro);
    }

}
