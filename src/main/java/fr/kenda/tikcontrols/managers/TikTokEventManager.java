package fr.kenda.tikcontrols.managers;

import fr.kenda.tikcontrols.TikControls;
import fr.kenda.tikcontrols.utils.Config;
import fr.kenda.tikcontrols.utils.Messages;
import io.github.jwdeveloper.tiktok.TikTokLive;
import io.github.jwdeveloper.tiktok.live.builder.LiveClientBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TikTokEventManager implements IManager {

    private final List<String> followActions = new ArrayList<>();
    private final List<String> likeActions = new ArrayList<>();
    private final Map<Integer, List<String>> actionsFromGiftId = new HashMap<>();

    @Override
    public void register() {
        loadEventFromConfig("events.like.commands", likeActions);
        loadEventFromConfig("events.follow.commands", followActions);
        loadGiftsEventFromConfig();

        final String username = Config.getString("username");
        LiveClientBuilder api = TikTokLive.newClient(username);

        if (!likeActions.isEmpty()) {
            api.onLike((liveClient, tikTokLikeEvent) ->
                    likeActions.forEach(cmd ->
                            Bukkit.getScheduler().runTask(TikControls.getInstance(), () ->
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%donator%", tikTokLikeEvent.getUser().getName()))
                            )
                    )
            );
        }

        if (!followActions.isEmpty()) {
            api.onFollow((liveClient, tikTokFollowEvent) ->
                    followActions.forEach(cmd ->
                            Bukkit.getScheduler().runTask(TikControls.getInstance(), () ->
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%donator%", tikTokFollowEvent.getUser().getName()))
                            )
                    )
            );
        }

        if (!actionsFromGiftId.isEmpty()) {
            api.onGift((liveClient, tikTokGiftEvent) -> actionsFromGiftId.get(tikTokGiftEvent.getGift().getId())
                    .forEach(cmd ->
                            Bukkit.getScheduler().runTask(TikControls.getInstance(), () ->
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%donator%", tikTokGiftEvent.getUser().getName()))
                            )
                    ));
        }

        api.onConnected((liveClient, tikTokConnectedEvent) -> Bukkit.getConsoleSender().sendMessage(Messages.transformColor(TikControls.PREFIX + "&aConnected to " + username)));
        Bukkit.getConsoleSender().sendMessage(Messages.transformColor(TikControls.PREFIX + "&aConnection to " + username + "..."));
        api.buildAndConnectAsync();
    }

    private void loadGiftsEventFromConfig() {
        ConfigurationSection section = TikControls.getInstance().getConfig().getConfigurationSection("events.gifts");
        if (section != null) {
            for (String id : section.getKeys(false)) {
                List<String> cmd = Config.getList("events.gifts." + id + ".commands");
                actionsFromGiftId.put(Integer.valueOf(id), cmd);
            }
        }
    }

    private void loadEventFromConfig(String path, List<String> actions) {
        if (!Config.isConfigSectionExist(path)) return;
        actions.addAll(Config.getList(path));
    }
}
