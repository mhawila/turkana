-- Create registrations.
INSERT INTO registration(id, date_registered, date_retired, device_id, phone_number, public_key, retired)
VALUES
  (1, '2019-05-17T14:34:06.686', null, '1000-1', '333', 'public-key-1', false),
  (2, '2019-05-17T14:40:00.686', null, '1000-2', '333', 'public-key-2', false),
  (3, '2019-05-18T10:30:00.686', null, '1000-3', '353', 'public-key-3', false);
