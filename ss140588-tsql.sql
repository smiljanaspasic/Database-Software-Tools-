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