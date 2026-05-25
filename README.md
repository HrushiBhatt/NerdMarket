# NerdMarket

A stock market–style trading card marketplace for Pokémon, Magic: The Gathering, and Yu-Gi-Oh! cards. Think "Robinhood for trading cards" — users track real-time card prices, monitor biggest movers, manage a personal binder, and chat with other collectors in card-specific rooms.

Built as a team project for COM S 309 (Software Development Practices) at Iowa State University.

---

## Application Demo

▶️ **[Watch the demo on YouTube](https://youtu.be/El1KD3GnCjg)**

---

## My Role

I was the lead backend developer on a 4-person team (2 backend, 2 Android). My contributions in this repo include:

- **External API aggregation pipeline** — fetches and normalizes card data from three different sources (TCGdex for Pokémon, Scryfall for MTG, YGOPRODeck for Yu-Gi-Oh!) into a unified `Market` schema, with pagination, rate limiting, and resilient per-card / per-set error handling.
- **Price tracking & analytics engine** — `PriceTracking` module computes biggest gainers/losers across configurable time windows (2-day, 7-day, 21-day, all-time), per card type or aggregated. Powers the "biggest movers" feature.
- **Real-time chat system** — WebSocket-based chat using Spring's STOMP messaging broker, with role-based room access (card-type rooms gated by binder ownership; moderator/admin-only rooms).
- **Notifications system** — STOMP WebSocket push notifications with both immediate broadcast and scheduled delivery. Includes a daily cron job that detects >1% price swings on cards in a user's binder and pushes targeted notifications.
- **CI/CD pipeline** — GitLab CI with a self-registered shell-executor runner running `mavenbuild` and `maventest` stages on every push.

Notable engineering decisions documented in the code:
- Conditional `setPrice()` calls to prevent overwriting valid DB prices with `0.0` when an external API returns no pricing
- `Map<String, Object>` request bodies on PUT endpoints to support partial updates without nullifying JPA relationships
- `@JsonIgnore` + read-only `@Column` pattern to expose foreign-key IDs in API responses without breaking Hibernate relationship management
- Configured `RestTemplate` connect/read timeouts to prevent indefinite hangs on external APIs

---

## The Team

- **Hrushi Bhatt** — SpringBoot Backend
- **Cooper Hoy** — SpringBoot Backend
- **Rudra Naik** — Java Frontend
- **Logan Heileman** — Java Frontend

---

## Tech Stack

**Backend:** Java 21, Spring Boot, Spring Data JPA, Spring WebSocket (STOMP), Hibernate, MySQL, Maven, Lombok, Swagger/OpenAPI
**Frontend:** Android Studio (Java), Gradle
**DevOps:** GitLab CI/CD with shell-executor runners
**External APIs:** TCGdex, Scryfall, YGOPRODeck

---