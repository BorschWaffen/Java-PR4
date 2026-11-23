CREATE TABLE IF NOT EXISTS books (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     type TEXT NOT NULL,
                                     title TEXT NOT NULL,
                                     author TEXT NOT NULL,
                                     publication_year INTEGER NOT NULL,
                                     pages INTEGER NOT NULL,
                                     genre TEXT NOT NULL,
                                     file_size_mb REAL,
                                     file_format TEXT,
                                     weight_grams INTEGER,
                                     cover_type TEXT,
                                     duration_minutes INTEGER,
                                     narrator TEXT,
                                     subject_area TEXT,
                                     has_index INTEGER
);