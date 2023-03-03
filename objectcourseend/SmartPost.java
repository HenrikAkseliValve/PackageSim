package objectcourseend;

import netscape.javascript.JSObject;

/** Class SmartPost */
public class SmartPost {
    
  
    /** Storage of the SmartPost */
    final private Storage storage;
  
    /** Saves SmartPost's marker's position */
    private double[] position;
  
    /** Address of the SmartPost */
    private String address;
  
    /** Postal code of the SmartPost*/
    private String postalCode;
  
    /** Region of the SmartPost */
    private String region;
  
    /** Name of the SmartPost */
    private String name;
  
    public SmartPost(String address,String postalcode,String region,double position[],String name){
        this.address=address;
        this.postalCode=postalcode;
        this.region=region;
        this.position=position;
        this.name=name;
        this.storage = new Storage();
    }

    /**
     * Get the name of the SmartPost.
     * @return Name of the SmartPost.
     */
    public String getName(){
        return this.name;
    }
    
    public String getFullAddress(){
        return this.address+", "+this.postalCode+" "+this.region;
    }
  
    /**
     * Get the value of storage
     * @return the value of storage
     */
    private Storage getStorage(){
        return this.storage;
    }

    /**
     * Get the position of the marker
     * Tells were the SmartPost is on at the map
     * @return The value marker's position
     */
    public double[] getPosition(){
        return this.position;
    }

    /**
     * Gives packet to SmartPost to deliver
     * @param _package
     */
    public void addPackage(AbstractPackage _package){
        this.storage.add(_package);
    }

    /**
     * Updates packages situation for  
     */
    public AbstractPackage update(){
        return this.storage.pop();
    }


}
