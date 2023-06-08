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
| Operands             | `+` `-` `*` `/` `=` `>` `<` `<=` `>=` `<>`                                    |
| Numbers              | `[0-9]`                                                                       |
| Letters              | `[a-zA-Z]`                                                                    |
| Assignment character | `:=`                                                                          |
| Separator            | `;`                                                                           |
| Construction         | `(` `)`                                                                       |

## Progress

Current version of Milan grammar supports expressions like:
```pascal
BEGIN
    a1 := 1;
    WHILE a1 < 10 DO
        b1 := (a1 / 2) * 2;
        OUTPUT(b1);
        IF a1 == 9 THEN
            WHILE b1 <> 1 DO
                OUTPUT(b1);
                b1 := b1 - 1;
            ENDDO;
        ENDIF;
        a1 := a1 + 1;
    ENDDO;
    OUTPUT(a1);
    OUTPUT(b1);
    c101 := (a1 - 30) * (b1 + 2);
    OUTPUT(c101);
    OUTPUT(65433214);
END
```
## How to Contribute

Fork repository, make changes, send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
$ mvn clean install
```

You will need Maven 3.3+ and Java 17+.