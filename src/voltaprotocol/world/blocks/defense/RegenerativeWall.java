package voltaprotocol.world.blocks.defense;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.world.blocks.defense.Wall;

public class RegenerativeWall extends Wall {

    public float healAmount = 2f;
    public float healInterval = 30f;
    public float regenDelay = 180f;
    public float maxHeal = 10f;

    public Effect successHealEffect = new ParticleEffect() {{
        particles = 3;
        lifetime = 15f;
        length = 8f;
        sizeFrom = 1f;
        sizeTo = 0f;
        colorFrom = Color.valueOf("84f5f5");
        colorTo = Color.valueOf("5eb1b1");
    }};

    public RegenerativeWall(String name, int size, int health, float armor, float healAmount, float maxHeal) {
        super(name);

        this.size = size;
        this.health = health;
        this.armor = armor;
        this.healAmount = healAmount;
        this.maxHeal = maxHeal;

        update = true;
        solid = true;
        buildType = RegenerativeWallBuild::new;
    }

    @Override
    public void setStats() {
        super.setStats();

        float ejecucionesPorSegundo = 60f / healInterval;
        float minRegen = healAmount * ejecucionesPorSegundo;
        float maxRegen = maxHeal * ejecucionesPorSegundo;

        stats.add(
            mindustry.world.meta.Stat.repairTime, 
            mindustry.world.meta.StatValues.string(
                "[green]+" + arc.util.Strings.fixed(minRegen, 1) + "[] a [green]+" + arc.util.Strings.fixed(maxRegen, 1) + "[] HP/s\n[lightgray]" + arc.Core.bundle.get("stat.voltaprotocol-regen-info") + "[]"
            )
        );
    }

    public class RegenerativeWallBuild extends WallBuild {
        private float healTimer = 0f;
        private float damageTimer = 0f;
        private boolean regenerationActive = false;

        @Override
        public void updateTile() {
            super.updateTile();

            if(damageTimer > 0f){
                damageTimer -= edelta();

                regenerationActive = false;
                return;
            }

            if(health < maxHealth){

                if(!regenerationActive){
                    regenerationActive = true;

                    successHealEffect.at(x, y);
                    Fx.healBlock.at(x, y, block.size);
                }

                healTimer += edelta();

                if(healTimer >= healInterval){
                    healTimer = 0f;

                    float missingPercent =
                        (maxHealth - health) / maxHealth;

                    float healValue =
                        healAmount +
                        healAmount * missingPercent * 2f;

                    healValue = Math.min(healValue, maxHeal);

                    heal(healValue);
                }

            }else{
                regenerationActive = false;
            }
        }

        @Override
        public void damage(float damage) {
            super.damage(damage);
            damageTimer = regenDelay;
            healTimer = 0f;
            regenerationActive = false;
        }
    }
}
