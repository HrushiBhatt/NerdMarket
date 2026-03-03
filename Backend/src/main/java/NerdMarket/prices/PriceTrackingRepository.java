package NerdMarket.prices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceTrackingRepository extends JpaRepository<PriceTracking, Long> {

    List<PriceTracking> findByCardId(Long cardId);

    List<PriceTracking> findByCardIdOrderByRecordedAtDesc(Long cardId);

    PriceTracking findFirstByCardIdOrderByRecordedAtDesc(Long cardId);

    List<PriceTracking> findByCardIdIn(List<Long> cardIds);

}