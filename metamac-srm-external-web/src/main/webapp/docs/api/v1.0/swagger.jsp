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
   "swagger":"2.0",
   "info":{
      "description":"<fmt:message key="api.doc.swagger.info.description" bundle="${i18n}"/>",
      "version":"1.0",
      "title":"API de recursos estructurales"
   },
   "host":"<%=SwaggerUtils.getApiBaseURLForSwagger()%>",
   "schemes":[

   ],
   "tags":[
      {
         "name":"Organizaciones",
         "description":"<fmt:message key="api.doc.swagger.tags.0.description" bundle="${i18n}"/>"
      },
      {
         "name":"Temas",
         "description":"<fmt:message key="api.doc.swagger.tags.1.description" bundle="${i18n}"/>"
      },
      {
         "name":"Clasificaciones",
         "description":"<fmt:message key="api.doc.swagger.tags.2.description" bundle="${i18n}"/>"
      },
      {
         "name":"Conceptos",
         "description":"<fmt:message key="api.doc.swagger.tags.3.description" bundle="${i18n}"/>"
      },
      {
         "name":"Definiciones de Estructuras de Datos (DSD)",
         "description":"<fmt:message key="api.doc.swagger.tags.4.description" bundle="${i18n}"/>"
      },
      {
         "name":"Recursos auxiliares",
         "description":"<fmt:message key="api.doc.swagger.tags.5.description" bundle="${i18n}"/>"
      }
   ],
   "definitions": <jsp:include page="definitions.jsp" /> ,
   "components":  <jsp:include page="components.jsp" /> 
   "paths":{
      "/v1.0/agencyschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_agencyschemes_findAgencySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AgencySchemes"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_agencyschemes__agencyID__findAgencySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AgencySchemes"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID__findAgencySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AgencySchemes"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID___version__retrieveAgencyScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/AgencyScheme"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.agencies.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID___version__agencies_findAgencies_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"Campos adicionales que se desean mostrar en la respuesta. Los valores pueden ser: <ul><li>+description: muestra la descripción de los códigos</li><li>+annotations: muestra las anotaciones</li></ul> Ejemplo: fields=\"+description,+annotations\""
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.agencies.get.parameters.7.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Agencies"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.agencies.organisationID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID___version__agencies__organisationID__retrieveAgency_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.agencies.organisationID.get.parameters.3.description" bundle="${i18n}"/>"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.agencyschemes.agencyID.resourceID.version.agencies.organisationID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Agency"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categorisations":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categorisations_findCategorisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorisationQueryParam"},
                {"$ref":"#/components/parameters/categorisationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Categorisations"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categorisations/{agencyID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categorisations__agencyID__findCategorisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorisationQueryParam"},
                {"$ref":"#/components/parameters/categorisationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Categorisations"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categorisations/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categorisations__agencyID___resourceID__findCategorisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorisationQueryParam"},
                {"$ref":"#/components/parameters/categorisationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Categorisations"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categorisations/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categorisations__agencyID___resourceID___version__retrieveCategorisation_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categorisations.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Categorisation"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryschemes":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryschemes_findCategorySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorySchemeQueryParam"},
                {"$ref":"#/components/parameters/categorySchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategorySchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryschemes__agencyID__findCategorySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorySchemeQueryParam"},
                {"$ref":"#/components/parameters/categorySchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategorySchemes"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID__findCategorySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorySchemeQueryParam"},
                {"$ref":"#/components/parameters/categorySchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategorySchemes"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID___version__retrieveCategoryScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategoryScheme"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID___version__categories_findCategories_GET",
            "produces":[
               "application/json",
               "application/xml",
               "text/tab-separated-values",
               "text/csv"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categoryQueryParam"},
                {"$ref":"#/components/parameters/categoryOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.get.parameters.7.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Categories"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.categoryID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID___version__categories__categoryID__retrieveCategory_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"categoryID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.categoryID.get.parameters.3.description" bundle="${i18n}"/>"
               },
               {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.categoryID.get.parameters.4.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryschemes.agencyID.resourceID.version.categories.categoryID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Category"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelistfamilies":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelistfamilies_findCodelistFamilies_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {"$ref":"#/components/parameters/limitParam"},
               {"$ref":"#/components/parameters/offsetParam"},
               {
                  "name":"orderBy",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.get.parameters.2.description" bundle="${i18n}"/>"
               },
               {
                  "name":"query",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.get.parameters.3.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CodelistFamilies"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelistfamilies/{id}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.id.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelistfamilies__id__retrieveCodelistFamilyById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.id.get.parameters.0.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelistfamilies.id.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CodelistFamily"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists_findCodelists_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Codelists"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID__findCodelists_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Codelists"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID__findCodelists_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Codelists"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__retrieveCodelist_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Codelist"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}/restrictions":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.restrictions.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__restrictions_findCodelistRestrictions_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.restrictions.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CodelistRestrictions"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}:recode":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__recode_retrieveRecodedCodes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
            {
                  "name":"agencyID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.parameters.0.description" bundle="${i18n}"/>"
            },
            {
                  "name":"resourceID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.parameters.1.description" bundle="${i18n}"/>"
            },
            {
                  "name":"version",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.parameters.2.description" bundle="${i18n}"/>"
            },
            {
                  "name":"referenceAgencyID",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.parameters.3.description" bundle="${i18n}"/>"
            },
            {
                  "name":"referenceResourceID",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.parameters.4.description" bundle="${i18n}"/>"
            },
            {
                  "name":"referenceVersion",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.parameters.5.description" bundle="${i18n}"/>"
            }
            
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.recode.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RecodedCodes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__codes_findCodes_GET",
            "produces":[
               "application/json",
               "application/xml",
               "text/tab-separated-values",
               "text/csv"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistCodesQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
               {
                  "name":"openness",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.parameters.7.description" bundle="${i18n}"/>"
               },
               {
                  "name":"order",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.parameters.8.description" bundle="${i18n}"/>"
               },
                {
                  "name":"restriction",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.parameters.9.description" bundle="${i18n}"/>"
               },
               {
                  "name":"extended",
                  "in":"query",
                  "type":"boolean",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.parameters.10.description" bundle="${i18n}"/>"
               },
              {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.parameters.11.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Codes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes/{codeID}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.codeID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__codes__codeID__retrieveCode_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
             {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"codeID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.codeID.get.parameters.3.description" bundle="${i18n}"/>"
               },
               {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.codeID.get.parameters.4.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.codelists.agencyID.resourceID.version.codes.codeID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Code"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptTypes":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptTypes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptTypes_retrieveConceptTypes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[

            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptTypes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptTypes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptschemes":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptschemes_findConceptSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptSchemasQueryParam"},
                {"$ref":"#/components/parameters/conceptSchemasOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptschemes__agencyID__findConceptSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptSchemasQueryParam"},
                {"$ref":"#/components/parameters/conceptSchemasOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptSchemes"
                  },                 
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID__findConceptSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptSchemasQueryParam"},
                {"$ref":"#/components/parameters/conceptSchemasOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptSchemes"
                  },                 
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID___version__retrieveConceptScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ConceptScheme"
                  },                 
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.concepts.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID___version__concepts_findConcepts_GET",
            "produces":[
               "application/json",
               "application/xml",
               "text/tab-separated-values",
               "text/csv"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptQueryParam"},
                {"$ref":"#/components/parameters/conceptOrderByParam"},
              {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.concepts.get.parameters.7.description" bundle="${i18n}"/>"
              }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.concepts.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Concepts"
                  },                   
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts/{conceptID}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.concepts.conceptID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID___version__concepts__conceptID__retrieveConcept_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {
                  "name":"conceptID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.concepts.conceptID.get.parameters.3.description" bundle="${i18n}"/>"
               },
             
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.conceptschemes.agencyID.resourceID.version.concepts.conceptID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Concept"
                  },                   
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/contentConstraints":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_contentConstraints_findContentConstraints_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/contentConstraintQueryParam"},
                {"$ref":"#/components/parameters/contentConstraintOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ContentConstraints"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_contentConstraints__agencyID__findContentConstraints_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/contentConstraintQueryParam"},
                {"$ref":"#/components/parameters/contentConstraintOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ContentConstraints"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_contentConstraints__agencyID___resourceID__findContentConstraints_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/contentConstraintQueryParam"},
                {"$ref":"#/components/parameters/contentConstraintOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ContentConstraints"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_contentConstraints__agencyID___resourceID___version__retrieveContentConstraint_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/ContentConstraint"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}/regions/{regionCode}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.version.regions.regionCode.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_contentConstraints__agencyID___resourceID___version__regions__regionCode__retrieveRegionForContentConstraint_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
             {
                  "name":"regionCode",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.version.regions.regionCode.get.parameters.0.description" bundle="${i18n}"/>"
               },
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.contentConstraints.agencyID.resourceID.version.regions.regionCode.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/RegionReference"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataconsumerschemes_findDataConsumerSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataConsumerSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID__findDataConsumerSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataConsumerSchemes"
                  },                  
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID__findDataConsumerSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataConsumerSchemes"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__retrieveDataConsumerScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataConsumerScheme"
                  },                  
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.dataconsumers.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__dataconsumers_findDataConsumers_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.dataconsumers.get.parameters.7.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.dataconsumers.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataConsumers"
                  },                 
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.dataconsumers.organisationID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__dataconsumers__organisationID__retrieveDataConsumer_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.dataconsumers.organisationID.get.parameters.3.description" bundle="${i18n}"/>"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataconsumerschemes.agencyID.resourceID.version.dataconsumers.organisationID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataConsumer"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataproviderschemes_findDataProviderSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataProviderSchemes"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID__findDataProviderSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataProviderSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID__findDataProviderSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataProviderSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID___version__retrieveDataProviderScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataProviderScheme"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.dataproviders.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID___version__dataproviders_findDataProviders_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.dataproviders.get.parameters.7.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.dataproviders.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataProviders"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.dataproviders.organisationID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID___version__dataproviders__organisationID__retrieveDataProvider_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.dataproviders.organisationID.get.parameters.3.description" bundle="${i18n}"/>"
               },
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.dataproviderschemes.agencyID.resourceID.version.dataproviders.organisationID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataProvider"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/datastructures":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_datastructures_findDataStructures_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/dataStructuresQueryParam"},
                {"$ref":"#/components/parameters/dataStructuresOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataStructures"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/datastructures/{agencyID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_datastructures__agencyID__findDataStructures_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/dataStructuresQueryParam"},
                {"$ref":"#/components/parameters/dataStructuresOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataStructures"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/datastructures/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_datastructures__agencyID___resourceID__findDataStructures_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/dataStructuresQueryParam"},
                {"$ref":"#/components/parameters/dataStructuresOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataStructures"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/datastructures/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_datastructures__agencyID___resourceID___version__retrieveDataStructure_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.resourceID.version.get.parameters.3.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.datastructures.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/DataStructure"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationschemes_findOrganisationSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationschemes__agencyID__findOrganisationSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationSchemes"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID__findOrganisationSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID___version__retrieveOrganisationScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationScheme"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.organisations.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID___version__organisations_findOrganisations_GET",
            "produces":[
               "application/json",
               "application/xml",
               "text/tab-separated-values",
               "text/csv"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.organisations.get.parameters.7.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.organisations.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Organisations"
                  },                 
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.organisations.organisationID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID___version__organisations__organisationID__retrieveOrganisation_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.organisations.organisationID.get.parameters.3.description" bundle="${i18n}"/>"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationschemes.agencyID.resourceID.version.organisations.organisationID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Organisation"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationunitschemes_findOrganisationUnitSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationUnitSchemes"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID__findOrganisationUnitSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationUnitSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID__findOrganisationUnitSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationUnitSchemes"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID___version__retrieveOrganisationUnitScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationUnitScheme"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.organisationunits.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID___version__organisationunits_findOrganisationUnits_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.organisationunits.get.parameters.7.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.organisationunits.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationUnits"
                  },                      
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.organisationunits.organisationID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID___version__organisationunits__organisationID__retrieveOrganisationUnit_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.organisationunits.organisationID.get.parameters.3.description" bundle="${i18n}"/>"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.organisationunitschemes.agencyID.resourceID.version.organisationunits.organisationID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/OrganisationUnit"
                  },                  
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variablefamilies":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variablefamilies_findVariableFamilies_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableFamilyQueryParam"},
                {"$ref":"#/components/parameters/variableFamilyOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableFamilies"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variablefamilies/{id}":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.id.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variablefamilies__id__retrieveVariableFamilyById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.id.get.parameters.0.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.id.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableFamily"
                  },                 
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variablefamilies/{id}/variables":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.id.variables.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variablefamilies__id__variables_findVariablesByFamily_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.id.variables.get.parameters.0.description" bundle="${i18n}"/>"
               },
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableQueryParam"},
                {"$ref":"#/components/parameters/variableOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variablefamilies.id.variables.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Variables"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variables":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variables_findVariables_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableQueryParam"},
                {"$ref":"#/components/parameters/variableOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Variables"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variables/{id}":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.id.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variables__id__retrieveVariableById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.id.get.parameters.0.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.id.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/Variable"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variables/{variableID}/variableelements":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variables__variableID__variableelements_findVariableElements_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"variableID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.get.parameters.0.description" bundle="${i18n}"/>"
               },
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableElementQueryParam"},
                {"$ref":"#/components/parameters/variableElementOrderByParam"},
                {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.get.parameters.5.description" bundle="${i18n}"/>"
                }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElements"
                  },                
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variables/{variableID}/variableelements/{resourceID}":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variables__variableID__variableelements__resourceID__retrieveVariableElementById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/resourceIdParam"},
               {
                  "name":"variableID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.get.parameters.1.description" bundle="${i18n}"/>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElement"
                  },               
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/variables/{variableID}/variableelements/{resourceID}/geoinfo":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.geoinfo.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_variables__variableID__variableelements__resourceID__geoinfo_findVariableElementsGeoInfoXml_GET",
            "produces":[
               "application/xml",
               "application/json"
            ],
            "parameters":[
                 {
                  "name":"variableID",
                  "in":"path",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.geoinfo.get.parameters.0.description" bundle="${i18n}"/>"
               },
               {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.geoinfo.get.parameters.1.description" bundle="${i18n}"/>"
               },
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableElementQueryParam"},
                {"$ref":"#/components/parameters/variableElementOrderByParam"}
            ],
            "responses":{
               "200":{
                  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.variables.variableID.variableelements.resourceID.geoinfo.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/VariableElementsGeoInfo"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
       "/v1.0/categoryelements":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryelements.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0_categoryElements_findCategoryElements_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categoryElementQueryParam"},
                {"$ref":"#/components/parameters/categoryElementOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryelements.get.responses.200.schema.description" bundle="${i18n}"/>",
                     "$ref":"#/definitions/CategoryElements"
                  },
                  "headers":{

                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      "/v1.0/categoryelements/{resourceID}":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryelements.resourceID.get.description" bundle="${i18n}"/>",
             "operationId":"resource__v1.0__categoryelements__resourceID_retrieveCategoryElement_GET",
           "produces":[
               "application/json",
               "application/xml"
            ],
             "parameters":[
                {"$ref":"#/components/parameters/resourceIdParam"}
            ],
            "responses":{
               "200":{
                  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryelements.resourceID.get.responses.200.schema.description" bundle="${i18n}"/>",
                  },
                  "headers":{
                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
       "/v1.0/categoryelements/{resourceID}/logo":{
         "get":{
            "tags":[
              "Recursos auxiliares"
            ],
            "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryelements.resourceID.logo.get.description" bundle="${i18n}"/>",
            "operationId":"resource__v1.0__categoryelements__resourceID__logo_retrieveCategoryElementLogo_GET",
            "produces":[
                "image/*"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/resourceIdParam"}
            ],
            "responses":{
               "200":{
                  "schema":{
                     "description":"<fmt:message key="api.doc.swagger.paths.v1_0.categoryelements.resourceID.logo.get.responses.200.schema.description" bundle="${i18n}"/>",
                  },
                  "headers":{
                  },
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.200" bundle="${i18n}"/>"
               },
               "406":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.406" bundle="${i18n}"/>"
               },
               "500":{
                  "description":"<fmt:message key="api.doc.swagger.paths.responses.500" bundle="${i18n}"/>"
               },
               "503":{
                   "description":"<fmt:message key="api.doc.swagger.paths.responses.503" bundle="${i18n}"/>"
               }
            }
         }
      },
      
   }
}