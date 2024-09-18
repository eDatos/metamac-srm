<%@page import="org.siemac.metamac.core.common.util.swagger.SwaggerUtils"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="org.siemac.metamac.core.common.util.InternationalizationUtils" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.apache.commons.lang.LocaleUtils" %>
<%@ page import="static java.util.ResourceBundle.Control.getNoFallbackControl" %>
<%@ page import="org.siemac.metamac.core.common.util.MessagesResourceBundle"%>

<%! 

public String queryDescriptionWithParams(String queryDescription, String validOperators, MessagesResourceBundle messagesBundle, String... a) {
    String result = "" + queryDescription + validOperators + messagesBundle.getString("api.doc.swagger.components.validFields");
    for (int i = 0; i < a.length; i++) {
        result += "<li>" + a[i] + "</li>";
    }
    return result + messagesBundle.getString("api.doc.swagger.components.example");    
}
%>

<%
   String locale = InternationalizationUtils.getInstance().getCurrentLocale(request);
   MessagesResourceBundle messagesBundle = new MessagesResourceBundle(locale, "i18n.messages");
   pageContext.setAttribute("msg", messagesBundle);

   String id = messagesBundle.getString("api.doc.swagger.components.id");
   String urn = messagesBundle.getString("api.doc.swagger.components.urn");
   String name = messagesBundle.getString("api.doc.swagger.components.name");
   String parent = messagesBundle.getString("api.doc.swagger.components.parent");
   String description = messagesBundle.getString("api.doc.swagger.components.description");
   String descriptionSource = messagesBundle.getString("api.doc.swagger.components.descriptionSource");
   String validFrom = messagesBundle.getString("api.doc.swagger.components.validFrom");
   String validTo = messagesBundle.getString("api.doc.swagger.components.validTo");
   String lastUpdatedDate = messagesBundle.getString("api.doc.swagger.components.lastUpdatedDate");
   String latest = messagesBundle.getString("api.doc.swagger.components.latest");
   String variableUrn = messagesBundle.getString("api.doc.swagger.components.variableUrn");
   String variableFamilyUrn = messagesBundle.getString("api.doc.swagger.components.variableFamilyUrn");
   String codelistFamilyUrn = messagesBundle.getString("api.doc.swagger.components.codelistFamilyUrn");
   String isRecommended = messagesBundle.getString("api.doc.swagger.components.isRecommended");
   String variableElementUrn = messagesBundle.getString("api.doc.swagger.components.variableElementUrn");
   String codelistUrn = messagesBundle.getString("api.doc.swagger.components.codelistUrn");
   String codelistLatest = messagesBundle.getString("api.doc.swagger.components.codelistLatest");
   String statisticalOperationUrn = messagesBundle.getString("api.doc.swagger.components.statisticalOperationUrn");
   String conceptSchemeLatest = messagesBundle.getString("api.doc.swagger.components.conceptSchemeLatest");
   String acronym = messagesBundle.getString("api.doc.swagger.components.acronym");
   String extendsUrn = messagesBundle.getString("api.doc.swagger.components.extendsUrn");
   String artefactUrn = messagesBundle.getString("api.doc.swagger.components.artefactUrn");
   String relatedConceptUrn = messagesBundle.getString("api.doc.swagger.components.relatedConceptUrn");
   String organisationSchemeUrn = messagesBundle.getString("api.doc.swagger.components.organisationSchemeUrn");
   String type = messagesBundle.getString("api.doc.swagger.components.type");
   String categoryUrn = messagesBundle.getString("api.doc.swagger.components.categoryUrn");
   String categorySchemeUrn = messagesBundle.getString("api.doc.swagger.components.categorySchemeUrn");
   String categorySchemeLatest = messagesBundle.getString("api.doc.swagger.components.categorySchemeLatest");
   String categoryElementCode = messagesBundle.getString("api.doc.swagger.components.categoryElementCode");
   String categoryElementUrn = messagesBundle.getString("api.doc.swagger.components.categoryElementUrn");
   String categoryElementShortName = messagesBundle.getString("api.doc.swagger.components.categoryElementShortName");
   String categoryElementVisible = messagesBundle.getString("api.doc.swagger.components.categoryElementVisible");
   String variableType = messagesBundle.getString("api.doc.swagger.components.variableType");
   String familyUrn = messagesBundle.getString("api.doc.swagger.components.familyUrn");
   String shortName = messagesBundle.getString("api.doc.swagger.components.shortName");
   String geographicalGranularityUrn = messagesBundle.getString("api.doc.swagger.components.geographicalGranularityUrn");
   String organisationSchemeLatest = messagesBundle.getString("api.doc.swagger.components.organisationSchemeLatest");
   String conceptSchemeUrn = messagesBundle.getString("api.doc.swagger.components.conceptSchemeUrn");
   String conceptSchemeStatisticalOperationUrn = messagesBundle.getString("api.doc.swagger.components.conceptSchemeStatisticalOperationUrn");
   String conceptSchemeType = messagesBundle.getString("api.doc.swagger.components.conceptSchemeType");
   String attributeConceptUrn = messagesBundle.getString("api.doc.swagger.components.attributeConceptUrn");
   String dimensionConceptUrn = messagesBundle.getString("api.doc.swagger.components.dimensionConceptUrn");

   String validOperators = messagesBundle.getString("api.doc.swagger.components.validOperators");
   String criteriosDeOrden = messagesBundle.getString("api.doc.swagger.components.criteriosDeOrden");
   String queryDescription = messagesBundle.getString("api.doc.swagger.components.queryDescription");
   String orderByDescription = messagesBundle.getString("api.doc.swagger.components.orderByDescription");

   String exampleOrderBy = messagesBundle.getString("api.doc.swagger.components.exampleOrderBy");
   String validFieldId = messagesBundle.getString("api.doc.swagger.components.validFieldId");
