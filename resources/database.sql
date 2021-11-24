DROP TABLE IF EXISTS users;
CREATE TABLE users (id integer primary key,
                    username text not null,
                    password text not null,
                    first_name text not null,
                    last_name text not null,
                    street text not null,
                    city text not null,
                    state text not null,
                    zip text not null,
                    op text,
                    ico text,
                    is_bidder integer not null,
                    is_organizer integer not null,
                    is_operator integer not null,
                    created_at text not null,
                    updated_at text not null,
                    deleted_at text,
                    check ( (op is null and ico is not null) or (op is not null and ico is null) ),
                    check ( (is_bidder == 1 and is_organizer == 0 and is_operator == 0) or
                            (is_bidder == 0 and is_organizer == 1 and is_operator == 0) or
                            (is_bidder == 0 and is_organizer == 0 and is_operator == 1) ));

DROP TABLE IF EXISTS charities;
CREATE TABLE charities (id integer primary key,
                        name text not null,
                        description text not null,
                        bank_code text not null,
                        bank_number text not null,
                        created_at text not null,
                        updated_at text not null,
                        deleted_at text);

DROP TABLE IF EXISTS auctions;
CREATE TABLE auctions (id integer primary key,
                       organizer_id integer not null,
                       charity_id integer not null,
                       name text not null,
                       description text not null,
                       auction_start text not null,
                       auction_end text not null,
                       created_at text not null,
                       updated_at text not null,
                       deleted_at text,
                       foreign key (organizer_id) references users (id),
                       foreign key (charity_id) references charities (id));

DROP TABLE IF EXISTS biddings;
CREATE TABLE biddings (id integer primary key,
                       auction_id integer not null,
                       name text not null,
                       description text not null,
                       starting_bid real not null,
                       highest_bid real,
                       highest_bidder_id integer,
                       bidding_start text not null,
                       bidding_end text not null,
                       created_at text not null,
                       updated_at text not null,
                       deleted_at text,
                       foreign key (auction_id) references auctions (id),
                       foreign key (highest_bidder_id) references users (id));

DROP TABLE IF EXISTS categories;
CREATE TABLE categories (id integer primary key,
                         name text not null,
                         description text not null,
                         requires_operator integer not null,
                         requires_organizer integer not null,
                         created_at text not null,
                         updated_at text not null,
                         deleted_at text,
                         check ( (requires_operator == 1 and requires_organizer == 0) or
                                 (requires_operator == 0 and requires_organizer == 1) ));

DROP TABLE IF EXISTS reports;
CREATE TABLE reports (id integer primary key,
                      issuer_id integer not null,
                      title text not null,
                      category_id integer not null,
                      description text not null,
                      user_id integer not null,
                      auction_id integer not null,
                      bidding_id integer not null,
                      resolver_id integer not null,
                      created_at text not null,
                      updated_at text not null,
                      resolved_at text,
                      deleted_at text,
                      foreign key (issuer_id) references users (id),
                      foreign key (user_id) references users (id),
                      foreign key (resolver_id) references users (id),
                      foreign key (auction_id) references auctions (id),
                      foreign key (bidding_id) references biddings (id));

DROP TABLE IF EXISTS bookmarks;
CREATE TABLE bookmarks (id integer primary key,
                        user_id integer not null,
                        auction_id text not null,
                        created_at text not null,
                        updated_at text not null,
                        deleted_at text,
                        foreign key (user_id) references users (id),
                        foreign key (auction_id) references auctions (id));

DROP TABLE IF EXISTS services;
CREATE TABLE services (id integer primary key,
                       name text not null,
                       description text not null,
                       website text not null,
                       created_at text not null,
                       updated_at text not null,
                       deleted_at text);


INSERT INTO users (username, password, first_name, last_name, street, city, state, zip, op,
                   is_bidder, is_organizer, is_operator, created_at, updated_at)
