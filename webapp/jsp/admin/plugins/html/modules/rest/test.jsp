<html>
    <head>
        <title>HTML Portlet - REST webservices test page</title>
        <link rel="stylesheet" type="text/css" href="../../../../../../css/portal_admin.css" title="lutece_admin">
    </head>
    <body>


        <script type="text/javascript">
            function onView()
            {
                var id = document.formGet.id_portlet.value;
                var format = document.formGet.format.value;
                document.location='../../../../../../rest/html/portlet/' + id + format;
            }
        </script>
        <div id="content" >
            <h1>HTML Portlet - REST webservices test page </h1>
            <div class="content-box">
            <div class="highlight-box">
                <h2>View WADL</h2>
                <form action="../../../../../../rest/html/wadl">
                    <br/>
                    <input class="button" type="submit" value="View WADL" />
                </form>
            </div>
            <div class="highlight-box">
                <h2>View portlet</h2>
                <form name="formGet">
                    <label for="id">Portlet ID : </label>
                    <input type="text" name="id_portlet" size="5"/>
                    <br/>
                    <label for="format">Format :</label>
                    <select name="format">
                        <option value=".json">JSON</option>
                        <option value=".xml">XML</option>
                    </select>
                    <br/>
                    <input class="button" type="button" value="View" onclick="javascript:onView()"/>
                </form>
            </div>
            <div class="highlight-box">
                <h2>Create a portlet</h2>
                <form action="../../../../../../rest/html/" method="post">
                    <label for="name">Portlet name : </label>
                    <input type="text" name="name" />
                    <br/>
                    <label for="html">HTML :</label>
                    <textarea name="html" cols="80" rows="5"></textarea>
                    <br />
                    <label for="id_page">Page ID : </label>
                    <input type="text" name="id_page"  />
                    <br/>
                    <input class="button" type="submit" value="Create" />
                </form>
            </div>

            <div class="highlight-box">
                <h2>Update a portlet</h2>
                <form action="../../../../../../rest/html/" method="post">
                    <label for="id_portlet">Portlet ID : </label>
                    <input type="text" name="id_portlet" size="5" />
                    <br/>
                    <label for="name">Portlet name : </label>
                    <input type="text" name="name" />
                    <br/>
                    <label for="html">HTML :</label>
                    <textarea name="html" cols="80" rows="5"></textarea>
                    <br />
                    <input class="button" type="submit" value="Modify" />
                </form>
            </div>
        </div>
        </div>
    </body>
</html>
