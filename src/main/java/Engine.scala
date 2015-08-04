import java.awt.{Robot, MouseInfo}

import math.geom2d.{Point2D, Vector2D}


/**
 * Created by Chris on 8/4/2015. This class actually handles
 */
class Engine(pollingRate: Option[Double], startupThreshold: Option[Double], giveupThreshold: Option[Double], drag: Option[Double]) extends Runnable{

  val robot = new Robot
  val noVector = new Vector2D(0,0)
  var lastVector = noVector



  def run() = while(true){
    Thread.sleep(pollingRate.getOrElse(10.00).toInt)
    val startPos = new Point2D(MouseInfo.getPointerInfo.getLocation)
    Thread.sleep(pollingRate.getOrElse(10.00).toInt)
    val endPos = new Point2D(MouseInfo.getPointerInfo.getLocation)
    //If this movement was significant
    if(!startPos.almostEquals(endPos, giveupThreshold.getOrElse(5))){
      //If this movement was violent
      if(!startPos.almostEquals(endPos, startupThreshold.getOrElse(10))){
        lastVector = new Vector2D(startPos, endPos)
      }else{
        lastVector = noVector
      }
    }else{
      robot.mouseMove((endPos.x + lastVector.x).toInt, (endPos.y + lastVector.y).toInt)
      lastVector = new Vector2D(lastVector.x * drag.getOrElse(.98), lastVector.y * drag.getOrElse(.98))
      if(lastVector.almostEquals(noVector, 2)){
        lastVector = noVector
      }
    }
  }
}
