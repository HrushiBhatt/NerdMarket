package NerdMarket.prices;

import NerdMarket.market.Market;
import NerdMarket.market.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

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
}