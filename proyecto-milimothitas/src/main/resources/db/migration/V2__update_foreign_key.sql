-- Eliminar la restricción de clave foránea existente
ALTER TABLE sale_items DROP CONSTRAINT IF EXISTS fk_sale_item_product;

-- Añadir la nueva restricción de clave foránea con ON DELETE SET NULL
ALTER TABLE sale_items ADD CONSTRAINT fk_sale_item_product 
FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL; 