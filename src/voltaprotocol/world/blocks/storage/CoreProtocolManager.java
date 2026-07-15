package voltaprotocol.world.blocks.storage;

import java.util.Arrays;

public class CoreProtocolManager {

    private final int[]           counts    = new int[ModuleProtocol.values().length];
    private final ModuleProtocol[] protocols = ModuleProtocol.values();

    public void reset() {
        Arrays.fill(counts, 0);
    }

    public void register(ModuleProtocol protocol) {
        counts[protocol.ordinal()]++;
    }

    public int count(ModuleProtocol protocol) {
        return counts[protocol.ordinal()];
    }

    public boolean isActive(ModuleProtocol protocol) {
        return counts[protocol.ordinal()] > 0;
    }

    public boolean isHealingBlocked() {
        return isActive(ModuleProtocol.DEFENSE) && isActive(ModuleProtocol.HEALING);
    }

    public int totalActive() {
        int sum = 0;
        for (int c : counts) sum += c;
        return sum;
    }

    public ModuleProtocol dominantProtocol() {
        int maxCount = 0;
        ModuleProtocol dominant = null;
        for (ModuleProtocol p : protocols) {
            int c = counts[p.ordinal()];
            if (c > maxCount) { maxCount = c; dominant = p; }
        }
        return dominant;
    }
}