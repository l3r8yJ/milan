<b>Based on</b>
<img alt="logo" src="https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fcdn.onlinewebfonts.com%2Fsvg%2Fimg_189624.png&f=1&nofb=1&ipt=96f2feb24e716bca2927efe519b26138829643d5931bddb3ba1dec5f51b5e517&ipo=images" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/objectionary/eo)](http://www.rultor.com/p/l3r8yJ/milan)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![codecov](https://codecov.io/gh/l3r8yJ/milan/branch/master/graph/badge.svg?token=W8HLMLKHDX)](https://codecov.io/gh/l3r8yJ/milan)

**Milan** is a very simple programming language designed to teach students at Volzhskiy Polytechnic Institute.

Current version of language – **JVM** based.

## Grammar

| **Terminals**        | **Values**                                                                    |
|----------------------|-------------------------------------------------------------------------------|
| Keywords             | `BEGIN` `DO` `ELSE` `END` `ENDDO` `ENDIF` `IF` `OUTPUT` `READ` `THEN` `WHILE` |
| Operands             | `+` `++` `-` `*` `/` `=` `>` `<` `<=` `>=` `<>`                               |
| Numbers              | `[0-9]`                                                                       |
| Letters              | `[a-zA-Z]`                                                                    |
| Assignment character | `:=`                                                                          |
| Separator            | `;`                                                                           |
| Comments             | `//`                                                                          |
| Construction         | `(` `)`                                                                       |

## Demo

![demo](https://user-images.githubusercontent.com/46355873/228844342-2179df20-9dd1-4354-a392-fbdfd29c9c03.gif)

## Progress

Current version of Milan grammar supports expressions like:
```pascal
BEGIN
    next := 1;
    rng := (1 + (2 * (3)));
    READ(c1);
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
```
