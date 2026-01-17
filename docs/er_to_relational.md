## Relational schema

### `currency`
currency (**code**, name)

---

### `counterpart`

counterpart (**counterpart_id**, name, phone_number, address_short, city, postal_code, country, contant_email, category, is_active, created_at, updated_at)

---

### `user`

user (**user_id**, counterpart_id, first_name, last_name, email, password_hash, role, is_active, created_at, updated_at) <br>
    `counterpart_id` references `counterpart.counterpart_id` NOT NULL

---

### `item`

item (**lot_id**, stock_name, purchase_date, supplier_id, origin, responsible_office_id,  is_available, created_at, updated_at) <br>
    `supplier_id` references `counterpart.counterpart_id` NOT NULL <br>
    `responsible_office_id` references `counterpart.counterpart_id` NOT NULL

white_diamond (**lot_id**, weight_ct, shape, length, width, depth, white_level, clarity) <br>
    `lot_id` references `loose_stone.lot_id`

colored_diamond (**lot_id**, weight_ct, shape, length, width, depth, gem_type, fancy_intensity, fancy_overton, fancy_color, clarity) <br>
    `lot_id` references `loose_stone.lot_id`

colored_gem_stone (**lot_id**, weight_ct, shape, length, width, depth, gem_type, gem_color, treatment) <br>
    `lot_id` references `loose_stone.lot_id`

---

### `action`

action (**action_id**, from_counterpart_id, to_counterpart_id, terms, remarks, created_at, updated_at, category, transfer_num, transfer_date, user_id, lot_id, price, currency_code) <br>
    `from_counterpart_id` references `counterpart.counterpart_id` NOT NULL <br>
    `to_counterpart_id` references `counterpart.counterpart_id` NOT NULL <br>
    `user_id` references `user.user_id` NOT NULL <br>
    `lot_id` references `item.lot_id` NOT NULL <br>
    `currency_code` references `currency.code` NOT NULL

