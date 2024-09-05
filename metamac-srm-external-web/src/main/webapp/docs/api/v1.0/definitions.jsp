<%@page import="org.siemac.metamac.core.common.util.swagger.SwaggerUtils"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.siemac.metamac.core.common.util.InternationalizationUtils" %>
<%
   String locale = InternationalizationUtils.getInstance().getCurrentLocale(request);
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="i18n.messages" var="i18n"/>
<fmt:bundle basename="application"/>
	{
      "Error":{
         "type":"object",
         "title":"Error",
         "allOf":[
            {
               "properties":{
                  "code":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Error.properties.code.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "message":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Error.properties.message.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "parameters":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Error.properties.parameters.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ErrorParameters"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Error.description" bundle="${i18n}"/>"
      },
      "ErrorParameters":{
         "type":"object",
         "title":"ErrorParameters",
         "allOf":[
            {
               "properties":{
                  "total":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ErrorParameters.properties.total.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "parameter":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ErrorParameters.properties.parameter.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ErrorParameters.description" bundle="${i18n}"/>"
      },
      "Errors":{
         "type":"object",
         "title":"Errors",
         "allOf":[
            {
               "properties":{
                  "total":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Errors.properties.total.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "error":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Errors.properties.error.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Error"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Errors.description" bundle="${i18n}"/>"
      },
      "Exception":{
         "type":"object",
         "title":"Exception",
         "allOf":[
            {
               "$ref":"#/definitions/Error"
            },
            {
               "properties":{
                  "errors":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Exception.properties.errors.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Errors"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Exception.description" bundle="${i18n}"/>"
      },
      "InternationalString":{
         "type":"object",
         "title":"InternationalString",
         "allOf":[
            {
               "properties":{
                  "text":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.InternationalString.properties.text.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/LocalisedString"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.InternationalString.description" bundle="${i18n}"/>"
      },
      "ListBase":{
         "type":"object",
         "title":"ListBase",
         "allOf":[
            {
               "properties":{
                  "limit":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase.properties.limit.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "offset":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase.properties.offset.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "total":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase.properties.total.description" bundle="${i18n}"/>",
                     "type":"number"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ListBase.description" bundle="${i18n}"/>"
      },
      "LocalisedString":{
         "type":"object",
         "title":"LocalisedString",
         "allOf":[
            {
               "properties":{
                  "lang":{
                     "xml":{
                        "attribute":true,
                        "namespace":"http://www.w3.org/XML/1998/namespace"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalisedString.properties.lang.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "(value)":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalisedString.properties.value.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.LocalisedString.description" bundle="${i18n}"/>"
      },
      "MetamacCriteria":{
         "type":"object",
         "title":"MetamacCriteria",
         "allOf":[
            {
               "properties":{
                  "countTotalResults":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteria.properties.countTotalResults.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "firstResult":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteria.properties.firstResult.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "maximumResultSize":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteria.properties.maximumResultSize.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "ordersBy":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteria.properties.ordersBy.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/MetamacCriteriaOrders"
                  },
                  "restriction":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteria.properties.restriction.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/MetamacCriteriaRestriction"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteria.description" bundle="${i18n}"/>"
      },
      "MetamacCriteriaDisjunctionRestriction":{
         "type":"object",
         "title":"MetamacCriteriaDisjunctionRestriction",
         "allOf":[
            {
               "$ref":"#/definitions/MetamacCriteriaRestriction"
            },
            {
               "properties":{
                  "restrictions":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaDisjunctionRestriction.properties.restrictions.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/MetamacCriteriaRestrictions"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaDisjunctionRestriction.description" bundle="${i18n}"/>"
      },
      "MetamacCriteriaOrder":{
         "type":"object",
         "title":"MetamacCriteriaOrder",
         "allOf":[
            {
               "properties":{
                  "propertyName":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaOrder.properties.propertyName.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "type":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaOrder.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrderType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaOrder.description" bundle="${i18n}"/>"
      },
      "MetamacCriteriaOrders":{
         "type":"object",
         "title":"MetamacCriteriaOrders",
         "allOf":[
            {
               "properties":{
                  "order":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaOrders.properties.order.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/MetamacCriteriaOrder"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaOrders.description" bundle="${i18n}"/>"
      },
      "MetamacCriteriaRestriction":{
         "type":"object",
         "title":"MetamacCriteriaRestriction",
         "allOf":[
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaRestriction.description" bundle="${i18n}"/>"
      },
      "MetamacCriteriaRestrictions":{
         "type":"object",
         "title":"MetamacCriteriaRestrictions",
         "allOf":[
            {
               "properties":{
                  "restriction":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaRestrictions.properties.restriction.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/MetamacCriteriaRestriction"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MetamacCriteriaRestrictions.description" bundle="${i18n}"/>"
      },
      "OrderType":{
         "type":"string",
         "title":"OrderType",
         "enum":[
            "DESC",
            "ASC"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrderType.description" bundle="${i18n}"/>"
      },
      "Resource":{
         "type":"object",
         "title":"Resource",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "name":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "urn":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Resource.description" bundle="${i18n}"/>"
      },
      "Codelist":{
         "type":"object",
         "title":"Codelist",
         "allOf":[
            {
               "$ref":"#/definitions/CodelistType"
            },
            {
               "properties":{
                  "descriptionSource":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.descriptionSource.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "family":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.family.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "isRecommended":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.isRecommended.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "replaceTo":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ReplaceToCodelist"
                  },
                  "replaceToVersion":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.replaceToVersion.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "replacedBy":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.replacedBy.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "replacedByVersion":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.replacedByVersion.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "shortName":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.shortName.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "urnProvider":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.urnProvider.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "variable":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.properties.variable.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Codelist.description" bundle="${i18n}"/>"
      },
      "CodelistFamilies":{
         "type":"object",
         "title":"CodelistFamilies",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "codelistFamily":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamilies.properties.codelistFamily.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CodelistFamily"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamilies.description" bundle="${i18n}"/>"
      },
      "CodelistFamily":{
         "type":"object",
         "title":"CodelistFamily",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urn":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "name":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "urnProvider":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily.properties.urnProvider.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily.description" bundle="${i18n}"/>"
      },
      "Codelists":{
         "type":"object",
         "title":"Codelists",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "codelist":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelists.properties.codelist.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Codelists.description" bundle="${i18n}"/>"
      },
      "CodelistRestrictions":{
         "type":"object",
         "title":"CodelistRestrictions",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "restriction":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistRestrictions.properties.restriction.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodelistRestrictions.description" bundle="${i18n}"/>"
      },
      "ReplaceToCodelist":{
         "type":"object",
         "title":"ReplaceToCodelist",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "replaceTo":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ReplaceToCodelist.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ReplaceToCodelist.description" bundle="${i18n}"/>"
      },
      "ReplaceToVariable":{
         "type":"object",
         "title":"ReplaceToVariable",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "replaceTo":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ReplaceToVariable.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ReplaceToVariable.description" bundle="${i18n}"/>"
      },
      "Variable":{
         "type":"object",
         "title":"Variable",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urn":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "families":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.families.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableFamilyCodes"
                  },
                  "name":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "replaceTo":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ReplaceToVariable"
                  },
                  "replacedBy":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.replacedBy.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "shortName":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.shortName.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "urnProvider":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.urnProvider.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validFrom":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.validFrom.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validTo":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable.properties.validTo.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Variable.description" bundle="${i18n}"/>"
      },
      "VariableFamilies":{
         "type":"object",
         "title":"VariableFamilies",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "variableFamily":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamilies.properties.variableFamily.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableFamily"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamilies.description" bundle="${i18n}"/>"
      },
      "VariableFamily":{
         "type":"object",
         "title":"VariableFamily",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urn":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "name":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "urnProvider":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily.properties.urnProvider.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily.description" bundle="${i18n}"/>"
      },
      "VariableFamilyCodes":{
         "type":"object",
         "title":"VariableFamilyCodes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "family":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamilyCodes.properties.family.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamilyCodes.description" bundle="${i18n}"/>"
      },
      "Variables":{
         "type":"object",
         "title":"Variables",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "variable":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variables.properties.variable.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Variable"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Variables.description" bundle="${i18n}"/>"
      },
      "Agencies":{
         "type":"object",
         "title":"Agencies",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "agency":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Agencies.properties.agency.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Agencies.description" bundle="${i18n}"/>"
      },
      "Agency":{
         "type":"object",
         "title":"Agency",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "$ref":"#/definitions/Organisation"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Agency.description" bundle="${i18n}"/>"
      },
      "AgencyScheme":{
         "type":"object",
         "title":"AgencyScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "$ref":"#/definitions/OrganisationScheme"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AgencyScheme.description" bundle="${i18n}"/>"
      },
      "AgencySchemes":{
         "type":"object",
         "title":"AgencySchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "agencyScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AgencySchemes.properties.agencyScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AgencySchemes.description" bundle="${i18n}"/>"
      },
      "AnnotableArtefact":{
         "type":"object",
         "title":"AnnotableArtefact",
         "allOf":[
            {
               "properties":{
                  "annotations":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotableArtefact.properties.annotations.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Annotations"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AnnotableArtefact.description" bundle="${i18n}"/>"
      },
      "Annotation":{
         "type":"object",
         "title":"Annotation",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Annotation.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "text":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Annotation.properties.text.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "title":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Annotation.properties.title.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Annotation.properties.type.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "url":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Annotation.properties.url.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Annotation.description" bundle="${i18n}"/>"
      },
      "Annotations":{
         "type":"object",
         "title":"Annotations",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "annotation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Annotations.properties.annotation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Annotation"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Annotations.description" bundle="${i18n}"/>"
      },
      "Attribute":{
         "type":"object",
         "title":"Attribute",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Component"
            },
            {
               "$ref":"#/definitions/AttributeBase"
            },
            {
               "properties":{
                  "assignmentStatus":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Attribute.properties.assignmentStatus.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AttributeUsageStatusType"
                  },
                  "attributeRelationship":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Attribute.properties.attributeRelationship.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AttributeRelationship"
                  },
                  "roleConcepts":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Attribute.properties.roleConcepts.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RoleConcepts"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Attribute.description" bundle="${i18n}"/>"
      },
      "AttributeBase":{
         "type":"object",
         "title":"AttributeBase",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Component"
            },
            {
               "properties":{
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AttributeBase.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AttributeQualifierType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AttributeBase.description" bundle="${i18n}"/>"
      },
      "AttributeQualifierType":{
         "type":"string",
         "title":"AttributeQualifierType",
         "enum":[
            "MEASURE",
            "SPATIAL",
            "TIME"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AttributeQualifierType.description" bundle="${i18n}"/>"
      },
      "AttributeRelationship":{
         "type":"object",
         "title":"AttributeRelationship",
         "allOf":[
            {
               "properties":{
                  "attachmentGroup":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AttributeRelationship.properties.attachmentGroup.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "dimension":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AttributeRelationship.properties.dimension.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "group":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AttributeRelationship.properties.group.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "none":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AttributeRelationship.properties.none.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Empty"
                  },
                  "primaryMeasure":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AttributeRelationship.properties.primaryMeasure.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AttributeRelationship.description" bundle="${i18n}"/>"
      },
      "AttributeUsageStatusType":{
         "type":"string",
         "title":"AttributeUsageStatusType",
         "enum":[
            "MANDATORY",
            "CONDITIONAL"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AttributeUsageStatusType.description" bundle="${i18n}"/>"
      },
      "Attributes":{
         "type":"object",
         "title":"Attributes",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Components"
            },
            {
               "properties":{
                  "attribute":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Attributes.properties.attribute.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AttributeBase"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Attributes.description" bundle="${i18n}"/>"
      },
      "Categories":{
         "type":"object",
         "title":"Categories",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "category":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Categories.properties.category.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategoryResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Categories.description" bundle="${i18n}"/>"
      },
      "Categorisation":{
         "type":"object",
         "title":"Categorisation",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Categorisation.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "source":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Categorisation.properties.source.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "target":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Categorisation.properties.target.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":""
      },
      "Categorisations":{
         "type":"object",
         "title":"Categorisations",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "categorisation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Categorisations.properties.categorisation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Categorisations.description" bundle="${i18n}"/>"
      },
      "Category":{
         "type":"object",
         "title":"Category",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Category.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                   "eniSchemaTypes":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Category.properties.eniSchemaTypes.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/EniSchemaTypes"
                  },
                   "categoryElement":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Category.properties.categoryElement.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategoryElementResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Category.description" bundle="${i18n}"/>"
      },
      "EniSchemaTypes":{
         "type":"object",
         "title":"EniSchemaTypes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "eniSchemaType":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.EniSchemaTypes.properties.eniSchemaType.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Item"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.EniSchemaTypes.description" bundle="${i18n}"/>"
      },
      "CategoryScheme":{
         "type":"object",
         "title":"CategoryScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryScheme.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CategoryScheme.description" bundle="${i18n}"/>"
      },
      "CategorySchemes":{
         "type":"object",
         "title":"CategorySchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "categoryScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategorySchemes.properties.categoryScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CategorySchemes.description" bundle="${i18n}"/>"
      },
      "Code":{
         "type":"object",
         "title":"Code",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Code.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "shortName":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Code.properties.shortName.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "extended":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Code.properties.extended.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Code.description" bundle="${i18n}"/>"
      },
      "CodeResource":{
         "type":"object",
         "title":"CodeResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "$ref":"#/definitions/ItemResource"
            },
            {
               "properties":{
                  "open":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeResource.properties.open.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "order":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeResource.properties.order.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "variableElement":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeResource.properties.variableElement.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "extended":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeResource.properties.extended.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodeResource.description" bundle="${i18n}"/>"
      },
        "CategoryResource":{
         "type":"object",
         "title":"CategoryResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "$ref":"#/definitions/ItemResource"
            },
            {
               "properties":{
                 "eniSchemaTypes":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryResource.properties.eniSchemaTypes.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/EniSchemaTypes"
                  },
                "categoryElement":{
                    "xml":{
                       "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                    },
                    "description":"<fmt:message key="api.doc.swagger.definitions.CategoryResource.properties.categoryElement.description" bundle="${i18n}"/>",
                    "$ref":"#/definitions/CategoryElementResource"
                  },
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CategoryResource.description" bundle="${i18n}"/>"
      },
      "RecodedCodeResource":{
         "type":"object",
         "title":"RecodedCodeResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "$ref":"#/definitions/ItemResource"
            },
            {
               "properties":{
                  "recodedId":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.RecodedCodeResource.properties.recodedId.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.RecodedCodeResource.description" bundle="${i18n}"/>"
      },
      "Codelist":{
         "type":"object",
         "title":"Codelist",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "descriptionSource":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.descriptionSource.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "isRecommended":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.isRecommended.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "opennessConfigurations":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.opennessConfigurations.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VisualisationConfigurations"
                  },
                  "orderConfigurations":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.orderConfigurations.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VisualisationConfigurations"
                  },
                  "replaceTo":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ReplaceToResources"
                  },
                  "shortName":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.shortName.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "variable":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.properties.variable.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Codelist_.description" bundle="${i18n}"/>"
      },
      "CodelistFamilies":{
         "type":"object",
         "title":"CodelistFamilies",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "codelistFamily":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamilies_.properties.codelistFamily.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamilies_.description" bundle="${i18n}"/>"
      },
      "CodelistFamily":{
         "type":"object",
         "title":"CodelistFamily",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily_.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodelistFamily_.description" bundle="${i18n}"/>"
      },
      "Codelists":{
         "type":"object",
         "title":"Codelists",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "codelist":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codelists_.properties.codelist.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Codelists_.description" bundle="${i18n}"/>"
      },
      "Codes":{
         "type":"object",
         "title":"Codes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "code":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Codes.properties.code.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CodeResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Codes.description" bundle="${i18n}"/>"
      },
      "RecodedCodes":{
         "type":"object",
         "title":"RecodedCodes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "code":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.RecodedCodes.properties.code.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RecodedCodeResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.RecodedCodes.description" bundle="${i18n}"/>"
      },
      "Component":{
         "type":"object",
         "title":"Component",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "properties":{
                  "conceptIdentity":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Component.properties.conceptIdentity.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "localRepresentation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Component.properties.localRepresentation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Representation"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Component.description" bundle="${i18n}"/>"
      },
      "Components":{
         "type":"object",
         "title":"Components",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Components.description" bundle="${i18n}"/>"
      },
      "Concept":{
         "type":"object",
         "title":"Concept",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "acronym":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.acronym.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "context":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.context.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "coreRepresentation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.coreRepresentation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Representation"
                  },
                  "derivation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.derivation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "descriptionSource":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.descriptionSource.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "docMethod":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.docMethod.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "extends":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.extends.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "legalActs":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.legalActs.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "pluralName":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.pluralName.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "quantity":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.quantity.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Quantity"
                  },
                  "relatedConcepts":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.relatedConcepts.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RelatedConcepts"
                  },
                  "roles":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.roles.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RoleConcepts"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Item"
                  },
                  "variable":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concept.properties.variable.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Concept.description" bundle="${i18n}"/>"
      },
      "ConceptResource":{
         "type":"object",
         "title":"ConceptResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "$ref":"#/definitions/ItemResource"
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ConceptResource.description" bundle="${i18n}"/>"
      },
      "ConceptScheme":{
         "type":"object",
         "title":"ConceptScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ConceptScheme.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "statisticalOperation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ConceptScheme.properties.statisticalOperation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ConceptScheme.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptSchemeType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ConceptScheme.description" bundle="${i18n}"/>"
      },
      "ConceptSchemeType":{
         "type":"string",
         "title":"ConceptSchemeType",
         "enum":[
            "GLOSSARY",
            "TRANSVERSAL",
            "OPERATION",
            "ROLE",
            "MEASURE"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ConceptSchemeType.description" bundle="${i18n}"/>"
      },
      "ConceptSchemes":{
         "type":"object",
         "title":"ConceptSchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "conceptScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ConceptSchemes.properties.conceptScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ConceptSchemes.description" bundle="${i18n}"/>"
      },
      "ConceptTypes":{
         "type":"object",
         "title":"ConceptTypes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "conceptType":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ConceptTypes.properties.conceptType.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Item"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ConceptTypes.description" bundle="${i18n}"/>"
      },
      "Concepts":{
         "type":"object",
         "title":"Concepts",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "concept":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Concepts.properties.concept.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Concepts.description" bundle="${i18n}"/>"
      },
      "Contact":{
         "type":"object",
         "title":"Contact",
         "allOf":[
            {
               "properties":{
                  "emails":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.emails.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "faxes":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.faxes.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "id":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "name":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "organisationUnit":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.organisationUnit.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "responsibility":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.responsibility.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "telephones":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.telephones.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urls":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contact.properties.urls.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Contact.description" bundle="${i18n}"/>"
      },
      "Contacts":{
         "type":"object",
         "title":"Contacts",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "contact":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Contacts.properties.contact.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Contact"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Contacts.description" bundle="${i18n}"/>"
      },
      "ContentConstraint":{
         "type":"object",
         "title":"ContentConstraint",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraint.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "constraintAttachment":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraint.properties.constraintAttachment.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "regions":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraint.properties.regions.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Regions"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraint.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ContentConstraintType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraint.description" bundle="${i18n}"/>"
      },
      "ContentConstraintType":{
         "type":"string",
         "title":"ContentConstraintType",
         "enum":[
            "ALLOWED",
            "ACTUAL"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraintType.description" bundle="${i18n}"/>"
      },
      "ContentConstraints":{
         "type":"object",
         "title":"ContentConstraints",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "contentConstraint":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraints.properties.contentConstraint.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ContentConstraints.description" bundle="${i18n}"/>"
      },
      "DataConsumer":{
         "type":"object",
         "title":"DataConsumer",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "$ref":"#/definitions/Organisation"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataConsumer.description" bundle="${i18n}"/>"
      },
      "DataConsumerScheme":{
         "type":"object",
         "title":"DataConsumerScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "$ref":"#/definitions/OrganisationScheme"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataConsumerScheme.description" bundle="${i18n}"/>"
      },
      "DataConsumerSchemes":{
         "type":"object",
         "title":"DataConsumerSchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dataConsumerScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataConsumerSchemes.properties.dataConsumerScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataConsumerSchemes.description" bundle="${i18n}"/>"
      },
      "DataConsumers":{
         "type":"object",
         "title":"DataConsumers",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dataConsumer":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataConsumers.properties.dataConsumer.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataConsumers.description" bundle="${i18n}"/>"
      },
      "DataProvider":{
         "type":"object",
         "title":"DataProvider",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "$ref":"#/definitions/Organisation"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataProvider.description" bundle="${i18n}"/>"
      },
      "DataProviderScheme":{
         "type":"object",
         "title":"DataProviderScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "$ref":"#/definitions/OrganisationScheme"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataProviderScheme.description" bundle="${i18n}"/>"
      },
      "DataProviderSchemes":{
         "type":"object",
         "title":"DataProviderSchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dataProviderScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataProviderSchemes.properties.dataProviderScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataProviderSchemes.description" bundle="${i18n}"/>"
      },
      "DataProviders":{
         "type":"object",
         "title":"DataProviders",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dataProvider":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataProviders.properties.dataProvider.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataProviders.description" bundle="${i18n}"/>"
      },
      "DataStructure":{
         "type":"object",
         "title":"DataStructure",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "autoOpen":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.autoOpen.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "dataStructureComponents":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.dataStructureComponents.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataStructureComponents"
                  },
                  "dimensionVisualisations":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.dimensionVisualisations.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionVisualisations"
                  },
                  "heading":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.heading.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionReferences"
                  },
                  "showDecimals":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.showDecimals.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "showDecimalsPrecisions":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.showDecimalsPrecisions.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ShowDecimalPrecisions"
                  },
                  "statisticalOperation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.statisticalOperation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "stub":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.properties.stub.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionReferences"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataStructure.description" bundle="${i18n}"/>"
      },
      "DataStructureComponents":{
         "type":"object",
         "title":"DataStructureComponents",
         "allOf":[
            {
               "properties":{
                  "attributes":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructureComponents.properties.attributes.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Attributes"
                  },
                  "dimensions":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructureComponents.properties.dimensions.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Dimensions"
                  },
                  "groups":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructureComponents.properties.groups.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Groups"
                  },
                  "measure":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructureComponents.properties.measure.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Measure"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataStructureComponents.description" bundle="${i18n}"/>"
      },
      "DataStructures":{
         "type":"object",
         "title":"DataStructures",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dataStructure":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DataStructures.properties.dataStructure.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataStructures.description" bundle="${i18n}"/>"
      },
      "DataType":{
         "type":"string",
         "title":"DataType",
         "enum":[
            "STRING",
            "ALPHA",
            "ALPHA_NUMERIC",
            "NUMERIC",
            "BIG_INTEGER",
            "INTEGER",
            "LONG",
            "SHORT",
            "DECIMAL",
            "FLOAT",
            "DOUBLE",
            "BOOLEAN",
            "URI",
            "COUNT",
            "INCLUSIVE_VALUE_RANGE",
            "EXCLUSIVE_VALUE_RANGE",
            "INCREMENTAL",
            "OBSERVATIONAL_TIME_PERIOD",
            "STANDARD_TIME_PERIOD",
            "BASIC_TIME_PERIOD",
            "GREGORIAN_TIME_PERIOD",
            "GREGORIAN_YEAR",
            "GREGORIAN_YEAR_MONTH",
            "GREGORIAN_DAY",
            "REPORTING_TIME_PERIOD",
            "REPORTING_YEAR",
            "REPORTING_SEMESTER",
            "REPORTING_TRIMESTER",
            "REPORTING_QUARTER",
            "REPORTING_MONTH",
            "REPORTING_WEEK",
            "REPORTING_DAY",
            "DATE_TIME",
            "TIME_RANGE",
            "MONTH",
            "MONTH_DAY",
            "DAY",
            "TIME",
            "XHTML"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DataType.description" bundle="${i18n}"/>"
      },
      "DimensionBase":{
         "type":"object",
         "title":"DimensionBase",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Component"
            },
            {
               "properties":{
                  "type":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DimensionBase.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DimensionBase.description" bundle="${i18n}"/>"
      },
      "DimensionReferences":{
         "type":"object",
         "title":"DimensionReferences",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dimension":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DimensionReferences.properties.dimension.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DimensionReferences.description" bundle="${i18n}"/>"
      },
      "DimensionType":{
         "type":"string",
         "title":"DimensionType",
         "enum":[
            "DIMENSION",
            "MEASURE_DIMENSION",
            "TIME_DIMENSION"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DimensionType.description" bundle="${i18n}"/>"
      },
      "DimensionVisualisation":{
         "type":"object",
         "title":"DimensionVisualisation",
         "allOf":[
            {
               "properties":{
                  "dimension":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DimensionVisualisation.properties.dimension.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DimensionVisualisation.description" bundle="${i18n}"/>"
      },
      "DimensionVisualisations":{
         "type":"object",
         "title":"DimensionVisualisations",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "dimensionVisualisation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.DimensionVisualisations.properties.dimensionVisualisation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionVisualisation"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.DimensionVisualisations.description" bundle="${i18n}"/>"
      },
      "Dimensions":{
         "type":"object",
         "title":"Dimensions",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Components"
            },
            {
               "properties":{
                  "dimension":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Dimensions.properties.dimension.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionBase"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Dimensions.description" bundle="${i18n}"/>"
      },
      "Empty":{
         "type":"object",
         "title":"Empty",
         "allOf":[
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Empty.description" bundle="${i18n}"/>"
      },
      "Group":{
         "type":"object",
         "title":"Group",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Components"
            },
            {
               "properties":{
                  "dimensions":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Group.properties.dimensions.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DimensionReferences"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Group.description" bundle="${i18n}"/>"
      },
      "Groups":{
         "type":"object",
         "title":"Groups",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "group":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Groups.properties.group.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Group"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Groups.description" bundle="${i18n}"/>"
      },
      "IdentifiableArtefact":{
         "type":"object",
         "title":"IdentifiableArtefact",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "properties":{
                  "childLinks":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.childLinks.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ChildLinks"
                  },
                  "id":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "managementAppLink":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.managementAppLink.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "nestedId":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.nestedId.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "parentLink":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.parentLink.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ResourceLink"
                  },
                  "selfLink":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.selfLink.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ResourceLink"
                  },
                  "uri":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.uri.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urn":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urnProvider":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.properties.urnProvider.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.IdentifiableArtefact.description" bundle="${i18n}"/>"
      },
      "Item":{
         "type":"object",
         "title":"Item",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "properties":{
                  "parent":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Item.properties.parent.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Item.description" bundle="${i18n}"/>"
      },
      "ItemResource":{
         "type":"object",
         "title":"ItemResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "properties":{
                  "parent":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ItemResource.properties.parent.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ItemResource.description" bundle="${i18n}"/>"
      },
      "ItemScheme":{
         "type":"object",
         "title":"ItemScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "properties":{
                  "isPartial":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ItemScheme.properties.isPartial.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ItemScheme.description" bundle="${i18n}"/>"
      },
      "Key":{
         "type":"object",
         "title":"Key",
         "allOf":[
            {
               "properties":{
                  "included":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Key.properties.included.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "keyParts":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Key.properties.keyParts.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/KeyParts"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Key.description" bundle="${i18n}"/>"
      },
      "KeyPart":{
         "type":"object",
         "title":"KeyPart",
         "allOf":[
            {
               "properties":{
                  "afterPeriod":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.afterPeriod.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "afterPeriodInclusive":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.afterPeriodInclusive.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "beforePeriod":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.beforePeriod.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "beforePeriodInclusive":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.beforePeriodInclusive.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "cascadeValues":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.cascadeValues.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "endPeriod":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.endPeriod.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "endPeriodInclusive":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.endPeriodInclusive.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "identifier":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.identifier.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "position":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.position.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "startPeriod":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.startPeriod.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "startPeriodInclusive":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.startPeriodInclusive.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/KeyPartType"
                  },
                  "value":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.properties.value.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.KeyPart.description" bundle="${i18n}"/>"
      },
      "KeyPartType":{
         "type":"string",
         "title":"KeyPartType",
         "enum":[
            "TIME_RANGE",
            "NORMAL"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.KeyPartType.description" bundle="${i18n}"/>"
      },
      "KeyParts":{
         "type":"object",
         "title":"KeyParts",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "keyPart":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.KeyParts.properties.keyPart.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/KeyPart"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.KeyParts.description" bundle="${i18n}"/>"
      },
      "Keys":{
         "type":"object",
         "title":"Keys",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "key":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Keys.properties.key.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Key"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Keys.description" bundle="${i18n}"/>"
      },
      "LifeCycle":{
         "type":"object",
         "title":"LifeCycle",
         "allOf":[
            {
               "properties":{
                  "lastUpdatedDate":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LifeCycle.properties.lastUpdatedDate.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.LifeCycle.description" bundle="${i18n}"/>"
      },
      "MaintainableArtefact":{
         "type":"object",
         "title":"MaintainableArtefact",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "properties":{
                  "agencyID":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.properties.agencyID.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "isExternalReference":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.properties.isExternalReference.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "isFinal":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.properties.isFinal.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "lifeCycle":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.properties.lifeCycle.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/LifeCycle"
                  },
                  "serviceUrl":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.properties.serviceUrl.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "structureUrl":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.properties.structureUrl.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MaintainableArtefact.description" bundle="${i18n}"/>"
      },
      "Measure":{
         "type":"object",
         "title":"Measure",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Components"
            },
            {
               "properties":{
                  "primaryMeasure":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Measure.properties.primaryMeasure.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/PrimaryMeasure"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Measure.description" bundle="${i18n}"/>"
      },
      "MeasureDimension":{
         "type":"object",
         "title":"MeasureDimension",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Component"
            },
            {
               "$ref":"#/definitions/DimensionBase"
            },
            {
               "properties":{
                  "roleConcepts":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.MeasureDimension.properties.roleConcepts.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RoleConcepts"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.MeasureDimension.description" bundle="${i18n}"/>"
      },
      "NameableArtefact":{
         "type":"object",
         "title":"NameableArtefact",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "properties":{
                  "description":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.NameableArtefact.properties.description.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "name":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.NameableArtefact.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.NameableArtefact.description" bundle="${i18n}"/>"
      },
      "Organisation":{
         "type":"object",
         "title":"Organisation",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Organisation.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "contacts":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Organisation.properties.contacts.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Contacts"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Organisation.description" bundle="${i18n}"/>"
      },
      "OrganisationScheme":{
         "type":"object",
         "title":"OrganisationScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationScheme.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationScheme.description" bundle="${i18n}"/>"
      },
      "OrganisationSchemes":{
         "type":"object",
         "title":"OrganisationSchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "organisationScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationSchemes.properties.organisationScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationSchemes.description" bundle="${i18n}"/>"
      },
      "OrganisationUnit":{
         "type":"object",
         "title":"OrganisationUnit",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/Item"
            },
            {
               "$ref":"#/definitions/Organisation"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationUnit.description" bundle="${i18n}"/>"
      },
      "OrganisationUnitScheme":{
         "type":"object",
         "title":"OrganisationUnitScheme",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "$ref":"#/definitions/VersionableArtefact"
            },
            {
               "$ref":"#/definitions/MaintainableArtefact"
            },
            {
               "$ref":"#/definitions/ItemScheme"
            },
            {
               "$ref":"#/definitions/OrganisationScheme"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationUnitScheme.description" bundle="${i18n}"/>"
      },
      "OrganisationUnitSchemes":{
         "type":"object",
         "title":"OrganisationUnitSchemes",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "organisationUnitScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationUnitSchemes.properties.organisationUnitScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationUnitSchemes.description" bundle="${i18n}"/>"
      },
      "OrganisationUnits":{
         "type":"object",
         "title":"OrganisationUnits",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "organisationUnit":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationUnits.properties.organisationUnit.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.OrganisationUnits.description" bundle="${i18n}"/>"
      },
      "Organisations":{
         "type":"object",
         "title":"Organisations",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "organisation":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Organisations.properties.organisation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Organisations.description" bundle="${i18n}"/>"
      },
      "PrimaryMeasure":{
         "type":"object",
         "title":"PrimaryMeasure",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/Component"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.PrimaryMeasure.description" bundle="${i18n}"/>"
      },
      "Quantity":{
         "type":"object",
         "title":"Quantity",
         "allOf":[
            {
               "properties":{
                  "decimalPlaces":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Quantity.properties.decimalPlaces.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "significantDigits":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Quantity.properties.significantDigits.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "unitCode":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Quantity.properties.unitCode.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "unitMultiplier":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Quantity.properties.unitMultiplier.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "unitSymbolPosition":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Quantity.properties.unitSymbolPosition.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/QuantityUnitSymbolPosition"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Quantity.description" bundle="${i18n}"/>"
      },
      "QuantityAmount":{
         "type":"object",
         "title":"QuantityAmount",
         "allOf":[
            {
               "$ref":"#/definitions/Quantity"
            },
            {

            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.QuantityAmount.description" bundle="${i18n}"/>"
      },
      "QuantityUnitSymbolPosition":{
         "type":"string",
         "title":"QuantityUnitSymbolPosition",
         "enum":[
            "START",
            "END"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.QuantityUnitSymbolPosition.description" bundle="${i18n}"/>"
      },
      "Region":{
         "type":"object",
         "title":"Region",
         "allOf":[
            {
               "properties":{
                  "code":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Region.properties.code.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "keys":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Region.properties.keys.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Keys"
                  },
                  "regionValueType":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Region.properties.regionValueType.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RegionValueType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Region.description" bundle="${i18n}"/>"
      },
      "RegionReference":{
         "type":"object",
         "title":"RegionReference",
         "allOf":[
            {
               "$ref":"#/definitions/Region"
            },
            {
               "properties":{
                  "contentConstraintUrn":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.RegionReference.properties.contentConstraintUrn.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.RegionReference.description" bundle="${i18n}"/>"
      },
      "RegionValueType":{
         "type":"string",
         "title":"RegionValueType",
         "enum":[
            "KEY_SET",
            "CUBE"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.RegionValueType.description" bundle="${i18n}"/>"
      },
      "Regions":{
         "type":"object",
         "title":"Regions",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "region":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Regions.properties.region.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Region"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Regions.description" bundle="${i18n}"/>"
      },
      "RelatedConcepts":{
         "type":"object",
         "title":"RelatedConcepts",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "relatedConcept":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.RelatedConcepts.properties.relatedConcept.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.RelatedConcepts.description" bundle="${i18n}"/>"
      },
      "ReplaceToResources":{
         "type":"object",
         "title":"ReplaceToResources",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "replaceTo":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ReplaceToResources.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ReplaceToResources.description" bundle="${i18n}"/>"
      },
      "Representation":{
         "type":"object",
         "title":"Representation",
         "allOf":[
            {
               "properties":{
                  "enumerationCodelist":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Representation.properties.enumerationCodelist.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "enumerationConceptScheme":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Representation.properties.enumerationConceptScheme.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "textFormat":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Representation.properties.textFormat.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/TextFormat"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Representation.description" bundle="${i18n}"/>"
      },
      "RoleConcepts":{
         "type":"object",
         "title":"RoleConcepts",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "role":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.RoleConcepts.properties.role.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.RoleConcepts.description" bundle="${i18n}"/>"
      },
      "ShowDecimalPrecision":{
         "type":"object",
         "title":"ShowDecimalPrecision",
         "allOf":[
            {
               "properties":{
                  "concept":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ShowDecimalPrecision.properties.concept.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "showDecimals":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ShowDecimalPrecision.properties.showDecimals.description" bundle="${i18n}"/>",
                     "type":"number"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ShowDecimalPrecision.description" bundle="${i18n}"/>"
      },
      "ShowDecimalPrecisions":{
         "type":"object",
         "title":"ShowDecimalPrecisions",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "showDecimalPrecision":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ShowDecimalPrecisions.properties.showDecimalPrecision.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ShowDecimalPrecision"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ShowDecimalPrecisions.description" bundle="${i18n}"/>"
      },
      "TextFormat":{
         "type":"object",
         "title":"TextFormat",
         "allOf":[
            {
               "properties":{
                  "decimals":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.decimals.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "endTime":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.endTime.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "endValue":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.endValue.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "interval":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.interval.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "isMultiLingual":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.isMultiLingual.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "isSequence":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.isSequence.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "maxLength":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.maxLength.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "maxValue":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.maxValue.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "minLength":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.minLength.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "minValue":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.minValue.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "pattern":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.pattern.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "startTime":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.startTime.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "startValue":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.startValue.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "textType":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.textType.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataType"
                  },
                  "timeInterval":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.properties.timeInterval.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.TextFormat.description" bundle="${i18n}"/>"
      },
      "Variable":{
         "type":"object",
         "title":"Variable",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "families":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.properties.families.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableFamiliesMetadata"
                  },
                  "replaceTo":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ReplaceToResources"
                  },
                  "replacedBy":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.properties.replacedBy.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "shortName":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.properties.shortName.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.properties.type.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Variable_.description" bundle="${i18n}"/>"
      },
      "VariableElement":{
         "type":"object",
         "title":"VariableElement",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElement.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "replaceTo":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElement.properties.replaceTo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ReplaceToResources"
                  },
                  "replacedBy":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElement.properties.replacedBy.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "variable":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElement.properties.variable.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  },
                  "renderingColor":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElement.properties.renderingColor.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElement.description" bundle="${i18n}"/>"
      },
      "VariableElements":{
         "type":"object",
         "title":"VariableElements",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "variableElement":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElements.properties.variableElement.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElementResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElements.description" bundle="${i18n}"/>"
      },      
      "VariableElementResource":{
         "type":"object",
         "title":"VariableElementResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "properties":{
                  "geographicalGranularity":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementResource.properties.geographicalGranularity.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "renderingColor":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementResource.properties.renderingColor.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementResource.description" bundle="${i18n}"/>"
      },
      "VariableElementsGeoInfo":{
         "type":"object",
         "title":"VariableElementsGeoInfo",
         "allOf":[
            {
               "properties":{
                  "features":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfo.properties.features.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElementsGeoInfoFeatures"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfo.properties.type.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfo.description" bundle="${i18n}"/>"
      },
      "VariableElementsGeoInfoFeature":{
         "type":"object",
         "title":"VariableElementsGeoInfoFeature",
         "allOf":[
            {
               "properties":{
                  "geometryWKT":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeature.properties.geometryWKT.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "id":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeature.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "properties":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeature.properties.properties.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElementsGeoInfoFeatureProperties"
                  },
                  "type":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeature.properties.type.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeature.description" bundle="${i18n}"/>"
      },
      "VariableElementsGeoInfoFeatureProperties":{
         "type":"object",
         "title":"VariableElementsGeoInfoFeatureProperties",
         "allOf":[
            {
               "properties":{
                  "geographicalGranularity":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatureProperties.properties.geographicalGranularity.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemResource"
                  },
                  "lastUpdatedDate":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatureProperties.properties.lastUpdatedDate.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "latitude":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatureProperties.properties.latitude.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "longitude":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatureProperties.properties.longitude.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "urn":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatureProperties.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatureProperties.description" bundle="${i18n}"/>"
      },
      "VariableElementsGeoInfoFeatures":{
         "type":"object",
         "title":"VariableElementsGeoInfoFeatures",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "feature":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatures.properties.feature.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElementsGeoInfoFeature"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableElementsGeoInfoFeatures.description" bundle="${i18n}"/>"
      },
      "VariableFamilies":{
         "type":"object",
         "title":"VariableFamilies",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "variableFamily":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamilies_.properties.variableFamily.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamilies_.description" bundle="${i18n}"/>"
      },
      "VariableFamiliesMetadata":{
         "type":"object",
         "title":"VariableFamiliesMetadata",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "family":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamiliesMetadata.properties.family.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamiliesMetadata.description" bundle="${i18n}"/>"
      },
      "VariableFamily":{
         "type":"object",
         "title":"VariableFamily",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily_.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableFamily_.description" bundle="${i18n}"/>"
      },
      "VariableType":{
         "type":"string",
         "title":"VariableType",
         "enum":[
            "GEOGRAPHICAL"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VariableType.description" bundle="${i18n}"/>"
      },
      "Variables":{
         "type":"object",
         "title":"Variables",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "variable":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Variables_.properties.variable.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Resource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Variables_.description" bundle="${i18n}"/>"
      },
      "VersionableArtefact":{
         "type":"object",
         "title":"VersionableArtefact",
         "allOf":[
            {
               "$ref":"#/definitions/AnnotableArtefact"
            },
            {
               "$ref":"#/definitions/IdentifiableArtefact"
            },
            {
               "$ref":"#/definitions/NameableArtefact"
            },
            {
               "properties":{
                  "replaceToVersion":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VersionableArtefact.properties.replaceToVersion.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "replacedByVersion":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VersionableArtefact.properties.replacedByVersion.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validFrom":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VersionableArtefact.properties.validFrom.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validTo":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VersionableArtefact.properties.validTo.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "version":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VersionableArtefact.properties.version.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VersionableArtefact.description" bundle="${i18n}"/>"
      },
      "VisualisationConfiguration":{
         "type":"object",
         "title":"VisualisationConfiguration",
         "allOf":[
            {
               "$ref":"#/definitions/Item"
            },
            {
               "properties":{
                  "default":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VisualisationConfiguration.properties.default.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VisualisationConfiguration.description" bundle="${i18n}"/>"
      },
      "VisualisationConfigurations":{
         "type":"object",
         "title":"VisualisationConfigurations",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "visualisationConfiguration":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.VisualisationConfigurations.properties.visualisationConfiguration.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VisualisationConfiguration"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.VisualisationConfigurations.description" bundle="${i18n}"/>"
      },
      "QueryTextType":{
         "type":"object",
         "title":"QueryTextType",
         "allOf":[
            {
               "$ref":"#/definitions/TextType"
            },
            {
               "properties":{
                  "operator":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.QueryTextType.properties.operator.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.QueryTextType.description" bundle="${i18n}"/>"
      },
      "CodeType":{
         "type":"object",
         "title":"CodeType",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "uri":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.uri.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urn":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "Annotations":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.Annotations.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AnnotationsType"
                  },
                  "Description":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.Description.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/TextType"
                  },
                  "Name":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.Name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/TextType"
                  },
                  "Parent":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.properties.Parent.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/LocalCodeReferenceType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodeType.description" bundle="${i18n}"/>"
      },
      "CodelistType":{
         "type":"object",
         "title":"CodelistType",
         "allOf":[
            {
               "properties":{
                  "agencyID":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.agencyID.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "isExternalReference":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.isExternalReference.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "isFinal":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.isFinal.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "isPartial":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.isPartial.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "serviceURL":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.serviceURL.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "structureURL":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.structureURL.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "uri":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.uri.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "urn":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validFrom":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.validFrom.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validTo":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.validTo.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "version":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.version.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "Annotations":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.Annotations.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AnnotationsType"
                  },
                  "Code":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.Code.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CodeType"
                  },
                  "Description":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.Description.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/TextType"
                  },
                  "Name":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.properties.Name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/TextType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CodelistType.description" bundle="${i18n}"/>"
      },
      "ChildLinks":{
         "type":"object",
         "title":"ChildLinks",
         "allOf":[
            {
               "properties":{
                  "total":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ChildLinks.properties.total.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "childLink":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ChildLinks.properties.childLink.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ResourceLink"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ChildLinks.description" bundle="${i18n}"/>"
      },
      "InternationalString":{
         "type":"object",
         "title":"InternationalString",
         "allOf":[
            {
               "properties":{
                  "text":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.InternationalString_.properties.text.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/LocalisedString"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.InternationalString_.description" bundle="${i18n}"/>"
      },
      "Item":{
         "type":"object",
         "title":"Item",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Item_.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "name":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Item_.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Item_.description" bundle="${i18n}"/>"
      },
      "ListBase":{
         "type":"object",
         "title":"ListBase",
         "allOf":[
            {
               "properties":{
                  "firstLink":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.firstLink.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "lastLink":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.lastLink.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "limit":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.limit.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "nextLink":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.nextLink.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "offset":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.offset.description" bundle="${i18n}"/>",
                     "type":"number"
                  },
                  "previousLink":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.previousLink.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "selfLink":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.selfLink.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "total":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.properties.total.description" bundle="${i18n}"/>",
                     "type":"number"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ListBase_.description" bundle="${i18n}"/>"
      },
      "LocalisedString":{
         "type":"object",
         "title":"LocalisedString",
         "allOf":[
            {
               "properties":{
                  "lang":{
                     "xml":{
                        "attribute":true,
                        "namespace":"http://www.w3.org/XML/1998/namespace"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalisedString_.properties.lang.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "(value)":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalisedString_.properties.value.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.LocalisedString_.description" bundle="${i18n}"/>"
      },
      "Resource":{
         "type":"object",
         "title":"Resource",
         "allOf":[
            {
               "properties":{
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "id":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "name":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.properties.name.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/InternationalString"
                  },
                  "nestedId":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.properties.nestedId.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "selfLink":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.properties.selfLink.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ResourceLink"
                  },
                  "urn":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/common/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.properties.urn.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.Resource_.description" bundle="${i18n}"/>"
      },
      "ResourceLink":{
         "type":"object",
         "title":"ResourceLink",
         "allOf":[
            {
               "properties":{
                  "href":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ResourceLink.properties.href.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "kind":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.ResourceLink.properties.kind.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ResourceLink.description" bundle="${i18n}"/>"
      },
      "AnnotationType":{
         "type":"object",
         "title":"AnnotationType",
         "allOf":[
            {
               "properties":{
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationType.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "AnnotationText":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationType.properties.AnnotationText.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/TextType"
                  },
                  "AnnotationTitle":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationType.properties.AnnotationTitle.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "AnnotationType":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationType.properties.AnnotationType.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "AnnotationURL":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationType.properties.AnnotationURL.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationType.description" bundle="${i18n}"/>"
      },
      "AnnotationsType":{
         "type":"object",
         "title":"AnnotationsType",
         "allOf":[
            {
               "properties":{
                  "Annotation":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationsType.properties.Annotation.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AnnotationType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.AnnotationsType.description" bundle="${i18n}"/>"
      },
      "ItemSchemePackageTypeCodelistType":{
         "type":"string",
         "title":"ItemSchemePackageTypeCodelistType",
         "enum":[
            "BASE",
            "CODELIST",
            "CATEGORYSCHEME",
            "CONCEPTSCHEME"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ItemSchemePackageTypeCodelistType.description" bundle="${i18n}"/>"
      },
      "ItemTypeCodelistType":{
         "type":"string",
         "title":"ItemTypeCodelistType",
         "enum":[
            "AGENCY",
            "CATEGORY",
            "CODE",
            "CONCEPT",
            "DATA_CONSUMER",
            "DATA_PROVIDER",
            "ORGANISATION_UNIT",
            "REPORTING_CATEGORY"
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.ItemTypeCodelistType.description" bundle="${i18n}"/>"
      },
      "LocalCodeRefType":{
         "type":"object",
         "title":"LocalCodeRefType",
         "allOf":[
            {
               "properties":{
                  "class":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeRefType.properties.class.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemTypeCodelistType"
                  },
                  "id":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeRefType.properties.id.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "local":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeRefType.properties.local.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "package":{
                     "xml":{
                        "attribute":true,
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeRefType.properties.package.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ItemSchemePackageTypeCodelistType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeRefType.description" bundle="${i18n}"/>"
      },
      "LocalCodeReferenceType":{
         "type":"object",
         "title":"LocalCodeReferenceType",
         "allOf":[
            {
               "properties":{
                  "Ref":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeReferenceType.properties.Ref.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/LocalCodeRefType"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.LocalCodeReferenceType.description" bundle="${i18n}"/>"
      },
      "TextType":{
         "type":"object",
         "title":"TextType",
         "allOf":[
            {
               "properties":{
                  "lang":{
                     "xml":{
                        "attribute":true,
                        "namespace":"http://www.w3.org/XML/1998/namespace"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextType.properties.lang.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "(value)":{
                     "xml":{
                        "namespace":"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.TextType.properties.value.description" bundle="${i18n}"/>",
                     "type":"string"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.TextType.description" bundle="${i18n}"/>"
      },
      "CategoryElements":{
         "type":"object",
         "title":"CategoryElements",
         "allOf":[
            {
               "$ref":"#/definitions/ListBase"
            },
            {
               "properties":{
                  "categoryElement":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElements.properties.categoryElement.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategoryElementResource"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElements.description" bundle="${i18n}"/>"
      },      
      "CategoryElementResource":{
         "type":"object",
         "title":"CategoryElementResource",
         "allOf":[
            {
               "$ref":"#/definitions/Resource"
            },
            {
               "properties":{
                   "validFrom":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElementResource.properties.validFrom.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "validTo":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElementResource.properties.validTo.description" bundle="${i18n}"/>",
                     "type":"string"
                  },
                  "visible":{
                     "xml":{
                        "namespace":""
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElementResource.properties.visible.description" bundle="${i18n}"/>",
                     "type":"boolean"
                  },
                  "logo":{
                     "xml":{
                        "namespace":"http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
                     },
                     "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElementResource.properties.logo.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ResourceLink"
                  }
               }
            }
         ],
         "description":"<fmt:message key="api.doc.swagger.definitions.CategoryElementResource.description" bundle="${i18n}"/>"
      }
   }