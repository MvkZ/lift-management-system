import core.Building
import models.Direction

/**
 * This class is used to print the lift status if the lift is moving, it is spawn as a seperate thread
 * @param building
 */
class ShowStatus(building: Building) extends Runnable {

  var currenttBuilding: Building = _

  def initialize() = {
    currenttBuilding = building
  }

  initialize()

  override def run(): Unit = {
    val lifts = currenttBuilding.lifts
    while (true) {
      var checkUp = lifts.exists(_.getLiftState.liftDirection.direction != Direction.idle)
      if (checkUp) {
        lifts.foreach(_.showPosition())
        println()
        Thread.sleep(1000)
        checkUp = lifts.exists(_.getLiftState.liftDirection.direction != Direction.idle)
      }
    }
  }
}
