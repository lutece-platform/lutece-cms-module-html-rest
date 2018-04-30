/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.html.modules.rest.rs;

import fr.paris.lutece.plugins.html.business.portlet.HtmlPortlet;
import fr.paris.lutece.plugins.html.business.portlet.HtmlPortletHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.plugins.rest.util.xml.XMLUtil;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.html.HtmlCleanerException;
import fr.paris.lutece.portal.service.html.HtmlCleanerService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


/**
 * QuizRest
 */
@Path( RestConstants.BASE_PATH + "html" )
/**
 *
 * @author pierre
 */
public class HtmlPortletRest
{
    private static final String TEMPLATE_WADL = "admin/plugins/html/modules/rest/wadl.xml";
    private static final String MARK_BASE_URL = "base_url";
    private static final String HTML = "html";

    @GET
    @Path( "wadl" )
    @Produces( MediaType.APPLICATION_XML )
    public String getWADL( @Context
    HttpServletRequest request )
    {
        String strBase = AppPathService.getBaseUrl( request ) + RestConstants.BASE_PATH + "html";
        Map model = new HashMap(  );
        model.put( MARK_BASE_URL, strBase );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_WADL, request.getLocale(  ), model );

        return t.getHtml(  );
    }

    @GET
    @Path( "/portlet/{id}" )
    @Produces( MediaType.APPLICATION_XML )
    public String getPortletXml( @PathParam( "id" )
    String strId ) throws SiteMessageException
    {
        StringBuilder sbXML = new StringBuilder(  );

        try
        {
            int nId = Integer.parseInt( strId );
            HtmlPortlet portlet = (HtmlPortlet) PortletHome.findByPrimaryKey( nId );

            if ( portlet != null )
            {
                sbXML.append( "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" );
                addPortletXml( sbXML, portlet );
            }
        }
        catch ( NumberFormatException e )
        {
            sbXML.append( XMLUtil.formatError( "Invalid portlet number", 3 ) );
        }
        catch ( Exception e )
        {
            sbXML.append( XMLUtil.formatError( "Portlet not found", 1 ) );
        }

        return sbXML.toString(  );
    }

    @GET
    @Path( "/portlet/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getPortletJson( @PathParam( "id" )
    String strId ) throws SiteMessageException
    {
        String strJSON = "";

        try
        {
            int nId = Integer.parseInt( strId );
            HtmlPortlet portlet = (HtmlPortlet) PortletHome.findByPrimaryKey( nId );

            if ( portlet != null )
            {
                JSONObject json = new JSONObject(  );
                json.accumulate( RestPortletConstants.ID_PORTLET, portlet.getId(  ) );
                json.accumulate( RestPortletConstants.NAME, portlet.getName(  ) );
                json.accumulate( RestPortletConstants.TYPE, portlet.getPortletTypeId(  ) );
                json.accumulate( RestPortletConstants.ID_PAGE, portlet.getPageId(  ) );
                json.accumulate( RestPortletConstants.ID_STYLE, portlet.getStyleId(  ) );
                json.accumulate( RestPortletConstants.COLUMN, portlet.getColumn(  ) );
                json.accumulate( RestPortletConstants.ORDER, portlet.getOrder(  ) );
                json.accumulate( RestPortletConstants.ACCEPT_ALIAS, portlet.getAcceptAlias(  ) );
                json.accumulate( RestPortletConstants.DISPLAY_PORTLET_TITLE, portlet.getDisplayPortletTitle(  ) );
                json.accumulate( HTML, portlet.getHtml(  ) );

                JSONObject jsonPortlet = new JSONObject(  );
                jsonPortlet.accumulate( "portlet", json );
                strJSON = jsonPortlet.toString( 4 );
            }
        }
        catch ( NumberFormatException e )
        {
            strJSON = JSONUtil.formatError( "Invalid portlet number", 3 );
        }
        catch ( Exception e )
        {
            strJSON = JSONUtil.formatError( "Portlet not found", 1 );
        }

        return strJSON;
    }

    @POST
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String createPortlet( @FormParam( "id_portlet" )
    String strId, @FormParam( RestPortletConstants.ID_PAGE )
    String strPageId, @FormParam( RestPortletConstants.NAME )
    String strName, @FormParam( RestPortletConstants.ID_STYLE )
    String strStyleId, @FormParam( RestPortletConstants.COLUMN )
    String strColumn, @FormParam( RestPortletConstants.ORDER )
    String strOrder, @FormParam( RestPortletConstants.ACCEPT_ALIAS )
    String strAcceptAlias, @FormParam( RestPortletConstants.DISPLAY_PORTLET_TITLE )
    String strDisplayPortletTitle, @FormParam( HTML )
    String strHtml ) throws HtmlCleanerException
    {
        boolean bUpdate = ( strId != null );
        HtmlPortlet portlet = null;

        if ( bUpdate )
        {
            portlet = (HtmlPortlet) HtmlPortletHome.findByPrimaryKey( Integer.parseInt( strId ) );
        }
        else
        {
            portlet = new HtmlPortlet(  );
        }

        strPageId = getNotNull( strPageId, "1" );
        strStyleId = getNotNull( strStyleId, "100" );
        strColumn = getNotNull( strColumn, "1" );
        strOrder = getNotNull( strOrder, "1" );

        String strPortletTypeId = "HTML_PORTLET";
        strAcceptAlias = getNotNull( strAcceptAlias, "0" );
        strDisplayPortletTitle = getNotNull( strDisplayPortletTitle, "0" );

        strName = strName.replaceAll( "\"", "" );

        // Check Mandatory fields
        if ( strName.equals( "" ) || ( strStyleId == null ) || ( strStyleId.trim(  ).equals( "" ) ) ||
                strOrder.equals( "" ) || strColumn.equals( "" ) || strStyleId.equals( "" ) ||
                strAcceptAlias.equals( "" ) || strDisplayPortletTitle.equals( "" ) )
        {
            // Error
        }

        int nPageId = Integer.parseInt( strPageId );
        int nOrder = Integer.parseInt( strOrder );
        int nColumn = Integer.parseInt( strColumn );
        int nStyleId = Integer.parseInt( strStyleId );
        int nAcceptAlias = Integer.parseInt( strAcceptAlias );
        int nDisplayPortletTitle = Integer.parseInt( strDisplayPortletTitle );

        portlet.setPageId( nPageId );
        portlet.setName( strName );
        portlet.setOrder( nOrder );
        portlet.setColumn( nColumn );
        portlet.setStyleId( nStyleId );
        portlet.setAcceptAlias( nAcceptAlias );
        portlet.setDisplayPortletTitle( nDisplayPortletTitle );
        portlet.setPortletTypeId( strPortletTypeId );
        portlet.setHtml( HtmlCleanerService.clean( strHtml ) );

        String strReturn;

        if ( bUpdate )
        {
            HtmlPortletHome.getInstance(  ).update( portlet );
            strReturn = "Portlet updated successfully";
        }
        else
        {
            // creates the portlet
            HtmlPortletHome.getInstance(  ).create( portlet );
            strReturn = "Portlet created successfully";
        }

        return strReturn;
    }

    private String getNotNull( String strSrc, String strDefault )
    {
        return ( strSrc != null ) ? strSrc : strDefault;
    }

    private void addPortletXml( StringBuilder sbXML, HtmlPortlet portlet )
    {
        sbXML.append( "<portlet>\n" );
        sbXML.append( "<portlet-name>" ).append( portlet.getName(  ) ).append( "</portlet-name>\n" );
        sbXML.append( "<portlet-id>" ).append( portlet.getId(  ) ).append( "</portlet-id>\n" );
        sbXML.append( "<page-id>" ).append( portlet.getPageId(  ) ).append( "</page-id>\n" );
        sbXML.append( "<display-portlet-title>" ).append( portlet.getDisplayPortletTitle(  ) )
             .append( "</display-portlet-title>\n" );
        sbXML.append( "<column>" ).append( portlet.getColumn(  ) ).append( "</column>\n" );
        sbXML.append( "<order>" ).append( portlet.getOrder(  ) ).append( "</order>\n" );
        sbXML.append( "<portlet-type>" ).append( portlet.getPortletTypeId(  ) ).append( "</portlet-type>\n" );
        sbXML.append( "<style-id>" ).append( portlet.getStyleId(  ) ).append( "</style-id>\n" );
        sbXML.append( "<status>" ).append( portlet.getStatus(  ) ).append( "</status>\n" );
        sbXML.append( "<html-portlet-content>" ).append( portlet.getHtml(  ) ).append( "</html-portlet-content>\n" );
        sbXML.append( "</portlet>" );
    }
}
