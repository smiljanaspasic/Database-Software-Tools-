CREATE TABLE [Administrator]
( 
	[IdKorisnik]         integer  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

CREATE TABLE [Adresa]
( 
	[IdAdresa]           integer IDENTITY  NOT NULL ,
	[Ulica]              varchar(100)  NOT NULL ,
	[Broj]               integer  NOT NULL ,
	[X]                  integer  NOT NULL ,
	[Y]                  integer  NOT NULL ,
	[IdGrad]             integer  NOT NULL 
)
go

ALTER TABLE [Adresa]
	ADD CONSTRAINT [XPKAdresa] PRIMARY KEY  CLUSTERED ([IdAdresa] ASC)
go

CREATE TABLE [Grad]
( 
	[IdGrad]             integer IDENTITY  NOT NULL  ,
	[Naziv]              varchar(100)  NOT NULL ,
	[Postbr]             varchar(100)  NOT NULL 
)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

CREATE TABLE [Kargo]
( 
	[IdKargo]            integer IDENTITY  NOT NULL ,
	[IdPaket]            integer  NULL ,
	[IdVozi]             integer  NULL 
)
go

ALTER TABLE [Kargo]
	ADD CONSTRAINT [XPKKargo] PRIMARY KEY  CLUSTERED ([IdKargo] ASC)
go

CREATE TABLE [Korisnik]
( 
	[IdKorisnik]         integer IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[IdAdresa]           integer  NOT NULL 
)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

CREATE TABLE [Kurir]
( 
	[IdKorisnik]         integer  NOT NULL ,
	[BrVozackeDozvole]   varchar(100)  NOT NULL ,
	[BrIsporucenihPaketa] integer  NOT NULL ,
	[Profit]             decimal(10,3)  NOT NULL ,
	[Status]             integer  NOT NULL 
	CONSTRAINT [Status_Kurira_1939305366]
		CHECK  ( Status BETWEEN 0 AND 1 )
)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

CREATE TABLE [KurirZahtev]
( 
	[IdKorisnik]         integer  NOT NULL ,
	[BrVozackeDozvole]   varchar(100)  NOT NULL 
)
go

ALTER TABLE [KurirZahtev]
	ADD CONSTRAINT [XPKKurirZahtev] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

CREATE TABLE [Magacin]
( 
	[IdMagacin]          integer IDENTITY  NOT NULL ,
	[IdAdresa]           integer  NOT NULL 
)
go

ALTER TABLE [Magacin]
	ADD CONSTRAINT [XPKMagacin] PRIMARY KEY  CLUSTERED ([IdMagacin] ASC)
go

CREATE TABLE [Paket]
( 
	[IdPaket]            integer IDENTITY  NOT NULL ,
	[Tip]                integer  NOT NULL 
	CONSTRAINT [Tip_paketa_1332019743]
		CHECK  ( Tip BETWEEN 0 AND 3 ),
	[Tezina]             decimal(10,3)  NOT NULL ,
	[Status]             integer  NOT NULL 
	CONSTRAINT [Status_Isporuke_2089222977]
		CHECK  ( Status BETWEEN 0 AND 4 ),
	[AdrPoslata]         integer  NOT NULL ,
	[AdrDostave]         integer  NOT NULL ,
	[Cena]               decimal(10,3)  NOT NULL ,
	[ZahtevKreiran]      datetime  NULL ,
	[ZahtevPrihvacen]    datetime  NULL ,
	[IdKorisnik]         integer  NOT NULL ,
	[Lokacija]           integer  NULL 
)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

CREATE TABLE [Plan]
( 
	[IdPlan]             integer IDENTITY NOT NULL ,
	[Kretanje]           integer  NOT NULL 
	CONSTRAINT [Kretanje_Pravilo_2145902578]
		CHECK  ( Kretanje BETWEEN 0 AND 2 ),
	[Narudzbina]         integer  NOT NULL ,
	[IdPaket]            integer  NOT NULL ,
	[IdVozi]             integer  NOT NULL 
)
go

ALTER TABLE [Plan]
	ADD CONSTRAINT [XPKPlan] PRIMARY KEY  CLUSTERED ([IdPlan] ASC)
go

CREATE TABLE [RanijeVozio]
( 
	[IdKorisnik]         integer  NOT NULL ,
	[IdVozilo]           integer  NOT NULL 
)
go

ALTER TABLE [RanijeVozio]
	ADD CONSTRAINT [XPKRanijeVozio] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC,[IdVozilo] ASC)
