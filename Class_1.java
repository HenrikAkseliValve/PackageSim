package objectcourseend;

/** Class Class_1*/
public class Class_1 extends AbstractPackage {
  
    /**
     * Constructor for package.
     *
     * @param item is the item in side of the package
     * @param target package is traveling to this SmartPost
     * @param from SmartPost package is coming from
     * @param travel length which package is delivered
     */
    public Class_1(Item item,SmartPost target,SmartPost from,int travel){
        super(item,target,from,(int)0.5*travel);
    }

    @Override
    public void onSending(){
        super.onSending();
        if(this.item.isFractional()){
          this.broken=true;
        }
    }
    
    

}
