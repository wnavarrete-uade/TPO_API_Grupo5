-- FitMarket - Schema y datos semilla
-- Grupo 5 - UADE - API 1C 2026

USE fitmarket;

-- Tablas

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('BUYER', 'SELLER') NOT NULL DEFAULT 'BUYER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(5, 2) DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    category_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS favorites (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Datos semilla

-- Passwords hasheadas con BCrypt (password original: 123456)
INSERT INTO users (username, email, password, first_name, last_name, role) VALUES
('vendedor1', 'vendedor@fitmarket.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Carlos', 'Garcia', 'SELLER'),
('comprador1', 'comprador@fitmarket.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Maria', 'Lopez', 'BUYER');

INSERT INTO categories (name, description) VALUES
('Proteinas', 'Whey, caseina, vegana y mas'),
('Creatina y Aminoacidos', 'Creatina monohidrato, BCAA, glutamina'),
('Pre-entrenos', 'Suplementos pre-workout y energizantes'),
('Vitaminas', 'Multivitaminicos y minerales'),
('Accesorios', 'Guantes, fajas, straps, munequeras'),
('Indumentaria', 'Ropa deportiva para entrenamiento');

INSERT INTO products (name, description, price, discount, stock, image_url, category_id, seller_id) VALUES
('Whey Protein Gold Standard', 'Proteina whey isolate 2lb - Sabor chocolate', 45000.00, 0, 50, 'https://images.unsplash.com/photo-1693711942336-f4f9963bd364?w=600&h=600&fit=crop', 1, 1),
('Creatina Monohidrato 300g', 'Creatina micronizada pura sin sabor', 22000.00, 10, 80, 'https://images.unsplash.com/photo-1709563728498-3fe4764aa06c?w=600&h=600&fit=crop', 2, 1),
('Pre-Workout C4 Original', 'Pre-entreno con beta-alanina y cafeina - Sabor frutal', 38000.00, 0, 30, 'https://images.unsplash.com/photo-1694482329224-e45f0ce6e700?w=600&h=600&fit=crop', 3, 1),
('BCAA 2:1:1 en polvo', 'Aminoacidos ramificados 400g - Sabor uva', 18000.00, 0, 60, 'https://images.unsplash.com/photo-1593095948071-474c5cc2c614?w=600&h=600&fit=crop', 2, 1),
('Multivitaminico Daily Formula', 'Complejo vitaminico y mineral - 100 tabletas', 15000.00, 5, 100, 'https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=600&h=600&fit=crop', 4, 1),
('Guantes de Gimnasio Pro', 'Guantes con munequera ajustable - Talle M', 12000.00, 0, 40, 'https://images.unsplash.com/photo-1583454110551-21f2fa2afe61?w=600&h=600&fit=crop', 5, 1),
('Faja Lumbar Neoprene', 'Faja de soporte lumbar para levantamiento', 16000.00, 15, 25, 'https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=600&h=600&fit=crop', 5, 1),
('Remera Dry-Fit Entrenamiento', 'Remera transpirable secado rapido - Talle L', 9500.00, 0, 70, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600&h=600&fit=crop', 6, 1),
('Proteina Vegana Organica', 'Proteina plant-based 1kg - Sabor vainilla', 42000.00, 0, 20, 'https://images.unsplash.com/photo-1622484212850-eb596d769edc?w=600&h=600&fit=crop', 1, 1),
('Straps de Levantamiento', 'Straps de algodon reforzado para peso muerto', 7500.00, 0, 55, 'https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=600&h=600&fit=crop', 5, 1);
