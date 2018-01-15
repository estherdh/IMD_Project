CREATE SCHEMA IF NOT EXISTS librarian;
SET SCHEMA librarian;


-- TALEN

CREATE TABLE Language (
  LanguageId INT AUTO_INCREMENT PRIMARY KEY,
  Short_Code VARCHAR(10) NOT NULL UNIQUE,
  `Name` VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE YearLanguage (
    `LanguageId` INT(11) NOT NULL,
    `Before` VARCHAR(45) NOT NULL,
    `After` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`LanguageId`)
);

-- GEBRUIKER

CREATE TABLE Users (
  UserId INT AUTO_INCREMENT PRIMARY KEY,
  `Email` VARCHAR(100) NOT NULL UNIQUE,
  `Password` VARCHAR(128) NOT NULL,
  Coins INT,
  `DisplayName` VARCHAR(50),
  LanguageId INT NOT NULL DEFAULT 1
);

-- NOTIFICATION

CREATE TABLE Notification (
  NotificationId INT AUTO_INCREMENT PRIMARY KEY,
  LanguageId INT NOT NULL,
  `NotificationText` VARCHAR(100) NOT NULL
);

CREATE TABLE UserNotification (
  UserNotificationId INT AUTO_INCREMENT PRIMARY KEY,
  NotificationId INT NOT NULL,
  UserId INT NOT NULL,
  `Read` TINYINT NOT NULL DEFAULT 0,
  `Date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE NotificationProperties (
  NotificationPropertiesId INT AUTO_INCREMENT PRIMARY KEY,
  UserNotificationId INT NOT NULL,
  `Key` VARCHAR(15) NOT NULL,
  `Value` VARCHAR(60) NOT NULL
);

-- MUSEA

CREATE TABLE Museum (
  MuseumId INT AUTO_INCREMENT PRIMARY KEY,
  `MuseumName` VARCHAR(50) NOT NULL,
  `Website` VARCHAR(50),
  `Region` VARCHAR(45),
  `Logo` VARCHAR(25),
  `QrCode` VARCHAR(60)
);

-- ADMINISTRATOR

CREATE TABLE Administrator (
  AdministratorId INT AUTO_INCREMENT PRIMARY KEY,
  `Email` VARCHAR(100) NOT NULL UNIQUE,
  `Password` VARCHAR(128) NOT NULL,
  Role INT NOT NULL DEFAULT 0,
  MuseumId INT NOT NULL
);

-- QUEST SYSTEEM

CREATE TABLE QuestType (
  QuestTypeId INT AUTO_INCREMENT PRIMARY KEY,
  `Reward` INT NOT NULL
);


CREATE TABLE QuestTypeLanguage (
  QuestTypeId INT NOT NULL,
  LanguageId INT NOT NULL,
  `Description` VARCHAR(256),
  `Name` VARCHAR(64) NOT NULL,
  CONSTRAINT pk_QuestTypeLanguage PRIMARY KEY (QuestTypeId, LanguageId)
);

CREATE TABLE QuestLog (
  EntryId INT AUTO_INCREMENT PRIMARY KEY,
  UserId INT NOT NULL,
  QuestTypeId INT NOT NULL,
  `Completed` BOOLEAN DEFAULT 0,
  Removed BOOLEAN DEFAULT 0
);

CREATE TABLE QuestProperties (
  `PropertyId` INT AUTO_INCREMENT PRIMARY KEY,
  `Key` VARCHAR(15) NOT NULL,
  `Value` VARCHAR(60) NOT NULL,
  `EntryId` INT
);

-- TOPSTUKKEN
CREATE TABLE Image (
  `ImageId` INT NOT NULL AUTO_INCREMENT,
  `Path` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ImageId`)
);

CREATE TABLE Exhibit (
  ExhibitId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  MuseumId INT NOT NULL,
  EraId INT NOT NULL,
  Year INT NOT NULL,
  Year2 INT NULL
);

CREATE TABLE ExhibitImage (
 `ExhibitId` INT NOT NULL,
 `ImageId` INT NOT NULL
);

