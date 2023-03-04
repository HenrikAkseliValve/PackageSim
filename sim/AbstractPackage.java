package sim;

import javafx.application.Platform;
import javafx.scene.paint.Color;

/** Class AbstractPackage*/
public abstract class AbstractPackage{

    /** True if item broke on delivery */
    Boolean broken=false;
    /** Reference to item package has */
    Item item;
    /** SmartPost the package is going */
    SmartPost target;
    /** Tells were item was send */
    SmartPost from;
    /** Tells how much package has to still travel  */
    private int travel;

    /** Package size limit */
    final public static int VOLUME_LIMIT = 200;
        
    /**
     * Constructor for package.
     *
     * @param item is the item in side of the package
     * @param target package is traveling to this SmartPost
     * @param from SmartPost package is coming from
     * @param travel travel length which package is delivered
     */
    public AbstractPackage(Item item,SmartPost target,SmartPost from,int travel){
      this.item=item;
      this.target=target;
      this.from=from;
      this.travel=travel;
    }

    /**
     * Is fired when package is send from SmartPost.
     * Used to calculate changes of broken or lost.
     */
    public void onSending(){
    
    }

    /**
     * When packages comes to SmartPost this is fired
     */
    public void onReceiving(){
        String sbroken;
        if(this.broken){
            sbroken = "broken";
        }
        else{
            sbroken = "intact";
        }
        final String buffer="Item "+this.item.getName()
                      +" has been received by "+this.target.getName()
                      +" from "+this.from.getName()
                      +" in state "+sbroken+".";
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                ObjectCourseEnd.help.setText(buffer);
                ObjectCourseEnd.help.setTextFill(Color.GREEN);
            }
        });
    }

    /**
     * Moves the package one step closer to destination and gives reminder.
     * @return Gives number of ticks until package is at it's destination.
     */
    public int move(){
      return this.travel--;  
    }
  
}
