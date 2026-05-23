package voltaprotocol.content;

import arc.graphics.Color;
import arc.util.Log;
import mindustry.type.Item;

public class VPItems {

    public static Item silver;
    public static Item palladium;
    public static Item aegesium;
    public static Item bioComposite;
    public static Item voltium;

    public static void load() {
        Log.info("[Volta Protocol] Registrando items desde Java de forma nativa...");

        silver = new Item("vw-1-silver", Color.valueOf("CFD6E6")){{
            hardness = 2;
            cost = 1.1f;
            radioactivity = 0f;
            flammability = 0f;
            explosiveness = 0f;
            charge = 0.3f;
        }};

        palladium = new Item("vw-2-palladium", Color.valueOf("e1897a")){{
            hardness = 4;
            cost = 2.5f;
            radioactivity = 0.1f;
            flammability = 0f;
            explosiveness = 0f;
            buildable = true;
        }};

        aegesium = new Item("vw-3-aegesium", Color.valueOf("1e4a5e")){{
            hardness = 3;
            cost = 1.5f;
            radioactivity = 0f;
            flammability = 0f;
            explosiveness = 0f;
        }};

        bioComposite = new Item("vw-4-bio-composite", Color.valueOf("6effc2")){{
            flammability = 0.2f;
            explosiveness = 0f;
            hardness = 0;
            cost = 1.6f;
        }};

        voltium = new Item("vw-5-voltium", Color.valueOf("ff9a3c")){{
            radioactivity = 0.6f;
            explosiveness = 0.4f;
            flammability = 0.1f;
            hardness = 5;
            cost = 3.5f;
            
            frames = 24;
            frameTime = 6f;
        }};

        Log.info("[Volta Protocol] Silver enlazado: " + silver);
        Log.info("[Volta Protocol] Palladium enlazado: " + palladium);
        Log.info("[Volta Protocol] Aegesium enlazado: " + aegesium);
        Log.info("[Volta Protocol] BioComposite enlazado: " + bioComposite);
        Log.info("[Volta Protocol] Voltium enlazado: " + voltium);
    }
}