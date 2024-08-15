package fr.kenda.tikcontrols.utils;


import fr.kenda.tikcontrols.TikControls;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PluginVersionning {

    private static final String URL = "https://github.com/KendaFR/TikControls/releases";
    private static final String API_URL = URL + "/latest";
    private final String lastestName;

    public PluginVersionning() throws IOException {
        String jsonString = getJsonString();

        // Créez un JSONObject à partir de la chaîne JSON
        JSONObject obj = new JSONObject(jsonString);

        // Créez un JSONArray à partir de la chaîne JSON
        JSONArray assets = obj.getJSONArray("assets");

        // Obtenez le premier objet du tableau
        JSONObject firstAsset = assets.getJSONObject(0);

        // Maintenant, vous pouvez obtenir le nom
        lastestName = firstAsset.getString("name");

    }

    private static String getJsonString() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        // Lisez une seule ligne de la réponse
        return br.readLine();
    }

    public boolean isLatestVersion() {
        String currentName = TikControls.getInstance().getDescription().getName() + "-" + TikControls.getInstance().getDescription().getVersion() + ".jar";
        return lastestName.equalsIgnoreCase(currentName);
    }

    public void sendNewVersionAlert() {
        String prefix = TikControls.PREFIX;
        String newVersionMessage = prefix + Messages.transformColor("&aNew version available:");
        String clickableText = Messages.transformColor("&7&o" + URL);

        Bukkit.getConsoleSender().sendMessage(newVersionMessage);
        Bukkit.getConsoleSender().sendMessage(prefix + "&a" + URL);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.isOp()) {
                player.sendMessage(newVersionMessage);
                ClickableText.makeTextOpenLink(clickableText, "Open link", URL, player);
            }
        }
    }
}
