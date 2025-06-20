package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import contrib.utils.components.health.DamageType;
import contrib.utils.components.skill.DamageProjectile;
import core.Entity;
import core.level.elements.ILevel;
import core.utils.Point;
import core.utils.components.path.IPath;
import core.utils.components.path.SimpleIPath;
import item.effects.BurningEffect;
import systems.EventScheduler;
import utils.EntityUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Subclass of {@link DamageProjectile}.
 *
 * <p>The FireballSkill class extends the functionality of {@link DamageProjectile} to implement the
 * specific behavior of the fireball skill. *
 *
 * <p>The projectile will fly through the dungeon, and if it hits an entity, it will deal damage and
 * be removed from the game. It will also be removed from the game if it hits a wall or has reached
 * the maximum distance.
 */
public final class ProtectorSkill implements Consumer<Entity> {
    private static final IPath PROJECTILE_TEXTURES = new SimpleIPath("skills/fireball");
    private static final IPath PROJECTILE_SOUND = new SimpleIPath("sounds/fireball.wav");
    private static final float PROJECTILE_SPEED = 30.0f;
    private static final DamageType DAMAGE_TYPE = DamageType.FIRE;
    private static final Point HIT_BOX_SIZE = new Point(3, 3);
    private static final BurningEffect BURNING_EFFECT = new BurningEffect(1f, 1);
    public static float PROJECTILE_RANGE = 14f;
    public static boolean UNLOCKED = false;
    public static int DAMAGE_AMOUNT = 20;

    private final Supplier<Point> targetSelection;

    /**
     * Create a {@link DamageProjectile} that looks like a fireball and will cause fire damage.
     *
     * @param targetSelection A function used to select the point where the projectile should fly to.
     * @see DamageProjectile
     */
    public ProtectorSkill(final Supplier<Point> targetSelection) {
        this.targetSelection = targetSelection;
    }

    private static Supplier<Point> getInitialPosition(final Supplier<Point> targetPosition) {
        int xOffset = ILevel.RANDOM.nextInt(9);
        int yOffset = ILevel.RANDOM.nextInt(9);
        Point p = targetPosition.get();

        return new Supplier<Point>() {
            @Override
            public Point get() {
              return new Point(p.x + xOffset, p.y + yOffset);
            }
        };
    }

    @Override
    public void accept(Entity entity) {
        int xOffset = ILevel.RANDOM.nextInt(3);
        int yOffset = ILevel.RANDOM.nextInt(3);
        Point p = targetSelection.get();
        Point randomPoint = new Point(p.x + xOffset, p.y + yOffset);

        MonsterType monster = MonsterType.ORC_WARRIOR;
        EntityUtils.spawnMonster(monster, randomPoint);
    }

//  @Override
//  protected Sound playSound() {
//    Sound soundEffect = Gdx.audio.newSound(Gdx.files.internal(PROJECTILE_SOUND.pathString()));
//
//    // Generate a random pitch between 1.5f and 2.0f
//    float minPitch = 2f;
//    float maxPitch = 3f;
//    float randomPitch = MathUtils.random(minPitch, maxPitch);
//
//    // Play the sound with the adjusted pitch
//    long soundId = soundEffect.play();
//    soundEffect.setPitch(soundId, randomPitch);
//
//    // Set the volume
//    soundEffect.setVolume(soundId, 0.05f);
//
//    EventScheduler.getInstance().scheduleAction(soundEffect::dispose, 1000L);
//    return soundEffect;
//  }
}
