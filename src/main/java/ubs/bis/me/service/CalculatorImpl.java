package ubs.bis.me.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ubs.bis.me.domain.Instrument;
import ubs.bis.me.domain.Market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * VWAP Calculator
 */
public class CalculatorImpl implements Calculator {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(CalculatorImpl.class);

    private static final int SCALE = 4;

    private Map<Market, LinkedList<MarketUpdate>> marketUpdateMap = new HashMap<>();

    /**
     * Assumption is that at least 4 decimal places resolution or scale is required.
     *
     * @param price VWAP price
     * @return rounded price
     */
    private static double rounding(double price) {
        BigDecimal bdPrice = new BigDecimal(price);
        bdPrice = bdPrice.setScale(SCALE, RoundingMode.HALF_UP);
        return bdPrice.doubleValue();
    }

    private Predicate<MarketUpdate> isInstrument(Instrument instrument) {
        return p -> p.getTwoWayPrice().getInstrument().equals(instrument);
    }

    /**
     * Get Latest price for market
     * @param market market
     * @return MarketUpdate
     */
    private Optional<MarketUpdate> getLatestTwoWayPrice(Market market, Instrument instrument) {
        Optional<LinkedList<MarketUpdate>> twoWayMarketPriceList = Optional.ofNullable(marketUpdateMap.get(market));
        if (twoWayMarketPriceList.isPresent())
            return Optional.ofNullable(twoWayMarketPriceList.get().stream().filter(isInstrument(instrument))
                    .collect(Collectors.toCollection(LinkedList::new)).descendingIterator().next());
        return Optional.empty();
    }

    /**
     * Add to the main storage latest TwoWayPrice
     *
     * @param twoWayMarketPrice twoWayMarketPrice
     */
    private void addTwoWayPrice(MarketUpdate twoWayMarketPrice) {
        Optional<LinkedList<MarketUpdate>> twoWayMarketPriceList = Optional.ofNullable(marketUpdateMap.get(twoWayMarketPrice.getMarket()));
        if (!twoWayMarketPriceList.isPresent()) {
            twoWayMarketPriceList = Optional.of(new LinkedList<>());
            marketUpdateMap.put(twoWayMarketPrice.getMarket(), twoWayMarketPriceList.get());
        }
        twoWayMarketPriceList.get().add(twoWayMarketPrice);
    }

    /**
     * Assumption is based on that VWAP price is calculated from all the 50 market data
     * And second assumption is made that up to date TwoWayPrice is taken into consideration.
     * @param twoWayMarketPrice twoWayMarketPrice
     * @return TwoWayPrice
     */
    @Override
    public TwoWayPrice applyMarketUpdate(MarketUpdate twoWayMarketPrice) {
        logger.info("MarketUpdate {}", twoWayMarketPrice);
        addTwoWayPrice(twoWayMarketPrice);

        List<MarketUpdate> marketUpdateList = Arrays.stream(Market.values()).
                map(m -> getLatestTwoWayPrice(m, twoWayMarketPrice.getTwoWayPrice().getInstrument()))
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        double bidPrice = marketUpdateList.parallelStream().
                mapToDouble(p -> p.getTwoWayPrice().getBidPrice() * p.getTwoWayPrice().getBidAmount()).sum() /
                marketUpdateList.parallelStream().mapToDouble(p -> p.getTwoWayPrice().getBidAmount()).sum();

        double offerPrice = marketUpdateList.parallelStream().
                mapToDouble(p -> p.getTwoWayPrice().getOfferPrice() * p.getTwoWayPrice().getOfferAmount()).sum() /
                marketUpdateList.parallelStream().mapToDouble(p -> p.getTwoWayPrice().getOfferAmount()).sum();

        logger.info("TwoWayPrice market {} instrument {} bid price {} offer price {} ",
                twoWayMarketPrice.getMarket(),
                twoWayMarketPrice.getTwoWayPrice().getInstrument(),
                bidPrice,
                offerPrice
        );
        return new TwoWayImpl(
                twoWayMarketPrice.getTwoWayPrice().getInstrument(),
                twoWayMarketPrice.getTwoWayPrice().getState(),
                rounding(bidPrice),
                twoWayMarketPrice.getTwoWayPrice().getBidAmount(),
                rounding(offerPrice),
                twoWayMarketPrice.getTwoWayPrice().getOfferAmount());
    }
}
