-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 20 fév. 2024 à 17:42
-- Version du serveur : 5.7.36
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `bdd_ede`
--

-- --------------------------------------------------------

--
-- Structure de la table `animal`
--

DROP TABLE IF EXISTS `animal`;
CREATE TABLE IF NOT EXISTS `animal` (
  `num_nat` varchar(10) NOT NULL,
  `num_tra` varchar(4) NOT NULL,
  `cod_pays` varchar(2) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `sexe` varchar(1) NOT NULL,
  `date_naiss` datetime NOT NULL,
  `cod_pays_naiss` varchar(2) NOT NULL,
  `num_exp_naiss` varchar(10) NOT NULL,
  `cod_pays_pere` varchar(2) DEFAULT NULL,
  `num_nat_pere` varchar(10) DEFAULT NULL,
  `cod_race_pere` varchar(3) DEFAULT NULL,
  `cod_pays_mere` varchar(2) NOT NULL,
  `num_nat_mere` varchar(10) NOT NULL,
  `cod_race_mere` varchar(3) NOT NULL,
  `cod_race` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`num_nat`),
  KEY `animal_race0_FK` (`cod_race`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `animal`
--

INSERT INTO `animal` (`num_nat`, `num_tra`, `cod_pays`, `nom`, `sexe`, `date_naiss`, `cod_pays_naiss`, `num_exp_naiss`, `cod_pays_pere`, `num_nat_pere`, `cod_race_pere`, `cod_pays_mere`, `num_nat_mere`, `cod_race_mere`, `cod_race`) VALUES
('9904022308', '2308', 'FR', 'UBER', '1', '2023-01-03 00:00:00', 'FR', '99275011', 'FR', '9924595810', '38', 'FR', '9904022181', '38', '38'),
('9904022309', '2309', 'FR', 'UGGLY', '2', '2023-01-12 00:00:00', 'FR', '99275011', 'FR', '9924595810', '38', 'FR', '9904022179', '38', '38'),
('9904022310', '2310', 'FR', 'ULLA', '2', '2023-01-13 00:00:00', 'FR', '99275011', 'FR', '9902666575', '38', 'FR', '9904022229', '38', '38'),
('9904022311', '2311', 'FR', 'ULANA', '2', '2023-01-21 00:00:00', 'FR', '99275011', 'FR', '9911118841', '38', 'FR', '9904022207', '38', '38'),
('9904022312', '2312', 'FR', 'UDADA', '2', '2023-01-22 00:00:00', 'FR', '99275011', 'FR', '9919103201', '38', 'FR', '9904022230', '38', '38'),
('9904022313', '2313', 'FR', 'UNEDEPLUS', '2', '2023-01-22 00:00:00', 'FR', '99275011', 'FR', '9919103201', '38', 'FR', '9904022184', '38', '38'),
('9904022399', '2399', 'FR', 'UDERZO', '1', '2023-01-28 00:00:00', 'FR', '99275011', 'FR', '9919103201', '38', 'FR', '9904022195', '38', '38'),
('9904095790', '5790', 'FR', NULL, '1', '2023-01-08 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095541', '56', '39'),
('9904095791', '5791', 'FR', NULL, '2', '2023-01-08 00:00:00', 'FR', '99124091', NULL, NULL, '66', 'FR', '9904095569', '39', '39'),
('9904095792', '5792', 'FR', NULL, '2', '2023-01-16 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095447', '39', '39'),
('9904095793', '5793', 'FR', NULL, '2', '2023-01-16 00:00:00', 'FR', '99124091', NULL, NULL, '15', 'FR', '9904095595', '15', '15'),
('9904095794', '5794', 'FR', NULL, '2', '2023-01-15 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095261', '39', '39'),
('9904095795', '5795', 'FR', NULL, '1', '2023-01-22 00:00:00', 'FR', '99124091', NULL, NULL, '25', 'FR', '9904095479', '39', '39'),
('9904122722', '2722', 'FR', 'UNIQUE', '2', '2023-01-10 00:00:00', 'FR', '99190016', 'FR', '9937800349', '79', 'FR', '9932742433', '79', '79'),
('9904122723', '2723', 'FR', NULL, '1', '2023-01-11 00:00:00', 'FR', '99190016', 'FR', '9943396600', '79', 'FR', '9904122677', '79', '79'),
('9904158437', '8437', 'FR', 'UNO', '1', '2023-01-02 00:00:00', 'FR', '99268099', 'FR', '9915020669', '79', 'FR', '9964474727', '79', '79'),
('9904169631', '9631', 'FR', 'MARCUS', '1', '2023-01-04 00:00:00', 'FR', '99292036', 'FR', '9925603382', '41', 'FR', '9938767786', '41', '41'),
('9904175249', '5249', 'FR', 'ULYSSE', '1', '2023-01-11 00:00:00', 'FR', '99296082', 'FR', '9904175212', '34', 'FR', '9904175172', '34', '34'),
('9904190105', '2105', 'FR', NULL, '2', '2023-01-08 00:00:00', 'FR', '99342070', 'FR', '9944535211', '38', 'FR', '9920590523', '39', '39'),
('9904302175', '2175', 'FR', NULL, '1', '2023-01-09 00:00:00', 'FR', '99370171', 'FR', '9974973537', '66', 'FR', '9904301760', '66', '66'),
('9904302176', '2176', 'FR', NULL, '1', '2023-01-17 00:00:00', 'FR', '99370171', 'FR', '9947295105', '56', 'FR', '9904301847', '56', '56'),
('9904302177', '2177', 'FR', 'ULISSE', '2', '2023-01-19 00:00:00', 'FR', '99370171', 'FR', '9970822884', '56', 'FR', '9904301741', '56', '56'),
('9904302178', '2178', 'FR', NULL, '1', '2023-01-22 00:00:00', 'FR', '99370171', 'FR', '9960076998', '34', 'FR', '9904301821', '56', '39'),
('9904302179', '2179', 'FR', NULL, '1', '2023-01-27 00:00:00', 'FR', '99370171', 'FR', '9922361136', '56', 'FR', '9904301641', '56', '56'),
('9904304616', '4616', 'FR', NULL, '2', '2023-01-22 00:00:00', 'FR', '99374024', 'FR', '9934109355', '34', 'FR', '9904304598', '34', '34'),
('9904304617', '4617', 'FR', NULL, '1', '2023-01-22 00:00:00', 'FR', '99374024', 'FR', '9915353586', '34', 'FR', '9904304603', '34', '34'),
('9904304618', '4618', 'FR', NULL, '1', '2023-01-27 00:00:00', 'FR', '99374024', NULL, NULL, '34', 'FR', '9904304600', '34', '34'),
('9904328944', '8944', 'FR', NULL, '1', '2023-01-11 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328625', '66', '66'),
('9904328945', '8945', 'FR', NULL, '1', '2023-01-16 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328631', '66', '66'),
('9904328946', '8946', 'FR', NULL, '1', '2023-01-05 00:00:00', 'FR', '99672102', 'FR', '9904328364', '66', 'FR', '9904328618', '66', '66'),
('9904329053', '9053', 'FR', 'ULTIMA', '2', '2023-01-04 00:00:00', 'FR', '99672102', 'FR', '9948001750', '66', 'FR', '9904328446', '66', '66'),
('9904329054', '9054', 'FR', 'UNICOPA', '2', '2023-01-02 00:00:00', 'FR', '99672102', 'FR', '9904328364', '66', 'FR', '9904328619', '66', '66'),
('9904329055', '9055', 'FR', 'UBUESQUE', '2', '2023-01-04 00:00:00', 'FR', '99672102', 'FR', '9928823654', '66', 'FR', '9904328340', '66', '66'),
('9904329056', '9056', 'FR', 'UEFA', '2', '2023-01-05 00:00:00', 'FR', '99672102', 'FR', '9904328364', '66', 'FR', '9904328628', '66', '66'),
('9904329057', '9057', 'FR', 'UHU', '2', '2023-01-11 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328532', '66', '66'),
('9904329058', '9058', 'FR', 'UNEPLOMBE', '2', '2023-01-11 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328640', '66', '66'),
('9904329059', '9059', 'FR', 'UCOCOTTE', '2', '2023-01-16 00:00:00', 'FR', '99672102', 'FR', '9902899681', '66', 'FR', '9904328262', '66', '66'),
('9904329060', '9060', 'FR', 'UNICOLORE', '2', '2023-01-20 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328629', '66', '66'),
('9904329061', '9061', 'FR', 'URANUS', '2', '2023-01-20 00:00:00', 'FR', '99672102', 'FR', '9904328551', '66', 'FR', '9904328623', '66', '66');

-- --------------------------------------------------------

--
-- Structure de la table `asda`
--

DROP TABLE IF EXISTS `asda`;
CREATE TABLE IF NOT EXISTS `asda` (
  `cod_asda` varchar(1) NOT NULL,
  `couleur` varchar(50) NOT NULL,
  PRIMARY KEY (`cod_asda`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `asda`
--

INSERT INTO `asda` (`cod_asda`, `couleur`) VALUES
('1', 'vert'),
('2', 'jaune'),
('3', 'orange'),
('4', 'rouge');

-- --------------------------------------------------------

--
-- Structure de la table `elevage`
--

DROP TABLE IF EXISTS `elevage`;
CREATE TABLE IF NOT EXISTS `elevage` (
  `num_elevage` varchar(6) NOT NULL,
  `nom` varchar(250) DEFAULT NULL,
  `mdp` varchar(10) DEFAULT NULL,
  `cod_asda` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`num_elevage`),
  UNIQUE KEY `num_elevage` (`num_elevage`),
  KEY `elevage_asda_FK` (`cod_asda`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `elevage`
--

INSERT INTO `elevage` (`num_elevage`, `nom`, `mdp`, `cod_asda`) VALUES
('990266', NULL, NULL, NULL),
('990289', NULL, NULL, NULL),
('990402', NULL, NULL, NULL),
('990409', NULL, NULL, NULL),
('990412', NULL, NULL, NULL),
('990415', NULL, NULL, NULL),
('990416', NULL, NULL, NULL),
('990417', NULL, NULL, NULL),
('990419', NULL, NULL, NULL),
('990430', NULL, NULL, NULL),
('990432', NULL, NULL, NULL),
('991111', NULL, NULL, NULL),
('991240', NULL, NULL, NULL),
('991502', NULL, NULL, NULL),
('991535', NULL, NULL, NULL),
('991900', NULL, NULL, NULL),
('991910', NULL, NULL, NULL),
('992059', NULL, NULL, NULL),
('992236', NULL, NULL, NULL),
('992459', NULL, NULL, NULL),
('992560', NULL, NULL, NULL),
('992680', NULL, NULL, NULL),
('992750', NULL, NULL, NULL),
('992882', NULL, NULL, NULL),
('992920', NULL, NULL, NULL),
('992960', NULL, NULL, NULL),
('993274', NULL, NULL, NULL),
('993410', NULL, NULL, NULL),
('993420', NULL, NULL, NULL),
('993701', NULL, NULL, NULL),
('993740', NULL, NULL, NULL),
('993780', NULL, NULL, NULL),
('993876', NULL, NULL, NULL),
('994339', NULL, NULL, NULL),
('994453', NULL, NULL, NULL),
('994729', NULL, NULL, NULL),
('994800', NULL, NULL, NULL),
('996007', NULL, NULL, NULL),
('996447', NULL, NULL, NULL),
('996721', NULL, NULL, NULL),
('997082', NULL, NULL, NULL),
('997497', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `race`
--

DROP TABLE IF EXISTS `race`;
CREATE TABLE IF NOT EXISTS `race` (
  `cod_race` varchar(3) NOT NULL,
  `lib_court` varchar(100) NOT NULL,
  `lib_long` varchar(100) NOT NULL,
  PRIMARY KEY (`cod_race`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `race`
--

INSERT INTO `race` (`cod_race`, `lib_court`, `lib_long`) VALUES
('0', 'Inconnu', 'Race inconnue'),
('10', 'Bison', 'BISON'),
('12', 'Abondance', 'ABONDANCE'),
('14', 'Aubrac', 'AUBRAC'),
('15', 'Jersiaise', 'JERSIAISE'),
('17', 'Angus', 'ANGUS'),
('18', 'Ayrshire', 'AYRSHIRE'),
('19', 'Pie Rouge', 'PIE ROUGE'),
('20', 'Buffle', 'BUFFLE'),
('21', 'Brune', 'BRUNE'),
('23', 'Salers', 'SALERS'),
('24', 'Bazadaise', 'BAZADAISE'),
('25', 'Blanc Bleu', 'BLANC BLEU'),
('26', 'Bordelaise', 'BORDELAISE'),
('29', 'Bretonne P.N.', 'BRETONNE PIE NOIRE'),
('30', 'Aurochs', 'AUROCHS RECONSTITUE'),
('31', 'Tarentaise', 'TARENTAISE'),
('32', 'Chianina', 'CHIANINA'),
('33', 'Lourdaise', 'LOURDAISE'),
('34', 'Limousine', 'LIMOUSINE'),
('35', 'Simmental Fr.', 'SIMMENTAL FRANCAISE'),
('36', 'Corse', 'CORSE'),
('37', 'Raco di Biou', 'RaçO DI BIOU'),
('38', 'Charolaise', 'CHAROLAISE'),
('39', 'Croise', 'Croisé'),
('41', 'Rouge d Pres', 'ROUGE DES PRES'),
('42', 'Dairy Short.', 'DAIRY SHORTHORN'),
('43', 'Armoricaine', 'ARMORICAINE'),
('44', 'Divers Lait', 'Autres races traites étrangères'),
('45', 'South Devon', 'SOUTH DEVON'),
('46', 'Montbéliarde', 'MONTBELIARDE'),
('48', 'Divers Viande', 'Autres races allaitantes étrangères'),
('49', 'Marchigiana', 'MARCHIGIANA'),
('51', 'De Combat', 'DE COMBAT (ESPAGNOLE BRAVA)'),
('52', 'Bleue Du Nord', 'BLEUE DU NORD'),
('53', 'Villard Lans', 'VILLARD DE LANS'),
('54', 'N‘ Dama', 'N‘ DAMA'),
('55', 'Creole', 'CREOLE'),
('56', 'Normande', 'NORMANDE'),
('57', 'Vosgienne', 'VOSGIENNE'),
('58', 'Maraichine', 'MARAICHINE'),
('61', 'Bearnaise', 'BEARNAISE'),
('63', 'Rouge Flamande', 'ROUGE FLAMANDE'),
('65', 'Ferrandaise', 'FERRANDAISE'),
('66', 'Prim’Holstein', 'PRIM’ HOLSTEIN'),
('69', 'Froment Leon', 'FROMENT DU LEON'),
('71', 'Parthenaise', 'PARTHENAISE'),
('72', 'Gasconne', 'GASCONNE'),
('73', 'Galloway', 'GALLOWAY'),
('74', 'Guernesey', 'GUERNESEY'),
('75', 'Piemontaise', 'PIEMONTAISE'),
('76', 'Nantaise', 'NANTAISE'),
('77', 'Mirandaise', 'MIRANDAISE (Gasconne aréolée)'),
('78', 'Gelbvieh', 'GELBVIEH'),
('79', 'Blonde Aquit.', 'BLONDE D’AQUITAINE'),
('81', 'Brahma', 'BRAHMA'),
('82', 'Herens', 'HERENS'),
('85', 'Hereford', 'HEREFORD'),
('86', 'Highland Cattle', 'HIGHLAND CATTLE'),
('88', 'Saosnoise', 'SAOSNOISE'),
('90', 'Zebu', 'ZEBU'),
('92', 'Canadienne', 'CANADIENNE'),
('95', 'Inra 95', 'INRA 95'),
('97', 'Casta', 'CASTA (AURE et ST GIRONS)');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `animal`
--
ALTER TABLE `animal`
  ADD CONSTRAINT `animal_race0_FK` FOREIGN KEY (`cod_race`) REFERENCES `race` (`cod_race`);

--
-- Contraintes pour la table `elevage`
--
ALTER TABLE `elevage`
  ADD CONSTRAINT `elevage_asda_FK` FOREIGN KEY (`cod_asda`) REFERENCES `asda` (`cod_asda`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
