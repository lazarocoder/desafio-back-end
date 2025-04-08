-- Alterar o tipo da coluna code para INTEGER
ALTER TABLE products ALTER COLUMN code TYPE INTEGER USING (regexp_replace(code, 'PROD-', '')::INTEGER);

-- Adicionar constraints de validação
ALTER TABLE products
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT chk_product_name_length CHECK (char_length(name) <= 100),
    ADD CONSTRAINT chk_product_description_length CHECK (char_length(description) <= 255),
    ADD CONSTRAINT chk_product_price_positive CHECK (price > 0),
    ALTER COLUMN status SET NOT NULL,
    ALTER COLUMN category_id SET NOT NULL;

-- Adicionar índices
CREATE INDEX idx_products_code ON products(code);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_status ON products(status); 