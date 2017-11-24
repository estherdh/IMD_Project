CREATE SCHEMA IF NOT EXISTS librarian;
SET SCHEMA librarian;

-- TALEN

CREATE TABLE Language (
  LanguageId INT AUTO_INCREMENT PRIMARY KEY,
  Short_Code VARCHAR(10) NOT NULL UNIQUE,
  `Name` VARCHAR(32) NOT NULL UNIQUE
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

-- MUSEA

CREATE TABLE Museum (
  MuseumId INT AUTO_INCREMENT PRIMARY KEY,
  `MuseumName` VARCHAR(50) NOT NULL,
  `Website` VARCHAR(50),
  `Region` VARCHAR(45)
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
  `Completed` BOOLEAN
);

CREATE TABLE QuestProperties (
  `PropertyId` INT AUTO_INCREMENT PRIMARY KEY,
  `Key` VARCHAR(15) NOT NULL,
  `Value` VARCHAR(60) NOT NULL,
  `EntryId` INT
);

-- TOPSTUKKEN

CREATE TABLE Exhibit (
  ExhibitId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  MuseumId INT NOT NULL,
  Year INT NOT NULL,
  `Video` VARCHAR(100)
);

CREATE TABLE ExhibitInfo (
  ExhibitInfoId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  ExhibitId INT NOT NULL,
  LanguageId INT NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Description` TEXT NOT NULL,
  `Image` VARCHAR(45) NOT NULL
);


CREATE TABLE Tags (
  TagId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `TagName` VARCHAR(30) NOT NULL
);


CREATE TABLE ExhibitTags (
  ExhibitId INT NOT NULL,
  TagId INT NOT NULL
);

-- REPLICA'S

CREATE TABLE Replica (
  ReplicaId INT AUTO_INCREMENT PRIMARY KEY,
  ExhibitInfoId INT NOT NULL,
  `Price` INT NOT NULL,
  Sprite VARCHAR(45) NOT NULL,
  `Type` VARCHAR(45) NOT NULL,
  `Position` INT NOT NULL
);

CREATE TABLE UserReplica (
  UserId INT NOT NULL,
  ReplicaId INT NOT NULL,
  Position INT NOT NULL DEFAULT 0
);

-- FOREIGN KEYS

ALTER TABLE Users ADD CONSTRAINT FK_Users_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE QuestTypeLanguage ADD CONSTRAINT FK_QuestTypeLanguage_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE QuestTypeLanguage ADD CONSTRAINT FK_QuestTypeLanguage_QuestType
FOREIGN KEY (QuestTypeId) REFERENCES QuestType (QuestTypeId);

ALTER TABLE QuestLog ADD CONSTRAINT FK_QuestLog_User
FOREIGN KEY (UserId) REFERENCES Users (UserId);

ALTER TABLE QuestLog ADD CONSTRAINT FK_QuestLog_QuestType
FOREIGN KEY (QuestTypeId) REFERENCES QuestType (QuestTypeId);

ALTER TABLE QuestProperties ADD CONSTRAINT FK_QuestProperties_QuestLog
FOREIGN KEY (EntryId) REFERENCES QuestLog (EntryId);

ALTER TABLE ExhibitInfo ADD CONSTRAINT FK_ExhibitInfo_Exhibit
FOREIGN KEY (ExhibitId) REFERENCES Exhibit (ExhibitId);

ALTER TABLE ExhibitInfo ADD CONSTRAINT FK_ExhibitInfo_Language
FOREIGN KEY (LanguageId) REFERENCES Language (LanguageId);

ALTER TABLE Replica ADD CONSTRAINT FK_Replica_ExhibitInfo
FOREIGN KEY (ExhibitInfoId) REFERENCES ExhibitInfo (ExhibitInfoId);

ALTER TABLE Exhibit ADD CONSTRAINT FK_Exhibit_Museum
FOREIGN KEY (MuseumId) REFERENCES Museum (MuseumId);

ALTER TABLE ExhibitTags ADD CONSTRAINT FK_ExhibitTags_Exhibit
FOREIGN KEY (ExhibitId) REFERENCES Exhibit (ExhibitId);

ALTER TABLE ExhibitTags ADD CONSTRAINT FK_ExhibitTags_Tags
FOREIGN KEY (TagId) REFERENCES Tags (TagId);

ALTER TABLE UserReplica ADD CONSTRAINT FK_UserReplica_User
FOREIGN KEY (UserId) REFERENCES Users (UserId);

ALTER TABLE UserReplica ADD CONSTRAINT FK_UserReplica_Replica
FOREIGN KEY (ReplicaId) REFERENCES Replica (ReplicaId);

-- Hier komen de insert scripts
INSERT INTO librarian.language (Short_Code, Name) VALUES ('nl', 'Nederlands');
INSERT INTO librarian.language (Short_Code, Name) VALUES ('en', 'English');
INSERT INTO librarian.language (Short_Code, Name) VALUES ('TESTING', 'Lorem ipsum');

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
INSERT INTO librarian.questlog (UserId, QuestTypeId, Completed) VALUES (3, 2, 0);

INSERT INTO librarian.museum (`MuseumName`, `Website`, `Region`) VALUES ('De oude schatten', 'https://nope.nl', 'Niet Gelderland');

INSERT INTO librarian.exhibit (`MuseumId`, `Year`, `Video`) VALUES (1, 1990, 'nope.avi');

INSERT INTO librarian.exhibitinfo (`exhibitid`, `LanguageId`, `Name`, `Description`, `Image`) VALUES (1, 1, 'De test tekst', 'LORUM IPSUM FOR THE WIN', 'image.png');
INSERT INTO librarian.exhibitinfo (`exhibitid`, `LanguageId`, `Name`, `Description`, `Image`) VALUES (1, 2, 'LoLoRum Ipsum', 'TestLorumIpsumText', 'image.bmp');

INSERT INTO librarian.replica(`exhibitInfoId`, `Price`, `Sprite`, `Type`, `Position`) VALUES (1, 5, 'image.png', 'boek', 0);

INSERT INTO librarian.userreplica(`UserId`, `ReplicaId`, `Position`) VALUES (2, 1, 0);