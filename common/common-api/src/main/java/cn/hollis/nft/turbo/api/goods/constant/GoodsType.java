package cn.hollis.nft.turbo.api.goods.constant;

/**
 * 商品类型
 */
public enum GoodsType {
    COLLECTION("藏品"),
    BLIND_BOX("盲盒");

    private String value;

    GoodsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
