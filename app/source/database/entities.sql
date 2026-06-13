create table actives (
	row  integer primary key autoincrement,
	name text not null unique
);

create table actives (
	row  integer primary key autoincrement,
	name text not null unique
);

create table medications (
	row           integer primary key autoincrement,
	barcode       text    not null unique,
	commercial    text    not null,
	active        integer not null,
	maker         integer not null, -- Laboraty
	concentration text    not null check (json_valid(concentration)), -- C×1000

	constraint concentration_value_must_be_positive check (json_extract(concentration, '$.value') > 0),
	constraint concentration_value_is_required      check (json_extract(concentration, '$.value') is not null),
	constraint concentration_unit_is_required       check (json_extract(concentration, '$.unit')  is not null),
	constraint concentration_unit_must_be_valid     check (json_extract(concentration, '$.unit')  in ('U/mL'))
) strict;

create table injections (
	row       integer primary key autoincrement,
	insulin   integer not null,
	dose      integer not null, -- U×1000
	timestamp text    not null default current_timestamp,

	foreign key (insulin) references medications (row) on delete cascade on update cascade
);
