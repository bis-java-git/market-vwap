package ubs.bis.me.service;

import ubs.bis.me.domain.Instrument;
import ubs.bis.me.domain.State;

/**
 * VWAP TwoWayPrice calculation engine
 */
public final class TwoWayImpl implements TwoWayPrice {

    private Instrument instrument;

    private State state;

    private double bidPrice;

    private double bidAmount;

    private double offerPrice;

    private double offerAmount;


    TwoWayImpl(Instrument instrument,
               State state,
               double bidPrice,
               double bidAmount,
               double offerPrice,
               double offerAmount) {

        this.instrument = instrument;
        this.state = state;
        this.bidPrice = bidPrice;
        this.bidAmount = bidAmount;
        this.offerPrice = offerPrice;
        this.offerAmount = offerAmount;
    }

    @Override
    public Instrument getInstrument() {
        return this.instrument;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public double getBidPrice() {
        return this.bidPrice;
    }

    @Override
    public double getOfferAmount() {
        return this.offerAmount;
    }

    @Override
    public double getOfferPrice() {
        return this.offerPrice;
    }

    @Override
    public double getBidAmount() {
        return this.bidAmount;
    }

    @Override
    public String toString() {
        return "TwoWayImpl{" + "instrument=" + instrument +
                ", state=" + state +
                ", bidPrice=" + bidPrice +
                ", bidAmount=" + bidAmount +
                ", offerPrice=" + offerPrice +
                ", offerAmount=" + offerAmount +
                '}';
    }
}
