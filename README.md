<b>Based on</b>
<img alt="logo" src="https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fcdn.onlinewebfonts.com%2Fsvg%2Fimg_189624.png&f=1&nofb=1&ipt=96f2feb24e716bca2927efe519b26138829643d5931bddb3ba1dec5f51b5e517&ipo=images" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/objectionary/eo)](http://www.rultor.com/p/l3r8yJ/milan)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

**Milan** is a very simple programming language designed to teach students at Volzhskiy Polytechnic Institute.

Current version of language – **JVM** based.

## Grammar

| **Terminals**        | **Values**                                                                                              |
|----------------------|---------------------------------------------------------------------------------------------------------|
| Keywords             | `BEGIN` `DO` `ELSE` `END` `ENDDO` `ENDIF` `IF` `OUTPUT` `READ` `THEN` `WHILE` `SWITCH` `CASE` `DEFAULT` |
| Operands             | `+` `++` `-` `*` `/` `=` `>` `<` `<=` `>=` `<>`                                                         |
| Numbers              | `[0-9]`                                                                                                 |
| Letters              | `[a-z]`                                                                                                 |
| Assignment character | `:=`                                                                                                    |
| Separator            | `;`                                                                                                     |
| Comments             | `//`                                                                                                    |
| Construction         | `(` `)` `{` `}` `:`                                                                                     |

## Progress

Current version of Milan grammar supports expressions like:
```javascript
{
    x = 5;
    y = 10;
    while (x < y) {
        x = x + 1;
    }
}
```
