package voltaprotocol.world.blocks.storage;

import mindustry.ai.types.*;
import mindustry.entities.units.AIController;

public enum DronePersonality {

    STORAGE(
        "personality.storage",
        () -> new BuilderAI(),
        () -> new MinerAI()
    ),

    HEALING(
        "personality.healing",
        () -> new RepairAI(),
        () -> new RepairAI()
    ),

    ENERGY(
        "personality.energy",
        () -> new BoostAI(),
        () -> new BoostAI()
    ),

    ASSAULT(
        "personality.assault",
        () -> new DefenderAI(),
        () -> new FlyingFollowAI()
    ),

    DEFENSE(
        "personality.defense",
        () -> new DefenderAI(),
        () -> new DefenderAI()
    );

    public final String bundleKey;
    public final java.util.function.Supplier<AIController> kernelController;
    public final java.util.function.Supplier<AIController> auxiliaryController;

    DronePersonality(
            String bundleKey,
            java.util.function.Supplier<AIController> kernel,
            java.util.function.Supplier<AIController> auxiliary) {
        this.bundleKey           = bundleKey;
        this.kernelController    = kernel;
        this.auxiliaryController = auxiliary;
    }

    public static DronePersonality fromProtocol(ModuleProtocol protocol) {
        return switch (protocol) {
            case HEALING -> HEALING;
            case ENERGY  -> ENERGY;
            case ASSAULT -> ASSAULT;
            case DEFENSE -> DEFENSE;
            default      -> STORAGE;
        };
    }

    public AIController createKernelController()    { return kernelController.get(); }
    public AIController createAuxiliaryController() { return auxiliaryController.get(); }
}