%>

{
    "parameters": {
        "limitParam" : {
            "name":"limit",
            "in":"query",
            "type":"string",
            "description":"${msg['api.doc.swagger.components.parameters.limitParam.description']}"
        },
        "offsetParam":{
              "name":"offset",
              "in":"query",
              "type":"string",
              "description":"${msg['api.doc.swagger.components.parameters.offsetParam.description']}"
        },
        "agencyIdParam":{
              "name":"agencyID",
              "in":"path",
              "type":"string",
              "description":"${msg['api.doc.swagger.components.parameters.agencyIdParam.description']}"
         },
        "resourceIdParam": {
              "name":"resourceID",
              "in":"path",
              "type":"string",
              "description":"${msg['api.doc.swagger.components.parameters.resourceIdParam.description']}"
         },
         "versionParam":  {
              "name":"version",
              "in":"path",
              "type":"string",
              "description":"${msg['api.doc.swagger.components.parameters.versionParam.description']}"
          },
        "codelistQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description": "<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id, urn, name, description, descriptionSource, validFrom, validTo, lastUpdatedDate, latest, variableUrn, variableFamilyUrn, codelistFamilyUrn, isRecommended) %>"
        },
        "codelistCodesQueryParam": {
        "name":"query",
        "in":"query",
        "type":"string",
        "description": "<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id, urn, name, shortName, parent, description, codelistUrn, codelistLatest, geographicalGranularityUrn, variableElementUrn) %>"
        },
        "codelistOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "conceptSchemasQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id,urn,name,description,type,statisticalOperationUrn,validFrom, validTo,lastUpdatedDate,latest) %>"
        },
        "conceptSchemasOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "conceptQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id,urn,name,parent,description,descriptionSource,acronym,extendsUrn,relatedConceptUrn,conceptSchemeUrn,conceptSchemeType,conceptSchemeStatisticalOperationUrn, conceptSchemeLatest) %>"
        },
        "conceptOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "contentConstraintQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id, urn ,name ,artefactUrn ,latest) %>"
        },
        "contentConstraintOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "dataStructuresQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id,urn, name, description, validFrom, validTo, lastUpdatedDate, latest, statisticalOperationUrn,dimensionConceptUrn,attributeConceptUrn) %>"
        },
        "dataStructuresOrderByParam": {
            "name":"orderBy",
            "in":"query",
            "type":"string",
            "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "organizationSchemeQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id, urn ,name , description ,validFrom ,validTo ,lastUpdatedDate ,latest ,type) %>"
        },
        "organizationSchemeOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
         "organizationQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name , parent, description ,organisationSchemeUrn ,organisationSchemeLatest ,type) %>"
        },
        "organizationOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "categorySchemeQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id, urn, name, description, validFrom, validTo, lastUpdatedDate, latest) %>"
        },
        "categorySchemeOrderByParam": {
            "name":"orderBy",
            "in":"query",
            "type":"string",
            "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
         "categorisationQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name ,artefactUrn ,categoryUrn ,latest) %>"
        },
        "categorisationOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
         "categoryQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name , parent, description ,categorySchemeUrn ,categorySchemeLatest, categoryElementCode, categoryElementUrn, categoryElementShortName, categoryElementVisible) %>"
        },
        "categoryOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "variableFamilyQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name) %>"
        },
        "variableFamilyOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "variableQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name ,shortName ,familyUrn ,variableType) %>"
        },
        "variableOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        "variableElementQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name ,geographicalGranularityUrn ,variableType)%>"
        },
        "variableElementOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%= orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
         "categoryElementQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(queryDescription, validOperators, messagesBundle, id ,urn ,name)%>"
        },
        "categoryElementOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%= orderByDescription %><br><%=validFieldId %><%= criteriosDeOrden %><br><%=exampleOrderBy %>"
        },
        
    }
},
