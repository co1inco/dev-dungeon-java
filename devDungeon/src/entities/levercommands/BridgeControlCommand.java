package entities.levercommands;

import core.Game;
import core.level.Tile;
import core.level.elements.tile.PitTile;
import core.level.utils.Coordinate;
import core.level.utils.LevelElement;
import systems.FogOfWarSystem;
import utils.ICommand;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The BridgeControlCommand class is responsible for controlling the bridge in the Bridge Guard
 * Riddle Level.
 *
 * <p>The bridge can be lowered and raised by executing and undoing this command.
 */
public class BridgeControlCommand implements ICommand {
  private final Coordinate topLeft;
  private final Coordinate bottomRight;

  /**
   * Creates a new BridgeControlCommand instance.
   *
   * @param topLeft The top left coordinate of the bridge.
   * @param bottomRight The bottom right coordinate of the bridge.
   */
  public BridgeControlCommand(Coordinate topLeft, Coordinate bottomRight) {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  private void foreachPit(Consumer<PitTile> predicate) {
      for (int x = this.topLeft.x; x <= this.bottomRight.x; x++) {
          for (int y = this.bottomRight.y; y <= this.topLeft.y; y++) {
              Tile tile = Game.currentLevel().tileAt(new Coordinate(x, y));
              if (!(tile instanceof PitTile pit)) // (tile is not PitTile pitTile) :((
                  return;

              predicate.accept(pit);
          }
      }
  }

  /**
   * Raises the bridge. By opening the pits, the bridge is raised.
   *
   * @see core.level.elements.tile.PitTile#open() PitTile.open
   * @see core.level.elements.ILevel#tilesInArea(Coordinate, Coordinate) tilesInArea
   */
  @Override
  public void execute() {
      foreachPit(PitTile::open);
  }

  /**
   * Lowers the bridge. By closing the pits, the bridge is lowered.
   *
   * @see core.level.elements.tile.PitTile#close() PitTile.close
   * @see core.level.elements.ILevel#tilesInArea(Coordinate, Coordinate) tilesInArea
   */
  @Override
  public void undo() {
      foreachPit(PitTile::close);
  }
}
