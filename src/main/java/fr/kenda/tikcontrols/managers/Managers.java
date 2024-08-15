package fr.kenda.tikcontrols.managers;

import fr.kenda.tikcontrols.TikControls;
import fr.kenda.tikcontrols.commands.TikControlsCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


interface IManager {
    void register();
}

public class Managers {
    private final Map<Class<? extends IManager>, IManager> managers = new HashMap<>();

    public Managers() {
        registerManager(CommandManager.class, new CommandManager());
        registerManager(TikTokEventManager.class, new TikTokEventManager());

        managers.forEach((aClass, iManager) -> iManager.register());
    }

    <T extends IManager> void registerManager(Class<T> managerClass, T managerInstance) {
        managers.putIfAbsent(managerClass, managerInstance);
    }
}

class CommandManager implements IManager {
    @Override
    public void register() {
        final TikControls instance = TikControls.getInstance();
        Objects.requireNonNull(instance.getCommand("tikcontrols")).setExecutor(new TikControlsCommand());
    }
}
