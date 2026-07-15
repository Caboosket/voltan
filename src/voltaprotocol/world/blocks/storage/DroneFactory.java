package voltaprotocol.world.blocks.storage;

import mindustry.type.UnitType;
import voltaprotocol.entities.type.KernelUnitType;

public class DroneFactory {

    private DroneFactory() {}

    public static void registerKernel(KernelUnitType kernel) {
        DroneManager.kernelType = kernel;
    }
    public static void registerAuxiliary(UnitType auxiliary) {
        DroneManager.auxiliaryType = auxiliary;
    }

    public static void setSpawnTimes(float auxiliarySeconds, float kernelMultiplier) {
        DroneManager.spawnCooldown      = auxiliarySeconds * 60f;
        DroneManager.kernelCooldownMult = kernelMultiplier;
    }
}