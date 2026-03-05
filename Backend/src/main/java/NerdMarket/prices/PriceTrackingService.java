package NerdMarket.prices;

import NerdMarket.market.Market;
import NerdMarket.market.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class PriceTrackingService {

    @Autowired
    private PriceTrackingRepository priceTrackingRepository;

    @Autowired
    private MarketRepository marketRepository;

    public String populatePriceData() {
        List<Market> topCards = marketRepository.findTop100ByOrderByPriceDesc();

        if (topCards.isEmpty()) {
            return "No cards found";
        }

        int count = 0;
        for (Market card : topCards) {
            double currentPrice = card.getPrice();

            //Price tracking for last week (7 days ago)
            PriceTracking record1 = new PriceTracking();
            record1.setCard(card);
            record1.setPrice(priceChange(currentPrice, 0.05, 0.15));
            record1.setRecordedAt(LocalDateTime.now().minusDays(7));
            priceTrackingRepository.save(record1);
            count++;

            //Price tracking for 3 days ago
            PriceTracking record2 = new PriceTracking();
            record2.setCard(card);
            record2.setPrice(priceChange(currentPrice, 0.02, 0.08));
            record2.setRecordedAt(LocalDateTime.now().minusDays(3));
            priceTrackingRepository.save(record2);
            count++;

            //Price tracking for current price today
            PriceTracking record3 = new PriceTracking();
            record3.setCard(card);
            record3.setPrice(currentPrice);
            record3.setRecordedAt(LocalDateTime.now());
            priceTrackingRepository.save(record3);
            count++;
        }
        return "Added " + count + " price records for " + topCards.size() + " cards";
    }

    private double priceChange(double price, double minChange, double maxChange) {
        double changePercent = minChange + (Math.random() * (maxChange - minChange));
        boolean increase = Math.random() > 0.5;
        if (increase) {
            return Math.round((price * (1 + changePercent)) * 100.0) / 100.0;
        } else {
            return Math.round((price * (1 - changePercent)) * 100.0) / 100.0;
        }
    }

    public List<Map<String, Object>> getBiggestMovers() {
      List<PriceTracking> allRecords = priceTrackingRepository.findAll();

      //Group by cardId
      Map<Long, List<PriceTracking>> cardPrices = new HashMap<>();
      for (PriceTracking record : allRecords) {
          Long cardId = record.getCardId();
          if (!cardPrices.containsKey(cardId)) {
              cardPrices.put(cardId, new ArrayList<>());
          }
          cardPrices.get(cardId).add(record);
      }

      //This calculates price change for each card
      List<Map<String, Object>> movers = new ArrayList<>();
      for (Long cardId : cardPrices.keySet()) {
          List<PriceTracking> prices = cardPrices.get(cardId);
          if (prices.size() < 2) {
              continue;
          }

          //Sorts by date to get oldest and newest
          prices.sort(Comparator.comparing(PriceTracking::getRecordedAt));
          double oldPrice = prices.get(0).getPrice();
          double newPrice = prices.get(prices.size() - 1).getPrice();

          if (oldPrice == 0) {
              continue;
          }

          double changePercent = ((newPrice - oldPrice) / oldPrice) * 100;
          changePercent = Math.round(changePercent * 100.0) / 100.0;

          Map<String, Object> mover = new HashMap<>();
          mover.put("cardId", cardId);
          mover.put("oldPrice", oldPrice);
          mover.put("newPrice", newPrice);
          mover.put("changePercent", changePercent);
          movers.add(mover);
      }

      //Sort by change percentage to get the top 10 biggest changed cards
      movers.sort((a, b) -> Double.compare(Math.abs((Double) b.get("changePercent")), Math.abs((Double) a.get("changePercent"))));
      if (movers.size() > 10) {
          return movers.subList(0, 10);
      }
      return movers;
    }
}