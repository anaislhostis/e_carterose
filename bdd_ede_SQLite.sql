--
-- Fichier g�n�r� par SQLiteStudio v3.4.4 sur mer. f�vr. 21 09:40:28 2024
--
-- Encodage texte utilis� : System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Tableau : animal
DROP TABLE IF EXISTS animal;
CREATE TABLE IF NOT EXISTS `animal` (
  `num_nat` TEXT NOT NULL,
  `num_tra` TEXT NOT NULL,
  `cod_pays` TEXT NOT NULL,
  `nom` TEXT DEFAULT NULL,
  `sexe` TEXT NOT NULL,
  `date_naiss` TEXT NOT NULL,
  `cod_pays_naiss` TEXT NOT NULL,
  `num_exp_naiss` TEXT NOT NULL,
  `cod_pays_pere` TEXT DEFAULT NULL,
  `num_nat_pere` TEXT DEFAULT NULL,
  `cod_race_pere` TEXT DEFAULT NULL,
  `cod_pays_mere` TEXT NOT NULL,
  `num_nat_mere` TEXT NOT NULL,
  `cod_race_mere` TEXT NOT NULL,
  `cod_race` TEXT DEFAULT NULL,
  PRIMARY KEY (`num_nat`),
  FOREIGN KEY (`cod_race`) REFERENCES `race` (`cod_race`)
);
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022308', '2308', 'FR', 'UBER', '1', '2023-01-03 00:00:00', 'FR', '99275011', 'FR', '9924595810', '38', 'FR', '9904022181', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022309', '2309', 'FR', 'UGGLY', '2', '2023-01-12 00:00:00', 'FR', '99275011', 'FR', '9924595810', '38', 'FR', '9904022179', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022310', '2310', 'FR', 'ULLA', '2', '2023-01-13 00:00:00', 'FR', '99275011', 'FR', '9902666575', '38', 'FR', '9904022229', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022311', '2311', 'FR', 'ULANA', '2', '2023-01-21 00:00:00', 'FR', '99275011', 'FR', '9911118841', '38', 'FR', '9904022207', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022312', '2312', 'FR', 'UDADA', '2', '2023-01-22 00:00:00', 'FR', '99275011', 'FR', '9919103201', '38', 'FR', '9904022230', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022313', '2313', 'FR', 'UNEDEPLUS', '2', '2023-01-22 00:00:00', 'FR', '99275011', 'FR', '9919103201', '38', 'FR', '9904022184', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904022399', '2399', 'FR', 'UDERZO', '1', '2023-01-28 00:00:00', 'FR', '99275011', 'FR', '9919103201', '38', 'FR', '9904022195', '38', '38');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904095790', '5790', 'FR', NULL, '1', '2023-01-08 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095541', '56', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904095791', '5791', 'FR', NULL, '2', '2023-01-08 00:00:00', 'FR', '99124091', NULL, NULL, '66', 'FR', '9904095569', '39', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904095792', '5792', 'FR', NULL, '2', '2023-01-16 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095447', '39', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904095793', '5793', 'FR', NULL, '2', '2023-01-16 00:00:00', 'FR', '99124091', NULL, NULL, '15', 'FR', '9904095595', '15', '15');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904095794', '5794', 'FR', NULL, '2', '2023-01-15 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095261', '39', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904095795', '5795', 'FR', NULL, '1', '2023-01-22 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095479', '39', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904122722', '2722', 'FR', 'UNIQUE', '2', '2023-01-10 00:00:00', 'FR', '99190016', 'FR', '9937800349', '79', 'FR', '9932742433', '79', '79');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904122723', '2723', 'FR', NULL, '1', '2023-01-11 00:00:00', 'FR', '99190016', 'FR', '9943396600', '79', 'FR', '9904122677', '79', '79');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904158437', '8437', 'FR', 'UNO', '1', '2023-01-02 00:00:00', 'FR', '99268099', 'FR', '9915020669', '79', 'FR', '9964474727', '79', '79');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904169631', '9631', 'FR', 'MARCUS', '1', '2023-01-04 00:00:00', 'FR', '99292036', 'FR', '9925603382', '41', 'FR', '9938767786', '41', '41');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904175249', '5249', 'FR', 'ULYSSE', '1', '2023-01-11 00:00:00', 'FR', '99296082', 'FR', '9904175212', '34', 'FR', '9904175172', '34', '34');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904190105', '2105', 'FR', NULL, '2', '2023-01-08 00:00:00', 'FR', '99342070', 'FR', '9944535211', '38', 'FR', '9920590523', '39', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904302175', '2175', 'FR', NULL, '1', '2023-01-09 00:00:00', 'FR', '99370171', 'FR', '9974973537', '66', 'FR', '9904301760', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904302176', '2176', 'FR', NULL, '1', '2023-01-17 00:00:00', 'FR', '99370171', 'FR', '9947295105', '56', 'FR', '9904301847', '56', '56');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904302177', '2177', 'FR', 'ULISSE', '2', '2023-01-19 00:00:00', 'FR', '99370171', 'FR', '9970822884', '56', 'FR', '9904301741', '56', '56');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904302178', '2178', 'FR', NULL, '1', '2023-01-22 00:00:00', 'FR', '99370171', 'FR', '9960076998', '34', 'FR', '9904301821', '56', '39');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904302179', '2179', 'FR', NULL, '1', '2023-01-27 00:00:00', 'FR', '99370171', 'FR', '9922361136', '56', 'FR', '9904301641', '56', '56');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904304616', '4616', 'FR', NULL, '2', '2023-01-22 00:00:00', 'FR', '99374024', 'FR', '9934109355', '34', 'FR', '9904304598', '34', '34');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904304617', '4617', 'FR', NULL, '1', '2023-01-22 00:00:00', 'FR', '99374024', 'FR', '9915353586', '34', 'FR', '9904304603', '34', '34');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904304618', '4618', 'FR', NULL, '1', '2023-01-27 00:00:00', 'FR', '99374024', NULL, NULL, '34', 'FR', '9904304600', '34', '34');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904328944', '8944', 'FR', NULL, '1', '2023-01-11 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328625', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904328945', '8945', 'FR', NULL, '1', '2023-01-16 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328631', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904328946', '8946', 'FR', NULL, '1', '2023-01-05 00:00:00', 'FR', '99672102', 'FR', '9904328364', '66', 'FR', '9904328618', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329053', '9053', 'FR', 'ULTIMA', '2', '2023-01-04 00:00:00', 'FR', '99672102', 'FR', '9948001750', '66', 'FR', '9904328446', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329054', '9054', 'FR', 'UNICOPA', '2', '2023-01-02 00:00:00', 'FR', '99672102', 'FR', '9904328364', '66', 'FR', '9904328619', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329055', '9055', 'FR', 'UBUESQUE', '2', '2023-01-04 00:00:00', 'FR', '99672102', 'FR', '9928823654', '66', 'FR', '9904328340', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329056', '9056', 'FR', 'UEFA', '2', '2023-01-05 00:00:00', 'FR', '99672102', 'FR', '9904328364', '66', 'FR', '9904328628', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329057', '9057', 'FR', 'UHU', '2', '2023-01-11 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328532', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329058', '9058', 'FR', 'UNEPLOMBE', '2', '2023-01-11 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328640', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329059', '9059', 'FR', 'UCOCOTTE', '2', '2023-01-16 00:00:00', 'FR', '99672102', 'FR', '9902899681', '66', 'FR', '9904328262', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329060', '9060', 'FR', 'UNICOLORE', '2', '2023-01-20 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328629', '66', '66');
INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race) VALUES ('9904329061', '9061', 'FR', 'URANUS', '2', '2023-01-20 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328623', '66', '66');

