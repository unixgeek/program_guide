ALTER TABLE program
ADD COLUMN tvmaze_id int(11) unsigned NULL,
ADD COLUMN network tinytext NULL;

UPDATE program SET tvmaze_id = 166   WHERE id = 13;
UPDATE program SET tvmaze_id = 27100 WHERE id = 75;
UPDATE program SET tvmaze_id = 15299 WHERE id = 86;
UPDATE program SET tvmaze_id = 1825  WHERE id = 88;
UPDATE program SET tvmaze_id = 39956 WHERE id = 89;
UPDATE program SET tvmaze_id = 1804  WHERE id = 90;
UPDATE program SET tvmaze_id = 1871  WHERE id = 91;
UPDATE program SET tvmaze_id = 5079  WHERE id = 92;
UPDATE program SET tvmaze_id = 28276 WHERE id = 93;
UPDATE program SET tvmaze_id = 5     WHERE id = 94;
UPDATE program SET tvmaze_id = 269   WHERE id = 95;
UPDATE program SET tvmaze_id = 565   WHERE id = 96;
UPDATE program SET tvmaze_id = 38963 WHERE id = 97;
UPDATE program SET tvmaze_id = 7480  WHERE id = 98;
UPDATE program SET tvmaze_id = 42193 WHERE id = 99;
UPDATE program SET tvmaze_id = 1371  WHERE id = 100;
UPDATE program SET tvmaze_id = 16544 WHERE id = 101;
UPDATE program SET tvmaze_id = 2174  WHERE id = 102;
UPDATE program SET tvmaze_id = 1369  WHERE id = 103;
UPDATE program SET tvmaze_id = 1370  WHERE id = 104;
UPDATE program SET tvmaze_id = 2175  WHERE id = 105;
UPDATE program SET tvmaze_id = 2176  WHERE id = 106;
UPDATE program SET tvmaze_id = 30960 WHERE id = 107;
UPDATE program SET tvmaze_id = 14055 WHERE id = 108;

UPDATE program set last_update  = NULL;
