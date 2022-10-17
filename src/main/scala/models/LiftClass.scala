package models

import scala.collection.mutable

/**
 * abstract class defining the structure of the lifts
 */
abstract class LiftClass {

  var id: Int
  var currentFloor: Floor
  var liftState: LiftState
  var upDestinations: mutable.Queue[Request]
  var downDestinations: mutable.Queue[Request]

  def goToFloorUp(requests: List[Request]): Unit

  def goToFloorDown(requests: List[Request]): Unit

  def occupancyStatus(): State

  def getLiftState: LiftState

  def showPosition(): Unit

  def initialize(inputId: Int, floor: Floor, state: LiftState): Unit
}
