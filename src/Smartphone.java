
public class Smartphone {

    private String name;
    String processor;
    String screenTechnology;
    String screenSize;
    String resolution;
    private double price;

    Smartphone(String name, String processor, String screenTechnology, String screenSize, String resolution, double price) {
        this.name = name;
        this.processor = processor;
        this.screenTechnology = screenTechnology;
        this.screenSize = screenSize;
        this.resolution = resolution;
        this.price = price;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

}
