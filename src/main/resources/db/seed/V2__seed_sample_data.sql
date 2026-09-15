-- Fictional sample data for local development (PostgreSQL only).
-- 2,000 customers. Customer 1 is a high-volume account with 2,000 orders;
-- every other customer has 50 orders. Each order has 1-4 line items.

INSERT INTO customers (name, email, created_at)
SELECT 'Sample Customer ' || lpad(g::text, 4, '0'),
       'customer' || lpad(g::text, 4, '0') || '@example.com',
       TIMESTAMP WITH TIME ZONE '2023-01-01 00:00:00+00' + (g || ' hours')::interval
FROM generate_series(1, 2000) AS g;

-- High-volume customer
INSERT INTO orders (customer_id, status, created_at)
SELECT 1,
       (ARRAY['PLACED', 'SHIPPED', 'DELIVERED', 'CANCELLED'])[1 + (g % 4)],
       TIMESTAMP WITH TIME ZONE '2024-01-01 00:00:00+00' + (g * 4 || ' hours')::interval
FROM generate_series(1, 2000) AS g;

-- Everyone else
INSERT INTO orders (customer_id, status, created_at)
SELECT c,
       (ARRAY['PLACED', 'SHIPPED', 'DELIVERED', 'CANCELLED'])[1 + ((c + n) % 4)],
       TIMESTAMP WITH TIME ZONE '2024-01-01 00:00:00+00' + (((c * 7 + n * 13) % 15000) || ' hours')::interval
FROM generate_series(2, 2000) AS c,
     generate_series(1, 50) AS n;

INSERT INTO order_line_items (order_id, product_name, quantity, unit_price)
SELECT o.id,
       (ARRAY['Notebook', 'Desk Lamp', 'USB-C Cable', 'Coffee Beans', 'Water Bottle', 'Headphones'])[1 + ((o.id + i) % 6)],
       1 + ((o.id + i) % 3),
       (ARRAY[19.99, 4.10, 0.10, 12.35, 0.20, 59.99])[1 + ((o.id * 3 + i) % 6)]
FROM orders o
CROSS JOIN LATERAL generate_series(1, 1 + (o.id % 4)::int) AS i;

ANALYZE customers;
ANALYZE orders;
ANALYZE order_line_items;
