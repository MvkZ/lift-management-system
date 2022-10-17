package models

/**
 * direction Class for updating the lift direction
 * @param inputDirection
 */
class Direction(inputDirection: String) {

  var direction: String = _

  def initialize(): Unit = {
    direction = inputDirection
  }

  initialize()
}

object Direction {
  val movingUp = "UP"
  val movingDown = "DOWN"
  val idle = "IDLE"
}
