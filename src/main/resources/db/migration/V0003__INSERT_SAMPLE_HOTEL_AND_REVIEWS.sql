INSERT INTO hotels(id, name, description, location, total_price, image_url)
VALUES ('26b84be7-ca2f-4330-a08a-2de099f98f53',
        'Mars Hotel',
        'This is an eloquent description from this sample hotel',
        'Mars',
        100000,
        'hotel-from-mars.com');

INSERT INTO reviews(rating, comment, hotel_id)
VALUES (3, 'It was not the best Mars hotel but it was nice...', '26b84be7-ca2f-4330-a08a-2de099f98f53');