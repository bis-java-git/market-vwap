package ubs.bis.me.service;


import ubs.bis.me.domain.Market;

public final class MarketUpdateImpl implements MarketUpdate {

    private Market market;

    private TwoWayPrice twoWayPrice;

    public MarketUpdateImpl(Market market,
                            TwoWayPrice twoWayPrice) {
        this.market = market;
        this.twoWayPrice = twoWayPrice;
    }

    @Override
    public Market getMarket() {
        return this.market;
    }

    @Override
    public TwoWayPrice getTwoWayPrice() {
        return this.twoWayPrice;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MarketUpdateImpl{");
        sb.append("market=").append(market);
        sb.append(", twoWayPrice=").append(twoWayPrice);
        sb.append('}');
        return sb.toString();
    }
}
