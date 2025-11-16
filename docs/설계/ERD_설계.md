# DB_ERD

## 1. 주요 테이블 구조

| 테이블               | 설명                               |
|-------------------|----------------------------------|
| **user**          | OAuth를 통해 인증된 사용자 계정 정보 (이메일 포함) |
| **company**       | 시뮬레이션 대상 기업, 현재 주가 및 변동성 포함      |
| **trade** | 사용자의 주식 거래 기록                    |
| **user_position** | 사용자가 구매한 기업 주식을 저장               |
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
    change_rate DOUBLE NOT NULL,
    recorded_at    DATETIME NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company (id),
    FOREIGN KEY (event_id) REFERENCES event (id),
    INDEX          idx_recorded_at (recorded_at)
);

CREATE TABLE trade
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    company_id  BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    price       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (company_id) REFERENCES company (id),
    CREATE      INDEX idx_trade_history_user_company ON trade_history(user_id, company_id);
);

CREATE TABLE user_position
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    company_id    BIGINT NOT NULL,
    quantity      BIGINT NOT NULL,
    average_price BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (company_id) REFERENCES company (id),
    UNIQUE (user_id, company_id)
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
    p.quantity AS share_count,                       -- 총 보유량
    p.average_price AS average_price,                -- 평균 매입가
    c.current_price AS current_price,                -- 현재가
    (c.current_price * p.quantity) - (p.average_price * p.quantity) AS unrealized_pl  -- 미실현 손익
FROM
    user_position p
        JOIN
    company c ON p.company_id = c.id
WHERE
    p.user_id = :userId;
```

- 장 마감 시 랭킹 조회
- 계산식 설명:
- 총 자산 = 보유 현금 + Σ (보유 주식 수량 × 현재 주가)
- 수익률 = (총 자산 - 초기 자본금) ÷ 초기 자본금
- 초기 자본금은 10,000,000원으로 가정
```sql
SELECT
    u.name AS username,
    u.money + COALESCE(SUM(p.quantity * c.current_price), 0) AS total_assets,
    (u.money + COALESCE(SUM(p.quantity * c.current_price), 0) - 10000000) / 10000000.0 AS profit_rate
FROM
    users u
        LEFT JOIN
    user_position p ON u.id = p.user_id
        LEFT JOIN
    company c ON p.company_id = c.id
GROUP BY
    u.id, u.name, u.money
ORDER BY
    total_assets DESC
    LIMIT 10;
```
