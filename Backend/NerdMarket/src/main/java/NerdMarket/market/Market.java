//This file will map to the Market table in MySQL.
//Will define fields such as Title, Category, Card Value, etc...

package NerdMarket.market;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardType;
    private String cardName;
    private String cardSet;
    private String cardRarity;
    private double price;

}
