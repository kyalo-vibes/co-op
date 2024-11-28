-- Insert sample users into the "user" table
INSERT INTO "app_user" (id, username, password_hash, email, created_at, modified_at)
VALUES
    (1, 'john_doe', '$2a$10$uCOm1uEdO2x8d1Iv2NMEeO68LbuNh3pRW44TrdHw2mAqmY8Y6Dwz6', 'john.doe@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'jane_smith', '$2a$10$uCOm1uEdO2x8d1Iv2NMEeO68LbuNh3pRW44TrdHw2mAqmY8Y6Dwz6', 'jane.smith@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'alice_johnson', '$2a$10$uCOm1uEdO2x8d1Iv2NMEeO68LbuNh3pRW44TrdHw2mAqmY8Y6Dwz6', 'alice.johnson@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample accounts (ensure user_ids match existing users)
-- After inserting users, their ids will be auto-generated starting from 1.
INSERT INTO account (id, user_id, account_holder, balance, created_at, modified_at)
VALUES
    (1,1, 'John Doe', 1000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),   -- user_id = 1
    (2,2, 'Jane Smith', 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- user_id = 2
    (3,3, 'Alice Johnson', 500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- user_id = 3

-- Insert sample transactions (ensure account_ids match existing accounts)
-- After inserting accounts, their ids will be auto-generated starting from 1.
INSERT INTO transaction (account_id, transaction_type, amount, created_at, modified_at)
VALUES
    (1, 'DEPOSIT', 1000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- account_id = 1
    (2, 'DEPOSIT', 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- account_id = 2
    (3, 'DEPOSIT', 500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);   -- account_id = 3
