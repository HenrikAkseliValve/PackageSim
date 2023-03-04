package sim;

/** Class Item */
public class Item{

    /** Name of the item */
    String name;
    /** Can item broke on delivery */
    boolean fractionality;
    /** In centimeters x-axel length of the item */
    int sizex;
    /** In centimeters y-axel length of the item */
    int sizey;
    /** In centimeters z-axel length of the item */
    int sizez;

    /* Basic constructor for item */
    public Item(String name, int width, int height, int dept, boolean fractional) {
      this.name=name;
      this.sizex=width;
      this.sizey=height;
      this.sizez=dept;
      this.fractionality=fractional;
    }


    /**
     * Gives item's name
     * @return       String
     */
    public String getName(){
      return this.name;
    }


    /**
     * Gives information on item's fractionality
     * @return       Boolean
     */
    public Boolean isFractional(){
      return this.fractionality;
    }


    /**
     * Gives the volume of the item in cm^3
     * @return       Integer
     */
    public Integer getVolume(){
      return this.sizex*this.sizey*this.sizez;
    }


}
