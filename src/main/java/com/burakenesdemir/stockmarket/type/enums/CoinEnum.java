package com.burakenesdemir.stockmarket.type.enums;

public enum CoinEnum {
    BITCOIN("bitcoin", "http://localhost:8081/images/bitcoin.png"),
    ETHEREUM("ethereum", "http://localhost:8081/images/ethereum.png"),
    HOLO("holo", "http://localhost:8081/images/holo.png"),
    SHIBACOIN("shibacoin", "http://localhost:8081/images/shiba.png"),
    DOGECOIN("dogecoin", "http://localhost:8081/images/dogecoin.png");

    private String name;

    private String imgUrl;

    CoinEnum(String enumName, String imgUrl) {
        this.name = enumName;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl(){
        return imgUrl;
    }
}
