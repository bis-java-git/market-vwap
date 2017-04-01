package ubs.bis.me.service;

import ubs.bis.me.domain.Market;

/**
 * VWAP MarketUpdate
 */
public interface MarketUpdate {

    Market getMarket();

    TwoWayPrice getTwoWayPrice();
}