VALUES ('admin', 'ADMINPASS', 'Admin', 'OfThisProject', 'Iľkovičova 2', 'Bratislava', 'Slovensko',
        '84216', 'EN123456', 0, 0, 1, '2021-11-01 08:24:11', '2021-11-01 08:24:11'),
       ('mhrnciar', 'password', 'Matej', 'Hrnciar', 'Iľkovičova 2', 'Bratislava', 'Slovensko',
        '84216', 'EN654321', 0, 1, 0, '2021-11-02 00:00:00', '2021-11-02 00:00:00');

INSERT INTO users (username, password, first_name, last_name, street, city, state, zip, ico,
                   is_bidder, is_organizer, is_operator, created_at, updated_at)
VALUES ('companyaccount', 'company123', 'TheName', 'OfCompany', 'Lichnerova 4', 'Senec', 'Slovensko',
        '90301', '000111', 1, 0, 0, '2021-11-06 22:07:06', '2021-11-06 22:07:06'),
       ('organizercomp', 'very_strong_password', 'Organizer', 'Company LTD.', 'Komárnická 112/4',
        'Bratislava', 'Slovensko', '82103', '000111', 0, 1, 0, '2021-11-06 12:00:45', '2021-11-06 12:00:45');

INSERT INTO charities (name, description, bank_code, bank_number, created_at, updated_at)
VALUES ('First Charity', 'This is a first created charity.', '0800', '0000005012345678', '2021-11-02 00:00:00', '2021-11-02 00:00:00'),
       ('Save turtles', 'Lower plastic consumption!', '0800', '0000001010100101', '2021-11-02 00:00:00', '2021-11-02 00:00:00');

INSERT INTO auctions (organizer_id, charity_id, name, description, auction_start, auction_end, created_at, updated_at)
VALUES (2, 1, 'First Auction', 'Please support this auction', '2021-11-05 00:00:00', '2021-11-31 00:00:00', '2021-11-02 11:11:11', '2021-11-02 11:11:11'),
       (4, 2, 'Auction to save turtles', 'We dont need plastic as much as we need turtles', '2021-11-10 00:00:00', '2021-11-17 00:00:00', '2021-11-07 00:12:56', '2021-11-07 00:12:56');

INSERT INTO biddings (auction_id, name, description, starting_bid, highest_bid, highest_bidder_id,
                      bidding_start, bidding_end, created_at, updated_at)
VALUES (1, 'Water', 'Its just water...', 1.50, 4.50, 3, '2021-11-05 00:00:00', '2021-11-07 00:00:00', '2021-11-02 11:12:34', '2021-11-07 11:12:34'),
       (1, 'Signed guitar', 'Used by Jimi Hendrix', 1000.00, 1250.00, 3, '2021-11-07 00:00:00', '2021-11-09 00:00:00', '2021-11-02 11:12:55', '2021-11-07 04:08:54'),
       (2, 'Haunted cupboard', 'Ghosts live here', 26.35, 32.60, 3, '2021-11-10 00:00:00', '2021-11-12 00:00:00', '2021-11-07 01:54:22', '2021-11-10 04:26:21');

INSERT INTO biddings (auction_id, name, description, starting_bid, bidding_start, bidding_end, created_at, updated_at)
VALUES (1, 'Painting', 'Modern art', 9999.99, '2021-11-09 00:00:00', '2021-11-31 00:00:00', '2021-11-02 11:14:26', '2021-11-02 11:14:26'),
       (2, 'Water', 'Its just water...', 1.50, '2021-11-12 00:00:00', '2021-11-17 00:00:00', '2021-11-07 01:55:00', '2021-11-09 01:55:00');

INSERT INTO categories (name, description, requires_operator, requires_organizer, created_at, updated_at)
VALUES ('Organizer is scammer', 'Organizer is known to have created auctions for his own gain.', 1, 0, '2021-11-01 00:00:00', '2021-11-01 00:00:00'),
       ('Bidding has incorrect description', 'Bidding has misleading or wrong description', 0, 1, '2021-11-01 00:00:00', '2021-11-01 00:00:00'),
       ('User is sharing inappropriate content', 'User makes other people uncomfortable', 1, 0, '2021-11-01 00:00:00', '2021-11-01 00:00:00');

