###
# Dockerfile usado para consutruir la imagen del srm-external
#
# Antes de construir la imagen debemos haber compilado previamente el proyecto (desde el directorio raíz):
#
# mvn clean package
#
# A continuación podremos construir la imagen ejecutando siempre desde el directorio raíz del proyecto:
#
# docker build -f dockerfiles/srm-external/Dockerfile -t structural-resources-external:<version> .
#
# Para ejecutar un contenedor asociado a esta imagen:
# docker run -i --rm -p 8080:8080 structural-resources-external:<version>
#
# Para ejecutar con opciones de debug habilitadas:
# docker run -i --rm \
# -e JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" \
# -p 8080:8080 \
# -p 5005:5005 \
# structural-resources-external:<version>
###

FROM tomcat:9.0.95-jdk8
LABEL MAINTAINER=info@arte-consultores.com

RUN mkdir -p /data/edatos/srm/conf

COPY dockerfiles/srm-external/conf/*.xml /data/edatos/srm/conf/
COPY metamac-srm-external-web/target/*.war /usr/local/tomcat/webapps/structural-resources.war

ENV LANG='es_ES.UTF-8'
ENV SRMCONF_PATH='file:/data/edatos/srm/conf/environment.xml'

RUN env

CMD ["catalina.sh", "run"]

