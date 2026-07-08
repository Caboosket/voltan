package voltaprotocol.content;

import mindustry.type.UnitType;
import mindustry.type.UnitType.UnitEngine;
import voltaprotocol.ai.types.LiquidCargoAI;
import voltaprotocol.entities.type.LiquidCargoUnitType;

import static mindustry.Vars.content;

public class VPUnits{
//tanks
    public static UnitType amper;
    public static UnitType ion;
    public static UnitType valance;
    public static UnitType isotope;
    public static UnitType radiolysis;
//aerodeslizadores
    public static UnitType syrinxI;
    public static UnitType syrinxII;
    public static UnitType syrinxIII;
    public static UnitType syrinxIV;
//bosses
    public static UnitType boss1;
//cargo-drone
    public static LiquidCargoUnitType sifon;

    public static void load(){

        amper = content.unit("w1-1a-amper");
        ion = content.unit("w1-a-ion");
        valance = content.unit("w1-b-valance");
        isotope = content.unit("w1-c-isotope");
        radiolysis = content.unit("w1-d-radiolysis");

        syrinxI = content.unit("w2-a-syrinx-i");
        syrinxII = content.unit("w2-b-syrinx-ii");
        syrinxIII = content.unit("w2-c-syrinx-iii");
        syrinxIV = content.unit("w2-d-syrinx-iv");

        boss1 = content.unit("xa-1a-boss-1");

        sifon = new LiquidCargoUnitType("w3-a-sifon"){{
            
            flying = true;
            drag = 0.05f;
            speed = 3.75f;
            accel = 0.05f;
            health = 250f;
            armor = 0f;
            hitSize = 7f;
            itemCapacity = 0;
            outlineRadius = 0;

            engines.clear();
            engines.add(new UnitEngine(5f, -5f, 2.0f, 45));
            engines.add(new UnitEngine( -5f,  -5f, 2.0f, -45f));
            
            isEnemy = false;
            logicControllable = false;
            playerControllable = false;
            allowedInPayloads = false;

            constructor = mindustry.gen.BuildingTetherPayloadUnit::create;
            aiController = LiquidCargoAI::new;
        }};
    }
}
