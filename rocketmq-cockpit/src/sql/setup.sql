USE cockpit;

INSERT INTO status_lu(id, name) VALUES (1, "DRAFT");
INSERT INTO status_lu(id, name) VALUES (2, "ACTIVE");
INSERT INTO status_lu(id, name) VALUES (3, "DELETED");

INSERT INTO cockpit_role(id, name) VALUES(1, 'ADMIN');
INSERT INTO cockpit_role(id, name) VALUES(2, 'MANAGER');
INSERT INTO cockpit_role(id, name) VALUES(3, 'USER');
INSERT INTO cockpit_role(id, name) VALUES(4, 'WATCHER');