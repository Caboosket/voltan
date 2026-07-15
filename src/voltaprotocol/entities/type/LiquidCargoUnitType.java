package voltaprotocol.entities.type;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.Table;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.Bar;
import voltaprotocol.ai.types.LiquidCargoAI;

public class LiquidCargoUnitType extends UnitType {

    public TextureRegion liquidRegion;

    public LiquidCargoUnitType(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        liquidRegion = Core.atlas.find(name + "-liquid");
    }

    @Override
    public void draw(Unit unit) {
        super.draw(unit);

        if (unit.controller() instanceof LiquidCargoAI ai && ai.currentLiquid != null) {
            Draw.color(ai.currentLiquid.color);
            Draw.rect(liquidRegion, unit.x, unit.y, unit.rotation - 90f);
            Draw.reset();
        }
    }

    @Override
    public void display(Unit unit, Table table) {
        super.display(unit, table);

        table.row();
        table.add(new Bar(
            () -> {
                if (unit.controller() instanceof LiquidCargoAI ai && ai.currentLiquid != null) {
                    String name = arc.Core.bundle.get(
                        "liquid." + ai.currentLiquid.name,
                        ai.currentLiquid.localizedName
                    );
                    return name + "  " + (int)ai.liquidAmount + "/" + (int)ai.droneLiquidCapacity;
                }
                return arc.Core.bundle.get("bar.liquid", "Vacío");
            },
            () -> {
                if (unit.controller() instanceof LiquidCargoAI ai && ai.currentLiquid != null)
                    return ai.currentLiquid.color;
                return Color.gray;
            },
            () -> {
                if (unit.controller() instanceof LiquidCargoAI ai && ai.droneLiquidCapacity > 0)
                    return ai.liquidAmount / ai.droneLiquidCapacity;
                return 0f;
            }
        )).growX().height(18f);
    }
}
