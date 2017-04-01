package ubs.bis.me.service;

import org.junit.Test;
import ubs.bis.me.domain.Instrument;
import ubs.bis.me.domain.Market;
import ubs.bis.me.domain.State;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Run test for VWAP calculation
 */
public class CalculatorTest {

    @Test
    public void applyMarketUpdateForSingleMarketTest() {
        double PRICE = 10.0;
        double AMOUNT = 10.0;

        Calculator calculator = new CalculatorImpl();
        TwoWayImpl twoWayPrice = new TwoWayImpl(
                Instrument.INSTRUMENT0,
                State.FIRM,
                PRICE,
                AMOUNT,
                PRICE,
                AMOUNT);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
        TwoWayPrice applyMarketUpdate = calculator.applyMarketUpdate(marketUpdate);
        assertThat(applyMarketUpdate, is(notNullValue()));
        double EXPECTED_RESULT = 10.0;
        assertThat(EXPECTED_RESULT, is(applyMarketUpdate.getBidPrice()));
        assertThat(EXPECTED_RESULT, is(applyMarketUpdate.getOfferPrice()));
    }

    @Test
    public void applyMarketUpdateForSingleMarketWithZeroPriceTest() {
        double AMOUNT = 10.0;

        Calculator calculator = new CalculatorImpl();
        TwoWayImpl twoWayPrice = new TwoWayImpl(
                Instrument.INSTRUMENT0,
                State.FIRM,
                0.0,
                AMOUNT,
                0.0,
                AMOUNT);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
        TwoWayPrice applyMarketUpdate = calculator.applyMarketUpdate(marketUpdate);
        assertThat(applyMarketUpdate, is(notNullValue()));
        double EXPECTED_RESULT = 0.0;
        assertThat(EXPECTED_RESULT, is(applyMarketUpdate.getBidPrice()));
        assertThat(EXPECTED_RESULT, is(applyMarketUpdate.getOfferPrice()));
    }

    @Test(expected=NumberFormatException.class)
    public void applyMarketUpdateForSingleMarketWithZeroAmountTest() {
        double AMOUNT = 0.0;

        Calculator calculator = new CalculatorImpl();
        TwoWayImpl twoWayPrice = new TwoWayImpl(
                Instrument.INSTRUMENT0,
                State.FIRM,
                0.0,
                AMOUNT,
                0.0,
                AMOUNT);
        MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
        TwoWayPrice applyMarketUpdate = calculator.applyMarketUpdate(marketUpdate);
        assertThat(applyMarketUpdate, is(notNullValue()));
        double EXPECTED_RESULT = 0.0;
        assertThat(EXPECTED_RESULT, is(applyMarketUpdate.getBidPrice()));
        assertThat(EXPECTED_RESULT, is(applyMarketUpdate.getOfferPrice()));
    }
}
