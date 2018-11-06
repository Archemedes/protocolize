package de.exceptionflug.protocolize;

import com.google.common.io.ByteStreams;
import de.exceptionflug.protocolize.api.protocol.ProtocolAPI;
import de.exceptionflug.protocolize.command.ProtocolizeCommand;
import de.exceptionflug.protocolize.command.ProxyInvCommand;
import de.exceptionflug.protocolize.command.TrafficCommand;
import de.exceptionflug.protocolize.injector.NettyPipelineInjector;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemsModule;
import de.exceptionflug.protocolize.listener.PlayerListener;
import de.exceptionflug.protocolize.world.WorldModule;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class ProtocolizePlugin extends Plugin {

    private final NettyPipelineInjector nettyPipelineInjector = new NettyPipelineInjector();
    private boolean enabled = true;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getLogger().info("======= PROTOCOLIZE =======");
        ProxyServer.getInstance().getLogger().info("Version "+getDescription().getVersion()+" by "+getDescription().getAuthor());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerListener(this));

        try {
            if(!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            final File file = new File(getDataFolder(), "config.yml");
            if(!file.exists()) {
                file.createNewFile();
                try (final InputStream is = getResourceAsStream("config.yml");
                     final OutputStream os = new FileOutputStream(file)) {
                     ByteStreams.copy(is, os);
                }
            }
            final Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            InventoryModule.setSpigotInventoryTracking(configuration.getBoolean("experimental.spigot-gui-inventory-tracking"));
            ItemsModule.setSpigotInventoryTracking(configuration.getBoolean("experimental.spigot-player-inventory-tracking"));
            ProtocolAPI.getEventManager().setFireBungeeEvent(configuration.getBoolean("fireBungeeEvents"));
        } catch (final IOException e) {
            ProxyServer.getInstance().getLogger().log(Level.SEVERE, "[Protocolize] Failed to load config", e);
        }


        // Init system components
        WorldModule.initModule();
        ItemsModule.initModule();
        InventoryModule.initModule();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ProxyInvCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ProtocolizeCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TrafficCommand());
    }

    public NettyPipelineInjector getNettyPipelineInjector() {
        return nettyPipelineInjector;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public static boolean isExceptionCausedByProtocolize(final Throwable e) {
        for(final StackTraceElement element : e.getStackTrace()) {
            if(element.getClassName().toLowerCase().contains("de.exceptionflug"))
                return true;
        }
        return false;
    }

}