CREATE TABLE ExhibitInfo (
  ExhibitInfoId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  ExhibitId INT NOT NULL,
  LanguageId INT NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Description` TEXT NOT NULL,
  `Video` VARCHAR(100)
);

CREATE TABLE Tags (
  TagId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `TagName` VARCHAR(30) NOT NULL
);

CREATE TABLE ExhibitTags (
  ExhibitId INT NOT NULL,
  TagId INT NOT NULL
);

CREATE TABLE Era(
  EraId INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE EraLanguage(
  TranslationId INT AUTO_INCREMENT PRIMARY KEY,
  LanguageId INT,
  EraId INT,
  `Name` VARCHAR(40) NOT NULL,
  CONSTRAINT EraLanguage_Unique_Translation UNIQUE (LanguageId, EraId),
  FOREIGN KEY (EraId) REFERENCES Era(EraId),
  FOREIGN KEY (LanguageId) REFERENCES Language(LanguageId)
);

-- REPLICA'S

CREATE TABLE ReplicaType (
  ReplicaTypeId INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `Name` VARCHAR(45) NOT NULL
);

CREATE TABLE Replica (
  ReplicaId INT AUTO_INCREMENT PRIMARY KEY,
  ExhibitId INT NOT NULL,
  `Price` INT NOT NULL,
  Sprite VARCHAR(45) NOT NULL,
  ReplicaTypeId INT NOT NULL
);


CREATE TABLE ReplicaPositions (
  ReplicaPositionId INT AUTO_INCREMENT PRIMARY KEY,
  ReplicaTypeId INT NOT NULL
);


CREATE TABLE UserReplica (
  UserId INT NOT NULL,
  ReplicaId INT NOT NULL,
  ReplicaPositionId INT NULL DEFAULT NULL
);

-- FOREIGN KEYS

ALTER TABLE Users ADD CONSTRAINT FK_Users_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE QuestTypeLanguage ADD CONSTRAINT FK_QuestTypeLanguage_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE QuestTypeLanguage ADD CONSTRAINT FK_QuestTypeLanguage_QuestType
FOREIGN KEY (QuestTypeId) REFERENCES QuestType (QuestTypeId);

ALTER TABLE QuestLog ADD CONSTRAINT FK_QuestLog_User
FOREIGN KEY (UserId) REFERENCES Users (UserId) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE QuestLog ADD CONSTRAINT FK_QuestLog_QuestType
FOREIGN KEY (QuestTypeId) REFERENCES QuestType (QuestTypeId);

ALTER TABLE QuestProperties ADD CONSTRAINT FK_QuestProperties_QuestLog
FOREIGN KEY (EntryId) REFERENCES QuestLog (EntryId)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE ExhibitInfo ADD CONSTRAINT FK_ExhibitInfo_Exhibit
FOREIGN KEY (ExhibitId) REFERENCES Exhibit (ExhibitId);

ALTER TABLE ExhibitImage ADD CONSTRAINT FK_ExhibitImage_Exhibit
FOREIGN KEY (ExhibitId) REFERENCES Exhibit (ExhibitId);

ALTER TABLE ExhibitImage ADD CONSTRAINT FK_ExhibitImage_Image
FOREIGN KEY (ImageId) REFERENCES Image (ImageId);

ALTER TABLE ExhibitInfo ADD CONSTRAINT FK_ExhibitInfo_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE Replica ADD CONSTRAINT FK_Replica_Exhibit
FOREIGN KEY (ExhibitId) REFERENCES Exhibit (ExhibitId);

ALTER TABLE Replica ADD CONSTRAINT FK_Replica_ReplicaType
FOREIGN KEY (ReplicaTypeId) REFERENCES ReplicaType (ReplicaTypeId);

ALTER TABLE ReplicaPositions ADD CONSTRAINT FK_ReplicaPositions_ReplicaType
FOREIGN KEY (ReplicaTypeId) REFERENCES ReplicaType (ReplicaTypeId);

ALTER TABLE UserReplica ADD CONSTRAINT FK_UserReplica_User
FOREIGN KEY (UserId) REFERENCES Users (UserId)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE UserReplica ADD CONSTRAINT FK_UserReplica_Replica
FOREIGN KEY (ReplicaId) REFERENCES Replica (ReplicaId);

ALTER TABLE UserReplica ADD CONSTRAINT FK_UserReplica_ReplicaPositions
FOREIGN KEY (ReplicaPositionId) REFERENCES ReplicaPositions (ReplicaPositionId);

ALTER TABLE YearLanguage ADD CONSTRAINT FK_YearLanguage_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE Exhibit ADD CONSTRAINT FK_Exhibit_Museum
FOREIGN KEY (MuseumId) REFERENCES Museum (MuseumId);

ALTER TABLE Exhibit ADD CONSTRAINT FK_Exhibit_Era
FOREIGN KEY (EraId) REFERENCES Era (EraId);

ALTER TABLE ExhibitTags ADD CONSTRAINT FK_ExhibitTags_Exhibit
FOREIGN KEY (ExhibitId) REFERENCES Exhibit (ExhibitId);

ALTER TABLE ExhibitTags ADD CONSTRAINT FK_ExhibitTags_Tags
FOREIGN KEY (TagId) REFERENCES Tags (TagId);

ALTER TABLE Administrator ADD CONSTRAINT FK_Administrator_Museum
FOREIGN KEY (MuseumId) REFERENCES Museum (MuseumId);

ALTER TABLE Notification ADD CONSTRAINT FK_Notification_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE UserNotification ADD CONSTRAINT FK_UserNotification_Notification
FOREIGN KEY (NotificationId) REFERENCES Notification (NotificationId);

ALTER TABLE UserNotification ADD CONSTRAINT FK_UserNotification_User
FOREIGN KEY (UserId) REFERENCES Users (UserId);

ALTER TABLE NotificationProperties ADD CONSTRAINT FK_NotificationProperties_UserNotification
FOREIGN KEY (UserNotificationId) REFERENCES UserNotification (UserNotificationId);

-- Hier komen de insert scripts
INSERT INTO librarian.language (Short_Code, Name) VALUES ('nl', 'Nederlands');
INSERT INTO librarian.language (Short_Code, Name) VALUES ('en', 'English');
INSERT INTO librarian.language (Short_Code, Name) VALUES ('TESTING', 'Lorem ipsum');

INSERT INTO librarian.yearlanguage (LanguageId, `Before`, `After`) VALUES (1, 'v.C.', 'n.C.');
INSERT INTO librarian.yearlanguage (LanguageId, `Before`, `After`) VALUES (2, 'BC', 'AD');

INSERT INTO librarian.users (Email, Password, Coins, DisplayName, LanguageId) VALUES ('mail', 'password', 0, 'DeNaam', 1);
INSERT INTO librarian.users (Email, Password, Coins, DisplayName, LanguageId) VALUES ('test@void', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 0, 'muspi merol', 2);
INSERT INTO librarian.users (Email, Password, Coins, DisplayName, LanguageId) VALUES ('Nope.avi@youtube.com', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 200, 'MONEYBAGS', 3);

INSERT INTO librarian.questtype (Reward) VALUES (10);
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (1, 1, '(NL)Scan een qr code', '(NL)Scannen qr code');
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (1, 2, '(EN)Scan a qr code', '(EN)Scan qr code');
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (1, 3, '(TESTING)Scan a qr code', '(TESTING)edoc rq nasc');

INSERT INTO librarian.questtype (Reward) VALUES (15);
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (2, 1, '(NL)Stuur een bepaald stuk tekst op', '(NL)Stuur tekst');
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (2, 3, '(TESTING)TEKStQuESTREquest', '(TESTING)TEKStQuESTREquest');

INSERT INTO librarian.questtype (Reward) VALUES (30);
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (3, 1, '(NL) Lees een topstuk', '(NL)Lezen topstuk');
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (3, 2, '(EN) Read an exhibit', '(EN)Read exhibit');

INSERT INTO librarian.questtype (Reward) VALUES (30);
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (4, 1, '(NL) Bekijk een tijdperk', '(NL)Bekijken tijdperk');
INSERT INTO librarian.questtypelanguage (QuestTypeId, LanguageId, Description, Name) VALUES (4, 2, '(EN) View an era', '(EN)View era');

INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (1, 1, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('QR', 'AAA', 1);
INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (2, 1, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('QR', 'AAB', 2);
INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (3, 1, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('QR', 'AAC', 3);
INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (1, 2, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('Tekst', 'AAD', 4);
INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (2, 2, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('Tekst', 'AAE', 5);


INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (1, 3, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('Topstuk', '2', 6);
INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (1, 3, 0);
INSERT INTO librarian.questproperties(`Key`, `Value`, EntryId) VALUES ('Topstuk', '3', 7);

INSERT INTO librarian.questlog (UserId, QuestTypeId) VALUES (1, 4);
INSERT INTO librarian.questproperties (`Key`, `Value`, EntryId) VALUES ('Tijdperk', '1', 8);
INSERT INTO librarian.questlog (UserId, QuestTypeId) VALUES (1, 4);
INSERT INTO librarian.questproperties (`Key`, `Value`, EntryId) VALUES ('Tijdperk', '2', 9);

INSERT INTO Image(ImageId, Path) VALUES(1, 'imagetest1');
INSERT INTO Image(ImageId, Path) VALUES(2, 'imagetest2');
INSERT INTO Image(ImageId, Path) VALUES(3, 'imagetest3');

INSERT INTO Era () VALUES ();
INSERT INTO eralanguage (`eraId`, `name`, `languageId`) VALUES (1, 'tijdperk test', 1);
INSERT INTO eralanguage (`eraId`, `name`, `languageId`) VALUES (1, 'test era', 2);

INSERT INTO Era () VALUES ();
INSERT INTO eralanguage (`eraId`, `name`, `languageId`) VALUES (2, 'tijdperk test2', 1);
INSERT INTO eralanguage (`eraId`, `name`, `languageId`) VALUES (2, 'test era2', 2);

INSERT INTO Era () VALUES ();
INSERT INTO eralanguage (`eraId`, `name`, `languageId`) VALUES (3, 'Steen tijd', 1);
INSERT INTO eralanguage (`eraId`, `name`, `languageId`) VALUES (3, 'Stone age', 2);


INSERT INTO Museum (`MuseumName`, `website`, `Region`, `QrCode`) VALUES ('test musei', 'http://google.nl', 'Nederland', 'AAA');

INSERT INTO Exhibit (`year`, `eraId`, `museumId`) VALUES ('1999', 1, 1);
INSERT INTO ExhibitImage(ExhibitId, ImageId) VALUES(1, 1);
INSERT INTO ExhibitImage(ExhibitId, ImageId) VALUES(1, 2);
INSERT INTO ExhibitInfo (`ExhibitId`, `languageId`, `name`, `description`)
VALUES (1, 1, 'Het test object', 'Dit object wordt altijd al gebruikt om te testen'),
  (1, 2, 'The test object', 'Possibly used for testing');

INSERT INTO Exhibit (`year`, `eraId`, `museumId`) VALUES ('2010', 1, 1);
INSERT INTO ExhibitImage(ExhibitId, ImageId) VALUES(2, 3);
INSERT INTO ExhibitInfo (`ExhibitId`, `languageId`, `name`, `description`)
VALUES (2, 1, 'Het voorbeeld beeldje', 'Dit beeldje is ware kunst, een ideaal voorbeeld.'),
  (2, 3, 'Lol look at tis translation', 'Possibly testing de taal');

INSERT INTO Museum (`MuseumName`, `website`, `Region`, `QrCode`) VALUES ('De verzamel schuur', 'http://google.twente', 'Twente', 'AAB');

INSERT INTO Exhibit (`year`, `eraId`, `museumId`) VALUES ('2015', 1, 2);
INSERT INTO ExhibitInfo (`ExhibitId`, `languageId`, `name`, `description`)
VALUES (3, 1, 'Trekker', 'Deze trekker is geen tractor!'),
  (3, 2, 'Farmers vehicle', 'Use primarily for 14 year olds to drive around without an actual drivers license');

INSERT INTO Exhibit (`year`, `eraId`, `museumId`) VALUES ('2017', 1, 2);
INSERT INTO ExhibitInfo (`ExhibitId`, `languageId`, `name`, `description`)
VALUES (4, 1, 'Voorbeeld streektaal', 'Dit papier bevat een stuk tekst in streektaal: Oet de goaldn korenaarn skeup God de Tweantenaarn, en oet t kaf en d restn de leu oet t Westn'),
  (4, 3, 'Lol look at tis translation', 'Possibly testing de taal');

INSERT INTO `replicatype` (`ReplicaTypeId`, `Name`) VALUES
  (1, 'wall'),
  (2, 'floor'),
  (3, 'table');

INSERT INTO `replica` (`ReplicaId`, `ExhibitId`, `Price`, `Sprite`, `ReplicaTypeId`) VALUES
    (1, 1, 10, 'traktor', 2),
    (2, 1, 15, 'test1', 2),
    (3, 1, 12, 'test2', 2);

INSERT INTO `replicapositions` (`ReplicaPositionId`, `ReplicaTypeId`) VALUES
  (1, 2),
  (2, 2),
  (3, 2);

INSERT INTO `userreplica` (`UserId`, `ReplicaId`, `ReplicaPositionId`) VALUES
  (1, 2, 1),
  (1, 3, NULL),
  (2, 1, NULL);

INSERT INTO `Notification` (`LanguageId`, `NotificationText`) VALUES (1, 'Quest `{{{1}}}` voltooid! Je hebt nu {{{2}}} munten');
INSERT INTO `UserNotification` (NotificationId, UserId) VALUES (1, 2);
INSERT INTO `NotificationProperties` (`Key`, `Value`, `UserNotificationId`) VALUES ('QuestId', '2', 1);
INSERT INTO `NotificationProperties` (`Key`, `Value`, `UserNotificationId`) VALUES ('Coins', '2000', 1);


INSERT INTO `Notification` (`LanguageId`, `NotificationText`) VALUES (1, 'Quest `{{{1}}}` verwijderd');
INSERT INTO `UserNotification` (NotificationId, UserId) VALUES (2, 2);
INSERT INTO `NotificationProperties` (`Key`, `Value`, `UserNotificationId`) VALUES ('QuestId', '2', 2);