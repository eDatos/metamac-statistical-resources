###
# Dockerfile usado para consutruir la imagen del statistical-resources-internal
#
# Antes de construir la imagen debemos haber compilado previamente el proyecto (desde el directorio raíz):
#
# mvn clean package
#
# A continuación podremos construir la imagen ejecutando siempre desde el directorio raíz del proyecto:
#
# docker build -f dockerfiles/statistical-resources-internal/Dockerfile -t statistical-resources-internal:<version> .
#
# Para ejecutar un contenedor asociado a esta imagen:
# docker run -i --rm -p 8080:8080 statistical-resources-internal:<version>
#
# Para ejecutar con opciones de debug habilitadas:
# docker run -i --rm \
# -e JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" \
# -p 8080:8080 \
# -p 5005:5005 \
# statistical-resources-internal:<version>
###
FROM tomcat:9.0.98-jdk8
LABEL MAINTAINER=info@arte-consultores.com

RUN mkdir -p /data/edatos/statistical-resources/conf

COPY dockerfiles/statistical-resources-internal/conf/*.xml /data/edatos/statistical-resources/conf/
COPY metamac-statistical-resources-web/target/*.war /usr/local/tomcat/webapps/statistical-resources-internal.war

ENV LANG='es_ES.UTF-8'
ENV STATISTICAL_RESOURCES_CONF_PATH='file:/data/edatos/statistical-resources/conf/environment.xml'

RUN env

EXPOSE 8080

CMD ["catalina.sh", "run"]