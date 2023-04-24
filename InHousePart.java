
package jenncoleinventory.model;


public class InHousePart extends Part {
    private int machineId;

    public InHousePart(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }
 
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
    public int getMachineId() {
        return machineId;
    }    
}
