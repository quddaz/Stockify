# DB_ERD

## 1. 주요 테이블 구조

| 테이블               | 설명                               |
|-------------------|----------------------------------|
| **user**          | OAuth를 통해 인증된 사용자 계정 정보 (이메일 포함) |
| **company**       | 시뮬레이션 대상 기업, 현재 주가 및 변동성 포함      |
| **trade_history** | 사용자가 구매한 기업의 주식                  |
| **price_history** | 각 기업의 시간대별 주가 변동 로그              |
| **event**         | 시장 사건(뉴스, 정책 등) 정보 및 영향도         |
| **ranking**       | 사용자별 수익률 및 순위 정보                 |


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
    description   TEXT,
    current_price BIGINT       NOT NULL,
    totalshares   BIGINT       NOT NULL,
    created_at    DATETIME     NOT NULL,
    modified_at   DATETIME     NOT NULL
);

CREATE TABLE event
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    type        ENUM('POSITIVE', 'NEGATIVE') NOT NULL,
    impact_rate DOUBLE NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE price_history
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id     BIGINT   NOT NULL,
    event_id       BIGINT NULL,       
    recorded_price BIGINT   NOT NULL, 
    price          BIGINT   NOT NULL, 
    recorded_at    DATETIME NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company (id),
    FOREIGN KEY (event_id) REFERENCES event (id),
    INDEX          idx_recorded_at (recorded_at)
);

CREATE TABLE trade_history
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    company_id     BIGINT NOT NULL,
    share_count   BIGINT NOT NULL,
    price  BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (company_id) REFERENCES company (id)
);

CREATE TABLE ranking
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    profit_rate DOUBLE NOT NULL,
    rank_no INT    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

```