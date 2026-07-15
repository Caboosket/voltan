package voltaprotocol.world.blocks.storage;

import arc.struct.Seq;
import arc.util.Time;
import mindustry.ai.types.BoostAI;
import mindustry.ai.types.BuilderAI;
import mindustry.ai.types.DefenderAI;
import mindustry.ai.types.FlyingFollowAI;
import mindustry.ai.types.MinerAI;
import mindustry.ai.types.RepairAI;
import mindustry.entities.units.AIController;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import voltaprotocol.entities.type.KernelUnitType;
import voltaprotocol.world.blocks.storage.ModularCoreV2.ModularCoreBuildV2;

import static mindustry.Vars.*;

public class DroneManager {

    public static KernelUnitType kernelType = null;
    public static UnitType auxiliaryType = null;
    public static float spawnCooldown       = 60f * 20f;
    public static float kernelCooldownMult  = 2f;

    private final ModularCoreBuildV2 core;
    private Unit kernel = null;

    private final Seq<Unit> auxiliaries = new Seq<>(false, 6);
    private float kernelRespawnTimer = 0f;
    private float auxRespawnTimer = 0f;
    private DronePersonality currentPersonality = DronePersonality.STORAGE;
    private final int[] targetAuxCount = new int[DronePersonality.values().length];
    public DroneManager(ModularCoreBuildV2 core) {
        this.core = core;
    }

    public void update() {
        if (kernelType == null || auxiliaryType == null) return;
        if (!state.isGame()) return;

        auxiliaries.removeAll(u -> !u.isAdded() || u.dead());
        if (kernel != null && (!kernel.isAdded() || kernel.dead())) {
            kernel = null;
        }

        if (kernel == null) {
            kernelRespawnTimer += Time.delta;
            if (kernelRespawnTimer >= spawnCooldown * kernelCooldownMult) {
                spawnKernel();
                kernelRespawnTimer = 0f;
            }
        }

        auxRespawnTimer += Time.delta;
        if (auxRespawnTimer >= spawnCooldown) {
            auxRespawnTimer = 0f;
            trySpawnAuxiliary();
        }
    }

    public void onModulesChanged(CoreProtocolManager protocols) {
        ModuleProtocol dominant = protocols.dominantProtocol();
        DronePersonality newPersonality = dominant != null
            ? DronePersonality.fromProtocol(dominant)
            : DronePersonality.STORAGE;

        if (newPersonality != currentPersonality) {
            currentPersonality = newPersonality;
            applyPersonalityToKernel();
        }

        java.util.Arrays.fill(targetAuxCount, 0);
        for (ModuleProtocol p : ModuleProtocol.values()) {
            if (!p.droneOnly && protocols.isActive(p)) {
                int idx = DronePersonality.fromProtocol(p).ordinal();
                targetAuxCount[idx] += protocols.count(p);
            }
        }
    }

    public void onCoreRemoved() {
        if (kernel != null && kernel.isAdded()) kernel.kill();
        for (Unit u : auxiliaries) {
            if (u.isAdded()) u.kill();
        }
        kernel = null;
        auxiliaries.clear();
    }

    public Unit getKernel() { return kernel; }
    public Seq<Unit> getAuxiliaries() { return auxiliaries; }
    public int totalAlive() {
        return (kernel != null && kernel.isAdded() ? 1 : 0) + auxiliaries.size;
    }

    private void spawnKernel() {
        Unit k = kernelType.create(core.team);
        k.add();
        kernel = k;
    }

    private void trySpawnAuxiliary() {
        if (auxiliaryType == null) return;

        int maxAux = core.protocolManager.totalActive();
        if (auxiliaries.size >= maxAux) return;

        DronePersonality needed = findNeededPersonality();
        if (needed == null) return;

        Unit aux = auxiliaryType.create(core.team);
        aux.set(core.x, core.y);

        AIController controller = needed.createAuxiliaryController();
        aux.controller(controller);

        if (needed == DronePersonality.ASSAULT
                && kernel != null && kernel.isAdded()
                && controller instanceof FlyingFollowAI followAI) {
        }

        aux.add();
        auxiliaries.add(aux);
    }

    private DronePersonality findNeededPersonality() {
        int[] aliveCount = new int[DronePersonality.values().length];
        for (Unit u : auxiliaries) {
            if (!u.isAdded() || u.dead()) continue;
            DronePersonality p = personalityFromController(u.controller());
            if (p != null) aliveCount[p.ordinal()]++;
        }

        DronePersonality best = null;
        int bestDeficit = 0;
        for (DronePersonality p : DronePersonality.values()) {
            int deficit = targetAuxCount[p.ordinal()] - aliveCount[p.ordinal()];
            if (deficit > bestDeficit) {
                bestDeficit = deficit;
                best = p;
            }
        }
        return best;
    }

    private void applyPersonalityToKernel() {
        if (kernel == null || !kernel.isAdded()) return;
        if (kernel.isPlayer()) return;
        kernel.controller(currentPersonality.createKernelController());
    }

    private static DronePersonality personalityFromController(
            mindustry.entities.units.UnitController ctrl) {
        if (ctrl instanceof RepairAI)       return DronePersonality.HEALING;
        if (ctrl instanceof BoostAI)        return DronePersonality.ENERGY;
        if (ctrl instanceof DefenderAI)     return DronePersonality.ASSAULT;
        if (ctrl instanceof FlyingFollowAI) return DronePersonality.ASSAULT;
        if (ctrl instanceof MinerAI)        return DronePersonality.STORAGE;
        if (ctrl instanceof BuilderAI)      return DronePersonality.STORAGE;
        return null;
    }
}