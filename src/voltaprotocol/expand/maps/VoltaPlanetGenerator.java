package voltaprotocol.expand.maps;

import arc.graphics.Color;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.Tmp;
import arc.util.noise.Simplex;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Tiles;
import mindustry.world.WorldParams;
import mindustry.world.TileGen;
import voltaprotocol.content.VPBlocks;

public class VoltaPlanetGenerator extends PlanetGenerator {
    
    public static final Color andesitaOscura = Color.valueOf("2d2d31");
    public static final Color plataConductiva = Color.valueOf("9aa4b2");
    public static final Color miasmaConcentrado = Color.valueOf("3a0d25");
    public static Interp interp = new Interp.Exp(2, 3);

    @Override
    public float getHeight(Vec3 position) {
        float raw = Simplex.noise3d(seed, 3, 0.5f, 0.3f, position.x, position.y, position.z);
        float height = interp.apply(Mathf.clamp((raw - 0.1f) * 0.8f, 0f, 1f));
        return height;
    }

    public Color getColor(Vec3 position) {
        float depth = getHeight(position);
        
        float patchNoise = Simplex.noise3d(seed + 1, 5, 0.5f, 0.6f, position.x * 3f, position.y * 3f, position.z * 3f);
        
        Color colorBase;

        if (depth > 0.45f) {
            colorBase = plataConductiva; 
        } else if (patchNoise > 0.15f) {
            colorBase = miasmaConcentrado; 
        } else {
            colorBase = andesitaOscura; 
        }

        float factorSombra = Mathf.clamp(0.4f + depth * 0.6f, 0.3f, 1.0f);
        
        return Tmp.c1.set(colorBase).mul(factorSombra);
    }

    @Override
    public void genTile(Vec3 position, TileGen tile) {
        float localNoise = Simplex.noise3d(seed, 4, 0.5f, 0.5f, position.x, position.y, position.z);

        if (localNoise > 0.2f) {
            tile.floor = VPBlocks.argentAndesite;
            tile.block = VPBlocks.argentAndesiteWall;
        } else {
            tile.floor = VPBlocks.darkAndesite;
            tile.block = VPBlocks.darkAndesiteWall;
        }
    }

    @Override
    public void generate(Tiles tiles, Sector sector, WorldParams params) {
        if (sector.id == 0) {
            return;
        }

        for (int x = 0; x < tiles.width; x++) {
            for (int y = 0; y < tiles.height; y++) {

                tiles.get(x, y).setFloor(VPBlocks.darkAndesite.asFloor());

                float oreNoise = noise(x, y, 3.0, 0.5, 22.0, 1.0);
               
                if (oreNoise > 0.78f) {
                    tiles.get(x, y).setOverlay(VPBlocks.silverOre);
                } else if (oreNoise > 0.84f) {
                    tiles.get(x, y).setOverlay(VPBlocks.palladiumOre);
                }
            }
        }
    }
}
