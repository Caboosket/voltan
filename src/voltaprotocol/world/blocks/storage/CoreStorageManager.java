package voltaprotocol.world.blocks.storage;

import mindustry.world.modules.ItemModule;

public class CoreStorageManager {

    public int vanillaCapacity = 0;
    public int activeModuleCapacity = 0;
    public int overflowModuleCapacity = 0;
    public int effectiveCapacity = 0;
    private int version = 0;

    public int compute(int vanillaCapacityAfterSuper, int activeBonus, int overflowBonus) {
        this.vanillaCapacity        = vanillaCapacityAfterSuper;
        this.activeModuleCapacity   = activeBonus;
        this.overflowModuleCapacity = overflowBonus;
        this.effectiveCapacity      = vanillaCapacityAfterSuper + activeBonus + overflowBonus;
        this.version++;
        return this.effectiveCapacity;
    }

    public int totalModuleBonus() {
        return activeModuleCapacity + overflowModuleCapacity;
    }

    public int getVersion() { return version; }

    public static int countItems(ItemModule items) {
        return items == null ? 0 : items.total();
    }

    public float usagePercent(ItemModule items) {
        if (effectiveCapacity <= 0) return 0f;
        return (float) countItems(items) / effectiveCapacity;
    }
}