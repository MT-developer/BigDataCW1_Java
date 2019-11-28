public class item {
    String itemName, itemLink, itemWebsite;
    int itemVatPrice, itemExVatPrice;

    public item(String iName, int iVatPrice, int iExVatPrice, String iLink, String iWebsite) {
        this.itemName = iName;
        this.itemVatPrice = iVatPrice;
        this.itemExVatPrice = iExVatPrice;
        this.itemLink = iLink;
        this.itemWebsite = iWebsite;
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

    public void setItemExVatPrice(int itemOrgPrice) {
        this.itemExVatPrice = itemOrgPrice;
    }

    public int getItemExVatPrice() {
        return itemExVatPrice;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public int getItemVatPrice() {
        return itemVatPrice;
    }

    public void setItemWebsite(String itemWebsite) {
        this.itemWebsite = itemWebsite;
    }

    public String getItemWebsite() {
        return itemWebsite;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}