-- Tableau : asda
DROP TABLE IF EXISTS asda;
CREATE TABLE IF NOT EXISTS asda (
  cod_asda TEXT PRIMARY KEY,
  couleur TEXT NOT NULL
);
INSERT INTO asda (cod_asda, couleur) VALUES ('1', 'vert');
INSERT INTO asda (cod_asda, couleur) VALUES ('2', 'jaune');
INSERT INTO asda (cod_asda, couleur) VALUES ('3', 'orange');
INSERT INTO asda (cod_asda, couleur) VALUES ('4', 'rouge');

-- Tableau : elevage
DROP TABLE IF EXISTS elevage;
CREATE TABLE IF NOT EXISTS `elevage` (
  `num_elevage` TEXT NOT NULL,
  `nom` TEXT DEFAULT NULL,
  `mdp` TEXT DEFAULT NULL,
  `cod_asda` TEXT DEFAULT NULL,
  PRIMARY KEY (`num_elevage`),
  FOREIGN KEY (`cod_asda`) REFERENCES `asda` (`cod_asda`)
);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990266', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990289', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990402', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990409', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990412', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990415', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990416', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990417', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990419', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990430', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('990432', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('991111', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('991240', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('991502', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('991535', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('991900', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('991910', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992059', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992236', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992459', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992560', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992680', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992750', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992882', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992920', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('992960', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993274', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993410', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993420', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993701', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993740', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993780', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('993876', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('994339', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('994453', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('994729', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('994800', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('996007', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('996447', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('996721', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('997082', NULL, NULL, NULL);
INSERT INTO elevage (num_elevage, nom, mdp, cod_asda) VALUES ('997497', NULL, NULL, NULL);

-- Tableau : race
DROP TABLE IF EXISTS race;
CREATE TABLE IF NOT EXISTS race (
  cod_race TEXT PRIMARY KEY,
  lib_court TEXT NOT NULL,
  lib_long TEXT NOT NULL
);
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('0', 'Inconnu', 'Race inconnue');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('10', 'Bison', 'BISON');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('12', 'Abondance', 'ABONDANCE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('14', 'Aubrac', 'AUBRAC');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('15', 'Jersiaise', 'JERSIAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('17', 'Angus', 'ANGUS');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('18', 'Ayrshire', 'AYRSHIRE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('19', 'Pie Rouge', 'PIE ROUGE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('20', 'Buffle', 'BUFFLE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('21', 'Brune', 'BRUNE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('23', 'Salers', 'SALERS');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('24', 'Bazadaise', 'BAZADAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('25', 'Blanc Bleu', 'BLANC BLEU');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('26', 'Bordelaise', 'BORDELAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('29', 'Bretonne P.N.', 'BRETONNE PIE NOIRE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('30', 'Aurochs', 'AUROCHS RECONSTITUE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('31', 'Tarentaise', 'TARENTAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('32', 'Chianina', 'CHIANINA');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('33', 'Lourdaise', 'LOURDAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('34', 'Limousine', 'LIMOUSINE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('35', 'Simmental Fr.', 'SIMMENTAL FRANCAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('36', 'Corse', 'CORSE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('37', 'Raco di Biou', 'Ra�O DI BIOU');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('38', 'Charolaise', 'CHAROLAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('39', 'Croise', 'Crois�');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('41', 'Rouge d Pres', 'ROUGE DES PRES');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('42', 'Dairy Short.', 'DAIRY SHORTHORN');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('43', 'Armoricaine', 'ARMORICAINE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('44', 'Divers Lait', 'Autres races traites �trang�res');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('45', 'South Devon', 'SOUTH DEVON');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('46', 'Montb�liarde', 'MONTBELIARDE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('48', 'Divers Viande', 'Autres races allaitantes �trang�res');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('49', 'Marchigiana', 'MARCHIGIANA');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('51', 'De Combat', 'DE COMBAT (ESPAGNOLE BRAVA)');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('52', 'Bleue Du Nord', 'BLEUE DU NORD');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('53', 'Villard Lans', 'VILLARD DE LANS');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('54', 'N� Dama', 'N� DAMA');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('55', 'Creole', 'CREOLE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('56', 'Normande', 'NORMANDE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('57', 'Vosgienne', 'VOSGIENNE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('58', 'Maraichine', 'MARAICHINE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('61', 'Bearnaise', 'BEARNAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('63', 'Rouge Flamande', 'ROUGE FLAMANDE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('65', 'Ferrandaise', 'FERRANDAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('66', 'Prim�Holstein', 'PRIM� HOLSTEIN');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('69', 'Froment Leon', 'FROMENT DU LEON');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('71', 'Parthenaise', 'PARTHENAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('72', 'Gasconne', 'GASCONNE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('73', 'Galloway', 'GALLOWAY');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('74', 'Guernesey', 'GUERNESEY');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('75', 'Piemontaise', 'PIEMONTAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('76', 'Nantaise', 'NANTAISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('77', 'Mirandaise', 'MIRANDAISE (Gasconne ar�ol�e)');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('78', 'Gelbvieh', 'GELBVIEH');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('79', 'Blonde Aquit.', 'BLONDE D�AQUITAINE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('81', 'Brahma', 'BRAHMA');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('82', 'Herens', 'HERENS');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('85', 'Hereford', 'HEREFORD');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('86', 'Highland Cattle', 'HIGHLAND CATTLE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('88', 'Saosnoise', 'SAOSNOISE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('90', 'Zebu', 'ZEBU');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('92', 'Canadienne', 'CANADIENNE');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('95', 'Inra 95', 'INRA 95');
INSERT INTO race (cod_race, lib_court, lib_long) VALUES ('97', 'Casta', 'CASTA (AURE et ST GIRONS)');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
