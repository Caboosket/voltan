package voltaprotocol.world.blocks.storage;

/**
 * Protocolos que un módulo puede aportar al núcleo.
 * Cada ModularModuleV2 declara cuál protocol implementa.
 * CoreProtocolManager lleva la cuenta de cuántos de cada tipo están activos.
 *
 * Los protocolos de drones (MINING, SHIELD, BOOSTER, RECOVERY) se reservan
 * para cuando DroneFactory y DronePersonality estén listos.
 */
public enum ModuleProtocol {

    //Módulos actuales
    STORAGE   ("protocol-storage",  6, false),   // SILVER, BASIC — capacidad
    DEFENSE   ("protocol-defense",  4, false),   // BLUE  — armadura + HP, bloquea HEALING
    HEALING   ("protocol-healing",  6, false),   // GREEN — curación pasiva
    ASSAULT   ("protocol-assault",  6, false),   // RED   — torreta
    ENERGY    ("protocol-energy",   2, false),   // ORANGE — generación de energía

    //Reservados para drones (DronePersonality)
    MINING    ("protocol-mining",   4, true),
    SHIELD    ("protocol-shield",   3, true),
    BOOSTER   ("protocol-booster",  3, true),
    RECOVERY  ("protocol-recovery", 3, true);

    public final String bundleKey;
    public final int defaultMaxActive;
    public final boolean droneOnly;

    ModuleProtocol(String bundleKey, int defaultMaxActive, boolean droneOnly) {
        this.bundleKey      = bundleKey;
        this.defaultMaxActive = defaultMaxActive;
        this.droneOnly      = droneOnly;
    }
}