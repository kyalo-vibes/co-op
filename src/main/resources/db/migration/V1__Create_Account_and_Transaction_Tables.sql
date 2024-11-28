-- Create the User table
CREATE TABLE "app_user" (
                        id SERIAL PRIMARY KEY,                  -- Unique user identifier
                        username VARCHAR(255) UNIQUE NOT NULL,   -- Username (e.g., for login)
                        password_hash VARCHAR(255) NOT NULL,     -- Hashed password for security
                        email VARCHAR(255) UNIQUE NOT NULL,      -- Email address
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Record creation timestamp
                        modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- Last modification timestamp
);

-- Create the Account table
CREATE TABLE account (
                         id SERIAL PRIMARY KEY,                        -- Unique account identifier
                         user_id INT NOT NULL,                         -- Associated user
                         account_holder VARCHAR(255) NOT NULL,         -- Name of the account holder
                         balance DECIMAL(15, 2) NOT NULL,              -- Current account balance
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Record creation timestamp
                         modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Last modification timestamp
                         FOREIGN KEY (user_id) REFERENCES "app_user" (id) ON DELETE CASCADE  -- Foreign key to User table
);

-- Create the Transaction table
CREATE TABLE transaction (
                             id SERIAL PRIMARY KEY,                        -- Unique transaction identifier
                             account_id INT NOT NULL,                      -- Associated account
                             transaction_type VARCHAR(50) NOT NULL,         -- Transaction type (e.g., DEPOSIT, WITHDRAWAL, TRANSFER)
                             amount DECIMAL(15, 2) NOT NULL,                -- Transaction amount
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Transaction creation timestamp
                             modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Last modification timestamp
                             FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE  -- Foreign key to Account table
);
