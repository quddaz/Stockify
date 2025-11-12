# DB_ERD

## 1. 주요 테이블 구조

| 테이블               | 설명                               |
|-------------------|----------------------------------|
| **user**          | OAuth를 통해 인증된 사용자 계정 정보 (이메일 포함) |
| **company**       | 시뮬레이션 대상 기업, 현재 주가 및 변동성 포함      |
| **trade_history** | 사용자가 구매한 기업의 주식                  |
| **event_history** | 각 기업의 시간대별 주가 변동 로그              |
| **event**         | 시장 사건(뉴스, 정책 등) 정보 및 영향도         |
| **sectorTheme**   | 격동하는 장을 저장합니다.                   |


## 2. 테이블 DDL
    
```sql

CREATE TABLE users
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    social_type VARCHAR(20)  NOT NULL,
    social_id   VARCHAR(255) NOT NULL,
    role        ENUM('Role_USER', 'Role_ADMIN') NOT NULL,
    money       BIGINT       NOT NULL DEFAULT 0
);

CREATE TABLE company
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    sector        VARCHAR(255) NOT NULL,
    description   TEXT,
    current_price BIGINT       NOT NULL,
    totalshares   BIGINT       NOT NULL,
    positive_rate DOUBLE NOT NULL DEFAULT 1.0,
    negative_rate DOUBLE NOT NULL DEFAULT 1.0,
    created_at    DATETIME     NOT NULL,
    modified_at   DATETIME     NOT NULL
);

CREATE TABLE event
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    type        ENUM('POSITIVE', 'NEGATIVE') NOT NULL,
    impact_rate DOUBLE NOT NULL,
    description TEXT   NOT NULL,
    weight      BIGINT NOT NULL,
);

CREATE TABLE event_history
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id     BIGINT   NOT NULL,
    event_id       BIGINT NULL,
    recorded_price BIGINT   NOT NULL,
    changed_price  BIGINT   NOT NULL,
    change_rate    DOUBLE   NOT NULL,
    recorded_at    DATETIME NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company (id),
    FOREIGN KEY (event_id) REFERENCES event (id),
    INDEX idx_recorded_at (recorded_at)
);

CREATE TABLE trade_history
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    company_id  BIGINT NOT NULL,
    share_count BIGINT NOT NULL,
    price       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (company_id) REFERENCES company (id),
    CREATE INDEX idx_trade_history_user_company ON trade_history(user_id, company_id);
);

CREATE TABLE sector_theme
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    sector_name VARCHAR(50) NOT NULL,
    positive_rate DOUBLE NOT NULL,
    negative_rate DOUBLE NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    INDEX idx_updated_at (updated_at DESC)
);

```

## 3. 쿼리 예시

- 특정 사용자의 포트폴리오 조회

계산식 설명:
- 평균 매입가: (Σ 매수 수량 × 단가) ÷ Σ 매수 수량
- 미실현 손익: (현재가 × 총 보유량) - Σ (매수 수량 × 단가)
- 총 보유량: Σ 매수 수량
```sql
SELECT
    c.id AS company_id,
    c.name AS company_name,
    SUM(t.share_count) AS share_count,  -- 총 보유량
    SUM(t.share_count * t.price) / SUM(t.share_count) AS average_price,  -- 평균 매입가
    c.current_price AS current_price,
    (c.current_price * SUM(t.share_count)) - SUM(t.share_count * t.price) AS unrealized_pl  -- 미실현 손익
FROM
    trade_history t
JOIN
    company c ON t.company_id = c.id
WHERE
    t.user_id = :userId
GROUP BY
    c.id, c.name, c.current_price;
```
