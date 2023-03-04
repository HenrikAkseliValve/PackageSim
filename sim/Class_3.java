package sim;

import static sim.SimMain.RANDOM;
        

/** Class Class_3 */
public class Class_3 extends AbstractPackage {


  /**
   * Constructor for package.
   *
   * @param item is the item in side of the package
   * @param target package is traveling to this SmartPost
   * @param from SmartPost package is coming from
   * @param travel length which package is delivered
   */
  public Class_3(Item item,SmartPost target,SmartPost from,int travel){
    super(item,target,from,2*travel);
  }

    @Override
    public void onSending(){
        super.onSending();
        
        // Multipler for this complitly out of hat calculation
        int addtive;
        if(this.item.isFractional()) addtive=32;
        else addtive=0;
        
        // Calculation out of random hat
        if(RANDOM.nextInt(5)+addtive+64-this.item.getVolume()*64/AbstractPackage.VOLUME_LIMIT>64){
            this.broken=true;
        }
    }

}


