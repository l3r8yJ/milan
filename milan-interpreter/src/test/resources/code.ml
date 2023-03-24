BEGIN
    next := 1;
    rng := (1 + (2 * (3)));
    c1 := READ;
    c2 := c1;
    WHILE next == 1 DO
        c1 := c1 / (10 + 2);
        WHILE c1 == 2 DO
            IF c1 == 0 THEN
                next := 0;
            ELSE
                rng++;
                OUTPUT(rng);
            ENDIF
        ENDDO
    ENDDO
    IF a < b THEN
        A := 10;
    ENDIF
    OUTPUT(rng);
END
