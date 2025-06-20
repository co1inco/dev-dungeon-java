package utils;

import com.badlogic.gdx.ai.pfa.GraphPath;
import contrib.components.AIComponent;
import contrib.utils.components.ai.AIUtils;
import contrib.utils.components.ai.ISkillUser;
import contrib.utils.components.skill.Skill;
import core.Entity;
import core.Game;
import core.components.PositionComponent;
import core.level.Tile;
import core.level.utils.LevelUtils;
import core.utils.Point;
import entities.MonsterType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static core.level.utils.LevelUtils.accessibleTilesInRange;

public final class ProtectorAI implements Consumer<Entity>, ISkillUser {

    private final float attackRange;
    private final float distance;
    private Skill skill;
    private GraphPath<Tile> path;

    /**
     * Attacks the nearest monster if he is within the given range between attackRange and distance. Otherwise,
     * it will move into that range.
     *
     * @param attackRange Maximal distance to monster in which the attack skill should be executed.
     * @param distance Minimal distance to hero in which the attack skill should be executed.
     * @param skill Skill to be used when an attack is performed.
     */
    public ProtectorAI(final float attackRange, final float distance, final Skill skill) {
        if (attackRange <= distance || distance < 0) {
            throw new IllegalArgumentException(
                "attackRange must be greater than distance and distance must be 0 or greater than 0");
        }
        this.attackRange = attackRange;
        this.distance = distance;
        this.skill = skill;
    }

    private Entity findNearestMob(Point targetPosition) {

        // Get all monsters. (Entities with AI and Position)
        List<Entity> entities = Game.entityStream(new HashSet<>(List.of(
            AIComponent.class,
            PositionComponent.class))).toList();

        Entity nearestEntity = null;
        double nearestEntityDistance = 50;

        for (Entity e : entities) {
            AIComponent ai = e.fetch(AIComponent.class).orElseThrow();
            PositionComponent ps = e.fetch(PositionComponent.class).orElseThrow();

            // Don't fire on other protectors
            if (Objects.equals(e.name(), MonsterType.PROTECTOR.name())) {
                continue;
            }
            // Ignore hero
            if (Game.hero().map(h -> h == e).orElse(false)) {
                continue;
            }

            double distance = ps.position().distance(targetPosition);

            if (nearestEntity == null) {
                nearestEntity = e;
                nearestEntityDistance = distance;
            }
            else if (distance < nearestEntityDistance && distance > 0.5) {
                nearestEntity = e;
                nearestEntityDistance = distance;
            }
        }

        return nearestEntity;
    }

    @Override
    public void accept(final Entity entity) {

        PositionComponent ps = entity.fetch(PositionComponent.class).orElseThrow();
        Entity targetEntity = findNearestMob(ps.position());

        if (targetEntity == null) {
            return;
        }

        PositionComponent targetPs = targetEntity.fetch(PositionComponent.class).orElseThrow();

        if (ps.position().distance(targetPs.position()) < attackRange) {
            this.useSkill(this.skill, entity);
        }
        else {

            path = LevelUtils.calculatePath(entity, targetEntity);
            AIUtils.move(entity, path);
        }
    }

    @Override
    public void useSkill(Skill skill, Entity skillUser) {
        if (skill == null) {
            return;
        }
        skill.execute(skillUser);
    }

    @Override
    public Skill getSkill() {
        return this.skill;
    }

    @Override
    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
