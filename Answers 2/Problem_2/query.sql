SELECT pid, 
       SUM(qty) 
FROM   ( 
       --Get current active items   
       SELECT p.product_id pid, 
              oi.quantity  qty 
       FROM   product p, 
              order_header oh, 
              order_item oi 
       WHERE  p.product_id = oi.product_id 
              AND oi.order_id = oh.order_id 
              AND Trunc(SYSDATE - oh.order_date) <= 365 
              AND p.discontinue_date IS NULL 
              AND p.replacement_product_id IS NULL 
        UNION ALL 
        --Get old items of current active items  
        SELECT p2.product_id pid, 
               oi.quantity   qty 
        FROM   product p, 
               product p2, 
               order_header oh, 
               order_item oi 
        WHERE  p.replacement_product_id = p2.product_id 
               AND p.product_id = oi.product_id 
               AND oi.order_id = oh.order_id 
               AND Trunc(SYSDATE - oh.order_date) <= 365 
               AND p2.discontinue_date IS NULL) 
GROUP  BY pid 
HAVING SUM(qty) >= 1;