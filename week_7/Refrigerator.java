class Refrigerator extends Appliance{
    public Refrigerator(String brand){
        super(brand);
    }
    @Override
    public void operate(){
        System.out.println("Stores food and drinks");
    }
}