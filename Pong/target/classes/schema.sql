-- schema.sql
CREATE TABLE LastGameScore (
                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                               playerScore INTEGER NOT NULL,
                               aiScore INTEGER NOT NULL
);