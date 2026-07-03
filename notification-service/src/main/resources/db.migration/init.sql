CREATE DATABASE notification_service;

USE notification_service;

CREATE TABLE notifications (
           id           VARCHAR(36) PRIMARY KEY,
           user_id      VARCHAR(255) NOT NULL,
           type         VARCHAR(255) NOT NULL,
           channel      ENUM('EMAIL', 'SMS', 'PUSH') NOT NULL,
           title        VARCHAR(255) NOT NULL,
           content      TEXT NOT NULL,
           status       ENUM('PENDING', 'SENT', 'FAILED') DEFAULT 'PENDING',
           reference_id VARCHAR(36)  NULL,     -- orderId, promotionId
           reference_type VARCHAR(50) NULL,    -- 'ORDER', 'PROMOTION'
           retry_count  INT DEFAULT 0,
           is_deleted   BOOLEAN DEFAULT FALSE,
           created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
           created_by   VARCHAR(255),
           updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
           updated_by   VARCHAR(255)
);