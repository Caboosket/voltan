package voltaprotocol.world.blocks.storage;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.core.UI;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.blocks.storage.CoreBlock;
import voltaprotocol.world.blocks.storage.ModularModuleV2.ModularModuleBuildV2;

public class ModularCoreV2 extends CoreBlock {

    public int   maxActiveModules = 6;
    public float baseArmor        = 0f;

    public ModularCoreV2(String name) {
        super(name);
        unitCapModifier = 16;
    }

    @Override
    public void load() {
        super.load();
        teamRegion = Core.atlas.find(name + "-team");
    }

    @Override
    public TextureRegion[] icons() {
        if (teamRegion != null && teamRegion.found())
            return new TextureRegion[]{region, teamRegion};
        return new TextureRegion[]{region};
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("health");
        removeBar("capacity");

        addBar("vp-stats", (ModularCoreBuildV2 b) -> new Bar(
            () -> {
                String armor = "[accent]" + (int)b.bonusArmor + "[] armor";
                String heal  = b.protocolManager.isActive(ModuleProtocol.HEALING)
                               && !b.protocolManager.isHealingBlocked()
                               ? "   [green]+" + (int)b.activeHealRate + "[]/s"
                               : "";
                return armor + heal;
            },
            () -> Pal.accent,
            () -> 1f
        ));

        addBar("vp-slots", (ModularCoreBuildV2 b) -> new Bar(
            () -> "Módulos: " + b.protocolManager.totalActive()
                  + "/" + ((ModularCoreV2)b.block).maxActiveModules,
            () -> Pal.items,
            () -> ((ModularCoreV2)b.block).maxActiveModules == 0 ? 0f
                  : (float)b.protocolManager.totalActive()
                    / ((ModularCoreV2)b.block).maxActiveModules
        ));

        //addBar("vp-protocol", (ModularCoreBuildV2 b) -> new Bar(
            //() -> {
             //   ModuleProtocol dom = b.protocolManager.dominantProtocol();
             //   return dom != null ? "Proto: " + dom.bundleKey : "Sin protocolo";
          //  },
          //  () -> Pal.accent,
         //   () -> 0f
       // ));

        addBar("health", (ModularCoreBuildV2 b) -> new Bar(
            () -> (int)b.health + " / " + (int)b.maxHealth,
            () -> Pal.health,
            () -> b.healthf()
        ));

        addBar("capacity", (ModularCoreBuildV2 b) -> new Bar(
            () -> "Cap: " + UI.formatAmount(b.storageManager.effectiveCapacity),
            () -> Pal.items,
            () -> b.storageManager.usagePercent(b.items)
        ));
    }

    public class ModularCoreBuildV2 extends CoreBuild {

        public final CoreStorageManager  storageManager  = new CoreStorageManager();
        public final CoreProtocolManager protocolManager = new CoreProtocolManager();
        public final DroneManager droneManager = new DroneManager(this);

        public int     bonusCapacity  = 0;
        public int     overflowCapacity = 0;
        public float   bonusArmor     = 0f;
        public float   bonusHealth    = 0f;
        public float   activeHealRate = 0f;
        public int     activeModCount = 0;
        public int     bonusUnitCap   = 0;

        private boolean recalcPending  = false;
        private boolean recalcRunning  = false;
        private int vanillaCapacitySnapshot = 0;

        @Override
        public void updateTile() {
            super.updateTile();
            droneManager.update();

            if (activeHealRate > 0f
                    && !protocolManager.isHealingBlocked()
                    && health < maxHealth()) {
                heal(activeHealRate / 60f * edelta());
            }

            if (activeHealRate > 0f && !protocolManager.isHealingBlocked()) {
                float modHeal = activeHealRate * 0.5f / 60f * edelta();
                for (Building b : proximity) {
                    if (b instanceof ModularModuleBuildV2 m && m.health < m.maxHealth())
                        m.heal(modHeal);
                }
            }
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            vanillaCapacitySnapshot = storageCapacity;
            scheduleRecalculate();
        }

