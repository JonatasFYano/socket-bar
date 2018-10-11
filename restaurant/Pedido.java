package restaurant;
public class Pedido {
    private String ID;
    private String status;
    private String waiterID;
    private String tableID;
    private String items;
    private String obs;

    public Pedido(String waiterID, String tableID, String itens, String obs){
        setWaiterID(waiterID);
        setTableID(tableID);
        setItems(itens);
        setObs(obs);
    }

    /**
     * @return the iD
     */
    public String getID() {
        return ID;
    }
    
    /**
     * @return the obs
     */
    public String getObs() {
        return obs;
    }

    /**
     * @param obs the obs to set
     */
    public void setObs(String obs) {
        this.obs = obs;
    }

    /**
     * @return the items
     */
    public String getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(String items) {
        this.items = items;
    }

    /**
     * @param iD the iD to set
     */
    public void setID(String iD) {
        this.ID = iD;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @return the tableID
     */
    public String getTableID() {
        return tableID;
    }
    /**
     * @param tableID the tableID to set
     */
    public void setTableID(String tableID) {
        this.tableID = tableID;
    }
    /**
     * @return the waiterID
     */
    public String getWaiterID() {
        return waiterID;
    }
    /**
     * @param waiterID the waiterID to set
     */
    public void setWaiterID(String waiterID) {
        this.waiterID = waiterID;
    }
}