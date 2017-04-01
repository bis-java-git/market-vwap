package ubs.bis.me.service;

import ubs.bis.me.domain.Instrument;
import ubs.bis.me.domain.State;

/**
 * VWAP TwoWayPrice
 */
public interface TwoWayPrice {

    Instrument getInstrument();

    State getState();

    double getBidPrice();

    double getOfferAmount();

    double getOfferPrice();

    double getBidAmount();
}
