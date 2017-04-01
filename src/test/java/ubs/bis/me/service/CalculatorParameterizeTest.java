package ubs.bis.me.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ubs.bis.me.domain.Instrument;
import ubs.bis.me.domain.Market;
import ubs.bis.me.domain.State;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test all the permutation of Market and Instrument for VWAP price
 */
@RunWith(Parameterized.class)
public class CalculatorParameterizeTest {

    private double price;

    private double amount;

    private double expectedResult;

    private double[] TEST_RANGE = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};

    public CalculatorParameterizeTest(double price,
                                      double amount,
                                      double expectedResult) {
        this.price = price;
        this.amount = amount;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection testParameters() {
        return Arrays.asList(new Object[]{10, 10, 100},
                new Object[]{100, 10, 1000},
                new Object[]{1000, 10, 10000},
                new Object[]{10, 100, 100},
                new Object[]{100, 1000, 1000},
                new Object[]{1000, 10000, 10000},
                new Object[]{5, 10, 50},
                new Object[]{50, 10, 500},
                new Object[]{500, 10, 5000},
                new Object[]{5, 100, 50},
                new Object[]{50, 1000, 500},
                new Object[]{500, 10000, 5000}
        );
    }

    @Test
    public void applyMarketUpdateTest() {
        Calculator calculator = new CalculatorImpl();
        Arrays.stream(Market.values()).forEach(market -> Arrays.stream(Instrument.values()).forEach(instrument -> {
            final TwoWayPrice[] applyMarketUpdate = {null};
            DoubleStream.of(TEST_RANGE).forEach(p -> {
                TwoWayImpl twoWayPrice = new TwoWayImpl(
                        instrument,
                        State.FIRM,
                        p * price,
                        p * amount,
                        p * price,
                        p * amount);
                MarketUpdate marketUpdate = new MarketUpdateImpl(market, twoWayPrice);
                applyMarketUpdate[0] = calculator.applyMarketUpdate(marketUpdate);
            });
            assertThat(applyMarketUpdate[0], is(notNullValue()));
            assertThat(expectedResult, is(applyMarketUpdate[0].getBidPrice()));
            assertThat(expectedResult, is(applyMarketUpdate[0].getOfferPrice()));
        }));
    }

    @Test
    public void applyMarketUpdateForSingleMarketTest() {
        Calculator calculator = new CalculatorImpl();
        final TwoWayPrice[] applyMarketUpdate = {null};
        DoubleStream.of(TEST_RANGE).forEach(p -> {
            TwoWayImpl twoWayPrice = new TwoWayImpl(
                    Instrument.INSTRUMENT0,
                    State.FIRM,
                    p * price,
                    p * amount,
                    p * price,
                    p * amount);
            MarketUpdate marketUpdate = new MarketUpdateImpl(Market.MARKET0, twoWayPrice);
            applyMarketUpdate[0] = calculator.applyMarketUpdate(marketUpdate);
        });
        assertThat(applyMarketUpdate[0], is(notNullValue()));
        assertThat(expectedResult, is(applyMarketUpdate[0].getBidPrice()));
        assertThat(expectedResult, is(applyMarketUpdate[0].getOfferPrice()));
    }
}
