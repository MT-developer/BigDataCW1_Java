package objects;

public class item {
    private static final long serialVersionUID = 1L;
    private String itemName, itemLink;
    private int itemVatPrice, id;
    private String itemImage;

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public item() {}

    public item(String iName, int iVatPrice, String iLink, String iImageLink) {
        this.itemName = iName;
        this.itemVatPrice = iVatPrice;
        this.itemImage = iImageLink;
        this.itemLink = iLink;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemVatPrice(int itemPrice) {
        this.itemVatPrice = itemPrice;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public int getItemVatPrice() {
        return itemVatPrice;
    }



    @Override
    public String toString() {
        return super.toString();
    }
}