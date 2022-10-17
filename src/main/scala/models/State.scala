package models

/**
 * State of the Lifts case class
 * @param status
 */
case class State(var status: String)

object State {
  val openState = "OPEN"
  val closeState = "CLOSE"
}
