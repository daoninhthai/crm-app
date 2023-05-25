CREATE TABLE deals (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    value NUMERIC(15, 2),
    stage VARCHAR(50) NOT NULL DEFAULT 'LEAD',
    company_id BIGINT REFERENCES companies(id) ON DELETE SET NULL,
    contact_id BIGINT REFERENCES contacts(id) ON DELETE SET NULL,
    expected_close_date DATE,
    probability NUMERIC(5, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_deals_stage ON deals(stage);
CREATE INDEX idx_deals_company ON deals(company_id);
CREATE INDEX idx_deals_contact ON deals(contact_id);
CREATE INDEX idx_deals_expected_close ON deals(expected_close_date);