go

CREATE TABLE [Vozi]
( 
	[IdVozi]             integer IDENTITY NOT NULL ,
	[IdKorisnik]         integer  NULL ,
	[IdVozilo]           integer  NULL ,
	[TrenAdresa]         integer  NULL 
)
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [XPKVozi] PRIMARY KEY  CLUSTERED ([IdVozi] ASC)
go

CREATE TABLE [Vozilo]
( 
	[IdVozilo]           integer IDENTITY  NOT NULL ,
	[RegBroj]            varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NOT NULL 
	CONSTRAINT [Tip_Goriva_2137544662]
		CHECK  ( TipGoriva BETWEEN 0 AND 2 ),
	[Potrosnja]          decimal(10,3)  NOT NULL ,
	[Kapacitet]          char(18)  NULL ,
	[Slobodno]           integer  NOT NULL 
	CONSTRAINT [Slobodno_Vozilo_53948836]
		CHECK  ( Slobodno BETWEEN 0 AND 1 ),
	[IdMagacin]          integer  NULL 
)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([IdVozilo] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Adresa]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Kargo]
	ADD CONSTRAINT [R_20] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Kargo]
	ADD CONSTRAINT [R_23] FOREIGN KEY ([IdVozi]) REFERENCES [Vozi]([IdVozi])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Korisnik]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [KurirZahtev]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Magacin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([AdrPoslata]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([AdrDostave]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([Lokacija]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Plan]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Plan]
	ADD CONSTRAINT [R_21] FOREIGN KEY ([IdVozi]) REFERENCES [Vozi]([IdVozi])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [RanijeVozio]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([IdKorisnik]) REFERENCES [Kurir]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [RanijeVozio]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdVozilo]) REFERENCES [Vozilo]([IdVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdKorisnik]) REFERENCES [Kurir]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdVozilo]) REFERENCES [Vozilo]([IdVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([TrenAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Vozilo]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([IdMagacin]) REFERENCES [Magacin]([IdMagacin])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

CREATE TRIGGER TR_TransportOffer_Insert 
   ON  Paket 
  FOR INSERT
AS 
BEGIN
	
	Declare @idpaket int
	Declare @tip int
	Declare @tezina decimal(10,3)
	Declare @pocetnaadr int
	Declare @krajnjaadr int
	Declare @Xpocetno int
	Declare @Ypocetno int
	Declare @Xkrajnje int
	Declare @Ykrajnje int
	Declare @distanca decimal(10,3)
	Declare @cena decimal(10,3)
	Declare @kursor Cursor

	SET @kursor = CURSOR FOR
	select IdPaket, Tip, Tezina, AdrPoslata, AdrDostave
	from inserted

	OPEN @kursor
	FETCH NEXT FROM @kursor
	INTO @idpaket, @tip, @tezina, @pocetnaadr, @krajnjaadr

	WHILE @@FETCH_STATUS = 0
	BEGIN

		SET @Xpocetno = (select X from Adresa where IdAdresa=@pocetnaadr)
		SET @Ypocetno = (select Y from Adresa where IdAdresa=@pocetnaadr)
		SET @Xkrajnje  = (select X from Adresa where IdAdresa=@krajnjaadr)
		SET @Ykrajnje = (select Y from Adresa where IdAdresa=@krajnjaadr)
		SET @distanca = SQRT(SQUARE(@Xpocetno-@Xkrajnje)+SQUARE(@Ypocetno-@Ykrajnje))
		SET @cena = 
		case
			when @tip = 0 then @cena*115 
			when @tip = 1 then @cena*(175 + @tezina*100)
			when @tip = 2 then @cena*(250 + @tezina*100) 
			when @tip = 3 then @cena*(350 + @tezina*500) 
		END

		UPDATE Paket
		set Cena = @cena
		where IdPaket = @idpaket

		FETCH NEXT FROM @kursor
		INTO @idpaket, @tip, @tezina, @pocetnaadr, @krajnjaadr

	END
	CLOSE @kursor
	DEALLOCATE @kursor
END

CREATE TRIGGER TR_TransportOffer_Update 
   ON  Paket 
  FOR UPDATE
AS 
BEGIN
	
	Declare @idpaket int
	Declare @tip int
	Declare @tezina decimal(10,3)
	Declare @pocetnaadr int
	Declare @krajnjaadr int
	Declare @status int
	Declare @idpaketstari int
	Declare @tipstari int
	Declare @tezinastari decimal(10,3)
	Declare @pocetnaadrstari int
	Declare @krajnjaadrstari int
	Declare @statusstari int
	Declare @Xpocetno int
	Declare @Ypocetno int
	Declare @Xkrajnje int
	Declare @Ykrajnje int
	Declare @distanca decimal(10,3)
	Declare @cena decimal(10,3)
	Declare @kursorI Cursor
	Declare @kursorD Cursor

	SET @kursorI = CURSOR FOR
	select IdPaket, Tip, Tezina, AdrPoslata, AdrDostave,Status
	from inserted
	SET @kursorD = CURSOR FOR
	select IdPaket, Tip, Tezina, AdrPoslata, AdrDostave,Status
	from deleted

	OPEN @kursorD
	FETCH NEXT FROM @kursorD
	INTO @idpaketstari, @tipstari, @tezinastari, @pocetnaadrstari, @krajnjaadrstari,@statusstari
	OPEN @kursorI
	FETCH NEXT FROM @kursorI
	INTO @idpaket, @tip, @tezina, @pocetnaadr, @krajnjaadr,@status

	WHILE @@FETCH_STATUS = 0
	BEGIN

	IF (@statusstari != 0 AND (@tipstari != @tip OR @tezinastari != @tezina)) BEGIN

			ROLLBACK TRANSACTION
			BREAK
		END

		ELSE BEGIN

		SET @Xpocetno = (select X from Adresa where IdAdresa=@pocetnaadr)
		SET @Ypocetno = (select Y from Adresa where IdAdresa=@pocetnaadr)
		SET @Xkrajnje  = (select X from Adresa where IdAdresa=@krajnjaadr)
		SET @Ykrajnje = (select Y from Adresa where IdAdresa=@krajnjaadr)
		SET @distanca = SQRT(SQUARE(@Xpocetno-@Xkrajnje)+SQUARE(@Ypocetno-@Ykrajnje))
		SET @cena = 
		case
			when @tip = 0 then @cena*115 
			when @tip = 1 then @cena*(175 + @tezina*100)
			when @tip = 2 then @cena*(250 + @tezina*100) 
			when @tip = 3 then @cena*(350 + @tezina*500) 
		END

		UPDATE Paket
		set Cena = @cena
		where IdPaket = @idpaket

		FETCH NEXT FROM @kursorD
	    INTO @idpaketstari, @tipstari, @tezinastari, @pocetnaadrstari, @krajnjaadrstari,@statusstari
		FETCH NEXT FROM @kursorI
		INTO @idpaket, @tip, @tezina, @pocetnaadr, @krajnjaadr,@status

		END
	END
	CLOSE @kursorI
	DEALLOCATE @kursorI
	CLOSE @kursorD
	DEALLOCATE @kursorD
END