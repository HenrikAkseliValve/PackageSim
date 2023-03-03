package objectcourseend;

import java.util.LinkedList;


/** Class Storage */
public class Storage {


    /** Packages in storage */
    private LinkedList<AbstractPackage> PackageQueue=new LinkedList();

    public void add(AbstractPackage _package) {
        this.PackageQueue.add(_package);
    }
    
    public AbstractPackage pop(){
        if(this.PackageQueue.size()>0){
          return this.PackageQueue.pop();
        }
        return null;
    }
  
}
