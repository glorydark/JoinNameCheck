package glorydark.nukkit.joinnamecheck;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author glorydark
 */
public class CheckMain extends PluginBase implements Listener {

    public boolean noSpace;

    public List<String> bannedChars = new ArrayList<>();

    public Language language = new Language("JoinNameCheck");

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.saveResource("languages/en_US.properties");
        this.saveResource("languages/zh_CN.properties");
        String path = this.getDataFolder().getPath();
        new File(path + "/languages/").mkdirs();
        // read default config
        Config config = new Config(path + "/config.yml", Config.YAML);
        noSpace = config.getBoolean("no_space", false);
        bannedChars = new ArrayList<>(config.getStringList("banned_chars"));
        // multi-language part
        language.addLanguage(new File(path + "/languages/en_US.properties"));
        language.addLanguage(new File(path + "/languages/zh_CN.properties"));
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            String name = player.getName();
            if (!name.trim().equals(name)) {
                player.kick(language.getTranslation("kick_no_space"));
            }
            for (String bannedChar : bannedChars) {
                if (name.contains(bannedChar)) {
                    player.kick(language.getTranslation("kick_banned_chars", bannedChar), true);
                }
            }
        }
    }
}