package voltaprotocol.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.content.TechTree;
import mindustry.graphics.g3d.*;
import mindustry.type.Planet;
import voltaprotocol.expand.maps.VoltaPlanetGenerator;
import voltaprotocol.expand.maps.VoltaRingMesh;

public class VPPlanets {
    public static Planet Volta;

    public static void load() {
        Volta = new Planet("volta", Planets.sun, 1.2f, 3) {{
            visible        = true;
            accessible     = true;
            alwaysUnlocked = true;

            generator = new VoltaPlanetGenerator();

            meshLoader = () -> new HexMesh(this, 6);

            cloudMeshLoader = () -> new MultiMesh(

                new HexSkyMesh(this, 2, 0.15f, 0.13f, 5,
                    Color.valueOf("1a0510").a(0.55f), 2, 0.50f, 1.00f, 0.30f),

                new HexSkyMesh(this, 5, 0.50f, 0.15f, 5,
                    Color.valueOf("d6147a").a(0.38f), 2, 0.45f, 1.09f, 0.42f),

                new HexSkyMesh(this, 8, 0.30f, 0.13f, 5,
                    Color.valueOf("ff6fb0").a(0.20f), 2, 0.40f, 1.18f, 0.56f),

                new VoltaRingMesh(this, 2.35f, 0.05f, 0.70f, 10, 18f, 0.18f,
                    Color.valueOf("8a2266").a(0.85f), Color.valueOf("4a1042").a(0.80f)),

                new VoltaRingMesh(this, 2.55f, 0.045f, 0.92f, 4, 18f, 0.18f,
                    Color.valueOf("5a1a8a").a(0.40f), Color.valueOf("7a2ba0").a(0.40f))
            );

            atmosphereColor   = Color.valueOf("d6147a").a(0.62f);
            atmosphereRadIn   = 1.02f;
            atmosphereRadOut  = 1.30f;

            bloom = false;

            allowLaunchToNumbered = true;
            defaultCore           = Blocks.coreNucleus;
            startSector           = 0;

            techTree = TechTree.all.find(t -> t.content == Blocks.coreShard);

            ruleSetter = r -> {
                r.waveSpacing = 120 * 60f;
                r.waves       = true;
            };
        }};
    }
}
