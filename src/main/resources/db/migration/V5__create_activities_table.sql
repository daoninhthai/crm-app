CREATE TABLE activities (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    description TEXT,
    contact_id BIGINT REFERENCES contacts(id) ON DELETE CASCADE,
    deal_id BIGINT REFERENCES deals(id) ON DELETE CASCADE,
    due_date TIMESTAMP,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_activities_contact ON activities(contact_id);
CREATE INDEX idx_activities_deal ON activities(deal_id);
CREATE INDEX idx_activities_type ON activities(type);
CREATE INDEX idx_activities_due_date ON activities(due_date);
CREATE INDEX idx_activities_completed ON activities(completed);
