<?xml version="1.0" encoding="UTF-8"?>

<html xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xsl:version="1.0">
    <head>
        <title> Pixmania Smartphones </title>
        <link href='https://fonts.googleapis.com/css?family=PT+Sans:400,700' rel='stylesheet' type='text/css'/>
        <style>
            body {
                background-color: #111;
                font-family: 'PT Sans', sans-serif;
            }
            table {
                margin: 0 20px;
                border: 1px solid black;
                border-radius: 5px;
                -moz-border-radius: 5px;
            }
            td {
                text-align: center;
                color: white;
            }
            tr:nth-child(even) {
                background-color: #0F607B;
            }
            tr:nth-child(odd) {
                background-color: #374C53;
            }
            .title {
                text-align: center;
                padding: 20px;
                color: white;
            }
            p {
                text-align: center;
                color: white;
            }
        </style>
    </head>
    <body>
        <h1 class="title"> Pixmania Smartphones</h1>
        <table border="1" class="">
            <tr style="background-color: #0f9d58;">
                <td>
                    <h4> Brand </h4>
                </td>
                <td>
                    <h4> Model </h4>
                </td>
                <td>
                    <h4> Processor </h4>
                </td>
                <td>
                    <h4> Screen Technology </h4>
                </td>
                <td>
                    <h4> Screen Size " </h4>
                </td>
                <td>
                    <h4> Screen Size Px </h4>
                </td>
                <td>
                    <h4> Resolution </h4>
                </td>
                <td>
                    <h4> Price </h4>
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
                            <xsl:value-of select="price"/> €
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:for-each>
        </table>
        <p>Produced by Daniel Amaral and Tomás Francisco</p>
    </body>
</html>