import java.awt.{Robot, MouseInfo}

import math.geom2d.{Point2D, Vector2D}


/**
 * Created by Chris on 8/4/2015.
 */
object Main extends App{
  val robot = new Robot

  val noVector = new Vector2D(0,0)

  val drag = .98
  var lastVector = noVector
  while(true){
    Thread.sleep(10)
    val startPos = new Point2D(MouseInfo.getPointerInfo.getLocation)
    Thread.sleep(10)
    val endPos = new Point2D(MouseInfo.getPointerInfo.getLocation)
    //If this movement was significant
    if(!startPos.almostEquals(endPos, 5)){
      //If this movement was violent
      if(!startPos.almostEquals(endPos, 10)){
        lastVector = new Vector2D(startPos, endPos)
      }else{
        lastVector = noVector
      }
    }else{
      robot.mouseMove((endPos.x + lastVector.x).toInt, (endPos.y + lastVector.y).toInt)
      lastVector = new Vector2D(lastVector.x * drag, lastVector.y * drag)
      if(lastVector.almostEquals(noVector, 2)){
        lastVector = noVector
      }
    }
  }
}
