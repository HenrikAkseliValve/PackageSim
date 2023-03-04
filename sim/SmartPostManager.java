package sim;

import static sim.SimMain.help;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/** Class SmartPostManager */
public class SmartPostManager extends Task{

    /* Only object of SmartPostManager */
    final private static SmartPostManager manager=new SmartPostManager();

    /* Reference to WebEngine so that certain distance can be calculated */
    private static WebEngine wengine;
    
    /* Set the wengine */
    static void setWebEngine(WebEngine engine) {
        wengine=engine;
    }
    
     
    /* list of items created in the program */
    private HashMap<String,Item> itemList = new HashMap();
    
    /** map of Smart post in a map */
    private volatile HashMap<String,SmartPost> smartPosts=new HashMap();
    /** Boolean that tell is the smartPosts taken */
    private volatile boolean semiSmartPosts = false;
    /** Packages that are moving to target location */
    private volatile LinkedList<AbstractPackage> movingPacks=new LinkedList();
    /** Hash map of all the roads that are drawn at the screen identified by Smartpost names
     *  as keys to length of the road.
     * FORMAT OF THE KEY: name1+"\n"+name2 */
    private volatile HashMap<String,Integer> roads = new HashMap();
    
    /* Sax parser for loading the SmartPost from XML */
    private SAXParser io;
    /* General String parser for SAX wih our parameters */
    final private SaxXML parser=new SaxXML(new String[]{"code","city","address","postoffice","lat","lng"});
    /* Amount of keys parser copies */
    final static private int PARSER_LENGTH = 6;
    
    
    @Override
    public Integer call(){
        /* Last time packages were moved */
        long execution = System.currentTimeMillis();
        /* Current time start of the loop */
        long current;
        while(!ObjectCourseEnd.closing){
            
            // Move the package if 100 milliseconds has passed
            current = System.currentTimeMillis();
            if(current-execution>100){
                if(!this.smartPosts.isEmpty()){
                    AbstractPackage n;
                    
                    // Wait untill resource is free
                    while(this.semiSmartPosts){
                        try{
                            Thread.sleep(100);
                        }
                        catch(InterruptedException ex){
                            Logger.getLogger(SmartPostManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //Grap the smartPosts resource
                    this.semiSmartPosts = true;
                    
                    // Iterate throught SmartPost and check if storage has any packages
                    Iterator<SmartPost> ite = this.smartPosts.values().iterator();
                    SmartPost post;
                    
                    while(ite.hasNext()){
                        post = ite.next();
                        // If there package in storage update pops it and returns it 
                        if((n=post.update())!=null){
                            
                            // Now pakage is list of moving packages
                            this.movingPacks.add(n);
                        }
                    }
                    // Free the resource of smartPost
                    this.semiSmartPosts = false;
                    
                    // Iterate throught the movingPacks and move the packs one notch
                    for(AbstractPackage pack:this.movingPacks){
                        int i=0;
                        if((i=pack.move())==0){System.out.println("Package removed");
                            this.movingPacks.remove(pack);
                            pack.onReceiving();
                        }
                    }
                    
                    // Set the execution time to right one
                    execution = System.currentTimeMillis();
                }
            }
        }
        return 0;
    }
    
    public String[] getPostNames(){
        String arr[]=new String[this.smartPosts.size()];
        this.smartPosts.keySet().toArray(arr);
        return arr;
    }
    /**
     * Add item to item list
     * 
     * @param name is name of the item
     * @param width is width of the item
     * @param height is height of the item
     * @param dept is depth of the item
     * @param fractional tells if the object is fractional
     */
    public void addItem(String name,int width,int height,int dept,boolean fractional){
      this.itemList.put(name,new Item(name,width,height,dept,fractional));
    }
    
    /**
     * Clear the list of drawn paths
     */
    public void clearPaths(){
        this.roads.clear();
    }
    
    /**
     *  Adds the string to drawn roads list
     * @param name1 is name of first point
     * @param name2 is name of second point
     * @param pathlength is length of path that is saved
     */
    public void addDrawnRoad(String name1,String name2,Integer pathlength){
        this.roads.put(name1+"\n"+name2,pathlength);
    }
    
    /**
     * Checks does the road already drawn
     * @param name1
     * @param name2
     * @return 
     */
    public boolean isRoadNotDrawn(String name1,String name2){
        
        /** right to left meaning name1 then name2 */
        String right = name1+"\n"+name2;
        /** left to right meaning name2 then name1 */
        String left  = name2+"\n"+name1;
        for(String str:this.roads.keySet()){
            if(right.equals(str)||left.equals(str)){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get SmartPost manager that is shared by the applications different parts
     * @return Did I say something?
     */
    public static SmartPostManager getManager(){
        return manager;
    }
    
    private SmartPostManager(){
        try{
            io=SAXParserFactory.newInstance().newSAXParser();
        }
        catch(Exception ex){
            Logger.getLogger(SmartPostManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Get GeoPoint of certain SmartPost
     * @param name Name of SmartPost
     * @return Latitude and longitude of the SmartPost 
     */
    public double[] getGeoPoint(String name){
        return this.smartPosts.get(name).getPosition();
    }

    public boolean isUniqueName(String name){
        // Check that key doesn't exist     
        return this.smartPosts.keySet().stream().noneMatch((key) -> (name.equals(key)));
    }
  
    /**
     * Adds smart post to the map. Check first that name doesn't exist
     * @param name of smart post and key to find it 
     * @param newpost the SmartPost that is added
     */
    public void addSmartPost(String name,SmartPost newpost){
        //Check that name doesn't exist
        if(SmartPostManager.getManager().isUniqueName(name)){
            // Add the smart post
            this.smartPosts.put(name,newpost);
        }
    }

    /**
     * Loads the SmartPost from XML file
     * @param url is the location of XML
     * @return returns array of string that tell name and full address of SmartPosts
     */
    public SmartPost[] loadSmartPosts(String url){
        // Returnning value
        SmartPost arr[]=null;
        try{
            // Parse the data
            io.parse(url,this.parser);
            // Get the answer
            String buffer[]=this.parser.getResult();
            // Calculate loop size
            int length=buffer.length/PARSER_LENGTH;
            // Calsulate size of returning value
            arr = new SmartPost[length];
            //Get access to add smartPosts
            while(this.semiSmartPosts){
                try{
                    Thread.sleep(100);
                }
                catch(InterruptedException ex){
                    Logger.getLogger(SmartPostManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.semiSmartPosts=true;
            // Loop the values
            for(int i=0;i<length;i++){
              arr[i]=new SmartPost(buffer[PARSER_LENGTH*i+2],buffer[PARSER_LENGTH*i],buffer[PARSER_LENGTH*i+1],new double[]{
                  Double.valueOf(buffer[PARSER_LENGTH*i+4]),Double.valueOf(buffer[PARSER_LENGTH*i+5])}
              ,buffer[PARSER_LENGTH*i+3]);
              addSmartPost(buffer[PARSER_LENGTH*i+3],arr[i]);  
            }
            this.semiSmartPosts=false;
            // reset so that io can be used next time
            io.reset();
        }
        catch(Exception ex){
            Logger.getLogger(SmartPostManager.class.getName()).log(Level.SEVERE, null, ex);
            help.setText("Something went wrong while loadding");
            help.setTextFill(Color.RED);
        }
        return arr;
    }

    /* Get item names */
    String[] getItems(){
        String arr[]=new String[this.itemList.size()];
        this.itemList.keySet().toArray(arr);
        return arr;
    }

    public void addPackage(String to,String from,String item,String _class){
        // Make path between chosen points
        ArrayList p=new ArrayList();
        double p1[] = SmartPostManager.getManager().getGeoPoint(from);
        double p2[]= SmartPostManager.getManager().getGeoPoint(to);
            
        p.add(p1[0]);
        p.add(p1[1]);
        p.add(p2[0]);
        p.add(p2[1]);
        AbstractPackage _package;
        Integer distance=0;
        if(this.isRoadNotDrawn(to,from)){
            distance =((Double)wengine.executeScript("document.createPath("+ p + ", 'green', 1)")).intValue();
            this.addDrawnRoad(to,from,distance);
        }
        else{
            String right = to+"\n"+from;
            String left  = from+"\n"+to;
            
            for(String key:this.roads.keySet()){
                if(right.equals(key)||left.equals(key)){
                    distance=this.roads.get(key);
                }
            }
        }
        // Reference to item that will be packed
        Item itemobj = this.itemList.get(item);
        
        // what class this is?
        if(_class.equals("Class 1")){
            //Is distace too big?
            if(distance<150){
                // Is volume too big?
                if(itemobj.getVolume()>AbstractPackage.VOLUME_LIMIT){
                    // Class 1 can be created
                    _package = new Class_1(itemobj,this.smartPosts.get(to),this.smartPosts.get(from),distance);
                }
                else{
                    // Inform user that volume is too big
                    help.setText("Volume too big for Class 1!");
                    help.setTextFill(Color.RED);
                    return;
                }
            }
            else{
                // Infor user that distance between SmartPost is too large
                help.setText("Too far away for Class 1!");
                help.setTextFill(Color.RED);
                return;
            }
        }
        else if(_class.equals("Class 2")){
            // Is volume too big?
            if(itemobj.getVolume()>AbstractPackage.VOLUME_LIMIT/2){
                _package = new Class_2(itemobj,this.smartPosts.get(to),this.smartPosts.get(from),distance);
            }
            else{
                // Inform user that volume is too big
                help.setText("Volume too big for Class 2!");
                help.setTextFill(Color.RED);
                return;
            }
        }
        else{
            // Is volume too big?
            if(itemobj.getVolume()>AbstractPackage.VOLUME_LIMIT){
                _package = new Class_3(itemobj,this.smartPosts.get(to),this.smartPosts.get(from),distance);
            }
            else{
                // Inform user that volume is too big
                help.setText("Volume too big for Class 2!");
                help.setTextFill(Color.RED);
                return;
            }
        }
        _package.onSending();
        this.smartPosts.get(from).addPackage(_package);
    }
}
