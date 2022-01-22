insert into addresses (id, areaId, value)
values (1, 2, '76209 Blue Bill Park Center');
insert into addresses (id, areaId, value)
values (2, 2, '113 Lakewood Road');
insert into addresses (id, areaId, value)
values (3, 1, '164 Bunker Hill Hill');
insert into addresses (id, areaId, value)
values (4, 2, '36 Rutledge Circle');
insert into addresses (id, areaId, value)
values (5, 3, '15895 Summit Center');

insert into area (id, addressesId)
values (1, 2);
insert into area (id, addressesId)
values (2, 2);
insert into area (id, addressesId)
values (3, 1);
insert into area (id, addressesId)
values (4, 2);
insert into area (id, addressesId)
values (5, 3);

insert into education (id, number, addressId)
values (1, 110, 2);

insert into education (id, number, addressId)
values (2, 9, 3);

insert into education (id, number, addressId)
values (3, 130, 1);