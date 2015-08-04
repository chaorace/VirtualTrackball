/**
 * Created by Chris on 8/4/2015. Main execution environment
 */
object Main extends App{
  override def main(args: Array[String]): Unit = {
    if(args.isEmpty){
      val gui = new Gui
      gui.main(new Array[String](0))
    }
  }
}
