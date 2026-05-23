package voltaprotocol.world.blocks.defense;

import mindustry.world.blocks.defense.Wall;

public class RegenerativeWall extends Wall {

    public float healAmount = 2f;
    public float healInterval = 30f;
    public float regenDelay = 180f;
    public float maxHeal = 10f;

    public RegenerativeWall(String name, int size, int health, float healAmount, float maxHeal) {
        super(name);

        this.size = size;
        this.health = health;
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

        @Override
        public void updateTile() {
            super.updateTile();

            if(damageTimer > 0f){
                damageTimer -= edelta();
                return;
            }

            if(health < maxHealth){
                healTimer += edelta();
                if(healTimer >= healInterval){
                    healTimer = 0f;
                    float missingPercent = (maxHealth - health) / maxHealth;
                    float healValue = healAmount + healAmount * missingPercent * 2f;
                    healValue = Math.min(healValue, maxHeal);
                    heal(healValue);
                }
            }
        }

        @Override
        public void damage(float damage) {
            super.damage(damage);
            damageTimer = regenDelay;
            healTimer = 0f;
        }
    }
}