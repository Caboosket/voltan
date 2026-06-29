package voltaprotocol.content;

import mindustry.type.UnitType;

import static mindustry.Vars.content;

public class VPUnits{

    public static UnitType amper;
    public static UnitType ion;
    public static UnitType valance;
    public static UnitType isotope;
    public static UnitType radiolysis;

    public static UnitType syrinxI;
    public static UnitType syrinxII;
    public static UnitType syrinxIII;
    public static UnitType syrinxIV;

    public static UnitType boss1;

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
    }
}