import java.awt.{Robot, MouseInfo}

import math.geom2d.{Point2D, Vector2D}


/**
 * Created by Chris on 8/4/2015. This class actually handles
 */
class Engine(pollingRate: Option[Double], startupThreshold: Option[Double], giveupThreshold: Option[Double], drag: Option[Double]) extends Runnable{

  //Setting up engine
  val robot = new Robot
  val noVector = new Vector2D(0,0)
  var lastVector = noVector



  def run() = while(true){
    //Poll for mouse movement
    Thread.sleep(pollingRate.getOrElse(10.00).toInt)
    val startPos = new Point2D(MouseInfo.getPointerInfo.getLocation)
    Thread.sleep(pollingRate.getOrElse(10.00).toInt)
    val endPos = new Point2D(MouseInfo.getPointerInfo.getLocation)

    //If this movement was significant
    if(!startPos.almostEquals(endPos, giveupThreshold.getOrElse(1))){
      //If this movement was violent
      if(!startPos.almostEquals(endPos, startupThreshold.getOrElse(7))){
        //Use the last polled movement as the new trackball speed
        lastVector = new Vector2D(startPos, endPos)
      }else{
        //The mouse is being used lightly, set trackball speed to nothing
        lastVector = noVector
      }
    //If last polled mouse movement was insignificant
    }else{
      //Only bother if there's a vector to move the mouse along
      if(lastVector != noVector){
        //Move the mouse according to the last trackball speed
        robot.mouseMove((endPos.x + lastVector.x).toInt, (endPos.y + lastVector.y).toInt)
        //Apply friction to trackball
        lastVector = new Vector2D(lastVector.x * drag.getOrElse(.975), lastVector.y * drag.getOrElse(.975))
        //If the trackball is almost stopped, stop it
        if(lastVector.almostEquals(noVector, 2)){
          lastVector = noVector
        }
      }
    }
  }
}
