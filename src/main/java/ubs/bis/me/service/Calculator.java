package ubs.bis.me.service;

/**
 * VWAP Calculator
 */
public interface Calculator {

    /**
     * Assumption is based on that VWAP price is calculated from all the 50 markets
     * And second assumption is taken that up to date (which is supplied value) is taken into consideration.
     *
     * @param twoWayMarketPrice twoWayMarketPrice
     * @return TwoWayPrice
     */
    TwoWayPrice applyMarketUpdate(final MarketUpdate twoWayMarketPrice);
}
