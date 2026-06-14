package voltaprotocol.content;

import mindustry.content.Items;
import mindustry.type.ItemStack;
import mindustry.type.SectorPreset;
import arc.struct.Seq;

public class VPSectors {
    public static SectorPreset laUltimaSenal;

    public static void load() {
        laUltimaSenal = new SectorPreset("la-ultima-senal", VPPlanets.Volta, 0) {{
            difficulty = 3;
            alwaysUnlocked = true;
            captureWave = 40;                
        }}; 

        laUltimaSenal.rules = r -> {
            r.bannedBlocks.clear();
            r.damageExplosions = true;
        r.loadout = Seq.with(ItemStack.with(
            Items.copper, 2000,
            Items.lead, 2000,
            Items.silicon, 1000,
            Items.graphite, 800,
            VPItems.silver, 500
        ));
        };
    }
}
