package models

import core.Lift

import scala.collection.mutable

/**
 * trait for any kind of building structure which has no of lifts
 */
trait BuildingStructure {

  var lifts: List[Lift]
  var floors: List[Floor]
  var requests: mutable.Queue[Request]

  def initializeLifts(no: Int): Unit

  def initializeFloors(no: Int): Unit

  def addRequest(source: Int, destination: Int): Unit

  def workLifts(): Unit
}
