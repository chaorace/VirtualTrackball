import org.clapper.argot.ArgotParser

/**
 * Created by Chris on 8/4/2015. Main execution environment
 */
object Main extends App {
  val name = "Virtual Trackball"
  val version = "1.0"
  val author = "chaorace (Chris Crockett)"

  val pollingRateDesc =
    "The rate at which mouse movement is measured.\n" +
      "Lower values are smoother, Higher values are less intensive.\n" +
      "Changing this option directly affects how all other options are evaluated\n" +
      "(don't change this unless you know what you're doing!).\n" +
      "Default value (5)"
  val startupThresholdDesc =
    "The lowest amount of movement that can start trackball spinning.\n" +
      "Default value (10)"
  val giveupThresholdDesc =
    "The lowest amount of movement that can interrupt trackball spinning.\n" +
      "Default value (5)"
  val dragDesc =
    "The rate at which the trackball slows down. 1 eliminates all slowdown.\n" +
      ">1 values cause the trackball to speed up.\n" +
      "Default value (.98)"

  override def main(args: Array[String]): Unit = {
    if(args.isEmpty){
      val gui = new Gui
      gui.main(new Array[String](0))
    }
  }
}