        public void scheduleRecalculate() {
            if (recalcPending) return;
            recalcPending = true;
            Time.run(1f, () -> {
                recalcPending = false;
                recalculateModules();
            });
        }

        public void recalculateModules() {
            if (recalcRunning) return;
            recalcRunning = true;

            try {
                int oldUnitCap = bonusUnitCap;

                bonusCapacity   = 0;
                overflowCapacity = 0;
                bonusArmor      = 0f;
                bonusHealth     = 0f;
                activeHealRate  = 0f;
                activeModCount  = 0;
                bonusUnitCap    = 0;
                protocolManager.reset();

                Seq<ModularModuleBuildV2> specialty = new Seq<>();
                Seq<ModularModuleBuildV2> overflow  = new Seq<>();

                for (Building b : proximity) {
                    if (!(b instanceof ModularModuleBuildV2 m) || m.team != team) continue;
                    m.activeSlot = false;
                    if (m.mb().canOverflow) overflow.add(m);
                    else                    specialty.add(m);
                }

                int[] protocolCount = new int[ModuleProtocol.values().length];
                for (ModularModuleBuildV2 m : specialty) tryActivate(m, protocolCount);
                for (ModularModuleBuildV2 m : overflow) {
                    if (!tryActivate(m, protocolCount)) {
                        overflowCapacity += m.mb().capacityBonus;
                    }
                }

                float newMax = Math.max(1f, block.health + bonusHealth);
                if (Math.abs(newMax - maxHealth) > 0.5f) {
                    float ratio = Mathf.clamp(health / maxHealth, 0f, 1f);
                    maxHealth = newMax;
                    health    = Math.max(maxHealth * ratio, 1f);
                }

                armor = ((ModularCoreV2) block).baseArmor + bonusArmor;

                if (team != null && team.data() != null && oldUnitCap != bonusUnitCap) {
                    team.data().unitCap += (bonusUnitCap - oldUnitCap);
                }

                storageCapacity = storageManager.compute(
                    vanillaCapacitySnapshot,
                    bonusCapacity,
                    overflowCapacity
                );

                droneManager.onModulesChanged(protocolManager);

            } finally {
                recalcRunning = false;
            }
        }

        private boolean tryActivate(ModularModuleBuildV2 m, int[] protocolCount) {
            ModularCoreV2 core = (ModularCoreV2) block;

            if (activeModCount >= core.maxActiveModules) return false;

            int pOrd  = m.mb().protocol.ordinal();
            int limit = m.mb().effectiveMaxActive();
            if (protocolCount[pOrd] >= limit) return false;

            m.activeSlot = true;
            protocolCount[pOrd]++;
            activeModCount++;
            protocolManager.register(m.mb().protocol);

            bonusCapacity  += m.mb().capacityBonus;
            bonusArmor     += m.mb().armorBonus;
            bonusHealth    += m.mb().healthBonus;
            activeHealRate += m.mb().healRate;

            return true;
        }

        @Override
        public int getMaximumAccepted(Item item) {
            return storageManager.effectiveCapacity;
        }

        @Override
        public void onRemoved() {
            super.onRemoved();
            droneManager.onCoreRemoved();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(bonusArmor);
            write.f(bonusHealth);
            write.f(activeHealRate);
            write.i(activeModCount);
            write.i(bonusCapacity);
            write.i(overflowCapacity);
            write.i(vanillaCapacitySnapshot);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            bonusArmor              = read.f();
            bonusHealth             = read.f();
            activeHealRate          = read.f();
            activeModCount          = read.i();
            bonusCapacity           = read.i();
            overflowCapacity        = read.i();
            vanillaCapacitySnapshot = read.i();

            storageManager.compute(vanillaCapacitySnapshot, bonusCapacity, overflowCapacity);
            storageCapacity = storageManager.effectiveCapacity;
        }
    }
}