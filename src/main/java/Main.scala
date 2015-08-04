import com.beust.jcommander.{JCommander, Parameter}


/**
 * Created by Chris on 8/4/2015. Main execution environment
 */
object Main extends App {
  //Strings
  final val name = "Virtual Trackball"
  final val version = "1.0"
  final val author = "chaorace (Chris Crockett)"

  //Tooltip descriptions
  final val pollingRateDesc =
    "The rate at which mouse movement is measured.\n" +
      "Lower values are smoother, Higher values are less intensive.\n" +
      "Changing this option directly affects how all other options are evaluated\n" +
      "(don't change this unless you know what you're doing!).\n" +
      "Default value (5)"
  final val startupThresholdDesc =
    "The lowest amount of movement that can start trackball spinning.\n" +
      "Default value (10)"
  final val giveupThresholdDesc =
    "The lowest amount of movement that can interrupt trackball spinning.\n" +
      "Default value (2)"
  final val dragDesc =
    "The rate at which the trackball slows down. 1 eliminates all slowdown.\n" +
      ">1 values cause the trackball to speed up.\n" +
      "Default value (.98)"

  //Command line parameters
  object Args {
    @Parameter(
      names = Array("-p", "--PollingRate"),
      description = pollingRateDesc)
    var pollingRate: String = null
    @Parameter(
      names = Array("-t", "--Tolerance"),
      description = startupThresholdDesc)
    var startupThreshold: String = null
    @Parameter(
      names = Array("-s", "--Sensitivity"),
      description = giveupThresholdDesc)
    var giveupThreshold: String = null
    @Parameter(
      names = Array("-f", "--Friction"),
      description = dragDesc)
    var drag: String = null
  }

  override def main(args: Array[String]): Unit = {
    //If there are no command line arguments, boot to gui
    if (args.isEmpty) {
      val gui = new Gui
      gui.main(new Array[String](0))
    } else {
      //An unreasonable amount of work to get a generally crummy library to play nice with Scala. 0/10 experience, would not recommend JCommander for Scala use
      try{
        new JCommander(Args, args.toArray: _*)
        val pollingRateString = Option(Args.pollingRate)
        val startupThresholdString = Option(Args.startupThreshold)
        val giveupThresholdString = Option(Args.giveupThreshold)
        val dragString = Option(Args.drag)

        val pollingRate = pollingRateString match {
          case None => None
          case Some(x: String) => Some(x.toDouble)
        }
        val startupThreshold = startupThresholdString match {
          case None => None
          case Some(x: String) => Some(x.toDouble)
        }
        val giveupThreshold = giveupThresholdString match {
          case None => None
          case Some(x: String) => Some(x.toDouble)
        }
        val drag = dragString match {
          case None => None
          case Some(x: String) => Some(x.toDouble)
        }

        //Start engine with provided parameters
        val engine = new Engine(pollingRate, startupThreshold, giveupThreshold, drag)
        engine.run()
      }catch{
        case e: Exception =>
          val engine = new Engine(None, None, None, None)
          engine.run()
      }
    }
  }
}
