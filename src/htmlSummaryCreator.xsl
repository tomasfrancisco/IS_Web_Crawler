<?xml version="1.0" encoding="UTF-8"?>

<html xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xsl:version="1.0">
    <head>
        <title> Pixmania Smartphones </title>
        <link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css"/>
    </head>
    <body>
        <h1> Pixmania Smartphones</h1>
        <table border="1" class="w3-card w3-table w3-blue">
            <tr>
                <td>
                    <b> Brand </b>
                </td>
                <td>
                    <b> Model </b>
                </td>
                <td>
                    <b> Processor </b>
                </td>
                <td>
                    <b> Screen Technology </b>
                </td>
                <td>
                    <b> Screen Size in Inches </b>
                </td>
                <td>
                    <b> Screen Size in Pixels </b>
                </td>
                <td>
                    <b> Resolution </b>
                </td>
                <td>
                    <b> Price </b>
                </td>
            </tr>
            <xsl:for-each select="//smartphonelist">
                <xsl:for-each select="//smartphone">
                    <tr>
                        <td>
                            <xsl:value-of select="brand"/>
                        </td>
                        <td>
                            <xsl:value-of select="model"/>
                        </td>
                        <td>
                            <xsl:value-of select="processor"/>
                        </td>
                        <td>
                            <xsl:value-of select="screenTechnology"/>
                        </td>
                        <td>
                            <xsl:value-of select="screenSizeInches"/>
                        </td>
                        <td>
                            <xsl:value-of select="screenSizePx"/>
                        </td>
                        <td>
                            <xsl:value-of select="resolution"/>
                        </td>
                        <td>
                            <xsl:value-of select="price"/>
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:for-each>
        </table>
    </body>
</html>